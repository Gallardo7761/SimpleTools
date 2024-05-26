package dev.gallardo.simpletools.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.gallardo.simpletools.SimpleTools;

public class ConfigWrapper {
	private YamlDocument config;
	
	public ConfigWrapper() {
	}
	
	public YamlDocument getConfig() {
		return config;
	}
	
	public void onEnable() {
		try {
            this.config = YamlDocument.create(new File(SimpleTools.plugin.getDataFolder(), "config.yml"), SimpleTools.plugin.getResource("config.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void reload() {
		try {
			config.reload();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			config.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getString(String route) {
		return config.getString(route);
	}
	
	public Boolean getBoolean(String route) {
		return config.getBoolean(route);
	}
	
	public Integer getInt(String route) {
		return config.getInt(route);
	}
	
	public List<String> getList(String route){
		return config.getStringList(route);
	}
}
