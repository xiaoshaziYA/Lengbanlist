package org.leng.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.leng.LengbanList;
import org.leng.object.BanEntry;
import org.leng.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class BanManager {

    public void banPlayer(BanEntry banEntry) {
        String ban = banEntry.toString();
        List<String> banList = LengbanList.getInstance().getConfig().getStringList("ban-list");
        banList.add(ban);
        LengbanList.getInstance().getConfig().set("ban-list", banList);
        LengbanList.getInstance().saveConfig();
        Player targetPlayer = Bukkit.getPlayer(banEntry.getTarget());
        if (targetPlayer != null) {
            targetPlayer.kickPlayer("§c§l您已被 " + banEntry.getStaff() + " §c§l封禁，§c§l原因："+banEntry.getReason()+" " + TimeUtils.timestampToReadable(banEntry.getTime()));
        }
        Bukkit.broadcastMessage(banEntry.getTarget() + " §c§l已被 " + banEntry.getStaff() + " §c§l封禁，§c§l原因：" + banEntry.getReason() + " " +  TimeUtils.timestampToReadable(banEntry.getTime()));
    }

    public void unbanPlayer(String target) {
        List<String> banList = LengbanList.getInstance().getConfig().getStringList("ban-list");
        for (int i = 0; i < banList.size(); i++) {
            String entry = banList.get(i);
            String[] parts = entry.split(":");
            if (parts[0].equals(target)) {
                banList.remove(i);
                break;
            }
        }
        LengbanList.getInstance().getConfig().set("ban-list", banList);
        LengbanList.getInstance().saveConfig();
    }

    public BanEntry getBanEntry(String target) {
        for (BanEntry ban : getBanList()) {
            if (ban.getTarget().equals(target)) {
                return ban;
            }
        }
        return null;
    }

    public boolean isPlayerBanned(String target) {
        List<String> banList = LengbanList.getInstance().getConfig().getStringList("ban-list");
        for (String entry : banList) {
            String[] parts = entry.split(":");
            if (parts[0].equals(target)) {
                return true;
            }
        }
        return false;
    }

    public List<BanEntry> getBanList() {
        List<String> banListStrings = LengbanList.getInstance().getConfig().getStringList("ban-list");
        List<BanEntry> banList = new ArrayList<>();
        for (String entry : banListStrings) {
            String[] parts = entry.split(":");
            if (parts.length == 4) {
                String target = parts[0];
                String staff = parts[1];
                long time = Long.parseLong(parts[2]);
                String reason = parts[3];
                banList.add(new BanEntry(target, staff, time, reason));
            }
        }
        return banList;
    }

    public void checkBanOnJoin(Player player) {
        BanEntry ban = getBanEntry(player.getName());
        if (ban != null) {
            long currentTime = System.currentTimeMillis();
            if (ban.getTime() <= currentTime) {
                unbanPlayer(player.getName());
            } else {
                player.kickPlayer("您仍处于封禁状态，原因：" + ban.getReason() + "，封禁时间：" + TimeUtils.timestampToReadable(ban.getTime()));
            }
        }
    }
}
