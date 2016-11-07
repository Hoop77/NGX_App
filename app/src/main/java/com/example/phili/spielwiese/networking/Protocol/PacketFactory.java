package com.example.phili.spielwiese.networking.protocol;

import com.example.phili.spielwiese.networking.InsufficientBufferSizeException;
import com.example.phili.spielwiese.networking.protocol.packet.Packet;
import com.example.phili.spielwiese.networking.protocol.packet.receivePacket.dataPacket.AllValuesDataPacket;
import com.example.phili.spielwiese.networking.protocol.packet.receivePacket.dataPacket.DataPacket;
import com.example.phili.spielwiese.networking.protocol.packet.receivePacket.dataPacket.SingleValueDataPacket;
import com.example.phili.spielwiese.networking.protocol.packet.receivePacket.ReceivePacket;
import com.example.phili.spielwiese.networking.protocol.packet.transmitPacket.RequestPacket.RequestPacket;

/**
 * Created by phili on 05.11.2016.
 */

public class PacketFactory
{
    public static DataPacket createPacketFromReceivedData( byte[] data ) throws InsufficientBufferSizeException
    {
        if( data.length < DataPacket.MIN_SIZE )
            return null;

        int packetType = ReceivePacket.getPacketType( data );
        if( packetType != Packet.PACKET_TYPE_DATA )
            return null;

        DataPacket packet = null;

        int requestType = DataPacket.getRequestType( data );
        if( requestType == RequestPacket.REQUEST_TYPE_SINGLE_VALUE  )
        {
            packet = new SingleValueDataPacket( data );
        }
        else if( requestType == RequestPacket.REQUEST_TYPE_ALL_VALUES )
        {
            packet = new AllValuesDataPacket( data );
        }
        else
        {
            return null;
        }

        return packet;
    }
}
