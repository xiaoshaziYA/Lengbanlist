package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.leng.Lengbanlist;
import org.leng.manager.ModelManager;
import org.leng.utils.Utils;

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

        if (command.equals("/lban add")) {
            openAnvilForBan(player, "playerID"); // 第一步：输入玩家ID
        } else if (command.equals("/lban remove")) {
            openAnvilForUnban(player);
        } else if (command.equals("/lban getip")) { // 新增对 getip 的处理
            player.sendMessage("§c请在聊天中输入 /lban getip <玩家名>");
        } else if (command.equals("/lban model")) {
            // 处理 "切换模型" 按钮点击事件
            if (player.hasPermission("lengbanlist.model")) {
                // 打开模型选择页面
                plugin.getModelChoiceListener().openModelSelectionUI(player);
            } else {
                player.sendMessage(Lengbanlist.getInstance().prefix() + "§c你没有权限使用此功能。");
            }
        } else {
            Lengbanlist.getInstance().getServer().dispatchCommand(player, command);
        }
    }

    private void openAnvilForBan(Player player, String step) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b封禁玩家 - 输入玩家ID");
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入玩家ID"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-action", new FixedMetadataValue(Lengbanlist.getInstance(), "ban"));
        player.setMetadata("lengbanlist-step", new FixedMetadataValue(Lengbanlist.getInstance(), step));
    }

    private void openAnvilForUnban(Player player) {
        Inventory anvil = Bukkit.createInventory(player, 9, "§b解封玩家");
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER); // 使用纸作为输入物品
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a输入玩家ID"); // 设置默认显示名称
        item.setItemMeta(meta);
        anvil.setItem(0, item);

        player.openInventory(anvil);
        player.setMetadata("lengbanlist-action", new FixedMetadataValue(Lengbanlist.getInstance(), "unban"));
    }
}