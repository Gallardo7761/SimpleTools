package dev.gallardo.simpletools.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import at.pcgamingfreaks.Minepacks.Bukkit.API.Backpack;
import at.pcgamingfreaks.Minepacks.Bukkit.API.MinepacksPlugin;
import dev.gallardo.simpletools.SimpleTools;

public class MinepacksAccessor {
	public static MinepacksPlugin getMinepacks() {
	    Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("Minepacks");
	    if(!(bukkitPlugin instanceof MinepacksPlugin)) {
	    	SimpleTools.plugin.getLogger().severe("Error trying to hook Minepacks, it's not available or installed!");
	        return null;
	    }
	    return (MinepacksPlugin) bukkitPlugin;
	}
	
	public static Inventory getPlayerBackpackInventory(Player player) {
	    Backpack bp = getMinepacks().getBackpackCachedOnly(player);
	    if(bp == null) return null;
	    return bp.getInventory();
	}
}
