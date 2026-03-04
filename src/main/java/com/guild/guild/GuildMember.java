package com.guild.guild;

import java.util.UUID;

public class GuildMember {
    
    private UUID uuid;
    private GuildRole role;
    private String nickname;
    private long joinedTime;
    private long totalContribution;
    private long dailyContribution;
    private boolean muted;
    private long mutedUntil;
    
    public GuildMember(UUID uuid, GuildRole role) {
        this.uuid = uuid;
        this.role = role;
        this.nickname = null;
        this.joinedTime = System.currentTimeMillis();
        this.totalContribution = 0;
        this.dailyContribution = 0;
        this.muted = false;
        this.mutedUntil = 0;
    }
    
    public void addContribution(long contribution) {
        this.totalContribution += contribution;
        this.dailyContribution += contribution;
    }
    
    public boolean isMuted() {
        return muted && System.currentTimeMillis() < mutedUntil;
    }
    
    public void mute(long duration) {
        this.muted = true;
        this.mutedUntil = System.currentTimeMillis() + duration;
    }
    
    public void unmute() {
        this.muted = false;
        this.mutedUntil = 0;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public GuildRole getRole() {
        return role;
    }
    
    public void setRole(GuildRole role) {
        this.role = role;
    }
    
    public long getJoinedTime() {
        return joinedTime;
    }
    
    public long getTotalContribution() {
        return totalContribution;
    }
    
    public void setTotalContribution(long totalContribution) {
        this.totalContribution = totalContribution;
    }
    
    public long getDailyContribution() {
        return dailyContribution;
    }
    
    public void setDailyContribution(long dailyContribution) {
        this.dailyContribution = dailyContribution;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}