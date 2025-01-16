package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.leng.manager.BanManager;

import java.lang.reflect.Field;

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
        getServer().getPluginManager().registerEvents(new Listener(),this);
        getCommandMap().register("",new LengbanListCommand("lban",this));
        if (getConfig().getBoolean("opensendtime")){
            new BroadCastBanCountMessage().runTaskTimer(LengbanList.getInstance(), 0L, getConfig().getInt("sendtime")*1200L);
        }
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

    public static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commandMap;
    }
}