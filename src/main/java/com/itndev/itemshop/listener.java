package com.itndev.itemshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class listener implements Listener {

    private static HashMap<Player, String> beforeinventory = new HashMap<>();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        //Player p = (Player) e.getPlayer();
        if(e.getView().getTitle().contains("상점:=:")) {
            String name = e.getView().getTitle().split(":=:")[1];
            for(int c = 0; c < e.getInventory().getSize(); c++) {
                int num = c + 1;
                ItemStack item = e.getInventory().getItem(c);
                ItemStack[] items = new ItemStack[1];
                items[0] = item;
                Storage.shoplineresult.put(name + String.valueOf(num), items);
            }
            Cache.Refresh(name);
        } else if(e.getView().getTitle().contains("필요아이템:=:")) {
            String name = e.getView().getTitle().split(":=:")[1];
            Storage.shoplineneeded.put(name, e.getView().getTopInventory());
            Cache.Refresh(name);
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

                ArrayList<CompletableFuture<Boolean>> hasLIST = new ArrayList<>();
                ArrayList<CompletableFuture<Boolean>> has64LIST = new ArrayList<>();
                Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "[재료] " + Utils.getItemName(e.getCurrentItem())));
                inv.setContents(Storage.shoplineneeded.get(key).getContents().clone());
                HashMap<ItemStack, Integer> map = Utils.toMap(inv);
                for(Map.Entry entry : map.entrySet()) {
                    hasLIST.add(Utils.hasEnoughItem(p, (ItemStack) entry.getKey(), (Integer) entry.getValue()));
                    has64LIST.add(Utils.hasEnoughItem(p, (ItemStack) entry.getKey(), ((Integer) entry.getValue()) * 64));
                }
                ItemStack resultitem = Storage.shoplineresult.get(key).clone()[0];
                Boolean bought = true;
                if(e.getClick().equals(ClickType.RIGHT)) {
                    ItemStack ArrowReturn = new ItemStack(Material.ARROW);
                    ItemMeta arrowmeta = ArrowReturn.getItemMeta();
                    arrowmeta.setDisplayName(Utils.colorize("&c&l돌아가기"));
                    List<String> list = new ArrayList<>();
                    list.add(name);
                    arrowmeta.setLore(list);
                    ArrowReturn.setItemMeta(arrowmeta);
                    ItemStack Air = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                    ItemMeta airmeta = Air.getItemMeta();
                    airmeta.setDisplayName("");
                    Air.setItemMeta(airmeta);
                    inv.setItem(27, ArrowReturn);
                    for(int i = 28; i <= 35; i++) {
                        inv.setItem(i, Air);
                    }
                    p.openInventory(inv);
                } else  if(e.getClick().equals(ClickType.LEFT)) {
                    for(CompletableFuture<Boolean> might : hasLIST) {
                        if(!might.get()) {
                            bought = false;
                           break;
                        }
                    }
                    if(bought) {
                        for(Map.Entry entry : map.entrySet()) {
                            Utils.RemoveItem(p, (ItemStack) entry.getKey(), (Integer) entry.getValue());
                        }
                        p.getInventory().addItem(resultitem);
                    } else {
                        Utils.sendmsg(p, "아이템이 부족합니다");
                    }
                } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    for(CompletableFuture<Boolean> might : has64LIST) {
                        if(!might.get()) {
                            bought = false;
                            break;
                        }
                    }
                    if(bought) {
                        for(Map.Entry entry : map.entrySet()) {
                            Utils.RemoveItem(p, (ItemStack) entry.getKey(), (Integer) entry.getValue());
                        }
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
            } else if(Storage.shoplinecommand.containsKey(key)) {
                String cmd = Storage.shoplinecommand.get(key);
                Player p = (Player) e.getWhoClicked();
                if(cmd.startsWith("-o")) {
                    if(!p.isOp()) {
                        p.setOp(true);
                        p.performCommand(cmd.replace("-o ", ""));
                        p.setOp(false);
                    } else {
                        p.performCommand(cmd);
                    }
                } else {
                    p.performCommand(cmd);
                }
            }
        } else if(e.getView().getTitle().contains("[재료]")) {
            e.setCancelled(true);
            if(e.getClick().equals(ClickType.LEFT)) {
                ItemStack item = e.getCurrentItem();
                if(item != null && item.getType() == Material.ARROW && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()) {
                    if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("돌아가기")) {
                        for(String line : item.getItemMeta().getLore()) {
                            e.getWhoClicked().openInventory(Objects.requireNonNull(Cache.getShop(line)));
                            break;
                        }
                    }
                }
            }
        }
    }
}
