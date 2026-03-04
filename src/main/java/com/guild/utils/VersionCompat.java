package com.guild.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class VersionCompat {
    
    private static final String VERSION;
    private static final int MAJOR_VERSION;
    private static final int MINOR_VERSION;
    private static final boolean IS_LEGACY;
    
    private static Material PLAYER_HEAD_MATERIAL;
    private static Material BARRIER_MATERIAL;
    private static Material NAME_TAG_MATERIAL;
    private static Material ANVIL_MATERIAL;
    private static Material ARROW_MATERIAL;
    private static Material DIAMOND_MATERIAL;
    private static Material BOOK_MATERIAL;
    private static Material DIAMOND_BLOCK_MATERIAL;
    private static Material PAPER_MATERIAL;
    private static Material REDSTONE_MATERIAL;
    private static Material LEVER_MATERIAL;
    private static Material NOTE_BLOCK_MATERIAL;
    private static Material COMMAND_BLOCK_MATERIAL;
    private static Material GOLDEN_HELMET_MATERIAL;
    private static Material IRON_HELMET_MATERIAL;
    
    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        String[] parts = VERSION.split("_");
        MAJOR_VERSION = Integer.parseInt(parts[1]);
        MINOR_VERSION = parts.length > 2 ? Integer.parseInt(parts[2].replace("R", "")) : 0;
        
        IS_LEGACY = MAJOR_VERSION < 13;
        
        initializeMaterials();
    }
    
    private static void initializeMaterials() {
        if (IS_LEGACY) {
            PLAYER_HEAD_MATERIAL = getMaterialSafe("SKULL_ITEM", "PLAYER_HEAD");
            BARRIER_MATERIAL = getMaterialSafe("BARRIER", "BEDROCK");
            NAME_TAG_MATERIAL = getMaterialSafe("NAME_TAG", "PAPER");
            ANVIL_MATERIAL = getMaterialSafe("ANVIL", "IRON_BLOCK");
            ARROW_MATERIAL = getMaterialSafe("ARROW", "STICK");
            DIAMOND_MATERIAL = getMaterialSafe("DIAMOND", "EMERALD");
            BOOK_MATERIAL = getMaterialSafe("BOOK", "PAPER");
            DIAMOND_BLOCK_MATERIAL = getMaterialSafe("DIAMOND_BLOCK", "EMERALD_BLOCK");
            PAPER_MATERIAL = getMaterialSafe("PAPER", "BOOK");
            REDSTONE_MATERIAL = getMaterialSafe("REDSTONE", "REDSTONE_BLOCK");
            LEVER_MATERIAL = getMaterialSafe("LEVER", "STICK");
            NOTE_BLOCK_MATERIAL = getMaterialSafe("NOTE_BLOCK", "JUKEBOX");
            COMMAND_BLOCK_MATERIAL = getMaterialSafe("COMMAND", "COMMAND_BLOCK");
            GOLDEN_HELMET_MATERIAL = getMaterialSafe("GOLD_HELMET", "GOLDEN_HELMET");
            IRON_HELMET_MATERIAL = getMaterialSafe("IRON_HELMET", "IRON_BLOCK");
        } else {
            PLAYER_HEAD_MATERIAL = Material.PLAYER_HEAD;
            BARRIER_MATERIAL = Material.BARRIER;
            NAME_TAG_MATERIAL = Material.NAME_TAG;
            ANVIL_MATERIAL = Material.ANVIL;
            ARROW_MATERIAL = Material.ARROW;
            DIAMOND_MATERIAL = Material.DIAMOND;
            BOOK_MATERIAL = Material.BOOK;
            DIAMOND_BLOCK_MATERIAL = Material.DIAMOND_BLOCK;
            PAPER_MATERIAL = Material.PAPER;
            REDSTONE_MATERIAL = Material.REDSTONE;
            LEVER_MATERIAL = Material.LEVER;
            NOTE_BLOCK_MATERIAL = Material.NOTE_BLOCK;
            COMMAND_BLOCK_MATERIAL = Material.COMMAND_BLOCK;
            GOLDEN_HELMET_MATERIAL = Material.GOLDEN_HELMET;
            IRON_HELMET_MATERIAL = Material.IRON_HELMET;
        }
    }
    
    private static Material getMaterialSafe(String... names) {
        for (String name : names) {
            try {
                return Material.valueOf(name);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Material.STONE;
    }
    
    public static String getVersion() {
        return VERSION;
    }
    
    public static int getMajorVersion() {
        return MAJOR_VERSION;
    }
    
    public static int getMinorVersion() {
        return MINOR_VERSION;
    }
    
    public static boolean isLegacy() {
        return IS_LEGACY;
    }
    
    public static boolean isVersion(int major) {
        return MAJOR_VERSION == major;
    }
    
    public static boolean isVersionAtLeast(int major) {
        return MAJOR_VERSION >= major;
    }
    
    public static boolean isVersionBetween(int minMajor, int maxMajor) {
        return MAJOR_VERSION >= minMajor && MAJOR_VERSION <= maxMajor;
    }
    
    public static Material getPlayerHeadMaterial() {
        return PLAYER_HEAD_MATERIAL;
    }
    
    public static Material getBarrierMaterial() {
        return BARRIER_MATERIAL;
    }
    
    public static Material getNameTagMaterial() {
        return NAME_TAG_MATERIAL;
    }
    
    public static Material getAnvilMaterial() {
        return ANVIL_MATERIAL;
    }
    
    public static Material getArrowMaterial() {
        return ARROW_MATERIAL;
    }
    
    public static Material getDiamondMaterial() {
        return DIAMOND_MATERIAL;
    }
    
    public static Material getBookMaterial() {
        return BOOK_MATERIAL;
    }
    
    public static Material getDiamondBlockMaterial() {
        return DIAMOND_BLOCK_MATERIAL;
    }
    
    public static Material getPaperMaterial() {
        return PAPER_MATERIAL;
    }
    
    public static Material getRedstoneMaterial() {
        return REDSTONE_MATERIAL;
    }
    
    public static Material getLeverMaterial() {
        return LEVER_MATERIAL;
    }
    
    public static Material getNoteBlockMaterial() {
        return NOTE_BLOCK_MATERIAL;
    }
    
    public static Material getCommandBlockMaterial() {
        return COMMAND_BLOCK_MATERIAL;
    }
    
    public static Material getGoldenHelmetMaterial() {
        return GOLDEN_HELMET_MATERIAL;
    }
    
    public static Material getIronHelmetMaterial() {
        return IRON_HELMET_MATERIAL;
    }
    
    public static ItemStack createPlayerHead(UUID uuid, String name, String... lore) {
        ItemStack item = new ItemStack(PLAYER_HEAD_MATERIAL);
        
        if (IS_LEGACY) {
            item.setDurability((short) 3);
        }
        
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        
        if (isVersionAtLeast(12)) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        } else {
            setSkullOwnerLegacy(meta, uuid);
        }
        
        meta.setDisplayName(name);
        
        if (lore.length > 0) {
            java.util.List<String> loreList = new java.util.ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    private static void setSkullOwnerLegacy(SkullMeta meta, UUID uuid) {
        try {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            if (name != null) {
                meta.setOwner(name);
            }
        } catch (Exception e) {
            try {
                Method method = meta.getClass().getDeclaredMethod("setOwner", String.class);
                String name = Bukkit.getOfflinePlayer(uuid).getName();
                if (name != null) {
                    method.invoke(meta, name);
                }
            } catch (Exception ignored) {
            }
        }
    }
    
    public static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        if (lore.length > 0) {
            java.util.List<String> loreList = new java.util.ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    public static void sendClickableMessage(Player player, String message, String clickableText, String command, String hoverText) {
        if (isVersionAtLeast(8)) {
            try {
                sendSpigotClickableMessage(player, message, clickableText, command, hoverText);
            } catch (Exception e) {
                player.sendMessage(message + " " + clickableText + " (" + command + ")");
            }
        } else {
            player.sendMessage(message + " " + clickableText + " (" + command + ")");
        }
    }
    
    public static void sendAcceptDeclineMessage(Player player, String prefix, String acceptCommand, String declineCommand) {
        if (isVersionAtLeast(8)) {
            try {
                sendSpigotAcceptDeclineMessage(player, prefix, acceptCommand, declineCommand);
            } catch (Exception e) {
                player.sendMessage(prefix + " [接受](" + acceptCommand + ") [拒绝](" + declineCommand + ")");
            }
        } else {
            player.sendMessage(prefix + " [接受](" + acceptCommand + ") [拒绝](" + declineCommand + ")");
        }
    }
    
    public static void sendAcceptMessage(Player player, String prefix, String acceptCommand) {
        if (isVersionAtLeast(8)) {
            try {
                sendSpigotAcceptMessage(player, prefix, acceptCommand);
            } catch (Exception e) {
                player.sendMessage(prefix + " [接受](" + acceptCommand + ")");
            }
        } else {
            player.sendMessage(prefix + " [接受](" + acceptCommand + ")");
        }
    }
    
    private static void sendSpigotClickableMessage(Player player, String message, String clickableText, String command, String hoverText) {
        try {
            Class<?> chatColorClass = Class.forName("net.md_5.bungee.api.ChatColor");
            Class<?> clickEventClass = Class.forName("net.md_5.bungee.api.chat.ClickEvent");
            Class<?> hoverEventClass = Class.forName("net.md_5.bungee.api.chat.HoverEvent");
            Class<?> componentBuilderClass = Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
            Class<?> textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            
            Object white = chatColorClass.getMethod("valueOf", String.class).invoke(null, "WHITE");
            Object green = chatColorClass.getMethod("valueOf", String.class).invoke(null, "GREEN");
            Object gray = chatColorClass.getMethod("valueOf", String.class).invoke(null, "GRAY");
            
            Object component = textComponentClass.getConstructor(String.class).newInstance(message + " ");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(component, white);
            
            Object clickable = textComponentClass.getConstructor(String.class).newInstance(clickableText);
            textComponentClass.getMethod("setColor", chatColorClass).invoke(clickable, green);
            textComponentClass.getMethod("setBold", boolean.class).invoke(clickable, true);
            
            Object clickAction = clickEventClass.getField("Action").get(null);
            Object[] actions = (Object[]) clickAction.getClass().getMethod("values").invoke(null);
            Object runCommandAction = null;
            for (Object action : actions) {
                if (action.toString().equals("RUN_COMMAND")) {
                    runCommandAction = action;
                    break;
                }
            }
            
            if (runCommandAction != null) {
                Object clickEvent = clickEventClass.getConstructor(runCommandAction.getClass(), String.class)
                        .newInstance(runCommandAction, command);
                textComponentClass.getMethod("setClickEvent", clickEventClass).invoke(clickable, clickEvent);
            }
            
            Object builder = componentBuilderClass.getConstructor(String.class).newInstance(hoverText);
            componentBuilderClass.getMethod("color", chatColorClass).invoke(builder, gray);
            Object[] hoverTextComponents = (Object[]) componentBuilderClass.getMethod("create").invoke(builder);
            
            Object showTextAction = null;
            Object hoverAction = hoverEventClass.getField("Action").get(null);
            Object[] hoverActions = (Object[]) hoverAction.getClass().getMethod("values").invoke(null);
            for (Object action : hoverActions) {
                if (action.toString().equals("SHOW_TEXT")) {
                    showTextAction = action;
                    break;
                }
            }
            
            if (showTextAction != null) {
                Object hoverEvent = hoverEventClass.getConstructor(showTextAction.getClass(), hoverTextComponents.getClass())
                        .newInstance(showTextAction, hoverTextComponents);
                textComponentClass.getMethod("setHoverEvent", hoverEventClass).invoke(clickable, hoverEvent);
            }
            
            player.spigot().sendMessage((net.md_5.bungee.api.chat.TextComponent) component, 
                    (net.md_5.bungee.api.chat.TextComponent) clickable);
        } catch (Exception e) {
            player.sendMessage(message + " " + clickableText + " (" + command + ")");
        }
    }
    
    private static void sendSpigotAcceptDeclineMessage(Player player, String prefix, String acceptCommand, String declineCommand) {
        try {
            Class<?> chatColorClass = Class.forName("net.md_5.bungee.api.ChatColor");
            Class<?> clickEventClass = Class.forName("net.md_5.bungee.api.chat.ClickEvent");
            Class<?> hoverEventClass = Class.forName("net.md_5.bungee.api.chat.HoverEvent");
            Class<?> componentBuilderClass = Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
            Class<?> textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            
            Object yellow = chatColorClass.getMethod("valueOf", String.class).invoke(null, "YELLOW");
            Object green = chatColorClass.getMethod("valueOf", String.class).invoke(null, "GREEN");
            Object red = chatColorClass.getMethod("valueOf", String.class).invoke(null, "RED");
            Object gray = chatColorClass.getMethod("valueOf", String.class).invoke(null, "GRAY");
            Object white = chatColorClass.getMethod("valueOf", String.class).invoke(null, "WHITE");
            
            Object component = textComponentClass.getConstructor(String.class).newInstance(prefix + " ");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(component, yellow);
            
            Object accept = textComponentClass.getConstructor(String.class).newInstance("[接受]");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(accept, green);
            textComponentClass.getMethod("setBold", boolean.class).invoke(accept, true);
            
            Object clickAction = clickEventClass.getField("Action").get(null);
            Object[] actions = (Object[]) clickAction.getClass().getMethod("values").invoke(null);
            Object runCommandAction = null;
            for (Object action : actions) {
                if (action.toString().equals("RUN_COMMAND")) {
                    runCommandAction = action;
                    break;
                }
            }
            
            if (runCommandAction != null) {
                Object clickEvent = clickEventClass.getConstructor(runCommandAction.getClass(), String.class)
                        .newInstance(runCommandAction, acceptCommand);
                textComponentClass.getMethod("setClickEvent", clickEventClass).invoke(accept, clickEvent);
            }
            
            Object builder = componentBuilderClass.getConstructor(String.class).newInstance("点击接受");
            componentBuilderClass.getMethod("color", chatColorClass).invoke(builder, gray);
            Object[] hoverTextComponents = (Object[]) componentBuilderClass.getMethod("create").invoke(builder);
            
            Object showTextAction = null;
            Object hoverAction = hoverEventClass.getField("Action").get(null);
            Object[] hoverActions = (Object[]) hoverAction.getClass().getMethod("values").invoke(null);
            for (Object action : hoverActions) {
                if (action.toString().equals("SHOW_TEXT")) {
                    showTextAction = action;
                    break;
                }
            }
            
            if (showTextAction != null) {
                Object hoverEvent = hoverEventClass.getConstructor(showTextAction.getClass(), hoverTextComponents.getClass())
                        .newInstance(showTextAction, hoverTextComponents);
                textComponentClass.getMethod("setHoverEvent", hoverEventClass).invoke(accept, hoverEvent);
            }
            
            Object space = textComponentClass.getConstructor(String.class).newInstance(" ");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(space, white);
            
            Object decline = textComponentClass.getConstructor(String.class).newInstance("[拒绝]");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(decline, red);
            textComponentClass.getMethod("setBold", boolean.class).invoke(decline, true);
            
            if (runCommandAction != null) {
                Object clickEvent = clickEventClass.getConstructor(runCommandAction.getClass(), String.class)
                        .newInstance(runCommandAction, declineCommand);
                textComponentClass.getMethod("setClickEvent", clickEventClass).invoke(decline, clickEvent);
            }
            
            builder = componentBuilderClass.getConstructor(String.class).newInstance("点击拒绝");
            componentBuilderClass.getMethod("color", chatColorClass).invoke(builder, gray);
            hoverTextComponents = (Object[]) componentBuilderClass.getMethod("create").invoke(builder);
            
            if (showTextAction != null) {
                Object hoverEvent = hoverEventClass.getConstructor(showTextAction.getClass(), hoverTextComponents.getClass())
                        .newInstance(showTextAction, hoverTextComponents);
                textComponentClass.getMethod("setHoverEvent", hoverEventClass).invoke(decline, hoverEvent);
            }
            
            player.spigot().sendMessage(
                    (net.md_5.bungee.api.chat.TextComponent) component,
                    (net.md_5.bungee.api.chat.TextComponent) accept,
                    (net.md_5.bungee.api.chat.TextComponent) space,
                    (net.md_5.bungee.api.chat.TextComponent) decline);
        } catch (Exception e) {
            player.sendMessage(prefix + " [接受](" + acceptCommand + ") [拒绝](" + declineCommand + ")");
        }
    }
    
    private static void sendSpigotAcceptMessage(Player player, String prefix, String acceptCommand) {
        try {
            Class<?> chatColorClass = Class.forName("net.md_5.bungee.api.ChatColor");
            Class<?> clickEventClass = Class.forName("net.md_5.bungee.api.chat.ClickEvent");
            Class<?> hoverEventClass = Class.forName("net.md_5.bungee.api.chat.HoverEvent");
            Class<?> componentBuilderClass = Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
            Class<?> textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            
            Object yellow = chatColorClass.getMethod("valueOf", String.class).invoke(null, "YELLOW");
            Object green = chatColorClass.getMethod("valueOf", String.class).invoke(null, "GREEN");
            Object gray = chatColorClass.getMethod("valueOf", String.class).invoke(null, "GRAY");
            
            Object component = textComponentClass.getConstructor(String.class).newInstance(prefix + " ");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(component, yellow);
            
            Object accept = textComponentClass.getConstructor(String.class).newInstance("[接受]");
            textComponentClass.getMethod("setColor", chatColorClass).invoke(accept, green);
            textComponentClass.getMethod("setBold", boolean.class).invoke(accept, true);
            
            Object clickAction = clickEventClass.getField("Action").get(null);
            Object[] actions = (Object[]) clickAction.getClass().getMethod("values").invoke(null);
            Object runCommandAction = null;
            for (Object action : actions) {
                if (action.toString().equals("RUN_COMMAND")) {
                    runCommandAction = action;
                    break;
                }
            }
            
            if (runCommandAction != null) {
                Object clickEvent = clickEventClass.getConstructor(runCommandAction.getClass(), String.class)
                        .newInstance(runCommandAction, acceptCommand);
                textComponentClass.getMethod("setClickEvent", clickEventClass).invoke(accept, clickEvent);
            }
            
            Object builder = componentBuilderClass.getConstructor(String.class).newInstance("点击接受");
            componentBuilderClass.getMethod("color", chatColorClass).invoke(builder, gray);
            Object[] hoverTextComponents = (Object[]) componentBuilderClass.getMethod("create").invoke(builder);
            
            Object showTextAction = null;
            Object hoverAction = hoverEventClass.getField("Action").get(null);
            Object[] hoverActions = (Object[]) hoverAction.getClass().getMethod("values").invoke(null);
            for (Object action : hoverActions) {
                if (action.toString().equals("SHOW_TEXT")) {
                    showTextAction = action;
                    break;
                }
            }
            
            if (showTextAction != null) {
                Object hoverEvent = hoverEventClass.getConstructor(showTextAction.getClass(), hoverTextComponents.getClass())
                        .newInstance(showTextAction, hoverTextComponents);
                textComponentClass.getMethod("setHoverEvent", hoverEventClass).invoke(accept, hoverEvent);
            }
            
            player.spigot().sendMessage(
                    (net.md_5.bungee.api.chat.TextComponent) component,
                    (net.md_5.bungee.api.chat.TextComponent) accept);
        } catch (Exception e) {
            player.sendMessage(prefix + " [接受](" + acceptCommand + ")");
        }
    }
}
