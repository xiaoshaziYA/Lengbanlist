package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.leng.Lengbanlist;
import org.leng.manager.ModelManager;
import org.leng.models.Model;
import org.leng.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelChoiceListener implements Listener {

    private final Lengbanlist plugin;

    public ModelChoiceListener(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    /**
     * 打开模型选择页面
     */
    public void openModelSelectionUI(Player player) {
        Inventory modelSelection = Bukkit.createInventory(null, 27, "§b选择模型"); // 模型选择页面

        // 设置玻璃背景
        ItemStack glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < 27; i++) {
            modelSelection.setItem(i, glass);
        }

        // 动态获取所有模型
        Map<String, Model> models = ModelManager.getInstance().getModels();
        int slot = 10; // 从第 10 格开始放置模型按钮
        for (Map.Entry<String, Model> entry : models.entrySet()) {
            String modelName = entry.getKey();
            ItemStack modelItem = createItem(
                    "§a" + modelName,
                    "§7点击选择此模型",
                    "§7当前模型: " + ModelManager.getInstance().getCurrentModelName(),
                    Sound.BLOCK_NOTE_BLOCK_PLING
            );

            // 如果是当前模型，给物品附魔
            if (modelName.equals(ModelManager.getInstance().getCurrentModelName())) {
                ItemMeta meta = modelItem.getItemMeta();
                meta.addEnchant(Enchantment.PROTECTION, 1, true); // 使用 PROTECTION 附魔
                modelItem.setItemMeta(meta);
            }

            modelSelection.setItem(slot, modelItem);
            slot++;
        }

        // 打开模型选择页面
        player.openInventory(modelSelection);
    }

    /**
     * 处理模型选择页面的点击事件
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§b选择模型")) {
            return; // 如果不是模型选择页面，直接返回
        }

        event.setCancelled(true); // 取消点击事件，防止物品被移动

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) {
            return; // 如果点击的是空气或无效物品，直接返回
        }

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return; // 如果物品没有显示名称，直接返回
        }

        String modelName = meta.getDisplayName().replace("§a", ""); // 去掉颜色代码，获取模型名称

        // 切换模型
        ModelManager.getInstance().switchModel(modelName);
        Utils.sendMessage(player, "§a已切换到模型: " + modelName);

        // 给选择的模型对应的物品附魔
        meta.addEnchant(Enchantment.PROTECTION, 1, true); // 使用 PROTECTION 附魔
        clickedItem.setItemMeta(meta);

        // 播放音效
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);

        // 关闭模型选择页面
        player.closeInventory();
    }

    /**
     * 创建模型选择页面的按钮
     */
    private ItemStack createItem(String displayName, String command, String description, Sound sound) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> lore = new ArrayList<>();
        lore.add(command);
        lore.add(description);
        meta.setLore(lore);
        item.setItemMeta(meta);

        // 播放音效
        if (sound != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Player player = Bukkit.getPlayer(meta.getDisplayName());
                if (player != null) {
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                }
            }, 1L);
        }

        return item;
    }
}