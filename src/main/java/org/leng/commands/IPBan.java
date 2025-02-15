package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.utils.SaveIP;
import org.leng.utils.Utils;

public class IPBan extends Command {
    public IPBan() {
        super("ban-ip");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() || !(player.hasPermission("lengban.banip"))) {
                return false;
            }
        }
        String ip;
        if (!Utils.isValidIPAddress(args[0])){
            ip = SaveIP.getIP(sender.getName());
        } else {
            ip = args[0];
        }
        if (Lengbanlist.getInstance().banManager.isPlayerBanned(ip)){
            Utils.sendMessage(sender,"§cIP " + ip + " 已经被封禁");
            return false;
        }
        if (args.length < 3) {
            Utils.sendMessage(sender,"§c用法错误: /ban <IP> <时间> <原因>");
            Utils.sendMessage(sender,"§c默认时间单位为天（若以天为单位请勿添加单位）");
            return false;
        }
        if (Utils.ComputingTime(sender, args)) return false;
        Utils.sendMessage(sender,"§l§a成功封禁 IP: " + ip + "，时长: " + args[1]);
        return false;
    }

}
