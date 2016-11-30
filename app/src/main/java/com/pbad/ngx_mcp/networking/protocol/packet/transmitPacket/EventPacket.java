package com.pbad.ngx_mcp.networking.protocol.packet.transmitPacket;

import com.pbad.ngx_mcp.networking.protocol.packet.Packet;

/**
 * Created by phili on 04.11.2016.
 */

public class EventPacket extends TransmitPacket
{
    // event-ID (32 bit)
    public static final int BYTE_POS_EVENT_ID_LOW_LOW = 2;
    public static final int BYTE_POS_EVENT_ID_LOW_HIGH = 3;
    public static final int BYTE_POS_EVENT_ID_HIGH_LOW = 4;
    public static final int BYTE_POS_EVENT_ID_HIGH_HIGH = 5;

    // event-parameter (32 bit)
    public static final int BYTE_POS_EVENT_PARAMETER_LOW_LOW = 6;
    public static final int BYTE_POS_EVENT_PARAMETER_LOW_HIGH = 7;
    public static final int BYTE_POS_EVENT_PARAMETER_HIGH_LOW = 8;
    public static final int BYTE_POS_EVENT_PARAMETER_HIGH_HIGH = 9;

    // packet min size + event-ID (4) + event-parameter
    public static final int SIZE = Packet.MIN_SIZE + 8;

    public EventPacket( int entityId, int eventId, int eventParameter )
    {
        super();

        data = new byte[ SIZE ];

        setPacketType( Packet.PACKET_TYPE_EVENT );
        setEntityId( entityId );
        setEventId( eventId );
        setEventParameter( eventParameter );
    }

    public void setEventId( int eventId )
    {
        data[ BYTE_POS_EVENT_ID_LOW_LOW ] = (byte) eventId;
        data[ BYTE_POS_EVENT_ID_LOW_HIGH ] = (byte) (eventId >> 8);
        data[ BYTE_POS_EVENT_ID_HIGH_LOW ] = (byte) (eventId >> 16);
        data[ BYTE_POS_EVENT_ID_HIGH_HIGH ] = (byte) (eventId >> 24);
    }

    public void setEventParameter( int eventParameter )
    {
        data[ BYTE_POS_EVENT_PARAMETER_LOW_LOW ] = (byte) eventParameter;
        data[ BYTE_POS_EVENT_PARAMETER_LOW_HIGH ] = (byte) (eventParameter >> 8);
        data[ BYTE_POS_EVENT_PARAMETER_HIGH_LOW ] = (byte) (eventParameter >> 16);
        data[ BYTE_POS_EVENT_PARAMETER_HIGH_HIGH ] = (byte) (eventParameter >> 24);
    }
}
