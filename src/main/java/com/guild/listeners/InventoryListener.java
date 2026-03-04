package com.guild.listeners;

import com.guild.GuildPlugin;
import com.guild.commands.GuildCommand;
import com.guild.gui.GuildGUI;
import com.guild.gui.GuildManageGUI;
import com.guild.gui.GuildMemberSettingsGUI;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.guild.GuildRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryListener implements Listener {
    
    private final GuildPlugin plugin;
    private ChatInputListener chatInputListener;
    
    public InventoryListener(GuildPlugin plugin) {
        this.plugin = plugin;
        this.chatInputListener = new ChatInputListener(plugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (!title.contains("公会")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String itemName = clicked.getItemMeta().getDisplayName();
        Guild guild = plugin.getGuildManager().getPlayerGuild(player.getUniqueId());
        
        if (title.contains("所有公会")) {
            handleGuildListGUI(player, itemName);
        } else if (title.contains("管理公会")) {
            handleManageGUI(player, guild, itemName);
        } else if (title.contains("玩家设置")) {
            handleMemberSettingsGUI(player, guild, itemName);
        } else if (title.contains("公会系统")) {
            handleNoGuildGUI(player, itemName);
        } else if (guild != null) {
            GuildMember member = guild.getMember(player.getUniqueId());
            if (member.getRole() == GuildRole.OWNER) {
                handleOwnerGUI(player, guild, itemName);
            } else if (member.getRole() == GuildRole.OFFICER) {
                handleOfficerGUI(player, guild, itemName);
            } else {
                handleMemberGUI(player, guild, itemName);
            }
        }
    }
    
    private void handleNoGuildGUI(Player player, String itemName) {
        if (itemName.contains("创建公会")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "使用 /guild create <公会名> [标签] 来创建公会");
        } else if (itemName.contains("查看所有公会")) {
            GuildGUI.openGuildListGUI(plugin, player);
        }
    }
    
    private void handleMemberGUI(Player player, Guild guild, String itemName) {
        if (itemName.contains("离开公会")) {
            player.closeInventory();
            plugin.getGuildManager().leaveGuild(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "你已离开公会");
        } else if (itemName.contains("公会邀请")) {
            boolean enabled = plugin.getGuildManager().togglePlayerInvites(player.getUniqueId());
            player.closeInventory();
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启公会邀请");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭公会邀请");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("上下线通知")) {
            boolean enabled = plugin.getGuildManager().togglePlayerNotify(player.getUniqueId());
            player.closeInventory();
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启上下线通知");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭上下线通知");
            }
            GuildGUI.openGUI(plugin, player);
        } else {
            for (GuildMember member : guild.getMembers().values()) {
                String memberName = Bukkit.getOfflinePlayer(member.getUuid()).getName();
                String displayName = member.getNickname() != null ? member.getNickname() : memberName;
                if (itemName.contains(memberName) || itemName.contains(displayName)) {
                    GuildMemberSettingsGUI.openGUI(plugin, player, guild, member.getUuid());
                    return;
                }
            }
        }
    }
    
    private void handleOfficerGUI(Player player, Guild guild, String itemName) {
        if (itemName.contains("离开公会")) {
            player.closeInventory();
            plugin.getGuildManager().leaveGuild(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "你已离开公会");
        } else if (itemName.contains("管理公会")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "管理功能开发中");
        } else if (itemName.contains("公会邀请")) {
            boolean enabled = plugin.getGuildManager().togglePlayerInvites(player.getUniqueId());
            player.closeInventory();
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启公会邀请");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭公会邀请");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("上下线通知")) {
            boolean enabled = plugin.getGuildManager().togglePlayerNotify(player.getUniqueId());
            player.closeInventory();
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启上下线通知");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭上下线通知");
            }
            GuildGUI.openGUI(plugin, player);
        } else {
            for (GuildMember member : guild.getMembers().values()) {
                String memberName = Bukkit.getOfflinePlayer(member.getUuid()).getName();
                String displayName = member.getNickname() != null ? member.getNickname() : memberName;
                if (itemName.contains(memberName) || itemName.contains(displayName)) {
                    GuildMemberSettingsGUI.openGUI(plugin, player, guild, member.getUuid());
                    return;
                }
            }
        }
    }
    
    private void handleOwnerGUI(Player player, Guild guild, String itemName) {
        if (itemName.contains("离开公会")) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "会长不能离开公会，请先转让会长或解散公会");
        } else if (itemName.contains("管理公会")) {
            GuildManageGUI.openGUI(plugin, player, guild);
        } else if (itemName.contains("公会邀请")) {
            boolean enabled = plugin.getGuildManager().togglePlayerInvites(player.getUniqueId());
            player.closeInventory();
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启公会邀请");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭公会邀请");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("上下线通知")) {
            boolean enabled = plugin.getGuildManager().togglePlayerNotify(player.getUniqueId());
            player.closeInventory();
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启上下线通知");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭上下线通知");
            }
            GuildGUI.openGUI(plugin, player);
        } else {
            for (GuildMember member : guild.getMembers().values()) {
                String memberName = Bukkit.getOfflinePlayer(member.getUuid()).getName();
                String displayName = member.getNickname() != null ? member.getNickname() : memberName;
                if (itemName.contains(memberName) || itemName.contains(displayName)) {
                    GuildMemberSettingsGUI.openGUI(plugin, player, guild, member.getUuid());
                    return;
                }
            }
        }
    }
    
    private void handleGuildListGUI(Player player, String itemName) {
        if (itemName.contains("返回")) {
            GuildGUI.openGUI(plugin, player);
        } else {
            for (Guild guild : plugin.getGuildManager().getGuilds().values()) {
                if (itemName.contains(guild.getName())) {
                    player.closeInventory();
                    plugin.getGuildManager().addRequest(guild.getName(), player.getUniqueId(), player.getName());
                    player.sendMessage(ChatColor.GREEN + "已向公会 " + guild.getName() + " 发送加入申请");
                    guild.broadcast(ChatColor.YELLOW + player.getName() + " 申请加入公会");
                    return;
                }
            }
        }
    }
    
    private void handleManageGUI(Player player, Guild guild, String itemName) {
        if (itemName.contains("返回")) {
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("重命名公会")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入新的公会名称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.RENAME_GUILD, guild, null));
        } else if (itemName.contains("更改标签")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入新的公会标签");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.CHANGE_TAG, guild, null));
        } else if (itemName.contains("解散公会")) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "请输入 'confirm' 确认解散公会");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.CONFIRM_DISBAND, guild, null));
        }
    }
    
    private void handleMemberSettingsGUI(Player player, Guild guild, String itemName) {
        UUID targetUuid = GuildMemberSettingsGUI.getTargetUuid(player);
        
        if (itemName.contains("返回")) {
            GuildMemberSettingsGUI.removeTargetUuid(player);
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("设置昵称")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入新的昵称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.SET_NICKNAME, guild, targetUuid));
        } else if (itemName.contains("设为管理员")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入玩家名称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.PROMOTE_MEMBER, guild, targetUuid));
        } else if (itemName.contains("降为成员")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入玩家名称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.DEMOTE_MEMBER, guild, targetUuid));
        } else if (itemName.contains("踢出公会")) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "请输入 'confirm' 确认踢出该玩家");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.CONFIRM_KICK, guild, targetUuid));
        }
    }
}