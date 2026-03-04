package com.guild.commands;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.guild.GuildManager;
import com.guild.guild.GuildRole;
import com.guild.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuildCommand implements CommandExecutor {
    
    private final GuildPlugin plugin;
    private final GuildManager guildManager;
    
    public GuildCommand(GuildPlugin plugin) {
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此命令只能由玩家执行");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "help":
                sendHelp(player);
                break;
            case "create":
                handleCreate(player, args);
                break;
            case "join":
                handleJoin(player, args);
                break;
            case "leave":
                handleLeave(player);
                break;
            case "info":
                handleInfo(player);
                break;
            case "list":
                handleList(player);
                break;
            case "listguilds":
                handleListGuilds(player);
                break;
            case "online":
                handleOnline(player);
                break;
            case "invite":
                handleInvite(player, args);
                break;
            case "accept":
                handleAccept(player, args);
                break;
            case "decline":
                handleDecline(player);
                break;
            case "kick":
                handleKick(player, args);
                break;
            case "promote":
                handlePromote(player, args);
                break;
            case "demote":
                handleDemote(player, args);
                break;
            case "transfer":
                handleTransfer(player, args);
                break;
            case "disband":
                handleDisband(player);
                break;
            case "tag":
                handleTag(player, args);
                break;
            case "tagcolor":
                handleTagColor(player, args);
                break;
            case "motd":
                handleMotd(player, args);
                break;
            case "chat":
                handleChat(player, args);
                break;
            case "officerchat":
                handleOfficerChat(player, args);
                break;
            case "top":
                handleTop(player, args);
                break;
            case "log":
                handleLog(player, args);
                break;
            case "requests":
                handleRequests(player, args);
                break;
            case "member":
                handleMember(player, args);
                break;
            case "settings":
                handleSettings(player, args);
                break;
            case "permission":
                handlePermission(player, args);
                break;
            case "party":
                handleParty(player);
                break;
            case "rename":
                handleRename(player, args);
                break;
            case "gexp":
                handleGExp(player, args);
                break;
            case "slevel":
                handleSLevel(player, args);
                break;
            case "clxplev":
                handleClxpLev(player, args);
                break;
            case "delete":
                handleDelete(player, args);
                break;
            case "reload":
                handleReload(player, args);
                break;
            default:
                sendHelp(player);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "--------------------公会---------------------");
        player.sendMessage(ChatColor.WHITE + "/guild leave" + ChatColor.GRAY + " - 离开你的公会");
        player.sendMessage(ChatColor.WHITE + "/guild list" + ChatColor.GRAY + " - 查看公会中的成员");
        player.sendMessage(ChatColor.WHITE + "/guild listguilds" + ChatColor.GRAY + " - 查看所有公会");
        player.sendMessage(ChatColor.WHITE + "/guild create <公会> [标签]" + ChatColor.GRAY + " - 创建公会");
        player.sendMessage(ChatColor.WHITE + "/guild tag <标签>" + ChatColor.GRAY + " - 设置公会标签");
        player.sendMessage(ChatColor.WHITE + "/guild join <公会>" + ChatColor.GRAY + " - 请求加入一个公会");
        player.sendMessage(ChatColor.WHITE + "/guild info" + ChatColor.GRAY + " - 查看你的公会信息");
        player.sendMessage(ChatColor.WHITE + "/guild motd help" + ChatColor.GRAY + " - 显示公会公告帮助");
        player.sendMessage(ChatColor.WHITE + "/guild settings <属性> <值>" + ChatColor.GRAY + " - 修改公会设置");
        player.sendMessage(ChatColor.WHITE + "/guild tagcolor <颜色序号>" + ChatColor.GRAY + " - 设置公会标签颜色");
        player.sendMessage(ChatColor.WHITE + "/guild accept <玩家>" + ChatColor.GRAY + " - 接受公会邀请/申请");
        player.sendMessage(ChatColor.WHITE + "/guild transfer <成员>" + ChatColor.GRAY + " - 转让公会会长");
        player.sendMessage(ChatColor.WHITE + "/guild party" + ChatColor.GRAY + " - 向公会在线玩家发起组队邀请");
        player.sendMessage(ChatColor.WHITE + "/guild role help" + ChatColor.GRAY + " - 显示公会职位帮助");
        player.sendMessage(ChatColor.WHITE + "/guild log <页码>" + ChatColor.GRAY + " - 查看公会日志");
        player.sendMessage(ChatColor.WHITE + "/guild requests <页码>" + ChatColor.GRAY + " - 查看公会申请列表");
        player.sendMessage(ChatColor.WHITE + "/guild promote <玩家>" + ChatColor.GRAY + " - 提升公会成员职位");
        player.sendMessage(ChatColor.WHITE + "/guild officerchat <聊天>" + ChatColor.GRAY + " - 发送聊天信息至公会管理频道");
        player.sendMessage(ChatColor.WHITE + "/guild top <页码>" + ChatColor.GRAY + " - 查看今天公会经验贡献排行榜");
        player.sendMessage(ChatColor.WHITE + "/guild member <成员>" + ChatColor.GRAY + " - 查看公会成员信息");
        player.sendMessage(ChatColor.WHITE + "/guild disband" + ChatColor.GRAY + " - 解散公会");
        player.sendMessage(ChatColor.WHITE + "/guild chat <聊天>" + ChatColor.GRAY + " - 公会聊天");
        player.sendMessage(ChatColor.WHITE + "/guild permission" + ChatColor.GRAY + " - 设置公会权限");
        player.sendMessage(ChatColor.WHITE + "/guild demote <玩家>" + ChatColor.GRAY + " - 降低公会成员职位");
        player.sendMessage(ChatColor.WHITE + "/guild kick <玩家> <原因>" + ChatColor.GRAY + " - 从公会中踢出一名玩家");
        player.sendMessage(ChatColor.WHITE + "/guild rename <新名称>" + ChatColor.GRAY + " - 重命名你的公会名称(需审核)");
        player.sendMessage(ChatColor.WHITE + "/guild online" + ChatColor.GRAY + " - 查看公会中在线的成员");
        player.sendMessage(ChatColor.WHITE + "/guild invite <玩家>" + ChatColor.GRAY + " - 邀请玩家加入到你的公会");
        
        if (player.hasPermission("guild.admin")) {
            player.sendMessage(ChatColor.RED + "-----------------公会ADMIN------------------");
            player.sendMessage(ChatColor.WHITE + "/guild gexp <公会> <数量>" + ChatColor.GRAY + " - 给予指定公会经验");
            player.sendMessage(ChatColor.WHITE + "/guild slevel <公会> <数量>" + ChatColor.GRAY + " - 设置指定公会等级");
            player.sendMessage(ChatColor.WHITE + "/guild clxplev <公会>" + ChatColor.GRAY + " - 将公会等级和经验清0");
            player.sendMessage(ChatColor.WHITE + "/guild delete <公会>" + ChatColor.GRAY + " - 强制删除一个公会(需确认操作)");
            player.sendMessage(ChatColor.WHITE + "/guild delete confirm" + ChatColor.GRAY + " - 确认删除");
            player.sendMessage(ChatColor.WHITE + "/guild reload" + ChatColor.GRAY + " - 重载插件配置文件");
            player.sendMessage(ChatColor.WHITE + "/guild reload database" + ChatColor.GRAY + " - 重载插件数据库");
        }
        
        player.sendMessage(ChatColor.GOLD + "--------------------------------------------");
    }
    
    private void handleCreate(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild create <公会名称> [标签]");
            return;
        }
        
        if (guildManager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "你已经在一个公会中了");
            return;
        }
        
        String guildName = args[1];
        int minLength = plugin.getConfig().getInt("guild.min-name-length", 3);
        int maxLength = plugin.getConfig().getInt("guild.max-name-length", 16);
        
        if (guildName.length() < minLength || guildName.length() > maxLength) {
            player.sendMessage(ChatColor.RED + "公会名称长度必须在 " + minLength + " 到 " + maxLength + " 之间");
            return;
        }
        
        String tag = "";
        if (args.length >= 3) {
            tag = args[2];
            int maxTagLength = plugin.getConfig().getInt("guild.max-tag-length", 4);
            if (tag.length() > maxTagLength) {
                player.sendMessage(ChatColor.RED + "标签长度不能超过 " + maxTagLength + " 个字符");
                return;
            }
        }
        
        double cost = plugin.getConfig().getDouble("guild.create-cost", 0);
        boolean deducted = false;
        
        if (cost > 0) {
            try {
                if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                    RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
                    if (rsp != null) {
                        Object economy = rsp.getProvider();
                        Method getBalance = economy.getClass().getMethod("getBalance", String.class);
                        Method withdraw = economy.getClass().getMethod("withdrawPlayer", String.class, double.class);
                        
                        double balance = (double) getBalance.invoke(economy, player.getName());
                        if (balance >= cost) {
                            withdraw.invoke(economy, player.getName(), cost);
                            deducted = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (!deducted) {
                try {
                    if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                        Object playerPoints = Bukkit.getPluginManager().getPlugin("PlayerPoints");
                        Method getAPI = playerPoints.getClass().getMethod("getAPI");
                        Object api = getAPI.invoke(playerPoints);
                        Method look = api.getClass().getMethod("look", UUID.class);
                        Method take = api.getClass().getMethod("take", UUID.class, int.class);
                        
                        int points = (int) look.invoke(api, player.getUniqueId());
                        if (points >= cost) {
                            take.invoke(api, player.getUniqueId(), (int) cost);
                            deducted = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (!deducted) {
                player.sendMessage(ChatColor.RED + "创建公会需要 " + cost + " 金币/点数");
                return;
            }
        }
        
        Guild guild = guildManager.createGuild(guildName, player);
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "公会名称已存在");
            if (cost > 0 && deducted) {
                try {
                    if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                        RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
                        if (rsp != null) {
                            Object economy = rsp.getProvider();
                            Method deposit = economy.getClass().getMethod("depositPlayer", String.class, double.class);
                            deposit.invoke(economy, player.getName(), cost);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        
        if (!tag.isEmpty()) {
            guild.setTag(tag);
        }
        
        player.sendMessage(ChatColor.GREEN + "成功创建公会: " + guildName);
        if (!tag.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "公会标签: " + tag);
        }
    }
    
    private void handleJoin(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild join <公会名称>");
            return;
        }
        
        if (guildManager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "你已经在一个公会中了");
            return;
        }
        
        String guildName = args[1];
        Guild guild = guildManager.getGuild(guildName);
        
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "未找到公会");
            return;
        }
        
        guildManager.addRequest(guildName, player.getUniqueId(), player.getName());
        player.sendMessage(ChatColor.GREEN + "已发送加入申请到公会: " + guildName);
        
        for (UUID uuid : guild.getMembers().keySet()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member != null && member.isOnline()) {
                MessageUtils.sendAcceptMessage(member, player.getName() + " 申请加入公会", 
                        "/guild accept " + player.getName());
            }
        }
    }
    
    private void handleLeave(Player player) {
        if (!guildManager.leaveGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中，或者你是会长无法离开");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "你已离开公会");
    }
    
    private void handleInfo(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== 公会信息 ===");
        player.sendMessage(ChatColor.WHITE + "名称: " + guild.getName());
        player.sendMessage(ChatColor.WHITE + "标签: " + guild.getTagColor() + guild.getTag());
        player.sendMessage(ChatColor.WHITE + "等级: " + guild.getLevel());
        player.sendMessage(ChatColor.WHITE + "成员: " + guild.getMembers().size() + "/" + guild.getMaxMembers());
        player.sendMessage(ChatColor.WHITE + "经验: " + guild.getExperience() + "/" + guild.getRequiredExperience());
        if (!guild.getMotd().isEmpty()) {
            player.sendMessage(ChatColor.WHITE + "公告: " + ChatColor.GRAY + guild.getMotd());
        }
    }
    
    private void handleList(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== 公会成员 ===");
        guild.getMembers().values().forEach(member -> {
            Player p = Bukkit.getPlayer(member.getUuid());
            boolean online = p != null && p.isOnline();
            String status = online ? ChatColor.GREEN + "在线" : ChatColor.RED + "离线";
            player.sendMessage(member.getRole().getColor() + Bukkit.getOfflinePlayer(member.getUuid()).getName() + 
                    ChatColor.WHITE + " - " + member.getRole().getDisplayName() + " " + status);
        });
    }
    
    private void handleOnline(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== 在线成员 ===");
        guild.getMembers().values().stream()
                .filter(member -> {
                    Player p = Bukkit.getPlayer(member.getUuid());
                    return p != null && p.isOnline();
                })
                .forEach(member -> {
                    player.sendMessage(member.getRole().getColor() + Bukkit.getPlayer(member.getUuid()).getName() + 
                            ChatColor.WHITE + " - " + member.getRole().getDisplayName());
                });
    }
    
    private void handleInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild invite <玩家>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.hasPermission(player.getUniqueId(), "invite")) {
            player.sendMessage(ChatColor.RED + "你没有权限邀请成员");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        if (guildManager.isInGuild(target.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "该玩家已经在公会中");
            return;
        }
        
        if (!guildManager.sendInvite(guild.getName(), player.getUniqueId(), target.getUniqueId(), target.getName())) {
            player.sendMessage(ChatColor.RED + "该玩家已有待处理的邀请");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "已邀请 " + target.getName() + " 加入公会");
        MessageUtils.sendAcceptDeclineMessage(target, player.getName() + " 邀请你加入公会 " + guild.getName(), 
                "/guild accept", "/guild decline");
    }
    
    private void handleAccept(Player player, String[] args) {
        if (args.length >= 2) {
            handleAcceptRequest(player, args);
        } else {
            handleAcceptInvite(player);
        }
    }
    
    private void handleAcceptInvite(Player player) {
        if (guildManager.isInGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "你已经在公会中");
            return;
        }
        
        if (!guildManager.acceptInvite(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "没有待处理的邀请");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild != null) {
            player.sendMessage(ChatColor.GREEN + "你已加入公会 " + guild.getName());
            guild.broadcast(ChatColor.YELLOW + player.getName() + " 加入了公会");
        }
    }
    
    private void handleAcceptRequest(Player player, String[] args) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.hasPermission(player.getUniqueId(), "invite")) {
            player.sendMessage(ChatColor.RED + "你没有权限接受申请");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        if (guildManager.isInGuild(target.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "该玩家已经在公会中");
            return;
        }
        
        if (!guildManager.acceptRequest(guild.getName(), target.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "没有该玩家的申请或公会已满");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "已接受 " + target.getName() + " 加入公会");
        target.sendMessage(ChatColor.GREEN + "你已加入公会 " + guild.getName());
        guild.broadcast(ChatColor.YELLOW + target.getName() + " 加入了公会");
    }
    
    private void handleDecline(Player player) {
        if (!guildManager.declineInvite(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "没有待处理的邀请");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "已拒绝邀请");
    }
    
    private void handleKick(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild kick <玩家> [原因]");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        String reason = args.length > 2 ? String.join(" ", args).substring(args[0].length() + args[1].length() + 2) : "无原因";
        
        if (!guildManager.kickMember(guild.getName(), target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "无法踢出该玩家");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "已踢出 " + target.getName());
        target.sendMessage(ChatColor.RED + "你被 " + player.getName() + " 踢出了公会。原因: " + reason);
        guild.broadcast(ChatColor.YELLOW + target.getName() + " 被 " + player.getName() + " 踢出了公会");
    }
    
    private void handlePromote(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild promote <玩家>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        if (guildManager.promoteMember(guild.getName(), target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已提升 " + target.getName());
            target.sendMessage(ChatColor.GREEN + "你被提升为 " + guild.getMember(target.getUniqueId()).getRole().getDisplayName());
            guild.broadcast(ChatColor.YELLOW + target.getName() + " 被提升为 " + guild.getMember(target.getUniqueId()).getRole().getDisplayName());
        } else {
            player.sendMessage(ChatColor.RED + "无法提升该玩家，你没有权限或该成员已是最高职位");
        }
    }
    
    private void handleDemote(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild demote <玩家>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        if (guildManager.demoteMember(guild.getName(), target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已降低 " + target.getName());
            target.sendMessage(ChatColor.YELLOW + "你被降职为 " + guild.getMember(target.getUniqueId()).getRole().getDisplayName());
            guild.broadcast(ChatColor.YELLOW + target.getName() + " 被降职为 " + guild.getMember(target.getUniqueId()).getRole().getDisplayName());
        } else {
            player.sendMessage(ChatColor.RED + "无法降低该玩家，你没有权限或该成员已是最低职位");
        }
    }
    
    private void handleTransfer(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild transfer <玩家>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "只有会长可以转让公会");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        if (!guildManager.transferOwnership(guild.getName(), target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "无法转让公会");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "已将公会转让给 " + target.getName());
        target.sendMessage(ChatColor.GREEN + "你已成为公会会长");
        guild.broadcast(ChatColor.YELLOW + target.getName() + " 成为新的公会会长");
    }
    
    private void handleDisband(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "只有会长可以解散公会");
            return;
        }
        
        if (!guildManager.disbandGuild(guild.getName())) {
            player.sendMessage(ChatColor.RED + "无法解散公会");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "公会已解散");
        guild.broadcast(ChatColor.RED + "公会已解散");
    }
    
    private void handleTag(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild tag <标签>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.hasPermission(player.getUniqueId(), "settings")) {
            player.sendMessage(ChatColor.RED + "你没有权限修改公会标签");
            return;
        }
        
        String tag = args[1];
        int maxLength = plugin.getConfig().getInt("guild.max-tag-length", 4);
        
        if (tag.length() > maxLength) {
            player.sendMessage(ChatColor.RED + "标签长度不能超过 " + maxLength);
            return;
        }
        
        guild.setTag(tag);
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "公会标签已设置为: " + guild.getTagColor() + tag);
        guild.broadcast(ChatColor.YELLOW + "公会标签已更新为: " + guild.getTagColor() + tag);
    }
    
    private void handleTagColor(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild tagcolor <颜色代码>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.hasPermission(player.getUniqueId(), "settings")) {
            player.sendMessage(ChatColor.RED + "你没有权限修改公会标签颜色");
            return;
        }
        
        String colorCode = "&" + args[1];
        String coloredText = ChatColor.translateAlternateColorCodes('&', colorCode + "测试");
        
        guild.setTagColor(colorCode);
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "公会标签颜色已设置为: " + coloredText);
    }
    
    private void handleMotd(Player player, String[] args) {
        if (args.length < 2) {
            Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
            if (guild == null) {
                player.sendMessage(ChatColor.RED + "你不在任何公会中");
                return;
            }
            
            if (guild.getMotd().isEmpty()) {
                player.sendMessage(ChatColor.GRAY + "当前没有公会公告");
            } else {
                player.sendMessage(ChatColor.GOLD + "公会公告: " + ChatColor.WHITE + guild.getMotd());
            }
            return;
        }
        
        if (args[1].equalsIgnoreCase("help")) {
            player.sendMessage(ChatColor.GOLD + "=== 公会公告帮助 ===");
            player.sendMessage(ChatColor.WHITE + "/guild motd" + ChatColor.GRAY + " - 查看公会公告");
            player.sendMessage(ChatColor.WHITE + "/guild motd <公告内容>" + ChatColor.GRAY + " - 设置公会公告");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.hasPermission(player.getUniqueId(), "motd")) {
            player.sendMessage(ChatColor.RED + "你没有权限修改公会公告");
            return;
        }
        
        String motd = String.join(" ", args).substring(6);
        guild.setMotd(motd);
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "公会公告已设置");
        guild.broadcast(ChatColor.YELLOW + "公会公告已更新: " + ChatColor.WHITE + motd);
    }
    
    private void handleChat(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild chat <消息>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        String message = String.join(" ", args).substring(5);
        guild.broadcast(ChatColor.translateAlternateColorCodes('&', guild.getTagColor() + "[" + guild.getTag() + "] ") + 
                ChatColor.WHITE + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message);
    }
    
    private void handleOfficerChat(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild officerchat <消息>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (guild.getMember(player.getUniqueId()).getRole() == GuildRole.MEMBER) {
            player.sendMessage(ChatColor.RED + "只有管理员和会长可以使用管理频道");
            return;
        }
        
        String message = String.join(" ", args).substring(13);
        guild.broadcastToOfficers(ChatColor.RED + "[管理频道] " + ChatColor.WHITE + player.getName() + 
                ChatColor.GRAY + ": " + ChatColor.WHITE + message);
    }
    
    private void handleTop(Player player, String[] args) {
        int page = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        List<GuildMember> sortedMembers = guild.getMembers().values().stream()
                .sorted((a, b) -> Long.compare(b.getDailyContribution(), a.getDailyContribution()))
                .collect(Collectors.toList());
        
        player.sendMessage(ChatColor.GOLD + "=== 今日经验贡献排行榜 ===");
        int start = (page - 1) * 10;
        int end = Math.min(start + 10, sortedMembers.size());
        
        for (int i = start; i < end; i++) {
            GuildMember member = sortedMembers.get(i);
            String playerName = Bukkit.getOfflinePlayer(member.getUuid()).getName();
            player.sendMessage(ChatColor.WHITE + "" + (i + 1) + ". " + playerName + 
                    ChatColor.GRAY + " - " + ChatColor.GREEN + member.getDailyContribution() + " 经验");
        }
    }
    
    private void handleLog(Player player, String[] args) {
        player.sendMessage(ChatColor.YELLOW + "公会日志功能暂未实现");
    }
    
    private void handleRequests(Player player, String[] args) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.hasPermission(player.getUniqueId(), "invite")) {
            player.sendMessage(ChatColor.RED + "你没有权限查看申请");
            return;
        }
        
        List<GuildManager.GuildRequest> requests = guildManager.getRequests(guild.getName());
        if (requests.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "当前没有待处理的申请");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== 公会申请 ===");
        requests.forEach(req -> {
            MessageUtils.sendAcceptMessage(player, req.getPlayerName(), 
                    "/guild accept " + req.getPlayerName());
        });
    }
    
    private void handleMember(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild member <玩家>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        GuildMember member = guild.getMember(target.getUniqueId());
        if (member == null) {
            player.sendMessage(ChatColor.RED + "该玩家不在公会中");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== 成员信息 ===");
        player.sendMessage(ChatColor.WHITE + "名称: " + target.getName());
        player.sendMessage(ChatColor.WHITE + "职位: " + member.getRole().getDisplayName());
        player.sendMessage(ChatColor.WHITE + "总贡献: " + member.getTotalContribution());
        player.sendMessage(ChatColor.WHITE + "今日贡献: " + member.getDailyContribution());
        player.sendMessage(ChatColor.WHITE + "加入时间: " + new java.util.Date(member.getJoinedTime()));
    }
    
    private void handleSettings(Player player, String[] args) {
        player.sendMessage(ChatColor.YELLOW + "公会设置功能暂未实现");
    }
    
    private void handlePermission(Player player, String[] args) {
        player.sendMessage(ChatColor.YELLOW + "公会权限设置功能暂未实现");
    }
    
    private void handleParty(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        player.sendMessage(ChatColor.GREEN + "已向公会在线玩家发送组队邀请");
        guild.broadcast(ChatColor.YELLOW + player.getName() + " 发起了组队邀请");
    }
    
    private void handleRename(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild rename <新名称>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (!guild.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "只有会长可以重命名公会");
            return;
        }
        
        player.sendMessage(ChatColor.YELLOW + "公会重命名请求已提交，等待管理员审核");
    }
    
    private void handleGExp(Player player, String[] args) {
        if (!player.hasPermission("guild.admin")) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "用法: /guild gexp <公会> <数量>");
            return;
        }
        
        Guild guild = guildManager.getGuild(args[1]);
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "未找到公会");
            return;
        }
        
        long exp = Long.parseLong(args[2]);
        guild.addExperience(exp);
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "已给予公会 " + guild.getName() + " " + exp + " 经验");
    }
    
    private void handleSLevel(Player player, String[] args) {
        if (!player.hasPermission("guild.admin")) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "用法: /guild slevel <公会> <等级>");
            return;
        }
        
        Guild guild = guildManager.getGuild(args[1]);
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "未找到公会");
            return;
        }
        
        int level = Integer.parseInt(args[2]);
        guild.setLevel(level);
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "已设置公会 " + guild.getName() + " 等级为 " + level);
    }
    
    private void handleClxpLev(Player player, String[] args) {
        if (!player.hasPermission("guild.admin")) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild clxplev <公会>");
            return;
        }
        
        Guild guild = guildManager.getGuild(args[1]);
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "未找到公会");
            return;
        }
        
        guild.setLevel(0);
        guild.setExperience(0);
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "已清零公会 " + guild.getName() + " 的等级和经验");
    }
    
    private void handleDelete(Player player, String[] args) {
        if (!player.hasPermission("guild.admin")) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild delete <公会>");
            return;
        }
        
        if (args.length >= 3 && args[2].equals("confirm")) {
            Guild guild = guildManager.getGuild(args[1]);
            if (guild == null) {
                player.sendMessage(ChatColor.RED + "未找到公会");
                return;
            }
            
            String guildName = guild.getName();
            guildManager.disbandGuild(guildName);
            player.sendMessage(ChatColor.GREEN + "已强制删除公会: " + guildName);
            return;
        }
        
        Guild guild = guildManager.getGuild(args[1]);
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "未找到公会");
            return;
        }
        
        player.sendMessage(ChatColor.RED + "确认删除公会 " + guild.getName() + "? 使用 /guild delete " + args[1] + " confirm 确认删除");
    }
    
    private void handleReload(Player player, String[] args) {
        if (!player.hasPermission("guild.admin")) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        if (args.length > 1 && args[1].equals("database")) {
            plugin.getDatabaseManager().loadGuilds();
            player.sendMessage(ChatColor.GREEN + "数据库已重载");
        } else {
            plugin.reloadConfig();
            player.sendMessage(ChatColor.GREEN + "配置文件已重载");
        }
    }
    
    private void handleListGuilds(Player player) {
        if (guildManager.getGuilds().isEmpty()) {
            player.sendMessage(ChatColor.RED + "服务器上没有公会");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== 所有公会 ===");
        guildManager.getGuilds().values().forEach(guild -> {
            player.sendMessage(ChatColor.YELLOW + guild.getName() + 
                    ChatColor.WHITE + " - 等级: " + guild.getLevel() + 
                    ChatColor.WHITE + " 成员: " + guild.getMembers().size() + 
                    ChatColor.WHITE + " 经验: " + guild.getExperience());
        });
    }
}