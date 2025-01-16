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
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getCommandMap().register("", new LengbanListCommand("lban", this));
        getCommandMap().register("", new LengbanListCommand("ban", this)); // 注册新的 /ban 命令

        getServer().getConsoleSender().sendMessage("  _                      ____              _      _     _   ");
        getServer().getConsoleSender().sendMessage(" | |                    |  _ \\            | |    (_)   | |  ");
        getServer().getConsoleSender().sendMessage(" | |     ___ _ __   __ _| |_) | __ _ _ __ | |     _ ___| |_ ");
        getServer().getConsoleSender().sendMessage(" | |    / _ \\ '_ \\ / _` |  _ < / _` | '_ \\| |    | / __| __|");
        getServer().getConsoleSender().sendMessage(" | |___|  __/ | | | (_| | |_) | (_| | | | | |____| \\__ \\ |_ ");
        getServer().getConsoleSender().sendMessage(" |______\\___|_| |_\\__, |____/ \\__,_|_| |_|______|_|___/\\__|");
        getServer().getConsoleSender().sendMessage("                    __/ |                                    ");
        getServer().getConsoleSender().sendMessage("                   |___/                                     ");
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
