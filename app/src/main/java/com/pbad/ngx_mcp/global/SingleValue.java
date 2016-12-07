package com.pbad.ngx_mcp.global;

import java.nio.ByteBuffer;

/**
 * Created by phili on 04.11.2016.
 */

public class SingleValue
{
    private int id;
    private int value;

    public SingleValue( int id, int value )
    {
        this.id = id;
        this.value = value;
    }

    public int getId() { return id; }

    public int getIntValue()
    {
        return value;
    }

    public boolean getBooleanValue()
    {
        int intVal = getIntValue();
        if( intVal == 0 )
            return false;
        return true;
    }

    public float getFloatValue()
    {
        byte[] raw = new byte[ 4 ];
        raw[ 0 ] = (byte) ((value & 0xff000000) >> 24);
        raw[ 1 ] = (byte) ((value & 0x00ff0000) >> 16);
        raw[ 2 ] = (byte) ((value & 0x0000ff00) >> 8);
        raw[ 3 ] = (byte) (value & 0x000000ff);

        return ByteBuffer.wrap( raw ).getFloat();
    }
}
