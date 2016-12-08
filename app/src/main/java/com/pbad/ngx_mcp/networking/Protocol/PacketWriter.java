package com.pbad.ngx_mcp.networking.Protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by phili on 07.12.2016.
 */

public class PacketWriter
{
    private final OutputStream outputStream;

    private byte[] raw;
    int len;

    public PacketWriter( OutputStream outputStream )
    {
        this.outputStream = outputStream;
    }

    public void write( Packet packet ) throws ProtocolException, IOException
    {
        raw = new byte[ Packet.MAX_SIZE ];
        len = 0;

        if( packet instanceof EventPacket )
        {
            getRawEventPacket( (EventPacket) packet );
        }
        else if( packet instanceof RequestPacket
                && packet instanceof SingleValueRequestPacket )
        {
            getRawSingleValueRequestPacket( (SingleValueRequestPacket) packet );
        }
        else if( packet instanceof  RequestPacket
                && packet instanceof AllValuesRequestPacket )
        {
            getRawAllValuesRequestPacket( (AllValuesRequestPacket) packet );
        }
        else
        {
            throw new ProtocolException();
        }

        outputStream.write( raw );
    }

    private void getRawEventPacket( EventPacket eventPacket )
    {
        raw[ 0 ] = (byte) (eventPacket.getPacketType().toInt() & 0x000000ff);
        raw[ 1 ] = (byte) (eventPacket.getEntityId() & 0x000000ff);
        raw[ 2 ] = (byte) (eventPacket.getEventId() & 0x000000ff);
        raw[ 3 ] = (byte) ((eventPacket.getEventId() & 0x0000ff00) >> 8);
        raw[ 4 ] = (byte) ((eventPacket.getEventId() & 0x00ff0000) >> 16);
        raw[ 5 ] = (byte) ((eventPacket.getEventId() & 0xff000000) >> 24);
        raw[ 6 ] = (byte) (eventPacket.getEventParameter() & 0x000000ff);
        raw[ 7 ] = (byte) ((eventPacket.getEventParameter() & 0x0000ff00) >> 8);
        raw[ 8 ] = (byte) ((eventPacket.getEventParameter() & 0x00ff0000) >> 16);
        raw[ 9 ] = (byte) ((eventPacket.getEventParameter() & 0xff000000) >> 24);
    }

    private void getRawSingleValueRequestPacket( SingleValueRequestPacket singleValueRequestPacket )
    {
        raw[ 0 ] = (byte) (singleValueRequestPacket.getPacketType().toInt() & 0x000000ff);
        raw[ 1 ] = (byte) (singleValueRequestPacket.getEntityId() & 0x000000ff);
        raw[ 2 ] = (byte) (singleValueRequestPacket.getRequestType().toInt() & 0x000000ff);
        raw[ 3 ] = (byte) (singleValueRequestPacket.getValueId() & 0x000000ff);
        raw[ 4 ] = (byte) ((singleValueRequestPacket.getValueId() & 0x0000ff00) >> 8);
    }

    private void getRawAllValuesRequestPacket( AllValuesRequestPacket allValuesRequestPacket )
    {
        raw[ 0 ] = (byte) (allValuesRequestPacket.getPacketType().toInt() & 0x000000ff);
        raw[ 1 ] = (byte) (allValuesRequestPacket.getEntityId() & 0x000000ff);
        raw[ 2 ] = (byte) (allValuesRequestPacket.getRequestType().toInt() & 0x000000ff);
    }
}
