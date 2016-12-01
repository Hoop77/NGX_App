package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class Packet
{
    public static final int MAX_SIZE = 1024;

    private PacketType packetType;
    private int entityId;

    public Packet( PacketType packetType, int entityId )
    {
        this.packetType = packetType;
        this.entityId = entityId;
    }

    public PacketType getPacketType()
    {
        return packetType;
    }

    public int getEntityId()
    {
        return entityId;
    }
}
