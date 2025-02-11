package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.utils.Utils;

public class Keqing implements Model {
    @Override
    public String getName() {
        return "Keqing";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 刻晴风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o查看被封禁的名单，刻晴办事，效率第一！");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前封禁人数，让大家都看看这些不守规矩的人！");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，想听就开，不想听就关！");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，试试不同的风格吧！");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重新加载配置，刷新一下，说不定有惊喜哦！");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁，不守规矩就封了！");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除封禁，知错能改，善莫大焉！");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示帮助信息，不懂就问，别装懂！");
        Utils.sendMessage(sender, "§6当前版本: 1.3.5 Model: 刻晴 Keqing");
    }

    public String toggleBroadcast(boolean enabled) {
        return "§b刻晴说：§a自动广播已经 " + (enabled ? "开启！" : "关闭！") + " 让大家都知道规则的重要性！";
    }

    public String reloadConfig() {
        return "§b刻晴说：§a配置重新加载完成！效率第一，刻晴办事，绝不拖沓！";
    }

    public String addBan(String player, int days, String reason) {
        return "§b刻晴说：§a" + player + " 已被封禁 " + days + " 天，原因是：" + reason + "！不守规矩，就别怪刻晴无情！";
    }

    public String removeBan(String player) {
        return "§b刻晴说：§a" + player + " 已从封禁名单中移除。知错能改，善莫大焉！";
    }
}