package com.guild.commands;

import com.guild.GuildPlugin;
import com.guild.gui.GuildGUI;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildGUICommand implements CommandExecutor {
    
    private final GuildPlugin plugin;
    
    public GuildGUICommand(GuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此命令只能由玩家执行");
            return true;
        }
        
        Player player = (Player) sender;
        GuildGUI.openGUI(plugin, player);
        
        return true;
    }
}