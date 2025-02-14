package org.leng.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.leng.Lengbanlist;
import org.leng.object.BanEntry;
import org.leng.manager.ModelManager;
import org.leng.models.Model;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;
import org.leng.utils.SaveIP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            case "model":
                if (!sender.hasPermission("lengbanlist.model")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban model <模型名称>");
                    Utils.sendMessage(sender, plugin.prefix() + "§6§l可用模型： §b Default HuTao Furina Zhongli Keqing Xiao Ayaka");
                    return true;
                }
                String modelName = args[1];
                if (!Lengbanlist.getInstance().getConfig().getString("valid-models").contains(modelName)) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不支持的模型名称。可用模型： §b Default HuTao Furina Zhongli Keqing Xiao Ayaka");
                    return true;
                }
                ModelManager.getInstance().switchModel(modelName);
                Utils.sendMessage(sender, plugin.prefix() + "§a已切换到模型: " + modelName);
                break;
            case "open":
                if (!sender.hasPermission("lengbanlist.open")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                    return true;
                }
                if (sender instanceof Player) {
                    openChestUI((Player) sender);
                } else {
                    Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
                }
                break;
            case "getip": // 新增 getip 子命令
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
                    Utils.sendMessage(sender, plugin.prefix() + "§a查询到玩家 " + target + " 的 IP 地址为 " + ip);
                }
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
        Inventory chest = Bukkit.createInventory(null, 9, "§bLengbanlist");

        // 创建一朵花（彩虹色的）
        ItemStack flower = createFlower();
        chest.setItem(0, flower);

        // 创建按钮
        ItemStack toggleBroadcast = createItem(
                "§a切换自动广播 (" + (Lengbanlist.getInstance().isBroadcastEnabled() ? "开启" : "关闭") + ")",
                "§7/lban toggle",
                "§7开启或关闭自动广播"
        );
        ItemStack broadcast = createItem("§a广播封禁人数", "§7/lban a", "§7广播当前封禁人数");
        ItemStack list = createItem("§a查看封禁名单", "§7/lban list", "§7查看被封禁的玩家列表");
        ItemStack reload = createItem("§a重新加载配置", "§7/lban reload", "§7重新加载插件配置");
        ItemStack add = createItem("§a添加封禁", "§7/lban add", "§7添加一个玩家到封禁名单");
        ItemStack remove = createItem("§a移除封禁", "§7/lban remove", "§7从封禁名单中移除一个玩家");
        ItemStack help = createItem("§a帮助信息", "§7/lban help", "§7显示帮助信息");
        ItemStack model = createItem(
                "§a切换模型 (" + ModelManager.getInstance().getCurrentModelName() + ")",
                "§7/lban model",
                "§7随机切换不同的风格模型"
        );
        ItemStack openUI = createItem("§a打开UI", "§7/lban open", "§7打开可视化操作界面");

        // 添加按钮到 Chest
        chest.setItem(1, toggleBroadcast);
        chest.setItem(2, broadcast);
        chest.setItem(3, list);
        chest.setItem(4, reload);
        chest.setItem(5, add);
        chest.setItem(6, remove);
        chest.setItem(7, help);
        chest.setItem(8, model);

        // 打开 Chest
        player.openInventory(chest);
    }

    // 创建一朵花（彩虹色的）
    private ItemStack createFlower() {
        ItemStack flower = new ItemStack(Material.POPPY); // 使用一朵花（例如蒲公英）
        ItemMeta meta = flower.getItemMeta();

        // 设置彩虹色的名称
        meta.setDisplayName("§cL§6e§eg§2b§3a§bn§5l§dst §d欢§2迎§3您§6的§c使§5用");

        // 设置描述（猫的颜文字）
        List<String> lore = new ArrayList<>();
        lore.add("§7(=^･ｪ･^=)");
        meta.setLore(lore);

        flower.setItemMeta(meta);
        return flower;
    }

    // 创建按钮
    private ItemStack createItem(String displayName, String command, String description) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> lore = new ArrayList<>();
        lore.add(command);
        lore.add(description);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}