package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.utils.Utils;

public class 胡桃_Hu_Tao implements Model {
    @Override
    public String getName() {
        return "胡桃_Hu_Tao";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanList §2§o帮助信息 - 胡桃风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o往生堂雇主");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o立即广播当前往生人数");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭 自动广播)");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型)");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重启胡桃的大脑");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加往生堂雇主");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除往生堂雇主");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示胡桃的帮助");
        Utils.sendMessage(sender, "§6当前版本: 1.3.3 Model: 胡桃 Hu Tao");
    }
}