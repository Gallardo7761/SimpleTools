package dev.gallardo.simpletools.common;

import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.utils.ConfigWrapper;
import dev.gallardo.simpletools.utils.Utils;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GlobalChest {
    private static File itemsFile;
    private static FileConfiguration items;
    private static ConfigWrapper config = SimpleTools.getConf();
    private static Inventory inv;

    public GlobalChest() {
    }

    public static File getItemsFile() {
        return itemsFile;
    }

    public static FileConfiguration getItems() {
        return items;
    }

    public static Inventory getInv() {
        return inv;
    }

    public static void loadChest() {
        ConfigurationSection inventorySection = items.getConfigurationSection("inventory");
        if (inventorySection != null && inventorySection.getList("items") != null) {
            inv.setContents((ItemStack[])inventorySection.getList("items").toArray((x$0) -> {
                return new ItemStack[x$0];
            }));
        }

    }

    public static void saveChest() {
        ConfigurationSection inventorySection = items.createSection("inventory");
        inventorySection.set("items", inv.getContents());

        try {
            items.save(itemsFile);
        } catch (IOException var2) {
            IOException e = var2;
            e.printStackTrace();
        }

    }

    public static void loadConfig() {
        itemsFile = new File(SimpleTools.plugin.getDataFolder(), "items.yml");
        items = YamlConfiguration.loadConfiguration(itemsFile);
    }

    static {
        inv = Bukkit.createInventory((InventoryHolder)null, 54, Utils.colorCodeParser(config.getString("language.globalChestTitle")));
    }
}
