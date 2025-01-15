package org.leng;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LengbanListCommandExecutor implements CommandExecutor {

    private final LengbanList plugin;

    public LengbanListCommandExecutor(LengbanList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("使用方法: /lban [子命令]");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "a":
                // 广播当前封禁人数
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
                break;
            case "remove":
                // 移除封禁
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

    // 显示封禁列表的方法
    private void showBanList(CommandSender sender) {
        BanList nameBanList = sender.getServer().getBanList(Type.NAME);
        sender.sendMessage("-----------------------------------LengbanList-------------------------------------");
        for (BanEntry entry : nameBanList.getEntries()) {
            String id = entry.getTargetName(); // 使用 getTargetName() 替代 getTarget()
            Date createdDate = entry.getCreated(); // 确保 getCreated() 方法存在
            String reason = entry.getReason(); // 确保 getReason() 方法存在
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = createdDate != null ? dateFormat.format(createdDate) : "未知";
            sender.sendMessage("ID:" + id + " 封禁时间：" + formattedDate + " 封禁原因：" + reason);
        }
    }

    // 显示帮助信息的方法
    private void showHelp(CommandSender sender) {
        sender.sendMessage("LengbanList 帮助信息:");
        sender.sendMessage("/lban list - 显示封禁列表");
        sender.sendMessage("/lban reload - 重载插件配置");
        sender.sendMessage("/lban add <玩家名> - 添加封禁");
        sender.sendMessage("/lban remove <玩家名> - 移除封禁");
        sender.sendMessage("作者: Leng");
        sender.sendMessage("版本: 1.0");
        sender.sendMessage("授权: ColorFulCraft Network");
    }
}