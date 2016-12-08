package com.pbad.ngx_mcp.networking.Protocol;

import com.pbad.ngx_mcp.global.Global;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by phili on 07.12.2016.
 */

public class PacketReader
{
    private final DataInputStream dataInputStream;

    private byte[] buf;
    private int pos;

    public PacketReader( InputStream inputStream )
    {
        dataInputStream = new DataInputStream( inputStream );
    }

    public Packet read() throws IOException, ProtocolException
    {
        buf = new byte[ Packet.MAX_SIZE ];
        pos = 0;

        return readPacket();
    }

    private void readNextBytes( int len ) throws IOException
    {
        dataInputStream.readFully( buf, pos, len );
        pos += len;
    }

    private Packet readPacket() throws ProtocolException, IOException
    {
        readNextBytes( 2 );
        int packetType = (int) buf[ 0 ];
        int entityId = (int) buf[ 1 ];

        if( packetType == PacketType.DATA.toInt() )
            return readDataPacket( entityId );
        else
            throw new ProtocolException();
    }

    private Packet readDataPacket( int entityId ) throws IOException, ProtocolException
    {
        readNextBytes( 1 );
        int requestType = (int) buf[ 2 ];

        if( requestType == RequestType.SINGLE_VALUE.toInt() )
            return readSingleValueDataPacket( entityId );
        else if( requestType == RequestType.ALL_VALUES.toInt() )
            return readAllValuesDataPacket( entityId );
        else
            throw new ProtocolException();
    }

    private Packet readSingleValueDataPacket( int entityId ) throws IOException
    {
        readNextBytes( 6 );

        int valueId = (((int) buf[ 3 ]) & 0x000000ff)
                | ((((int) buf[ 4 ]) & 0x000000ff) << 8);

        int value = (((int) buf[ 5 ]) & 0x000000ff)
                | ((((int) buf[ 6 ]) & 0x000000ff) << 8)
                | ((((int) buf[ 7 ]) & 0x000000ff) << 16)
                | ((((int) buf[ 8 ]) & 0x000000ff) << 24);

        return new SingleValueDataPacket( entityId, valueId, value );
    }

    private Packet readAllValuesDataPacket( int entityId )
    {
        int valueCount = Global.getValueIdCountFromEntityId( entityId );
        int[] values = new int[ valueCount ];

        for( int i = 0; i < valueCount; i++ )
        {
            int offset = 3 + i * 4;
            values[ i ] = (((int) buf[ offset ]) & 0x000000ff)
                    | ((((int) buf[ offset + 1 ]) & 0x000000ff) << 8)
                    | ((((int) buf[ offset + 2 ]) & 0x000000ff) << 16)
                    | ((((int) buf[ offset + 3 ]) & 0x000000ff) << 24);
        }

        return new AllValuesDataPacket( entityId, values );
    }
}
