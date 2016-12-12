package com.pbad.ngx_mcp.global;

/**
 * Created by philipp on 08.12.16.
 */

public enum MCPEventId
{
    COURSE_SELECTOR_L( 69632 + 376 ),
    FD_SWITCH_L( 69632 + 378 ),
    AT_ARM_SWITCH( 69632 + 380 ),
    N1_SWITCH( 69632 + 381 ),
    SPEED_SWITCH( 69632 + 382 ),
    CO_SWITCH( 69632 + 383 ),
    SPEED_SELECTOR( 69632 + 384 ),
    VNAV_SWITCH( 69632 + 386 ),
    SPD_INTV_SWITCH( 69632 + 387 ),
    BANK_ANGLE_SELECTOR( 69632 + 389 ),
    HEADING_SELECTOR( 69632 + 390 ),
    LVL_CHG_SWITCH( 69632 + 391 ),
    HDG_SEL_SWITCH( 69632 + 392 ),
    APP_SWITCH( 69632 + 393 ),
    ALT_HOLD_SWITCH( 69632 + 394 ),
    VS_SWITCH( 69632 + 395 ),
    VOR_LOC_SWITCH( 69632 + 396 ),
    LNAV_SWITCH( 69632 + 397 ),
    ALTITUDE_SELECTOR( 69632 + 400 ),
    VS_SELECTOR( 69632 + 401 ),
    CMD_A_SWITCH( 69632 + 402 ),
    CMD_B_SWITCH( 69632 + 403 ),
    CWS_A_SWITCH( 69632 + 404 ),
    CWS_B_SWITCH( 69632 + 405 ),
    DISENGAGE_BAR( 69632 + 406 ),
    FD_SWITCH_R( 69632 + 407 ),
    COURSE_SELECTOR_R( 69632 + 409 ),
    ALT_INTV_SWITCH( 69632 + 885 );

    private int eventId;
    MCPEventId( int eventId )
    {
        this.eventId = eventId;
    }

    public int toInt()
    {
        return eventId;
    }
}
