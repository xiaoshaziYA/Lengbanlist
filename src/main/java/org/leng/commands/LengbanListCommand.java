package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.LengbanList;
import org.leng.object.BanEntry;
import org.leng.manager.ModelManager;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class LengbanListCommand extends Command {

    private final LengbanList plugin;

    public LengbanListCommand(String name, LengbanList plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                if (!sender.hasPermission("lengbanlist.toggle")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                Utils.sendMessage(sender, LengbanList.getInstance().toggleBroadcast());
                break;
            case "a":
                if (!sender.hasPermission("lengbanlist.broadcast")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                LengbanList.getInstance().getServer().broadcastMessage(LengbanList.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(LengbanList.getInstance().banManager.getBanList().size())));
                break;
            case "list":
                if (!sender.hasPermission("lengbanlist.list")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                showBanList(sender);
                break;
            case "reload":
                if (!sender.hasPermission("lengbanlist.reload")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                plugin.reloadConfig();
                Utils.sendMessage(sender, plugin.prefix() + "§a配置已重新加载。");
                break;
            case "add":
                if (!sender.hasPermission("lengbanlist.ban")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 4) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban add <玩家名> <天数> <原因>");
                    return true;
                }
                LengbanList.getInstance().banManager.banPlayer(new BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.valueOf(args[2])), args[3]));
                Utils.sendMessage(sender, "§a成功封禁" + args[1]);
                break;
            case "remove":
                if (!sender.hasPermission("lengbanlist.unban")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban remove <玩家名>");
                    return true;
                }
                LengbanList.getInstance().banManager.unbanPlayer(args[1]);
                Utils.sendMessage(sender, "§a成功解封" + args[1]);
                break;
            case "help":
                if (!sender.hasPermission("lengbanlist.help")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                showHelp(sender);
                break;
            case "model":
                if (!sender.hasPermission("lengbanlist.model")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban model <模型名称>");
                    Utils.sendMessage(sender, plugin.prefix() + "§6§l可用模型： §b胡桃_Hu_Tao 芙宁娜_Fu_Ningna ");
                    return true;
                }
                ModelManager.switchModel(args[1]);
                break;
            default:
                Utils.sendMessage(sender, "未知的子命令。");
                break;
        }
        return true;
    }

    private void showBanList(CommandSender sender) {
        Utils.sendMessage(sender, "§7--§bLengbanList§7--");
        for (BanEntry entry : LengbanList.getInstance().banManager.getBanList()) {
            Utils.sendMessage(sender, "§9§o被封禁者: " + entry.getTarget() + " §6处理人: " + entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
    }

    private void showHelp(CommandSender sender) {
        ModelManager.getCurrentModel().showHelp(sender);
    }
}