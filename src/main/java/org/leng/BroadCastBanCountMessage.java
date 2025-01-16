package org.leng;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastBanCountMessage extends BukkitRunnable {
    @Override
    public void run() {
        if (!LengbanList.getInstance().getServer().getOnlinePlayers().isEmpty()){
            for (Player player : LengbanList.getInstance().getServer().getOnlinePlayers()){
                player.sendMessage(LengbanList.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(LengbanList.getInstance().banManager.getBanList().size())));
            }
        }
    }
}
