package com.pbad.ngx_mcp.networking.connectionStateManaging;

/**
 * Created by phili on 07.11.2016.
 */

public class Connection
{
    public enum State
    {
        CONNECTED,
        DISCONNECTED
    }

    public interface OnStateChangedListener
    {
        void onStateChanged( State newState );
    }

    protected int id;
    protected String userId;
    protected State state;
    protected ConnectionManager manager;
    protected OnStateChangedListener onStateChangedListener;

    protected Connection( int id, String userId, ConnectionManager manager )
    {
        this.id = id;
        this.userId = userId;
        this.state = State.DISCONNECTED;
        this.manager = manager;
    }

    public void setState( State newState )
    {
        State oldState = state;
        if( oldState != newState )
        {
            state = newState;
            if( onStateChangedListener != null )
                onStateChangedListener.onStateChanged( newState );
            manager.connectionStateChanged( this );
        }
    }

    public void setOnConnectionStateChangedListener( OnStateChangedListener onStateChangedListener )
    {
        this.onStateChangedListener = onStateChangedListener;
    }

    public State getState() { return state; }
    public String getUserId() { return userId; }
}