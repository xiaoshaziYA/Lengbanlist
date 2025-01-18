package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.LengbanList;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player){
            if (!sender.isOp()) {
                return false;
            }
        }
        if (args.length < 3) {
            Utils.sendMessage(sender,"§c用法错误: /ban <玩家> <时间> <原因>");
            return false;
        }
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
            return false;
        }
        LengbanList.getInstance().banManager.banPlayer(
                new org.leng.object.BanEntry(args[0], sender.getName(), banTimestamp, args[2])
        );
        Utils.sendMessage(sender,"§l§a成功封禁 玩家: " + args[0] + "，时长: " + args[1]);
        return true;
    }
}
