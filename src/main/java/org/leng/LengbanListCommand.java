package org.leng;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.leng.manager.BanManager;
import org.leng.object.BanEntry;
import org.leng.utils.TimeUtils;

public class LengbanListCommand extends Command implements CommandExecutor {

    private final LengbanList plugin;

    public LengbanListCommand(String name, LengbanList plugin) {
        super(name);
        this.plugin = plugin;
    }

    // 显示封禁列表的方法
    private void showBanList(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "-----------------------------------LengbanList-------------------------------------");
        for (BanEntry entry : plugin.getBanManager().getBanList()) {
            sender.sendMessage(ChatColor.YELLOW + "Target: " + entry.getTarget() + 
                               ChatColor.GREEN + " Staff: " + entry.getStaff() + 
                               ChatColor.RED + " 封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + 
                               ChatColor.BLUE + " 封禁原因: " + entry.getReason());
        }
    }

    // 显示帮助信息的方法
    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "LengbanList 帮助信息:");
        sender.sendMessage(ChatColor.YELLOW + "/lban list - " + ChatColor.AQUA + "显示封禁列表");
        sender.sendMessage(ChatColor.YELLOW + "/lban broadcast - " + ChatColor.AQUA + "立即广播当前封禁人数");
        sender.sendMessage(ChatColor.YELLOW + "/lban reload - " + ChatColor.AQUA + "重载插件配置");
        sender.sendMessage(ChatColor.YELLOW + "/lban add <玩家名> <天数> <封禁原因> - " + ChatColor.AQUA + "添加封禁");
        sender.sendMessage(ChatColor.YELLOW + "/lban remove <玩家名> - " + ChatColor.AQUA + "移除封禁");
        sender.sendMessage(ChatColor.YELLOW + "/ban <玩家名> <封禁原因> - " + ChatColor.AQUA + "封禁玩家");
        sender.sendMessage(ChatColor.RED + "作者: Leng");
        sender.sendMessage(ChatColor.BLUE + "版本: 1.0");
        sender.sendMessage(ChatColor.DARK_PURPLE + "授权: ColorFulCraft Network");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender.isOp() || sender.hasPermission("lengbanlist.admin"))) {
            sender.sendMessage(ChatColor.RED + "[LengBanList] 错误：你没有权限执行此命令！");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "[LengBanList] " + ChatColor.RED + "错误的指令格式！使用方法: /lban [子命令]");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "broadcast":
                // 广播当前封禁人数
                String message = LengbanList.getInstance().getConfig().getString("default-message")
                        .replace("%s", String.valueOf(plugin.getBanManager().getBanList().size()));
                LengbanList.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                break;
            case "list":
                showBanList(sender);
                break;
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(plugin.prefix() + ChatColor.GREEN + "配置已重新加载。");
                break;
            case "add":
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "错误的指令格式！使用方法: /lban add <玩家名> <天数> <封禁原因>");
                    return true;
                }
                try {
                    int days = Integer.parseInt(args[2]);
                    String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length));
                    plugin.getBanManager().banPlayer(new BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(days), reason), days);
                    sender.sendMessage(ChatColor.GREEN + "成功封禁玩家 " + args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "天数必须是一个整数！");
                }
                break;
            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "错误的指令格式！使用方法: /lban remove <玩家名>");
                    return true;
                }
                plugin.getBanManager().unbanPlayer(args[1]);
                sender.sendMessage(ChatColor.GREEN + "成功移除玩家 " + args[1] + " 的封禁");
                break;
            case "ban":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "错误的指令格式！使用方法: /ban <玩家名> <封禁原因>");
                    return true;
                }
                String target = args[1];
                String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                int days = 365; // 默认封禁365天，即一年
                BanEntry banEntry = new BanEntry(target, sender.getName(), TimeUtils.generateTimestampFromDays(days), reason);
                plugin.getBanManager().banPlayer(banEntry, days);
                sender.sendMessage(ChatColor.GREEN + "成功封禁玩家 " + target + "，原因: " + reason);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "未知的子命令。");
                break;
        }
        return true;
    }
}
