package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class RequestPacket extends Packet
{
    private RequestType requestType;

    public RequestPacket( int entityId, RequestType requestType )
    {
        super( PacketType.REQUEST, entityId );

        this.requestType = requestType;
    }

    public RequestType getRequestType()
    {
        return requestType;
    }
}
