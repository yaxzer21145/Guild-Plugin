package com.guild.guild;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Guild {
    
    private String name;
    private String tag;
    private String tagColor;
    private UUID owner;
    private int level;
    private long experience;
    private long dailyExperience;
    private String motd;
    private boolean publicGuild;
    private long createdTime;
    private Map<UUID, GuildMember> members;
    private Map<String, GuildPermission> permissions;
    private GuildBank bank;
    
    public Guild(String name, UUID owner) {
        this.name = name;
        this.tag = name.substring(0, Math.min(4, name.length()));
        this.tagColor = "&f";
        this.owner = owner;
        this.level = 0;
        this.experience = 0;
        this.dailyExperience = 0;
        this.motd = "";
        this.publicGuild = true;
        this.createdTime = System.currentTimeMillis();
        this.members = new HashMap<>();
        this.permissions = new HashMap<>();
        
        GuildMember ownerMember = new GuildMember(owner, GuildRole.OWNER);
        members.put(owner, ownerMember);
        
        initializeDefaultPermissions();
        this.bank = new GuildBank();
    }
    
    private void initializeDefaultPermissions() {
        permissions.put("invite", GuildPermission.OWNER);
        permissions.put("kick", GuildPermission.OFFICER);
        permissions.put("promote", GuildPermission.OFFICER);
        permissions.put("demote", GuildPermission.OFFICER);
        permissions.put("chat", GuildPermission.MEMBER);
        permissions.put("motd", GuildPermission.OFFICER);
        permissions.put("settings", GuildPermission.OFFICER);
        permissions.put("disband", GuildPermission.OWNER);
        permissions.put("withdraw", GuildPermission.OFFICER);
    }
    
    public int getMaxMembers() {
        return 25 + (level * 5);
    }
    
    public boolean canAddMember() {
        return members.size() < getMaxMembers();
    }
    
    public void addMember(UUID uuid, GuildRole role) {
        members.put(uuid, new GuildMember(uuid, role));
    }
    
    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }
    
    public GuildMember getMember(UUID uuid) {
        return members.get(uuid);
    }
    
    public boolean isMember(UUID uuid) {
        return members.containsKey(uuid);
    }
    
    public void addExperience(long exp) {
        this.experience += exp;
        this.dailyExperience += exp;
        checkLevelUp();
    }
    
    private void checkLevelUp() {
        long requiredExp = getRequiredExperience();
        while (experience >= requiredExp && level < 100) {
            experience -= requiredExp;
            level++;
        }
    }
    
    public long getRequiredExperience() {
        return (long) (1000 * Math.pow(1.5, level));
    }
    
    public boolean hasPermission(UUID uuid, String permission) {
        GuildMember member = members.get(uuid);
        if (member == null) return false;
        
        GuildPermission required = permissions.get(permission);
        if (required == null) return false;
        
        return member.getRole().getLevel() >= required.getLevel();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getTagColor() {
        return tagColor;
    }
    
    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }
    
    public UUID getOwner() {
        return owner;
    }
    
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public long getExperience() {
        return experience;
    }
    
    public void setExperience(long experience) {
        this.experience = experience;
    }
    
    public long getDailyExperience() {
        return dailyExperience;
    }
    
    public void setDailyExperience(long dailyExperience) {
        this.dailyExperience = dailyExperience;
    }
    
    public String getMotd() {
        return motd;
    }
    
    public void setMotd(String motd) {
        this.motd = motd;
    }
    
    public boolean isPublicGuild() {
        return publicGuild;
    }
    
    public void setPublicGuild(boolean publicGuild) {
        this.publicGuild = publicGuild;
    }
    
    public long getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
    
    public Map<UUID, GuildMember> getMembers() {
        return members;
    }
    
    public Map<String, GuildPermission> getPermissions() {
        return permissions;
    }
    
    public GuildBank getBank() {
        return bank;
    }
    
    public void setBank(GuildBank bank) {
        this.bank = bank;
    }
    
    public void broadcast(String message) {
        for (UUID uuid : members.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendMessage(message);
            }
        }
    }
    
    public void broadcastToOfficers(String message) {
        for (GuildMember member : members.values()) {
            if (member.getRole() == GuildRole.OFFICER || member.getRole() == GuildRole.OWNER) {
                Player player = Bukkit.getPlayer(member.getUuid());
                if (player != null && player.isOnline()) {
                    player.sendMessage(message);
                }
            }
        }
    }
}