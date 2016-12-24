package com.pbad.ngx_mcp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pbad.ngx_mcp.global.EntityId;
import com.pbad.ngx_mcp.global.EventId;
import com.pbad.ngx_mcp.global.EventParameter;
import com.pbad.ngx_mcp.global.SingleValue;
import com.pbad.ngx_mcp.global.ValueId;

/**
 * Created by phili on 10.11.2016.
 */

public class MCPFragment extends Fragment implements Receiver
{
    public static final String BLANK = "-";
    // This value seems to be send when the vertical speed turns off (is becoming "blank").
    public static final float VS_BLANK_VALUE = -16960;

    private View view;
    private Commander commander;

    private TextView
            txtHdg, txtAlt, txtVs, txtIas, txtCrsL, txtCrsR;

    private Button
            btnHdgPlus, btnHdgMinus,
            btnAltPlus, btnAltMinus,
            btnVsPlus, btnVsMinus,
            btnIasPlus, btnIasMinus,
            btnCrsLPlus, btnCrsLMinus,
            btnCrsRPlus, btnCrsRMinus;
    private Button
            btnAltInv, btnSpdInv;
    private SwitchView
            svLnav, svHdgSel, svVorLoc,
            svVnav, svAltHld, svVs, svLvlChg,
            svAtArm, svN1, svSpeed,
            svApp,
            svCmdA, svCmdB, svCwsA, svCwsB, svDisengangeBar,
            svFdL, svFdR,
            svBank10, svBank15, svBank20, svBank25, svBank30;
    private SwitchViewGroup bankSelector;

