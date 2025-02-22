package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.leng.Lengbanlist;
import org.leng.manager.BanManager;
import org.leng.object.BanEntry;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class AnvilGUIListener implements Listener {

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        if (!(event.getView().getTitle().contains("封禁玩家") || event.getView().getTitle().contains("解封玩家"))) {
            return;
        }

        ItemStack item = event.getInventory().getItem(0);
        if (item == null || item.getType().isAir()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§a输入内容"); // 设置默认显示名称
            item.setItemMeta(meta);
        }
        event.getInventory().setItem(0, item);
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().contains("封禁玩家") || event.getView().getTitle().contains("解封玩家"))) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        MetadataValue action = player.getMetadata("lengbanlist-action").get(0);
        MetadataValue step = player.getMetadata("lengbanlist-step").get(0);
        String actionType = action.asString();
        String currentStep = step.asString();

        if (actionType.equals("ban")) {
            handleBan(player, event, currentStep);
        } else if (actionType.equals("unban")) {
            handleUnban(player, event);
        }
    }

    private void handleBan(Player player, InventoryClickEvent event, String currentStep) {
        ItemStack item = event.getInventory().getItem(0);
        if (item == null || item.getItemMeta() == null) {
            return;
        }

        String input = item.getItemMeta().getDisplayName();

        if (currentStep.equals("playerID")) {
            player.setMetadata("lengbanlist-playerID", new FixedMetadataValue(Lengbanlist.getInstance(), input));
            openAnvilForBan(player, "time");
        } else if (currentStep.equals("time")) {
            if (!TimeUtils.isValidTime(input)) {
                Utils.sendMessage(player, "§c时间格式无效，请使用以下格式：10s, 5m, 2h, 7d, 1w, 1M, 1y");
                return;
            }

            player.setMetadata("lengbanlist-time", new FixedMetadataValue(Lengbanlist.getInstance(), input));
            openAnvilForBan(player, "reason");
        } else if (currentStep.equals("reason")) {
            String playerID = player.getMetadata("lengbanlist-playerID").get(0).asString();
            String time = player.getMetadata("lengbanlist-time").get(0).asString();
            long banTimestamp = TimeUtils.parseTime(time);

            Lengbanlist.getInstance().getBanManager().banPlayer(new BanEntry(playerID, player.getName(), banTimestamp, input));
            Utils.sendMessage(player, "§a成功封禁玩家：" + playerID + "，时长：" + time + "，原因：" + input);
            player.removeMetadata("lengbanlist-action", Lengbanlist.getInstance());
            player.removeMetadata("lengbanlist-step", Lengbanlist.getInstance());
            player.removeMetadata("lengbanlist-playerID", Lengbanlist.getInstance());
            player.removeMetadata("lengbanlist-time", Lengbanlist.getInstance());
        }
    }

    private void handleUnban(Player player, InventoryClickEvent event) {
        ItemStack item = event.getInventory().getItem(0);
        if (item == null || item.getItemMeta() == null) {
            return;
        }

        String playerID = item.getItemMeta().getDisplayName();

        Lengbanlist.getInstance().getBanManager().unbanPlayer(playerID);
        Utils.sendMessage(player, "§a成功解封玩家：" + playerID);
        player.removeMetadata("lengbanlist-action", Lengbanlist.getInstance());
    }

    private void openAnvilForBan(Player player, String step) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b封禁玩家 - 输入" + (step.equals("playerID") ? "玩家ID" : (step.equals("time") ? "时间" : "原因")));
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入内容"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-step", new FixedMetadataValue(Lengbanlist.getInstance(), step));
    }
}