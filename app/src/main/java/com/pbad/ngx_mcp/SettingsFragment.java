package com.pbad.ngx_mcp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phili.ngx_mcp.R;

/**
 * Created by phili on 10.11.2016.
 */

public class SettingsFragment extends Fragment
{
    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate( R.layout.settings_page, container, false );
    }
}
