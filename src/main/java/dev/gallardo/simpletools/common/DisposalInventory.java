package dev.gallardo.simpletools.common;

import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.utils.ConfigWrapper;
import dev.gallardo.simpletools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DisposalInventory {
    private static ConfigWrapper config = SimpleTools.getConf();
    private static Inventory inv;

    public DisposalInventory() {
    }

    public static Inventory getInv() {
        return inv;
    }

    static {
        inv = Bukkit.createInventory((InventoryHolder)null, 54, Utils.colorCodeParser(config.getString("language.disposalTitle")));
    }
}
