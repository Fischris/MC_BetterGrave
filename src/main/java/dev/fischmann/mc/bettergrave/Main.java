package dev.fischmann.mc.bettergrave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	public static String pluginName = "BetterGrave";
	public static String label = ChatColor.GREEN+"["+ChatColor.WHITE+ pluginName +ChatColor.GREEN+"] "+ChatColor.WHITE;
	Events events;

	public void onEnable(){

		events = new Events(this);
		Bukkit.getPluginManager().registerEvents(events, this);
		Bukkit.getConsoleSender().sendMessage(label + " activated!");
	}
	
	
	public void onDisable()
	{
		Bukkit.getConsoleSender().sendMessage(label + " deactivated!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
			if (!(sender instanceof Player) ||  sender.isOp()) {

			}
		
	  return false;
	}
	
}
