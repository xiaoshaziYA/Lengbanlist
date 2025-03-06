package org.leng.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.leng.Lengbanlist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubUpdateChecker {
    private static final String REPO_URL = "https://api.github.com/repos/xiaoshaziYA/Lengbanlist/releases/latest";

    public static String getLatestReleaseVersion() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(REPO_URL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonResponse.get("tag_name").getAsString();
        }
    }

    public static void checkUpdate() { 
        try {
            String localVersion = Lengbanlist.getInstance().getDescription().getVersion();
            String latestVersion = getLatestReleaseVersion();
            if (!localVersion.equals(latestVersion)) {
                Lengbanlist.getInstance().getLogger().info("有新版本可用，当前版本：" + localVersion + "，最新版本：" + latestVersion + " 请前往: https://github.com/xiaoshaziYA/Lengbanlist/releases/latest 进行更新插件");
            } else {
                Lengbanlist.getInstance().getLogger().info("当前是最新版本！");
            }
        } catch (Exception e) {
            Lengbanlist.getInstance().getLogger().warning("检查更新时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
}