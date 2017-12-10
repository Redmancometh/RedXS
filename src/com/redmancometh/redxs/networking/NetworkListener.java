package com.redmancometh.redxs.networking;

import lilypad.client.connect.api.event.*;

import java.io.UnsupportedEncodingException;

import com.redmancometh.redxs.RedXS;

public class NetworkListener
{
    @EventListener
    public void onMessage(MessageEvent messageEvent)
    {
        if (messageEvent.getSender().equalsIgnoreCase(RedXS.getCurrentServer())) return;
        try
        {
            RedXS.getInstance().getRequestManager().receiveMessage(messageEvent.getChannel(), messageEvent.getMessageAsString());
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}
