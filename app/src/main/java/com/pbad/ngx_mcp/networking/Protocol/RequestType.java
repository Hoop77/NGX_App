package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by philipp on 01.12.16.
 */

public enum RequestType
{
    SINGLE_VALUE( 0 ),
    ALL_VALUES( 1 );

    private int requestType;
    private RequestType( int requestType )
    {
        this.requestType = requestType;
    }

    public int toInt()
    {
        return requestType;
    }
}
