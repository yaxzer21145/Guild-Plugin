package com.guild.listeners;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatInputListener implements Listener {
    
    private final GuildPlugin plugin;
    private final Map<UUID, ChatInput> pendingInputs;
    
    public ChatInputListener(GuildPlugin plugin) {
        this.plugin = plugin;
        this.pendingInputs = new HashMap<>();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        if (!pendingInputs.containsKey(uuid)) {
            return;
        }
        
        event.setCancelled(true);
        
        ChatInput input = pendingInputs.get(uuid);
        String message = event.getMessage();
        
        if (message.equalsIgnoreCase("C")) {
            player.sendMessage(ChatColor.YELLOW + "已取消操作");
            pendingInputs.remove(uuid);
            return;
        }
        
        if (System.currentTimeMillis() > input.getExpireTime()) {
            player.sendMessage(ChatColor.RED + "操作已超时");
            pendingInputs.remove(uuid);
            return;
        }
        
        switch (input.getType()) {
            case RENAME_GUILD:
                handleRenameGuild(player, input.getGuild(), message);
                break;
            case CHANGE_TAG:
                handleChangeTag(player, input.getGuild(), message);
                break;
            case SET_NICKNAME:
                handleSetNickname(player, input.getGuild(), input.getTargetUuid(), message);
                break;
            case PROMOTE_MEMBER:
                handlePromoteMember(player, input.getGuild(), message);
                break;
            case DEMOTE_MEMBER:
                handleDemoteMember(player, input.getGuild(), message);
                break;
            case CONFIRM_DISBAND:
                handleConfirmDisband(player, input.getGuild(), message);
                break;
            case CONFIRM_KICK:
                handleConfirmKick(player, input.getGuild(), input.getTargetUuid(), message);
                break;
        }
        
        pendingInputs.remove(uuid);
    }
    
    private void handleRenameGuild(Player player, Guild guild, String newName) {
        if (newName.length() < 3 || newName.length() > 16) {
            player.sendMessage(ChatColor.RED + "公会名称长度必须在3到16之间");
            return;
        }
        
        guild.setName(newName);
        plugin.getDatabaseManager().saveGuild(guild);
        player.sendMessage(ChatColor.GREEN + "公会已重命名为: " + newName);
        guild.broadcast(ChatColor.YELLOW + "公会已重命名为: " + newName);
    }
    
    private void handleChangeTag(Player player, Guild guild, String newTag) {
        if (newTag.length() > 4) {
            player.sendMessage(ChatColor.RED + "标签长度不能超过4个字符");
            return;
        }
        
        guild.setTag(newTag);
        plugin.getDatabaseManager().saveGuild(guild);
        player.sendMessage(ChatColor.GREEN + "公会标签已更改为: " + newTag);
        guild.broadcast(ChatColor.YELLOW + "公会标签已更改为: " + newTag);
    }
    
    private void handleSetNickname(Player player, Guild guild, UUID targetUuid, String nickname) {
        GuildMember member = guild.getMember(targetUuid);
        if (member == null) {
            player.sendMessage(ChatColor.RED + "该玩家不在公会中");
            return;
        }
        
        member.setNickname(nickname);
        plugin.getDatabaseManager().saveGuild(guild);
        player.sendMessage(ChatColor.GREEN + "已设置 " + Bukkit.getOfflinePlayer(targetUuid).getName() + " 的昵称为: " + nickname);
    }
    
    private void handlePromoteMember(Player player, Guild guild, String playerName) {
        UUID targetUuid = getTargetUuidFromInput(player, guild);
        if (targetUuid == null) {
            player.sendMessage(ChatColor.RED + "无法找到目标玩家");
            return;
        }
        
        if (!guild.isMember(targetUuid)) {
            player.sendMessage(ChatColor.RED + "该玩家不在公会中");
            return;
        }
        
        if (plugin.getGuildManager().promoteMember(guild.getName(), targetUuid, player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已将 " + Bukkit.getOfflinePlayer(targetUuid).getName() + " 升为管理员");
            guild.broadcast(ChatColor.YELLOW + Bukkit.getOfflinePlayer(targetUuid).getName() + " 被升为管理员");
        } else {
            player.sendMessage(ChatColor.RED + "升职失败，你没有权限或该成员已是最高职位");
        }
    }
    
    private void handleDemoteMember(Player player, Guild guild, String playerName) {
        UUID targetUuid = getTargetUuidFromInput(player, guild);
        if (targetUuid == null) {
            player.sendMessage(ChatColor.RED + "无法找到目标玩家");
            return;
        }
        
        if (!guild.isMember(targetUuid)) {
            player.sendMessage(ChatColor.RED + "该玩家不在公会中");
            return;
        }
        
        if (plugin.getGuildManager().demoteMember(guild.getName(), targetUuid, player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已将 " + Bukkit.getOfflinePlayer(targetUuid).getName() + " 降为普通成员");
            guild.broadcast(ChatColor.YELLOW + Bukkit.getOfflinePlayer(targetUuid).getName() + " 被降为普通成员");
        } else {
            player.sendMessage(ChatColor.RED + "降职失败，你没有权限或该成员已是最低职位");
        }
    }
    
    private UUID getTargetUuidFromInput(Player player, Guild guild) {
        ChatInput input = pendingInputs.get(player.getUniqueId());
        if (input != null && input.getTargetUuid() != null) {
            return input.getTargetUuid();
        }
        return null;
    }
    
    private void handleConfirmDisband(Player player, Guild guild, String message) {
        if (!message.equalsIgnoreCase("confirm")) {
            player.sendMessage(ChatColor.RED + "已取消解散公会");
            return;
        }
        
        plugin.getGuildManager().disbandGuild(guild.getName());
        player.sendMessage(ChatColor.GREEN + "公会已解散");
        guild.broadcast(ChatColor.RED + "公会已被解散");
    }
    
    private void handleConfirmKick(Player player, Guild guild, UUID targetUuid, String message) {
        if (!message.equalsIgnoreCase("confirm")) {
            player.sendMessage(ChatColor.RED + "已取消踢出操作");
            return;
        }
        
        String targetName = Bukkit.getOfflinePlayer(targetUuid).getName();
        if (plugin.getGuildManager().kickMember(guild.getName(), targetUuid, player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已将 " + targetName + " 踢出公会");
            guild.broadcast(ChatColor.YELLOW + targetName + " 被踢出了公会");
            
            Player targetPlayer = Bukkit.getPlayer(targetUuid);
            if (targetPlayer != null && targetPlayer.isOnline()) {
                targetPlayer.sendMessage(ChatColor.RED + "你已被踢出公会");
            }
        } else {
            player.sendMessage(ChatColor.RED + "踢出失败");
        }
    }
    
    public void addPendingInput(UUID uuid, ChatInput input) {
        pendingInputs.put(uuid, input);
    }
    
    public void removePendingInput(UUID uuid) {
        pendingInputs.remove(uuid);
    }
    
    public static class ChatInput {
        private final InputType type;
        private final Guild guild;
        private final UUID targetUuid;
        private final long expireTime;
        
        public ChatInput(InputType type, Guild guild, UUID targetUuid) {
            this.type = type;
            this.guild = guild;
            this.targetUuid = targetUuid;
            this.expireTime = System.currentTimeMillis() + 60000;
        }
        
        public InputType getType() {
            return type;
        }
        
        public Guild getGuild() {
            return guild;
        }
        
        public UUID getTargetUuid() {
            return targetUuid;
        }
        
        public long getExpireTime() {
            return expireTime;
        }
    }
    
    public enum InputType {
        RENAME_GUILD,
        CHANGE_TAG,
        SET_NICKNAME,
        PROMOTE_MEMBER,
        DEMOTE_MEMBER,
        CONFIRM_DISBAND,
        CONFIRM_KICK
    }
}
