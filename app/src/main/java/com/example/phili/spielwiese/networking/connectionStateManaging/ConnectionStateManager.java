package com.example.phili.spielwiese.networking.connectionStateManaging;

import java.util.ArrayList;

/**
 * Created by phili on 07.11.2016.
 */

public class ConnectionStateManager
{
    private ConnectionStateManagerContext context;
    private ArrayList<Connection> connections;

    private OnOneIsDisconnectedListener onOneIsDisconnectedListener;
    private OnAllAreDisconnectedListener onAllAreDisconnectedListener;
    private OnOneIsConnectingListener onOneIsConnectingListener;
    private OnOneIsConnectedListener onOneIsConnectedListener;
    private OnAllAreConnectedListener onAllAreConnectedListener;

    public ConnectionStateManager( ConnectionStateManagerContext context )
    {
        this.context = context;

        connections = new ArrayList<>();
    }

    public synchronized Connection addConnection( String userId )
    {
        int id = connections.size();
        Connection connection = new Connection( id, userId, this );
        connections.add( connection );
        return connection;
    }

    protected synchronized void updateState( Connection connection )
    {
        if( connection.state == ConnectionState.DISCONNECTED )
        {
            if( onOneIsDisconnectedListener != null )
                onOneIsDisconnectedListener.onOneIsDisconnected( context, connection );

            if( onAllAreDisconnectedListener != null )
            {
                boolean allAreDisconnected = true;

                for( Connection conn : connections )
                {
                    if( conn.state != ConnectionState.DISCONNECTED )
                    {
                        allAreDisconnected = false;
                        break;
                    }
                }

                if( allAreDisconnected )
                    onAllAreDisconnectedListener.onAllAreDisconnected( context, connection );
            }
        }
        else if( connection.state == ConnectionState.CONNECTING )
        {
            if( onOneIsConnectingListener != null )
                onOneIsConnectingListener.onOneIsConnecting( context, connection );
        }
        else if( connection.state == ConnectionState.CONNECTED )
        {
            if( onOneIsConnectedListener != null )
                onOneIsConnectedListener.onOneIsConnected( context, connection );

            if( onAllAreConnectedListener != null )
            {
                boolean allAreConnected = true;

                for( Connection conn : connections )
                {
                    if( conn.state != ConnectionState.CONNECTED )
                    {
                        allAreConnected = false;
                        break;
                    }
                }

                if( allAreConnected )
                    onAllAreConnectedListener.onAllAreConnected( context, connection );
            }
        }
    }

    public void setOnOneIsDisconnectedListener( OnOneIsDisconnectedListener onOneIsDisconnectedListener )
    {
        this.onOneIsDisconnectedListener = onOneIsDisconnectedListener;
    }

    public void setOnAllAreDisconnectedListener( OnAllAreDisconnectedListener onAllAreDisconnectedListener )
    {
        this.onAllAreDisconnectedListener = onAllAreDisconnectedListener;
    }

    public void setOnOneIsConnectingListener( OnOneIsConnectingListener onOneIsConnectingListener )
    {
        this.onOneIsConnectingListener = onOneIsConnectingListener;
    }

    public void setOnOneIsConnectedListener( OnOneIsConnectedListener onOneIsConnectedListener )
    {
        this.onOneIsConnectedListener = onOneIsConnectedListener;
    }

    public void setOnAllAreConnectedListener( OnAllAreConnectedListener onAllAreConnectedListener )
    {
        this.onAllAreConnectedListener = onAllAreConnectedListener;
    }
}
