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
    
    public String getBankTitle(String guildName) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.title.bank", "&6公会银行: &e{name}").replace("{name}", guildName));
    }
    
    public String getBankBalanceName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.bank.balance.name", "&e当前余额"));
    }
    
    public String getBankBalanceLore(long balance) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.bank.balance.lore", "&f余额: &a{balance}").replace("{balance}", String.valueOf(balance)));
    }
    
    public String getBankDepositName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.bank.deposit.name", "&a存入资金"));
    }
    
    public String getBankDepositLore() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.bank.deposit.lore", "&f点击存入资金到公会银行"));
    }
    
    public String getBankWithdrawName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.bank.withdraw.name", "&c取出资金"));
    }
    
    public String getBankWithdrawLore() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.bank.withdraw.lore", "&f点击从公会银行取出资金"));
    }
    
    public String getBankName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("gui.items.member.bank.name", "&e公会银行"));
    }
    
    public List<String> getBankLore() {
        return config.getStringList("gui.items.member.bank.lore");
    }
    
    // Slot配置方法
    public int getNoGuildBarrierSlot() {
        return config.getInt("gui.items.no_guild.barrier.slot", 22);
    }
    
    public int getCreateSlot() {
        return config.getInt("gui.items.no_guild.create.slot", 11);
    }
    
    public int getViewAllSlot() {
        return config.getInt("gui.items.no_guild.view_all.slot", 15);
    }
    
    public int getBackSlot() {
        return config.getInt("gui.items.all_guilds.back.slot", 49);
    }
    
    public int getInfoSlot() {
        return config.getInt("gui.items.member.info.slot", 10);
    }
    
    public int getMembersSlot() {
        return config.getInt("gui.items.member.members.slot", 13);
    }
    
    public int getSettingsSlot() {
        return config.getInt("gui.items.member.settings.slot", 16);
    }
    
    public int getLeaveSlot() {
        return config.getInt("gui.items.member.leave.slot", 31);
    }
    
    public int getInviteToggleSlot() {
        return config.getInt("gui.items.member.invite_toggle.slot", 28);
    }
    
    public int getNotifyToggleSlot() {
        return config.getInt("gui.items.member.notify_toggle.slot", 34);
    }
    
    public int getManageSlot() {
        return config.getInt("gui.items.officer.manage.slot", 37);
    }
    
    public int getUpgradeSlot() {
        return config.getInt("gui.items.owner.upgrade.slot", 19);
    }
    
    public int getBuyExpSlot() {
        return config.getInt("gui.items.owner.buy_exp.slot", 25);
    }
    
    public int getBankSlot() {
        return config.getInt("gui.items.member.bank.slot", 22);
    }
    
    // Bank GUI slots
    public int getBankBalanceSlot() {
        return config.getInt("gui.items.bank.balance.slot", 13);
    }
    
    public int getBankDepositSlot() {
        return config.getInt("gui.items.bank.deposit.slot", 20);
    }
    
    public int getBankWithdrawSlot() {
        return config.getInt("gui.items.bank.withdraw.slot", 24);
    }
    
    public int getBankBackSlot() {
        return config.getInt("gui.items.bank.back.slot", 40);
    }
}
