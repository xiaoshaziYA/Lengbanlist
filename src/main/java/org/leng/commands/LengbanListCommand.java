package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.LengbanList;
import org.leng.object.BanEntry;
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
        if (sender instanceof Player) {
            if (!sender.isOp()){
                return false;
            }
        }

        if (args.length == 0) {
            Utils.sendMessage(sender,plugin.prefix() + "§c§l错误的命令格式，正确格式/lban [子命令]§f，\n" + plugin.prefix() + "§c§l请输入/lban help打开帮助信息！");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                if (!sender.hasPermission("lengban.toggle") && !(sender instanceof Player)){
                    return false;
                }
                Utils.sendMessage(sender,LengbanList.getInstance().toggleBroadcast());
                break;
            case "a":
                if (!sender.hasPermission("lengban.a") && !(sender instanceof Player)){
                    return false;
                }
                LengbanList.getInstance().getServer().broadcastMessage(LengbanList.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(LengbanList.getInstance().banManager.getBanList().size())));
                break;
            case "list":
                showBanList(sender);
                break;
            case "reload":
                if (!sender.hasPermission("lengban.reload") && !(sender instanceof Player)){
                    return false;
                }
                plugin.reloadConfig();
                Utils.sendMessage(sender,plugin.prefix() + "§a配置已重新加载。");
                break;
            case "add":
                if (!sender.hasPermission("lengban.ban") && !(sender instanceof Player)){
                    return false;
                }
                if (args.length < 4) {
                    Utils.sendMessage(sender,plugin.prefix() + "§c§l错误的命令格式，正确格式/lban add <玩家名> <天数> <原因>");
                    return true;
                }
                LengbanList.getInstance().banManager.banPlayer(new org.leng.object.BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.valueOf(args[2])), args[3]));
                Utils.sendMessage(sender,"§a成功封禁" + args[1]);
                break;
            case "remove":
                if (!sender.hasPermission("lengban.unban") && !(sender instanceof Player)){
                    return false;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender,plugin.prefix() + "§c§l错误的命令格式，正确格式/lban remove <玩家名>");
                    return true;
                }
                LengbanList.getInstance().banManager.unbanPlayer(args[1]);
                Utils.sendMessage(sender,"§a成功解封" + args[1]);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                Utils.sendMessage(sender,"未知的子命令。");
                break;
        }
        return true;
    }

    /**
     * 显示封禁列表的方法
     * @param sender
     */
    private void showBanList(CommandSender sender) {
        Utils.sendMessage(sender,"§7--§bLengbanList§7--");
        for (BanEntry entry : LengbanList.getInstance().banManager.getBanList()) {
            Utils.sendMessage(sender,"§9§o被封禁者: " + entry.getTarget() +" §6处理人: "+entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
    }

    /**
     * 显示帮助信息的方法
     * @param sender
     */
    private void showHelp(CommandSender sender) {
        Utils.sendMessage(sender,"§bLengbanList §2§o帮助信息:");
        Utils.sendMessage(sender,"§b§l/lban list - §3§o显示封禁列表");
        Utils.sendMessage(sender,"§b§l/lban a - §3§o立即广播当前封禁人数");
        Utils.sendMessage(sender,"§b§l/lban toggle - §3§o开启/关闭 自动广播");
        Utils.sendMessage(sender,"§b§l/lban reload - §3§o重载插件配置");
        Utils.sendMessage(sender,"§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁");
        Utils.sendMessage(sender,"§b§l/lban remove <玩家名> - §3§o移除封禁");
        Utils.sendMessage(sender,"§6推荐使用/lban 来添加封禁和解封，");
        Utils.sendMessage(sender,"§6否则功能将无法实现！");
        Utils.sendMessage(sender,"§6当前版本: 1.3");
        Utils.sendMessage(sender,"§6授权: §bColorFulCraft §fNetwork");
    }
}
