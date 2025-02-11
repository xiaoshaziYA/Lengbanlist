package org.leng;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastBanCountMessage extends BukkitRunnable {
    @Override
    public void run() {
        if (!Lengbanlist.getInstance().getServer().getOnlinePlayers().isEmpty()){
            for (Player player : Lengbanlist.getInstance().getServer().getOnlinePlayers()){
                player.sendMessage(Lengbanlist.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(Lengbanlist.getInstance().banManager.getBanList().size())));
            }
        }
    }
}
