package com.pbad.ngx_mcp.networking.protocol.packet.receivePacket.dataPacket;

import com.pbad.ngx_mcp.networking.protocol.packet.Packet;
import com.pbad.ngx_mcp.networking.protocol.packet.receivePacket.ReceivePacket;
import com.pbad.ngx_mcp.networking.protocol.packet.transmitPacket.RequestPacket.RequestPacket;

/**
 * Created by phili on 04.11.2016.
 */

public class DataPacket extends ReceivePacket
{
    // request type (8 bit)
    public static final int BYTE_POS_REQUEST_TYPE = 2;

    // packet min size + request type (1)
    public static final int MIN_SIZE = Packet.MIN_SIZE + 1;

    public DataPacket()
    {
        super();
    }

    public static int getRequestType( byte[] data )
    {
        return data[ RequestPacket.BYTE_POS_REQUEST_TYPE ];
    }

    public int getRequestType()
    {
        return getRequestType( data );
    }
}
