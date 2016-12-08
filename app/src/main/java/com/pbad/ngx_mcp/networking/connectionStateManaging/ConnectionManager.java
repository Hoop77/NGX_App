package com.pbad.ngx_mcp.networking.connectionStateManaging;

import java.util.ArrayList;

/**
 * Created by phili on 07.11.2016.
 */

public class ConnectionManager
{
    private ArrayList<Connection> connections = new ArrayList<>();
    private OnStateChangedListener onStateChangedListener;
    private State state = new NotAllConnectedState();
    private Connection connection;

    public ConnectionManager() {}

    public synchronized Connection addConnection( String userId )
    {
        int id = connections.size();
        Connection connection = new Connection( id, userId, this );
        connections.add( connection );
        return connection;
    }

    protected synchronized void connectionStateChanged( Connection connection )
    {
        this.connection = connection;
        state = state.transition();
    }

    public synchronized void setOnStateChangedListener( OnStateChangedListener onStateChangedListener )
    {
        this.onStateChangedListener = onStateChangedListener;
    }

    public interface State
    {
        State transition();
    }

    public class AllConnectedState implements State
    {
        @Override
        public State transition()
        {
            if( connection.getState() == Connection.State.DISCONNECTED )
            {
                State newState = this;
                if( onStateChangedListener != null )
                    onStateChangedListener.onStateChanged( newState );
                return newState;
            }

            return new AllConnectedState();
        }
    }

    public class NotAllConnectedState implements State
    {
        @Override
        public State transition()
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
                    State newState = this;
                    if( onStateChangedListener != null )
                        onStateChangedListener.onStateChanged( newState );
                    return newState;
                }
            }

            return new NotAllConnectedState();
        }
    }

    public interface OnStateChangedListener
    {
        void onStateChanged( State newState );
    }
}
