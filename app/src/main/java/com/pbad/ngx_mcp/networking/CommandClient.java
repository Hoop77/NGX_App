package com.pbad.ngx_mcp.networking;

import android.support.annotation.Nullable;
import android.util.Log;

import com.pbad.ngx_mcp.EventId;
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
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by phili on 07.11.2016.
 */

public class CommandClient implements Runnable
{
    private InetAddress serverAddress;
    private int port;
    private Connection connection;

    private boolean running = false;
    private Thread thread;

    private BlockingDeque<Packet> packetQueue = new LinkedBlockingDeque<>();

    private OnDataReceivedListener onDataReceivedListener;

    public CommandClient( InetAddress serverAddress, int port, Connection connection )
    {
        this.serverAddress = serverAddress;
        this.port = port;
        this.connection = connection;

        thread = new Thread( this );
    }

    public void start()
    {
        thread.start();
    }

    public void stop()
    {
        running = false;
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
            try
            {
                if( !retry )
                {
                    packet = packetQueue.poll( 1000, TimeUnit.MILLISECONDS );
                    if( packet == null )
                        continue;
                }

                boolean success = handlePacketIO( packet );
                // only retry when disconnected
                if( success || connection.getState() == Connection.State.CONNECTED )
                    retry = false;
                else
                    retry = true;
            }
            catch( InterruptedException e )
            {
                Thread.currentThread().interrupt();
                Log.d( "CommandClient", "run(): packetQueue.poll() interrupted!" );
            }
        }
    }

    private boolean handlePacketIO( Packet packet )
    {
        Socket socket = connect();
        if( socket == null )
            return false;

        boolean connected = sendPacket( packet, socket );
        if( !connected )
            return false;

        if( packet instanceof EventPacket )
        {
            connected = receiveResponse( socket );
            if( !connected )
                return false;
        }
        else if( packet instanceof RequestPacket )
        {
            DataPacket requestedDataPacket = receiveRequest( socket );
            if( requestedDataPacket == null )
                return false;

            if( onDataReceivedListener != null)
                onDataReceivedListener.onDataReceived( requestedDataPacket );
        }

        close( socket );

        return true;
    }

    @Nullable
    private Socket connect()
    {
        try
        {
            Socket socket = new Socket();
            socket.connect( new InetSocketAddress( serverAddress, port ), 1000 );
            connection.setState( Connection.State.CONNECTED );
            return socket;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }

        return null;
    }

    private boolean sendPacket( Packet packet, Socket socket )
    {
        try
        {
            PacketIO.write( packet, socket.getOutputStream() );
            return true;
        }
        catch( ProtocolException e )
        {
            Log.d( "CommandClient", "sendPacket(): Invalid packet!" );
            // TODO: How should we handle invalid packets?
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }

        return false;
    }

    private boolean receiveResponse( Socket socket )
    {
        try
        {
            int response = socket.getInputStream().read();
            if( response == Response.OK.toInt() )
                return true;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }

        return false;
    }

    private DataPacket receiveRequest( Socket socket )
    {
        try
        {
            Packet packet = PacketIO.read( socket.getInputStream() );
            if( packet instanceof DataPacket )
                return (DataPacket) packet;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }
        catch( ProtocolException e )
        {
            Log.d( "CommandClient", "receiveRequest(): Invalid packet received!" );
            // TODO: How should we handle invalid packets?
        }

        return null;
    }

    private void close( Socket socket )
    {
        try
        {
            socket.close();
        }
        catch( IOException e ) {}
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
