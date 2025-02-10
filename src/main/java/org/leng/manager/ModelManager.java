package org.leng.manager;

import org.leng.LengbanList;
import org.leng.models.HuTao;
import org.leng.models.FuNingna;
import org.leng.models.Zhongli;
import org.leng.models.Keqing;
import org.leng.models.Xiao;
import org.leng.models.Ayaka;
import org.leng.models.Model;
import org.leng.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private static Map<String, Model> models = new HashMap<>();
    private static Model currentModel;

    public static void loadModel(String modelName) {
        try {
            Class<?> modelClass = Class.forName("org.leng.models." + modelName);
            Model model = (Model) modelClass.getDeclaredConstructor().newInstance();
            models.put(modelName, model);
            currentModel = model;
            LengbanList.getInstance().getServer().getConsoleSender().sendMessage("§a模型 " + modelName + " 已加载。");
        } catch (Exception e) {
            LengbanList.getInstance().getServer().getConsoleSender().sendMessage("§c模型 " + modelName + " 加载失败！");
            e.printStackTrace();
        }
    }

    public static Model getCurrentModel() {
        return currentModel;
    }

    public static String getCurrentModelName() {
        return currentModel != null ? currentModel.getName() : "未知模型";
    }

    public static void switchModel(String modelName) {
        if (models.containsKey(modelName)) {
            currentModel = models.get(modelName);
            LengbanList.getInstance().getConfig().set("Model", modelName);
            LengbanList.getInstance().saveConfig();
            LengbanList.getInstance().getServer().getConsoleSender().sendMessage("§a已切换到模型: " + modelName);
        } else {
            LengbanList.getInstance().getServer().getConsoleSender().sendMessage("§c模型 " + modelName + " 不存在。");
        }
    }
}