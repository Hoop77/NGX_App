package com.pbad.ngx_mcp.networking.protocol.packet.transmitPacket.RequestPacket;

import com.pbad.ngx_mcp.networking.protocol.packet.Packet;

/**
 * Created by phili on 04.11.2016.
 */

public class AllValuesRequestPacket extends RequestPacket
{
    // request packet min size (nothing more)
    public static final int SIZE = RequestPacket.MIN_SIZE;

    public AllValuesRequestPacket( int entityId )
    {
        super();

        data = new byte[ SIZE ];

        setPacketType(Packet.PACKET_TYPE_REQUEST );
        setEntityId( entityId );
        setRequestType( RequestPacket.REQUEST_TYPE_ALL_VALUES );
    }
}
