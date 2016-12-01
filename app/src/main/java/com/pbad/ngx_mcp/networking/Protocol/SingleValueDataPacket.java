package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class SingleValueDataPacket extends DataPacket
{
    private int valueId;
    private int value;

    public SingleValueDataPacket( int entityId, int valueId, int value )
    {
        super( entityId, RequestType.SINGLE_VALUE );

        this.valueId = valueId;
        this.value = value;
    }

    public int getValueId()
    {
        return valueId;
    }

    public int getValue()
    {
        return value;
    }
}
