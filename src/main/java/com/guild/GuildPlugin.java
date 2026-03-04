package com.guild;

import com.guild.commands.GuildCommand;
import com.guild.commands.GuildChatCommand;
import com.guild.commands.GuildGUICommand;
import com.guild.config.GUIConfig;
import com.guild.database.DatabaseManager;
import com.guild.guild.GuildManager;
import com.guild.listeners.ChatInputListener;
import com.guild.listeners.InventoryListener;
import com.guild.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class GuildPlugin extends JavaPlugin {
    
    private static GuildPlugin instance;
    private DatabaseManager databaseManager;
    private GuildManager guildManager;
    private GUIConfig guiConfig;
    private Map<String, String> messages;
    private String currentLanguage;
    
    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        
        loadLanguage();
        saveLanguageFiles();
        
        guildManager = new GuildManager(this);
        
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();
        
        guiConfig = new GUIConfig(this);
        
        registerCommands();
        registerListeners();
        
        printEnableMessage();
    }
    
    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        printDisableMessage();
    }
    
    private void loadLanguage() {
        String lang = getConfig().getString("language", "zh_cn");
        currentLanguage = lang;
        messages = loadLanguageFile(lang);
        getLogger().info("Loaded language file: " + lang + ".yml");
        getLogger().info("Default language set to: " + lang);
    }
    
    private Map<String, String> loadLanguageFile(String lang) {
        Map<String, String> langMessages = new HashMap<>();
        
        try {
            String fileName = "lang/" + lang + ".yml";
            InputStream inputStream = getResource(fileName);
            
            if (inputStream == null) {
                getLogger().warning("Could not load " + lang + ".yml, using default messages");
                return getDefaultMessages();
            }
            
            File langFile = new File(getDataFolder(), fileName);
            if (!langFile.getParentFile().exists()) {
                langFile.getParentFile().mkdirs();
            }
            
            Files.copy(inputStream, langFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
            
            String content = new String(Files.readAllBytes(langFile.toPath()));
            
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        langMessages.put(key, value);
                    }
                }
            }
            
        } catch (IOException e) {
            getLogger().warning("Could not load language file: " + lang + ".yml");
            e.printStackTrace();
        }
        
        return langMessages;
    }
    
    private Map<String, String> getDefaultMessages() {
        Map<String, String> defaultMessages = new HashMap<>();
        defaultMessages.put("plugin.enabling", "Enabling Guild");
        defaultMessages.put("plugin.enabled", "GuildPlugin has been enabled!");
        defaultMessages.put("plugin.disabling", "Disabling Guild");
        defaultMessages.put("plugin.disabled", "GuildPlugin has been disabled!");
        defaultMessages.put("plugin.author", "GuildPlugin");
        defaultMessages.put("plugin.version", "v" + getDescription().getVersion());
        return defaultMessages;
    }
    
    private void saveLanguageFiles() {
        String[] languages = {"en_US", "zh_cn", "zh_tw", "zh_hk", "de_de", "fr_fr", "ja_jp"};
        
        for (String lang : languages) {
            try {
                String fileName = "lang/" + lang + ".yml";
                InputStream inputStream = getResource(fileName);
                
                if (inputStream != null) {
                    File langFile = new File(getDataFolder(), fileName);
                    if (!langFile.getParentFile().exists()) {
                        langFile.getParentFile().mkdirs();
                    }
                    
                    if (langFile.exists()) {
                        getLogger().warning("Could not save " + lang + ".yml to " + langFile.getPath() + " because " + lang + ".yml already exists.");
                    } else {
                        Files.copy(inputStream, langFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        inputStream.close();
                    }
                }
            } catch (IOException e) {
                getLogger().warning("Could not save language file: " + lang + ".yml");
            }
        }
    }
    
    private void printEnableMessage() {
        String version = getDescription().getVersion();
        String author = getDescription().getAuthors().toString();
        
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "========================================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "  ██████╗  ███████╗  █████╗  ███████╗");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  ██╔══██╗ ██╔════╝ ██╔══██╗ ██╔══██╗");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  ███████╗ █████╗   ██████╔╝ ██║   ██║");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  ██╔══██╗ ██╔══╝   ██╔═══╝  ██║   ██║");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  ██║  ██║ ███████╗ ██║      ╚██████╔╝");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  ╚═╝  ╚═╝ ╚══════╝ ╚═╝       ╚═════╝");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "========================================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "  " + ChatColor.BOLD + "GUILD " + messages.getOrDefault("plugin.version", "v" + version));
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "  " + messages.getOrDefault("plugin.enabling", "Enabling Guild"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "========================================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  " + messages.getOrDefault("plugin.enabled", "GuildPlugin has been enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "  " + messages.getOrDefault("plugin.author", "By GuildPlugin"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "  Loaded " + guildManager.getGuilds().size() + " guilds");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "========================================");
        Bukkit.getConsoleSender().sendMessage("");
    }
    
    private void printDisableMessage() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "========================================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "  " + messages.getOrDefault("plugin.disabling", "Disabling Guild"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "  " + messages.getOrDefault("plugin.disabled", "GuildPlugin has been disabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "========================================");
        Bukkit.getConsoleSender().sendMessage("");
    }
    
    private void registerCommands() {
        getCommand("guild").setExecutor(new GuildCommand(this));
        getCommand("guildchat").setExecutor(new GuildChatCommand(this));
        getCommand("guildgui").setExecutor(new GuildGUICommand(this));
    }
    
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatInputListener(this), this);
    }
    
    public static GuildPlugin getInstance() {
        return instance;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public GuildManager getGuildManager() {
        return guildManager;
    }
    
    public GUIConfig getGUIConfig() {
        return guiConfig;
    }
}