package com.pbad.ngx_mcp;

import java.net.InetAddress;

/**
 * Created by philipp on 08.12.16.
 */

public interface Commander
{
    void connect();
    void connect( InetAddress serverAddress, int commandPort, int notificationPort );
    void showSettings();
    boolean requestAllValuesFromAllEntities();
    boolean sendEvent( int entityId, int eventId, int eventParameter );
}
