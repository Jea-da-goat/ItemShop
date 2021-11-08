package com.itndev.itemshop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission("itemshop.use.admin")) {
                Utils.sendmsg(p, "권한이 없습니다");
                return false;
            }
            if(label.equalsIgnoreCase("아이템상점")) {
                if(args.length < 1) {
                    Utils.sendhelp(p);
                    return false;
                } else if(args[0].equalsIgnoreCase("목록")) {
                    if(!Storage.shopline.isEmpty()) {
                        Utils.sendmsg2(p, "===============[ 상점 목록 ]================");
                        for(String k : Storage.shopline.keySet()) {
                            Utils.sendmsg2(p, "상점 : " + k);
                        }
                    }
                } else if(args[0].equalsIgnoreCase("생성")) {
                    if(args.length < 2) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shopline.containsKey(args[1])) {
                        Storage.shopline.put(args[1], 0);
                        Utils.sendmsg(p, "새 상점 " + args[1] + "(을)를 생성");
                    } else {
                        Utils.sendmsg(p, "이미 존재하는 상점입니다");
                    }
                } else if(args[0].equalsIgnoreCase("열기")) {
                    if(args.length < 2) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(Storage.shopline.containsKey(args[1]) && !Storage.shopline.get(args[1]).equals(0)) {
                        int lines = Storage.shopline.get(args[1]);
                        Inventory inv = Bukkit.createInventory(null, lines * 9, "[상점] " + args[1]);

                        for(int c = 0; c < lines * 9; c++) {
                            int k = c + 1;

                            if(Storage.shoplineresult.containsKey(args[1] + k)) {
                                ItemStack itemtemp = Storage.shoplineresult.get(args[1] + k)[0];
                                if (itemtemp != null && !itemtemp.getType().equals(Material.AIR) && itemtemp.hasItemMeta()) {
                                    ItemStack item = itemtemp.clone();
                                    ItemStack needed;
                                    if(Storage.shoplineneeded.containsKey(args[1] + k)) {
                                        needed = Storage.shoplineneeded.get(args[1] + k)[0].clone();
                                        String neededname;
                                        if (needed != null && needed.hasItemMeta()) {
                                            neededname = needed.getItemMeta().getDisplayName();
                                        } else {
                                            neededname = needed.getType().name();
                                        }
                                        ItemStack item1 = item.clone();
                                        ItemMeta itemmeta = item1.getItemMeta();
                                        String lores[] = new String[4];

                                        lores[0] = "";
                                        lores[1] = "&3&l[필요한 아이템] &r&l: &7&l[ " + neededname + " &7&l/ " + Storage.shoplineneededamount.get(args[1] + k) + "개 &7&l]";
                                        lores[2] = "";
                                        lores[3] = "&3&l[좌클릭 : 아이템 구매 / 쉬프트 + 좌클릭 : 아이템 64개 구매]";
                                        item1.setItemMeta(Utils.AddLore(itemmeta, lores));
                                        inv.setItem(c, item1.clone());
                                    } else if(Storage.shoplineprice.containsKey(args[1] + k)){
                                        double price = Storage.shoplineprice.get(args[1] + k);
                                        ItemStack item1 = item.clone();
                                        ItemMeta itemmeta = item1.getItemMeta();
                                        String lores[] = new String[4];

                                        lores[0] = "";
                                        lores[1] = "&3&l[가격] &r&l: &7&l[ " + price + "원 &7&l]";
                                        lores[2] = "";
                                        lores[3] = "&3&l[좌클릭 : 아이템 구매 / 쉬프트 + 좌클릭 : 아이템 64개 구매]";
                                        item1.setItemMeta(Utils.AddLore(itemmeta, lores));
                                        inv.setItem(c, item1.clone());
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

                        p.openInventory(inv);
                    } else {
                        Utils.sendmsg(p, "해당 상점은 존재하지 않거나 설정이 되있지 않습니다");
                    }
                } else if(args[0].equalsIgnoreCase("라인")) {
                    if(args.length < 3) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(Storage.shopline.containsKey(args[1])) {
                        Storage.shopline.put(args[1], Integer.valueOf(args[2]));
                        Utils.sendmsg(p, "상점 " + args[1] + "의 라인 갯수를 " + args[2] + "로 설정했습니다");
                    } else {
                        Utils.sendmsg(p, "존재하지 않는 상점입니다");
                    }

                } else if(args[0].equalsIgnoreCase("재료")) {
                    if(args.length < 4) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shoplineprice.containsKey(args[1] + args[2])) {
                        if (Storage.shopline.containsKey(args[1])) {
                            int lines = Storage.shopline.get(args[1]);
                            if (lines > 0) {
                                if (Integer.valueOf(args[2]) <= lines * 9) {
                                    ItemStack[] items = new ItemStack[1];
                                    items[0] = p.getInventory().getItemInMainHand().clone();
                                    Storage.shoplineneeded.put(args[1] + args[2], items);
                                    Storage.shoplineneededamount.put(args[1] + args[2], Integer.valueOf(args[3]));
                                    Utils.sendmsg(p, "재료를 " + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() + " " + args[3] + "개로 설정했습니다");
                                } else {
                                    Utils.sendmsg(p, "해당 슬롯은 상점에 존재하지 않습니다.");
                                }
                            } else {
                                Utils.sendmsg(p, "해당 상점은 설정이 되있지 않습니다");
                            }
                        } else {
                            Utils.sendmsg(p, "존재하지 않는 상점입니다");
                        }
                    } else {
                        Utils.sendmsg(p, "이미 해당 슬롯 " + args[2] + " 은 가격이 설정되어 있습니다");
                    }

                } else if(args[0].equalsIgnoreCase("설정")) {
                    if(args.length < 2) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(Storage.shopline.containsKey(args[1]) && !Storage.shopline.get(args[1]).equals(0)) {
                        int lines = Storage.shopline.get(args[1]);
                        Inventory inv = Bukkit.createInventory(null, lines * 9, "상점-" + args[1]);

                        for(int c = 0; c < lines * 9; c++) {
                            int k = c + 1;
                            if(Storage.shoplineresult.containsKey(args[1] + k)) {
                                ItemStack[] items = Storage.shoplineresult.get(args[1] + k);
                                inv.setItem(c, items[0]);
                            }
                        }

                        p.openInventory(inv);
                    } else {
                        Utils.sendmsg(p, "해당 상점은 존재하지 않거나 설정이 되있지 않습니다");
                    }
                } else if(args[0].equalsIgnoreCase("가격")) {
                    if (Storage.shopline.containsKey(args[1])) {
                        if(!Storage.shoplineneeded.containsKey(args[1] + args[2])) {
                            double price = Double.valueOf(args[3]);
                            Storage.shoplineprice.put(args[1] + args[2], price);
                            Utils.sendmsg(p, "가격을 " + price + " 원으로 설정했습니다");
                        } else {
                            Utils.sendmsg(p, "이미 해당 슬롯 " + args[2] + " 은 재료가 추가되어 있습니다");
                        }
                    } else {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                    }

                    //아이템상점 가격 <상점이름> <슬롯> <금액>

                } else if(args[0].equalsIgnoreCase("가격제거")) {

                    if(Storage.shopline.containsKey(args[1])) {
                        if(Storage.shoplineprice.containsKey(args[1] + args[2])) {
                            Storage.shoplineprice.remove(args[1] + args[2]);
                            Utils.sendmsg(p, "해당 칸 " + args[2] + "번에 있는 가격을 제거했습니다");
                        } else {
                            Utils.sendmsg(p, "해당 슬롯 " + args[2] + " 번에는 가격이 존재하지 않습니다");
                        }
                    } else {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                    }
                    //아이템상점 가격 <상점이름> <슬롯> <금액>

                } else if(args[0].equalsIgnoreCase("삭제")) {
                    if(args.length < 2) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(Storage.shopline.containsKey(args[1])) {
                        for(int c = 1; c <= Storage.shopline.get(args[1]) * 9; c++) {
                            String key = args[1] + "" + c;
                            Storage.shoplineresult.remove(key);
                            Storage.shoplineneeded.remove(key);
                            Storage.shoplineneededamount.remove(key);
                        }
                        Storage.shopline.remove(args[1]);
                        Utils.sendmsg(p, "성공적으로 상점 " + args[1] + " 을 삭제했습니다");
                    } else {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                    }
                } else if(args[0].equalsIgnoreCase("재료제거")) {
                    if(args.length < 3) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(Storage.shopline.containsKey(args[1])) {
                        String key = args[1] + args[2];
                        if(Storage.shoplineneeded.containsKey(key)) {
                            Storage.shoplineneeded.remove(key);
                            Storage.shoplineneededamount.remove(key);
                            Utils.sendmsg(p, "해당 칸 " + args[2] + "번에 있는 재료를 제거했습니다");
                        } else {
                            Utils.sendmsg(p, "존재하지 않는 재료입니다");
                        }
                    } else {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                    }
                } else if(args[1].equalsIgnoreCase("도움말")) {
                    Utils.sendhelp(p);
                } else {
                    Utils.sendhelp(p);
                }

            }
        }
        return false;
    }
}
