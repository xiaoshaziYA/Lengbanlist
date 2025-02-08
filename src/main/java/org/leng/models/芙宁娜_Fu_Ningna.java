package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.utils.Utils;

public class 芙宁娜_Fu_Ningna implements Model {
    @Override
    public String getName() {
        return "芙宁娜_Fu_Ningna";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§bLengbanList §2§o帮助信息 - 芙宁娜风格:");
        Utils.sendMessage(sender, "§b§l/lban list - §3§o显示水酱列表");
        Utils.sendMessage(sender, "§b§l/lban a - §3§o立即广播当前划水人数");
        Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭 自动广播)");
        Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型)");
        Utils.sendMessage(sender, "§b§l/lban reload - §3§o重启水神的大脑");
        Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加划水");
        Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除划水");
        Utils.sendMessage(sender, "§b§l/lban help - §3§o显示芙芙的帮助");
        Utils.sendMessage(sender, "§6当前版本: 1.3.3 Model: 芙宁娜 Fu Ningna");
    }
}