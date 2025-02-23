package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.models.Model;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration; // 导入 FileConfiguration

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private static ModelManager instance;
    private static Map<String, Model> models = new HashMap<>();
    private static Model currentModel;

    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    private ModelManager() {
        // 初始化所有模型
        loadModel("Default");
        loadModel("HuTao");
        loadModel("Furina");
        loadModel("Zhongli");
        loadModel("Keqing");
        loadModel("Xiao");
        loadModel("Ayaka");
        loadModel("Zero");
        loadModel("Herta");

        // 加载当前配置中指定的模型
        String modelName = Lengbanlist.getInstance().getConfig().getString("Model", "Default");
        switchModel(modelName.toLowerCase());
    }

    public static void loadModel(String modelName) {
        try {
            Class<?> modelClass = Class.forName("org.leng.models." + modelName);
            Model model = (Model) modelClass.getDeclaredConstructor().newInstance();
            models.put(modelName.toLowerCase(), model);
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§a模型 " + modelName + " 已加载。");
        } catch (Exception e) {
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§c模型 " + modelName + " 加载失败！");
            e.printStackTrace();
        }
    }

    public static Model getCurrentModel() {
        return currentModel;
    }

    public static String getCurrentModelName() {
        return currentModel != null ? currentModel.getName() : "未知模型";
    }

    public static void switchModel(String modelName) {
        if (models.containsKey(modelName.toLowerCase())) {
            currentModel = models.get(modelName.toLowerCase());
            Lengbanlist.getInstance().getConfig().set("Model", currentModel.getName());
            Lengbanlist.getInstance().saveConfig();
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§a已切换到模型: " + currentModel.getName());
        } else {
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§c模型 " + modelName + " 不存在。");
        }
    }

    public Map<String, Model> getModels() {
        return models;
    }

    public void reloadModel() {
        // 重新加载当前配置中指定的模型
        String modelName = Lengbanlist.getInstance().getConfig().getString("Model", "Default");
        switchModel(modelName.toLowerCase());
        Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§a模型已重新加载，当前模型: " + currentModel.getName());
    }

    // 获取模型的材料（通过配置文件）
    public static Material getModelMaterial(String modelName) {
        // 从配置文件中获取模型的材料名称
        FileConfiguration config = Lengbanlist.getInstance().getConfig();
        String materialName = config.getString("models." + modelName.toLowerCase() + ".material", "PAPER");

        // 将材料名称转换为 Material 类型
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            // 如果找不到对应的 Material，返回默认值 PAPER
            return Material.PAPER;
        }
        return material;
    }

    // 添加 openModelSelectionUI 方法
    public void openModelSelectionUI(Player player) {
        // 创建一个新的库存界面，用于选择模型
        Inventory modelSelectionUI = Bukkit.createInventory(player, 9, "§b选择模型");

        // 遍历所有模型，将它们添加到库存界面中
        for (Map.Entry<String, Model> entry : models.entrySet()) {
            ItemStack item = new ItemStack(Material.PAPER); // 使用纸作为物品
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a" + entry.getKey()); // 设置模型名称为物品的显示名称
            item.setItemMeta(meta);
            modelSelectionUI.addItem(item);
        }

        // 打开库存界面给玩家
        player.openInventory(modelSelectionUI);
    }
}