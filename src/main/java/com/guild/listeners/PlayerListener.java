package com.guild.listeners;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final GuildPlugin plugin;
    
    public PlayerListener(GuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Guild guild = plugin.getGuildManager().getPlayerGuild(player.getUniqueId());
        
        if (guild != null) {
            for (com.guild.guild.GuildMember member : guild.getMembers().values()) {
                if (!member.getUuid().equals(player.getUniqueId())) {
                    com.guild.guild.PlayerSettings settings = plugin.getGuildManager().getPlayerSettings(member.getUuid());
                    if (settings.isNotifyOnlineStatus()) {
                        Player memberPlayer = org.bukkit.Bukkit.getPlayer(member.getUuid());
                        if (memberPlayer != null && memberPlayer.isOnline()) {
                            memberPlayer.sendMessage(ChatColor.YELLOW + player.getName() + " 上线了");
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Guild guild = plugin.getGuildManager().getPlayerGuild(player.getUniqueId());
        
        if (guild != null) {
            for (com.guild.guild.GuildMember member : guild.getMembers().values()) {
                if (!member.getUuid().equals(player.getUniqueId())) {
                    com.guild.guild.PlayerSettings settings = plugin.getGuildManager().getPlayerSettings(member.getUuid());
                    if (settings.isNotifyOnlineStatus()) {
                        Player memberPlayer = org.bukkit.Bukkit.getPlayer(member.getUuid());
                        if (memberPlayer != null && memberPlayer.isOnline()) {
                            memberPlayer.sendMessage(ChatColor.YELLOW + player.getName() + " 下线了");
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        
        if (killer != null && killer != player) {
            long exp = plugin.getConfig().getLong("experience.player-kill", 50);
            plugin.getGuildManager().addExperience(killer.getUniqueId(), exp);
        }
    }
}