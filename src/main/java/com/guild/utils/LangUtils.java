package com.guild.utils;

import com.guild.GuildPlugin;
import org.bukkit.ChatColor;

public class LangUtils {
    
    private static GuildPlugin plugin;
    
    public static void initialize(GuildPlugin plugin) {
        LangUtils.plugin = plugin;
    }
    
    public static String get(String key) {
        if (plugin == null) return key;
        String message = plugin.getMessage(key);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String get(String key, String... replacements) {
        if (plugin == null) return key;
        String message = plugin.getMessage(key, replacements);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String prefix(String key) {
        return get("messages.prefix") + get(key);
    }
    
    public static String prefix(String key, String... replacements) {
        return get("messages.prefix") + get(key, replacements);
    }
}
