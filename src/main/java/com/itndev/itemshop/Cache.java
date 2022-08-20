package com.itndev.itemshop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Cache {

    private static HashMap<String, Inventory> CachedShop = new HashMap<>();

    public static void removeShop(String ShopName) {
        CachedShop.remove(ShopName);
    }
    public static Inventory getShop(String ShopName) {
        if(!Storage.shopline.containsKey(ShopName)) {
            return null;
        }
        if(!CachedShop.containsKey(ShopName)) {
            Refresh(ShopName);
        }
        Inventory originalInventory = CachedShop.get(ShopName);
        InventoryGUIHolder inventoryGUIHolder = new InventoryGUIHolder(ShopName, InventoryGUIHolder.TYPE_Shop, originalInventory.getSize(), InventoryUtils.getShopDisplayName(ShopName));
        Inventory inventory = inventoryGUIHolder.getInventory();
        inventory.setContents(originalInventory.getContents());
        return inventory;
    }

    public static void Refresh(String ShopName) {
        if(Storage.shopline.containsKey(ShopName) && !Storage.shopline.get(ShopName).equals(0)) {
            int lines = Storage.shopline.get(ShopName);
            //Inventory inv = Bukkit.createInventory(null, lines * 9, "[상점] " + ShopName);
            InventoryGUIHolder inventoryGUIHolder = new InventoryGUIHolder(ShopName, InventoryGUIHolder.TYPE_Shop, lines * 9, InventoryUtils.getShopDisplayName(ShopName));
            Inventory inv = inventoryGUIHolder.getInventory();

            for (int c = 0; c < lines * 9; c++) {
                int k = c + 1;

                if (Storage.shoplineresult.containsKey(ShopName + k)) {
                    ItemStack itemtemp = Storage.shoplineresult.get(ShopName + k)[0];
                    if (itemtemp != null && !itemtemp.getType().equals(Material.AIR)) {
                        ItemStack item = itemtemp.clone();
                        if (Storage.shoplineneeded.containsKey(ShopName + k)) {
                            Inventory tempinv = Bukkit.createInventory(null, 54, "temp");
                            tempinv.setContents(Storage.shoplineneeded.get(ShopName + k).getContents().clone());
                            LinkedHashMap<ItemStack, Integer> map = Utils.toMap(tempinv);
                            ItemStack item1 = item.clone();
                            ItemMeta itemmeta = item1.getItemMeta();
                            String lores[] = new String[3 + 1 + map.size()];

                            lores[0] = "";
                            int weeknow = 1;
                            for (ItemStack maptem : map.keySet()) {
                                String neededname;
                                if (maptem.hasItemMeta() && maptem.getItemMeta().hasDisplayName()) {
                                    neededname = maptem.getItemMeta().getDisplayName();
                                } else {
                                    neededname = maptem.getType().name();
                                }
                                lores[weeknow] = "&3&l[필요한 아이템] &r&l: &7&l[ " + neededname + "&r &7&l/ " + map.get(maptem) + "개 &7&l]";
                                weeknow++;
                            }
                            lores[map.size() + 1] = "";
                            lores[map.size() + 2] = "&3&l[좌클릭 : 아이템 구매 / 쉬프트 + 좌클릭 : 아이템 64개 구매]";
                            lores[map.size() + 3] = "&3&l[우클릭 : 필요한 아이템 목록 보기]";
                            item1.setItemMeta(Utils.AddLore(itemmeta, lores));
                            inv.setItem(c, item1.clone());
                        } else if (Storage.shoplineprice.containsKey(ShopName + k)) {
                            if (Storage.issellshop.containsKey(ShopName + k)) {
                                double price = Storage.shoplineprice.get(ShopName + k);
                                ItemStack item1 = item.clone();
                                ItemMeta itemmeta = item1.getItemMeta();
                                String lores[] = new String[4];

                                lores[0] = "";
                                lores[1] = "&3&l[판매가격] &r&l: &7&l[ " + price + "원 &7&l]";
                                lores[2] = "";
                                lores[3] = "&3&l[좌클릭 : 아이템 판매 / 쉬프트 + 좌클릭 : 아이템 64개 판매]";
                                item1.setItemMeta(Utils.AddLore(itemmeta, lores));
                                inv.setItem(c, item1.clone());
                            } else {
                                double price = Storage.shoplineprice.get(ShopName + k);
                                ItemStack item1 = item.clone();
                                ItemMeta itemmeta = item1.getItemMeta();
                                String lores[] = new String[4];

                                lores[0] = "";
                                lores[1] = "&3&l[구매가격] &r&l: &7&l[ " + price + "원 &7&l]";
                                lores[2] = "";
                                lores[3] = "&3&l[좌클릭 : 아이템 구매 / 쉬프트 + 좌클릭 : 아이템 64개 구매]";
                                item1.setItemMeta(Utils.AddLore(itemmeta, lores));
                                inv.setItem(c, item1.clone());
                            }

                        } else {
                            ItemStack item1 = item.clone();
                            inv.setItem(c, item1.clone());
                        }

                        //lore.add("");
                        //lore.add("&3&l[필요한 아이템] &r&l: &7&l[ " + needed.getItemMeta().getDisplayName() + " &7&l]");
                        //lore.add("");
                        //lore.add("&3&l[좌클릭 : 아이템 구매 / 쉬프트 + 좌클릭 : 아이템 64개 구매]");

                    }
                }
            }
            CachedShop.remove(ShopName);
            CachedShop.put(ShopName, inv);
        }
            //
    }
}
