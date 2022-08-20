package com.itndev.itemshop;

public class InventoryUtils {

    public static String getShopDisplayName(String ShopName) {
        return Storage.shoplinedisplayname.containsKey(ShopName) ? Storage.shoplinedisplayname.get(ShopName) : "[상점] " + ShopName;
    }
}
