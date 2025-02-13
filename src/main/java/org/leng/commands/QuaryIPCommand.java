package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.leng.utils.SaveIP;

public class QuaryIPCommand extends Command {
    public QuaryIPCommand() {
        super("quaryip");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("§c§l错误的命令格式，正确格式/quaryip <玩家名>");
            return false;
        }
        String target = strings[0];
        String ip = SaveIP.getIP(target);
        if (ip == null) {
            commandSender.sendMessage("§c§l查询不到玩家 " + target + " 的IP地址");
        } else {
            commandSender.sendMessage("§a查询到玩家 " + target + " 的IP地址为 " + ip);
        }
        return false;
    }
}
