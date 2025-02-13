package org.leng.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;

public class Utils {
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage(message);
        }
    }
}
