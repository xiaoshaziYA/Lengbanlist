package org.leng.models;

import org.bukkit.command.CommandSender;

public interface Model {
    String getName();

    void showHelp(CommandSender sender);
}