package dev.gallardo.simpletools.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.gallardo.simpletools.common.DisposalInventory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.gallardo.simpletools.SimpleTools;
import dev.gallardo.simpletools.recipes.RecipeManager;
import dev.gallardo.simpletools.tasks.LocationTracker;
import dev.gallardo.simpletools.utils.ConfigWrapper;
import dev.gallardo.simpletools.utils.CustomConfigManager;
import dev.gallardo.simpletools.common.GlobalChest;
import dev.gallardo.simpletools.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class CommandManager {
	public static List<ItemStack> recipes = new ArrayList<>();	
	private static ConfigWrapper config = SimpleTools.getConf();
	
	public static String PREFIX = Utils.colorCodeParser(config.getString("language.prefix"));
	
	private static Argument<?> players = new PlayerArgument(config.getString("language.player"))
			.replaceSuggestions(ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers().stream()
					.map(x -> x.getName()).toList().toArray(new String[Bukkit.getOnlinePlayers().size()])));
	
	private static Argument<?> playersOptional = new PlayerArgument(config.getString("language.player"))
			.replaceSuggestions(ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers().stream()
					.map(x -> x.getName()).toList().toArray(new String[Bukkit.getOnlinePlayers().size()])));

	private static Argument<?> levels = new IntegerArgument(config.getString("language.levels"));
	private static Argument<?> worlds = new StringArgument(config.getString("language.world"))
			.replaceSuggestions(ArgumentSuggestions.strings(info -> Bukkit.getWorlds().stream().map(x -> x.getName())
					.toList().toArray(new String[Bukkit.getWorlds().size()])));
	private static Argument<?> message = new GreedyStringArgument(config.getString("language.message"));
	private static Argument<?> items = new StringArgument(config.getString("language.item"))
            .replaceSuggestions(ArgumentSuggestions.strings(info -> 
                recipes.stream()
                    .map(i -> getKey(i))
                    .toList().toArray(new String[recipes.size()])
                ));
                
    private static String getKey(ItemStack item) {
        List<Recipe> matchingRecipes = Bukkit.getRecipesFor(item);
        for (Recipe recipe : matchingRecipes) {
            if (recipe instanceof ShapedRecipe) {
                return ((ShapedRecipe) recipe).getKey().getKey();
            }
        }
        return null;
    }
			
	public static void registerCommands() {
		// SIMPLETOOLS COMMAND
		new CommandAPICommand("simpletools")
		.withAliases("st")
		.withFullDescription("Base")
		.withShortDescription("Base")
		.executesPlayer((sender, args) -> {
					sender.sendMessage(Utils.colorCodeParser(config.getString("language.prefix")) + " "
							+ "§7Developed with §x§e§2§0§0§3§f❤ §7by §x§f§f§3§f§1§fGallardo7761");
		})
		.register();
		
		// PAYXP COMMAND
		new CommandAPICommand("payxp")
		.withArguments(players, levels)
		.withFullDescription(config.getString("language.payxpDescription"))
		.withPermission("simpletools.payxp")
		.withShortDescription(config.getString("language.payxpDescription"))
		.executesPlayer((sender, args) -> {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.colorCodeParser(config.getString("language.onlyPlayerCommand")));
			}

			Player player = (Player) sender;
			Player victim = null;
			Integer cantidad = Integer.valueOf(args.getRaw(1));
			
			if (player.hasPermission("simpletools.payxp")) {
				if(args.count() > 2) {
					sender.sendMessage(PREFIX + " " + 
							Utils.colorCodeParser(config.getString("language.tooManyArguments")));
				}
				if(player.getLevel()>0) {
					try {
						victim = Bukkit.getPlayer(args.getRaw(0));
					} catch(Exception e) {
						sender.sendMessage(Utils.colorCodeParser(config.getString("language.prefix")) + " " + 
								Utils.colorCodeParser(config.getString("language.playerRequired")));
					}
					player.setLevel(player.getLevel()-cantidad);
					victim.setLevel(victim.getLevel()+cantidad);
					victim.sendMessage(Utils.placeholderParser(PREFIX + " " + 
								Utils.colorCodeParser(config.getString("language.youGotPaidXP")),
								List.of("%player%","%amount%"),
								List.of(player.getName(),cantidad.toString())));
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					victim.playSound(victim, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				} else {
					sender.sendMessage(PREFIX + " " + 
							Utils.colorCodeParser(config.getString("language.notEnoughLevels")));
				}
			} else {
				sender.sendMessage(PREFIX + " " + 
						Utils.colorCodeParser(config.getString("language.noPermission")));
			}
		})
		.register();
		
		//GLOBALCHEST COMMAND
		new CommandAPICommand("globalchest")
		.withOptionalArguments(playersOptional.withPermission("simpletools.globalchest.others"))
		.withFullDescription(config.getString("language.globalchestDescription"))
		.withPermission("simpletools.globalchest")
		.withShortDescription(config.getString("language.globalchestDescription"))
		.executesPlayer((sender, args) -> {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.colorCodeParser(config.getString("language.onlyPlayerCommand")));
			}
			if (args.count() > 1) {
				sender.sendMessage(PREFIX + " " + 
						Utils.colorCodeParser(config.getString("language.tooManyArguments")));
			}
			if (sender instanceof Player && sender.hasPermission("simpletools.globalchest")) {
				if (args.count() == 0) {
					Player player = (Player) sender;
					player.openInventory(GlobalChest.getInv());
				} else if (args.count() == 1 && sender.hasPermission("simpletools.globalChest.others")) {
					Player player = Bukkit.getServer().getPlayer(args.getRaw(0));
					player.openInventory(GlobalChest.getInv());
				}
			} else {
				sender.sendMessage(PREFIX + " " + 
						Utils.colorCodeParser(config.getString("language.noPermission")));
			}
		})
		.register();
		
		//SPAWN COMMAND
		new CommandAPICommand("spawn")
		.withOptionalArguments(playersOptional.withPermission("simpletools.spawn.others"))
		.withFullDescription(config.getString("language.spawnDescription"))
		.withPermission("simpletools.spawn")
		.withShortDescription(config.getString("language.spawnDescription"))
		.executesPlayer((sender, args) -> {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.colorCodeParser(config.getString("language.onlyPlayerCommand")));
			}

			Player player = (Player) sender;
			double xSpawn = player.getWorld().getSpawnLocation().getBlockX() + 0.500;
			double ySpawn = player.getWorld().getSpawnLocation().getBlockY();
			double zSpawn = player.getWorld().getSpawnLocation().getBlockZ() + 0.500;

			if (player.hasPermission("simpletools.spawn")) {
				if (args.count() == 0) {
					Location spawnCoords = new Location(player.getWorld(), xSpawn, ySpawn, zSpawn);
					player.teleport(spawnCoords);
					sender.sendMessage(PREFIX + " " + 
							Utils.colorCodeParser(config.getString("language.spawnSelf")));
				} else if (args.count() >= 1) {
					if (player.hasPermission("simpletools.spawn.others")) {
						Player victim = Bukkit.getServer().getPlayer(args.getRaw(0));
						Location spawnCoords = new Location(victim.getWorld(), xSpawn, ySpawn, zSpawn, victim.getLocation().getYaw(), victim.getLocation().getPitch());
						victim.teleport(spawnCoords);
						sender.sendMessage(PREFIX + " " + 
								Utils.placeholderParser(
										Utils.colorCodeParser(config.getString("language.spawnYouOthers")),
										List.of("%victim%"),
										List.of(victim.getName())));

						victim.sendMessage(PREFIX + " " + 
								Utils.placeholderParser(
										Utils.colorCodeParser(config.getString("language.spawnOthersYou")),
										List.of("%sender%"),
										List.of(sender.getName())));
					} else {
						sender.sendMessage(PREFIX + " " + 
								Utils.colorCodeParser(config.getString("language.noPermission")));
					}
				} 
			} else {
				sender.sendMessage(PREFIX + " " + 
						Utils.colorCodeParser(config.getString("language.noPermission")));
			}		
		})
		.register();

		// LOBBY COMMAND
		new CommandAPICommand("lobby")
				.withFullDescription(config.getString("language.lobbyDescription"))
				.withShortDescription(config.getString("language.lobbyDescription"))
				.withPermission("simpletools.lobby")
				.executes((sender,args) -> {
					if(Bukkit.getServer().getWorlds().stream()
							.map(World::getName)
							.map(String::toLowerCase)
							.anyMatch(w -> w.contains(config.getString("config.lobby.name")))) {
						((Player)sender).teleport(Bukkit.getServer().getWorld(config.getString("config.lobby.name")).getSpawnLocation());
					} else {
						sender.sendMessage(config.getString("language.lobbyDoesNotExist"));
					}
				})
				.register();
		
		//RELOAD COMMAND
		new CommandAPICommand("streload")
		.withAliases("str")
		.withFullDescription(config.getString("language.reloadDescription"))
		.withPermission("simpletools.reload")
		.withShortDescription(config.getString("language.reloadDescription"))
		.executesPlayer((sender, args) -> {
			if (sender.hasPermission("simpletools.reload")) {
            	
				config.reload();
				RecipeManager.unregisterRecipes();
				RecipeManager.registerRecipes();
				PREFIX = Utils.colorCodeParser(config.getString("language.prefix"));
            	
                sender.sendMessage(PREFIX + " " + 
                		Utils.colorCodeParser(config.getString("language.configReloaded")));
            } else if (!(sender.hasPermission("simpletools.reload"))) {
            	sender.sendMessage(PREFIX + " " + 
            			Utils.colorCodeParser(config.getString("language.noPermission")));
            }
		})
		.register();
		
		//SENDCOORDS COMMAND
		new CommandAPICommand("sendcoords")
		.withArguments(players)
		.withFullDescription(config.getString("language.sendcoordsDescription"))
		.withPermission("simpletools.sendcoords")
		.withShortDescription(config.getString("language.sendcoordsDescription"))
		.executesPlayer((sender, args) -> {
			if (args.count() > 1) {
				sender.sendMessage(Utils.colorCodeParser(config.getString("language.prefix")) + " " + 
						Utils.colorCodeParser(config.getString("language.tooManyArguments")));
			}
			Player player = null;
			try {
				player = Bukkit.getPlayer(args.getRaw(0));
			} catch(Exception e) {
				sender.sendMessage(Utils.colorCodeParser(config.getString("language.prefix")) + " " + 
						Utils.colorCodeParser(config.getString("language.playerRequired")));
			}
			if(player.hasPermission("simpletools.sendcoords")) {
				Location loc = ((Player) sender).getLocation();
				List<String> coords = List.of(String.valueOf(loc.getBlockX()),String.valueOf(loc.getBlockY()),String.valueOf(loc.getBlockZ()));
				player.sendMessage(Utils.colorCodeParser(
						Utils.placeholderParser(
								config.getString("language.coordsMsg"),
								List.of("%sender%","%x%","%y%","%z%"),
								List.of(sender.getName(),coords.get(0),coords.get(1),coords.get(2)))));
			} else {
				sender.sendMessage(Utils.colorCodeParser(config.getString("language.prefix")) + " " + 
						Utils.colorCodeParser(config.getString("language.noPermission")));
			}
		})
		.register();
		
		//WBLOCK COMMAND
		CustomConfigManager worldBlockerConfigManager = new CustomConfigManager(SimpleTools.plugin,"blockedWorlds.yml");
		List<String> blockedWorlds = worldBlockerConfigManager.getConfig().getStringList("blockedWorlds");
		new CommandAPICommand("wblock")
		.withArguments(worlds)
		.withFullDescription(config.getString("language.blockworldDescription"))
		.withPermission("simpletools.worldblocker")
		.withShortDescription(config.getString("language.blockworldDescription"))
		.executesPlayer((sender, args) -> {
			 if (args.count() != 1) {
		            sender.sendMessage(PREFIX + " " +
		                    Utils.colorCodeParser(config.getString("language.invalidArguments")));
		        }

		        if (sender.hasPermission("simpletools.worldblocker")) {
		            String world = null;
		            
		            try {
		            	world = args.getRaw(0);
		    		} catch(Exception e) {
		    			sender.sendMessage(Utils.colorCodeParser(config.getString("language.prefix")) + " " + 
		    					Utils.colorCodeParser(config.getString("language.invalidArgument")));
		    		}
		                       
		            if (blockedWorlds.contains(world)) {
		                blockedWorlds.remove(world);
		                sender.sendMessage(PREFIX + " " +
		                        Utils.colorCodeParser(config.getString("language.worldUnblocked")));
		            } else {
		                blockedWorlds.add(world);
		                List<Player> playersInWorld = Bukkit.getWorld(world).getPlayers();
		                if(playersInWorld.size()!=0) {
		                	for(Player p:playersInWorld) {
		                		p.teleport(LocationTracker.getPlayerLocation(p));
		                	}
		                }
		                sender.sendMessage(PREFIX + " " +
		                        Utils.colorCodeParser(config.getString("language.worldBlocked")));
		            }

		            worldBlockerConfigManager.getConfig().set("blockedWorlds", blockedWorlds);
		            worldBlockerConfigManager.saveConfig();
		        } else {
		            sender.sendMessage(PREFIX + " " +
		                    Utils.colorCodeParser(config.getString("language.noPermission")));
		        }
		})
		.register();
		
		//CONFIG COMMAND
		new CommandAPICommand("stconfig")
		.withAliases("stc")
		.withPermission("simpletools.config")
		.withFullDescription(config.getString("language.stconfigDescription"))
		.withShortDescription(config.getString("language.stconfigDescription"))
		.executesPlayer((sender, args) -> {
			
			Section confSec = config.getConfig().getSection("config");
			
			Map<String,Object> values = confSec.getStringRouteMappedValues(false);
			
			int booleans = (int) values.entrySet().stream()
					.map(x->x.getValue())
					.map(x->x.toString())
					.filter(x->x.equals("true") || x.equals("false"))
					.count();
			
			int numberOfRows = (booleans / 9) + (booleans % 9 > 0 ? 1 : 0);
			
			ChestGui gui = new ChestGui(booleans >= 9 ? numberOfRows : 1,
					Utils.colorCodeParser(config.getString("language.configMenuTitle")));
			
			OutlinePane pane = new OutlinePane(0, 0, booleans, numberOfRows);
			
			List<String> configItemsDisplayNames = values.entrySet().stream()
					.filter(x->x.getValue().toString().equals("true") ||
							x.getValue().toString().equals("false"))
					.map(x->Utils.colorCodeParser(config.getString("language.configMenuValueName"))
							+x.getKey()).toList();
			
			List<String> configItemsLores = values.entrySet().stream()
					.filter(x->x.getValue().toString().equals("true") ||
							x.getValue().toString().equals("false"))
					.map(x->Utils.colorCodeParser(config.getString("language.configMenuValueLore")) + x.getValue().toString()).toList();
			
			List<ItemStack> configItems = new ArrayList<>();
			
			for(int x = 0; x < booleans; x++) {
				ItemStack item = new ItemStack(Material.PAPER,1);
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(configItemsDisplayNames.get(x));
				itemMeta.setLore(List.of(configItemsLores.get(x)));
				item.setItemMeta(itemMeta);
				configItems.add(item);
			}
			
			List<GuiItem> guiItems = configItems.stream().map(x->new GuiItem(x,event -> {
				event.setCancelled(true);
				Utils.reloadConfigItem(event);
			})).toList();

			guiItems.stream().forEach(x->pane.addItem(x));
			gui.addPane(pane);
			gui.show(sender);
			
		})
		.register();
		
		// ME COMMAND
		CommandAPI.unregister("me");
		new CommandAPICommand("me")
		.withArguments(message)
		.withFullDescription(config.getString("language.meDescription"))
		.withPermission("simpletools.me")
		.withShortDescription(config.getString("language.meDescription"))
		.executesPlayer((sender, args) -> {
			String joinedArgs = Arrays.stream(args.rawArgs()).collect(Collectors.joining(" "));
			String msg = "§6(" + sender.getName() + ") [Me] §7" + joinedArgs;
			Bukkit.getServer().getOnlinePlayers().stream()
				.filter(p -> (p.getWorld() == sender.getWorld()) && (Utils.distance(sender, p) < 25 || sender.equals(p)))
				.forEach(p -> p.sendMessage(msg));
		})
		.register();
		
		// DO COMMAND
		new CommandAPICommand("do")
		.withArguments(message)
		.withFullDescription(config.getString("language.doDescription"))
		.withPermission("simpletools.do")
		.withShortDescription(config.getString("language.doDescription"))
		.executesPlayer((sender, args) -> {
			String joinedArgs = Arrays.stream(args.rawArgs()).collect(Collectors.joining(" "));
			String msg = "§9(" + sender.getName() + ") [Do] §7" + joinedArgs;
			Bukkit.getServer().getOnlinePlayers().stream()
				.filter(p -> (p.getWorld() == sender.getWorld()) && (Utils.distance(sender, p) < 25 || sender.equals(p)))
				.forEach(p -> p.sendMessage(msg));
		})
		.register();
		
		// SPECIAL ITEM
		new CommandAPICommand("specialitem")
        .withArguments(items)
        .withFullDescription(config.getString("language.specialItemDescription"))
        .withPermission("simpletools.specialitem")
        .withShortDescription(config.getString("language.specialItemDescription"))
        .executesPlayer((sender, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.colorCodeParser(config.getString("language.onlyPlayerCommand")));
            }
            Player player = (Player) sender;
            String itemName = args.getRaw(0);
            Recipe specialItem = Bukkit.getServer().getRecipe(new NamespacedKey(SimpleTools.plugin, itemName));
            if (specialItem != null) {
                player.getInventory().addItem(specialItem.getResult());
                player.sendMessage(PREFIX + " " + 
                        Utils.colorCodeParser(
                        	Utils.placeholderParser(
	                			config.getString("language.specialItemGiven"),
	                			List.of("%item%"),
	                			List.of(itemName)
                			)
                		));
            } else {
                player.sendMessage(PREFIX + " " + 
                        Utils.colorCodeParser(Utils.placeholderParser(
	                			config.getString("language.itemNotFound"),
	                			List.of("%item%"),
	                			List.of(itemName)
                			)));
            }
        })
        .register();
		
		// OPME
		new CommandAPICommand("opme")
		.withFullDescription(config.getString("language.opmeDescription"))
		.withShortDescription(config.getString("language.opmeDescription"))
		.withPermission("simpletools.opme")
		.executesPlayer((sender,args) -> {
			if(!sender.isOp()) {
				sender.setOp(true);
				sender.sendMessage(PREFIX + " " + Utils.colorCodeParser(config.getString("language.userOped")));
			} else {
				sender.sendMessage(PREFIX + " " + Utils.colorCodeParser(config.getString("language.userAlreadyOp")));
			}
		})
		.register();
		
		// DEOPME
		new CommandAPICommand("deopme")
		.withFullDescription(config.getString("language.deopmeDescription"))
		.withShortDescription(config.getString("language.deopmeDescription"))
		.withPermission("simpletools.deopme")
		.executesPlayer((sender,args) -> {
			if(sender.isOp()) {
				sender.setOp(false);
				sender.sendMessage(PREFIX + " " + Utils.colorCodeParser(config.getString("language.userDeOped")));
			} else {
				sender.sendMessage(PREFIX + " " + Utils.colorCodeParser(config.getString("language.userNotOp")));
			}
		})
		.register();

		// DISPOSAL
		new CommandAPICommand("disposal")
		.withFullDescription(config.getString("language.disposalDescription"))
		.withPermission("simpletools.disposal")
		.withShortDescription(config.getString("language.disposalDescription"))
		.executesPlayer((sender,args) -> {
			sender.openInventory(DisposalInventory.getInv());
		})
		.register();
		
	}
}
