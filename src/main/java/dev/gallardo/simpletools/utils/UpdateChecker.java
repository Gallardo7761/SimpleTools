package dev.gallardo.simpletools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import dev.gallardo.simpletools.SimpleTools;

public class UpdateChecker {
    private SimpleTools plugin;
    private int resourceId;

    public UpdateChecker(SimpleTools plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

	public void getLatestVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream);){
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            }
            catch (IOException exception) {
                this.plugin.getLogger().info("There was an error finding an update!" + exception.getMessage());
            }
        });
    }
}
