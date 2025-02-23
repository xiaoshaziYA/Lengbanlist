package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    public void openModelSelectionUI(Player player) {
        Inventory modelSelection = Bukkit.createInventory(null, 27, "§b选择模型");

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
        int slot = 10;
        for (Map.Entry<String, Model> entry : models.entrySet()) {
            String modelName = entry.getKey();

            // 获取模型对应的材料
            Material modelMaterial = ModelManager.getModelMaterial(modelName);

            ItemStack modelItem = new ItemStack(modelMaterial);
            ItemMeta meta = modelItem.getItemMeta();
            meta.setDisplayName("§a" + modelName);
            List<String> lore = new ArrayList<>();
            lore.add("§7点击选择此模型");
            lore.add("§7当前模型: " + ModelManager.getInstance().getCurrentModelName());
            meta.setLore(lore);
            modelItem.setItemMeta(meta);

            // 如果是当前模型，给物品附魔
            if (modelName.equals(ModelManager.getInstance().getCurrentModelName())) {
                meta.addEnchant(Enchantment.PROTECTION, 1, true);
                modelItem.setItemMeta(meta);
            }

            modelSelection.setItem(slot, modelItem);
            slot++;
        }

        player.openInventory(modelSelection);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§b选择模型")) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String modelName = meta.getDisplayName().replace("§a", "");
        ModelManager.getInstance().switchModel(modelName);
        Utils.sendMessage(player, "§a已切换到模型: " + modelName);

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        player.closeInventory();
    }
}