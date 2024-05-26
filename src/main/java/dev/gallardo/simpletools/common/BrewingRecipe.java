package dev.gallardo.simpletools.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gallardo.simpletools.SimpleTools;

public class BrewingRecipe {
	private static List<BrewingRecipe> recipes = new ArrayList<>();
	private ItemStack ingredient;
	private BrewAction action;
	private boolean perfect;
	
	public BrewingRecipe(ItemStack ingredient, BrewAction action, boolean perfect) {
		this.ingredient = ingredient;
		this.action = action;
		this.perfect = perfect;
		recipes.add(this);
	}
	
	public BrewingRecipe(Material ingredient, BrewAction action) {
		this(new ItemStack(ingredient), action, false);
	}
	
	public static List<BrewingRecipe> getRecipes() {
		return recipes;
	}

	public ItemStack getIngredient() {
		return ingredient;
	}

	public BrewAction getAction() {
		return action;
	}

	public boolean isPerfect() {
		return perfect;
	}

	@Nullable
	public static BrewingRecipe getRecipe(BrewerInventory inventory) {
		boolean notAllAir = Arrays.stream(inventory.getContents())
				.allMatch(i -> i == null || i.getType() == Material.AIR);
		if(!notAllAir)
			return null;
		for(BrewingRecipe recipe : recipes) {
			if(!recipe.isPerfect() && inventory.getIngredient().getType() == recipe.getIngredient().getType())
				return recipe;
			if(recipe.isPerfect() && inventory.getIngredient().isSimilar(recipe.getIngredient()))
				return recipe;
		}
		return null;
	}
	
	public void startBrewing(BrewerInventory inventory) {
		new BrewClock(this,inventory);
	}
	
	private class BrewClock extends BukkitRunnable {
        private BrewerInventory inventory;
        private BrewingRecipe recipe;
        private ItemStack ingredient;
        private BrewingStand stand;
        private int time = 400;
     
        public BrewClock(BrewingRecipe recipe , BrewerInventory inventory) {
            this.recipe = recipe;
            this.inventory = inventory;
            this.ingredient = inventory.getIngredient();
            this.stand = inventory.getHolder();
            runTaskTimer(SimpleTools.plugin, 0L, 1L);
        }
     
        @Override
        public void run() {
            if(time == 0) {
                inventory.setIngredient(new ItemStack(Material.AIR));
                for(int i = 0; i < 3 ; i ++) {
                    if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
                        continue;
                    recipe.getAction().brew(inventory, inventory.getItem(i), ingredient);
                }
                cancel();
                return;
            }
            
            if(inventory.getIngredient().isSimilar(ingredient)) {
            	stand.update();
                stand.setBrewingTime(400);
                cancel();
                return;
            }

            time--;
            stand.update();
            stand.setBrewingTime(time);
        }
    }
}
