package com.guild.config;

import com.guild.GuildPlugin;
import com.guild.currency.GuildCurrency;
import org.bukkit.configuration.file.FileConfiguration;

public class CurrencyConfig {
    
    private final GuildPlugin plugin;
    private FileConfiguration config;
    
    public CurrencyConfig(GuildPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }
    
    public GuildCurrency.CurrencyType getCurrencyType() {
        String type = config.getString("currency.type", "DEFAULT");
        try {
            return GuildCurrency.CurrencyType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return GuildCurrency.CurrencyType.DEFAULT;
        }
    }
    
    public long getLevelUpCost() {
        return config.getLong("currency.level_up_cost", 100000);
    }
    
    public long getExperienceCost() {
        return config.getLong("currency.experience_cost", 5000);
    }
    
    public int getExperienceAmount() {
        return config.getInt("currency.experience_amount", 50);
    }
    
    public String getGuildCurrencyName() {
        return config.getString("currency.guild_currency_name", "公会币");
    }
    
    public String getVaultCurrencyName() {
        return config.getString("currency.vault_currency_name", "金币");
    }
    
    public String getPlayerPointsCurrencyName() {
        return config.getString("currency.playerpoints_currency_name", "积分");
    }
    
    public boolean isBankEnabled() {
        return config.getBoolean("currency.bank_enabled", true);
    }
    
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}