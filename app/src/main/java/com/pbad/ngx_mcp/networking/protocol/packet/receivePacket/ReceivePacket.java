package com.pbad.ngx_mcp.networking.protocol.packet.receivePacket;

import com.pbad.ngx_mcp.networking.protocol.packet.Packet;

/**
 * Created by phili on 04.11.2016.
 */

public class ReceivePacket extends Packet
{
    public ReceivePacket()
    {
        super();
    }

    public static int getPacketType( byte[] data )
    {
        return data[ Packet.BYTE_POS_PACKET_TYPE ];
    }

    public static int getEntityId( byte[] data )
    {
        return data[ Packet.BYTE_POS_ENTITY_ID ];
    }

    public int getPacketType()
    {
        return getPacketType( data );
    }

    public int getEntityId()
    {
        return getEntityId( data );
    }
}
