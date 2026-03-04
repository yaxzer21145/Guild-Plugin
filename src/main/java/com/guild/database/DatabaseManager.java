package com.guild.database;

import com.guild.GuildPlugin;
import com.guild.guild.Guild;
import com.guild.guild.GuildMember;
import com.guild.guild.GuildPermission;
import com.guild.guild.GuildRole;

import java.io.File;
import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager {
    
    private final GuildPlugin plugin;
    private Connection connection;
    
    public DatabaseManager(GuildPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void initialize() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            
            String url = "jdbc:sqlite:" + dataFolder.getAbsolutePath() + "/guilds.db";
            connection = DriverManager.getConnection(url);
            
            createTables();
            loadGuilds();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS guilds (" +
                    "name TEXT PRIMARY KEY," +
                    "tag TEXT," +
                    "tag_color TEXT," +
                    "owner TEXT," +
                    "level INTEGER," +
                    "experience INTEGER," +
                    "daily_experience INTEGER," +
                    "motd TEXT," +
                    "public_guild INTEGER," +
                    "created_time INTEGER" +
                    ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS guild_members (" +
                    "guild_name TEXT," +
                    "uuid TEXT," +
                    "role TEXT," +
                    "joined_time INTEGER," +
                    "total_contribution INTEGER," +
                    "daily_contribution INTEGER," +
                    "muted INTEGER," +
                    "muted_until INTEGER," +
                    "PRIMARY KEY (guild_name, uuid)," +
                    "FOREIGN KEY (guild_name) REFERENCES guilds(name)" +
                    ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS guild_permissions (" +
                    "guild_name TEXT," +
                    "permission TEXT," +
                    "level INTEGER," +
                    "PRIMARY KEY (guild_name, permission)," +
                    "FOREIGN KEY (guild_name) REFERENCES guilds(name)" +
                    ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS player_settings (" +
                    "uuid TEXT PRIMARY KEY," +
                    "guild_invites INTEGER," +
                    "join_notifications INTEGER" +
                    ")");
        }
    }
    
    public void loadGuilds() {
        try {
            Map<String, Guild> guilds = plugin.getGuildManager().getGuilds();
            Map<UUID, String> playerGuilds = plugin.getGuildManager().getPlayerGuilds();
            
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM guilds");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String name = rs.getString("name");
                String tag = rs.getString("tag");
                String tagColor = rs.getString("tag_color");
                UUID owner = UUID.fromString(rs.getString("owner"));
                int level = rs.getInt("level");
                long experience = rs.getLong("experience");
                long dailyExperience = rs.getLong("daily_experience");
                String motd = rs.getString("motd");
                boolean publicGuild = rs.getBoolean("public_guild");
                long createdTime = rs.getLong("created_time");
                
                Guild guild = new Guild(name, owner);
                guild.setTag(tag);
                guild.setTagColor(tagColor);
                guild.setLevel(level);
                guild.setExperience(experience);
                guild.setDailyExperience(dailyExperience);
                guild.setMotd(motd);
                guild.setPublicGuild(publicGuild);
                guild.setCreatedTime(createdTime);
                
                loadGuildMembers(guild);
                loadGuildPermissions(guild);
                
                guilds.put(name.toLowerCase(), guild);
            }
            
            rs.close();
            stmt.close();
            
            for (Guild guild : guilds.values()) {
                for (UUID uuid : guild.getMembers().keySet()) {
                    playerGuilds.put(uuid, guild.getName().toLowerCase());
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadGuildMembers(Guild guild) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM guild_members WHERE guild_name = ?");
        stmt.setString(1, guild.getName());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            UUID uuid = UUID.fromString(rs.getString("uuid"));
            GuildRole role = GuildRole.valueOf(rs.getString("role"));
            long totalContribution = rs.getLong("total_contribution");
            long dailyContribution = rs.getLong("daily_contribution");
            boolean muted = rs.getBoolean("muted");
            long mutedUntil = rs.getLong("muted_until");
            
            GuildMember member = new GuildMember(uuid, role);
            member.setTotalContribution(totalContribution);
            member.setDailyContribution(dailyContribution);
            if (muted) {
                member.mute(mutedUntil - System.currentTimeMillis());
            }
            
            guild.getMembers().put(uuid, member);
        }
        
        rs.close();
        stmt.close();
    }
    
    private void loadGuildPermissions(Guild guild) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM guild_permissions WHERE guild_name = ?");
        stmt.setString(1, guild.getName());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String permission = rs.getString("permission");
            int level = rs.getInt("level");
            
            guild.getPermissions().put(permission, 
                    level == 1 ? GuildPermission.MEMBER : 
                    level == 2 ? GuildPermission.OFFICER : 
                    GuildPermission.OWNER);
        }
        
        rs.close();
        stmt.close();
    }
    
    public void saveGuild(Guild guild) {
        try {
            connection.setAutoCommit(false);
            
            PreparedStatement guildStmt = connection.prepareStatement(
                    "INSERT OR REPLACE INTO guilds (name, tag, tag_color, owner, level, experience, " +
                    "daily_experience, motd, public_guild, created_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            guildStmt.setString(1, guild.getName());
            guildStmt.setString(2, guild.getTag());
            guildStmt.setString(3, guild.getTagColor());
            guildStmt.setString(4, guild.getOwner().toString());
            guildStmt.setInt(5, guild.getLevel());
            guildStmt.setLong(6, guild.getExperience());
            guildStmt.setLong(7, guild.getDailyExperience());
            guildStmt.setString(8, guild.getMotd());
            guildStmt.setBoolean(9, guild.isPublicGuild());
            guildStmt.setLong(10, guild.getCreatedTime());
            guildStmt.executeUpdate();
            guildStmt.close();
            
            PreparedStatement deleteMembersStmt = connection.prepareStatement(
                    "DELETE FROM guild_members WHERE guild_name = ?");
            deleteMembersStmt.setString(1, guild.getName());
            deleteMembersStmt.executeUpdate();
            deleteMembersStmt.close();
            
            for (GuildMember member : guild.getMembers().values()) {
                PreparedStatement memberStmt = connection.prepareStatement(
                        "INSERT INTO guild_members (guild_name, uuid, role, joined_time, " +
                        "total_contribution, daily_contribution, muted, muted_until) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                
                memberStmt.setString(1, guild.getName());
                memberStmt.setString(2, member.getUuid().toString());
                memberStmt.setString(3, member.getRole().name());
                memberStmt.setLong(4, member.getJoinedTime());
                memberStmt.setLong(5, member.getTotalContribution());
                memberStmt.setLong(6, member.getDailyContribution());
                memberStmt.setBoolean(7, member.isMuted());
                memberStmt.setLong(8, member.isMuted() ? 
                        System.currentTimeMillis() + 86400000 : 0);
                memberStmt.executeUpdate();
                memberStmt.close();
            }
            
            connection.commit();
            connection.setAutoCommit(true);
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    
    public void deleteGuild(String name) {
        try {
            connection.setAutoCommit(false);
            
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM guilds WHERE name = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            stmt.close();
            
            connection.commit();
            connection.setAutoCommit(true);
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}