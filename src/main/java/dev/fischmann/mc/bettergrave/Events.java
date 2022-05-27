package dev.fischmann.mc.bettergrave;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {
	public Plugin plugin;

	public Events(Plugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		PlayerInventory inventory = p.getInventory();
		List<ItemStack> items= new ArrayList<>();

		for (int i=5; i<46; i++){
			ItemStack is = inventory.getItem(i);
			if (is != null && !Material.AIR.equals(is.getType())){
				items.add(is);
			}
		}
		
	}
	
}
