package com.example.phili.spielwiese.networking.connectionStateManaging;

/**
 * Created by phili on 07.11.2016.
 */

public class Connection
{
    protected int id;
    protected String userId;
    protected ConnectionState state;
    protected ConnectionStateManager handler;

    protected Connection( int id, String userId, ConnectionStateManager handler )
    {
        this.id = id;
        this.userId = userId;
        this.state = ConnectionState.DISCONNECTED;
        this.handler = handler;
    }

    public void setState( ConnectionState newState )
    {
        state = newState;
        handler.updateState( this );
    }

    public ConnectionState getState() { return state; }
    public String getUserId() { return userId; }
}