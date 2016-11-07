package com.example.phili.spielwiese.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.phili.spielwiese.ValueSetter;
import com.example.phili.spielwiese.networking.connectionStateManaging.Connection;
import com.example.phili.spielwiese.networking.connectionStateManaging.ConnectionState;
import com.example.phili.spielwiese.networking.connectionStateManaging.ConnectionStateManager;
import com.example.phili.spielwiese.networking.protocol.packet.receivePacket.dataPacket.DataPacket;
import com.example.phili.spielwiese.networking.protocol.packet.receivePacket.dataPacket.SingleValueDataPacket;
import com.example.phili.spielwiese.networking.protocol.PacketFactory;

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
    private ValueSetter valueSetter;

    private static final int RESPONSE_OK = 1;

    public NotificationClient( InetAddress serverAddress, int port, Connection connection, Activity activity )
    {
        this.serverAddress= serverAddress;
        this.port = port;
        this.connection = connection;

        thread = new Thread( this );
        valueSetter = new ValueSetter( activity );
    }

    public void restart( InetAddress serverAddress, int port )
    {
        stop();

        this.serverAddress = serverAddress;
        this.port = port;

        start();
    }

    public void start()
    {
        thread.start();
    }

    @Override
    public void run()
    {
        Socket client = null;
        byte[] receivedData = new byte[ 1024 ];

        boolean connected = false;

        running = true;
        while( running )
        {
            if( !connected )
            {
                client = tryConnect();
                if( client == null )
                    continue;
            }

            connected = receiveData( client, receivedData );
            if( !connected )
                continue;

            connected = respond( client );

            interpretData( receivedData );
        }
    }

    private Socket tryConnect()
    {
        connection.setState( ConnectionState.CONNECTING );

        try
        {
            Socket client = new Socket();
            client.connect( new InetSocketAddress( serverAddress, port ), 10000 );
            connection.setState( ConnectionState.CONNECTED );
            return client;
        }
        catch( IOException e )
        {
            connection.setState( ConnectionState.DISCONNECTED );
        }

        return null;
    }

    private boolean receiveData( Socket client, byte[] buffer )
    {
        boolean connected = true;

        try
        {
            int bytesRead = client.getInputStream().read( buffer );
            // check for active connection
            if( bytesRead < 0 )
            {
                connected = false;
            }
        }
        catch( IOException e )
        {
            connected = false;
            connection.setState( ConnectionState.DISCONNECTED );
        }

        return connected;
    }

    private boolean respond( Socket client )
    {
        try
        {
            client.getOutputStream().write( RESPONSE_OK );
            return true;
        }
        catch (IOException e)
        {
            connection.setState( ConnectionState.DISCONNECTED );
        }

        return false;
    }

    private void interpretData( byte[] receivedData )
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

    public void stop()
    {
        if( !running )
            return;

        running = false;
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
