package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.object.BanEntry;
import org.leng.manager.ModelManager;
import org.leng.models.Model;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class LengbanlistCommand extends Command {

    private final Lengbanlist plugin;

    public LengbanlistCommand(String name, Lengbanlist plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Model currentModel = ModelManager.getInstance().getCurrentModel();
        if (args.length == 0) {
            currentModel.showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                if (!sender.hasPermission("lengbanlist.toggle")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                boolean enabled = !Lengbanlist.getInstance().isBroadcastEnabled();
                Lengbanlist.getInstance().setBroadcastEnabled(enabled);
                Utils.sendMessage(sender, currentModel.toggleBroadcast(enabled));
                break;
            case "a":
                if (!sender.hasPermission("lengbanlist.broadcast")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                Lengbanlist.getInstance().getServer().broadcastMessage(Lengbanlist.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(Lengbanlist.getInstance().banManager.getBanList().size())));
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
                Utils.sendMessage(sender, currentModel.reloadConfig());
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
                Lengbanlist.getInstance().banManager.banPlayer(new BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.valueOf(args[2])), args[3]));
                Utils.sendMessage(sender, currentModel.addBan(args[1], Integer.valueOf(args[2]), args[3]));
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
                Lengbanlist.getInstance().banManager.unbanPlayer(args[1]);
                Utils.sendMessage(sender, currentModel.removeBan(args[1]));
                break;
            case "help":
                if (!sender.hasPermission("lengbanlist.help")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                currentModel.showHelp(sender);
                break;
            case "model":
                if (!sender.hasPermission("lengbanlist.model")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban model <模型名称>");
                    Utils.sendMessage(sender, plugin.prefix() + "§6§l可用模型： §b Default HuTao FuNingna Zhongli Keqing Xiao Ayaka");
                    return true;
                }
                String modelName = args[1];
                if (!Lengbanlist.getInstance().getConfig().getString("valid-models").contains(modelName)) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不支持的模型名称。可用模型： §b Default HuTao FuNingna Zhongli Keqing Xiao Ayaka");
                    return true;
                }
                ModelManager.getInstance().switchModel(modelName);
                Utils.sendMessage(sender, plugin.prefix() + "§a已切换到模型: " + modelName);
                break;
            default:
                Utils.sendMessage(sender, "未知的子命令。");
                break;
        }
        return true;
    }

    private void showBanList(CommandSender sender) {
        Utils.sendMessage(sender, "§7--§bLengbanlist§7--");
        for (BanEntry entry : Lengbanlist.getInstance().banManager.getBanList()) {
            Utils.sendMessage(sender, "§9§o被封禁者: " + entry.getTarget() + " §6处理人: " + entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
    }
}