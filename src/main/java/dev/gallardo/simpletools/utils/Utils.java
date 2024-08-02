package dev.gallardo.simpletools.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.gallardo.simpletools.common.MinepacksAccessor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.gallardo.simpletools.SimpleTools;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	private static ConfigWrapper config = SimpleTools.getConf();
	
	public static String placeholderParser(String message, List<String> placeholders, List<String> values) {
        int i = 0;
    	for(String p:placeholders) {
    		if(message.contains(p)) {
        	    message = message.replace(p, values.get(i));
        	    i++;
        	}
    	}
    	return message;
    }
	
	public static String colorCodeParser(String message) {
		message = MojangHEXParser(message).replace('&', '§');
		return message;
	}
	
	public static String MojangHEXParser(String input) {
       String hex = "&#[0-9A-Fa-f]{6}";
       Pattern pattern = Pattern.compile(hex);
       Matcher matcher = pattern.matcher(input);
       String res = null;
        
       if(matcher.find()) {
    	   String hexColor = matcher.group();
           String minecraftColor = convertHexToMinecraftColor(hexColor);
           res = input.replace(hexColor, minecraftColor);
       } else {
    	   res = input;
       }
       return res;
    }

	public static String convertHexToMinecraftColor(String hexColor) {
        // Extraer los valores R, G y B del formato HEX
        String r1 = hexColor.substring(2, 3);
        String r2 = hexColor.substring(3,4);
        String g1 = hexColor.substring(4, 5);
        String g2 = hexColor.substring(5, 6);
        String b1 = hexColor.substring(6,7);
        String b2 = hexColor.substring(7);

        // Construir el formato de color de Minecraft 
        return "&x&" + r1 + "&" + r2 + "&" + g1 + "&" + g2 + "&" + b1 + "&" + b2;
    }
	
	public static Material getMaterialWithProb() {
    	double n = Math.random();
    	Material res = null;
    	if(n>0.40) {
    		res = Material.BEEF;
    	} else {
    		res = Material.BONE;
    	}
    	return res;
    }
	
	public static Boolean strToBool(String s) {
		Boolean res = null;
		if(s=="true") {
			res = true;
		} else if(s=="false") {
			res = false;
		}
		return res;
	}
	
	public static String millisToDate(long millis) {
    	Date fecha = new Date(millis);
        Instant instant = fecha.toInstant();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String res = instant.atZone(ZoneId.systemDefault()).format(formato);
        return res;
	}
	
	public static String millisToTimeQuantity(long millis) {
		long segundos = millis / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        long dias = horas / 24;
        return dias+";"+horas+";"+minutos+";"+segundos;
	}
		
	public static void refillItem(Player player, Material material, EquipmentSlot hand) {
		ItemStack[] items = player.getInventory().getStorageContents();

		for (int i = 0; i < 36; ++i) {
			if (items[i] != null && isValidSlot(i, player) && items[i].getType().equals(material)) {
				if (hand.equals(EquipmentSlot.HAND)) {
					player.getInventory().setItemInMainHand(items[i]);
					player.getInventory().setItem(i, (ItemStack) null);
					break;
				}

				if (hand.equals(EquipmentSlot.OFF_HAND)) {
					player.getInventory().setItemInOffHand(items[i]);
					player.getInventory().setItem(i, (ItemStack) null);
					break;
				}
			}
		}

	}
	
	public static void refillItemFromMinepack(Player player, Material material, EquipmentSlot hand) {
	    Inventory backpack = MinepacksAccessor.getPlayerBackpackInventory(player);
	    
	    if (backpack != null) {
	        ItemStack[] contents = backpack.getContents();
	        for (int i = 0; i < contents.length; i++) {
	            ItemStack itemStack = contents[i];
	            if (itemStack != null && isValidSlot(i, player) && itemStack.getType() == material) {
	                if (hand == EquipmentSlot.HAND) {
	                    player.getInventory().setItemInMainHand(itemStack);
	                } else if (hand == EquipmentSlot.OFF_HAND) {
	                    player.getInventory().setItemInOffHand(itemStack);
	                }
	                contents[i] = (ItemStack) null;
	                backpack.setContents(contents);
	                break; 
	            }
	        }
	    }
	}

	public static boolean isValidSlot(int i, Player player) {
		return i != player.getInventory().getHeldItemSlot() && i != 40;
	}
	
	public static int getItemCount(Inventory inventory, Material material) {
        int count = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }
	
	public static void copyResourceToFile(String resourceName, String destinationPath) throws IOException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName);
             FileOutputStream outputStream = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
    }
	
	public static void createLangs() {
		try {
			File langs = new File(SimpleTools.plugin.getDataFolder(), "langs.yml");
			langs.createNewFile();
			copyResourceToFile("langs.yml", new File(SimpleTools.plugin.getDataFolder(), "langs.yml").getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void reloadConfigItem(InventoryClickEvent event) {
	    ItemStack clickedItem = event.getCurrentItem();
	    if (clickedItem == null || clickedItem.getType() != Material.PAPER) {
	        return; 
	    }
	    
	    ItemMeta itemMeta = clickedItem.getItemMeta();
	    if (itemMeta == null || !itemMeta.hasDisplayName()) {
	        return;
	    }
	    
	    String displayName = itemMeta.getDisplayName();
	    String configKey = "config." + ChatColor.stripColor(displayName);
	    
	    boolean currentValue = config.getBoolean(configKey);
	    boolean newValue = !currentValue;
	    
	    config.getConfig().set(configKey, newValue);
	    config.save();
	    
	    itemMeta.setLore(List.of(Utils.colorCodeParser(config.getString("language.configMenuValueLore")) + newValue));
	    clickedItem.setItemMeta(itemMeta);
	    
	    if (event.getWhoClicked() instanceof Player) {
	        Player player = (Player) event.getWhoClicked();
	        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
	    }
	    
	    event.setCancelled(true);
	    event.getInventory().setItem(event.getSlot(), clickedItem);
	}
	
	public static double distance(Player p1, Player p2) {
	    return p1.getLocation().distance(p2.getLocation());
	}

}
