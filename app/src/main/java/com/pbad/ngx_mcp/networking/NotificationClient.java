package com.pbad.ngx_mcp.networking;

import android.util.Log;

import com.pbad.ngx_mcp.global.SingleValue;
import com.pbad.ngx_mcp.networking.Protocol.Packet;
import com.pbad.ngx_mcp.networking.Protocol.PacketReader;
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

public class NotificationClient extends Client
{
    public NotificationClient( InetAddress serverAddress, int port, Connection connection )
    {
        super( serverAddress, port, connection );
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

    private Packet receive() throws IOException, ProtocolException
    {
        return new PacketReader( socket.getInputStream() ).read();
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
}
