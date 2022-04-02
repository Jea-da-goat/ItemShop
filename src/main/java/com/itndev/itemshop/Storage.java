package com.itndev.itemshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Storage {

    public static HashMap<String, Integer> shopline = new HashMap<>();

    public static HashMap<String, ItemStack[]> shoplineresult = new HashMap<>();

    public static HashMap<String, ItemStack[]> shoplineneeded = new HashMap<>();

    public static HashMap<String, Integer> shoplineneededamount = new HashMap<>();

    public static HashMap<String, Double> shoplineprice = new HashMap<>();

    public static HashMap<String, Boolean> issellshop = new HashMap<>();

    public static File file = new File(Main.getInstance().getDataFolder(), "LocalStorage.yml");

    public static FileConfiguration customlocalstorage;

    public static void createlocalstorage() {
        //file = new File(main.getInstance().getDataFolder(), "LocalStorage.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


        }
        customlocalstorage = YamlConfiguration.loadConfiguration(file);
    }
    public static void resetlocalstorage() throws IOException {
        //file = new File(main.getInstance().getDataFolder(), "LocalStorage.yml");
        file.delete();
        file.createNewFile();
        customlocalstorage.getKeys(false).forEach(key ->{
            customlocalstorage.set(key, null);
        });
        saveStorage();


    }


    public static void saveStorage() {

        try {
            customlocalstorage.save(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static FileConfiguration getStorage() {
        return customlocalstorage;
    }
    public static void reloadStorage() {
        customlocalstorage = YamlConfiguration.loadConfiguration(file);
    }








    public static void onSaveissellshopData() {
        for (Map.Entry<String, Boolean> entry : Storage.issellshop.entrySet())
            Storage.getStorage().set("issellshop." + (String)entry.getKey(), entry.getValue());
        //saveStorage();
    }
    public static void onRestoreissellshopData() {
        Storage.getStorage().getConfigurationSection("issellshop.").getKeys(false).forEach(key -> {
            String v = Storage.getStorage().get("issellshop." + key).toString();
            Storage.issellshop.put(key, Boolean.valueOf(v));
        });
    }
    public static void onSaveshoplineData() {
        for (Map.Entry<String, Integer> entry : Storage.shopline.entrySet())
            Storage.getStorage().set("shopline." + (String)entry.getKey(), entry.getValue());
        //saveStorage();
    }
    public static void onRestoreshoplineData() {
        Storage.getStorage().getConfigurationSection("shopline.").getKeys(false).forEach(key -> {
            String v = Storage.getStorage().get("shopline." + key).toString();
            Storage.shopline.put(key, Integer.valueOf(v));
        });
    }
    public static void onSaveshoplineresultData() {
        for (Map.Entry<String, ItemStack[]> entry : Storage.shoplineresult.entrySet())
            Storage.getStorage().set("shoplineresult." + entry.getKey(), entry.getValue());

        //saveStorage();
    }
    public static void onRestoreshoplineresultData() {
        Storage.getStorage().getConfigurationSection("shoplineresult.").getKeys(false).forEach(key -> {
            ItemStack[] v = ((List<ItemStack>) Storage.getStorage().get("shoplineresult." + key)).toArray(new ItemStack[1]);
            Storage.shoplineresult.put(key, v);
        });
    }
    public static void onSaveshoplineneededData() {
        for (Map.Entry<String, ItemStack[]> entry : Storage.shoplineneeded.entrySet())
            Storage.getStorage().set("shoplineneeded." + (String)entry.getKey(), entry.getValue());


        //saveStorage();
    }
    public static void onRestoreshoplineneededData() {
        Storage.getStorage().getConfigurationSection("shoplineneeded.").getKeys(false).forEach(key -> {
            ItemStack[] v = ((List<ItemStack>) Storage.getStorage().get("shoplineneeded." + key)).toArray(new ItemStack[1]);
            Storage.shoplineneeded.put(key, v);
        });
    }
    public static void onSaveshoplineneededamountData() {
        for (Map.Entry<String, Integer> entry : Storage.shoplineneededamount.entrySet())
            Storage.getStorage().set("shoplineneededamount." + (String)entry.getKey(), entry.getValue());
        //saveStorage();
    }
    public static void onRestoreshoplineneededamountData() {
        Storage.getStorage().getConfigurationSection("shoplineneededamount.").getKeys(false).forEach(key -> {
            String v = Storage.getStorage().get("shoplineneededamount." + key).toString();
            Storage.shoplineneededamount.put(key, Integer.valueOf(v));
        });
    }
    public static void onSaveshoplinepriceData() {
        for (Map.Entry<String, Double> entry : Storage.shoplineprice.entrySet())
            Storage.getStorage().set("shoplineprice." + (String)entry.getKey(), entry.getValue());
        //saveStorage();
    }
    public static void onRestoreshoplinepriceData() {
        Storage.getStorage().getConfigurationSection("shoplineprice.").getKeys(false).forEach(key -> {
            String v = Storage.getStorage().get("shoplineprice." + key).toString();
            Storage.shoplineprice.put(key, Double.valueOf(v));
        });
    }


}
