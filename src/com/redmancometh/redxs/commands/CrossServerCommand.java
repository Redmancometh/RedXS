package com.redmancometh.redxs.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.redmancometh.redcore.commands.ServerCommand;
import com.redmancometh.redxs.requests.NetworkRequest;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Unfinished
 * 
 * @author Redmancometh
 *
 * @param <T>
 */
public class CrossServerCommand<T extends NetworkRequest> extends ServerCommand implements NetworkRequest<T>
{
    private Consumer<T> receiveCallback;
    private String channel;

    /**
     * The BiConsumer represents the action called when the command is run locally.
     * The Consumer<T> represents the action upon receiving a request of this type
     * 
     * The command is the command without a / that is being registered
     * 
     * The channel is the channel which data is received on in lilypad, and must be unique!
     * 
     * @param command
     * @param channel
     * @param plugin
     */
    public CrossServerCommand(String command, String channel, BiConsumer<CommandSender, String[]> action, Consumer<T> receive, JavaPlugin registering)
    {
        super(command, registering);
        this.receiveCallback = receive;
    }

    /**
     * If this constructor is used super.setAction(BiConsumer<CommandSender, args>) to satisfy ServerCommand
     * You must also supply setReceiveCallback() before any data is received.
     * 
     * The command is the command without a / that is being registered
     * 
     * The channel is the channel which data is received on in lilypad, and must be unique!
     * 
     * @param command
     * @param channel
     * @param plugin
     */
    public CrossServerCommand(String command, String channel, JavaPlugin plugin)
    {
        super(command, plugin);
        this.channel = channel;
    }

    public Consumer<T> getReceiveCallback()
    {
        return receiveCallback;
    }

    public void setReceiveCallback(Consumer<T> receiveCallback)
    {
        this.receiveCallback = receiveCallback;
    }

    @Override
    public void receive(T e)
    {
        this.receiveCallback.accept(e);
    }

    @Override
    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

}
