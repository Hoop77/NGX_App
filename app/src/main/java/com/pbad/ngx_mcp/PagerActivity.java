package com.pbad.ngx_mcp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.pbad.ngx_mcp.global.EntityId;
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

public class PagerActivity extends FragmentActivity implements Commander
{
    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;

    private MCPFragment mcpFragment = new MCPFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    private ConnectionManager connectionManager;
    private NotificationClient notificationClient;
    private CommandClient commandClient;

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
                return mcpFragment;

            return settingsFragment;
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }

    @Override
    public void connect()
    {
        notificationClient.start();
        commandClient.start();
    }

    @Override
    public void connect( InetAddress serverAddress, int commandPort, int notificationPort )
    {
        commandClient.restart( serverAddress, commandPort );
        notificationClient.restart( serverAddress, notificationPort );
    }

    @Override
    public void showSettings()
    {
        viewPager.setCurrentItem( 1, true );
    }

    @Override
    public boolean requestAllValuesFromAllEntities()
    {
        for( int i = 0; i < EntityId.COUNT.toInt(); i++ )
        {
            boolean success = commandClient.requestAllValues( i );
            if( !success )
                return false;
        }

        return true;
    }

    @Override
    public boolean sendEvent( int entityId, int eventId, int eventParameter )
    {
        return commandClient.sendEvent( entityId, eventId, eventParameter );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.pager );

        myPagerAdapter = new MyPagerAdapter( getSupportFragmentManager() );
        viewPager = (ViewPager) findViewById( R.id.pager );
        viewPager.setAdapter( myPagerAdapter );

        setupClients();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void setupClients()
    {
        setupConnectionManager();

        try
        {
            InetAddress serverAddress = InetAddress.getByName( getString( R.string.default_server_ip ) );

            setupCommandClient( connectionManager, serverAddress );
            setupNotificationClient( connectionManager, serverAddress );
        }
        catch( UnknownHostException e ) {}
    }

    private void setupConnectionManager()
    {
        connectionManager = new ConnectionManager();

        connectionManager.setOnStateChangedListener( new ConnectionManager.OnStateChangedListener()
        {
            @Override
            public void onAllConnected()
            {
                showConnected();
            }

            @Override
            public void onNotAllConnected()
            {
                showDisconnected();
            }
        } );
    }

    private void setupCommandClient( ConnectionManager connectionManager, InetAddress serverAddress )
    {
        Connection commandClientConnection = connectionManager.addConnection( "Connection 1" );

        int commandPort = Integer.parseInt( getString( R.string.default_command_port ) );

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
                    final int entityId = allValuesDataPacket.getEntityId();
                    final int[] values = allValuesDataPacket.getValues();
                    for( int i = 0; i < values.length; i++ )
                    {
                        final int finalI = i;
                        runOnUiThread( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mcpFragment.receiveValue( entityId, finalI, values[ finalI ] );
                            }
                        } );
                    }
                }
            }
        } );
    }

    private void setupNotificationClient( ConnectionManager connectionManager, InetAddress serverAddress )
    {
        Connection notificationClientConnection = connectionManager.addConnection( "Connection 2" );
        notificationClientConnection.setOnConnectionStateChangedListener( new Connection.OnStateChangedListener()
        {
            @Override
            public void onStateChanged( Connection.State newState )
            {
                if( newState == Connection.State.CONNECTED )
                {
                    // On reconnect: update all values.
                    requestAllValuesFromAllEntities();
                }
            }
        } );

        int notificationPort = Integer.parseInt( getString( R.string.default_notification_port ) );

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
                    final SingleValueDataPacket singleValueDataPacket = (SingleValueDataPacket) packet;
                    runOnUiThread( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mcpFragment.receiveValue(
                                    singleValueDataPacket.getEntityId(),
                                    singleValueDataPacket.getValueId(),
                                    singleValueDataPacket.getValue()
                            );
                        }
                    } );
                }
            }
        } );
    }

    private void showConnected()
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                mcpFragment.showConnected();
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
                mcpFragment.showDisconnected();
            }
        } );
    }
}
