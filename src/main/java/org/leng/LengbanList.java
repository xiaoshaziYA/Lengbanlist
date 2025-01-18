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
        getServer().getConsoleSender().sendMessage(prefix() + "§f§2正在加载");
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getCommandMap().register("", new LengbanListCommand("lban", this));

        getServer().getConsoleSender().sendMessage("§b  _                      ____              _      _     _   ");
        getServer().getConsoleSender().sendMessage("§b | |                    |  _ \\            | |    (_)   | |  ");
        getServer().getConsoleSender().sendMessage("§b | |     ___ _ __   __ _| |_) | __ _ _ __ | |     _ ___| |_ ");
        getServer().getConsoleSender().sendMessage("§b | |    / _ \\ '_ \\ / _` |  _ < / _` | '_ \\| |    | / __| __|");
        getServer().getConsoleSender().sendMessage("§b | |___|  __/ | | | (_| | |_) | (_| | | | | |____| \\__ \\ |_ ");
        getServer().getConsoleSender().sendMessage(" §b|______\\___|_| |_\\__, |____/ \\__,_|_| |_|______|_|___/\\__|");
        getServer().getConsoleSender().sendMessage("§b                   __/ |                                    ");
        getServer().getConsoleSender().sendMessage("§b                   |___/                                     ");
        getServer().getConsoleSender().sendMessage("§b当前运行版本：v1.3 - SPIGOT");
        getServer().getConsoleSender().sendMessage("§f推荐检查新版本在:https://github.com/xiaoshaziYA/Lengbanlist");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§4正在卸载");
        getServer().getConsoleSender().sendMessage(prefix() + "§f期待我们的下一次相遇！");
        getServer().getConsoleSender().sendMessage("§f推荐检查新版本在:https://github.com/xiaoshaziYA/Lengbanlist");
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
