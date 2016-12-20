package com.pbad.ngx_mcp.global;

import com.example.phili.ngx_mcp.R;

/**
 * Created by phili on 04.11.2016.
 */

public class Global
{
    public static int getValueIdCountFromEntityId( int entityId )
    {
        if( entityId == EntityId.MCP.toInt() )
            return EntityId.COUNT.toInt();
        else
            return 0;
    }
}
