package dev.gallardo.simpletools;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dev.gallardo.simpletools.commands.CommandManager;
import dev.gallardo.simpletools.events.EventListener;
import dev.gallardo.simpletools.recipes.RecipeManager;
import dev.gallardo.simpletools.tasks.LocationTracker;
import dev.gallardo.simpletools.utils.ConfigWrapper;
import dev.gallardo.simpletools.utils.GlobalChest;
import dev.gallardo.simpletools.utils.UpdateChecker;
import dev.gallardo.simpletools.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

public class SimpleTools extends JavaPlugin implements Listener {
    
    public static SimpleTools plugin;
    private final Integer ID = 108067;
    private final String SPIGOT_LINK = "https://www.spigotmc.org/resources/simpletools.108067/";
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
        new UpdateChecker(this, ID).getLatestVersion(version -> {
            ComparableVersion versionFromAPI = new ComparableVersion(version);
        	ComparableVersion pluginVersion = new ComparableVersion(plugin.getDescription().getVersion());
            
            if (pluginVersion.compareTo(versionFromAPI) == 0) {
                this.getLogger().info("I'm up to date!");
            } else if(pluginVersion.compareTo(versionFromAPI) > 0) {
            	this.getLogger().warning("Using version " + pluginVersion.toString() + " which is a DEV BUILD!");
            } else {
                this.getLogger().severe("I'm not up to date! You can download my last version from " + SPIGOT_LINK);
            }
        });
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