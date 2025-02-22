package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Default implements Model {
    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 默认风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o查看被封禁的名单");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前封禁人数");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重新加载配置");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除封禁");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示帮助信息");
        Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面");
        Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址");
        Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 默认 Default");
    }

    public String toggleBroadcast(boolean enabled) {
        return "§b默认模型：§a自动广播已经 " + (enabled ? "开启" : "关闭");
    }

    public String reloadConfig() {
        return "§b默认模型：§a配置重新加载完成";
    }

    public String addBan(String player, int days, String reason) {
        return "§b默认模型：§a玩家 " + player + " 已被封禁 " + days + " 天，原因是：" + reason;
    }

    public String removeBan(String player) {
        return "§b默认模型：§a玩家 " + player + " 已从封禁名单中移除";
    }
}