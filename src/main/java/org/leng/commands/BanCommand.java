package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() || !(player.hasPermission("lengban.ban"))) {
                return false;
            }
        }
        if (Lengbanlist.getInstance().banManager.isPlayerBanned(args[0])){
            Utils.sendMessage(sender,"§c玩家 " + args[0] + " 已经被封禁");
            return false;
        }
        if (args.length < 3) {
            Utils.sendMessage(sender,"§c用法错误: /ban <玩家> <时间> <原因>");
            Utils.sendMessage(sender,"§c默认时间单位为天（若以天为单位请勿添加单位）");            
            return false;
        }
        if (Utils.ComputingTime(sender, args)) return false;
        Utils.sendMessage(sender,"§l§a成功封禁 玩家: " + args[0] + "，时长: " + args[1]);
        return true;
    }
}
