package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.leng.commands.BanCommand;
import org.leng.commands.LengbanListCommand;
import org.leng.commands.UnbanCommand;
import org.leng.manager.BanManager;
import org.leng.manager.ModelManager;
import org.leng.utils.GitHubUpdateChecker;

import java.lang.reflect.Field;

public class LengbanList extends JavaPlugin {
    private static LengbanList instance;
    public BanManager banManager;
    public BukkitTask task;
    private boolean isBroadcast;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        instance = this;
        banManager = new BanManager();
        isBroadcast = getConfig().getBoolean("opensendtime");
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§f§2正在加载原神");
        String modelName = getConfig().getString("Model", "HuTao");
        ModelManager.loadModel(modelName);
        getServer().getConsoleSender().sendMessage(prefix() + "§f§2传送锚点已解锁，当前Model: " + ModelManager.getCurrentModelName());
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getCommandMap().register("", new LengbanListCommand("lban", this));
        getCommandMap().register("", new BanCommand());
        getCommandMap().register("", new UnbanCommand());
        getServer().getConsoleSender().sendMessage("§b  _                      ____              _      _     _   ");
        getServer().getConsoleSender().sendMessage("§6 | |                    |  _ \\            | |    (_)   | |  ");
        getServer().getConsoleSender().sendMessage("§b | |     ___ _ __   __ _| |_) | __ _ _ __ | |     _ ___| |_ ");
        getServer().getConsoleSender().sendMessage("§f | |    / _ \\ '_ \\ / _` |  _ < / _` | '_ \\| |    | / __| __|");
        getServer().getConsoleSender().sendMessage("§b | |___|  __/ | | | (_| | |_) | (_| | | | | |____| \\__ \\ |_ ");
        getServer().getConsoleSender().sendMessage(" §6|______\\___|_| |_\\__, |____/ \\__,_|_| |_|______|_|___/\\__|");
        getServer().getConsoleSender().sendMessage("§b                   __/ |                                    ");
        getServer().getConsoleSender().sendMessage("§f                   |___/                                     ");
        getServer().getConsoleSender().sendMessage("§b当前运行版本：v1.3.4");
        getServer().getConsoleSender().sendMessage("§b当前运行在：" + Bukkit.getServer().getVersion());
        getServer().getConsoleSender().sendMessage("§b赞助获得更多福利:https://afdian.com/a/lengbanlist");
        new Metrics(this, 24495);
        GitHubUpdateChecker.checkUpdata();
        if (isBroadcast) {
            task = new BroadCastBanCountMessage().runTaskTimer(LengbanList.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§4正在卸载");
        getServer().getConsoleSender().sendMessage(prefix() + "§f期待我们的下一次相遇！");
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

    public boolean isBroadcastEnabled() {
        return isBroadcast;
    }

    public void setBroadcastEnabled(boolean broadcastEnabled) {
        this.isBroadcast = broadcastEnabled;
        if (isBroadcast) {
            task = new BroadCastBanCountMessage().runTaskTimer(LengbanList.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
        } else {
            if (task != null) {
                task.cancel();
            }
        }
    }

    public String toggleBroadcast() {
        setBroadcastEnabled(!isBroadcastEnabled());
        return isBroadcastEnabled() ? "§a已开启" : "§c已关闭";
    }
}