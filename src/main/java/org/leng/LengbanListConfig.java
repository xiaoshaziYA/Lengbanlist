package org.leng;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // 更新封禁人数
    public void updateBanCount(int newBanCount) {
        config.set("ban-count", newBanCount);
        saveConfig();
    }

    // 更新封禁列表
    public void updateBanList(List<String> newBanList) {
        config.set("ban-list", newBanList);
        saveConfig();
    }

    // 获取封禁人数
    public int getBanCount() {
        return config.getInt("ban-count");
    }

    // 获取封禁列表
    public List<String> getBanList() {
        return config.getStringList("ban-list");
    }

    // 获取前缀
    public String getPrefix() {
        return config.getString("prefix");
    }

    // 获取默认消息
    public String getDefaultMessage() {
        return config.getString("default-message");
    }
}
