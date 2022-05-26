package dev.fischmann.mc.betterfarming;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import dev.fischmann.mc.betterfarming.constants.Enchants;
import dev.fischmann.mc.betterfarming.constants.Setting;
import dev.fischmann.mc.betterfarming.settings.SettingsStorage;

public class EnchantmentEvents implements Listener {

	Plugin plugin;
	Random rand;

	public boolean forceEnchant = false;

	public EnchantmentEvents(Plugin plugin) {

		this.plugin = plugin;
		rand = new Random();
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent eie) {

		if (Tools.isValidToEnchantTool(eie.getItem())) {

			if (eie.getExpLevelCost() > 29 || forceEnchant) {
				int i = rand.nextInt(100);

				System.out.println(
						"Spitzhackenverzauberung durch " + eie.getEnchanter().getDisplayName() + ": " + i);

				if (forceEnchant || i < SettingsStorage.INSTANCE.getValueOf(Setting.PROBABILITY_PICKAXE)) {
					forceEnchant = false;

					Enchants.FARMERLORE.add(eie.getItem());
				}
			}
		}
	}

	@EventHandler
	public void onAnvilPrepare(PrepareAnvilEvent e) {
		AnvilInventory ai = e.getInventory();

		boolean containsFarmer = false;

		ItemStack isSlot0 = ai.getItem(0);
		ItemStack isSlot1 = ai.getItem(1);
		ItemStack isResult = ai.getItem(2);

		if (BlockFarming.isFarmerTool(isSlot0)) {
			containsFarmer = true;
		}

		if (BlockFarming.isFarmerTool(isSlot1)) {
			containsFarmer = true;
		}

		/*
		 * this is needed because PrepareAnvilEvent is called 3 times. sometimes, there
		 * will be in the last called event the lore from a event before. if there is a
		 * lore already, it is nevertheless necessary to set the result by hand again,
		 * otherwise the server will be replace it by the default result!
		 */
		if (isResult != null && containsFarmer) {
			ItemStack resultNew = new ItemStack(isResult);
			Enchants.FARMERLORE.remove(resultNew);
			Enchants.FARMERLORE.add(resultNew);
			e.setResult(resultNew);
		}
	}


	@EventHandler
	public void onGrindStoneUse(InventoryClickEvent e) {

		final int RESULTSLOT = 2;

		InventoryView view = e.getView();
		Inventory ivTop = view.getTopInventory();
		
		if (!(ivTop instanceof GrindstoneInventory)) {
			return;
		}

		int rawSlot = e.getRawSlot();
		if (rawSlot == RESULTSLOT) {
			return;
		}

		GrindstoneInventory gi = (GrindstoneInventory) ivTop;
		Player p = (Player) e.getWhoClicked();

		ClickType clickType = e.getClick();

		if (ClickType.SHIFT_LEFT.equals(clickType)) {

			ItemStack slots[] = { gi.getItem(0), gi.getItem(1) };
			if (rawSlot > 2) {

				for (int i = 0; i < slots.length; i++) {
					if (slots[i] == null) {
						Result result = updateGrindStone(gi, e.getCurrentItem(), i);
						if (result.cancelEvent) {
							view.setItem(rawSlot, result.updateCursor);
							p.updateInventory();
							e.setCancelled(true);
						}
					}
				}
			} else {
				Result result = updateGrindStone(gi, null, rawSlot);
				if (result.cancelEvent) {

					int firstEmpty = view.getBottomInventory().firstEmpty();
					if (firstEmpty != -1) {
						view.getBottomInventory().setItem(firstEmpty, result.updateCursor);
					} else {
						view.setCursor(result.updateCursor);
					}
					p.updateInventory();
					e.setCancelled(true);
				}
			}
			return;
		}
 
		if (ClickType.SWAP_OFFHAND.equals(clickType)) {

			if (rawSlot < 2) {
				Result result = updateGrindStone(gi, p.getInventory().getItemInOffHand(), rawSlot);

				if (result.cancelEvent) {
					p.getInventory().setItemInOffHand(result.updateCursor);
					p.updateInventory();
					e.setCancelled(true);
				}
			}
			return;
		}
		
		if (ClickType.NUMBER_KEY.equals(clickType)) {

			if (rawSlot < 2) {
				Inventory btm = view.getBottomInventory();
				int hotbarBtn = e.getHotbarButton();
				Result result = updateGrindStone(gi, btm.getItem(hotbarBtn), rawSlot);

				if (result.cancelEvent) {
					btm.setItem(hotbarBtn, result.updateCursor);
					((Player) e.getWhoClicked()).updateInventory();
					e.setCancelled(true);
				}
			}
			return;
		}

		Inventory iv = e.getClickedInventory();
		
		if (!(iv instanceof GrindstoneInventory)) {
			return;
		}

		ItemStack cursor = view.getCursor();

		Result result = updateGrindStone(gi, cursor, rawSlot);
		if (result.cancelEvent) {
			view.setCursor(result.updateCursor);
			e.setCancelled(true);
		}
	}

