package com.guild.gui;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.utils.VersionCompat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuildManageGUI {
    
    public static void openGUI(GuildPlugin plugin, Player player, Guild guild) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "管理公会: " + guild.getName());
        
        ItemStack rename = VersionCompat.createItem(VersionCompat.getNameTagMaterial(), ChatColor.YELLOW + "重命名公会",
                ChatColor.GRAY + "点击重命名公会",
                ChatColor.GRAY + "在聊天栏输入新名称",
                ChatColor.GRAY + "1分钟内有效，输入C取消");
        inv.setItem(11, rename);
        
        ItemStack tag = VersionCompat.createItem(VersionCompat.getAnvilMaterial(), ChatColor.YELLOW + "更改标签", 
                ChatColor.GRAY + "点击更改公会标签",
                ChatColor.GRAY + "在聊天栏输入新标签",
                ChatColor.GRAY + "1分钟内有效，输入C取消");
        inv.setItem(13, tag);
        
        ItemStack disband = VersionCompat.createItem(VersionCompat.getBarrierMaterial(), ChatColor.RED + "解散公会", 
                ChatColor.GRAY + "点击解散公会",
                ChatColor.RED + "警告: 此操作不可撤销!",
                ChatColor.GRAY + "需要2次确认");
        inv.setItem(15, disband);
        
        ItemStack back = VersionCompat.createItem(VersionCompat.getArrowMaterial(), ChatColor.RED + "返回", 
                ChatColor.GRAY + "返回公会界面");
        inv.setItem(22, back);
        
        player.openInventory(inv);
    }
}
