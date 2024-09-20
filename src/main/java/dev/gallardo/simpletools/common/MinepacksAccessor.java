package dev.gallardo.simpletools.common;

import at.pcgamingfreaks.Minepacks.Bukkit.API.Backpack;
import at.pcgamingfreaks.Minepacks.Bukkit.API.MinepacksPlugin;
import dev.gallardo.simpletools.SimpleTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class MinepacksAccessor {
    public MinepacksAccessor() {
    }

    public static MinepacksPlugin getMinepacks() {
        Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("Minepacks");
        if (!isLoaded()) {
            SimpleTools.plugin.getLogger().severe("Error trying to hook Minepacks, it's not available or installed!");
            return null;
        } else {
            return (MinepacksPlugin)bukkitPlugin;
        }
    }

    public static Inventory getPlayerBackpackInventory(Player player) {
        Backpack bp = getMinepacks().getBackpackCachedOnly(player);
        return bp == null ? null : bp.getInventory();
    }

    public static boolean isLoaded() {
        boolean res = false;
        Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("Minepacks");
        if (bukkitPlugin instanceof MinepacksPlugin) {
            res = true;
        }
        return res;
    }
}