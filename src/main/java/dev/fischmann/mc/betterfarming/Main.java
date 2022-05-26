package dev.fischmann.mc.betterfarming;

import java.io.IOException;

import dev.fischmann.mc.betterfarming.constants.Enchants;
import dev.fischmann.mc.betterfarming.constants.NamespacedKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.fischmann.mc.betterfarming.settings.LanguageStorage;
import dev.fischmann.mc.betterfarming.settings.SettingsStorage;
import dev.fischmann.mc.betterfarming.util.ConfigFileReader;
import dev.fischmann.mc.betterfarming.util.Template;

public class Main extends JavaPlugin{
	
	public static String pluginName = "BetterFarming";
	public static String label = ChatColor.GREEN+"["+ChatColor.WHITE+ pluginName +ChatColor.GREEN+"] "+ChatColor.WHITE;
	FarmerEvents dbe;
	EnchantmentEvents enchantmentEvents; 

	public void onEnable(){
		NamespacedKeys.FARMER_UUID.setPlugin(this);

		loadData();

		dbe = new FarmerEvents(this);
		enchantmentEvents = new EnchantmentEvents(this);
		
		Bukkit.getPluginManager().registerEvents(dbe, this);
		Bukkit.getPluginManager().registerEvents(enchantmentEvents, this);
		
		Bukkit.getConsoleSender().sendMessage("Das " + label + " wurde aktiviert!");	
	}
	
	
	public void onDisable()
	{
		Bukkit.getConsoleSender().sendMessage("Das "+ label + " wurde deaktiviert!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
			if (!(sender instanceof Player) ||  sender.isOp()) {
				
				if ("betterfarming".equalsIgnoreCase(label)) {
					
					if (args.length > 0) {
						
						if ("force".equalsIgnoreCase(args[0] )) {
							
							if (args.length > 1) {
								if ("false".equalsIgnoreCase(args[1] )) {
									sender.sendMessage(Main.label + "Force farming enchantment is set to false.");
									return true;
								}
							}
							
							enchantmentEvents.forceEnchant = true;
							sender.sendMessage(Main.label + "Next farming enchantment is forced.");
							return true;
						}

						if ("add".equalsIgnoreCase(args[0])) {
							if (args.length > 1) {
								addFarmerToPlayersMainhand(args[1]);
							} else {
								sender.sendMessage(label + "Playername needed.");
							}
							return true;
						}


						if ("reload".equalsIgnoreCase(args[0])) {
							resetData();
							sender.sendMessage(Main.label + "Reloaded config files.");
							return true;
						}
						


						sender.sendMessage(label + "Args needed.");
					}
				}
			}
		
	  return false;
	}
	

	 boolean addFarmerToPlayersMainhand (String playerName){
		Player p = getServer().getPlayer(playerName);

		if (p == null) {
			return false;
		}

		ItemStack mainHand = p.getInventory().getItemInMainHand();

		if (Tools.isValidToEnchantTool(mainHand)){
			Enchants.FARMERLORE.add(mainHand);
			return true;
		}
		return false;
	}

	private void resetData() {
		LanguageStorage.INSTANCE.reset();
		SettingsStorage.INSTANCE.reset();
		loadData();
	}
	
	private void loadData() {
		
		try {
			String languageConfig = ConfigFileReader.read(pluginName, "language.config", Template.GENEARLSETTINGINFO.getValue() + Template.LANGUAGEFILE.getValue());
			LanguageStorage.INSTANCE.addRecord(languageConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			String settings = ConfigFileReader.read(pluginName, "settings.config", Template.GENEARLSETTINGINFO.getValue() +  Template.SETTINGSFILE.getValue());
			SettingsStorage.INSTANCE.addRecord(settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
