package org.leng;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        sender.sendMessage("-----------------------------------LengbanList-------------------------------------");
        for (BanEntry entry : LengbanList.getInstance().banManager.getBanList()) {
            sender.sendMessage("Target: " + entry.getTarget() +" Staff: "+entry.getStaff() + " 封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " 封禁原因: " + entry.getReason());
        }
    }

    // 显示帮助信息的方法
    private void showHelp(CommandSender sender) {
        sender.sendMessage("LengbanList 帮助信息:");
        sender.sendMessage("/lban list - 显示封禁列表");
        sender.sendMessage("/lban broadcast - 立即广播当前封禁人数");
        sender.sendMessage("/lban reload - 重载插件配置");
        sender.sendMessage("/lban add <玩家名> - 添加封禁");
        sender.sendMessage("/lban remove <玩家名> - 移除封禁");
        sender.sendMessage("作者: Leng");
        sender.sendMessage("版本: 1.0");
        sender.sendMessage("授权: ColorFulCraft Network");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender.isOp() || sender.hasPermission("lengbanlist.admin"))) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage("[LengBanList] 错误的指令格式！使用方法: /lban [子命令]");
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
                sender.sendMessage(plugin.prefix() + "配置已重新加载。");
                break;
            case "add":
                // 添加封禁
                LengbanList.getInstance().banManager.banPlayer(new org.leng.object.BanEntry(args[1],sender.getName(), TimeUtils.generateTimestampFromDays(Integer.valueOf(args[2])),args[3]),Integer.valueOf(args[2]));
                sender.sendMessage("成功");
                break;
            case "remove":
                LengbanList.getInstance().banManager.unbanPlayer(args[1]);
                sender.sendMessage("成功");
                // 移除封禁
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                sender.sendMessage("未知的子命令。");
                break;
        }
        return false;
    }
}
