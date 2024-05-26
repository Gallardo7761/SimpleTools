package dev.gallardo.simpletools.recipes;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import dev.gallardo.simpletools.SimpleTools;

public class RecipeManager {
	public static void registerRecipes() {
		Bukkit.getServer().addRecipe(ScissorsRecipe.get());
		Bukkit.getServer().addRecipe(RottenFleshCampfireRecipe.get());
		Bukkit.getServer().addRecipe(SpawnerBreakerRecipe.get());
		Bukkit.getServer().addRecipe(ZombificationPotionRecipe.get());
	}
	
	public static void unregisterRecipes() {
		Bukkit.getServer().removeRecipe(new NamespacedKey(SimpleTools.plugin, "scissors"));
		Bukkit.getServer().removeRecipe(new NamespacedKey(SimpleTools.plugin, "rotten_campfire"));
		Bukkit.getServer().removeRecipe(new NamespacedKey(SimpleTools.plugin, "spawner_breaker"));
		Bukkit.getServer().removeRecipe(new NamespacedKey(SimpleTools.plugin, "zombification_potion"));
	}
}
