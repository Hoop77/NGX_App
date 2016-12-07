package com.pbad.ngx_mcp.networking.connectionStateManaging;

import java.util.ArrayList;

/**
 * Created by phili on 07.11.2016.
 */

public class ConnectionManager
{
    public enum State
    {
        ALL_CONNECTED,
        NOT_ALL_CONNECTED
    }

    public interface OnStateChangedListener
    {
        void onStateChanged( State newState );
    }

    private ArrayList<Connection> connections;
    private OnStateChangedListener onStateChangedListener;
    private State state = State.NOT_ALL_CONNECTED;

    public ConnectionManager()
    {
        connections = new ArrayList<>();
    }

    public synchronized Connection addConnection( String userId )
    {
        int id = connections.size();
        Connection connection = new Connection( id, userId, this );
        connections.add( connection );
        return connection;
    }

    protected synchronized void connectionStateChanged( Connection connection )
    {
        boolean stateChanged = false;

        if( connection.getState() == Connection.State.CONNECTED
            && state == State.NOT_ALL_CONNECTED )
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
                state = State.ALL_CONNECTED;
                stateChanged = true;
            }
        }
        else if( connection.getState() == Connection.State.DISCONNECTED
            && state == State.ALL_CONNECTED )
        {
            state = State.NOT_ALL_CONNECTED;
            stateChanged = true;
        }

        if( stateChanged && onStateChangedListener != null )
            onStateChangedListener.onStateChanged( state );
    }

    public synchronized void setOnStateChangedListener( OnStateChangedListener onStateChangedListener )
    {
        this.onStateChangedListener = onStateChangedListener;
    }
}
