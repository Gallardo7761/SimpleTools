package dev.gallardo.simpletools.events;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityEventHelper {
	
	private Player player;
	private Entity entity;
	private ItemStack itemStack;
	private ItemMeta itemMeta;
	private int damage = 0;
	private Damageable dMeta;
	private int maxDamage;
	private int amount;
	
	@SuppressWarnings("deprecation")
	private EntityEventHelper(PlayerInteractEntityEvent event) {
		player = event.getPlayer();
		entity = event.getRightClicked();
		itemStack = player.getItemInHand();
		itemMeta = itemStack.getItemMeta();
		if (itemMeta instanceof Damageable) {
			dMeta = (Damageable) itemMeta;
			damage = dMeta.getDamage();
		}
		maxDamage = itemStack.getType().getMaxDurability();
		amount = itemStack.getAmount();
	}
	
	public static EntityEventHelper of(PlayerInteractEntityEvent event) {
		return new EntityEventHelper(event);
	}
	
	public void handleScissorsOnCreeper() {
		double r = Math.random();
		int n = (int) ((Math.random() + 1) * 1.25);
		if (r < 0.10) {
			this.entity.remove();
			this.player.playSound(this.player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
			this.player.playEffect(EntityEffect.FIREWORK_EXPLODE);
			this.player.getWorld().dropItemNaturally(this.entity.getLocation(), new ItemStack(Material.VINE, n));
			this.player.getWorld().dropItemNaturally(this.entity.getLocation(), new ItemStack(Material.GUNPOWDER, n));
			double r2 = Math.random();
			System.out.println("Número random 2: " + r2);
			if (r2 < 0.30) {
				this.player.getWorld().dropItemNaturally(this.entity.getLocation(), new ItemStack(Material.CREEPER_HEAD, 1));
			}
			if (damage + 2 <= this.maxDamage) {
				dMeta.setDamage(damage + 2);
				this.itemStack.setItemMeta(dMeta);
			}
		} else {
			if (damage + 1 <= maxDamage) {
				dMeta.setDamage(damage + 1);
				this.itemStack.setItemMeta(dMeta);
			}
		}
	}
	
	public void handleScissorsOnPig() {
		if (((Ageable) this.entity).isAdult()) {
			int n = (int) ((Math.random() + 1) * 1.25);
			this.player.playSound(this.player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
			((Ageable) this.entity).setBaby();
			this.player.getWorld().dropItemNaturally(this.entity.getLocation(), new ItemStack(Material.PORKCHOP, n));
			if (damage + 2 <= this.maxDamage) {
				dMeta.setDamage(damage + 2);
				this.itemStack.setItemMeta(dMeta);
			}
		}
	}
	
	public void handleScissorsOnCow() {
		if (((Ageable) this.entity).isAdult()) {
			int n = (int) ((Math.random() + 1) * 1.25);
			this.player.playSound(this.player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
			((Ageable) this.entity).setBaby();
			this.player.getWorld().dropItemNaturally(this.entity.getLocation(), new ItemStack(Material.BEEF, n));
			if (damage + 2 <= this.maxDamage) {
				dMeta.setDamage(damage + 2);
				this.itemStack.setItemMeta(dMeta);
			}
		}
	}

	public void handleScissorsOnZombie() {
		if (((Ageable) this.entity).isAdult()) {
			int n = (int) ((Math.random() + 1) * 1.25);
			this.player.playSound(this.player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
			((Ageable) this.entity).remove();
			Skeleton skeleton = (Skeleton) this.entity.getLocation().getWorld().spawnEntity(this.entity.getLocation(),
					EntityType.SKELETON);
			EntityEquipment equipment = skeleton.getEquipment();
			equipment.setItemInMainHand(null);
			this.player.getWorld().dropItemNaturally(this.entity.getLocation(), new ItemStack(Material.ROTTEN_FLESH, n));
			if (damage + 2 <= this.maxDamage) {
				dMeta.setDamage(damage + 2);
				this.itemStack.setItemMeta(dMeta);
			}

		}
	}
	
	public void handleRottenFleshOnSkeleton() {
		if(this.amount >= 15) {
			this.entity.remove();
			this.itemStack.setAmount(amount - 15);
			Zombie zombie = (Zombie) this.entity.getLocation().getWorld()
					.spawnEntity(this.entity.getLocation(), EntityType.ZOMBIE);
			EntityEquipment equipment = zombie.getEquipment();
			equipment.setItemInMainHand(new ItemStack(Material.BOW));
		}
	}
	
	public void handleTotemOnPillager() {
		this.itemStack.setAmount(0);
		double n = Math.random();
		if (n < 0.15) {
			this.entity.remove();
			Villager villager = (Villager) this.entity.getLocation().getWorld()
					.spawnEntity(this.entity.getLocation(),
					EntityType.VILLAGER);
			villager.setBaby();
			this.player.playSound(this.player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
			this.player.playEffect(EntityEffect.TOTEM_RESURRECT);
		} else {

		}
	}
}
