package com.redmancometh.redxs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.redmancometh.redxs.RedXS;

import net.md_5.bungee.api.ChatColor;

public class ReloadCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.isOp()) return true;
        sender.sendMessage(ChatColor.GREEN + "Reloading config...");
        RedXS.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reloaded config.");
        return true;
    }

}
