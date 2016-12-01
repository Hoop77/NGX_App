package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public class AllValuesDataPacket extends DataPacket
{
    private int[] values;

    public AllValuesDataPacket( int entityId, int[] values )
    {
        super( entityId, RequestType.ALL_VALUES );

        this.values = values;
    }

    public int[] getValues()
    {
        return values;
    }
}
