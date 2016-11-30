package com.pbad.ngx_mcp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phili.ngx_mcp.R;

/**
 * Created by phili on 10.11.2016.
 */

public class MainFragment extends Fragment
{
    private View view;

    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate( R.layout.main_page, container, false );
        return view;
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
