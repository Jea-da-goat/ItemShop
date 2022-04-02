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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public void onInventoryClick(InventoryClickEvent e) throws ExecutionException, InterruptedException {
        if(e.getView().getTitle().contains("[상점]")) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null ||e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            String name = e.getView().getTitle().split(" ")[1];
            String key = name + String.valueOf(e.getRawSlot() + 1);
            if(Storage.shoplineneeded.containsKey(key)) {
                Player p = (Player) e.getWhoClicked();


                ItemStack needed = Storage.shoplineneeded.get(key).clone()[0];
                int neededamount = Storage.shoplineneededamount.get(key);
                ItemStack resultitem = Storage.shoplineresult.get(key).clone()[0];
                CompletableFuture<Boolean> has = Utils.hasEnoughItem(p, needed, neededamount);
                CompletableFuture<Boolean> has64 = Utils.hasEnoughItem(p, needed, neededamount * 64);
                Boolean bought = false;
                if(e.getClick().equals(ClickType.LEFT)) {
                    if(has.get()) {
                        Utils.RemoveItem(p, needed, neededamount);
                        bought = true;
                    }
                    if(bought) {
                        p.getInventory().addItem(resultitem);
                    } else {
                        Utils.sendmsg(p, "아이템이 부족합니다");
                    }
                } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    if(has64.get()) {
                        Utils.RemoveItem(p, needed, neededamount * 64);
                        bought = true;
                    }
                    if(bought) {
                        resultitem.setAmount(resultitem.getAmount() * 64);
                        p.getInventory().addItem(resultitem);
                    } else {
                        Utils.sendmsg(p, "아이템이 부족합니다");
                    }
                }
            } else if(Storage.shoplineprice.containsKey(key)) {
                Player p = (Player) e.getWhoClicked();
                OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
                double price = Storage.shoplineprice.get(key);
                double bal = Main.getEconomy().getBalance(op);
                ItemStack result = Storage.shoplineresult.get(key).clone()[0];
                CompletableFuture<Boolean> has = Utils.hasEnoughItem(p, result, 1);
                CompletableFuture<Boolean> has64 = Utils.hasEnoughItem(p, result, 64);
                if(Storage.issellshop.containsKey(key)) {
                    //ItemStack result = Storage.shoplineresult.get(key).clone()[0];
                    if(e.getClick().equals(ClickType.LEFT)) {
                        if(has.get()) {
                            Main.getEconomy().depositPlayer(op, price);
                            Utils.RemoveItem(p, result, 1);//Utils.Additem(p, result, 1);
                        } else {
                            Utils.sendmsg(p, "아이템이 부족합니다");
                        }
                    } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                        if(has64.get()) {
                            Main.getEconomy().depositPlayer(op, price*64);
                            Utils.RemoveItem(p, result, 64);
                        } else {
                            Utils.sendmsg(p, "아이템이 부족합니다");
                        }
                    }
                } else {
                    //double price = Storage.shoplineprice.get(key);
                    //Player p = (Player) e.getWhoClicked();
                    //OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());

                    //double bal = Main.getEconomy().getBalance(op);
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
}
