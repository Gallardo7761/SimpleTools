package dev.gallardo.simpletools.recipes;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.commands.CommandManager;
import dev.gallardo.simpletools.utils.Utils;

public class SpawnerBreakerRecipe {
	private static ItemStack crear() {
        ItemStack spawnerBreaker = new ItemStack(Material.GOLDEN_PICKAXE);
        
        ItemMeta meta = spawnerBreaker.getItemMeta();  
        meta.setDisplayName(Utils.colorCodeParser(SimpleTools.plugin.getConfig()
        		.getString("language.spawnerBreakerName")));
        meta.setLore(Collections.singletonList(Utils.colorCodeParser(SimpleTools.plugin.getConfig()
        		.getString("language.spawnerBreakerLore"))));
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
               
        spawnerBreaker.setItemMeta(meta);
        
        NBTItem nbtItem = new NBTItem(spawnerBreaker);
        nbtItem.setString("specialItem", "SPAWNER_BREAKER");
        
        CommandManager.recipes.add(nbtItem.getItem());
        return nbtItem.getItem();
    }
    
    public static ShapedRecipe get() {
    	ItemStack spawnerBreaker = crear();
        NamespacedKey spawnerBreakerRecipeKey = new NamespacedKey(SimpleTools.plugin, "spawner_breaker");
        ShapedRecipe spawnerBreakerRecipe = new ShapedRecipe(spawnerBreakerRecipeKey, spawnerBreaker);
        spawnerBreakerRecipe.shape(
        		" N ", 
        		" P ", 
        		" D ");
        spawnerBreakerRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        spawnerBreakerRecipe.setIngredient('P', Material.GOLDEN_PICKAXE);
        spawnerBreakerRecipe.setIngredient('D', Material.DIAMOND);
        return spawnerBreakerRecipe;
    }
}
