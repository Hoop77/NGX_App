package com.pbad.ngx_mcp.networking;

import android.app.Activity;
import android.util.Log;

import com.pbad.ngx_mcp.ValueSetter;
import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;
import com.pbad.ngx_mcp.networking.protocol.packet.Packet;
import com.pbad.ngx_mcp.networking.protocol.packet.receivePacket.dataPacket.DataPacket;
import com.pbad.ngx_mcp.networking.protocol.packet.receivePacket.dataPacket.SingleValueDataPacket;
import com.pbad.ngx_mcp.networking.protocol.PacketFactory;

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

    private boolean running = false;
    private Thread thread;
    private Socket client;
    private byte[] receivedData;
    private ValueSetter valueSetter;

    private static final int RESPONSE_OK = 1;

    public NotificationClient( InetAddress serverAddress, int port, Connection connection, Activity activity )
    {
        this.serverAddress= serverAddress;
        this.port = port;
        this.connection = connection;

        client = new Socket();
        receivedData = new byte[ Packet.MAX_SIZE ];
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
        close();

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
            if( tryConnect() && receiveData() && respond() )
            {
                interpretData();
            }
        }

        close();
    }

    private boolean tryConnect()
    {
        if( connection.getState() == Connection.State.CONNECTED )
            return true;

        try
        {
            close();
            client = new Socket();
            client.connect( new InetSocketAddress( serverAddress, port ) );
            connection.setState( Connection.State.CONNECTED );
            return true;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }

        return false;
    }

    private boolean receiveData()
    {
        try
        {
            client.getInputStream().read( receivedData );
            return true;
        }
        catch( IOException e )
        {
            connection.setState( Connection.State.DISCONNECTED );
        }

        return false;
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

    private void interpretData()
    {
        try
        {
            DataPacket packet = PacketFactory.createPacketFromReceivedData( receivedData );
            if( packet instanceof SingleValueDataPacket )
            {
                // get the single value from the packet and push it to the UI
                SingleValueDataPacket singleValueDataPacket = (SingleValueDataPacket) packet;
                valueSetter.setValue( singleValueDataPacket.getSingleValue() );
            }
        }
        catch( InsufficientBufferSizeException e )
        {
            Log.d( "NotificationClient", "Invalid data received!" );
        }
    }

    private void close()
    {
        try
        {
            client.close();
        }
        catch( IOException e ) {}
    }
}
