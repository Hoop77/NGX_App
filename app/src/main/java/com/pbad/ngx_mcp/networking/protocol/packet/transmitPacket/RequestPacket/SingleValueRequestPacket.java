package com.pbad.ngx_mcp.networking.protocol.packet.transmitPacket.RequestPacket;

import com.pbad.ngx_mcp.networking.protocol.packet.Packet;

/**
 * Created by phili on 04.11.2016.
 */

public class SingleValueRequestPacket extends RequestPacket
{
    // value-ID (16 bit)
    public static final int BYTE_POS_VALUE_ID_LOW = 3;
    public static final int BYTE_POS_VALUE_ID_HIGH = 4;

    // request packet min size + value-ID (2)
    public static final int SIZE = RequestPacket.MIN_SIZE + 2;

    public SingleValueRequestPacket( int entityId, int valueId )
    {
        super();

        data = new byte[ SIZE ];

        setPacketType( Packet.PACKET_TYPE_REQUEST );
        setEntityId( entityId );
        setRequestType( RequestPacket.REQUEST_TYPE_SINGLE_VALUE );
        setValueId( valueId );
    }

    protected void setValueId( int valueId )
    {
        data[ BYTE_POS_VALUE_ID_LOW ] = (byte) valueId;
        data[ BYTE_POS_VALUE_ID_HIGH ] = (byte) (valueId >> 8);
    }
}
