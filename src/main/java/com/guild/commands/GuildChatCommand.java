package com.guild.commands;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildChatCommand implements CommandExecutor {
    
    private final GuildPlugin plugin;
    
    public GuildChatCommand(GuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此命令只能由玩家执行");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "用法: /guildchat <消息>");
            return true;
        }
        
        Guild guild = plugin.getGuildManager().getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return true;
        }
        
        // 检查是否被禁言
        if (guild.getMember(player.getUniqueId()).isMuted()) {
            player.sendMessage(ChatColor.RED + "你已被禁言，无法在公会频道发言");
            return true;
        }
        
        String message = String.join(" ", args);
        guild.broadcast(ChatColor.translateAlternateColorCodes('&', guild.getTagColor() + "[" + guild.getTag() + "] ") + 
                ChatColor.WHITE + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message);
        
        return true;
    }
}