package org.leng.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.leng.LengbanList;
import org.leng.object.BanEntry;
import org.leng.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class BanManager {

    public void banPlayer(BanEntry banEntry, int day) {
        Long time = TimeUtils.generateTimestampFromDays(day);
        String ban = banEntry.getTarget() + ":" + banEntry.getStaff() + ":" + time + ":" + banEntry.getReason();
        List<String> banList = LengbanList.getInstance().getConfig().getStringList("ban-list");
        banList.add(ban);
        LengbanList.getInstance().getConfig().set("ban-list", banList);
        LengbanList.getInstance().saveConfig();

        // 尝试获取玩家对象，如果玩家在线则立即踢出
        Player targetPlayer = Bukkit.getPlayerExact(banEntry.getTarget());
        if (targetPlayer != null) {
            targetPlayer.kickPlayer("您已被 " + banEntry.getStaff() + " 封禁，原因：" + banEntry.getReason() + " " + TimeUtils.timestampToReadable(time));
        }

        // 广播封禁信息
        Bukkit.broadcastMessage(banEntry.getTarget() + " 已被 " + banEntry.getStaff() + " 封禁，原因：" + banEntry.getReason() + " " + TimeUtils.timestampToReadable(time));
    }

    public void unbanPlayer(String target) {
        List<String> banList = LengbanList.getInstance().getConfig().getStringList("ban-list");
        banList.removeIf(entry -> entry.split(":")[0].equals(target));
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
}