    private boolean iasBlank, vsBlank;
    private float ias;
    private int vs;

    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState )
    {
        super.onCreateView( inflater, container, savedInstanceState );

        this.view = inflater.inflate( R.layout.mcp_fragment, container, false );

        setupViewsAndEvents();

        Button openSettingsBtn = (Button) view.findViewById( R.id.open_settings );
        openSettingsBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.showSettings();
            }
        } );

        showDisconnected();
        commander.connect();

        return view;
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );

        commander = (Commander) context;
    }

    public void showConnected()
    {
        View connectingView = view.findViewById( R.id.connecting_layout );
        connectingView.setVisibility( View.INVISIBLE );

        View mainLayout = view.findViewById( R.id.main_layout );
        mainLayout.setClickable( true );
    }

    public void showDisconnected()
    {
        Log.d( "MCPFragment", "showDisconnected()" );

        View connectingView = view.findViewById( R.id.connecting_layout );
        connectingView.setVisibility( View.VISIBLE );

        View mainLayout = view.findViewById( R.id.main_layout );
        mainLayout.setClickable( false );
    }

    @Override
    public void receiveValue( int entityId, int valueId, int value )
    {
        if( entityId == EntityId.MCP.toInt() )
            setValue( new SingleValue( valueId, value ) );
    }

    private void setupViewsAndEvents()
    {
        txtHdg = (TextView) view.findViewById( R.id.heading_value );
        txtAlt = (TextView) view.findViewById( R.id.alt_value );
        txtVs = (TextView) view.findViewById( R.id.vs_value );
        txtIas = (TextView) view.findViewById( R.id.ias_value );
        txtCrsL = (TextView) view.findViewById( R.id.course_left_value );
        txtCrsR = (TextView) view.findViewById( R.id.course_right_value );

        // heading selector
        btnHdgPlus = (Button) view.findViewById( R.id.heading_plus );
        setupButton( btnHdgPlus, EntityId.MCP, EventId.MCP_HEADING_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        btnHdgMinus = (Button) view.findViewById( R.id.heading_minus );
        setupButton( btnHdgMinus, EntityId.MCP, EventId.MCP_HEADING_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        // alt selector
        btnAltPlus = (Button) view.findViewById( R.id.alt_plus );
        setupButton( btnAltPlus, EntityId.MCP, EventId.MCP_ALTITUDE_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        btnAltMinus = (Button) view.findViewById( R.id.alt_minus );
        setupButton( btnAltMinus, EntityId.MCP, EventId.MCP_ALTITUDE_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        // svVs selector
        btnVsPlus = (Button) view.findViewById( R.id.vs_plus );
        setupButton( btnVsPlus, EntityId.MCP, EventId.MCP_VS_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        btnVsMinus = (Button) view.findViewById( R.id.vs_minus );
        setupButton( btnVsMinus, EntityId.MCP, EventId.MCP_VS_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        // ias selector
        btnIasPlus = (Button) view.findViewById( R.id.ias_plus );
        setupButton( btnIasPlus, EntityId.MCP, EventId.MCP_SPEED_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        btnIasMinus = (Button) view.findViewById( R.id.ias_minus );
        setupButton( btnIasMinus, EntityId.MCP, EventId.MCP_SPEED_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        // crs_left selector
        btnCrsLPlus = (Button) view.findViewById( R.id.course_left_plus );
        setupButton( btnCrsLPlus, EntityId.MCP, EventId.MCP_COURSE_SELECTOR_L, EventParameter.MOUSE_WHEEL_UP );
        btnCrsLMinus = (Button) view.findViewById( R.id.course_left_minus );
        setupButton( btnCrsLMinus, EntityId.MCP, EventId.MCP_COURSE_SELECTOR_L, EventParameter.MOUSE_WHEEL_DOWN );
        // crs_right selector
        btnCrsRPlus = (Button) view.findViewById( R.id.course_right_plus );
        setupButton( btnCrsRPlus, EntityId.MCP, EventId.MCP_COURSE_SELECTOR_R, EventParameter.MOUSE_WHEEL_UP );
        btnCrsRMinus = (Button) view.findViewById( R.id.course_right_minus );
        setupButton( btnCrsRMinus, EntityId.MCP, EventId.MCP_COURSE_SELECTOR_R, EventParameter.MOUSE_WHEEL_DOWN );

        // alt_intv button
        btnAltInv = (Button) view.findViewById( R.id.alt_intv_switch );
        setupPushButton( btnAltInv, EntityId.MCP, EventId.MCP_ALT_INTV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // spd_intv button
        btnSpdInv = (Button) view.findViewById( R.id.spd_intv_switch );
        setupPushButton( btnSpdInv, EntityId.MCP, EventId.MCP_SPD_INTV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );

        // svLnav switch
        svLnav = (SwitchView) view.findViewById( R.id.lnav_switch );
        setupSwitchView( svLnav, EntityId.MCP, EventId.MCP_LNAV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // hdg_sel switch
        svHdgSel = (SwitchView) view.findViewById( R.id.hdg_sel_switch );
        setupSwitchView( svHdgSel, EntityId.MCP, EventId.MCP_HDG_SEL_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // vor_loc switch
        svVorLoc = (SwitchView) view.findViewById( R.id.vor_loc_switch );
        setupSwitchView( svVorLoc, EntityId.MCP, EventId.MCP_VOR_LOC_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // svVnav switch
        svVnav = (SwitchView) view.findViewById( R.id.vnav_switch );
        setupSwitchView( svVnav, EntityId.MCP, EventId.MCP_VNAV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // alt_hld switch
        svAltHld = (SwitchView) view.findViewById( R.id.alt_hold_switch );
        setupSwitchView( svAltHld, EntityId.MCP, EventId.MCP_ALT_HOLD_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // svVs switch
        svVs = (SwitchView) view.findViewById( R.id.vs_switch );
        setupSwitchView( svVs, EntityId.MCP, EventId.MCP_VS_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // lvl_chg switch
        svLvlChg = (SwitchView) view.findViewById( R.id.lvl_chg_switch );
        setupSwitchView( svLvlChg, EntityId.MCP, EventId.MCP_LVL_CHG_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // at_arm switch
        svAtArm = (SwitchView) view.findViewById( R.id.at_arm_switch );
        setupSwitchView( svAtArm, EntityId.MCP, EventId.MCP_AT_ARM_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // svN1 switch
        svN1 = (SwitchView) view.findViewById( R.id.n1_switch );
        setupSwitchView( svN1, EntityId.MCP, EventId.MCP_N1_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // svSpeed switch
        svSpeed = (SwitchView) view.findViewById( R.id.speed_switch );
        setupSwitchView( svSpeed, EntityId.MCP, EventId.MCP_SPEED_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // app switch
        svApp = (SwitchView) view.findViewById( R.id.app_switch );
        setupSwitchView( svApp, EntityId.MCP, EventId.MCP_APP_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cmd_a switch
        svCmdA = (SwitchView) view.findViewById( R.id.cmd_a_switch );
        setupSwitchView( svCmdA, EntityId.MCP, EventId.MCP_CMD_A_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cmd_b switch
        svCmdB = (SwitchView) view.findViewById( R.id.cmd_b_switch );
        setupSwitchView( svCmdB, EntityId.MCP, EventId.MCP_CMD_B_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cws_a switch
        svCwsA = (SwitchView) view.findViewById( R.id.cws_a_switch );
        setupSwitchView( svCwsA, EntityId.MCP, EventId.MCP_CWS_A_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cws_b switch
        svCwsB = (SwitchView) view.findViewById( R.id.cws_b_switch );
        setupSwitchView( svCwsB, EntityId.MCP, EventId.MCP_CWS_B_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // disengage bar
        svDisengangeBar = (SwitchView) view.findViewById( R.id.disengage_bar );
        setupSwitchView( svDisengangeBar, EntityId.MCP, EventId.MCP_DISENGAGE_BAR, EventParameter.MOUSE_LEFT_CLICK );
        // flight directors
        svFdL = (SwitchView) view.findViewById( R.id.fd_switch_l );
        setupSwitchView( svFdL, EntityId.MCP, EventId.MCP_FD_SWITCH_L, EventParameter.MOUSE_LEFT_CLICK );
        svFdR = (SwitchView) view.findViewById( R.id.fd_switch_r );
        setupSwitchView( svFdR, EntityId.MCP, EventId.MCP_FD_SWITCH_R, EventParameter.MOUSE_LEFT_CLICK );

        // bank selector
        svBank10 = (SwitchView) view.findViewById( R.id.bank_angle_10 );
        svBank15 = (SwitchView) view.findViewById( R.id.bank_angle_15 );
        svBank20 = (SwitchView) view.findViewById( R.id.bank_angle_20 );
        svBank25 = (SwitchView) view.findViewById( R.id.bank_angle_25 );
        svBank30 = (SwitchView) view.findViewById( R.id.bank_angle_30 );
        setupBankSelector();
    }

    private void setupBankSelector()
    {
        SwitchView[] bankViews = new SwitchView[]{ svBank10, svBank15, svBank20, svBank25, svBank30 };
        bankSelector = new SwitchViewGroup( bankViews, 4 );
        bankSelector.setOnSwitchViewClickedListener( new OnSwitchViewClickedListener()
        {
            @Override
            public void onSwitchViewClicked( int keySelected, int keyClicked )
            {
                int diff = keyClicked - keySelected;
                int turns = Math.abs( diff );
                while( turns > 0 )
                {
                    if( diff < 0 )
                    {
                        commander.sendEvent(
                                EntityId.MCP.toInt(),
                                EventId.MCP_BANK_ANGLE_SELECTOR.toInt(),
                                EventParameter.MOUSE_LEFT_CLICK.toInt() );
                    } else if( diff > 0 )
                    {
                        commander.sendEvent(
                                EntityId.MCP.toInt(),
                                EventId.MCP_BANK_ANGLE_SELECTOR.toInt(),
                                EventParameter.MOUSE_RIGHT_CLICK.toInt() );
                    }

                    turns--;
                }
            }
        } );
    }

    private void setupButton( final Button btn,
                              final EntityId entityId,
                              final EventId eventId,
                              final EventParameter eventParameter )
    {
        btn.setOnTouchListener( new RepeatListener( 400, 100, 20, 10, new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.sendEvent( entityId.toInt(), eventId.toInt(), eventParameter.toInt() );
            }
        } ) );
    }

    private void setupPushButton( final Button btn,
                                  final EntityId entityId,
                                  final EventId eventId,
                                  final EventParameter eventParameter )
    {
        btn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.sendEvent( entityId.toInt(), eventId.toInt(), eventParameter.toInt() );
            }
        } );
    }

    private void setupSwitchView( final SwitchView switchView,
                                  final EntityId entityId,
                                  final EventId eventId,
                                  final EventParameter eventParameter )
    {
        switchView.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.sendEvent( entityId.toInt(), eventId.toInt(), eventParameter.toInt() );
            }
        } );
    }

    private void setValue( SingleValue singleValue )
    {
        int valueId = singleValue.getId();
        int iVal = singleValue.getIntValue();
        boolean bVal = singleValue.getBooleanValue();
        float fVal = singleValue.getFloatValue();

        if( valueId == ValueId.MCP_VERT_SPEED_BLANK.toInt() )
        {
            vsBlank = bVal;
            setVs( vs );
        }
        else if( valueId == ValueId.MCP_IAS_BLANK.toInt() )
        {
            iasBlank = bVal;
            setIas( ias );
        }
        else if( valueId == ValueId.MCP_FD_SW_L.toInt() )
        {
            if( bVal )
                svFdL.setText( getString( R.string.fd_switch_l_on ) );
            else
                svFdL.setText( getString( R.string.fd_switch_l_off ) );
        }
        else if( valueId == ValueId.MCP_FD_SW_R.toInt() )
        {
            if( bVal )
                svFdR.setText( getString( R.string.fd_switch_r_on ) );
            else
                svFdR.setText( getString( R.string.fd_switch_r_off ) );
        }
        else if( valueId == ValueId.MCP_HEADING.toInt() )
            txtHdg.setText( String.valueOf( iVal ) );
        else if( valueId == ValueId.MCP_ALTITUDE.toInt() )
            txtAlt.setText( String.valueOf( iVal ) );
        else if( valueId == ValueId.MCP_VERT_SPEED.toInt() )
            setVs( iVal );
        else if( valueId == ValueId.MCP_IAS_MACH.toInt() )
            setIas( fVal );
        else if( valueId == ValueId.MCP_COURSE_L.toInt() )
            txtCrsL.setText( String.valueOf( iVal ) );
        else if( valueId == ValueId.MCP_COURSE_R.toInt() )
            txtCrsR.setText( String.valueOf( iVal ) );

        else if( valueId == ValueId.MCP_ANNUN_LNAV.toInt() )
            svLnav.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_HDG_SEL.toInt() )
            svHdgSel.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_VOR_LOC.toInt() )
            svVorLoc.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_VNAV.toInt() )
            svVnav.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_ALT_HOLD.toInt() )
            svAltHld.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_VS.toInt() )
            svVs.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_LVL_CHG.toInt() )
            svLvlChg.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_AT_ARM.toInt() )
            svAtArm.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_N1.toInt() )
            svN1.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_SPEED.toInt() )
            svSpeed.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_APP.toInt() )
            svApp.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_CMD_A.toInt() )
            svCmdA.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_CMD_B.toInt() )
            svCmdB.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_CWS_A.toInt() )
            svCwsA.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_CWS_B.toInt() )
            svCwsB.setOn( bVal );
        else if( valueId == ValueId.MCP_DISENGAGE_BAR.toInt() )
            svDisengangeBar.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_FD_L.toInt() )
            svFdL.setOn( bVal );
        else if( valueId == ValueId.MCP_ANNUN_FD_R.toInt() )
            svFdR.setOn( bVal );

        else if( valueId == ValueId.MCP_BANK_LIMIT_SEL.toInt() )
            bankSelector.select( iVal );
    }

    private void setIas( float ias )
    {
        this.ias = ias;

        if( iasBlank )
            txtIas.setText( BLANK );
        else
        {
            // knots
            if( ias >= 100.0f )
                txtIas.setText( String.valueOf( (int) ias ) );
                // mach
            else
                txtIas.setText( String.valueOf( ias ) );
        }
    }

    private void setVs( int vs )
    {
        this.vs = vs;

        if( vsBlank || vs == VS_BLANK_VALUE )
            txtVs.setText( BLANK );
        else
            txtVs.setText( String.valueOf( vs ) );
    }
}
