package com.pbad.ngx_mcp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.phili.ngx_mcp.R;

/**
 * Created by philipp on 20.12.16.
 */

public class SwitchView extends View
{
    private static final int[] STATE_SWITCH_ON = { R.attr.state_switch_on };

    private boolean on = false;

    // TODO add a SwitchViewSelector!

    public SwitchView( Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes )
    {
        super( context, attrs, defStyleAttr, defStyleRes );
    }

    @Override
    protected int[] onCreateDrawableState( int extraSpace )
    {
        if( on )
        {
            // add our custom state
            final int[] drawableState = super.onCreateDrawableState( extraSpace + 1 );
            mergeDrawableStates( drawableState, STATE_SWITCH_ON );
            return drawableState;
        }
        else
        {
            return super.onCreateDrawableState( extraSpace );
        }
    }

    public void setOn( boolean on )
    {
        if( this.on != on )
        {
            this.on = on;
            refreshDrawableState();
        }
    }

    public boolean getOn()
    {
        return on;
    }
}
