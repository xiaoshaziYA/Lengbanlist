package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Herta implements Model {
    @Override
    public String getName() {
        return "Herta";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 希儿风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o查看被封禁的名单，希儿的正义不容挑战！");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前封禁人数，让违规者无处可逃！");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，掌控一切！");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，体验不同的风格。");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重新加载配置，确保一切正常运行。");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁，维护正义。");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除封禁，给予机会。");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示帮助信息，不懂就问。");
        Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，希儿的正义领域。");
        Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，找出违规者。");
        Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 希儿 Herta");
    }

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b希儿说：§a自动广播已经 " + (enabled ? "开启！" : "关闭！") + " 正义需要维护！";
    }

    @Override
    public String reloadConfig() {
        return "§b希儿说：§a配置重新加载完成！一切正常运行。";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b希儿说：§a玩家 " + player + " 已被封禁 " + days + " 天，原因是：" + reason + "。正义不容挑战！";
    }

    @Override
    public String removeBan(String player) {
        return "§b希儿说：§a玩家 " + player + " 已从封禁名单中移除。给予机会，重新开始。";
    }
}