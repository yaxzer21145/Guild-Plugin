package com.guild.listeners;

import com.guild.GuildPlugin;
import com.guild.gui.GuildBankGUI;
import com.guild.gui.GuildGUI;
import com.guild.gui.GuildManageGUI;
import com.guild.gui.GuildMemberSettingsGUI;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.guild.GuildRole;
import org.bukkit.Bukkit;
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

import java.util.UUID;

public class InventoryListener implements Listener {
    
    private final GuildPlugin plugin;
    private ChatInputListener chatInputListener;
    
    public InventoryListener(GuildPlugin plugin) {
        this.plugin = plugin;
        this.chatInputListener = new ChatInputListener(plugin);
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
        
        if (title == null || !title.contains("公会")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String itemName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
        Guild guild = plugin.getGuildManager().getPlayerGuild(player.getUniqueId());
        
        if (title.contains("公会银行")) {
            handleBankClick(player, guild, itemName);
        } else if (title.contains("所有公会")) {
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
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        String title = getInventoryTitle(event);
        
        if (title == null || !title.contains("公会")) return;
        
        event.setCancelled(true);
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
    
    private void handleNoGuildGUI(Player player, String itemName) {
        if (itemName.contains("创建公会")) {
            player.sendMessage(ChatColor.YELLOW + "使用 /guild create <公会名> [标签] 来创建公会");
        } else if (itemName.contains("查看所有公会")) {
            GuildGUI.openGuildListGUI(plugin, player);
        }
    }
    
    private void handleBankClick(Player player, Guild guild, String itemName) {
        if (itemName.contains("存入资金")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请输入要存入的金额: /guild deposit <金额>");
        } else if (itemName.contains("取出资金")) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "请输入要取出的金额: /guild withdraw <金额>");
        } else if (itemName.contains("返回")) {
            if (guild != null) {
                GuildGUI.openGUI(plugin, player);
            }
        }
    }
    
    private void handleMemberGUI(Player player, Guild guild, String itemName) {
        if (itemName.contains("离开公会")) {
            plugin.getGuildManager().leaveGuild(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "你已离开公会");
        } else if (itemName.contains("公会邀请")) {
            boolean enabled = plugin.getGuildManager().togglePlayerInvites(player.getUniqueId());
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启公会邀请");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭公会邀请");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("上下线通知")) {
            boolean enabled = plugin.getGuildManager().togglePlayerNotify(player.getUniqueId());
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启上下线通知");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭上下线通知");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("公会银行")) {
            GuildBankGUI.openBankGUI(plugin, player, guild);
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
            plugin.getGuildManager().leaveGuild(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "你已离开公会");
        } else if (itemName.contains("管理公会")) {
            GuildManageGUI.openGUI(plugin, player, guild);
        } else if (itemName.contains("公会邀请")) {
            boolean enabled = plugin.getGuildManager().togglePlayerInvites(player.getUniqueId());
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启公会邀请");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭公会邀请");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("上下线通知")) {
            boolean enabled = plugin.getGuildManager().togglePlayerNotify(player.getUniqueId());
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启上下线通知");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭上下线通知");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("公会银行")) {
            GuildBankGUI.openBankGUI(plugin, player, guild);
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
            player.sendMessage(ChatColor.RED + "会长不能离开公会，请先转让会长或解散公会");
        } else if (itemName.contains("管理公会")) {
            GuildManageGUI.openGUI(plugin, player, guild);
        } else if (itemName.contains("公会邀请")) {
            boolean enabled = plugin.getGuildManager().togglePlayerInvites(player.getUniqueId());
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启公会邀请");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭公会邀请");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("上下线通知")) {
            boolean enabled = plugin.getGuildManager().togglePlayerNotify(player.getUniqueId());
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "已开启上下线通知");
            } else {
                player.sendMessage(ChatColor.RED + "已关闭上下线通知");
            }
            GuildGUI.openGUI(plugin, player);
        } else if (itemName.contains("公会银行")) {
            GuildBankGUI.openBankGUI(plugin, player, guild);
        } else if (itemName.contains("升级公会")) {
            if (!guild.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "只有公会会长才能升级公会");
                return;
            }
            
            if (guild.getLevel() >= 100) {
                player.sendMessage(ChatColor.RED + "公会已达到最高等级");
                return;
            }
            
            if (plugin.getGuildManager().upgradeGuild(guild.getName(), player.getUniqueId())) {
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
            if (plugin.getGuildManager().addExperienceWithCurrency(guild.getName(), player.getUniqueId())) {
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
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入新的公会名称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.RENAME_GUILD, guild, null));
        } else if (itemName.contains("更改标签")) {
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入新的公会标签");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.CHANGE_TAG, guild, null));
        } else if (itemName.contains("解散公会")) {
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
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入新的昵称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.SET_NICKNAME, guild, targetUuid));
        } else if (itemName.contains("设为管理员")) {
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入玩家名称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.PROMOTE_MEMBER, guild, targetUuid));
        } else if (itemName.contains("降为成员")) {
            player.sendMessage(ChatColor.YELLOW + "请在聊天栏输入玩家名称");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.DEMOTE_MEMBER, guild, targetUuid));
        } else if (itemName.contains("踢出公会")) {
            player.sendMessage(ChatColor.RED + "请输入 'confirm' 确认踢出该玩家");
            player.sendMessage(ChatColor.GRAY + "1分钟内有效，输入C取消");
            chatInputListener.addPendingInput(player.getUniqueId(), 
                    new ChatInputListener.ChatInput(ChatInputListener.InputType.CONFIRM_KICK, guild, targetUuid));
        }
    }
}