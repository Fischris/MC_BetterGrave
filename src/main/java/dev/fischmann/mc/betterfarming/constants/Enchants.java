package dev.fischmann.mc.betterfarming.constants;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum Enchants {
	FARMERLORE("Farming");

	final String loreName;
	Enchants(String loreName) {
		this.loreName = loreName;
	}
	
	public String getValue() {
		return loreName;
	}

	public void remove(ItemStack is) {
		ItemMeta im = is.getItemMeta();


		if (!im.hasLore()) {
			return;
		}

		List<String> lore = im.getLore();
		lore.remove(Enchants.FARMERLORE.getValue());
		PersistentDataContainer container = im.getPersistentDataContainer();
		container.remove(NamespacedKeys.FARMER_UUID.getKey());

		im.setLore(lore);
		is.setItemMeta(im);
	}

	public void add(ItemStack is) {
		ItemMeta im = is.getItemMeta();

		UUID uuid = UUID.randomUUID();
		PersistentDataContainer container = im.getPersistentDataContainer();
		container.set(NamespacedKeys.FARMER_UUID.getKey(), PersistentDataType.STRING, uuid.toString());

		List<String> lore = new ArrayList<String>();
		if (im.hasLore()) {
			lore = im.getLore();
		}
		lore.add(Enchants.FARMERLORE.getValue());
		im.setLore(lore);
		is.setItemMeta(im);
	}
}
