package org.leng;

import org.bukkit.plugin.java.JavaPlugin;
import org.leng.manager.BanManager;

public class LengbanList extends JavaPlugin {
    private static LengbanList instance;
    public BanManager banManager;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
        banManager = new BanManager();
        getServer().getConsoleSender().sendMessage(prefix() + "正在加载");
        this.getCommand("lban").setExecutor(new LengbanListCommandExecutor(this));
        getServer().getPluginManager().registerEvents(new Listener(),this);
    }

    @Override
    public void onDisable() {
    }

    public String prefix() {
        return getConfig().getString("prefix");
    }


    public static LengbanList getInstance() {
        return instance;
    }
}