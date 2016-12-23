package com.pbad.ngx_mcp;

import android.app.Activity;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pbad.ngx_mcp.global.EntityId;
import com.pbad.ngx_mcp.global.SingleValue;
import com.pbad.ngx_mcp.global.ValueId;

/**
 * Created by phili on 07.11.2016.
 */

public class ValueSetter
{
    private Activity activity;

    public ValueSetter( Activity activity )
    {
        this.activity = activity;
    }

    public void setValue( final int entityId, final int valueId, final int value )
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                SingleValue singleValue = new SingleValue( valueId, value );
                if( entityId == EntityId.MCP.toInt() )
                {
                    setValueOfMcp( singleValue );
                }
            }
        });
    }

    public void setAllValues( int entityId, int[] values )
    {
        for( int id = 0; id < values.length; id++ )
        {
            setValue( entityId, id, values[ id ] );
        }
    }

    private void setValueOfMcp( SingleValue singleValue )
    {
        int valueId = singleValue.getId();
        int iVal = singleValue.getIntValue();
        boolean bVal = singleValue.getBooleanValue();

        if( valueId == ValueId.MCP_ANNUN_HDG_SEL.toInt() )
        {
            SwitchView switchView = (SwitchView) activity.findViewById( R.id.hdg_sel_switch );
            switchView.setOn( bVal );
        }
        else if( valueId == ValueId.MCP_HEADING.toInt() )
        {
            TextView txt = (TextView) activity.findViewById( R.id.heading_value );
            txt.setText( String.valueOf( iVal ) );
        }
        else if( valueId == ValueId.MCP_BANK_LIMIT_SEL.toInt() )
        {
            setBank( iVal );
        }
    }

    private void setBank( int bankValue )
    {
        ToggleButton bank10 = (ToggleButton) activity.findViewById( R.id.bank_angle_10 );
        ToggleButton bank15 = (ToggleButton) activity.findViewById( R.id.bank_angle_15 );
        ToggleButton bank20 = (ToggleButton) activity.findViewById( R.id.bank_angle_20 );
        ToggleButton bank25 = (ToggleButton) activity.findViewById( R.id.bank_angle_25 );
        ToggleButton bank30 = (ToggleButton) activity.findViewById( R.id.bank_angle_30 );
        ToggleButton[] bankBtns = new ToggleButton[] { bank10, bank15, bank20, bank25, bank30 };

        for( int i = 0; i < bankBtns.length; i++ )
        {
            if( i == bankValue )
                bankBtns[ i ].setChecked( true );
            else
                bankBtns[ i ].setChecked( false );
        }
    }
}
