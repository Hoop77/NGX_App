package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class AllValuesRequestPacket extends RequestPacket
{
    public AllValuesRequestPacket( int entityId )
    {
        super( entityId, RequestType.ALL_VALUES );
    }
}
