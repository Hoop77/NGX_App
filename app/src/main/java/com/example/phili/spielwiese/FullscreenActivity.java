package com.example.phili.spielwiese;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.phili.spielwiese.networking.CommandClient;
import com.example.phili.spielwiese.networking.connectionStateManaging.Connection;
import com.example.phili.spielwiese.networking.connectionStateManaging.ConnectionStateManager;
import com.example.phili.spielwiese.networking.connectionStateManaging.ConnectionStateManagerContext;
import com.example.phili.spielwiese.networking.NotificationClient;
import com.example.phili.spielwiese.networking.connectionStateManaging.OnAllAreConnectedListener;
import com.example.phili.spielwiese.networking.connectionStateManaging.OnAllAreDisconnectedListener;
import com.example.phili.spielwiese.networking.connectionStateManaging.OnOneIsConnectedListener;
import com.example.phili.spielwiese.networking.connectionStateManaging.OnOneIsConnectingListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity
{
    private ProgressDialog connectingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        // Dialog showing if someone tries to connect.
        connectingDialog = new ProgressDialog( this );
        connectingDialog.setMessage( "Connecting... Please wait." );
        connectingDialog.setIndeterminate( true );
        connectingDialog.setCancelable( false );

        // Managing connection events.
        ConnectionStateManager connectionStateManager = setupConnectionStateManager();
        Connection notificationClientConnection = connectionStateManager.addConnection( "Connection 1" );

        try
        {
            InetAddress serverAddress = InetAddress.getByName( "192.168.178.29" );

            NotificationClient notificationClient = new NotificationClient(
                    serverAddress , 7653, notificationClientConnection, this
            );

            notificationClient.start();
        }
        catch( UnknownHostException e ) {}

        //setupButtons(  );
    }

    private ConnectionStateManager setupConnectionStateManager()
    {
        ConnectionStateManagerContext context = new ConnectionStateManagerContext();
        ConnectionStateManager manager = new ConnectionStateManager( context );

        manager.setOnAllAreDisconnectedListener( new OnAllAreDisconnectedListener()
        {
            @Override
            public void onAllAreDisconnected( ConnectionStateManagerContext context, Connection connection )
            {
                showDisconnected();
            }
        } );

        manager.setOnOneIsConnectingListener( new OnOneIsConnectingListener()
        {
            @Override
            public void onOneIsConnecting( ConnectionStateManagerContext context, Connection connection )
            {
                showConnecting();
            }
        } );

        manager.setOnOneIsConnectedListener( new OnOneIsConnectedListener()
        {
            @Override
            public void onOneIsConnected( ConnectionStateManagerContext context, Connection connection )
            {
                showOneConnected( connection.getUserId() );
            }
        } );

        manager.setOnAllAreConnectedListener( new OnAllAreConnectedListener()
        {
            @Override
            public void onAllAreConnected( ConnectionStateManagerContext context, Connection connection )
            {
                showAllConnected();
            }
        } );

        return manager;
    }

    private void showConnecting()
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                connectingDialog.show();
            }
        } );
    }

    private void showOneConnected( final String who )
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(
                        FullscreenActivity.this,
                        who + " connected!",
                        Toast.LENGTH_LONG
                ).show();
            }
        } );
    }

    private void showAllConnected()
    {
        connectingDialog.dismiss();

        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(
                        FullscreenActivity.this,
                        "Connection established!",
                        Toast.LENGTH_LONG
                ).show();
            }
        } );
    }

    private void showDisconnected()
    {
        connectingDialog.dismiss();

        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(
                        FullscreenActivity.this,
                        "Disconnected!",
                        Toast.LENGTH_LONG
                ).show();
            }
        } );
    }

    private void setupButtons( CommandClient commandClient )
    {
        final ToggleButton hdgSelSwitch = (ToggleButton) findViewById( R.id.hdg_sel_switch );
        hdgSelSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
}
