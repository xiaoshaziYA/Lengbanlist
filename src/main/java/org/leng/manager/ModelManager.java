package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.models.Default;
import org.leng.models.HuTao;
import org.leng.models.Furina;
import org.leng.models.Zhongli;
import org.leng.models.Keqing;
import org.leng.models.Xiao;
import org.leng.models.Ayaka;
import org.leng.models.Zero;
import org.leng.models.Herta;
import org.leng.models.Model;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private static ModelManager instance;
    private static Map<String, Model> models = new HashMap<>();
    private static Model currentModel;

    public static ModelManager getInstance() {
        if (instance == null) {
            instance = new ModelManager();
        }
        return instance;
    }

    private ModelManager() {
        // 初始化所有模型
        loadModel("Default");
        loadModel("HuTao");
        loadModel("Furina");
        loadModel("Zhongli");
        loadModel("Keqing");
        loadModel("Xiao");
        loadModel("Ayaka");
        loadModel("Zero"); 
        loadModel("Herta"); 

        // 加载当前配置中指定的模型
        String modelName = Lengbanlist.getInstance().getConfig().getString("Model", "Default");
        switchModel(modelName);
    }

    public static void loadModel(String modelName) {
        try {
            Class<?> modelClass = Class.forName("org.leng.models." + modelName);
            Model model = (Model) modelClass.getDeclaredConstructor().newInstance();
            models.put(modelName, model);
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§a模型 " + modelName + " 已加载。");
        } catch (Exception e) {
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§c模型 " + modelName + " 加载失败！");
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
            Lengbanlist.getInstance().getConfig().set("Model", modelName);
            Lengbanlist.getInstance().saveConfig();
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§a已切换到模型: " + modelName);
        } else {
            Lengbanlist.getInstance().getServer().getConsoleSender().sendMessage("§c模型 " + modelName + " 不存在。");
        }
    }

    public Map<String, Model> getModels() {
        return models;
    }
}