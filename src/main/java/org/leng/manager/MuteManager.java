package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.object.MuteEntry;

import java.util.ArrayList;
import java.util.List;

public class MuteManager {
    private List<MuteEntry> muteList;

    public MuteManager() {
        muteList = new ArrayList<>();
    }

    public void mutePlayer(String target, String staff, String reason) {
        MuteEntry muteEntry = new MuteEntry(target, staff, reason);
        muteList.add(muteEntry);
        Lengbanlist.getInstance().getLogger().info("玩家 " + target + " 被禁言，原因：" + reason);
    }

    public void unmutePlayer(String target) {
        muteList.removeIf(entry -> entry.getTarget().equalsIgnoreCase(target));
        Lengbanlist.getInstance().getLogger().info("玩家 " + target + " 的禁言已被解除");
    }

    public boolean isMuted(String target) {
        return muteList.stream().anyMatch(entry -> entry.getTarget().equalsIgnoreCase(target));
    }

    public List<MuteEntry> getMuteList() {
        return muteList;
    }
}