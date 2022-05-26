package dev.fischmann.mc.betterfarming;

import org.bukkit.entity.Player;

import dev.fischmann.mc.betterfarming.settings.LanguageStorage;

public class Message {

	public static void send(Player p, String languageLabel) {
		String pluginLabel = Main.label;
		String languageCode = p.getLocale();

		String text = LanguageStorage.INSTANCE.getNearestMatch(languageCode, languageLabel);

		p.sendMessage(pluginLabel + text);
	}
}
