package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class DataPacket extends Packet
{
    private RequestType requestType;

    public DataPacket( int entityId, RequestType requestType )
    {
        super( PacketType.DATA, entityId );

        this.requestType = requestType;
    }

    public RequestType getRequestType()
    {
        return requestType;
    }
}
