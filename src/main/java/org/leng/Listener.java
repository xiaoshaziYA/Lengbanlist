package org.leng;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(Lengbanlist.getInstance().banManager.isPlayerBanned(player.getName())){
            Lengbanlist.getInstance().banManager.checkBanOnJoin(player);
        }
    }
}
