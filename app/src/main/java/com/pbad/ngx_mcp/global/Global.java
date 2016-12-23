package com.pbad.ngx_mcp.global;

/**
 * Created by phili on 04.11.2016.
 */

public class Global
{
    public static int getValueIdCountFromEntityId( int entityId )
    {
        if( entityId == EntityId.MCP.toInt() )
            return ValueId.MCP_COUNT.toInt();
        else
            return 0;
    }
}
