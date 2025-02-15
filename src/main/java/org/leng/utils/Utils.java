package org.leng.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;

import java.util.regex.Pattern;

public class Utils {
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            Lengbanlist.getInstance().getLogger().info(message);
        }
    }


    public static boolean ComputingTime(CommandSender sender, String[] args) {
        long banTimestamp = TimeUtils.parseTime(args[1]);
        if (banTimestamp == -1) {
            Utils.sendMessage(sender,"§c时间格式错误，请使用以下格式:");
            Utils.sendMessage(sender,"§c - 10s: 秒 (10 秒)");
            Utils.sendMessage(sender,"§c - 5m: 分钟 (5 分钟)");
            Utils.sendMessage(sender,"§c - 2h: 小时 (2 小时)");
            Utils.sendMessage(sender,"§c - 7d: 天 (7 天)");
            Utils.sendMessage(sender,"§c - 1w: 周 (1 周，等于 7 天)");
            Utils.sendMessage(sender,"§c - 1M: 月 (1 月，按 30 天计算)");
            Utils.sendMessage(sender,"§c - 1y: 年 (1 年，按 365 天计算)");
            return true;
        }
        Lengbanlist.getInstance().banManager.banPlayer(
                new org.leng.object.BanEntry(args[0], sender.getName(), banTimestamp, args[2])
        );
        return false;
    }

    public static boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null) && (!ipAddress.isEmpty())) {
            return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
        }
        return false;
    }

}