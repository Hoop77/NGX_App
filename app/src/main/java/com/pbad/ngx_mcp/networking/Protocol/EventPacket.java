package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class EventPacket extends Packet
{
    private int eventId;
    private int eventParameter;

    public EventPacket( int entityId, int eventId, int eventParameter )
    {
        super( PacketType.EVENT, entityId );

        this.eventId = eventId;
        this.eventParameter = eventParameter;
    }

    public int getEventId()
    {
        return eventId;
    }

    public int getEventParameter()
    {
        return eventParameter;
    }
}
