package com.pbad.ngx_mcp;

import android.app.Activity;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.phili.ngx_mcp.R;

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

    public void setValue( final SingleValue value )
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                int valueId = value.getId();

                if( valueId == ValueId.ANNUN_HDG_SEL.toInt() )
                {
                    ToggleButton btn = (ToggleButton) activity.findViewById( R.id.hdg_sel_switch );
                    boolean bVal = value.getBooleanValue();
                    btn.setChecked( bVal );
                }
                else if( valueId == ValueId.HEADING.toInt() )
                {
                    TextView txt = (TextView) activity.findViewById( R.id.heading_value );
                    int iVal = value.getIntValue();
                    txt.setText( String.valueOf( iVal ) );
                }
            }
        });
    }
}
