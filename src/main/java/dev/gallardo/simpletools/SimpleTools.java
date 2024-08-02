package dev.gallardo.simpletools;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import org.apache.maven.artifact.versioning.ComparableVersion;

import dev.gallardo.simpletools.commands.CommandManager;
import dev.gallardo.simpletools.events.EventListener;
import dev.gallardo.simpletools.recipes.RecipeManager;
import dev.gallardo.simpletools.tasks.LocationTracker;
import dev.gallardo.simpletools.utils.ConfigWrapper;
import dev.gallardo.simpletools.common.GlobalChest;
import dev.gallardo.simpletools.utils.UpdateChecker;
import dev.gallardo.simpletools.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

public class SimpleTools extends JavaPlugin implements Listener {
    
    public static SimpleTools plugin;
    private static ConfigWrapper config = new ConfigWrapper();
    
    public static ConfigWrapper getConf() {
    	return config;
    }

	public void onEnable() {
        super.onEnable();
        plugin = this;
        config.onEnable();
        Utils.createLangs();
        CommandAPI.onEnable();
        CommandManager.registerCommands();
        EventListener.registerEvents();
        RecipeManager.registerRecipes();
        GlobalChest.loadConfig();
        GlobalChest.loadChest();
        LocationTracker.startLocationTrackingTask();
        this.getLogger().info("I've been enabled! :)");
    }
    
	@Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true)); // Load with verbose output
    }
	
    public void onDisable() {
        super.onDisable();
        GlobalChest.saveChest();
        this.getLogger().info("I've been disabled! :(");
    }
}