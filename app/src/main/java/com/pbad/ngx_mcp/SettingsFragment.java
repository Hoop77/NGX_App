package com.pbad.ngx_mcp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by phili on 10.11.2016.
 */

public class SettingsFragment extends Fragment
{
    private class InvalidIpException extends Exception {}

    private Commander commander;
    private View view;
    private Context context;

    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState )
    {
        view = inflater.inflate( R.layout.settings_fragment, container, false );

        Button btnReconnect = (Button) view.findViewById( R.id.reconnect_btn );
        final EditText txtServerAddress = (EditText) view.findViewById( R.id.server_address );
        final EditText txtCommandPort = (EditText) view.findViewById( R.id.command_port );
        final EditText txtNotificationPort = (EditText) view.findViewById( R.id.notification_port );

        btnReconnect.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                try
                {
                    InetAddress serverAddress = getServerAddress( txtServerAddress );
                    int commandPort = Integer.parseInt( txtCommandPort.getText().toString() );
                    int notificationPort = Integer.parseInt( txtNotificationPort.getText().toString() );

                    commander.connect( serverAddress, commandPort, notificationPort );
                }
                catch( InvalidIpException e )
                {
                    showInvalidIp();
                }
            }
        } );

        return view;
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );

        this.context = context;
        commander = (Commander) context;
    }

    private InetAddress getServerAddress( EditText txtServerAddress ) throws InvalidIpException
    {
        String ip = txtServerAddress.getText().toString();

        boolean ipIsValid = Patterns.IP_ADDRESS.matcher( txtServerAddress.getText().toString() ).matches();
        if( !ipIsValid )
        {
            throw new InvalidIpException();
        }

        InetAddress serverAddress = null;
        try
        {
            serverAddress = InetAddress.getByName( ip );
        }
        catch( UnknownHostException e )
        {
            throw new InvalidIpException();
        }

        return serverAddress;
    }

    private void showInvalidIp()
    {
        Toast.makeText( context, "Invalid IP Address!", Toast.LENGTH_LONG ).show();
    }
}
