package com.pbad.ngx_mcp;

/**
 * Created by philipp on 08.12.16.
 */

public interface SimConnectCommander
{
    void connect();
    boolean requestAllValuesFromAllEntities();
    boolean sendEvent( int entityId, int eventId, int eventParameter );
}
