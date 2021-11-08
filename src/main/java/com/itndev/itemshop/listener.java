package com.itndev.itemshop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class listener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        //Player p = (Player) e.getPlayer();
        if(e.getView().getTitle().contains("상점-")) {
            String name = e.getView().getTitle().split("-")[1];
            for(int c = 0; c < e.getInventory().getSize(); c++) {
                int num = c + 1;
                ItemStack item = e.getInventory().getItem(c);
                ItemStack[] items = new ItemStack[1];
                items[0] = item;
                Storage.shoplineresult.put(name + String.valueOf(num), items);
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().contains("[상점]")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            String name = e.getView().getTitle().split(" ")[1];
            if(Storage.shoplineneeded.containsKey(name + String.valueOf(e.getRawSlot() + 1))) {
                Player p = (Player) e.getWhoClicked();


                ItemStack needed = Storage.shoplineneeded.get(name + String.valueOf(e.getRawSlot() + 1))[0].clone();
                int neededamount = Storage.shoplineneededamount.get(name + String.valueOf(e.getRawSlot() + 1));
                Boolean bought = false;
                if(e.getClick().equals(ClickType.LEFT)) {
                    for(ItemStack it : p.getInventory().getContents()) {
                        if(it != null) {
                            if(it.getType() != Material.AIR) {
                                if (it.isSimilar(needed) && it.getAmount() >= neededamount) {

                                    it.setAmount(it.getAmount() - neededamount);
                                    bought = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(bought) {
                        p.getInventory().addItem(Storage.shoplineresult.get(name + String.valueOf(e.getRawSlot() + 1))[0].clone());
                    } else {
                        Utils.sendmsg(p, "아이템이 부족합니다");
                    }
                } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    int itamount = 0;
                    for(ItemStack it : p.getInventory().getContents()) {
                        if(it != null) {
                            if(it.getType() != Material.AIR) {
                                if (it.isSimilar(needed)) {
                                    itamount = itamount + it.getAmount();
                                }
                            }
                        }
                    }
                    if(itamount >= neededamount * 64) {
                        for(ItemStack it : p.getInventory().getContents()) {
                            if(it.isSimilar(needed)) {
                                if(itamount >= it.getAmount()) {
                                    itamount = itamount - it.getAmount();
                                    it.setAmount(0);
                                } else {
                                    it.setAmount(it.getAmount() - itamount);
                                    itamount = 0;
                                }
                                if(itamount == 0) {
                                    break;
                                }
                            }
                        }
                        ItemStack result = Storage.shoplineresult.get(name + String.valueOf(e.getRawSlot() + 1))[0].clone();
                        result.setAmount(result.getAmount() * 64);
                        p.getInventory().addItem(result);
                    } else {
                        Utils.sendmsg(p, "아이템이 부족합니다");
                    }
                }
            } else if(Storage.shoplineprice.containsKey(name + String.valueOf(e.getRawSlot() + 1))) {
                double price = Storage.shoplineprice.get(name + String.valueOf(e.getRawSlot() + 1));
                Player p = (Player) e.getWhoClicked();
                OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
                ItemStack result = Storage.shoplineresult.get(name + String.valueOf(e.getRawSlot() + 1))[0].clone();
                double bal = Main.getEconomy().getBalance(op);
                if(e.getClick().equals(ClickType.LEFT)) {
                    if(bal > price) {
                        Main.getEconomy().withdrawPlayer(op, price);
                        Utils.Additem(p, result, 1);
                    } else {
                        Utils.sendmsg(p, "돈이 부족합니다 &7(" + String.valueOf(price - bal + 1) + "원)");
                    }
                } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    if(bal > price*64) {
                        Main.getEconomy().withdrawPlayer(op, price*64);
                        Utils.Additem(p, result, 64);
                    } else {
                        Utils.sendmsg(p, "돈이 부족합니다 &7(" + String.valueOf(price*64 - bal + 1) + "원)");
                    }
                }
            }


        }
    }
}
