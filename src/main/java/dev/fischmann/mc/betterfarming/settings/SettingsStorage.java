package dev.fischmann.mc.betterfarming.settings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import dev.fischmann.mc.betterfarming.constants.Setting;

public class SettingsStorage extends Storage {

	public static final SettingsStorage INSTANCE = new SettingsStorage();

	private SettingsStorage() {
	};

	private HashMap<Setting, Integer> settingValue = new HashMap<>();
	
	public void reset() {
		settingValue.clear();
	}

	protected void putRecord(String line) throws Exception {

		final String key = line.substring(0, line.indexOf('='));
		String value = line.substring(line.indexOf('=') + 1);

		Optional<Setting> settingkey = Arrays.stream(Setting.values()).filter(s -> key.equals(s.getLanguageLabel()))
				.findFirst();

		if (settingkey.isEmpty()) {
			throw new Exception("Wrong key spelling: " + key);
		}

		if (value.toLowerCase().contains("true")) {
			settingValue.put(settingkey.get(), 1);
			return;
		}

		if (value.toLowerCase().contains("false")) {
			settingValue.put(settingkey.get(), 0);
			return;
		}

		value = value.replaceAll("[^0-9]+", "");
		Integer iValue = Integer.parseInt(value);

		settingValue.put(settingkey.get(), iValue);
	}

	public Integer getValueOf(Setting setting) {
		return settingValue.get(setting);
	}

}
