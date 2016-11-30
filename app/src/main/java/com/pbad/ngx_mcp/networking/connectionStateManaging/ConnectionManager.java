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

    private Object stateLock = new Object();

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

        switch( connection.state )
        {
            case CONNECTED:

                switch( state )
                {
                    case ALL_CONNECTED:
                        // no state change
                        break;

                    case NOT_ALL_CONNECTED:
                        // check if now all are connected
                        boolean allAreConnected = true;
                        for( Connection conn : connections )
                        {
                            if( conn.state != Connection.State.CONNECTED )
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

                        break;
                }

                break;

            case DISCONNECTED:

                switch( state )
                {
                    case ALL_CONNECTED:
                        state = State.NOT_ALL_CONNECTED;
                        stateChanged = true;
                        break;

                    case NOT_ALL_CONNECTED:
                        // no state change
                        break;
                }

                break;
        }

        if( stateChanged && onStateChangedListener != null )
            onStateChangedListener.onStateChanged( state );
    }

    public void setOnStateChangedListener( OnStateChangedListener onStateChangedListener )
    {
        this.onStateChangedListener = onStateChangedListener;
    }
}
