package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public enum PacketType
{
    EVENT( 0 ),
    REQUEST( 1 ),
    DATA( 2 );

    private int packetType;
    PacketType( int packetType )
    {
        this.packetType = packetType;
    }

    public int toInt()
    {
        return packetType;
    }
}
