package com.pbad.ngx_mcp.networking.protocol.packet.receivePacket.dataPacket;

import com.pbad.ngx_mcp.global.Global;
import com.pbad.ngx_mcp.networking.InsufficientBufferSizeException;

import java.nio.ByteBuffer;

/**
 * Created by phili on 04.11.2016.
 */

public class AllValuesDataPacket extends DataPacket
{
    // values start position (variable size)
    public static final int BYTE_POS_ALL_VALUES = 3;

    // data packet min size (nothing more)
    public static final int MIN_SIZE = DataPacket.MIN_SIZE;

    // max value count
    public static final int MAX_VALUE_COUNT = (MAX_SIZE - MIN_SIZE) / 4;

    public AllValuesDataPacket( byte[] data ) throws InsufficientBufferSizeException
    {
        super();

        // The size of this packet depends on the value count of the entity.
        int packetSize = getPacketSize( data );
        if( data.length < packetSize )
        {
            throw new InsufficientBufferSizeException();
        }

        this.data = new byte[ packetSize ];
        // copy bytes
        for( int i = 0; i < packetSize; i++ )
        {
            this.data[ i ] = data[ i ];
        }
    }

    private int getPacketSize(byte[] data ) throws InsufficientBufferSizeException
    {
        // First, check if we can at least get the entity-ID.
        if( data.length < MIN_SIZE )
        {
            throw new InsufficientBufferSizeException();
        }

        int entityId = getEntityId( data );
        int valueCount = Global.getValueIdCountFromEntityId( entityId );
        return MIN_SIZE + valueCount * 4;
    }

    public int[] getValues()
    {
        int entityId = getEntityId();
        int valueCount = Global.getValueIdCountFromEntityId( entityId );
        int[] values = new int[ valueCount ];
        for( int i = 0; i < valueCount; i++ )
        {
            int offset = i * 4 + BYTE_POS_ALL_VALUES;
            values[ i ] = ByteBuffer.wrap( data, offset, 4 ).getInt();
        }

        return values;
    }
}
