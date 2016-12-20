package com.pbad.ngx_mcp.networking;

import android.support.annotation.Nullable;
import android.util.Log;

import com.pbad.ngx_mcp.networking.Protocol.AllValuesRequestPacket;
import com.pbad.ngx_mcp.networking.Protocol.DataPacket;
import com.pbad.ngx_mcp.networking.Protocol.EventPacket;
import com.pbad.ngx_mcp.networking.Protocol.Packet;
import com.pbad.ngx_mcp.networking.Protocol.PacketReader;
import com.pbad.ngx_mcp.networking.Protocol.PacketWriter;
import com.pbad.ngx_mcp.networking.Protocol.ProtocolException;
import com.pbad.ngx_mcp.networking.Protocol.RequestPacket;
import com.pbad.ngx_mcp.networking.Protocol.Response;
import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by phili on 07.11.2016.
 */

public class CommandClient extends Client
{
    private BlockingQueue<Packet> packetQueue = new LinkedBlockingQueue<>( 100 );

    public CommandClient( InetAddress serverAddress, int port, Connection connection )
    {
        super( serverAddress, port, connection );
    }

    @Override
    public void run()
    {
        // We'll see if we're really connected when we send a packet.
        connection.setState( Connection.State.CONNECTED );

        Packet packet = null;
        boolean retry = false;

        running = true;
        while( running )
        {
            // Polling packets from the queue and send them to the server (eventually receive requested data packets).
            // If connection fails (IOException is thrown), keep trying to send the last polled packet.
            // If a ProtocolException is thrown, discard the packet - there must be a bug in this case or someone spams us
            // so we prevent the queue from overflowing.
            try
            {
                if( !retry )
                {
                    packet = packetQueue.poll( 1000, TimeUnit.MILLISECONDS );
                    if( packet == null )
                        continue;
                }

                handlePacketIO( packet );
                connection.setState( Connection.State.CONNECTED );
                retry = false;
            }
            catch( InterruptedException e )
            {
                Thread.currentThread().interrupt();
                Log.d( "CommandClient", "run(): poll() interrupted!" );
            }
            catch( IOException e )
            {
                connection.setState( Connection.State.DISCONNECTED );
                retry = true;
            }
            catch( ProtocolException e )
            {
                retry = false;
            }
        }
    }

    private void handlePacketIO( Packet packet ) throws IOException, ProtocolException
    {
        try
        {
            connect();
            new PacketWriter( socket.getOutputStream() ).write( packet );

            if( packet instanceof EventPacket )
            {
                receiveResponse();
            }
            else if( packet instanceof RequestPacket )
            {
                DataPacket requestedDataPacket = receiveRequest();
                if( requestedDataPacket != null
                    && onDataReceivedListener != null )
                {
                    onDataReceivedListener.onDataReceived( requestedDataPacket );
                }
            }
        }
        catch( IOException e )
        {
            throw e;
        }
        catch( ProtocolException e )
        {
            throw e;
        }
        finally
        {
            close();
        }
    }

    private void receiveResponse() throws IOException, ProtocolException
    {
        int response = socket.getInputStream().read();
        if( response == Response.OK.toInt() )
            throw new ProtocolException();
    }

    @Nullable
    private DataPacket receiveRequest() throws IOException, ProtocolException
    {
        Packet packet = new PacketReader( socket.getInputStream() ).read();

        if( packet instanceof DataPacket )
            return (DataPacket) packet;

        return null;
    }

    public boolean requestAllValues( int entityId )
    {
        return packetQueue.offer( new AllValuesRequestPacket( entityId ) );
    }

    public boolean sendEvent( int entityId, int eventId, int eventParameter )
    {
        return packetQueue.offer( new EventPacket( entityId, eventId, eventParameter ) );
    }
}
