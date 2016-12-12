package com.pbad.ngx_mcp.networking.connectionStateManaging;

/**
 * Created by phili on 07.11.2016.
 */

public class Connection
{
    private int id;
    private State state;
    private ConnectionManager manager;
    private OnStateChangedListener onStateChangedListener;

    public enum State
    {
        CONNECTED,
        DISCONNECTED
    }

    public interface OnStateChangedListener
    {
        void onStateChanged( State newState );
    }

    protected Connection( int id, ConnectionManager manager )
    {
        this.id = id;
        this.state = State.DISCONNECTED;
        this.manager = manager;
    }

    public synchronized void setState( State newState )
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

    public synchronized State getState() { return state; }

    public int getId()
    {
        return id;
    }

    public void setOnConnectionStateChangedListener( OnStateChangedListener onStateChangedListener )
    {
        this.onStateChangedListener = onStateChangedListener;
    }
}