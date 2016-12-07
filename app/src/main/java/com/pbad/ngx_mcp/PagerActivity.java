package com.pbad.ngx_mcp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.phili.ngx_mcp.R;
import com.pbad.ngx_mcp.global.SingleValue;
import com.pbad.ngx_mcp.networking.CommandClient;
import com.pbad.ngx_mcp.networking.OnDataReceivedListener;
import com.pbad.ngx_mcp.networking.Protocol.AllValuesDataPacket;
import com.pbad.ngx_mcp.networking.Protocol.DataPacket;
import com.pbad.ngx_mcp.networking.Protocol.SingleValueDataPacket;
import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;
import com.pbad.ngx_mcp.networking.connectionStateManaging.ConnectionManager;
import com.pbad.ngx_mcp.networking.NotificationClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PagerActivity extends FragmentActivity
{
    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;

    private MainFragment mainFragment;
    private SettingsFragment settingsFragment;

    private NotificationClient notificationClient;
    private CommandClient commandClient;

    private ValueSetter valueSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.pager );

        mainFragment = new MainFragment();
        settingsFragment = new SettingsFragment();

        // pager and fragments setup
        myPagerAdapter = new MyPagerAdapter( getSupportFragmentManager() );
        viewPager = (ViewPager) findViewById( R.id.pager );
        viewPager.setAdapter( myPagerAdapter );

        valueSetter = new ValueSetter( this );

        setupClients();
    }

    public void setupClients()
    {
        // Managing connection states.
        ConnectionManager connectionManager = setupConnectionManager();

        try
        {
            // address and ports
            InetAddress serverAddress = InetAddress.getByName( getString( R.string.default_server_ip ) );

            setupCommandClient( connectionManager, serverAddress );
            setupNotificationClient( connectionManager, serverAddress );
        }
        catch( UnknownHostException e ) {}
    }

    void setupNotificationClient( ConnectionManager connectionManager, InetAddress serverAddress )
    {
        Connection notificationClientConnection = connectionManager.addConnection( "Connection 1" );
        int notificationPort = Integer.parseInt( getString( R.string.default_port_1 ) );

        // client threads
        notificationClient = new NotificationClient(
                serverAddress,
                notificationPort,
                notificationClientConnection
        );

        notificationClient.setOnDataReceivedListener( new OnDataReceivedListener()
        {
            @Override
            public void onDataReceived( DataPacket packet )
            {
                if( packet instanceof SingleValueDataPacket )
                {
                    SingleValueDataPacket singleValueDataPacket = (SingleValueDataPacket) packet;
                    valueSetter.setValue(
                        singleValueDataPacket.getEntityId(),
                        singleValueDataPacket.getValueId(),
                        singleValueDataPacket.getValue()
                    );
                }
            }
        } );
    }

    void setupCommandClient( ConnectionManager connectionManager, InetAddress serverAddress )
    {
        Connection commandClientConnection = connectionManager.addConnection( "Connection 2" );

        int commandPort = Integer.parseInt( getString( R.string.default_port_2 ) );

        commandClient = new CommandClient(
                serverAddress,
                commandPort,
                commandClientConnection
        );

        commandClient.setOnDataReceivedListener( new OnDataReceivedListener()
        {
            @Override
            public void onDataReceived( DataPacket packet )
            {
                if( packet instanceof AllValuesDataPacket )
                {
                    AllValuesDataPacket allValuesDataPacket = (AllValuesDataPacket) packet;
                    int entityId = allValuesDataPacket.getEntityId();
                    int[] values = allValuesDataPacket.getValues();
                    for( int id = 0; id < values.length; id++ )
                    {
                        valueSetter.setValue( entityId, id, values[ id ] );
                    }
                }
            }
        } );
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //notificationClient.stop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private ConnectionManager setupConnectionManager()
    {
        ConnectionManager manager = new ConnectionManager();

        manager.setOnStateChangedListener( new ConnectionManager.OnStateChangedListener()
        {
            @Override
            public void onStateChanged( ConnectionManager.State newState )
            {
                switch( newState )
                {
                    case ALL_CONNECTED:
                        showConnected();
                        break;

                    case NOT_ALL_CONNECTED:
                        showDisconnected();
                        break;
                }
            }
        } );

        return manager;
    }

    private void showConnected()
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                mainFragment.showConnected();
            }
        } );
    }

    private void showDisconnected()
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                mainFragment.showDisconnected();
            }
        } );
    }

    public void connect()
    {
        notificationClient.start();
        commandClient.start();
    }

    class MyPagerAdapter extends FragmentPagerAdapter
    {
        public MyPagerAdapter( FragmentManager fm )
        {
            super( fm );
        }

        @Override
        public Fragment getItem( int position )
        {
            if( position == 0 )
                return mainFragment;

            return settingsFragment;
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }
}
