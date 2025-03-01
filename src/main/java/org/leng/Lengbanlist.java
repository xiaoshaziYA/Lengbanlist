package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.leng.commands.BanCommand;
import org.leng.commands.BanIpCommand;
import org.leng.commands.LengbanlistCommand;
import org.leng.commands.UnbanCommand;
import org.leng.listeners.ChestUIListener;
import org.leng.listeners.PlayerJoinListener;
import org.leng.listeners.AnvilGUIListener;
import org.leng.listeners.ModelChoiceListener;
import org.leng.manager.BanManager;
import org.leng.manager.MuteManager;
import org.leng.manager.ModelManager;
import org.leng.utils.GitHubUpdateChecker;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class Lengbanlist extends JavaPlugin {
    private static Lengbanlist instance;
    public BanManager banManager;
    public MuteManager muteManager;
    public BukkitTask task;
    private boolean isBroadcast;
    public FileConfiguration ipFC;
    private FileConfiguration banFC;
    private FileConfiguration banIpFC;
    private FileConfiguration muteFC;
    private ModelChoiceListener modelChoiceListener;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        instance = this;
        banManager = new BanManager();
        muteManager = new MuteManager();
        isBroadcast = getConfig().getBoolean("opensendtime");

        // 初始化 ipFC
        File ipFile = new File(getDataFolder(), "ip.yml");
        if (!ipFile.exists()) {
            ipFile.getParentFile().mkdirs();
            saveResource("ip.yml", false);
        }
        ipFC = YamlConfiguration.loadConfiguration(ipFile);

        // 初始化 banFC 和 banIpFC
        File banFile = new File(getDataFolder(), "ban-list.yml");
        File banIpFile = new File(getDataFolder(), "banip-list.yml");
        File muteFile = new File(getDataFolder(), "mute-list.yml");
        if (!banFile.exists()) {
            banFile.getParentFile().mkdirs();
            saveResource("ban-list.yml", false);
        }
        if (!banIpFile.exists()) {
            banIpFile.getParentFile().mkdirs();
            saveResource("banip-list.yml", false);
        }
        if (!muteFile.exists()) {
            muteFile.getParentFile().mkdirs();
            saveResource("mute-list.yml", false);
        }
        banFC = YamlConfiguration.loadConfiguration(banFile);
        banIpFC = YamlConfiguration.loadConfiguration(banIpFile);
        muteFC = YamlConfiguration.loadConfiguration(muteFile);
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§f原神§2正在加载");
        ModelManager.getInstance();
        getServer().getConsoleSender().sendMessage(prefix() + "§f哇！传送锚点已解锁，当前Model: " + ModelManager.getInstance().getCurrentModelName());

        // 注册监听器
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChestUIListener(this), this);
        getServer().getPluginManager().registerEvents(new AnvilGUIListener(this), this);

        // 初始化 ModelChoiceListener
        modelChoiceListener = new ModelChoiceListener(this);
        getServer().getPluginManager().registerEvents(modelChoiceListener, this);

        // 注册命令
        getCommandMap().register("", new LengbanlistCommand("lban", this));
        getCommandMap().register("", new BanCommand());
        getCommandMap().register("", new BanIpCommand());
        getCommandMap().register("", new UnbanCommand());
        getCommandMap().register("", new BanIpCommand()); 

        getServer().getConsoleSender().sendMessage("§b  _                      ____              _      _     _   ");
        getServer().getConsoleSender().sendMessage("§6 | |                    |  _ \\            | |    (_)   | |  ");
        getServer().getConsoleSender().sendMessage("§b | |     ___ _ __   __ _| |_) | __ _ _ __ | |     _ ___| |_ ");
        getServer().getConsoleSender().sendMessage("§f | |    / _ \\ '_ \\ / _` |  _ < / _` | '_ \\| |    | / __| __|");
        getServer().getConsoleSender().sendMessage("§b | |___|  __/ | | | (_| | |_) | (_| | | | | |____| \\__ \\ |_ ");
        getServer().getConsoleSender().sendMessage(" §6|______\\___|_| |_\\__,|_|___/ \\__,_|_| |_|______|_|___/\\__|");
        getServer().getConsoleSender().sendMessage("§b                   __/ |                                    ");
        getServer().getConsoleSender().sendMessage("§f                   |___/                                     ");
        getServer().getConsoleSender().sendMessage("§b当前运行版本：v" + getPluginVersion());
        getServer().getConsoleSender().sendMessage("§b当前运行在：" + Bukkit.getServer().getVersion());
        getServer().getConsoleSender().sendMessage("§b赞助获得更多福利:https://afdian.com/a/lengbanlist");

        new Metrics(this, 24495);
        GitHubUpdateChecker.checkUpdata();

        if (isBroadcast) {
            task = new BroadCastBanCountMessage().runTaskTimer(Lengbanlist.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
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

    public static Lengbanlist getInstance() {
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
            task = new BroadCastBanCountMessage().runTaskTimer(Lengbanlist.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
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

    public ModelManager getModelManager() {
        return ModelManager.getInstance();
    }

    public String getPluginVersion() {
        return getDescription().getVersion();
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public ModelChoiceListener getModelChoiceListener() {
        return modelChoiceListener;
    }

    public FileConfiguration getBanFC() {
        return banFC;
    }

    public FileConfiguration getBanIpFC() {
        return banIpFC;
    }

    public FileConfiguration getMuteFC() {
        return muteFC;
    }

    public void saveBanConfig() {
        try {
            banFC.save(new File(getDataFolder(), "ban-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBanIpConfig() {
        try {
            banIpFC.save(new File(getDataFolder(), "banip-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMuteConfig() {
        try {
            muteFC.save(new File(getDataFolder(), "mute-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ChestUIListener getChestUIListener() {
    return new ChestUIListener(this);
    }
}