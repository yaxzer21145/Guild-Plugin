package com.guild.config;

import com.guild.GuildPlugin;

public class FeatureConfig {
    
    private final GuildPlugin plugin;
    
    public FeatureConfig(GuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    public boolean isBankEnabled() {
        return plugin.getConfig().getBoolean("features.bank-enabled", true);
    }
    
    public boolean isChatEnabled() {
        return plugin.getConfig().getBoolean("features.chat-enabled", true);
    }
    
    public boolean isExperienceEnabled() {
        return plugin.getConfig().getBoolean("features.experience-enabled", true);
    }
    
    public boolean isLevelEnabled() {
        return plugin.getConfig().getBoolean("features.level-enabled", true);
    }
    
    public boolean isMotdEnabled() {
        return plugin.getConfig().getBoolean("features.motd-enabled", true);
    }
    
    public boolean isTagEnabled() {
        return plugin.getConfig().getBoolean("features.tag-enabled", true);
    }
    
    public boolean isNicknameEnabled() {
        return plugin.getConfig().getBoolean("features.nickname-enabled", true);
    }
    
    public boolean isNotificationEnabled() {
        return plugin.getConfig().getBoolean("features.notification-enabled", true);
    }
    
    public boolean isGuiEnabled() {
        return plugin.getConfig().getBoolean("features.gui-enabled", true);
    }
}
