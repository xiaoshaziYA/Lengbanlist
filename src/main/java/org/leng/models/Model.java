package org.leng.models;

import org.bukkit.command.CommandSender;

public interface Model {
    String getName();

    void showHelp(CommandSender sender);

    String toggleBroadcast(boolean enabled);

    String reloadConfig();

    String addBan(String player, int days, String reason);

    String removeBan(String player);
}