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
            if(e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            String name = e.getView().getTitle().split(" ")[1];
            if(Storage.shoplineneeded.containsKey(name + String.valueOf(e.getRawSlot() + 1))) {
                Player p = (Player) e.getWhoClicked();


                ItemStack needed = Storage.shoplineneeded.get(name + String.valueOf(e.getRawSlot() + 1))[0].clone();
                int neededamount = Storage.shoplineneededamount.get(name + String.valueOf(e.getRawSlot() + 1));
                CompletableFuture<Boolean> has = Utils.hasEnoughItem(p, needed, neededamount);
                CompletableFuture<Boolean> has64 = Utils.hasEnoughItem(p, needed, neededamount * 64);
                Boolean bought = false;
                if(e.getClick().equals(ClickType.LEFT)) {
                    if(has.get()) {
                        Utils.RemoveItem(p, needed, neededamount);
                        bought = true;
                    }
                    if(bought) {
                        p.getInventory().addItem(needed);
                    } else {
                        Utils.sendmsg(p, "아이템이 부족합니다");
                    }
                } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    if(has64.get()) {
                        Utils.RemoveItem(p, needed, neededamount * 64);
                        bought = true;
                    }
                    if(bought) {
                        ItemStack result = needed;
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
