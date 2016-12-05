package com.pbad.ngx_mcp.networking.Protocol;

/**
 * Created by phili on 05.12.2016.
 */

public enum Response
{
    FAIL( 0 ),
    OK( 1 );

    private int response;
    Response( int response )
    {
        this.response = response;
    }

    public int toInt()
    {
        return response;
    }
}
