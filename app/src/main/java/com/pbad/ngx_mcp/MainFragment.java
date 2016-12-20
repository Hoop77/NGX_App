package com.pbad.ngx_mcp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.phili.ngx_mcp.R;
import com.pbad.ngx_mcp.global.EntityId;
import com.pbad.ngx_mcp.global.EventId;
import com.pbad.ngx_mcp.global.EventParameter;

/**
 * Created by phili on 10.11.2016.
 */

public class MainFragment extends Fragment
{
    private View view;
    private SimConnectCommander commander;

    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState )
    {
        super.onCreateView( inflater, container, savedInstanceState );

        this.view = inflater.inflate( R.layout.main_page, container, false );

        showDisconnected();
        commander.connect();

        setupEvents();

        return view;
    }

    private void setupEvents()
    {
        // heading selector
        setupButton( view.findViewById( R.id.heading_plus ), EntityId.MCP, EventId.MCP_HEADING_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        setupButton( view.findViewById( R.id.heading_minus ), EntityId.MCP, EventId.MCP_HEADING_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        // alt selector
        setupButton( view.findViewById( R.id.alt_plus ), EntityId.MCP, EventId.MCP_ALTITUDE_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        setupButton( view.findViewById( R.id.alt_minus ), EntityId.MCP, EventId.MCP_ALTITUDE_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        // vs selector
        setupButton( view.findViewById( R.id.vs_plus ), EntityId.MCP, EventId.MCP_VS_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        setupButton( view.findViewById( R.id.vs_minus ), EntityId.MCP, EventId.MCP_VS_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );
        // ias selector
        setupButton( view.findViewById( R.id.ias_plus ), EntityId.MCP, EventId.MCP_SPEED_SELECTOR, EventParameter.MOUSE_WHEEL_UP );
        setupButton( view.findViewById( R.id.ias_minus ), EntityId.MCP, EventId.MCP_SPEED_SELECTOR, EventParameter.MOUSE_WHEEL_DOWN );

        // lnav switch
        setupToggleButton( view.findViewById( R.id.lnav_switch ), EntityId.MCP, EventId.MCP_LNAV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // hdg_sel switch
        setupToggleButton( view.findViewById( R.id.hdg_sel_switch ), EntityId.MCP, EventId.MCP_HDG_SEL_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // vor_loc switch
        setupToggleButton( view.findViewById( R.id.vor_loc_switch ), EntityId.MCP, EventId.MCP_VOR_LOC_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // vnav switch
        setupToggleButton( view.findViewById( R.id.vnav_switch ), EntityId.MCP, EventId.MCP_VNAV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // alt_intv button
        setupPushButton( view.findViewById( R.id.alt_intv_switch ), EntityId.MCP, EventId.MCP_ALT_INTV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // spd_intv button
        setupPushButton( view.findViewById( R.id.spd_intv_switch ), EntityId.MCP, EventId.MCP_SPD_INTV_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // alt_hld switch
        setupToggleButton( view.findViewById( R.id.alt_hold_switch ), EntityId.MCP, EventId.MCP_ALT_HOLD_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // vs switch
        setupToggleButton( view.findViewById( R.id.vs_switch ), EntityId.MCP, EventId.MCP_VS_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // lvl_chg switch
        setupToggleButton( view.findViewById( R.id.lvl_chg_switch ), EntityId.MCP, EventId.MCP_LVL_CHG_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // at_arm switch
        setupToggleButton( view.findViewById( R.id.at_arm_switch ), EntityId.MCP, EventId.MCP_AT_ARM_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // n1 switch
        setupToggleButton( view.findViewById( R.id.n1_switch ), EntityId.MCP, EventId.MCP_N1_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // speed switch
        setupToggleButton( view.findViewById( R.id.speed_switch ), EntityId.MCP, EventId.MCP_SPEED_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cmd_a switch
        setupToggleButton( view.findViewById( R.id.cmd_a_switch ), EntityId.MCP, EventId.MCP_CMD_A_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cmd_b switch
        setupToggleButton( view.findViewById( R.id.cmd_b_switch ), EntityId.MCP, EventId.MCP_CMD_B_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cws_a switch
        setupToggleButton( view.findViewById( R.id.cws_a_switch ), EntityId.MCP, EventId.MCP_CWS_A_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // cws_b switch
        setupToggleButton( view.findViewById( R.id.cws_b_switch ), EntityId.MCP, EventId.MCP_CWS_B_SWITCH, EventParameter.MOUSE_LEFT_CLICK );
        // disengage bar
        setupPushButton( view.findViewById( R.id.disengage_bar ), EntityId.MCP, EventId.MCP_DISENGAGE_BAR, EventParameter.MOUSE_LEFT_CLICK );
        // fd_l switch
        setupToggleButton( view.findViewById( R.id.fd_switch_l ), EntityId.MCP, EventId.MCP_FD_SWITCH_L, EventParameter.MOUSE_LEFT_CLICK );
        // fd_r switch
        setupToggleButton( view.findViewById( R.id.fd_switch_r ), EntityId.MCP, EventId.MCP_FD_SWITCH_R, EventParameter.MOUSE_LEFT_CLICK );
        // bank switches
        setupBankSwitches();
    }

    private void setupBankSwitches()
    {
        ToggleButton bank10 = (ToggleButton) view.findViewById( R.id.bank_angle_10 );
        ToggleButton bank15 = (ToggleButton) view.findViewById( R.id.bank_angle_15 );
        ToggleButton bank20 = (ToggleButton) view.findViewById( R.id.bank_angle_20 );
        ToggleButton bank25 = (ToggleButton) view.findViewById( R.id.bank_angle_25 );
        ToggleButton bank30 = (ToggleButton) view.findViewById( R.id.bank_angle_30 );
        bank30.setChecked( true );  // default
        final ToggleButton[] bankBtns = new ToggleButton[] { bank10, bank15, bank20, bank25, bank30 };

        for( int i = 0; i < bankBtns.length; i++ )
        {
            final ToggleButton bankBtn = bankBtns[ i ];
            final int bankValue = i;
            bankBtn.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    sendBankEvents( bankValue, bankBtns );
                    preventToggling( bankBtn );
                }
            } );
        }
    }

    private void sendBankEvents( int newBankValue, ToggleButton[] bankBtns )
    {
        int diff = newBankValue - getCurrentBankValue( bankBtns );
        int turns = Math.abs( diff );
        while( turns > 0 )
        {
            if( diff > 0 )
            {
                commander.sendEvent(
                        EntityId.MCP.toInt(),
                        EventId.MCP_BANK_ANGLE_SELECTOR.toInt(),
                        EventParameter.MOUSE_RIGHT_CLICK.toInt() );
            }
            else if( diff < 0 )
            {
                commander.sendEvent(
                        EntityId.MCP.toInt(),
                        EventId.MCP_BANK_ANGLE_SELECTOR.toInt(),
                        EventParameter.MOUSE_LEFT_CLICK.toInt() );
            }

            turns--;
        }
    }

    private int getCurrentBankValue( ToggleButton[] btns )
    {
        for( int i = 0; i < btns.length; i++ )
        {
            if( btns[ i ].isChecked() )
                return i;
        }

        return -1;
    }

    private void setupButton( View btnView,
                              final EntityId entityId,
                              final EventId eventId,
                              final EventParameter eventParameter )
    {
        final Button btn = (Button) btnView;
        btn.setOnTouchListener( new RepeatListener( 400, 100, 20, 10, new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.sendEvent( entityId.toInt(), eventId.toInt(), eventParameter.toInt() );
            }
        } ) );
    }

    private void setupPushButton( View btnView,
                                  final EntityId entityId,
                                  final EventId eventId,
                                  final EventParameter eventParameter )
    {
        final Button btn = (Button) btnView;
        btn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.sendEvent( entityId.toInt(), eventId.toInt(), eventParameter.toInt() );
            }
        } );
    }

    private void setupToggleButton( View btnView,
                                    final EntityId entityId,
                                    final EventId eventId,
                                    final EventParameter eventParameter )
    {
        final ToggleButton btn = (ToggleButton) btnView;
        btn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                commander.sendEvent( entityId.toInt(), eventId.toInt(), eventParameter.toInt() );
                preventToggling( btn );
            }
        } );
    }

    private void preventToggling( ToggleButton btn )
    {
        if( btn.isChecked() )
            btn.setChecked( false );
        else
            btn.setChecked( true );
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );

        commander = (SimConnectCommander) context;
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
        Log.d( "MainFragment", "showDisconnected()" );

        View connectingView = view.findViewById( R.id.connecting_layout );
        connectingView.setVisibility( View.VISIBLE );

        View mainLayout = view.findViewById( R.id.main_layout );
        mainLayout.setClickable( false );
    }
}
