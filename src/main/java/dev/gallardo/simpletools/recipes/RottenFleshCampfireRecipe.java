package dev.gallardo.simpletools.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.recipe.CookingBookCategory;

import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.utils.ConfigWrapper;

public class RottenFleshCampfireRecipe {
	private static ConfigWrapper config = SimpleTools.getConf();
	
    public static CampfireRecipe get() {
    	
        NamespacedKey rottenRecipeKey = new NamespacedKey(SimpleTools.plugin, "rotten_campfire");
        CampfireRecipe rottenRecipe = new CampfireRecipe(
        		rottenRecipeKey, 
        		new ItemStack(Material.BEEF), 
        		Material.ROTTEN_FLESH, 
        		0, config.getInt("config.rottenFleshCookTime") * 20);
        
        rottenRecipe.setCategory(CookingBookCategory.FOOD);
        
        return rottenRecipe;
    }

}
