package com.guild.gui;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.utils.VersionCompat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuildBankGUI {
    
    public static void openBankGUI(GuildPlugin plugin, Player player, Guild guild) {
        String title = ChatColor.translateAlternateColorCodes('&', 
                plugin.getGUIConfig().getBankTitle(guild.getName()));
        Inventory inv = Bukkit.createInventory(null, 45, title);
        
        long balance = guild.getBank().getBalance();
        
        ItemStack balanceItem = createItem(VersionCompat.getGoldIngotMaterial(),
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBankBalanceName()),
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBankBalanceLore(balance)));
        inv.setItem(plugin.getGUIConfig().getBankBalanceSlot(), balanceItem);
        
        ItemStack depositItem = createItem(VersionCompat.getEmeraldMaterial(),
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBankDepositName()),
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBankDepositLore()));
        inv.setItem(plugin.getGUIConfig().getBankDepositSlot(), depositItem);
        
        ItemStack withdrawItem = createItem(VersionCompat.getRedstoneMaterial(), 
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBankWithdrawName()),
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBankWithdrawLore()));
        inv.setItem(plugin.getGUIConfig().getBankWithdrawSlot(), withdrawItem);
        
        ItemStack backItem = createItem(VersionCompat.getArrowMaterial(), 
                ChatColor.translateAlternateColorCodes('&', plugin.getGUIConfig().getBackName()),
                plugin.getGUIConfig().getBackLore().toArray(new String[0]));
        inv.setItem(plugin.getGUIConfig().getBankBackSlot(), backItem);
        
        ItemStack glass = createItem(VersionCompat.getGrayStainedGlassPaneMaterial(),
                " ", "");
        for (int i = 0; i < 45; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }
        
        player.openInventory(inv);
    }
    
    private static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        if (lore.length > 0 && !lore[0].isEmpty()) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(loreList);
        }
        
        item.setItemMeta(meta);
        return item;
    }
}
