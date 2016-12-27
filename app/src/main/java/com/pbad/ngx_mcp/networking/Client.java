package com.pbad.ngx_mcp.networking;

import android.util.Log;

import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by philipp on 08.12.16.
 */

public abstract class Client implements Runnable
{
    protected InetAddress serverAddress;
    protected int port;
    protected Connection connection;
    protected Socket socket = new Socket();

    protected volatile boolean running = false;
    protected Thread thread;

    protected OnDataReceivedListener onDataReceivedListener;

    public Client( InetAddress serverAddress, int port, Connection connection )
    {
        this.serverAddress = serverAddress;
        this.port = port;
        this.connection = connection;
    }

    public void start()
    {
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
            Log.d( "CommandClient", "stop(): join() interrupted!" );
        }
    }

    public void restart( InetAddress serverAddress, int port )
    {
        stop();
        this.serverAddress = serverAddress;
        this.port = port;
        start();
    }

    public void setOnDataReceivedListener( OnDataReceivedListener onDataReceivedListener )
    {
        this.onDataReceivedListener = onDataReceivedListener;
    }

    public Connection getConnection()
    {
        return connection;
    }

    protected synchronized void connect() throws IOException
    {
        // Throw IOException to signal that we're not connected.
        if( !running )
            throw new IOException();

        socket = new Socket();
        // Turn off nagle's algorithm - we're only transferring very small packets.
        // Since this will only affect the local network,
        // we don't need to panic about the internet being collapsed by this.
        socket.setTcpNoDelay( true );
        socket.connect( new InetSocketAddress( serverAddress, port ), 5000 );
        connection.setState( Connection.State.CONNECTED );
    }

    protected synchronized void close()
    {
        try
        {
            socket.close();
        }
        catch( IOException e )
        {
            // Nothing to handle - we will use a new socket when reconnecting anyway.
        }
    }
}
