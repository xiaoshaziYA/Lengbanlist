package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class HuTao implements Model {
    @Override
    public String getName() {
        return "HuTao";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 胡桃风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o查看往生堂的黑名单！");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前往生人数，看看谁又倒霉了！");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，想听就开，不想听就关！");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，试试别的风格吧！");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重启胡桃的大脑，说不定能发现新东西！");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o把不听话的人加入往生堂！");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o从往生堂名单里移除一个人，知错能改，善莫大焉！");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示胡桃的帮助，不懂就问！");
        Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，看看胡桃的魔法！");
        Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，看看谁在捣乱！");
        Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 胡桃 Hu Tao");
    }

    public String toggleBroadcast(boolean enabled) {
        return "§b胡桃说：§a自动广播已经 " + (enabled ? "开启啦！" : "关闭啦！") + " 快来听听谁又倒霉啦！";
    }

    public String reloadConfig() {
        return "§b胡桃说：§a配置重新加载完成！胡桃的大脑又清晰啦！";
    }

    public String addBan(String player, int days, String reason) {
        return "§b胡桃说：§a" + player + " 已被加入往生堂黑名单！封禁 " + days + " 天，原因是：" + reason;
    }

    public String removeBan(String player) {
        return "§b胡桃说：§a" + player + " 已从往生堂黑名单中移除啦！知错能改，善莫大焉！";
    }
}