package com.pbad.ngx_mcp;

import android.view.View;

/**
 * Created by phili on 22.12.2016.
 */

public class SwitchViewGroup
{
    private SwitchView[] members = null;
    private OnSwitchViewClickedListener onSwitchViewClickedListener;

    private int keySelected = -1;

    public SwitchViewGroup( SwitchView[] members, int firstKeySelected )
    {
        this.members = members;

        setMembers( members );
        select( firstKeySelected );
    }

    private void setMembers( SwitchView[] members )
    {
        if( members.length == 0 )
            throw new IllegalArgumentException( "Array must at least contain one element!" );

        for( int i = 0; i < members.length; i++ )
        {
            SwitchView member = members[ i ];

            if( member == null )
                throw new IllegalArgumentException( "Array must not contain null elements!" );

            final int keyClicked = i;
            member.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick( View v )
                {
                    if( keyClicked != keySelected )
                    {
                        if( onSwitchViewClickedListener != null )
                            onSwitchViewClickedListener.onSwitchViewClicked( keySelected, keyClicked );
                    }
                }
            } );
        }
    }

    public void select( int newKeySelected )
    {
        if( newKeySelected < 0 || newKeySelected >= members.length )
            throw new IllegalArgumentException( "Invalid key: '" + newKeySelected + "'!" );

        if( newKeySelected == keySelected )
            return;

        for( int i = 0; i < members.length; i++ )
        {
            if( i == newKeySelected )
                members[ i ].setOn( true );
            else
                members[ i ].setOn( false );
        }

        keySelected = newKeySelected;
    }

    public void setOnSwitchViewClickedListener( OnSwitchViewClickedListener onSwitchViewClickedListener )
    {
        this.onSwitchViewClickedListener = onSwitchViewClickedListener;
    }
}
