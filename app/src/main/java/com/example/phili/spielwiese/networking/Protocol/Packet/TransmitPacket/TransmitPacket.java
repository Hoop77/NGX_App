package com.example.phili.spielwiese.networking.protocol.packet.transmitPacket;

import com.example.phili.spielwiese.networking.protocol.packet.Packet;

/**
 * Created by phili on 04.11.2016.
 */

public abstract class TransmitPacket extends Packet
{
    public TransmitPacket()
    {
        super();
    }

    protected void setPacketType( int packetType )
    {
        data[ Packet.BYTE_POS_PACKET_TYPE ] = (byte) packetType;
    }

    protected void setEntityId( int entityId )
    {
        data[ Packet.BYTE_POS_ENTITY_ID ] = (byte) entityId;
    }
}
