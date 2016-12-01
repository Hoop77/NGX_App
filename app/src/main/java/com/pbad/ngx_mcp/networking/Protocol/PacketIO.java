package com.pbad.ngx_mcp.networking.Protocol;

import com.pbad.ngx_mcp.global.Global;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by philipp on 01.12.16.
 */

public class PacketIO
{
    private static class ReadArgs
    {
        public Packet packet;
        public DataInputStream dis;
        public byte[] buf;
        public int pos;

        public ReadArgs( DataInputStream dis, byte[] buf, int pos )
        {
            packet = null;
            this.dis = dis;
            this.buf = buf;
            this.pos = pos;
        }
    }

    public static Packet read( InputStream is ) throws IOException, ProtocolException
    {
        ReadArgs args = new ReadArgs(
            new DataInputStream( is ),
            new byte[ Packet.MAX_SIZE ],
            0 );

        readPacket( args );

        return args.packet;
    }

    private static void readNext( ReadArgs args, int len ) throws IOException
    {
        args.dis.readFully( args.buf, args.pos, len );
        args.pos += len;
    }

    private static void readPacket( ReadArgs args ) throws ProtocolException, IOException
    {
        readNext( args, 2 );
        int packetType = (int) args.buf[ 0 ];
        int entityId = (int) args.buf[ 1 ];

        if( packetType == PacketType.DATA.toInt() )
            readDataPacket( args, entityId );
        else
            throw new ProtocolException();
    }

    private static void readDataPacket( ReadArgs args, int entityId ) throws IOException, ProtocolException
    {
        readNext( args, 1 );
        int requestType = (int) args.buf[ 2 ];

        if( requestType == RequestType.SINGLE_VALUE.toInt() )
            readSingleValueDataPacket( args, entityId );
        else if( requestType == RequestType.ALL_VALUES.toInt() )
            readAllValuesDataPacket( args, entityId );
        else
            throw new ProtocolException();
    }

    private static void readSingleValueDataPacket( ReadArgs args, int entityId ) throws IOException
    {
        readNext( args, 6 );

        int valueId =
            (int) args.buf[ 3 ]
                + ((int) (args.buf[ 4 ]) << 8);

        int value =
            (int) args.buf[ 5 ]
                + ((int) (args.buf[ 6 ]) << 8)
                + ((int) (args.buf[ 7 ]) << 16)
                + ((int) (args.buf[ 8 ]) << 24);

        args.packet = new SingleValueDataPacket( entityId, valueId, value );
    }

    private static void readAllValuesDataPacket( ReadArgs args, int entityId )
    {
        int valueCount = Global.getValueIdCountFromEntityId( entityId );
        int[] values = new int[ valueCount ];

        for( int i = 0; i < valueCount; i++ )
        {
            int offset = 3 + i * 4;
            values[ i ] =
                (int) args.buf[ offset ]
                    + ((int) args.buf[ offset + 1 ] << 8)
                    + ((int) args.buf[ offset + 2 ] << 16)
                    + ((int) args.buf[ offset + 3 ] << 24);
        }

        args.packet = new AllValuesDataPacket( entityId, values );
    }

    public static void write( Packet packet, OutputStream os ) throws ProtocolException
    {
        byte[] raw = new byte[ Packet.MAX_SIZE ];
        int len = 0;

        switch( packet.getPacketType() )
        {
            case EVENT:
                getRawEventPacket( (EventPacket) packet, raw );
                break;

            case REQUEST:
                RequestPacket requestPacket = (RequestPacket) packet;
                switch( requestPacket.getRequestType() )
                {
                    case SINGLE_VALUE:
                        getRawSingleValueRequestPacket( (SingleValueRequestPacket) packet, raw );
                        break;

                    case ALL_VALUES:
                        getRawAllValuesRequestPacket( (AllValuesRequestPacket) packet, raw );
                        break;

                    default:
                        throw new ProtocolException();
                }

                break;

            case DATA:
                // not needed
                break;

            default:
                throw new ProtocolException( "invalid packet type" );
        }

        // write raw data to stream
        try
        {
            os.write( raw );
        }
        catch( IOException e )
        {
            throw new ProtocolException( "failed to write data to stream" );
        }
    }

    private static void getRawEventPacket( EventPacket eventPacket, byte[] raw )
    {
        raw[ 0 ] = (byte) (eventPacket.getPacketType().toInt() & 0x000000ff);
        raw[ 1 ] = (byte) (eventPacket.getEntityId() & 0x000000ff);
        raw[ 2 ] = (byte) (eventPacket.getEventId() & 0x000000ff);
        raw[ 3 ] = (byte) ((eventPacket.getEventId() & 0x0000ff00) >> 8);
        raw[ 4 ] = (byte) ((eventPacket.getEventId() & 0x00ff0000) >> 16);
        raw[ 5 ] = (byte) ((eventPacket.getEventId() & 0xff000000) >> 24);
        raw[ 6 ] = (byte) (eventPacket.getEventParameter() & 0x000000ff);
        raw[ 7 ] = (byte) ((eventPacket.getEventParameter() & 0x0000ff00) >> 8);
        raw[ 8 ] = (byte) ((eventPacket.getEventParameter() & 0x00ff0000) >> 16);
        raw[ 9 ] = (byte) ((eventPacket.getEventParameter() & 0xff000000) >> 24);
    }

    private static void getRawSingleValueRequestPacket( SingleValueRequestPacket singleValueRequestPacket, byte[] raw )
    {
        raw[ 0 ] = (byte) (singleValueRequestPacket.getPacketType().toInt() & 0x000000ff);
        raw[ 1 ] = (byte) (singleValueRequestPacket.getEntityId() & 0x000000ff);
        raw[ 2 ] = (byte) (singleValueRequestPacket.getRequestType().toInt() & 0x000000ff);
        raw[ 3 ] = (byte) (singleValueRequestPacket.getValueId() & 0x000000ff);
        raw[ 4 ] = (byte) ((singleValueRequestPacket.getValueId() & 0x0000ff00) >> 8);
    }

    private static void getRawAllValuesRequestPacket( AllValuesRequestPacket allValuesRequestPacket, byte[] raw )
    {
        raw[ 0 ] = (byte) (allValuesRequestPacket.getPacketType().toInt() & 0x000000ff);
        raw[ 1 ] = (byte) (allValuesRequestPacket.getEntityId() & 0x000000ff);
        raw[ 2 ] = (byte) (allValuesRequestPacket.getRequestType().toInt() & 0x000000ff);
    }
}
