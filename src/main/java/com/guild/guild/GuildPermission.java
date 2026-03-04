package com.guild.guild;

public enum GuildPermission {
    
    MEMBER(1),
    OFFICER(2),
    OWNER(3);
    
    private int level;
    
    GuildPermission(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
}