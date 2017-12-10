package com.redmancometh.redxs.networking;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.redmancometh.redcore.RedCore;
import com.redmancometh.redxs.RedXS;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.RedirectResult;

public class LilyUtil
{
    private static Set<String> serverList;

    public static void setServerList(Set<String> servers)
    {
        serverList = servers;
    }

    public static Set<String> getServerList()
    {
        return serverList;
    }

    public static void redirectRequest(String server, final Player player)
    {
        try
        {
            Connect c = Bukkit.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
            c.request(new RedirectRequest(server, player.getName())).registerListener(new FutureResultListener<RedirectResult>()
            {
                public void onResult(RedirectResult redirectResult)
                {
                    if (redirectResult.getStatusCode() == StatusCode.SUCCESS)
                    {
                        return;
                    }
                    player.sendMessage("Could not connect");
                }
            });
        }
        catch (Exception exception)
        {
            player.sendMessage("Could not connect");
        }
    }

    public static void sendToServer(String channel, String msg, String serverName)
    {
        CompletableFuture.runAsync(() ->
        {
            if (!serverName.equals(getServerName()))
            {
                Request request = null;
                try
                {
                    request = new MessageRequest(serverName, channel, msg);
                    getConnect().request(request).await().getStatusCode();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, RedCore.getInstance().getPool());

    }

    public static void sendToServers(String channel, String msg, String... serverNames)
    {
        CompletableFuture.runAsync(() ->
        {
            for (String serverName : serverNames)
            {
                if (!serverName.equals(getServerName()))
                {
                    Request request = null;
                    try
                    {
                        request = new MessageRequest(serverName, channel, msg);
                        getConnect().request(request).await().getStatusCode();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, RedCore.getInstance().getPool());

    }

    public static void sendToAllServers(String channel, String msg)
    {
        CompletableFuture.runAsync(() ->
        {
            //String serverName = LilyEssentials.getServerName();
            for (String serverName : getServerList())
            {
                if (!serverName.equals(getServerName()))
                {
                    Request request = null;
                    try
                    {
                        request = new MessageRequest(serverName, channel, msg);
                        getConnect().request(request).await().getStatusCode();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, RedCore.getInstance().getPool());
    }

    public static Connect getConnect()
    {
        return Bukkit.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
    }

    public static void sendMessage(String message)
    {
        CompletableFuture.runAsync(() -> sendToAllServers("chat", message), RedCore.getInstance().getPool());
    }

    public void processMessage()
    {

    }

    public static String getServerName()
    {
        return RedXS.getCurrentServer();
    }

    public static void sendMessage(String channelName, String message)
    {
        CompletableFuture.runAsync(() -> sendToAllServers(channelName, "Server: " + message), RedCore.getInstance().getPool());
    }

}
