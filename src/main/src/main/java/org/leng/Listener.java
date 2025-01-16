package org.leng;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.leng.object.BanEntry;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(LengbanList.getInstance().banManager.isPlayerBanned(player.getName())){
            BanEntry banEntry = LengbanList.getInstance().banManager.getBanEntry(player.getName());
            if (banEntry != null) {
                String reason = banEntry.getReason();
                String message = "您已被封禁。\n" +
                        "封禁原因: " + reason + "\n" +
                        "请联系管理员了解更多信息。";
                player.kickPlayer(message);
            }
        }
    }
}
