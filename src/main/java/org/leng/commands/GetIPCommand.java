package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.leng.utils.SaveIP;

public class GetIPCommand extends Command {
    public GetIPCommand() {
        super("lban getIP"); // 修改命令名称为 /lban getIP
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("§c§l错误的命令格式，正确格式 /lban getIP <玩家名>");
            return false;
        }
        String target = strings[0];
        String ip = SaveIP.getIP(target);
        if (ip == null) {
            commandSender.sendMessage("§c§l查询不到玩家 " + target + " 的 IP 地址");
        } else {
            commandSender.sendMessage("§a查询到玩家 " + target + " 的 IP 地址为 " + ip);
        }
        return true; // 返回值为 true 表示命令成功执行
    }
}