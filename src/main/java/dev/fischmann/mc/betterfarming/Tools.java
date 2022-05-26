package dev.fischmann.mc.betterfarming;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tools {

	private static final ItemStack DIAMOND_PICKAXE = new ItemStack(Material.DIAMOND_PICKAXE);
	private static final ItemStack DIAMOND_SHOVEL = new ItemStack(Material.DIAMOND_SHOVEL);
	private static final ItemStack DIAMOND_AXE = new ItemStack(Material.DIAMOND_AXE);
	private static final ItemStack SHEARS = new ItemStack(Material.SHEARS);
	private static final ItemStack DIAMOND_HOE = new ItemStack(Material.DIAMOND_HOE);

	private static Map<Material, Object> toolMap = Map.ofEntries(Map.entry(Material.DIAMOND_PICKAXE, DIAMOND_PICKAXE),
			Map.entry(Material.GOLDEN_PICKAXE, DIAMOND_PICKAXE), Map.entry(Material.IRON_PICKAXE, DIAMOND_PICKAXE),
			Map.entry(Material.NETHERITE_PICKAXE, DIAMOND_PICKAXE), Map.entry(Material.STONE_PICKAXE, DIAMOND_PICKAXE),
			Map.entry(Material.WOODEN_PICKAXE, DIAMOND_PICKAXE),

			Map.entry(Material.DIAMOND_SHOVEL, DIAMOND_SHOVEL), Map.entry(Material.GOLDEN_SHOVEL, DIAMOND_SHOVEL),
			Map.entry(Material.IRON_SHOVEL, DIAMOND_SHOVEL), Map.entry(Material.NETHERITE_SHOVEL, DIAMOND_SHOVEL),
			Map.entry(Material.STONE_SHOVEL, DIAMOND_SHOVEL), Map.entry(Material.WOODEN_SHOVEL, DIAMOND_SHOVEL),

			Map.entry(Material.DIAMOND_AXE, DIAMOND_AXE), Map.entry(Material.GOLDEN_AXE, DIAMOND_AXE),
			Map.entry(Material.IRON_AXE, DIAMOND_AXE), Map.entry(Material.NETHERITE_AXE, DIAMOND_AXE),
			Map.entry(Material.STONE_AXE, DIAMOND_AXE), Map.entry(Material.WOODEN_AXE, DIAMOND_AXE),

			Map.entry(Material.DIAMOND_HOE, DIAMOND_HOE), Map.entry(Material.GOLDEN_HOE, DIAMOND_HOE),
			Map.entry(Material.IRON_HOE, DIAMOND_HOE), Map.entry(Material.NETHERITE_HOE, DIAMOND_HOE),
			Map.entry(Material.STONE_HOE, DIAMOND_HOE), Map.entry(Material.WOODEN_HOE, DIAMOND_HOE),

			Map.entry(Material.SHEARS, SHEARS));

	private static final ItemStack[] testTools = { DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_AXE, DIAMOND_HOE, SHEARS };

	public static boolean isFastestToolInHand(Block b, Player p) {

		if (!b.getType().isSolid()) {
			return false;
		}

		ItemStack isSave = new ItemStack(p.getInventory().getItemInMainHand());
		ItemStack fastestTool = null;
		float fastestBreakSpeed = 0;

		for (ItemStack testTool : testTools) {
			p.getInventory().setItemInMainHand(testTool);
			
			float breakspeed = b.getBreakSpeed(p);

			if (breakspeed > fastestBreakSpeed) {
				fastestBreakSpeed = breakspeed;
				fastestTool = testTool;
			}
		}
		p.getInventory().setItemInMainHand(isSave);

		Material pMainMat = isSave.getType();
		return fastestTool != null && fastestTool.equals(toolMap.get(pMainMat));
	}

	public static boolean isValidToEnchantTool(ItemStack is) {
		return toolMap.containsKey(is.getType());
	}
	
	public static boolean isAxe(ItemStack is) {
		return DIAMOND_AXE.equals(toolMap.get(is.getType()));
	}
}
