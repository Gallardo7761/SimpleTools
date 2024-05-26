package dev.gallardo.simpletools.recipes;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.commands.CommandManager;
import dev.gallardo.simpletools.utils.Utils;

public class ZombificationPotionRecipe {
	private static ItemStack crear() {
		ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        
        ItemMeta meta = potion.getItemMeta();
        meta.setDisplayName(Utils.colorCodeParser(SimpleTools.plugin.getConfig().getString("language.zombificationPotionName")));
        meta.setLore(Collections.singletonList(Utils.colorCodeParser(SimpleTools.plugin.getConfig().getString("language.zombificationPotionLore"))));
        meta.addEnchant(Enchantment.MENDING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        
        potion.setItemMeta(meta);
        
        NBTItem nbtItem = new NBTItem(potion);
        nbtItem.setString("specialItem", "ZOMBIFICATION_POTION");

        CommandManager.recipes.add(nbtItem.getItem());
        return nbtItem.getItem();
    }
    
    public static ShapedRecipe get() {
    	ItemStack potion = crear();
    	
        NamespacedKey zombificationKey = new NamespacedKey(SimpleTools.plugin, "zombification_potion");
        ShapedRecipe recipe = new ShapedRecipe(zombificationKey, potion);
        recipe.shape(
        		" Z ", 
        		" P ", 
        		"   ");
        recipe.setIngredient('Z', Material.ZOMBIE_HEAD);
        recipe.setIngredient('P', Material.SPLASH_POTION);
        return recipe;
    }
}
