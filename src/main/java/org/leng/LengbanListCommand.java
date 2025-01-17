package org.leng;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.leng.object.BanEntry;
import org.leng.utils.TimeUtils;

public class LengbanListCommand extends Command {

    private final LengbanList plugin;

    public LengbanListCommand(String name, LengbanList plugin) {
        super(name);
        this.plugin = plugin;
    }

    // 显示封禁列表的方法
    private void showBanList(CommandSender sender) {
        sender.sendMessage("§7--§bLengbanList§7--");
        for (BanEntry entry : LengbanList.getInstance().banManager.getBanList()) {
            sender.sendMessage("§9§o被封禁者: " + entry.getTarget() +" §6处理人: "+entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
    }

    // 显示帮助信息的方法
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§bLengbanList §2§o帮助信息:");
        sender.sendMessage("§b§l/lban list - §3§o显示封禁列表");
        sender.sendMessage("§b§l/lban broadcast - §3§o立即广播当前封禁人数");
        sender.sendMessage("§b§l/lban reload - §3§o重载插件配置");
        sender.sendMessage("§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁");
        sender.sendMessage("§b§l/lban remove <玩家名> - §3§o移除封禁");
        sender.sendMessage("§6推荐使用/lban 来添加封禁和解封，");
        sender.sendMessage("§6否则功能将无法实现！");
        sender.sendMessage("§6当前版本: 1.1");
        sender.sendMessage("§6授权: §bColorFulCraft §fNetwork");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        // 检查是否为控制台或有权限的玩家
        if (!(sender.isOp() || sender.hasPermission("lengbanlist.admin") || sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(plugin.prefix() + "§c§l你没有权限执行此命令！");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.prefix() + "§c§l错误的命令格式，正确格式/lban [子命令]§f，\n" + plugin.prefix() + "§c§l请输入/lban help打开帮助信息！");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "broadcast":
                // 广播当前封禁人数
                LengbanList.getInstance().getServer().broadcastMessage(LengbanList.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(LengbanList.getInstance().banManager.getBanList().size())));
                break;
            case "list":
                showBanList(sender);
                break;
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(plugin.prefix() + "§a配置已重新加载。");
                break;
            case "add":
                if (args.length < 4) {
                    sender.sendMessage(plugin.prefix() + "§c§l错误的命令格式，正确格式/lban add <玩家名> <天数> <原因>");
                    return true;
                }
                // 添加封禁
                LengbanList.getInstance().banManager.banPlayer(new org.leng.object.BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.valueOf(args[2])), args[3]), Integer.valueOf(args[2]));
                sender.sendMessage("§a成功封禁" + args[1]);
                break;
            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(plugin.prefix() + "§c§l错误的命令格式，正确格式/lban remove <玩家名>");
                    return true;
                }
                // 移除封禁
                LengbanList.getInstance().banManager.unbanPlayer(args[1]);
                sender.sendMessage("§a成功解封" + args[1]);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                sender.sendMessage("未知的子命令。");
                break;
        }
        return true;
    }
}
