package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.ClickEvent; 
import net.md_5.bungee.api.chat.TextComponent; 

public class BroadCastBanCountMessage extends BukkitRunnable {
    @Override
    public void run() {
        if (!Lengbanlist.getInstance().getServer().getOnlinePlayers().isEmpty()) {
            for (Player player : Lengbanlist.getInstance().getServer().getOnlinePlayers()) {
                String defaultMessage = Lengbanlist.getInstance().getConfig().getString("default-message");
                String replacedMessage = defaultMessage.replace("%s", String.valueOf(Lengbanlist.getInstance().banManager.getBanList().size()));
                String clickableText = ChatColor.YELLOW + "【点击查看】";
                String finalMessage = replacedMessage + " " + clickableText;

                // 创建 TextComponent 对象并设置点击事件
                TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', finalMessage));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lban list"));

                // 使用 PlayerSpigot 发送消息
                player.spigot().sendMessage(message);
            }
        }
    }
}