package com.itndev.itemshop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static Economy econ = null;

    public static Main instance;

    public static Main getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        setupEconomy();
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("아이템상점"))).setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new listener(),this);

        Storage.createlocalstorage();
        Storage.saveStorage();
        if(Storage.getStorage().contains("shopline.")) {
            Storage.onRestoreshoplineData();
        }
        if(Storage.getStorage().contains("shoplineresult.")) {
            Storage.onRestoreshoplineresultData();
        }
        if(Storage.getStorage().contains("shoplineneeded.")) {
            Storage.onRestoreshoplineneededData();
        }
        if(Storage.getStorage().contains("shoplineneededamount.")) {
            Storage.onRestoreshoplineneededamountData();
        }
        if(Storage.getStorage().contains("shoplineprice.")) {
            Storage.onRestoreshoplinepriceData();
        }




    }

    @Override
    public void onDisable() {
        try {
            Storage.resetlocalstorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!Storage.shopline.isEmpty()) {
            Storage.onSaveshoplineData();
        }
        if(!Storage.shoplineresult.isEmpty()) {
            Storage.onSaveshoplineresultData();
        }
        if(!Storage.shoplineneeded.isEmpty()) {
            Storage.onSaveshoplineneededData();
        }
        if(!Storage.shoplineneededamount.isEmpty()) {
            Storage.onSaveshoplineneededamountData();
        }
        if(!Storage.shoplineprice.isEmpty()) {
            Storage.onSaveshoplinepriceData();
        }
        Storage.saveStorage();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }
}
