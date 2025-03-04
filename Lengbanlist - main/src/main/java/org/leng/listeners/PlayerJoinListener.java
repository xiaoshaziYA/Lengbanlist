package org.leng.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.leng.Lengbanlist;
import org.leng.manager.BanManager;
import org.leng.utils.SaveIP;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SaveIP.saveIP(player);
        if (Lengbanlist.getInstance().getBanManager().isPlayerBanned(player.getName())) {
            Lengbanlist.getInstance().getBanManager().checkBanOnJoin(player);
        }
    }
}