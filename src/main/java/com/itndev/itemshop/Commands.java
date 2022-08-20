package com.itndev.itemshop;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Commands implements CommandExecutor {

    public static String publicsplitter = "-";

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
                        /*int lines = Storage.shopline.get(args[1]);
                        Inventory inv = Bukkit.createInventory(null, lines * 9, "[상점] " + args[1]);

                        for(int c = 0; c < lines * 9; c++) {
                            int k = c + 1;

                            if(Storage.shoplineresult.containsKey(args[1] + k)) {
                                ItemStack itemtemp = Storage.shoplineresult.get(args[1] + k)[0];
                                if (itemtemp != null && !itemtemp.getType().equals(Material.AIR)) {
                                    ItemStack item = itemtemp.clone();
                                    if (Storage.shoplineneeded.containsKey(args[1] + k)) {
                                        Inventory tempinv = Bukkit.createInventory(null, 54, "temp");
                                        tempinv.setContents(Storage.shoplineneeded.get(args[1] + k).getContents().clone());
                                        LinkedHashMap<ItemStack, Integer> map = Utils.toMap(tempinv);
                                        ItemStack item1 = item.clone();
                                        ItemMeta itemmeta = item1.getItemMeta();
                                        String lores[] = new String[3 + map.size()];

                                        lores[0] = "";
                                        int weeknow = 1;
                                        for(ItemStack maptem : map.keySet()) {
                                            String neededname;
                                            if(maptem.hasItemMeta() && maptem.getItemMeta().hasDisplayName()) {
                                                neededname = maptem.getItemMeta().getDisplayName();
                                            } else {
                                                neededname = maptem.getType().name();
                                            }
                                            lores[weeknow] = "&3&l[필요한 아이템] &r&l: &7&l[ " + neededname + "&r &7&l/ " + map.get(maptem) + "개 &7&l]";
                                            weeknow++;
                                        }
                                        lores[map.size() + 1] = "";
                                        lores[map.size() + 2] = "&3&l[좌클릭 : 아이템 구매 / 쉬프트 + 좌클릭 : 아이템 64개 구매]";
                                        item1.setItemMeta(Utils.AddLore(itemmeta, lores));
                                        inv.setItem(c, item1.clone());
                                    } else if (Storage.shoplineprice.containsKey(args[1] + k)) {
                                        if (Storage.issellshop.containsKey(args[1] + k)) {
                                            double price = Storage.shoplineprice.get(args[1] + k);
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
                                            double price = Storage.shoplineprice.get(args[1] + k);
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

                        p.openInventory(inv);*/
                        p.openInventory(Objects.requireNonNull(Cache.getShop(args[1])));
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
                        Cache.Refresh(args[1]);
                    } else {
                        Utils.sendmsg(p, "존재하지 않는 상점입니다");
                    }

                } else if(args[0].equalsIgnoreCase("재료")) {
                    if(args.length < 3) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shoplineprice.containsKey(args[1] + args[2])) {
                        if (Storage.shopline.containsKey(args[1])) {
                            int lines = Storage.shopline.get(args[1]);
                            if (lines > 0) {
                                if (Integer.valueOf(args[2]) <= lines * 9) {
                                    Inventory inv = Bukkit.createInventory(null, 27, "필요아이템:=:" + args[1] + args[2]);
                                    if(Storage.shoplineneeded.containsKey(args[1] + args[2])) {
                                        inv.setContents(Storage.shoplineneeded.get(args[1] + args[2]).getContents());
                                    }
                                    p.openInventory(inv);
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
                        Inventory inv = Bukkit.createInventory(null, lines * 9, "상점:=:" + args[1]);

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
                        Cache.Refresh(args[1]);
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
                        Cache.Refresh(args[1]);
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
                        Cache.removeShop(args[1]);
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
                        Cache.Refresh(args[1]);
                    } else {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                    }
                } else if(args[0].equalsIgnoreCase("판매토글")) {
                    if (args.length < 3) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    String shopname = args[1];
                    String linenumb = args[2];
                    String key = shopname + linenumb;
                    if (!Storage.shopline.containsKey(shopname)) {
                        Utils.sendmsg(p, "해당 상점 " + shopname + " 은 존재하지 않습니다");
                        return false;
                    }
                    if (Storage.shoplineprice.containsKey(key)) {
                        if (Storage.issellshop.containsKey(key)) {
                            Storage.issellshop.remove(key);
                            Utils.sendmsg(p, "해당 상점 " + shopname + "의 " + linenumb + "번을 구매상점으로 설정했습니다");
                        } else {
                            Storage.issellshop.put(key, true);
                            Utils.sendmsg(p, "해당 상점 " + shopname + "의 " + linenumb + "번을 판매상점으로 설정했습니다");
                        }
                    } else {
                        Utils.sendmsg(p, "해당 상점 " + shopname + "의 " + linenumb + "번에는 가격이 설정되어 있지 않습니다");
                    }
                    Cache.Refresh(args[1]);
                } else if(args[0].equalsIgnoreCase("명령어추가")) {
                    if (args.length < 4) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shopline.containsKey(args[1])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                        return false;
                    }
                    if(Storage.shoplinecommand.containsKey(args[1] + publicsplitter + args[2])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 의 " + args[2]  + " 에는 이미 다른 명령어가 존재합니다 명령어 삭제 후 다시 추가 바랍니다");
                        return false;
                    }
                    String cmd = Utils.Args2String(args, 3);
                    Storage.shoplinecommand.put(args[1] + publicsplitter + args[2], cmd);
                    Utils.sendmsg(p, "해당 상점 " + args[1] + " 의 " + args[2]  + " 에 명령어 " + cmd + " 을 성공적으로 추가했습니다");
                } else if(args[0].equalsIgnoreCase("명령어제거")) {
                    if (args.length < 3) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shopline.containsKey(args[1])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                        return false;
                    }
                    if(!Storage.shoplinecommand.containsKey(args[1] + publicsplitter + args[2])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 의 " + args[2]  + " 에는 이미 다른 명령어가 존재하지 않습니다");
                        return false;
                    }
                    String cmd = Storage.shoplinecommand.get(args[1] + publicsplitter + args[2]);
                    Storage.shoplinecommand.remove(args[1] + publicsplitter + args[2]);
                    Utils.sendmsg(p, "해당 상점 " + args[1] + " 의 " + args[2]  + " 에 있는 명령어 " + cmd + " 을 삭제했습니다");
                } else if(args[0].equalsIgnoreCase("상점이름추가")) {
                    if (args.length < 3) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shopline.containsKey(args[1])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                        return false;
                    }
                    if(Storage.shoplinedisplayname.containsKey(args[1])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 에는 이미 상점 디스플레이 이름이 존재합니다 디스플레이 이름 삭제 후 다시 추가 바랍니다");
                        return false;
                    }
                    String name = Utils.Args2String(args, 2);
                    Storage.shoplinedisplayname.put(args[1], name);
                    Utils.sendmsg(p, "해당 상점 " + args[1] + " 에 상점 디스플레이 이름 [" + name + "&r] 을 성공적으로 추가했습니다");
                } else if(args[0].equalsIgnoreCase("상점이름제거")) {
                    if (args.length < 2) {
                        Utils.sendhelp(p);
                        return false;
                    }
                    if(!Storage.shopline.containsKey(args[1])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 은 존재하지 않습니다");
                        return false;
                    }
                    if(!Storage.shoplinedisplayname.containsKey(args[1])) {
                        Utils.sendmsg(p, "해당 상점 " + args[1] + " 에는 상점 디스플레이 이름이 존재하지않습니다");
                        return false;
                    }
                    String name = Storage.shoplinedisplayname.get(args[1]);
                    Storage.shoplinedisplayname.remove(args[1]);
                    Utils.sendmsg(p, "해당 상점 " + args[1] + " 에 있는 상점이름 [" + name + "&r] 을 삭제했습니다");
                } else if(args[0].equalsIgnoreCase("도움말")) {
                    Utils.sendhelp(p);
                } else {
                    Utils.sendhelp(p);
                }

            }
        }
        return false;
    }
}
