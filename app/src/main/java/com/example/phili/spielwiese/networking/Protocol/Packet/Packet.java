package com.example.phili.spielwiese.networking.protocol.packet;

/**
 * Created by phili on 04.11.2016.
 */

public abstract class Packet
{
    // packet types
    public static final int PACKET_TYPE_EVENT = 0;
    public static final int PACKET_TYPE_REQUEST = 1;
    public static final int PACKET_TYPE_DATA = 2;

    // packet type (8 bit)
    public static final int BYTE_POS_PACKET_TYPE = 0;

    // entity-ID (8 bit)
    public static final int BYTE_POS_ENTITY_ID = 1;

    // packet type (1) + entity id (1)
    public static final int MIN_SIZE = 2;

    // fixed max size for every packet
    public static final int MAX_SIZE = 1024;

    // the byte array
    protected byte[] data;

    public Packet() {}

    public byte[] getData()
    {
        return data;
    }
}
