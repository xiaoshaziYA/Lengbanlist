package org.leng;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastBanCountMessage extends BukkitRunnable {
    @Override
    public void run() {
        if (!LengbanList.getInstance().getServer().getOnlinePlayers().isEmpty()) {
            String message = LengbanList.getInstance().getConfig().getString("default-message");
            int banCount = LengbanList.getInstance().banManager.getBanList().size();
            message = message.replace("%s", String.valueOf(banCount));
            message = ChatColor.translateAlternateColorCodes('&', message); // 解析颜色代码

            for (Player player : LengbanList.getInstance().getServer().getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }
}
