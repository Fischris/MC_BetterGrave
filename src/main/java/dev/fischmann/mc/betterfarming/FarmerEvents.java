package dev.fischmann.mc.betterfarming;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import dev.fischmann.mc.betterfarming.constants.Direction;

public class FarmerEvents implements Listener {

	public Plugin plugin;
	private Random rand;

	private String label = ChatColor.GREEN + "[" + ChatColor.WHITE + "BetterFarming" + ChatColor.GREEN + "] "
			+ ChatColor.WHITE;

	ArrayList<Material> ShovelList = new ArrayList<>();

	public FarmerEvents(Plugin plugin) {
		this.plugin = plugin;
		rand = new Random();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		FarmerData.INSTANCE.prepareMaps(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		FarmerData.INSTANCE.removeMaps(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onChangeFarmerBlocks(PlayerSwapHandItemsEvent e) {

		Player p = e.getPlayer();

		ItemStack main = e.getMainHandItem();
		ItemStack off = e.getOffHandItem();
		FarmerData farmerData = FarmerData.INSTANCE;
		if (BlockFarming.isFarmerTool(off)) {
			farmerData.setFarmerSwitchTime(p);
		}

		if (BlockFarming.isFarmerTool(main)) {
			Direction direction = farmerData.decideIfDirectionChange(p, main);

			if (direction != null) {
				Message.send(p, direction.getLanguageLabel());
			}
		}
	}

	@EventHandler
	public void OnBlockBreak(BlockBreakEvent bbe) {

		Block b = bbe.getBlock();
		Player p = bbe.getPlayer();

		if (FarmerData.INSTANCE.isFarmersLocation(p.getUniqueId(), bbe.getBlock().getLocation())) {
			return;
		}

		ItemStack mainHand = p.getInventory().getItemInMainHand();

		if (!p.isSneaking()) {
			if (BlockFarming.isFarmerTool(mainHand)) {
				boolean cancelBlockBreak = BlockFarming.farmBlock(p, b);
				bbe.setCancelled(cancelBlockBreak);
			}
		}
	}

	@EventHandler
	void onClayCraft(CraftItemEvent cie) {

		if (Material.CLAY.equals(cie.getCurrentItem().getType())) {
			cie.getWhoClicked().sendMessage(label + "Craften von Lehmblcken ist nicht mglich."
					+ "\nNutze Silk, damit du diese erhalten kannst.");
			cie.getWhoClicked().closeInventory();
			cie.setCancelled(true);
		}

	}

	@EventHandler
	void onClayCraftPrepareEvent(PrepareItemCraftEvent pce) {

		if (pce.getInventory().getResult() != null && Material.CLAY.equals(pce.getInventory().getResult().getType())) {
			pce.getInventory().setResult(new ItemStack(Material.AIR));

		}
	}

	void SpawnItemsForLootShovel(Player p, Block b) {

		int randy = rand.nextInt(100);
		switch (p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) {
		case 1:
			if (randy < 33) {

				Collection<ItemStack> is = b.getDrops();
				for (ItemStack i : is) {
					p.getWorld().dropItem(b.getLocation(), i);

				}
			}
			break;

		case 2:
			if (randy < 33) {
				Collection<ItemStack> is = b.getDrops();
				for (ItemStack i : is) {
					p.getWorld().dropItem(b.getLocation(), i);
				}

			} else if (randy > 24 && randy < 50) {

				Collection<ItemStack> is = b.getDrops();
				for (ItemStack i : is) {
					p.getWorld().dropItem(b.getLocation(), i);
					p.getWorld().dropItem(b.getLocation(), i);
				}
			}
			break;

		case 3:

			if (randy < 20) {

				Collection<ItemStack> is = b.getDrops();
				for (ItemStack i : is) {
					p.getWorld().dropItem(b.getLocation(), i);
				}

			} else if (randy > 19 && randy < 40) {

				Collection<ItemStack> is = b.getDrops();

				for (ItemStack i : is) {
					p.getWorld().dropItem(b.getLocation(), i);
					p.getWorld().dropItem(b.getLocation(), i);
				}
			} else if (randy > 39 && randy < 60) {

				Collection<ItemStack> is = b.getDrops();

				for (ItemStack i : is) {
					p.getWorld().dropItem(b.getLocation(), i);
					p.getWorld().dropItem(b.getLocation(), i);
					p.getWorld().dropItem(b.getLocation(), i);
					// only 3 because of 1 time natural drop by System.
				}
			}
			break;
		}
	}

}
