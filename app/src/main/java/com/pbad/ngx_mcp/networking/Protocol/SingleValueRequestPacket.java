package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class SingleValueRequestPacket extends RequestPacket
{
    private int valueId;

    public SingleValueRequestPacket( int entityId, int valueId )
    {
        super( entityId, RequestType.SINGLE_VALUE );

        this.valueId = valueId;
    }

    public int getValueId()
    {
        return valueId;
    }
}
