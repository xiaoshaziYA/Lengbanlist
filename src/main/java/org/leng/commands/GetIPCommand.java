package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.leng.utils.SaveIP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetIPCommand extends Command {
    public GetIPCommand() {
        super("lban getIP"); 
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("§c§l错误的命令格式，正确格式 /lban getIP <玩家名>");
            return false;
        }
        String target = strings[0];
        String ip = SaveIP.getIP(target);
        if (ip == null) {
            commandSender.sendMessage("§c§l查询不到玩家 " + target + " 的 IP 地址");
        } else {
            // 调用 API 解析 IP 地址
            String location = getIPLocation(ip);
            if (location != null) {
                commandSender.sendMessage("§a查询到玩家 " + target + " 的 IP 地址为 " + ip + "，地理位置：" + location);
            } else {
                commandSender.sendMessage("§a查询到玩家 " + target + " 的 IP 地址为 " + ip + "，但无法解析地理位置");
            }
        }
        return true; // 返回值为 true 表示命令成功执行
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