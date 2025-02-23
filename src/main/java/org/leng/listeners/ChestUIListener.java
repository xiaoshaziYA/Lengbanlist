package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.leng.Lengbanlist;
import org.leng.manager.ModelManager;
import org.leng.utils.Utils;
import org.bukkit.inventory.Inventory;

public class ChestUIListener implements Listener {
    private final Lengbanlist plugin;

    public ChestUIListener(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§bLengbanlist")) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || meta.getLore() == null || meta.getLore().isEmpty()) {
            return;
        }

        String command = meta.getLore().get(0).replace("§7", ""); // 去掉颜色字符
        Player player = (Player) event.getWhoClicked();

        switch (command) {
            case "/lban add":
                openAnvilForBan(player, "playerID"); // 第一步：输入玩家ID
                break;
            case "/lban remove":
                openAnvilForUnban(player);
                break;
            case "/lban mute":
                openAnvilForMute(player, "playerID"); // 第一步：输入玩家ID
                break;
            case "/lban unmute":
                openAnvilForUnmute(player);
                break;
            case "/lban model":
                // 打开模型选择页面
                ModelManager.getInstance().openModelSelectionUI(player);
                break;
            default:
                plugin.getServer().dispatchCommand(player, command);
                break;
        }
    }

    private void openAnvilForBan(Player player, String step) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b封禁玩家 - 输入" + (step.equals("playerID") ? "玩家ID" : (step.equals("time") ? "时间" : "原因")));
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入内容"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-action", new FixedMetadataValue(plugin, "ban"));
        player.setMetadata("lengbanlist-step", new FixedMetadataValue(plugin, step));
    }

    private void openAnvilForMute(Player player, String step) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b禁言玩家 - 输入" + (step.equals("playerID") ? "玩家ID" : "原因"));
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入内容"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-action", new FixedMetadataValue(plugin, "mute"));
        player.setMetadata("lengbanlist-step", new FixedMetadataValue(plugin, step));
    }

    private void openAnvilForUnban(Player player) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b解封玩家");
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入玩家ID"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-action", new FixedMetadataValue(plugin, "unban"));
    }

    private void openAnvilForUnmute(Player player) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b解除禁言");
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入玩家ID"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-action", new FixedMetadataValue(plugin, "unmute"));
    }
}