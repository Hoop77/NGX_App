package com.pbad.ngx_mcp.global;

/**
 * Created by philipp on 08.12.16.
 */

public enum SpecialEventParameter
{
    MOUSE_FLAG_RIGHTSINGLE( 0x80000000 ),
    MOUSE_FLAG_MIDDLESINGLE( 0x40000000 ),
    MOUSE_FLAG_LEFTSINGLE( 0x20000000 ),
    MOUSE_FLAG_RIGHTDOUBLE( 0x10000000 ),
    MOUSE_FLAG_MIDDLEDOUBLE( 0x08000000 ),
    MOUSE_FLAG_LEFTDOUBLE( 0x04000000 ),
    MOUSE_FLAG_RIGHTDRAG( 0x02000000 ),
    MOUSE_FLAG_MIDDLEDRAG( 0x01000000 ),
    MOUSE_FLAG_LEFTDRAG( 0x00800000 ),
    MOUSE_FLAG_MOVE( 0x00400000 ),
    MOUSE_FLAG_DOWN_REPEAT( 0x00200000 ),
    MOUSE_FLAG_RIGHTRELEASE( 0x00080000 ),
    MOUSE_FLAG_MIDDLERELEASE( 0x00040000 ),
    MOUSE_FLAG_LEFTRELEASE( 0x00020000 ),
    MOUSE_FLAG_WHEEL_FLIP( 0x00010000 ),  // invert direction of mouse wheel
    MOUSE_FLAG_WHEEL_SKIP( 0x00008000 ),   // look at next 2 rect for mouse wheel commands
    MOUSE_FLAG_WHEEL_UP( 0x00004000 ),
    MOUSE_FLAG_WHEEL_DOWN( 0x00002000 );

    private int parameter;

    SpecialEventParameter( int parameter )
    {
        this.parameter = parameter;
    }

    public int toInt()
    {
        return parameter;
    }
}
