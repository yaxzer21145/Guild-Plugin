package com.guild.listeners;

import com.guild.GuildPlugin;
import com.guild.gui.GuildBankGUI;
import com.guild.gui.GuildGUI;
import com.guild.guild.Guild;
import com.guild.guild.GuildManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    
    private final GuildPlugin plugin;
    private final GuildManager guildManager;
    
    public GUIListener(GuildPlugin plugin) {
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInv = event.getClickedInventory();
        
        if (clickedInv == null) return;
        
        if (clickedInv.getType() == InventoryType.PLAYER) {
            String title = getInventoryTitle(event);
            if (title != null && title.contains("公会")) {
                event.setCancelled(true);
                return;
            }
        }
        
        String title = getInventoryTitle(event);
        
        if (title == null) return;
        
        if (title.contains("公会银行") || title.contains("公会: ") || title.contains("公会管理: ") || title.contains("公会会长: ")) {
            event.setCancelled(true);
            
            if (title.contains("公会银行")) {
                handleBankClick(player, event.getCurrentItem(), title);
            } else {
                handleGuildClick(player, event.getCurrentItem(), title);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        String title = getInventoryTitle(event);
        
        if (title == null) return;
        
        if (title.contains("公会银行") || title.contains("公会: ") || title.contains("公会管理: ") || title.contains("公会会长: ")) {
            event.setCancelled(true);
        }
    }
    
    private String getInventoryTitle(InventoryClickEvent event) {
        try {
            Object view = event.getView();
            java.lang.reflect.Method getTitleMethod = view.getClass().getMethod("getTitle");
            return (String) getTitleMethod.invoke(view);
        } catch (Exception e) {
            try {
                Inventory inv = event.getInventory();
                if (inv.getHolder() != null) {
                    return inv.getHolder().toString();
                }
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }
    
    private String getInventoryTitle(InventoryDragEvent event) {
        try {
            Object view = event.getView();
            java.lang.reflect.Method getTitleMethod = view.getClass().getMethod("getTitle");
            return (String) getTitleMethod.invoke(view);
        } catch (Exception e) {
            try {
                Inventory inv = event.getInventory();
                if (inv.getHolder() != null) {
                    return inv.getHolder().toString();
                }
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }
    
    private void handleGuildClick(Player player, ItemStack item, String title) {
        if (item == null || !item.hasItemMeta()) return;
        
        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        
        if (itemName.contains("公会银行")) {
            Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
            if (guild != null) {
                GuildBankGUI.openBankGUI(plugin, player, guild);
            }
        } else if (itemName.contains("升级公会")) {
            Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
            if (guild == null) return;
            
            if (!guild.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "只有公会会长才能升级公会");
                return;
            }
            
            if (guild.getLevel() >= 100) {
                player.sendMessage(ChatColor.RED + "公会已达到最高等级");
                return;
            }
            
            if (guildManager.upgradeGuild(guild.getName(), player.getUniqueId())) {
                long cost = plugin.getCurrencyConfig().getLevelUpCost();
                String currencyName = plugin.getGuildCurrency().formatAmount(cost, plugin.getCurrencyConfig().getCurrencyType());
                player.sendMessage(ChatColor.GREEN + "成功使用 " + currencyName + " 将公会升级到 " + guild.getLevel() + " 级！");
                guild.broadcast(ChatColor.YELLOW + "恭喜！公会在 " + player.getName() + " 的努力下升级到了 " + guild.getLevel() + " 级！");
                player.closeInventory();
            } else {
                long cost = plugin.getCurrencyConfig().getLevelUpCost();
                String currencyName = plugin.getGuildCurrency().formatAmount(cost, plugin.getCurrencyConfig().getCurrencyType());
                player.sendMessage(ChatColor.RED + "升级失败，你可能没有足够的 " + currencyName);
            }
        } else if (itemName.contains("购买经验")) {
            Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
            if (guild == null) return;
            
            if (guildManager.addExperienceWithCurrency(guild.getName(), player.getUniqueId())) {
                int expAmount = plugin.getCurrencyConfig().getExperienceAmount();
                long cost = plugin.getCurrencyConfig().getExperienceCost();
                String currencyName = plugin.getGuildCurrency().formatAmount(cost, plugin.getCurrencyConfig().getCurrencyType());
                player.sendMessage(ChatColor.GREEN + "成功使用 " + currencyName + " 购买了 " + expAmount + " 经验！");
                guild.broadcast(ChatColor.YELLOW + player.getName() + " 使用 " + currencyName + " 为公会购买了 " + expAmount + " 经验");
            } else {
                long cost = plugin.getCurrencyConfig().getExperienceCost();
                String currencyName = plugin.getGuildCurrency().formatAmount(cost, plugin.getCurrencyConfig().getCurrencyType());
                player.sendMessage(ChatColor.RED + "购买失败，你可能没有足够的 " + currencyName);
            }
        }
    }
    
    private void handleBankClick(Player player, ItemStack item, String title) {
        if (item == null || !item.hasItemMeta()) return;
        
        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        
        if (itemName.contains("存入资金")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请输入要存入的金额: /guild deposit <金额>");
        } else if (itemName.contains("取出资金")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请输入要取出的金额: /guild withdraw <金额>");
        } else if (itemName.contains("返回")) {
            Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
            if (guild != null) {
                GuildGUI.openGUI(plugin, player);
            }
        }
    }
}