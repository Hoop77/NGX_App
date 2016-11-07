package com.example.phili.spielwiese;

/**
 * Created by phili on 05.11.2016.
 */

public enum ValueId
{
    COURSE_L( 0 ),				// unsigned short
    COURSE_R( 1 ),				// unsigned short
    IAS_MACH( 2 ),				// float
    IAS_BLANK( 3 ),				// bool
    IAS_OVERSPEED_FLASH( 4 ),	// bool
    IAS_UNDERSPEED_FLASH( 5 ),	// bool
    HEADING( 6 ),				// unsigned short
    ALTITUDE( 7 ),				// unsigned short
    VERT_SPEED( 8 ),			// short
    VERT_SPEED_BLANK( 9 ),		// bool
    FD_SW_L( 10 ),				// bool
    FD_SW_R( 11 ),				// bool
    AT_ARM_SW( 12 ),			// bool
    BANK_LIMIT_SEL( 13 ),		// unsigned char
    DISENGAGE_BAR( 14 ),		// bool
    ANNUN_FD_L( 15 ),			// bool
    ANNUN_FD_R( 16 ),			// bool
    ANNUN_AT_ARM( 17 ),			// bool
    ANNUN_N1( 18 ),				// bool
    ANNUN_SPEED( 19 ),			// bool
    ANNUN_VNAV( 20 ),			// bool
    ANNUN_LVL_CHG( 21 ),		// bool
    ANNUN_HDG_SEL( 22 ),		// bool
    ANNUN_LNAV( 23 ),			// bool
    ANNUN_VOR_LOC( 24 ),		// bool
    ANNUN_ALT_HOLD( 26 ),		// bool
    ANNUN_APP( 25 ),			// bool
    ANNUN_VS( 27 ),				// bool
    ANNUN_CMD_A( 28 ),			// bool
    ANNUN_CWS_A( 29 ),			// bool
    ANNUN_CMD_B( 30 ),			// bool
    ANNUN_CWS_B( 31 ),			// bool

    COUNT( 32 );

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
