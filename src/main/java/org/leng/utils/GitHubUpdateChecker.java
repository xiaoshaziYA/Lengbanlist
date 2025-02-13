package org.leng.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.leng.Lengbanlist;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubUpdateChecker {
    private static final String REPO_URL = "https://api.github.com/repos/xiaoshaziYA/Lengbanlist/releases/latest";

    private static String getLatestReleaseVersion() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(REPO_URL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            StringBuilder response = new StringBuilder();
            int data = reader.read();
            while (data != -1) {
                response.append((char) data);
                data = reader.read();
            }
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonResponse.get("tag_name").getAsString();
        }
    }

    private static boolean isUpdateAvailable(String localVersion) throws Exception {
        String latestVersion = getLatestReleaseVersion();
        return !localVersion.equals(latestVersion);
    }

    public static void checkUpdata() {
        try {
            String localVersion = Lengbanlist.getInstance().getDescription().getVersion();
            if (isUpdateAvailable(localVersion)) {
                Lengbanlist.getInstance().getLogger().info("有新版本可用，当前版本：" + localVersion + "，最新版本：" + getLatestReleaseVersion() +" 请前往: https://github.com/xiaoshaziYA/Lengbanlist 进行更新插件");
            } else {
                Lengbanlist.getInstance().getLogger().info("当前是最新版本！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
