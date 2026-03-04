package com.guild.config;

import com.guild.GuildPlugin;
import com.guild.utils.VersionCompat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class GUIConfig {
    
    private final GuildPlugin plugin;
    private FileConfiguration config;
    
    public GUIConfig(GuildPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        reloadConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    
    public String getMainTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.main", "&6公会系统"));
    }
    
    public String getNoGuildTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.no_guild", "&6公会系统"));
    }
    
    public String getAllGuildsTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.all_guilds", "&6所有公会"));
    }
    
    public String getMemberTitle(String guildName) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.member", "&6公会: &e{name}").replace("{name}", guildName));
    }
    
    public String getOfficerTitle(String guildName) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.officer", "&6公会管理: &e{name}").replace("{name}", guildName));
    }
    
    public String getOwnerTitle(String guildName) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.owner", "&6公会会长: &e{name}").replace("{name}", guildName));
    }
    
    public int getMainSize() {
        return config.getInt("gui.size.main", 54);
    }
    
    public int getAllGuildsSize() {
        return config.getInt("gui.size.all_guilds", 54);
    }
    
    private Material getMaterialSafe(String configPath, String... fallbacks) {
        String configValue = config.getString(configPath);
        if (configValue != null) {
            try {
                return Material.valueOf(configValue);
            } catch (IllegalArgumentException ignored) {
            }
        }
        for (String fallback : fallbacks) {
            try {
                return Material.valueOf(fallback);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Material.STONE;
    }
    
    public Material getNoGuildBarrierMaterial() {
        return getMaterialSafe("gui.items.no_guild.barrier.material", "BARRIER", "BEDROCK");
    }
    
    public String getNoGuildBarrierName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.no_guild.barrier.name", "&c你还没有公会"));
    }
    
    public List<String> getNoGuildBarrierLore() {
        return config.getStringList("gui.items.no_guild.barrier.lore");
    }
    
    public Material getCreateMaterial() {
        return getMaterialSafe("gui.items.no_guild.create.material", "DIAMOND", "EMERALD");
    }
    
    public String getCreateName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.no_guild.create.name", "&a创建公会"));
    }
    
    public List<String> getCreateLore() {
        return config.getStringList("gui.items.no_guild.create.lore");
    }
    
    public Material getViewAllMaterial() {
        return getMaterialSafe("gui.items.no_guild.view_all.material", "BOOK", "PAPER");
    }
    
    public String getViewAllName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.no_guild.view_all.name", "&e查看所有公会"));
    }
    
    public List<String> getViewAllLore() {
        return config.getStringList("gui.items.no_guild.view_all.lore");
    }
    
    public Material getGuildItemMaterial() {
        return getMaterialSafe("gui.items.all_guilds.guild_item.material", "DIAMOND_BLOCK", "EMERALD_BLOCK");
    }
    
    public List<String> getGuildItemLore() {
        return config.getStringList("gui.items.all_guilds.guild_item.lore");
    }
    
    public Material getBackMaterial() {
        return getMaterialSafe("gui.items.all_guilds.back.material", "ARROW", "STICK");
    }
    
    public String getBackName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.all_guilds.back.name", "&c返回"));
    }
    
    public List<String> getBackLore() {
        return config.getStringList("gui.items.all_guilds.back.lore");
    }
    
    public Material getInfoMaterial() {
        return getMaterialSafe("gui.items.member.info.material", "PAPER", "BOOK");
    }
    
    public String getInfoName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.info.name", "&e公会信息"));
    }
    
    public List<String> getInfoLore() {
        return config.getStringList("gui.items.member.info.lore");
    }
    
    public Material getMembersMaterial() {
        return VersionCompat.getPlayerHeadMaterial();
    }
    
    public String getMembersName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.members.name", "&e公会成员"));
    }
    
    public List<String> getMembersLore() {
        return config.getStringList("gui.items.member.members.lore");
    }
    
    public Material getSettingsMaterial() {
        return getMaterialSafe("gui.items.member.settings.material", "REDSTONE", "REDSTONE_BLOCK");
    }
    
    public String getSettingsName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.settings.name", "&e个人设置"));
    }
    
    public List<String> getSettingsLore() {
        return config.getStringList("gui.items.member.settings.lore");
    }
    
    public Material getLeaveMaterial() {
        return getMaterialSafe("gui.items.member.leave.material", "BARRIER", "BEDROCK");
    }
    
    public String getLeaveName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.leave.name", "&c离开公会"));
    }
    
    public List<String> getLeaveLore() {
        return config.getStringList("gui.items.member.leave.lore");
    }
    
    public Material getInviteToggleMaterial() {
        return getMaterialSafe("gui.items.member.invite_toggle.material", "LEVER", "STICK");
    }
    
    public String getInviteToggleName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.invite_toggle.name", "&e公会邀请"));
    }
    
    public List<String> getInviteToggleLore() {
        return config.getStringList("gui.items.member.invite_toggle.lore");
    }
    
    public Material getNotifyToggleMaterial() {
        return getMaterialSafe("gui.items.member.notify_toggle.material", "NOTE_BLOCK", "JUKEBOX");
    }
    
    public String getNotifyToggleName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.notify_toggle.name", "&e上下线通知"));
    }
    
    public List<String> getNotifyToggleLore() {
        return config.getStringList("gui.items.member.notify_toggle.lore");
    }
    
    public Material getManageMaterial() {
        return getMaterialSafe("gui.items.officer.manage.material", "COMMAND_BLOCK", "COMMAND", "BEDROCK");
    }
    
    public String getManageName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.officer.manage.name", "&c管理公会"));
    }
    
    public List<String> getManageLore() {
        return config.getStringList("gui.items.officer.manage.lore");
    }
    
    public String getOwnerColor() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.colors.owner", "&c"));
    }
    
    public String getOfficerColor() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.colors.officer", "&6"));
    }
    
    public String getMemberColor() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.colors.member", "&a"));
    }
    
    public String getOnlineColor() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.colors.online", "&a"));
    }
    
    public String getOfflineColor() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.colors.offline", "&c"));
    }
}
