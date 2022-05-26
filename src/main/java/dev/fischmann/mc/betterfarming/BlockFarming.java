package dev.fischmann.mc.betterfarming;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import dev.fischmann.mc.betterfarming.constants.Direction;
import dev.fischmann.mc.betterfarming.constants.Enchants;
import dev.fischmann.mc.betterfarming.constants.Setting;
import dev.fischmann.mc.betterfarming.settings.SettingsStorage;

public class BlockFarming {

	public static boolean isValidBlock(Block b, Player p) {

		Material type = b.getType();

		if (SettingsStorage.INSTANCE.getValueOf(Setting.OBSIDIAN_VALID_BLOCK) == 0 && Material.OBSIDIAN.equals(type)) {
			return false;
		}

		return Tools.isFastestToolInHand(b, p);
	}

	// returns true if block break event should be canceled.
	public static boolean farmBlock(Player p, Block bb) {

		FarmerData farmerData = FarmerData.INSTANCE;
		UUID uuid = p.getUniqueId();
		ItemStack mainHand = p.getInventory().getItemInMainHand();


		if (farmerData.isDirection(mainHand, Direction.TOOL_NONE)) {
			return false;
		}

		if (isValidBlock(bb, p)) {

			Location locOne;
			Location locTwo;

			World world = p.getWorld();

			if (farmerData.isDirection(mainHand, Direction.TOOL_TREE_FARMING)){
				if (farmTreeWithAxe(bb, p)) {
					return true;
				}
				return false;
			}

			if (farmerData.isDirection(mainHand, Direction.TOOL_VERTICAL)) {
				locOne = new Location(world, bb.getX(), bb.getY() - 1, bb.getZ());
				locTwo = new Location(world, bb.getX(), bb.getY() + 1, bb.getZ());
			} else {

				Vector dic = p.getLocation().getDirection();
				double x = Math.abs(dic.getX());
				double z = Math.abs(dic.getZ());

				if (z > x) {
					locOne = new Location(world, bb.getX() - 1, bb.getY(), bb.getZ());
					locTwo = new Location(world, bb.getX() + 1, bb.getY(), bb.getZ());
				} else {
					locOne = new Location(world, bb.getX(), bb.getY(), bb.getZ() - 1);
					locTwo = new Location(world, bb.getX(), bb.getY(), bb.getZ() + 1);
				}
			}

			Block bOne = world.getBlockAt(locOne);
			if (BlockFarming.isValidBlock(bOne, p)) {
				farmerData.addFarmerLocation(uuid, locOne);
				p.breakBlock(bOne);
			}

			Block bTwo = world.getBlockAt(locTwo);

			if (BlockFarming.isValidBlock(bTwo, p)) {
				farmerData.addFarmerLocation(uuid, locTwo);
				p.breakBlock(bTwo);

			}
		}
		return false;
	}
	
	
	public static boolean farmTreeWithAxe(Block b, Player p) {

		return TreeFarming.farmTree(b, p);
	}

	public static boolean isFarmerTool(ItemStack is) {

		if (is == null) {
			return false;
		}
		
		if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
			return (is.getItemMeta().getLore().contains(Enchants.FARMERLORE.getValue()));
		}
		return false;
	}
}
