package com.guild.guild;

import com.guild.GuildPlugin;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GuildManager {
    
    private final GuildPlugin plugin;
    private final Map<String, Guild> guilds;
    private final Map<UUID, String> playerGuilds;
    private final Map<String, List<GuildRequest>> requests;
    private final Map<UUID, GuildInvite> invites;
    private final Map<UUID, PlayerSettings> playerSettings;
    
    public GuildManager(GuildPlugin plugin) {
        this.plugin = plugin;
        this.guilds = new ConcurrentHashMap<>();
        this.playerGuilds = new ConcurrentHashMap<>();
        this.requests = new ConcurrentHashMap<>();
        this.invites = new ConcurrentHashMap<>();
        this.playerSettings = new ConcurrentHashMap<>();
    }
    
    public Guild createGuild(String name, Player owner) {
        if (guilds.containsKey(name.toLowerCase())) {
            return null;
        }
        
        Guild guild = new Guild(name, owner.getUniqueId());
        guilds.put(name.toLowerCase(), guild);
        playerGuilds.put(owner.getUniqueId(), name.toLowerCase());
        
        plugin.getDatabaseManager().saveGuild(guild);
        
        return guild;
    }
    
    public boolean disbandGuild(String name) {
        Guild guild = guilds.remove(name.toLowerCase());
        if (guild == null) return false;
        
        for (UUID uuid : guild.getMembers().keySet()) {
            playerGuilds.remove(uuid);
        }
        
        plugin.getDatabaseManager().deleteGuild(name);
        return true;
    }
    
    public Guild getGuild(String name) {
        return guilds.get(name.toLowerCase());
    }
    
    public Guild getPlayerGuild(UUID uuid) {
        String guildName = playerGuilds.get(uuid);
        return guildName != null ? guilds.get(guildName) : null;
    }
    
    public boolean isInGuild(UUID uuid) {
        return playerGuilds.containsKey(uuid);
    }
    
    public boolean joinGuild(String guildName, Player player) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.canAddMember()) return false;
        
        guild.addMember(player.getUniqueId(), GuildRole.MEMBER);
        playerGuilds.put(player.getUniqueId(), guildName.toLowerCase());
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public boolean leaveGuild(UUID uuid) {
        String guildName = playerGuilds.get(uuid);
        if (guildName == null) return false;
        
        Guild guild = guilds.get(guildName);
        if (guild == null) return false;
        
        if (guild.getOwner().equals(uuid)) {
            return false;
        }
        
        guild.removeMember(uuid);
        playerGuilds.remove(uuid);
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public boolean kickMember(String guildName, UUID targetUuid, UUID kickerUuid) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.hasPermission(kickerUuid, "kick")) return false;
        
        guild.removeMember(targetUuid);
        playerGuilds.remove(targetUuid);
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public boolean promoteMember(String guildName, UUID targetUuid, UUID promoterUuid) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.hasPermission(promoterUuid, "promote")) return false;
        
        GuildMember member = guild.getMember(targetUuid);
        if (member == null) return false;
        
        member.setRole(member.getRole().promote());
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public boolean demoteMember(String guildName, UUID targetUuid, UUID demoterUuid) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.hasPermission(demoterUuid, "demote")) return false;
        
        GuildMember member = guild.getMember(targetUuid);
        if (member == null) return false;
        
        member.setRole(member.getRole().demote());
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public boolean transferOwnership(String guildName, UUID newOwner, UUID currentOwner) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.getOwner().equals(currentOwner)) return false;
        
        GuildMember newOwnerMember = guild.getMember(newOwner);
        if (newOwnerMember == null) return false;
        
        GuildMember oldOwnerMember = guild.getMember(currentOwner);
        oldOwnerMember.setRole(GuildRole.OFFICER);
        newOwnerMember.setRole(GuildRole.OWNER);
        guild.setOwner(newOwner);
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public void addRequest(String guildName, UUID playerUuid, String playerName) {
        requests.computeIfAbsent(guildName.toLowerCase(), k -> new ArrayList<>())
                .add(new GuildRequest(playerUuid, playerName));
    }
    
    public List<GuildRequest> getRequests(String guildName) {
        return requests.getOrDefault(guildName.toLowerCase(), new ArrayList<>());
    }
    
    public boolean acceptRequest(String guildName, UUID playerUuid) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.canAddMember()) return false;
        
        List<GuildRequest> guildRequests = requests.get(guildName.toLowerCase());
        if (guildRequests == null) return false;
        
        GuildRequest request = guildRequests.stream()
                .filter(r -> r.getPlayerUuid().equals(playerUuid))
                .findFirst()
                .orElse(null);
        
        if (request == null) return false;
        
        guildRequests.remove(request);
        guild.addMember(playerUuid, GuildRole.MEMBER);
        playerGuilds.put(playerUuid, guildName.toLowerCase());
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public void addExperience(UUID uuid, long exp) {
        String guildName = playerGuilds.get(uuid);
        if (guildName == null) return;
        
        Guild guild = guilds.get(guildName);
        if (guild == null) return;
        
        guild.addExperience(exp);
        
        GuildMember member = guild.getMember(uuid);
        if (member != null) {
            member.addContribution(exp);
        }
        
        plugin.getDatabaseManager().saveGuild(guild);
    }
    
    public Map<String, Guild> getGuilds() {
        return guilds;
    }
    
    public Map<UUID, String> getPlayerGuilds() {
        return playerGuilds;
    }
    
    public boolean sendInvite(String guildName, UUID inviterUuid, UUID targetUuid, String targetName) {
        Guild guild = guilds.get(guildName.toLowerCase());
        if (guild == null) return false;
        
        if (!guild.hasPermission(inviterUuid, "invite")) return false;
        
        if (invites.containsKey(targetUuid)) {
            return false;
        }
        
        invites.put(targetUuid, new GuildInvite(guildName.toLowerCase(), inviterUuid, targetUuid, targetName));
        return true;
    }
    
    public boolean acceptInvite(UUID playerUuid) {
        GuildInvite invite = invites.remove(playerUuid);
        if (invite == null) return false;
        
        Guild guild = guilds.get(invite.getGuildName());
        if (guild == null) return false;
        
        if (!guild.canAddMember()) return false;
        
        guild.addMember(playerUuid, GuildRole.MEMBER);
        playerGuilds.put(playerUuid, invite.getGuildName());
        
        plugin.getDatabaseManager().saveGuild(guild);
        return true;
    }
    
    public boolean declineInvite(UUID playerUuid) {
        return invites.remove(playerUuid) != null;
    }
    
    public GuildInvite getInvite(UUID playerUuid) {
        return invites.get(playerUuid);
    }
    
    public Map<UUID, GuildInvite> getInvites() {
        return invites;
    }
    
    public PlayerSettings getPlayerSettings(UUID playerUuid) {
        return playerSettings.computeIfAbsent(playerUuid, PlayerSettings::new);
    }
    
    public boolean togglePlayerInvites(UUID playerUuid) {
        PlayerSettings settings = getPlayerSettings(playerUuid);
        settings.toggleInvites();
        return settings.isAllowInvites();
    }
    
    public boolean togglePlayerNotify(UUID playerUuid) {
        PlayerSettings settings = getPlayerSettings(playerUuid);
        settings.toggleNotify();
        return settings.isNotifyOnlineStatus();
    }
    
    public static class GuildInvite {
        private final String guildName;
        private final UUID inviterUuid;
        private final UUID targetUuid;
        private final String targetName;
        private final long inviteTime;
        
        public GuildInvite(String guildName, UUID inviterUuid, UUID targetUuid, String targetName) {
            this.guildName = guildName;
            this.inviterUuid = inviterUuid;
            this.targetUuid = targetUuid;
            this.targetName = targetName;
            this.inviteTime = System.currentTimeMillis();
        }
        
        public String getGuildName() {
            return guildName;
        }
        
        public UUID getInviterUuid() {
            return inviterUuid;
        }
        
        public UUID getTargetUuid() {
            return targetUuid;
        }
        
        public String getTargetName() {
            return targetName;
        }
        
        public long getInviteTime() {
            return inviteTime;
        }
    }
    
    public static class GuildRequest {
        private final UUID playerUuid;
        private final String playerName;
        private final long requestTime;
        
        public GuildRequest(UUID playerUuid, String playerName) {
            this.playerUuid = playerUuid;
            this.playerName = playerName;
            this.requestTime = System.currentTimeMillis();
        }
        
        public UUID getPlayerUuid() {
            return playerUuid;
        }
        
        public String getPlayerName() {
            return playerName;
        }
        
        public long getRequestTime() {
            return requestTime;
        }
    }
}