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
            case "balance":
                handleBalance(player);
                break;
            case "deposit":
                handleDeposit(player, args);
                break;
            case "withdraw":
                handleWithdraw(player, args);
                break;
            case "upgrade":
                handleUpgrade(player, args);
                break;
            case "mute":
                handleMute(player, args);
                break;
            case "unmute":
                handleUnmute(player, args);
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
        player.sendMessage(ChatColor.WHITE + "/guild upgrade <bexp50 | upgrade>" + ChatColor.GRAY + " - 购买经验或直接升级公会");
        player.sendMessage(ChatColor.WHITE + "/guild mute <玩家> <时间>" + ChatColor.GRAY + " - 禁言公会成员（管理员以上）");
        player.sendMessage(ChatColor.WHITE + "/guild unmute <玩家>" + ChatColor.GRAY + " - 解除公会成员禁言（管理员以上）");
        
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
        
        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        UUID targetUuid = null;
        String displayName = null;
        
        if (target != null) {
            targetUuid = target.getUniqueId();
            displayName = target.getName();
        } else {
            // 尝试通过公会成员列表查找（支持离线玩家）
            boolean found = false;
            for (GuildMember member : guild.getMembers().values()) {
                // 由于没有存储玩家名，这里无法通过名称查找
                // 直接使用在线玩家检测
                Player onlinePlayer = Bukkit.getPlayer(member.getUuid());
                if (onlinePlayer != null && onlinePlayer.getName().equalsIgnoreCase(targetName)) {
                    targetUuid = member.getUuid();
                    displayName = onlinePlayer.getName();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                player.sendMessage(ChatColor.RED + "玩家不在线或不在公会中");
                return;
            }
        }
        
        if (guildManager.promoteMember(guild.getName(), targetUuid, player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已提升 " + displayName);
            if (target != null) {
                target.sendMessage(ChatColor.GREEN + "你被提升为 " + guild.getMember(targetUuid).getRole().getDisplayName());
            }
            guild.broadcast(ChatColor.YELLOW + displayName + " 被提升为 " + guild.getMember(targetUuid).getRole().getDisplayName());
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
        
        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        UUID targetUuid = null;
        String displayName = null;
        
        if (target != null) {
            targetUuid = target.getUniqueId();
            displayName = target.getName();
        } else {
            // 尝试通过公会成员列表查找（支持离线玩家）
            boolean found = false;
            for (GuildMember member : guild.getMembers().values()) {
                // 由于没有存储玩家名，这里无法通过名称查找
                // 直接使用在线玩家检测
                Player onlinePlayer = Bukkit.getPlayer(member.getUuid());
                if (onlinePlayer != null && onlinePlayer.getName().equalsIgnoreCase(targetName)) {
                    targetUuid = member.getUuid();
                    displayName = onlinePlayer.getName();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                player.sendMessage(ChatColor.RED + "玩家不在线或不在公会中");
                return;
            }
        }
        
        if (guildManager.demoteMember(guild.getName(), targetUuid, player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "已降低 " + displayName);
            if (target != null) {
                target.sendMessage(ChatColor.YELLOW + "你被降职为 " + guild.getMember(targetUuid).getRole().getDisplayName());
            }
            guild.broadcast(ChatColor.YELLOW + displayName + " 被降职为 " + guild.getMember(targetUuid).getRole().getDisplayName());
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
        if (args.length < 2) {
            player.sendMessage(ChatColor.GOLD + "=== 个人设置 ===");
            player.sendMessage(ChatColor.WHITE + "/guild settings nickname <昵称>" + ChatColor.GRAY + " - 设置你的公会昵称");
            player.sendMessage(ChatColor.WHITE + "/guild settings clearnick" + ChatColor.GRAY + " - 清除你的公会昵称");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        GuildMember member = guild.getMember(player.getUniqueId());
        if (member == null) {
            player.sendMessage(ChatColor.RED + "你不在公会中");
            return;
        }
        
        String subCommand = args[1].toLowerCase();
        
        if (subCommand.equals("nickname")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "用法: /guild settings nickname <昵称>");
                return;
            }
            
            String nickname = String.join(" ", args).substring(23);
            if (nickname.length() > 16) {
                player.sendMessage(ChatColor.RED + "昵称长度不能超过16个字符");
                return;
            }
            
            member.setNickname(nickname);
            plugin.getDatabaseManager().saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "你的公会昵称已设置为: " + nickname);
        } else if (subCommand.equals("clearnick")) {
            member.setNickname(null);
            plugin.getDatabaseManager().saveGuild(guild);
            player.sendMessage(ChatColor.GREEN + "你的公会昵称已清除");
        } else {
            player.sendMessage(ChatColor.RED + "未知的设置命令");
        }
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
    
    private void handleBalance(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        long balance = guildManager.getGuildBalance(guild.getName());
        player.sendMessage(ChatColor.GOLD + "=== 公会银行 ===");
        player.sendMessage(ChatColor.WHITE + "公会名称: " + ChatColor.YELLOW + guild.getName());
        player.sendMessage(ChatColor.WHITE + "当前余额: " + ChatColor.GREEN + balance);
    }
    
    private void handleDeposit(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild deposit <金额>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        try {
            long amount = Long.parseLong(args[1]);
            if (amount <= 0) {
                player.sendMessage(ChatColor.RED + "金额必须大于0");
                return;
            }
            
            if (!checkPlayerBalance(player, amount)) {
                player.sendMessage(ChatColor.RED + "你的余额不足");
                return;
            }
            
            if (deductPlayerBalance(player, amount)) {
                if (guildManager.depositToBank(guild.getName(), player.getUniqueId(), amount)) {
                    player.sendMessage(ChatColor.GREEN + "成功存入 " + amount + " 到公会银行");
                    guild.broadcast(ChatColor.YELLOW + player.getName() + " 向公会银行存入了 " + amount);
                } else {
                    refundPlayerBalance(player, amount);
                    player.sendMessage(ChatColor.RED + "存款失败");
                }
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "无效的金额");
        }
    }
    
    private void handleWithdraw(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild withdraw <金额>");
            return;
        }
        
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        try {
            long amount = Long.parseLong(args[1]);
            if (amount <= 0) {
                player.sendMessage(ChatColor.RED + "金额必须大于0");
                return;
            }
            
            if (guildManager.withdrawFromBank(guild.getName(), player.getUniqueId(), amount)) {
                player.sendMessage(ChatColor.GREEN + "成功从公会银行取出 " + amount);
                guild.broadcast(ChatColor.YELLOW + player.getName() + " 从公会银行取出了 " + amount);
            } else {
                player.sendMessage(ChatColor.RED + "取款失败，余额不足或没有权限");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "无效的金额");
        }
    }
    
    private boolean checkPlayerBalance(Player player, long amount) {
        try {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
                if (rsp != null) {
                    Object economy = rsp.getProvider();
                    Method getBalance = economy.getClass().getMethod("getBalance", String.class);
                    double balance = (double) getBalance.invoke(economy, player.getName());
                    return balance >= amount;
                }
            }
            
            if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                Object playerPoints = Bukkit.getPluginManager().getPlugin("PlayerPoints");
                Method getAPI = playerPoints.getClass().getMethod("getAPI");
                Object api = getAPI.invoke(playerPoints);
                Method look = api.getClass().getMethod("look", UUID.class);
                int points = (int) look.invoke(api, player.getUniqueId());
                return points >= amount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean deductPlayerBalance(Player player, long amount) {
        try {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
                if (rsp != null) {
                    Object economy = rsp.getProvider();
                    Method withdraw = economy.getClass().getMethod("withdrawPlayer", String.class, double.class);
                    withdraw.invoke(economy, player.getName(), (double) amount);
                    return true;
                }
            }
            
            if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                Object playerPoints = Bukkit.getPluginManager().getPlugin("PlayerPoints");
                Method getAPI = playerPoints.getClass().getMethod("getAPI");
                Object api = getAPI.invoke(playerPoints);
                Method take = api.getClass().getMethod("take", UUID.class, int.class);
                take.invoke(api, player.getUniqueId(), (int) amount);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void refundPlayerBalance(Player player, long amount) {
        try {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
                if (rsp != null) {
                    Object economy = rsp.getProvider();
                    Method deposit = economy.getClass().getMethod("depositPlayer", String.class, double.class);
                    deposit.invoke(economy, player.getName(), (double) amount);
                }
            }
            
            if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                Object playerPoints = Bukkit.getPluginManager().getPlugin("PlayerPoints");
                Method getAPI = playerPoints.getClass().getMethod("getAPI");
                Object api = getAPI.invoke(playerPoints);
                Method give = api.getClass().getMethod("give", UUID.class, int.class);
                give.invoke(api, player.getUniqueId(), (int) amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleUpgrade(Player player, String[] args) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild upgrade <bexp50 | upgrade>");
            player.sendMessage(ChatColor.GRAY + "  bexp50 - 使用货币购买50经验");
            player.sendMessage(ChatColor.GRAY + "  upgrade - 使用货币直接升级公会");
            return;
        }
        
        String action = args[1].toLowerCase();
        
        switch (action) {
            case "bexp50":
            case "buyexp":
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
                break;
            case "upgrade":
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
                } else {
                    long cost = plugin.getCurrencyConfig().getLevelUpCost();
                    String currencyName = plugin.getGuildCurrency().formatAmount(cost, plugin.getCurrencyConfig().getCurrencyType());
                    player.sendMessage(ChatColor.RED + "升级失败，你可能没有足够的 " + currencyName);
                }
                break;
            default:
                player.sendMessage(ChatColor.RED + "用法: /guild upgrade <bexp50 | upgrade>");
                player.sendMessage(ChatColor.GRAY + "  bexp50 - 使用货币购买50经验");
                player.sendMessage(ChatColor.GRAY + "  upgrade - 使用货币直接升级公会");
                break;
        }
    }
    
    private void handleMute(Player player, String[] args) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "用法: /guild mute <玩家> <时间>");
            player.sendMessage(ChatColor.GRAY + "时间格式: 90d20m1s (天、分、秒)");
            player.sendMessage(ChatColor.GRAY + "最长时间: 999天");
            return;
        }
        
        String targetName = args[1];
        String timeStr = args[2];
        
        // 解析时间
        long duration = parseTime(timeStr);
        if (duration <= 0) {
            player.sendMessage(ChatColor.RED + "无效的时间格式");
            player.sendMessage(ChatColor.GRAY + "正确格式: 90d20m1s (天、分、秒)");
            return;
        }
        
        // 检查最长时间限制（999天）
        long maxDuration = 999 * 24 * 60 * 60 * 1000L;
        if (duration > maxDuration) {
            player.sendMessage(ChatColor.RED + "时间过长，最长只能为999天");
            return;
        }
        
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        UUID targetUuid = target.getUniqueId();
        if (!guild.isMember(targetUuid)) {
            player.sendMessage(ChatColor.RED + "目标玩家不在你的公会中");
            return;
        }
        
        // 检查权限
        GuildMember playerMember = guild.getMember(player.getUniqueId());
        GuildMember targetMember = guild.getMember(targetUuid);
        
        if (playerMember.getRole() == GuildRole.MEMBER) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        // 管理员不能禁言其他管理员或会长
        if (playerMember.getRole() == GuildRole.OFFICER) {
            if (targetMember.getRole() == GuildRole.OFFICER || targetMember.getRole() == GuildRole.OWNER) {
                player.sendMessage(ChatColor.RED + "你不能禁言其他管理员或会长");
                return;
            }
        }
        
        // 会长不能禁言自己
        if (playerMember.getRole() == GuildRole.OWNER && targetUuid.equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "你不能禁言自己");
            return;
        }
        
        // 执行禁言
        targetMember.mute(duration);
        plugin.getDatabaseManager().saveGuild(guild);
        
        String timeDisplay = formatTime(duration);
        player.sendMessage(ChatColor.GREEN + "成功禁言 " + target.getName() + " " + timeDisplay);
        target.sendMessage(ChatColor.RED + "你被禁言 " + timeDisplay + "，无法在公会频道发言");
        guild.broadcast(ChatColor.YELLOW + target.getName() + " 被禁言 " + timeDisplay);
    }
    
    private void handleUnmute(Player player, String[] args) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.RED + "你不在任何公会中");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "用法: /guild unmute <玩家>");
            return;
        }
        
        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "玩家不在线");
            return;
        }
        
        UUID targetUuid = target.getUniqueId();
        if (!guild.isMember(targetUuid)) {
            player.sendMessage(ChatColor.RED + "目标玩家不在你的公会中");
            return;
        }
        
        // 检查权限
        GuildMember playerMember = guild.getMember(player.getUniqueId());
        if (playerMember.getRole() == GuildRole.MEMBER) {
            player.sendMessage(ChatColor.RED + "你没有权限执行此操作");
            return;
        }
        
        // 执行解除禁言
        GuildMember targetMember = guild.getMember(targetUuid);
        if (!targetMember.isMuted()) {
            player.sendMessage(ChatColor.RED + "该玩家未被禁言");
            return;
        }
        
        targetMember.unmute();
        plugin.getDatabaseManager().saveGuild(guild);
        
        player.sendMessage(ChatColor.GREEN + "成功解除 " + target.getName() + " 的禁言");
        target.sendMessage(ChatColor.GREEN + "你的禁言已被解除");
        guild.broadcast(ChatColor.YELLOW + target.getName() + " 的禁言已被解除");
    }
    
    private long parseTime(String timeStr) {
        long duration = 0;
        
        // 匹配天、分、秒
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([0-9]+)([dms])").matcher(timeStr);
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);
            
            switch (unit) {
                case 'd':
                    duration += (long) value * 24 * 60 * 60 * 1000;
                    break;
                case 'm':
                    duration += (long) value * 60 * 1000;
                    break;
                case 's':
                    duration += (long) value * 1000;
                    break;
            }
        }
        
        return duration;
    }
    
    private String formatTime(long duration) {
        long days = duration / (24 * 60 * 60 * 1000);
        long hours = (duration % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (duration % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (duration % (60 * 1000)) / 1000;
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("天");
        if (hours > 0) sb.append(hours).append("小时");
        if (minutes > 0) sb.append(minutes).append("分钟");
        if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("秒");
        
        return sb.toString();
    }
}