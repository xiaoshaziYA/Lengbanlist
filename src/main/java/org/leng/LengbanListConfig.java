package org.leng;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LengbanListConfig {

    private final LengbanList plugin;

    public LengbanListConfig(LengbanList plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        plugin.reloadConfig();
    }
}
