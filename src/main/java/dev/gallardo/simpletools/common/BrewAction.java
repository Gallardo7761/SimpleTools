package dev.gallardo.simpletools.common;

import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public interface BrewAction {
	public void brew(BrewerInventory inventory, ItemStack item, ItemStack ingredient);
}
