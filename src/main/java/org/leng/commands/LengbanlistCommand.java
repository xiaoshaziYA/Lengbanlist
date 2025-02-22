package org.leng.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.leng.Lengbanlist;
import org.leng.object.BanEntry;
import org.leng.manager.ModelManager;
import org.leng.models.Model;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;
import org.leng.utils.SaveIP;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class LengbanlistCommand extends Command {

    private final Lengbanlist plugin;

    public LengbanlistCommand(String name, Lengbanlist plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Model currentModel = ModelManager.getInstance().getCurrentModel();
        if (args.length == 0) {
            currentModel.showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                if (!sender.hasPermission("lengbanlist.toggle")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                boolean enabled = !Lengbanlist.getInstance().isBroadcastEnabled();
                Lengbanlist.getInstance().setBroadcastEnabled(enabled);
                Utils.sendMessage(sender, currentModel.toggleBroadcast(enabled));
                break;
            case "a":
                if (!sender.hasPermission("lengbanlist.broadcast")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                Lengbanlist.getInstance().getServer().broadcastMessage(Lengbanlist.getInstance().getConfig().getString("default-message").replace("%s", String.valueOf(Lengbanlist.getInstance().banManager.getBanList().size())));
                break;
            case "list":
                if (!sender.hasPermission("lengbanlist.list")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                showBanList(sender);
                break;
            case "reload":
                if (!sender.hasPermission("lengbanlist.reload")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                plugin.reloadConfig();
                Utils.sendMessage(sender, currentModel.reloadConfig());
                break;
            case "add":
                if (!sender.hasPermission("lengbanlist.ban")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 4) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban add <玩家名> <天数> <原因>");
                    return true;
                }
                Lengbanlist.getInstance().banManager.banPlayer(new BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.valueOf(args[2])), args[3]));
                Utils.sendMessage(sender, currentModel.addBan(args[1], Integer.valueOf(args[2]), args[3]));
                break;
            case "remove":
                if (!sender.hasPermission("lengbanlist.unban")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban remove <玩家名>");
                    return true;
                }
                Lengbanlist.getInstance().banManager.unbanPlayer(args[1]);
                Utils.sendMessage(sender, currentModel.removeBan(args[1]));
                break;
            case "help":
                if (!sender.hasPermission("lengbanlist.help")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                currentModel.showHelp(sender);
                break;
            case "open":
                if (!sender.hasPermission("lengbanlist.open")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f); // 播放音效
                    openChestUI(player);
                } else {
                    Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
                }
                break;
            case "getip":
                if (!sender.hasPermission("lengbanlist.getIP")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式 /lban getip <玩家名>");
                    return false;
                }
                String target = args[1];
                String ip = SaveIP.getIP(target);
                if (ip == null) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l查询不到玩家 " + target + " 的 IP 地址");
                } else {
                    // 调用 API 解析 IP 地址
                    String location = getIPLocation(ip);
                    if (location != null) {
                        Utils.sendMessage(sender, plugin.prefix() + "§a查询到玩家 " + target + " 的 IP 地址为 " + ip + "，地理位置：" + location);
                    } else {
                        Utils.sendMessage(sender, plugin.prefix() + "§a查询到玩家 " + target + " 的 IP 地址为 " + ip + "，但无法解析地理位置");
                    }
                }
                break;
            case "model":
                if (!sender.hasPermission("lengbanlist.model")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban model <模型名称>");
                    // 动态获取可用模型名称
                    StringBuilder availableModels = new StringBuilder("§6§l可用模型： §b");
                    for (String modelName : ModelManager.getInstance().getModels().keySet()) {
                        availableModels.append(modelName).append(" ");
                    }
                    Utils.sendMessage(sender, availableModels.toString());
                    return true;
                }
                String modelName = args[1];
                if (!ModelManager.getInstance().getModels().containsKey(modelName)) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不支持的模型名称。");
                    // 动态获取可用模型名称
                    StringBuilder availableModels = new StringBuilder("§6§l可用模型： §b");
                    for (String name : ModelManager.getInstance().getModels().keySet()) {
                        availableModels.append(name).append(" ");
                    }
                    Utils.sendMessage(sender, availableModels.toString());
                    return true;
                }
                ModelManager.switchModel(modelName);
                Utils.sendMessage(sender, plugin.prefix() + "§a已切换到模型: " + modelName);
                break;
            default:
                Utils.sendMessage(sender, "未知的子命令。");
                break;
        }
        return true;
    }

    private void showBanList(CommandSender sender) {
        Utils.sendMessage(sender, "§7--§bLengbanlist§7--");
        for (BanEntry entry : Lengbanlist.getInstance().banManager.getBanList()) {
            Utils.sendMessage(sender, "§9§o被封禁者: " + entry.getTarget() + " §6处理人: " + entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
    }

    private void openChestUI(Player player) {
        Inventory chest = Bukkit.createInventory(null, 27, "§bLengbanlist"); // 扩大为 27 格

        // 设置玻璃背景
        ItemStack glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < 27; i++) {
            chest.setItem(i, glass);
        }

        // 创建按钮
        ItemStack toggleBroadcast = createItem(
                "§a切换自动广播 (" + (Lengbanlist.getInstance().isBroadcastEnabled() ? "开启" : "关闭") + ")",
                "§7/lban toggle",
                "§7开启或关闭自动广播",
                Sound.BLOCK_LEVER_CLICK
        );
        ItemStack broadcast = createItem("§a广播封禁人数", "§7/lban a", "§7广播当前封禁人数", Sound.BLOCK_NOTE_BLOCK_PLING);
        ItemStack list = createItem("§a查看封禁名单", "§7/lban list", "§7查看被封禁的玩家列表", Sound.BLOCK_NOTE_BLOCK_HARP);
        ItemStack reload = createItem("§a重新加载配置", "§7/lban reload", "§7重新加载插件配置", Sound.BLOCK_NOTE_BLOCK_BELL);
        ItemStack add = createItem("§a添加封禁", "§7/lban add", "§7添加一个玩家到封禁名单", Sound.BLOCK_NOTE_BLOCK_BASS);
        ItemStack remove = createItem("§a移除封禁", "§7/lban remove", "§7从封禁名单中移除一个玩家", Sound.BLOCK_NOTE_BLOCK_SNARE);
        ItemStack help = createItem("§a帮助信息", "§7/lban help", "§7显示帮助信息", Sound.BLOCK_NOTE_BLOCK_FLUTE);
        ItemStack model = createItem(
                "§a切换模型 (" + ModelManager.getInstance().getCurrentModelName() + ")",
                "§7/lban model",
                "§7当前模型: " + ModelManager.getInstance().getCurrentModelName(),
                Sound.BLOCK_NOTE_BLOCK_CHIME
        );
        ItemStack sponsor = createItem("§6赞助作者", "§7点击打开赞助链接", "§7https://afdian.com/a/lengbanlist", Sound.BLOCK_NOTE_BLOCK_PLING);

        // 添加按钮到 Chest
        chest.setItem(10, toggleBroadcast);
        chest.setItem(11, broadcast);
        chest.setItem(12, list);
        chest.setItem(13, reload);
        chest.setItem(14, add);
        chest.setItem(15, remove);
        chest.setItem(16, help);
        chest.setItem(17, model);
        chest.setItem(22, sponsor);

        // 打开 Chest
        player.openInventory(chest);
    }

    private ItemStack createFlower() {
        ItemStack flower = new ItemStack(Material.POPPY); // 使用一朵花（例如蒲公英）
        ItemMeta meta = flower.getItemMeta();

        meta.setDisplayName("§bLengbanlist §d欢§2迎§3您§6的§c使§5用");

        List<String> lore = new ArrayList<>();
        lore.add("§7(=^･ｪ･^=)");
        meta.setLore(lore);

        flower.setItemMeta(meta);
        return flower;
    }

    // 创建按钮
    private ItemStack createItem(String displayName, String command, String description, Sound sound) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> lore = new ArrayList<>();
        lore.add(command);
        lore.add(description);
        meta.setLore(lore);
        item.setItemMeta(meta);

        // 播放音效
        if (sound != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Player player = Bukkit.getPlayer(meta.getDisplayName());
                if (player != null) {
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                }
            }, 1L);
        }

        return item;
    }

    /**
     * 调用 API 解析 IP 地址的地理位置
     *
     * @param ip 需要解析的 IP 地址
     * @return 解析后的地理位置信息，如果解析失败则返回 null
     */
    private String getIPLocation(String ip) {
        try {
            // 构建 API 请求 URL
            String apiUrl = "https://www.ip.cn/api/index?ip=" + ip + "&type=0";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 读取 API 响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 解析 API 响应（假设返回的是 JSON 格式）
            String jsonResponse = response.toString();
            // 这里可以根据 API 返回的实际 JSON 结构进行解析
            // 例如，假设返回的 JSON 中包含 "location" 字段
            String location = jsonResponse.split("\"location\":\"")[1].split("\"")[0];
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 解析失败时返回 null
        }
    }
}