package com.pbad.ngx_mcp.networking;

import com.pbad.ngx_mcp.networking.Protocol.DataPacket;

/**
 * Created by phili on 05.12.2016.
 */

public interface OnDataReceivedListener
{
    void onDataReceived( DataPacket packet );
}
