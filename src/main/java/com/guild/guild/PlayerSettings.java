package com.guild.guild;

import java.util.UUID;

public class PlayerSettings {
    
    private final UUID playerUuid;
    private boolean allowInvites;
    private boolean notifyOnlineStatus;
    
    public PlayerSettings(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.allowInvites = true;
        this.notifyOnlineStatus = true;
    }
    
    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    public boolean isAllowInvites() {
        return allowInvites;
    }
    
    public void setAllowInvites(boolean allowInvites) {
        this.allowInvites = allowInvites;
    }
    
    public boolean isNotifyOnlineStatus() {
        return notifyOnlineStatus;
    }
    
    public void setNotifyOnlineStatus(boolean notifyOnlineStatus) {
        this.notifyOnlineStatus = notifyOnlineStatus;
    }
    
    public void toggleInvites() {
        this.allowInvites = !this.allowInvites;
    }
    
    public void toggleNotify() {
        this.notifyOnlineStatus = !this.notifyOnlineStatus;
    }
}
