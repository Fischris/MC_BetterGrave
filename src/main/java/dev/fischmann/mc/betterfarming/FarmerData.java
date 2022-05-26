package dev.fischmann.mc.betterfarming;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import dev.fischmann.mc.betterfarming.constants.NamespacedKeys;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.fischmann.mc.betterfarming.constants.Direction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public enum FarmerData {

	INSTANCE;
	
	private HashMap <String, Direction> hmFarmers = new HashMap<>();
	private HashMap <UUID, Long> hmFarmersTimeinMillis = new HashMap<>();

	private HashMap <UUID, ArrayList<Location>> hmFarmerBlocks = new HashMap<>();

	public void prepareMaps(UUID uuid) {
		hmFarmersTimeinMillis.put(uuid, (long)0);
	}
	
	public void removeMaps(UUID uuid) {
		hmFarmersTimeinMillis.remove(uuid);
	}
	
	public void addFarmerLocation (UUID uuid, Location loc) {
		ArrayList<Location> locations = hmFarmerBlocks.computeIfAbsent(uuid, k -> new ArrayList<>());
		locations.add(loc);
	}
	
	public boolean isFarmersLocation (UUID uuid, Location loc) {
		ArrayList<Location> locations = hmFarmerBlocks.get(uuid);
		if (locations == null) {
			return false;
		}
		return locations.remove(loc);
	}
	
	
	public void setFarmerSwitchTime(Player p) {
		hmFarmersTimeinMillis.put(p.getUniqueId(), Calendar.getInstance().getTimeInMillis());
	}
	
	public boolean switchedToolFastEnough (UUID uuid) {
		Long start = hmFarmersTimeinMillis.get(uuid);
		if (start == null) {
			start = 0L;
		}
		return (Calendar.getInstance().getTimeInMillis() - start) < 1001;
	}
	
	
	public Direction decideIfDirectionChange (Player p, ItemStack main) {
		UUID uuid = p.getUniqueId();
		if (switchedToolFastEnough(uuid)) {

			String farmerUuid = getToolUUID(main);
			Direction directionNow = hmFarmers.get(farmerUuid);

			if (directionNow == null) {
				directionNow = Direction.TOOL_NONE;
			}

			switch (directionNow) {

				case TOOL_VERTICAL-> {
					hmFarmers.put(farmerUuid, Direction.TOOL_NONE);
					return Direction.TOOL_NONE;
				}
				case TOOL_NONE -> {
					if (Tools.isAxe(main)) {
						hmFarmers.put(farmerUuid, Direction.TOOL_TREE_FARMING);
						return Direction.TOOL_TREE_FARMING;
					}
					hmFarmers.put(farmerUuid, Direction.TOOL_HORIZONTAL);
					return Direction.TOOL_HORIZONTAL;
				}

				case TOOL_TREE_FARMING -> {
					hmFarmers.put(farmerUuid, Direction.TOOL_HORIZONTAL);
					return Direction.TOOL_HORIZONTAL;
				}

				case TOOL_HORIZONTAL-> {
					hmFarmers.put(farmerUuid, Direction.TOOL_VERTICAL);
					return Direction.TOOL_VERTICAL;
				}
			}
		}
		return null;
	}
	
	public boolean isDirection(ItemStack main, Direction direction) {
		return direction.equals(hmFarmers.get(getToolUUID(main)));
	}

	private String getToolUUID(ItemStack is) {
		PersistentDataContainer pdc = is.getItemMeta().getPersistentDataContainer();
		return pdc.get(NamespacedKeys.FARMER_UUID.getKey(), PersistentDataType.STRING);
	}
	
}


 
