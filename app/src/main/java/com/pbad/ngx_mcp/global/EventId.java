package com.pbad.ngx_mcp.global;

/**
 * Created by philipp on 08.12.16.
 */

public enum EventId
{
    MCP_COURSE_SELECTOR_L( 69632 + 376 ),
    MCP_FD_SWITCH_L( 69632 + 378 ),
    MCP_AT_ARM_SWITCH( 69632 + 380 ),
    MCP_N1_SWITCH( 69632 + 381 ),
    MCP_SPEED_SWITCH( 69632 + 382 ),
    MCP_CO_SWITCH( 69632 + 383 ),
    MCP_SPEED_SELECTOR( 69632 + 384 ),
    MCP_VNAV_SWITCH( 69632 + 386 ),
    MCP_SPD_INTV_SWITCH( 69632 + 387 ),
    MCP_BANK_ANGLE_SELECTOR( 69632 + 389 ),
    MCP_HEADING_SELECTOR( 69632 + 390 ),
    MCP_LVL_CHG_SWITCH( 69632 + 391 ),
    MCP_HDG_SEL_SWITCH( 69632 + 392 ),
    MCP_APP_SWITCH( 69632 + 393 ),
    MCP_ALT_HOLD_SWITCH( 69632 + 394 ),
    MCP_VS_SWITCH( 69632 + 395 ),
    MCP_VOR_LOC_SWITCH( 69632 + 396 ),
    MCP_LNAV_SWITCH( 69632 + 397 ),
    MCP_ALTITUDE_SELECTOR( 69632 + 400 ),
    MCP_VS_SELECTOR( 69632 + 401 ),
    MCP_CMD_A_SWITCH( 69632 + 402 ),
    MCP_CMD_B_SWITCH( 69632 + 403 ),
    MCP_CWS_A_SWITCH( 69632 + 404 ),
    MCP_CWS_B_SWITCH( 69632 + 405 ),
    MCP_DISENGAGE_BAR( 69632 + 406 ),
    MCP_FD_SWITCH_R( 69632 + 407 ),
    MCP_COURSE_SELECTOR_R( 69632 + 409 ),
    MCP_ALT_INTV_SWITCH( 69632 + 885 );

    private int eventId;
    EventId( int eventId )
    {
        this.eventId = eventId;
    }

    public int toInt()
    {
        return eventId;
    }
}
