package com.pbad.ngx_mcp.global;

/**
 * Created by phili on 19.12.2016.
 */

public enum EventParameter
{
    MOUSE_WHEEL_UP( 0 ),
    MOUSE_WHEEL_DOWN( 1 ),
    MOUSE_LEFT_CLICK( 2 ),
    MOUSE_RIGHT_CLICK( 3 );

    private int parameter;

    EventParameter( int parameter )
    {
        this.parameter = parameter;
    }

    public int toInt()
    {
        return parameter;
    }
}
