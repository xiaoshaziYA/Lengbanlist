package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetIPCommand implements CommandExecutor {
    private final Lengbanlist plugin;

    public GetIPCommand(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查是否有权限（可选，根据需要添加权限检查）
        if (!sender.hasPermission("lengbanlist.getip")) {
            sender.sendMessage(plugin.prefix() + "§c你没有权限使用该命令！");
            return true;
        }

        // 如果没有提供参数，尝试获取发送者的 IP（如果发送者是玩家）
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerIp = player.getAddress().getAddress().getHostAddress();
                String ipLocation = getIPLocation(playerIp);

                if (ipLocation != null) {
                    player.sendMessage(plugin.prefix() + "§a你的 IP 地理位置为：§e" + ipLocation);
                } else {
                    player.sendMessage(plugin.prefix() + "§c无法获取 IP 地理位置信息，请检查网络连接！");
                }
            } else {
                sender.sendMessage(plugin.prefix() + "§c请指定一个玩家名称，例如: /getip <玩家名称>");
            }
        } else {
            // 如果提供了玩家名称，尝试获取该玩家的 IP
            Player targetPlayer = plugin.getServer().getPlayer(args[0]);
            if (targetPlayer != null) {
                String playerIp = targetPlayer.getAddress().getAddress().getHostAddress();
                String ipLocation = getIPLocation(playerIp);

                if (ipLocation != null) {
                    sender.sendMessage(plugin.prefix() + "§a玩家 " + targetPlayer.getName() + " 的 IP 地理位置为：§e" + ipLocation);
                } else {
                    sender.sendMessage(plugin.prefix() + "§c无法获取玩家 " + targetPlayer.getName() + " 的 IP 地理位置信息！");
                }
            } else {
                sender.sendMessage(plugin.prefix() + "§c未找到玩家：" + args[0]);
            }
        }
        return true;
    }

    /**
     * 调用 API 解析 IP 地址的地理位置
     *
     * @param ip 需要解析的 IP 地址
     * @return 解析后的地理位置信息，如果解析失败则返回 null
     */
    private String getIPLocation(String ip) {
        try {
            String apiUrl = "https://ipapi.co/" + ip + "/json/";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                plugin.getLogger().warning("IP API 请求失败，状态码: " + responseCode);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();
            if (jsonResponse.contains("\"country_name\"")) {
                String country = jsonResponse.split("\"country_name\":\"")[1].split("\"")[0];
                String region = jsonResponse.split("\"region\":\"")[1].split("\"")[0];
                String city = jsonResponse.split("\"city\":\"")[1].split("\"")[0];
                return country + ", " + region + ", " + city;
            } else {
                plugin.getLogger().warning("API 响应格式异常: " + jsonResponse);
                return null;
            }
        } catch (Exception e) {
            plugin.getLogger().warning("解析 IP 地理位置时出错: " + e.getMessage());
            return null;
        }
    }
}