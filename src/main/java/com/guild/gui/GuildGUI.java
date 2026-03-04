package com.guild.gui;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.guild.GuildRole;
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
import java.util.UUID;

public class GuildGUI {
    
    public static void openGUI(GuildPlugin plugin, Player player) {
        Guild guild = plugin.getGuildManager().getPlayerGuild(player.getUniqueId());
        
        if (guild == null) {
            openNoGuildGUI(plugin, player);
        } else {
            GuildMember member = guild.getMember(player.getUniqueId());
            if (member.getRole() == GuildRole.OWNER) {
                openOwnerGUI(plugin, player, guild);
            } else if (member.getRole() == GuildRole.OFFICER) {
                openOfficerGUI(plugin, player, guild);
            } else {
                openMemberGUI(plugin, player, guild);
            }
        }
    }
    
    private static void openNoGuildGUI(GuildPlugin plugin, Player player) {
        int size = plugin.getGUIConfig().getMainSize();
        String title = plugin.getGUIConfig().getNoGuildTitle();
        Inventory inv = Bukkit.createInventory(null, size, title);
        
        ItemStack barrier = createItem(VersionCompat.getBarrierMaterial(), 
                plugin.getGUIConfig().getNoGuildBarrierName(), 
                plugin.getGUIConfig().getNoGuildBarrierLore().toArray(new String[0]));
        inv.setItem(22, barrier);
        
        ItemStack create = createItem(VersionCompat.getDiamondMaterial(), 
                plugin.getGUIConfig().getCreateName(), 
                plugin.getGUIConfig().getCreateLore().toArray(new String[0]));
        inv.setItem(11, create);
        
        ItemStack list = createItem(VersionCompat.getBookMaterial(), 
                plugin.getGUIConfig().getViewAllName(), 
                plugin.getGUIConfig().getViewAllLore().toArray(new String[0]));
        inv.setItem(15, list);
        
        player.openInventory(inv);
    }
    
    private static void openMemberGUI(GuildPlugin plugin, Player player, Guild guild) {
        String title = plugin.getGUIConfig().getMemberTitle(guild.getName());
        Inventory inv = Bukkit.createInventory(null, 54, title);
        
        ItemStack info = createItem(VersionCompat.getPaperMaterial(), 
                plugin.getGUIConfig().getInfoName(), 
                plugin.getGUIConfig().getInfoLore().toArray(new String[0]));
        inv.setItem(10, info);
        
        ItemStack members = createItem(VersionCompat.getPlayerHeadMaterial(), 
                plugin.getGUIConfig().getMembersName(), 
                plugin.getGUIConfig().getMembersLore().toArray(new String[0]));
        inv.setItem(13, members);
        
        ItemStack settings = createItem(VersionCompat.getRedstoneMaterial(), 
                plugin.getGUIConfig().getSettingsName(), 
                plugin.getGUIConfig().getSettingsLore().toArray(new String[0]));
        inv.setItem(16, settings);
        
        ItemStack leave = createItem(VersionCompat.getBarrierMaterial(), 
                plugin.getGUIConfig().getLeaveName(), 
                plugin.getGUIConfig().getLeaveLore().toArray(new String[0]));
        inv.setItem(31, leave);
        
        ItemStack inviteToggle = createItem(VersionCompat.getLeverMaterial(), 
                plugin.getGUIConfig().getInviteToggleName(), 
                plugin.getGUIConfig().getInviteToggleLore().toArray(new String[0]));
        inv.setItem(28, inviteToggle);
        
        ItemStack notifyToggle = createItem(VersionCompat.getNoteBlockMaterial(), 
                plugin.getGUIConfig().getNotifyToggleName(), 
                plugin.getGUIConfig().getNotifyToggleLore().toArray(new String[0]));
        inv.setItem(34, notifyToggle);
        
        int slot = 45;
        for (GuildMember member : guild.getMembers().values()) {
            Player p = Bukkit.getPlayer(member.getUuid());
            boolean online = p != null && p.isOnline();
            String status = online ? ChatColor.GREEN + "在线" : ChatColor.RED + "离线";
            String displayName = member.getNickname() != null ? member.getNickname() : Bukkit.getOfflinePlayer(member.getUuid()).getName();
            
            ItemStack memberItem = VersionCompat.createPlayerHead(member.getUuid(), 
                    member.getRole().getColor() + displayName,
                    ChatColor.GRAY + "职位: " + member.getRole().getDisplayName(),
                    ChatColor.GRAY + "状态: " + status);
            
            inv.setItem(slot, memberItem);
            slot++;
            if (slot > 53) break;
        }
        
        player.openInventory(inv);
    }
    
