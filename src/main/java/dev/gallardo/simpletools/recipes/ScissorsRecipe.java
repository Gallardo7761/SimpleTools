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

public class ScissorsRecipe {
	private static ItemStack crear() {
        ItemStack scissors = new ItemStack(Material.SHEARS);

        ItemMeta meta = scissors.getItemMeta();
        meta.setDisplayName(Utils.colorCodeParser(SimpleTools.plugin.getConfig().getString("language.scissorsName")));
        meta.setLore(Collections.singletonList(Utils.colorCodeParser(SimpleTools.plugin.getConfig().getString("language.scissorsLore"))));
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                
        scissors.setItemMeta(meta);
        
        NBTItem nbtItem = new NBTItem(scissors);
        nbtItem.setString("specialItem", "SCISSORS");

        CommandManager.recipes.add(nbtItem.getItem());
        return nbtItem.getItem();
    }
    
    public static ShapedRecipe get() {
    	ItemStack scissors = crear();
        NamespacedKey scissorsRecipeKey = new NamespacedKey(SimpleTools.plugin, "scissors");
        ShapedRecipe scissorsRecipe = new ShapedRecipe(scissorsRecipeKey, scissors);
        scissorsRecipe.shape(
        		" D ", 
        		"DSD", 
        		" D ");
        scissorsRecipe.setIngredient('D', Material.DIAMOND);
        scissorsRecipe.setIngredient('S', Material.SHEARS);
        return scissorsRecipe;
    }
}
