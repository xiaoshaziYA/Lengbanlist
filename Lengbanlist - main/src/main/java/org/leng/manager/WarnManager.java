package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.object.WarnEntry;
import org.leng.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class WarnManager {
    private final Map<String, WarnEntry> warnList = new HashMap<>();

public int warnPlayer(String player, String reason) {
    WarnEntry warnEntry = warnList.getOrDefault(player, new WarnEntry(player, 0));
    warnEntry.setWarnCount(warnEntry.getWarnCount() + 1);
    warnEntry.setReason(reason);
    warnList.put(player, warnEntry);

    if (warnEntry.getWarnCount() >= 3) {
        Lengbanlist.getInstance().getBanManager().banPlayer(
                new org.leng.object.BanEntry(player, "System", Long.MAX_VALUE, "警告次数过多")
        );
        Utils.sendMessage(null, "§c玩家 " + player + " 因警告次数过多被自动封禁！");
    }

    return warnEntry.getWarnCount(); // 返回警告次数
}

    public void unwarnPlayer(String player) {
        warnList.remove(player);
    }

    public boolean isPlayerWarned(String player) {
        return warnList.containsKey(player);
    }

    public int getWarnCount(String player) {
        return warnList.getOrDefault(player, new WarnEntry(player, 0)).getWarnCount();
    }
}