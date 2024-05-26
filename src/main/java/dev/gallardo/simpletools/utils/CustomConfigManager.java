package dev.gallardo.simpletools.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomConfigManager {

    private final JavaPlugin plugin;
    private final File configFile;
    private FileConfiguration config;

    public CustomConfigManager(JavaPlugin plugin, String fileName) {
    	this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), fileName);
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }
    
    public void loadConfig() {
    	try {
    		YamlConfiguration.loadConfiguration(configFile);
    	} catch(Exception e) {
    		plugin.getLogger().severe(e.getMessage());
    	}
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(configFile.getName(), false);
        }
    }

    // Métodos adicionales para obtener datos específicos de la configuración si es necesario

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    // Otros métodos según tus necesidades

}
