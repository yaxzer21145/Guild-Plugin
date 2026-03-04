package com.guild.guild;

public enum GuildRole {
    
    MEMBER("成员", 1, "&7"),
    OFFICER("管理员", 2, "&a"),
    OWNER("会长", 3, "&6");
    
    private String displayName;
    private int level;
    private String color;
    
    GuildRole(String displayName, int level, String color) {
        this.displayName = displayName;
        this.level = level;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getColor() {
        return color;
    }
    
    public GuildRole promote() {
        switch (this) {
            case MEMBER:
                return OFFICER;
            case OFFICER:
            case OWNER:
            default:
                return this;
        }
    }
    
    public GuildRole demote() {
        switch (this) {
            case OFFICER:
                return MEMBER;
            case MEMBER:
            case OWNER:
            default:
                return this;
        }
    }
}