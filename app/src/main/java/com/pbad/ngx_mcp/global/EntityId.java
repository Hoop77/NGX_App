package com.pbad.ngx_mcp.global;

/**
 * Created by philipp on 06.12.16.
 */

public enum EntityId
{
    MCP( 0 ),
    COUNT( 1 );

    private int entityId;
    EntityId( int entityId )
    {
        this.entityId = entityId;
    }

    public int toInt()
    {
        return entityId;
    }
}
