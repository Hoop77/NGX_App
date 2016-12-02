package com.pbad.ngx_mcp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.phili.ngx_mcp.R;
import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;
import com.pbad.ngx_mcp.networking.connectionStateManaging.ConnectionManager;
import com.pbad.ngx_mcp.networking.NotificationClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PagerActivity extends FragmentActivity implements OnViewCreatedListener
{
    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;

    private MainFragment mainFragment;
    private SettingsFragment settingsFragment;

    private NotificationClient notificationClient;

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

        // Managing connection states.
        ConnectionManager connectionStateManager = setupConnectionManager();
        Connection notificationClientConnection = connectionStateManager.addConnection( "Connection 1" );

        try
        {
            InetAddress notificationServerAddress = InetAddress.getByName( getString( R.string.default_server_ip ) );
            notificationClient = new NotificationClient(
                    notificationServerAddress, 7653, notificationClientConnection, this
            );
        }
        catch( UnknownHostException e ) {}
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

    @Override
    public void onViewCreated()
    {
        notificationClient.start();
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