    private static void openOfficerGUI(GuildPlugin plugin, Player player, Guild guild) {
        String title = plugin.getGUIConfig().getOfficerTitle(guild.getName());
        Inventory inv = Bukkit.createInventory(null, 54, title);
        
        ItemStack info = createItem(VersionCompat.getPaperMaterial(), 
                plugin.getGUIConfig().getInfoName(), 
                plugin.getGUIConfig().getInfoLore().toArray(new String[0]));
        inv.setItem(10, info);
        
        ItemStack members = createItem(VersionCompat.getPlayerHeadMaterial(), 
                plugin.getGUIConfig().getMembersName(), 
                plugin.getGUIConfig().getMembersLore().toArray(new String[0]));
        inv.setItem(13, members);
        
        ItemStack settings = createItem(VersionCompat.getRedstoneMaterial(), 
                plugin.getGUIConfig().getSettingsName(), 
                plugin.getGUIConfig().getSettingsLore().toArray(new String[0]));
        inv.setItem(16, settings);
        
        ItemStack manage = createItem(VersionCompat.getCommandBlockMaterial(), 
                plugin.getGUIConfig().getManageName(), 
                plugin.getGUIConfig().getManageLore().toArray(new String[0]));
        inv.setItem(37, manage);
        
        ItemStack leave = createItem(VersionCompat.getBarrierMaterial(), 
                plugin.getGUIConfig().getLeaveName(), 
                plugin.getGUIConfig().getLeaveLore().toArray(new String[0]));
        inv.setItem(31, leave);
        
        ItemStack inviteToggle = createItem(VersionCompat.getLeverMaterial(), 
                plugin.getGUIConfig().getInviteToggleName(), 
                plugin.getGUIConfig().getInviteToggleLore().toArray(new String[0]));
        inv.setItem(28, inviteToggle);
        
        ItemStack notifyToggle = createItem(VersionCompat.getNoteBlockMaterial(), 
                plugin.getGUIConfig().getNotifyToggleName(), 
                plugin.getGUIConfig().getNotifyToggleLore().toArray(new String[0]));
        inv.setItem(34, notifyToggle);
        
        int slot = 45;
        for (GuildMember member : guild.getMembers().values()) {
            Player p = Bukkit.getPlayer(member.getUuid());
            boolean online = p != null && p.isOnline();
            String status = online ? ChatColor.GREEN + "在线" : ChatColor.RED + "离线";
            String displayName = member.getNickname() != null ? member.getNickname() : Bukkit.getOfflinePlayer(member.getUuid()).getName();
            
            ItemStack memberItem = VersionCompat.createPlayerHead(member.getUuid(), 
                    member.getRole().getColor() + displayName,
                    ChatColor.GRAY + "职位: " + member.getRole().getDisplayName(),
                    ChatColor.GRAY + "状态: " + status);
            
            inv.setItem(slot, memberItem);
            slot++;
            if (slot > 53) break;
        }
        
        player.openInventory(inv);
    }
    