	class Result {

		public boolean cancelEvent = false;
		public ItemStack updateCursor = null;

		public Result(boolean cancelEvent) {
			this.cancelEvent = cancelEvent;
		}

		public Result(ItemStack updateCursor) {
			this.cancelEvent = true;
			this.updateCursor = updateCursor;
		}
	}

	private Result updateGrindStone(GrindstoneInventory gi, ItemStack cursor, int rawSlot) {

		ItemStack cursorNew = newItemStackOrAir(gi.getItem(rawSlot));
		ItemStack slots[] = { gi.getItem(0), gi.getItem(1) };

		if (cursor != null && Material.AIR.equals(cursor.getType())) {
			slots[rawSlot] = null;
		} else {
			slots[rawSlot] = cursor;
		}

		if (!(BlockFarming.isFarmerTool(slots[0]) || BlockFarming.isFarmerTool(slots[1]))) { 
			return new Result(false);
		}

		if (slots[0] != null && slots[1] != null && !slots[0].getType().equals(slots[1].getType())) {
			return new Result(false);
		}

		ItemStack result = null;
		int durabilityTotal = 0;
		boolean bonus = true;

		for (ItemStack slot : slots) {

			if (slot == null) {
				bonus = false;
				continue;
			}
			result = new ItemStack(slot);
			durabilityTotal += (result.getType().getMaxDurability() - ((Damageable) result.getItemMeta()).getDamage());
		}

		short max = result.getType().getMaxDurability();
		ItemMeta meta = result.getItemMeta();
		Damageable dmgbl = (Damageable) meta;
		
		if (bonus) {
			durabilityTotal += (int) (0.05F * max);
		}


		if (durabilityTotal > max) {
			durabilityTotal = max;
		}

		dmgbl.setDamage(max - durabilityTotal);
		result.setItemMeta(dmgbl);

		removeAllEnchantements(result);
		Enchants.FARMERLORE.remove(result);

		gi.setItem(rawSlot, cursor);
		gi.setItem(2, result);
		return new Result(cursorNew);

	}
	
	
	private void removeAllEnchantements(ItemStack is) {

		Map<Enchantment, Integer> enchs = is.getEnchantments();
		enchs.entrySet().forEach(set -> is.removeEnchantment(set.getKey()));
	}

	private ItemStack newItemStackOrAir(ItemStack is) {
		return is == null ? new ItemStack(Material.AIR) : new ItemStack(is);
	}

	/*
	 * This needs to be done because of this:
	 * https://www.spigotmc.org/threads/inventoryclickevent-not-always-triggering-
	 * when-clicking-fast.508699/ if you dont click, its not a click, its a drag!
	 * lol
	 */
	@EventHandler
	public void on(InventoryDragEvent e) {
		Inventory iv = e.getInventory();
		if (iv instanceof GrindstoneInventory) {
			GrindstoneInventory gi = (GrindstoneInventory) iv;
			if ((BlockFarming.isFarmerTool(gi.getItem(0)) || BlockFarming.isFarmerTool(gi.getItem(0))
					|| BlockFarming.isFarmerTool(e.getOldCursor()))) {
				e.setCancelled(true);
			}
		}
	}
}
