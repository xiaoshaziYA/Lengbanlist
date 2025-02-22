package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Furina implements Model {
    @Override
    public String getName() {
        return "Furina";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 芙宁娜风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o查看水酱列表，看看谁在划水！");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前划水人数，让大家都知道！");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，想听就开，不想听就关！");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，试试别的风格吧！");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重启水神的大脑，说不定能发现新东西！");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o把划水的人加入黑名单！");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o从黑名单里移除一个人，知错能改，善莫大焉！");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示芙宁娜的帮助，不懂就问！");
        Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，芙宁娜带你看看水的力量！");
        Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，看看谁在偷偷摸鱼！");
        Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 芙宁娜 Furina");
    }

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b芙宁娜说：§a自动广播已经 " + (enabled ? "开启啦！" : "关闭啦！") + " 水酱们要注意啦！";
    }

    @Override
    public String reloadConfig() {
        return "§b芙宁娜说：§a配置重新加载完成！水神的大脑又清晰啦！";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b芙宁娜说：§a" + player + " 已被加入黑名单！封禁 " + days + " 天，原因是：" + reason + "。划水可不是好习惯哦！";
    }

    @Override
    public String removeBan(String player) {
        return "§b芙宁娜说：§a" + player + " 已从黑名单中移除啦！知错能改，善莫大焉！";
    }
}