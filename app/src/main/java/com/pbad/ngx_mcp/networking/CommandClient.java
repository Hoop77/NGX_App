package com.pbad.ngx_mcp.networking;

import android.support.annotation.Nullable;
import android.util.Log;

import com.pbad.ngx_mcp.global.EventId;
import com.pbad.ngx_mcp.networking.Protocol.AllValuesRequestPacket;
import com.pbad.ngx_mcp.networking.Protocol.DataPacket;
import com.pbad.ngx_mcp.networking.Protocol.EventPacket;
import com.pbad.ngx_mcp.networking.Protocol.Packet;
import com.pbad.ngx_mcp.networking.Protocol.PacketIO;
import com.pbad.ngx_mcp.networking.Protocol.ProtocolException;
import com.pbad.ngx_mcp.networking.Protocol.RequestPacket;
import com.pbad.ngx_mcp.networking.Protocol.Response;
import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by phili on 07.11.2016.
 */

public class CommandClient implements Runnable
{
    private InetAddress serverAddress;
    private int port;
    private Connection connection;
    private Socket socket;

    private boolean running = false;
    private Thread thread;

    private BlockingQueue<Packet> packetQueue = new LinkedBlockingQueue<>( 100 );

    private OnDataReceivedListener onDataReceivedListener;

    public CommandClient( InetAddress serverAddress, int port, Connection connection )
    {
        this.serverAddress = serverAddress;
        this.port = port;
        this.connection = connection;

        socket = new Socket();

        thread = new Thread( this );
    }

    public void start()
    {
        thread.start();
    }

    public void stop()
    {
        running = false;
        close();

        try
        {
            thread.join();
        }
        catch( InterruptedException e )
        {
            Thread.currentThread().interrupt();
            Log.d( "CommandClient", "stop(): join() interrupted!" );
        }
    }

    @Override
    public void run()
    {
        // We'll see if we're really connected when we send a packet.
        connection.setState( Connection.State.CONNECTED );

        Packet packet = null;
        boolean retry = false;

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
                retry = false;
            }
            catch( InterruptedException e )
            {
                Thread.currentThread().interrupt();
                Log.d( "CommandClient", "run(): poll() interrupted!" );
            }
            catch( IOException e )
            {
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
            PacketIO.write( packet, socket.getOutputStream() );

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

    private synchronized void connect() throws IOException
    {
        // Throw IOException to signal that we're not connected.
        if( !running )
            throw new IOException();

        socket = new Socket();
        socket.connect( new InetSocketAddress( serverAddress, port ), 5000 );
        connection.setState( Connection.State.CONNECTED );
    }

    private synchronized void close()
    {
        connection.setState( Connection.State.DISCONNECTED );

        try
        {
            socket.close();
        }
        catch( IOException e )
        {
            // Nothing to handle - we will use a new socket when reconnecting anyway.
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
        Packet packet = PacketIO.read( socket.getInputStream() );

        if( packet instanceof DataPacket )
            return (DataPacket) packet;

        return null;
    }

    public boolean requestAllValues( int entityId )
    {
        return packetQueue.offer( new AllValuesRequestPacket( entityId ) );
    }

    public boolean sendEvent( int entityId, EventId eventId, int eventParameter )
    {
        return packetQueue.offer( new EventPacket( entityId, entityId, eventParameter ) );
    }

    public void setOnDataReceivedListener( OnDataReceivedListener onDataReceivedListener )
    {
        this.onDataReceivedListener = onDataReceivedListener;
    }
}
