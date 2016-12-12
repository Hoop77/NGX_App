package com.pbad.ngx_mcp;

import android.app.Activity;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.phili.ngx_mcp.R;
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

        if( valueId == ValueId.ANNUN_HDG_SEL.toInt() )
        {
            ToggleButton btn = (ToggleButton) activity.findViewById( R.id.hdg_sel_switch );
            boolean bVal = singleValue.getBooleanValue();
            btn.setChecked( bVal );
        }
        else if( valueId == ValueId.HEADING.toInt() )
        {
            TextView txt = (TextView) activity.findViewById( R.id.heading_value );
            int iVal = singleValue.getIntValue();
            txt.setText( String.valueOf( iVal ) );
        }
    }
}
