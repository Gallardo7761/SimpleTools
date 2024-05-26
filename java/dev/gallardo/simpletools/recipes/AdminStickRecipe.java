package dev.gallardo.simpletools.recipes;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.commands.CommandManager;
import dev.gallardo.simpletools.utils.Utils;

public class AdminStickRecipe {
	public static ItemStack crear() {
        ItemStack stick = new ItemStack(Material.STICK);
       
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName(Utils.colorCodeParser(SimpleTools.plugin.getConfig().getString("language.adminStickName")));
        meta.setLore(Collections.singletonList(Utils.colorCodeParser(SimpleTools.plugin.getConfig().getString("language.adminStickLore"))));
        
        stick.setItemMeta(meta);
        
        NBTItem nbtItem = new NBTItem(stick);
        nbtItem.setString("specialItem", "ADMIN_STICK");

        CommandManager.recipes.add(nbtItem.getItem());
        return nbtItem.getItem();
    }
    
    public static ShapedRecipe get() {
    	ItemStack palo = crear();
        NamespacedKey paloRecipeKey = new NamespacedKey(SimpleTools.plugin, "admin_stick");
        ShapedRecipe paloRecipe = new ShapedRecipe(paloRecipeKey, palo);
        paloRecipe.shape(
        		"DDD", 
        		"DSD", 
        		"DDD");
        paloRecipe.setIngredient('D', Material.BEDROCK);
        paloRecipe.setIngredient('S', Material.STICK);
        return paloRecipe;
    }
    
    
}

