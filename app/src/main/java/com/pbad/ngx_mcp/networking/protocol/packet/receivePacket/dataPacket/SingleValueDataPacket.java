package com.pbad.ngx_mcp.networking.protocol.packet.receivePacket.dataPacket;

import com.pbad.ngx_mcp.SingleValue;
import com.pbad.ngx_mcp.networking.InsufficientBufferSizeException;

/**
 * Created by phili on 04.11.2016.
 */

public class SingleValueDataPacket extends DataPacket
{
    // value-ID (16 bit)
    public static final int BYTE_POS_VALUE_ID_LOW = 3;
    public static final int BYTE_POS_VALUE_ID_HIGH = 4;

    // value (32 bit)
    public static final int BYTE_POS_SINGLE_VALUE_LOW_LOW = 5;
    public static final int BYTE_POS_SINGLE_VALUE_LOW_HIGH = 6;
    public static final int BYTE_POS_SINGLE_VALUE_HIGH_LOW = 7;
    public static final int BYTE_POS_SINGLE_VALUE_HIGH_HIGH = 8;

    // data packet min size + value-ID (2) + value (4)
    public static final int SIZE = DataPacket.MIN_SIZE + 6;
    
    public SingleValueDataPacket( byte[] data ) throws InsufficientBufferSizeException
    {
        super();

        if( data.length < SIZE )
        {
            throw new InsufficientBufferSizeException();
        }

        this.data = new byte[ SIZE ];
        // copy bytes
        for( int i = 0; i < SIZE; i++ )
        {
            this.data[ i ] = data[ i ];
        }
    }

    public SingleValue getSingleValue()
    {
        int valueId =
            data[ BYTE_POS_VALUE_ID_LOW ]
            + (data[ BYTE_POS_VALUE_ID_HIGH ] << 8);

        byte[] value = new byte[ 4 ];
        value[ 0 ] = data[ BYTE_POS_SINGLE_VALUE_HIGH_HIGH ];
        value[ 1 ] = data[ BYTE_POS_SINGLE_VALUE_HIGH_LOW ];
        value[ 2 ] = data[ BYTE_POS_SINGLE_VALUE_LOW_HIGH ];
        value[ 3 ] = data[ BYTE_POS_SINGLE_VALUE_LOW_LOW ];

        return new SingleValue( valueId, value );
    }
}
