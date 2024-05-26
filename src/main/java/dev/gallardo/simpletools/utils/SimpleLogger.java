package dev.gallardo.simpletools.utils;

import dev.gallardo.simpletools.SimpleTools;

public class SimpleLogger {
	private SimpleTools main;
	
	public SimpleLogger() {
		this.main = SimpleTools.plugin;
	}
	
	public void error(String msg) {
		main.getLogger().severe(msg);
	}
	
	public void info(String msg) {
		main.getLogger().info(msg);
	}
	
	public void warning(String msg) {
		main.getLogger().warning(msg);
	}
}
