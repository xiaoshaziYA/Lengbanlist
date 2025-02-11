package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.utils.Utils;

public class Zhongli implements Model {
    @Override
    public String getName() {
        return "Zhongli";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 钟离风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o查看被封禁的名单，一切尽在掌控之中。");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前封禁人数，维护秩序。");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，一切尽在掌控之中。");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，体验不同的风格。");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重新加载配置，确保一切完美无缺。");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁，维护秩序。");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除封禁，宽恕是美德。");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示帮助信息，优雅地解决问题。");
        Utils.sendMessage(sender, "§6当前版本: 1.3.5 Model: 钟离 Zhongli");
    }

    public String toggleBroadcast(boolean enabled) {
        return "§b钟离说：§a自动广播已经 " + (enabled ? "开启。" : "关闭。") + " 秩序需要维护。";
    }

    public String reloadConfig() {
        return "§b钟离说：§a配置重新加载完成。一切尽在掌控之中。";
    }

    public String addBan(String player, int days, String reason) {
        return "§b钟离说：§a" + player + " 已被封禁 " + days + " 天，原因是：" + reason + "。秩序不容破坏。";
    }

    public String removeBan(String player) {
        return "§b钟离说：§a" + player + " 已从封禁名单中移除。宽恕是美德。";
    }
}