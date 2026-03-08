package com.guild.currency;

import com.guild.GuildPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildCurrency {
    
    public enum CurrencyType {
        DEFAULT,    // 公会币
        VAULT,      // Vault经济系统
        PLAYERPOINTS // PlayerPoints积分系统
    }
    
    private final GuildPlugin plugin;
    private Economy economy;
    private boolean playerPointsEnabled;
    
    public GuildCurrency(GuildPlugin plugin) {
        this.plugin = plugin;
        initializeVault();
        initializePlayerPoints();
    }
    
    private void initializeVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            org.bukkit.plugin.RegisteredServiceProvider<Economy> rsp = 
                Bukkit.getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
            }
        }
    }
    
    private void initializePlayerPoints() {
        playerPointsEnabled = Bukkit.getPluginManager().isPluginEnabled("PlayerPoints");
    }
    
    public boolean hasVault() {
        return economy != null;
    }
    
    public boolean hasPlayerPoints() {
        return playerPointsEnabled;
    }
    
    public long getBalance(UUID uuid, CurrencyType type) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return 0;
        
        switch (type) {
            case VAULT:
                if (economy != null) {
                    return (long) economy.getBalance(player);
                }
                return 0;
            case PLAYERPOINTS:
                if (playerPointsEnabled) {
                    return getPlayerPoints(player);
                }
                return 0;
            case DEFAULT:
                return plugin.getGuildManager().getPlayerGuildCurrency(uuid);
            default:
                return 0;
        }
    }
    
    public boolean withdraw(UUID uuid, long amount, CurrencyType type) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        
        switch (type) {
            case VAULT:
                if (economy != null && economy.getBalance(player) >= amount) {
                    economy.withdrawPlayer(player, amount);
                    return true;
                }
                return false;
            case PLAYERPOINTS:
                if (playerPointsEnabled && getPlayerPoints(player) >= amount) {
                    return takePlayerPoints(player, amount);
                }
                return false;
            case DEFAULT:
                return plugin.getGuildManager().withdrawPlayerGuildCurrency(uuid, amount);
            default:
                return false;
        }
    }
    
    public boolean deposit(UUID uuid, long amount, CurrencyType type) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        
        switch (type) {
            case VAULT:
                if (economy != null) {
                    economy.depositPlayer(player, amount);
                    return true;
                }
                return false;
            case PLAYERPOINTS:
                if (playerPointsEnabled) {
                    return givePlayerPoints(player, amount);
                }
                return false;
            case DEFAULT:
                return plugin.getGuildManager().depositPlayerGuildCurrency(uuid, amount);
            default:
                return false;
        }
    }
    
    private long getPlayerPoints(Player player) {
        try {
            Class<?> ppClass = Class.forName("org.black_ixx.playerpoints.PlayerPoints");
            Object ppAPI = ppClass.getMethod("getAPI").invoke(null);
            return (long) ppAPI.getClass().getMethod("look", UUID.class).invoke(ppAPI, player.getUniqueId());
        } catch (Exception e) {
            return 0;
        }
    }
    
    private boolean takePlayerPoints(Player player, long amount) {
        try {
            Class<?> ppClass = Class.forName("org.black_ixx.playerpoints.PlayerPoints");
            Object ppAPI = ppClass.getMethod("getAPI").invoke(null);
            return (boolean) ppAPI.getClass().getMethod("take", UUID.class, int.class)
                .invoke(ppAPI, player.getUniqueId(), (int) amount);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean givePlayerPoints(Player player, long amount) {
        try {
            Class<?> ppClass = Class.forName("org.black_ixx.playerpoints.PlayerPoints");
            Object ppAPI = ppClass.getMethod("getAPI").invoke(null);
            return (boolean) ppAPI.getClass().getMethod("give", UUID.class, int.class)
                .invoke(ppAPI, player.getUniqueId(), (int) amount);
        } catch (Exception e) {
            return false;
        }
    }
    
    public String formatAmount(long amount, CurrencyType type) {
        switch (type) {
            case VAULT:
                if (economy != null) {
                    return economy.format(amount);
                }
                return amount + " " + plugin.getCurrencyConfig().getVaultCurrencyName();
            case PLAYERPOINTS:
                return amount + " " + plugin.getCurrencyConfig().getPlayerPointsCurrencyName();
            case DEFAULT:
                return amount + " " + plugin.getCurrencyConfig().getGuildCurrencyName();
            default:
                return String.valueOf(amount);
        }
    }
}