package com.pbad.ngx_mcp.networking;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pbad.ngx_mcp.SingleValue;
import com.pbad.ngx_mcp.ValueSetter;
import com.pbad.ngx_mcp.networking.Protocol.Packet;
import com.pbad.ngx_mcp.networking.Protocol.PacketIO;
import com.pbad.ngx_mcp.networking.Protocol.ProtocolException;
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
    private Socket client;
    private ValueSetter valueSetter;

    private static final byte RESPONSE_OK = 1;

    public NotificationClient( InetAddress serverAddress, int port, Connection connection, Activity activity )
    {
        this.serverAddress= serverAddress;
        this.port = port;
        this.connection = connection;

        client = new Socket();
        valueSetter = new ValueSetter( activity );
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
        closeSocket();

        try
        {
            thread.join();
        }
        catch( InterruptedException e )
        {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run()
    {
        running = true;
        while( running )
        {
            if( connection.getState() == Connection.State.DISCONNECTED )
            {
                tryConnect();
                continue;
            }

            Packet packet = receive();
            if( packet == null )
                continue;

            if( !respond() )
                continue;

            interpret( packet );
        }

        closeSocket();
    }

    private void tryConnect()
    {
        try
        {
            closeSocket();
            newSocket();
            // We need the next check for proper multithreading:
            // If "stop()" gets called (from a different thread), we set running to false and closeSocket the socket.
            // But if this closeSocket operation is called before "newSocket()" gets called, we would still have
            // a valid socket to connect. If running is false, we prevent using this 'valid' socket from connecting.
            if( running )
            {
                client.connect( new InetSocketAddress( serverAddress, port ), 5000 );
                connection.setState( Connection.State.CONNECTED );
            }
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }
    }

    @Nullable
    private Packet receive()
    {
        try
        {
            Packet packet = PacketIO.read( client.getInputStream() );
            return packet;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }
        catch( ProtocolException e )
        {
            Log.d( "NotificationClient", "Invalid packet received!" );
            // TODO: How should we handle invalid packets?
        }

        return null;
    }

    private boolean respond()
    {
        try
        {
            client.getOutputStream().write( RESPONSE_OK );
            return true;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }

        return false;
    }

    private void interpret( Packet packet )
    {
        if( packet instanceof SingleValueDataPacket )
        {
            // get the single value from the packet and push it to the UI
            SingleValueDataPacket singleValueDataPacket = (SingleValueDataPacket) packet;
            SingleValue singleValue = new SingleValue(
                    singleValueDataPacket.getValueId(), singleValueDataPacket.getValue() );
            valueSetter.setValue( singleValue );
        }
    }

    // newSocket() and closeSocket() need to be synchronized because another thread may call stop()
    // which closes the socket which should be done on an completely initialized socket.
    private synchronized void newSocket()
    {
        client = new Socket();
    }

    private synchronized void closeSocket()
    {
        try
        {
            client.close();
        }
        catch( IOException e ) {}
    }
}
