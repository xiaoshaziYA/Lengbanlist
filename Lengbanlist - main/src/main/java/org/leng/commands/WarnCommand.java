package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.manager.WarnManager;
import org.leng.models.Model;
import org.leng.utils.Utils;

public class WarnCommand extends Command {
    private final Lengbanlist plugin;

    public WarnCommand(Lengbanlist plugin) {
        super("warn");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        // 检查权限
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() && !player.hasPermission("lengban.warn")) {
                Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                return false;
            }
        }

        // 检查参数长度
        if (args.length < 3) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /lban warn <玩家名> <原因>");
            return false;
        }

        String target = args[1];
        String reason = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);

        // 获取当前模型
        Model currentModel = plugin.getModelManager().getCurrentModel();
        WarnManager warnManager = plugin.getWarnManager();

        // 添加警告
        int warnCount = warnManager.warnPlayer(target, reason);
        Utils.sendMessage(sender, currentModel.addWarn(target, reason));

        // 检查警告次数是否超过3次
        if (warnCount >= 3) {
            // 自动封禁玩家
            plugin.getBanManager().banPlayer(new org.leng.object.BanEntry(target, sender.getName(), Long.MAX_VALUE, "警告次数过多"));
            Utils.sendMessage(sender, plugin.prefix() + "§c警告次数过多，已自动进行账户保护，请联系管理员处理。");
            Utils.sendMessage(sender, currentModel.addBan(target, -1, "警告次数过多"));
        }

        return true;
    }
}