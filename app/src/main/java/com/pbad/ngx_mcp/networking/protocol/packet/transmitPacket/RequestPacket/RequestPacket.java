package com.pbad.ngx_mcp.networking.protocol.packet.transmitPacket.RequestPacket;

import com.pbad.ngx_mcp.networking.protocol.packet.Packet;
import com.pbad.ngx_mcp.networking.protocol.packet.transmitPacket.TransmitPacket;

/**
 * Created by phili on 04.11.2016.
 */

public abstract class RequestPacket extends TransmitPacket
{
    // request types
    public static final int REQUEST_TYPE_SINGLE_VALUE = 0;
    public static final int REQUEST_TYPE_ALL_VALUES = 1;

    // request type (8 bit)
    public static final int BYTE_POS_REQUEST_TYPE = 2;

    // packet min size + request type (1)
    public static final int MIN_SIZE = Packet.MIN_SIZE + 1;

    public RequestPacket()
    {
        super();
    }

    protected void setRequestType( int requestType )
    {
        data[ BYTE_POS_REQUEST_TYPE ] = (byte) requestType;
    }
}
