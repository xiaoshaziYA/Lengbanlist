package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.leng.commands.*;
import org.leng.listeners.*;
import org.leng.manager.*;
import org.leng.utils.GitHubUpdateChecker;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Lengbanlist extends JavaPlugin {
    private static Lengbanlist instance;
    public BanManager banManager;
    public MuteManager muteManager;
    public WarnManager warnManager;
    public BukkitTask task;
    private boolean isBroadcast;
    public FileConfiguration ipFC;
    private FileConfiguration banFC;
    private FileConfiguration banIpFC;
    private FileConfiguration muteFC;
    private FileConfiguration broadcastFC;
    private FileConfiguration warnFC;
    private ModelChoiceListener modelChoiceListener;
    private String hitokoto; 

    @Override
    public void onLoad() {
        saveDefaultConfig();
        instance = this;
        banManager = new BanManager();
        muteManager = new MuteManager();
        warnManager = new WarnManager();
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
        File warnFile = new File(getDataFolder(), "warn-list.yml");
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
        if (!warnFile.exists()) {
            warnFile.getParentFile().mkdirs();
            saveResource("warn-list.yml", false);
        }
        banFC = YamlConfiguration.loadConfiguration(banFile);
        banIpFC = YamlConfiguration.loadConfiguration(banIpFile);
        muteFC = YamlConfiguration.loadConfiguration(muteFile);
        warnFC = YamlConfiguration.loadConfiguration(warnFile);

        // 获取一言并存储到成员变量
        hitokoto = getHitokoto();

        // 初始化 broadcastFC
        File broadcastFile = new File(getDataFolder(), "broadcast.yml");
        if (!broadcastFile.exists()) {
            broadcastFile.getParentFile().mkdirs();
            saveResource("broadcast.yml", false);
        }
        broadcastFC = YamlConfiguration.loadConfiguration(broadcastFile);
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§f原神§2正在加载");
        getServer().getConsoleSender().sendMessage(prefix() + "§6偷偷告诉你: §e" + hitokoto); 
        ModelManager.getInstance();
        getServer().getConsoleSender().sendMessage(prefix() + "§f哇！传送锚点已解锁，当前Model: " + ModelManager.getInstance().getCurrentModelName());

        // 注册监听器
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new OpJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestUIListener(this), this);
        getServer().getPluginManager().registerEvents(new AnvilGUIListener(this), this);
        modelChoiceListener = new ModelChoiceListener(this);
        getServer().getPluginManager().registerEvents(modelChoiceListener, this);

        // 注册命令
        getCommandMap().register("", new LengbanlistCommand("lban", this));
        getCommandMap().register("", new BanCommand());
        getCommandMap().register("", new BanIpCommand());
        getCommandMap().register("", new UnbanCommand());
        getCommandMap().register("", new WarnCommand(this));
        getCommandMap().register("", new UnwarnCommand(this));
        getCommandMap().register("", new CheckCommand(this));

        getServer().getConsoleSender().sendMessage("§b  _                      ____              _      _     _   ");
        getServer().getConsoleSender().sendMessage("§6 | |                    |  _ \\            | |    (_)   | |  ");
        getServer().getConsoleSender().sendMessage("§b | |     ___ _ __   __ _| |_) | __ _ _ __ | |     _ ___| |_ ");
        getServer().getConsoleSender().sendMessage("§f | |    / _ \\ '_ \\ / _` |  _ < / _` | '_ \\| |    | / __| __|");
        getServer().getConsoleSender().sendMessage("§b | |___|  __/ | | | (_| | |_) | (_| | | | | |____| \\__ \\ |_ ");
        getServer().getConsoleSender().sendMessage(" §6|______\\___|_| |_\\__,|_|___/ \\__,_|_| |_|______|_|___/\\__|");
        getServer().getConsoleSender().sendMessage("§b                   __/ |                                    ");
        getServer().getConsoleSender().sendMessage("§f                   |___/                                     ");
        getServer().getConsoleSender().sendMessage("§6当前运行版本：v" + getPluginVersion());
        getServer().getConsoleSender().sendMessage("§3当前运行在：" + Bukkit.getServer().getVersion());
        getServer().getConsoleSender().sendMessage("§b感谢开源社区以及作者：§nLeng");

        new Metrics(this, 24495);
        GitHubUpdateChecker.checkUpdate();

        if (isBroadcast) {
            task = new BroadCastBanCountMessage().runTaskTimer(Lengbanlist.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§k§4正在卸载");
        if (task != null) {
            task.cancel();
        }

        // 注销所有监听器
        org.bukkit.event.HandlerList.unregisterAll(this);

        // 保存配置文件
        saveBanConfig();
        saveBanIpConfig();
        saveMuteConfig();
        saveBroadcastConfig();
        saveWarnConfig();
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

    public WarnManager getWarnManager() {
        return warnManager;
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

    public FileConfiguration getBroadcastFC() {
        return broadcastFC;
    }

    public FileConfiguration getWarnFC() {
        return warnFC;
    }

    public FileConfiguration getIpFC() {
        return ipFC;
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

    public void saveBroadcastConfig() {
        try {
            broadcastFC.save(new File(getDataFolder(), "broadcast.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWarnConfig() {
        try {
            warnFC.save(new File(getDataFolder(), "warn-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChestUIListener getChestUIListener() {
        return new ChestUIListener(this);
    }

    // 获取一言的方法
    public String getHitokoto() {
        try {
            URL url = new URL("https://v1.hitokoto.cn/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                getLogger().warning("一言 API 请求失败，状态码: " + responseCode);
                return "§c无法获取一言";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 解析 JSON 响应
            String jsonResponse = response.toString();
            String hitokoto = jsonResponse.split("\"hitokoto\":\"")[1].split("\"")[0];
            String from = jsonResponse.split("\"from\":\"")[1].split("\"")[0];
            return hitokoto + " —— " + from;
        } catch (Exception e) {
            getLogger().warning("获取一言时出错: " + e.getMessage());
            return "§c无法获取一言";
        }
    }
}