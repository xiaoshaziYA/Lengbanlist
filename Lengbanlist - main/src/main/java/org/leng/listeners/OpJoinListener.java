package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.leng.Lengbanlist;
import org.leng.utils.GitHubUpdateChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class OpJoinListener implements Listener {
    private final Lengbanlist plugin;

    // 修改构造函数，接受 Lengbanlist 参数
    public OpJoinListener(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            try {
                String pluginName = plugin.getDescription().getName();
                String pluginVersion = plugin.getDescription().getVersion();
                String updateUrl = "https://github.com/xiaoshaziYA/Lengbanlist/releases/latest";
                String latestVersion = GitHubUpdateChecker.getLatestReleaseVersion();

                String prefix = plugin.prefix();
                TextComponent message = new TextComponent(prefix + " 当前插件版本：" + pluginVersion + "，最新版本：" + latestVersion);
                TextComponent clickableLink = new TextComponent("§b§n点击更新awa");
                clickableLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateUrl));
                clickableLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7点击打开更新链接").create()));

                player.spigot().sendMessage(message, clickableLink);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(plugin.prefix() + "§c无法获取最新版本信息，请检查网络连接！");
            }
        }
    }
}