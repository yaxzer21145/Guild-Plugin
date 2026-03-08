package com.guild.papi;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.guild.GuildRole;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildPlaceholderExpansion extends PlaceholderExpansion {
    
    private final GuildPlugin plugin;
    
    public GuildPlaceholderExpansion(GuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getIdentifier() {
        return "guild";
    }
    
    @Override
    public String getAuthor() {
        return "ya_xzer21145";
    }
    
    @Override
    public String getVersion() {
        return "2.0.1";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return null;
        
        UUID uuid = player.getUniqueId();
        Guild guild = plugin.getGuildManager().getPlayerGuild(uuid);
        
        if (params.equalsIgnoreCase("name")) {
            return guild != null ? guild.getName() : "";
        }
        
        if (params.equalsIgnoreCase("tag")) {
            return guild != null ? guild.getTag() : "";
        }
        
        if (params.equalsIgnoreCase("tag_color")) {
            return guild != null ? guild.getTagColor() : "";
        }
        
        if (params.equalsIgnoreCase("level")) {
            return guild != null ? String.valueOf(guild.getLevel()) : "0";
        }
        
        if (params.equalsIgnoreCase("experience")) {
            return guild != null ? String.valueOf(guild.getExperience()) : "0";
        }
        
        if (params.equalsIgnoreCase("required_experience")) {
            return guild != null ? String.valueOf(guild.getRequiredExperience()) : "0";
        }
        
        if (params.equalsIgnoreCase("owner")) {
            if (guild == null) return "";
            return plugin.getServer().getOfflinePlayer(guild.getOwner()).getName();
        }
        
        if (params.equalsIgnoreCase("member_count")) {
            return guild != null ? String.valueOf(guild.getMembers().size()) : "0";
        }
        
        if (params.equalsIgnoreCase("motd")) {
            return guild != null ? guild.getMotd() : "";
        }
        
        if (params.equalsIgnoreCase("role")) {
            if (guild == null) return "";
            GuildMember member = guild.getMember(uuid);
            return member != null ? member.getRole().getDisplayName() : "";
        }
        
        if (params.equalsIgnoreCase("role_level")) {
            if (guild == null) return "0";
            GuildMember member = guild.getMember(uuid);
            return member != null ? String.valueOf(member.getRole().getLevel()) : "0";
        }
        
        if (params.equalsIgnoreCase("contribution")) {
            if (guild == null) return "0";
            GuildMember member = guild.getMember(uuid);
            return member != null ? String.valueOf(member.getTotalContribution()) : "0";
        }
        
        if (params.equalsIgnoreCase("daily_contribution")) {
            if (guild == null) return "0";
            GuildMember member = guild.getMember(uuid);
            return member != null ? String.valueOf(member.getDailyContribution()) : "0";
        }
        
        if (params.equalsIgnoreCase("joined_time")) {
            if (guild == null) return "0";
            GuildMember member = guild.getMember(uuid);
            return member != null ? String.valueOf(member.getJoinedTime()) : "0";
        }
        
        if (params.equalsIgnoreCase("is_owner")) {
            if (guild == null) return "false";
            return String.valueOf(guild.getOwner().equals(uuid));
        }
        
        if (params.equalsIgnoreCase("is_officer")) {
            if (guild == null) return "false";
            GuildMember member = guild.getMember(uuid);
            return member != null ? String.valueOf(member.getRole() == GuildRole.OFFICER) : "false";
        }
        
        if (params.equalsIgnoreCase("is_member")) {
            return String.valueOf(guild != null);
        }
        
        if (params.equalsIgnoreCase("bank_balance")) {
            return guild != null ? String.valueOf(guild.getBank().getBalance()) : "0";
        }
        
        if (params.equalsIgnoreCase("currency")) {
            return String.valueOf(plugin.getGuildManager().getPlayerGuildCurrency(uuid));
        }
        
        if (params.equalsIgnoreCase("has_guild")) {
            return String.valueOf(guild != null);
        }
        
        return null;
    }
}