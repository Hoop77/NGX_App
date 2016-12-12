package com.pbad.ngx_mcp.networking.connectionStateManaging;

import java.util.ArrayList;

/**
 * Created by phili on 07.11.2016.
 */

public class ConnectionManager
{
    private ArrayList<Connection> connections = new ArrayList<>();
    private OnStateChangedListener onStateChangedListener;

    private final ManagerState allConnectedState = new AllConnectedState();
    private final ManagerState notAllConnectedState = new NotAllConnectedState();

    private ManagerState currentState = new NotAllConnectedState();

    private class AllConnectedState implements ManagerState
    {
        @Override
        public ManagerState connectionStateChanged( Connection connection )
        {
            if( connection.getState() == Connection.State.DISCONNECTED )
            {
                ManagerState newState = notAllConnectedState;
                if( onStateChangedListener != null )
                    onStateChangedListener.onNotAllConnected();
                return newState;
            }

            return allConnectedState;
        }
    }

    private class NotAllConnectedState implements ManagerState
    {
        @Override
        public ManagerState connectionStateChanged( Connection connection )
        {
            if( connection.getState() == Connection.State.CONNECTED )
            {
                // check if now all are connected
                boolean allAreConnected = true;
                for( Connection conn : connections )
                {
                    if( conn.getState() != Connection.State.CONNECTED )
                    {
                        allAreConnected = false;
                        break;
                    }
                }

                if( allAreConnected )
                {
                    if( onStateChangedListener != null )
                        onStateChangedListener.onAllConnected();
                    return allConnectedState;
                }
            }

            return notAllConnectedState;
        }
    }

    public interface OnStateChangedListener
    {
        void onAllConnected();
        void onNotAllConnected();
    }

    public ConnectionManager() {}

    public synchronized Connection addConnection( String userId )
    {
        int id = connections.size();
        Connection connection = new Connection( id, this );
        connections.add( connection );
        return connection;
    }

    public synchronized void removeConnection( Connection connectionToRemove )
    {
        for( int i = 0; i < connections.size(); i++ )
        {
            Connection connection = connections.get( i );
            if( connection.getId() == connectionToRemove.getId() )
                connections.remove( i );
        }
    }

    protected synchronized void connectionStateChanged( Connection connection )
    {
        currentState = currentState.connectionStateChanged( connection );
    }

    public void setOnStateChangedListener( OnStateChangedListener onStateChangedListener )
    {
        this.onStateChangedListener = onStateChangedListener;
    }

    private interface ManagerState
    {
        ManagerState connectionStateChanged( Connection connection );
    }
}
