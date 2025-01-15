package org.leng;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LengbanListConfig {

    private final LengbanList plugin;
    private FileConfiguration config;
    private File configFile;

    public LengbanListConfig(LengbanList plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        // 创建配置文件对象
        configFile = new File(plugin.getDataFolder(), "config.yml");
        // 如果配置文件不存在，则创建一个默认的
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        // 加载配置文件
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // 获取配置文件对象
    public FileConfiguration getConfig() {
        return config;
    }

    // 保存配置文件
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    // 重新加载配置文件
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // 更新封禁人数
    public void updateBanCount(int newBanCount) {
        config.set("ban-count", newBanCount);
        saveConfig();
    }

    // 更新封禁列表
    public void updateBanList(java.util.List<String> newBanList) {
        config.set("ban-list", newBanList);
        saveConfig();
    }
}
