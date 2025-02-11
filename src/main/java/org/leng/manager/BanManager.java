package org.leng.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.models.Model;
import org.leng.object.BanEntry;
import org.leng.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class BanManager {

    public void banPlayer(BanEntry banEntry) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String banResult = currentModel.addBan(banEntry.getTarget(), (int) ((banEntry.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)), banEntry.getReason());
        
        if (banResult != null && !banResult.isEmpty()) {
            String ban = banEntry.toString();
            List<String> banList = Lengbanlist.getInstance().getConfig().getStringList("ban-list");
            banList.add(ban);
            Lengbanlist.getInstance().getConfig().set("ban-list", banList);
            Lengbanlist.getInstance().saveConfig();
            Player targetPlayer = Bukkit.getPlayer(banEntry.getTarget());
            if (targetPlayer != null) {
                targetPlayer.kickPlayer(banResult); // 使用模型返回的封禁提示
            }
            Bukkit.broadcastMessage(banResult); // 广播封禁信息
        } else {
            // 修改失败警告信息，包含当前模型名称
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 封禁玩家 [" + banEntry.getTarget() + "] 失败！");
        }
    }

    public void unbanPlayer(String target) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String unbanResult = currentModel.removeBan(target);
        
        if (unbanResult != null && !unbanResult.isEmpty()) {
            List<String> banList = Lengbanlist.getInstance().getConfig().getStringList("ban-list");
            for (int i = 0; i < banList.size(); i++) {
                String entry = banList.get(i);
                String[] parts = entry.split(":");
                if (parts[0].equals(target)) {
                    banList.remove(i);
                    break;
                }
            }
            Lengbanlist.getInstance().getConfig().set("ban-list", banList);
            Lengbanlist.getInstance().saveConfig();
            Bukkit.broadcastMessage(unbanResult); // 广播解封信息
        } else {
            // 修改失败警告信息，包含当前模型名称
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 解封玩家 [" + target + "] 失败！");
        }
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
        List<String> banList = Lengbanlist.getInstance().getConfig().getStringList("ban-list");
        for (String entry : banList) {
            String[] parts = entry.split(":");
            if (parts[0].equals(target)) {
                return true;
            }
        }
        return false;
    }

    public List<BanEntry> getBanList() {
        List<String> banListStrings = Lengbanlist.getInstance().getConfig().getStringList("ban-list");
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
                player.kickPlayer("您仍处于封禁状态，原因：" + ban.getReason() + "，封禁到：" + TimeUtils.timestampToReadable(ban.getTime()));
            }
        }
    }
}