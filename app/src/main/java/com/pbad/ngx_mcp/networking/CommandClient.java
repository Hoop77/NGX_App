package com.pbad.ngx_mcp.networking;

import com.pbad.ngx_mcp.networking.connectionStateManaging.Connection;

import java.net.InetAddress;

/**
 * Created by phili on 07.11.2016.
 */

public class CommandClient implements Runnable
{
    private InetAddress serverAddress;
    private int port;
    private Connection connection;

    private boolean running = false;
    private Thread thread;

    public CommandClient( InetAddress serverAddress, int port, Connection connection )
    {
        this.serverAddress = serverAddress;
        this.port = port;
        this.connection = connection;

        thread = new Thread( this );
    }

    public void start()
    {
        thread.start();
    }

    @Override
    public void run()
    {

    }
}
