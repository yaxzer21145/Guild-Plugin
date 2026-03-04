package com.guild.utils;

import org.bukkit.entity.Player;

public class MessageUtils {
    
    public static void sendClickableMessage(Player player, String message, String clickableText, String command, String hoverText) {
        VersionCompat.sendClickableMessage(player, message, clickableText, command, hoverText);
    }
    
    public static void sendAcceptDeclineMessage(Player player, String prefix, String acceptCommand, String declineCommand) {
        VersionCompat.sendAcceptDeclineMessage(player, prefix, acceptCommand, declineCommand);
    }
    
    public static void sendAcceptMessage(Player player, String prefix, String acceptCommand) {
        VersionCompat.sendAcceptMessage(player, prefix, acceptCommand);
    }
}
