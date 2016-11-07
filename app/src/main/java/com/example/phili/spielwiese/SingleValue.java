package com.example.phili.spielwiese;

import java.nio.ByteBuffer;

/**
 * Created by phili on 04.11.2016.
 */

public class SingleValue
{
    public int id;
    public byte[] value;

    public SingleValue( int id, byte[] value )
    {
        this.id = id;
        this.value = value;
    }

    public int getIntValue()
    {
        return ByteBuffer.wrap( value ).getInt();
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
        return ByteBuffer.wrap( value ).getFloat();
    }
}
