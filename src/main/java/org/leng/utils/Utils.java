package org.leng.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.LengbanList;

public class Utils {
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            LengbanList.getInstance().getLogger().info(message);
        }
    }
}