    private static void openOwnerGUI(GuildPlugin plugin, Player player, Guild guild) {
        String title = plugin.getGUIConfig().getOwnerTitle(guild.getName());
        Inventory inv = Bukkit.createInventory(null, 54, title);
        
        ItemStack info = createItem(VersionCompat.getPaperMaterial(), 
                plugin.getGUIConfig().getInfoName(), 
                plugin.getGUIConfig().getInfoLore().toArray(new String[0]));
        inv.setItem(10, info);
        
        ItemStack members = createItem(VersionCompat.getPlayerHeadMaterial(), 
                plugin.getGUIConfig().getMembersName(), 
                plugin.getGUIConfig().getMembersLore().toArray(new String[0]));
        inv.setItem(13, members);
        
        ItemStack settings = createItem(VersionCompat.getRedstoneMaterial(), 
                plugin.getGUIConfig().getSettingsName(), 
                plugin.getGUIConfig().getSettingsLore().toArray(new String[0]));
        inv.setItem(16, settings);
        
        ItemStack manage = createItem(VersionCompat.getCommandBlockMaterial(), 
                plugin.getGUIConfig().getManageName(), 
                plugin.getGUIConfig().getManageLore().toArray(new String[0]));
        inv.setItem(37, manage);
        
        ItemStack leave = createItem(VersionCompat.getBarrierMaterial(), 
                plugin.getGUIConfig().getLeaveName(), 
                plugin.getGUIConfig().getLeaveLore().toArray(new String[0]));
        inv.setItem(31, leave);
        
        ItemStack inviteToggle = createItem(VersionCompat.getLeverMaterial(), 
                plugin.getGUIConfig().getInviteToggleName(), 
                plugin.getGUIConfig().getInviteToggleLore().toArray(new String[0]));
        inv.setItem(28, inviteToggle);
        
        ItemStack notifyToggle = createItem(VersionCompat.getNoteBlockMaterial(), 
                plugin.getGUIConfig().getNotifyToggleName(), 
                plugin.getGUIConfig().getNotifyToggleLore().toArray(new String[0]));
        inv.setItem(34, notifyToggle);
        
        int slot = 45;
        for (GuildMember member : guild.getMembers().values()) {
            Player p = Bukkit.getPlayer(member.getUuid());
            boolean online = p != null && p.isOnline();
            String status = online ? ChatColor.GREEN + "在线" : ChatColor.RED + "离线";
            String displayName = member.getNickname() != null ? member.getNickname() : Bukkit.getOfflinePlayer(member.getUuid()).getName();
            
            ItemStack memberItem = VersionCompat.createPlayerHead(member.getUuid(), 
                    member.getRole().getColor() + displayName,
                    ChatColor.GRAY + "职位: " + member.getRole().getDisplayName(),
                    ChatColor.GRAY + "状态: " + status);
            
            inv.setItem(slot, memberItem);
            slot++;
            if (slot > 53) break;
        }
        
        player.openInventory(inv);
    }
    
    private static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        if (lore.length > 0) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    public static void openGuildListGUI(GuildPlugin plugin, Player player) {
        if (plugin.getGuildManager().getGuilds().isEmpty()) {
            player.sendMessage(ChatColor.RED + "服务器上没有公会");
            return;
        }
        
        int size = plugin.getGUIConfig().getAllGuildsSize();
        String title = plugin.getGUIConfig().getAllGuildsTitle();
        Inventory inv = Bukkit.createInventory(null, size, title);
        
        int slot = 0;
        for (Guild guild : plugin.getGuildManager().getGuilds().values()) {
            ItemStack guildItem = createItem(VersionCompat.getDiamondBlockMaterial(), 
                    ChatColor.YELLOW + guild.getName(),
                    ChatColor.GRAY + "等级: " + guild.getLevel(),
                    ChatColor.GRAY + "成员: " + guild.getMembers().size() + "/" + guild.getMaxMembers(),
                    ChatColor.GRAY + "经验: " + guild.getExperience(),
                    ChatColor.GRAY + "标签: " + (guild.getTag() != null ? guild.getTag() : ChatColor.RED + "无"));
            
            inv.setItem(slot, guildItem);
            slot++;
            if (slot > 44) break;
        }
        
        ItemStack back = createItem(VersionCompat.getArrowMaterial(), 
                plugin.getGUIConfig().getBackName(), 
                plugin.getGUIConfig().getBackLore().toArray(new String[0]));
        inv.setItem(49, back);
        
        player.openInventory(inv);
    }
}
