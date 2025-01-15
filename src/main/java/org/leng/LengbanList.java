package org.leng;

import org.bukkit.plugin.java.JavaPlugin;

public class LengbanList extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(prefix() + "正在加载");
        this.getCommand("lban").setExecutor(new LengbanListCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String prefix() {
        return getConfig().getString("prefix");
    }
}