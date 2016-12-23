package com.pbad.ngx_mcp.global;

/**
 * Created by phili on 05.11.2016.
 */

public enum ValueId
{
    MCP_COURSE_L( 0 ),				// unsigned short
    MCP_COURSE_R( 1 ),				// unsigned short
    MCP_IAS_MACH( 2 ),				// float
    MCP_IAS_BLANK( 3 ),				// bool
    MCP_IAS_OVERSPEED_FLASH( 4 ),	// bool
    MCP_IAS_UNDERSPEED_FLASH( 5 ),	// bool
    MCP_HEADING( 6 ),				// unsigned short
    MCP_ALTITUDE( 7 ),				// unsigned short
    MCP_VERT_SPEED( 8 ),			// short
    MCP_VERT_SPEED_BLANK( 9 ),		// bool
    MCP_FD_SW_L( 10 ),				// bool
    MCP_FD_SW_R( 11 ),				// bool
    MCP_AT_ARM_SW( 12 ),			// bool
    MCP_BANK_LIMIT_SEL( 13 ),		// unsigned char
    MCP_DISENGAGE_BAR( 14 ),		// bool
    MCP_ANNUN_FD_L( 15 ),			// bool
    MCP_ANNUN_FD_R( 16 ),			// bool
    MCP_ANNUN_AT_ARM( 17 ),			// bool
    MCP_ANNUN_N1( 18 ),				// bool
    MCP_ANNUN_SPEED( 19 ),			// bool
    MCP_ANNUN_VNAV( 20 ),			// bool
    MCP_ANNUN_LVL_CHG( 21 ),		// bool
    MCP_ANNUN_HDG_SEL( 22 ),		// bool
    MCP_ANNUN_LNAV( 23 ),			// bool
    MCP_ANNUN_VOR_LOC( 24 ),		// bool
    MCP_ANNUN_ALT_HOLD( 26 ),		// bool
    MCP_ANNUN_APP( 25 ),			// bool
    MCP_ANNUN_VS( 27 ),				// bool
    MCP_ANNUN_CMD_A( 28 ),			// bool
    MCP_ANNUN_CWS_A( 29 ),			// bool
    MCP_ANNUN_CMD_B( 30 ),			// bool
    MCP_ANNUN_CWS_B( 31 ),			// bool

    MCP_COUNT( 32 );

    private int valueId;

    ValueId( int valueId )
    {
        this.valueId = valueId;
    }

    public int toInt()
    {
        return valueId;
    }
}
