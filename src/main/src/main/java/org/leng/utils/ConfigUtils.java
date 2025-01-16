package org.leng.utils;

import org.leng.LengbanList;

import java.io.IOException;
import java.util.List;

public class ConfigUtils {
    public static void addValueToList(String listname,String newValue) {
        List<String> myList = LengbanList.getInstance().getConfig().getStringList(listname);
        myList.add(newValue);
        LengbanList.getInstance().getConfig().set(listname, myList);
        try {
            LengbanList.getInstance().getConfig().save(LengbanList.getInstance().getConfig().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeValueToList(String listname,String value) {
        List<String> myList = LengbanList.getInstance().getConfig().getStringList(listname);
        myList.remove(value);
        LengbanList.getInstance().getConfig().set(listname, myList);
        try {
            LengbanList.getInstance().getConfig().save(LengbanList.getInstance().getConfig().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
