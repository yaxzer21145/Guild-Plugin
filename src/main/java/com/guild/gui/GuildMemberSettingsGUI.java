package com.guild.gui;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.utils.VersionCompat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GuildMemberSettingsGUI {
    
    private static final Map<UUID, UUID> targetPlayerMap = new ConcurrentHashMap<>();
    
    public static void openGUI(GuildPlugin plugin, Player player, Guild guild, UUID targetUuid) {
        GuildMember targetMember = guild.getMember(targetUuid);
        if (targetMember == null) {
            player.sendMessage(ChatColor.RED + "该玩家不在公会中");
            return;
        }
        
        targetPlayerMap.put(player.getUniqueId(), targetUuid);
        
        String targetName = Bukkit.getOfflinePlayer(targetUuid).getName();
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "玩家设置: " + targetName);
        
        ItemStack info = VersionCompat.createPlayerHead(targetUuid, 
                targetMember.getRole().getColor() + targetName,
                ChatColor.GRAY + "职位: " + targetMember.getRole().getDisplayName());
        inv.setItem(13, info);
        
        ItemStack nickname = VersionCompat.createItem(VersionCompat.getNameTagMaterial(), ChatColor.YELLOW + "设置昵称", 
                ChatColor.GRAY + "点击设置该玩家的公会昵称",
                ChatColor.GRAY + "在聊天栏输入新昵称",
                ChatColor.GRAY + "1分钟内有效，输入C取消");
        inv.setItem(10, nickname);
        
        ItemStack promote = VersionCompat.createItem(VersionCompat.getGoldenHelmetMaterial(), ChatColor.GREEN + "设为管理员", 
                ChatColor.GRAY + "点击将该玩家设为管理员");
        inv.setItem(12, promote);
        
        ItemStack demote = VersionCompat.createItem(VersionCompat.getIronHelmetMaterial(), ChatColor.YELLOW + "降为成员", 
                ChatColor.GRAY + "点击将该玩家降为普通成员");
        inv.setItem(14, demote);
        
        ItemStack kick = VersionCompat.createItem(VersionCompat.getBarrierMaterial(), ChatColor.RED + "踢出公会", 
                ChatColor.GRAY + "点击将该玩家踢出公会",
                ChatColor.RED + "警告: 此操作不可撤销!",
                ChatColor.GRAY + "需要2次确认");
        inv.setItem(16, kick);
        
        ItemStack back = VersionCompat.createItem(VersionCompat.getArrowMaterial(), ChatColor.RED + "返回", 
                ChatColor.GRAY + "返回公会界面");
        inv.setItem(22, back);
        
        player.openInventory(inv);
    }
    
    public static UUID getTargetUuid(Player player) {
        return targetPlayerMap.get(player.getUniqueId());
    }
    
    public static void removeTargetUuid(Player player) {
        targetPlayerMap.remove(player.getUniqueId());
    }
}
