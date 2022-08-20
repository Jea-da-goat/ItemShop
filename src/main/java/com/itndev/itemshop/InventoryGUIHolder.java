package com.itndev.itemshop;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class InventoryGUIHolder implements InventoryHolder {


    public static String TYPE_Shop = "상점";
    public static String TYPE_Needed = "재료";
    private Inventory inventory;
    private String shopname;

    private String shoptype;

    public InventoryGUIHolder(String name, String type, int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.shopname = name;
        this.shoptype = type;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public String getShopname() {
        return shopname;
    }

    public String getShopType() {
        return shoptype;
    }
}
