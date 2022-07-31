package com.itndev.itemshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Utils {
    public static void sendmsg(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l[ &fFax &3&l] &r" + message));
    }
    public static void sendmsg2(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static ItemMeta AddLore(ItemMeta item, String[] lores) {
        for(int c = 0; c < lores.length; c++) {
            List<String> lore = new ArrayList<>();
            if(item.hasLore()) {
                lore = item.getLore();
            }
            lore.add(colorize(lores[c]));
            item.setLore(lore);
        }
        return item;
    }

    public static void Additem(Player p, ItemStack item, int amount) {
        int itemamount = item.getAmount();
        if(itemamount * amount > 64) {
            int tempamount = amount;
            for(int k = 1; k <= amount; k++) {
                if(tempamount <= 0) {
                    break;
                }
                tempamount = tempamount - 1;
                p.getInventory().addItem(item);
            }
        } else {
            p.getInventory().addItem(item);
        }

    }

    public static String colorize(String v) {
        return ChatColor.translateAlternateColorCodes('&', v);
    }

    public static void sendhelp(Player p) {
        Utils.sendmsg(p, "&f========&3&l[ &f아이템상점 도움말 &3&l]&f========\n" +
                "/아이템상점 목록 &8&l:&r 등록된 모든 상점의 리스트를 보여준다\n" +
                "/아이템상점 생성 &7<이름> &8&l:&r 새 상점을 생성\n" +
                "/아이템상점 라인 &7<이름> <라인갯수> &8&l:&r 해당 상점의 라인 갯수를 설정\n" +
                "/아이템상점 설정 &7<이름> &8&l:&r 해당 상점을 설정하는 GUI를 염\n" +
                "/아이템상점 재료 &7<이름> <칸번호> <갯수> &8&l:&r 해당 상점에 있는 해당 칸번호에 재료를 추가\n" +
                "/아이템상점 가격 &7<이름> <칸번호> <가격> &8&l:&r 해당 상점에 있는 해당 칸번호에 가격을 설정\n" +
                "/아이템상점 삭제 &7<이름> &8&l:&r 해당 상점을 삭제\n" +
                "/아이템상점 판매토글 &7<이름> <칸번호> &8&l:&r 판매상점일시 구매로, 구매상점일시 판매로 변경\n" +
                "/아이템상점 재료제거 &7<이름> <칸번호> &8&l:&r 해당 상점에 있는 해당 칸번호에 있는 재료를 제거\n" +
                "/아이템상점 가격제거 &7<이름> <칸번호> &8&l:&r 해당 상점에 있는 해당 칸번호에 있는 가격을 제거\n" +
                "/아이템상점 도움말 &8&l:&r 해당 도움말을 보여줌\n");
    }

    public static CompletableFuture<Boolean> hasEnoughItem(Player p, ItemStack item, int amount) {
        CompletableFuture<Boolean> futureboolean = new CompletableFuture<>();
        new Thread( () -> {
            item.setAmount(1);
            int temp = amount;
            for(ItemStack slot : p.getInventory().getContents()) {
                if(slot != null) {
                    if (slot.getType() != Material.AIR) {
                        if(slot.isSimilar(item)) {
                            if(temp <= slot.getAmount()) {
                                futureboolean.complete(true);
                                temp = 0;
                                break;
                            } else {
                                temp = temp - slot.getAmount();
                            }
                        }
                    }
                }

            }
            if(temp > 0) {
                futureboolean.complete(false);
            }
        }).start();
        return futureboolean;
    }

    public static HashMap<ItemStack, Integer> toMap(Inventory inv) {
        HashMap<ItemStack, Integer> neededmap = new HashMap<>();
        for(ItemStack item : inv.getContents()) {
            if(item != null && item.getType() != Material.AIR) {
                int amount = item.getAmount();
                item.setAmount(1);
                if (neededmap.containsKey(item)) {
                    amount = amount + neededmap.get(item);
                }
                neededmap.put(item, amount);
            }
        }
        return neededmap;
    }

    public static void RemoveItem(Player p, ItemStack needed, int amount) {
        int tempamount = amount;
        int tempitem = 0;
        for(ItemStack item : p.getInventory().getContents()) {
            if(tempamount >= 1) {
                if(item != null) {
                    if (item.getType() != Material.AIR) {
                        if (item.isSimilar(needed)) {
                            tempitem = item.getAmount();
                            if (item.getAmount() < tempamount) {
                                item.setAmount(0);
                                tempamount = tempamount - tempitem;
                            } else {
                                item.setAmount(tempitem - tempamount);
                                tempamount = 0;
                            }
                        }
                    }
                }
            } else {
                break;
            }
        }
    }

}
