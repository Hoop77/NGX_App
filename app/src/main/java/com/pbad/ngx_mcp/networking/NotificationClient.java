package com.pbad.ngx_mcp.networking;

import android.util.Log;

import com.pbad.ngx_mcp.global.SingleValue;
import com.pbad.ngx_mcp.networking.Protocol.Packet;
import com.pbad.ngx_mcp.networking.Protocol.PacketIO;
import com.pbad.ngx_mcp.networking.Protocol.ProtocolException;
import com.pbad.ngx_mcp.networking.Protocol.Response;
import com.pbad.ngx_mcp.networking.Protocol.SingleValueDataPacket;
import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by phili on 04.11.2016.
 */

public class NotificationClient implements Runnable
{
    private InetAddress serverAddress;
    private int port;
    private Connection connection;

    private volatile boolean running = false;
    private Thread thread;
    private Socket socket;
    private OnDataReceivedListener onDataReceivedListener;

    public NotificationClient( InetAddress serverAddress, int port, Connection connection )
    {
        this.serverAddress= serverAddress;
        this.port = port;
        this.connection = connection;

        socket = new Socket();
    }

    public void setServerAddress( InetAddress serverAddress )
    {
        this.serverAddress = serverAddress;
    }

    private void setPort( int port )
    {
        this.port = port;
    }

    public void start()
    {
        if( running )
            return;

        thread = new Thread( this );
        thread.start();
    }

    public void stop()
    {
        if( !running )
            return;

        running = false;
        close();

        try
        {
            thread.join();
        }
        catch( InterruptedException e )
        {
            Thread.currentThread().interrupt();
            Log.d( "NotificationClient", "stop(): join() interrupted!" );
        }
    }

    @Override
    public void run()
    {
        connection.setState( Connection.State.DISCONNECTED );

        running = true;
        while( running )
        {
            try
            {
                if( connection.getState() == Connection.State.DISCONNECTED )
                    connect();

                Packet packet = receive();
                respond();
                interpret( packet );
            }
            catch( IOException e )
            {
                close();
            }
            catch( ProtocolException e )
            {
                // Since this would mean a bug in the Protocol or someone spams us,
                // we leave this unhandled and return null.
                Log.d( "NotificationClient", "receive(): Invalid packet received!" );
            }
        }

        close();
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

    private Packet receive() throws IOException, ProtocolException
    {
        return PacketIO.read( socket.getInputStream() );
    }

    private void respond() throws IOException
    {
        socket.getOutputStream().write( Response.OK.toInt() );
    }

    private void interpret( Packet packet )
    {
        if( packet instanceof SingleValueDataPacket )
        {
            // get the single value from the packet and push it to the UI
            SingleValueDataPacket singleValueDataPacket = (SingleValueDataPacket) packet;
            SingleValue singleValue = new SingleValue(
                    singleValueDataPacket.getValueId(), singleValueDataPacket.getValue() );
            if( onDataReceivedListener != null )
                onDataReceivedListener.onDataReceived( singleValueDataPacket );
        }
    }

    public void setOnDataReceivedListener( OnDataReceivedListener onDataReceivedListener )
    {
        this.onDataReceivedListener = onDataReceivedListener;
    }
}
