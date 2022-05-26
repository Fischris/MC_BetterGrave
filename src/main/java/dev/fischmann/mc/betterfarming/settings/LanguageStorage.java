package dev.fischmann.mc.betterfarming.settings;

import java.util.HashMap;
import java.util.Map.Entry;

public class LanguageStorage extends Storage {

	public static final LanguageStorage INSTANCE = new LanguageStorage();

	private LanguageStorage() {
	};

	HashMap<String, HashMap<String, String>> languageLabelValueMap = new HashMap<>();
	private String defaultLanguageCode = "en_us";
	
	public void reset() {
		languageLabelValueMap.clear();
		defaultLanguageCode = "en_us";
	}
	
	protected void putRecord(String line) throws Exception {

		String languageCode = line.substring(0, line.indexOf(':'));
		String label = line.substring(line.indexOf(':') + 1, line.indexOf('='));
		String value = line.substring(line.indexOf('=') + 1);

		if (languageLabelValueMap.isEmpty()) {
			defaultLanguageCode = languageCode;
		}
		
		HashMap<String, String> labelValue = languageLabelValueMap.get(languageCode);
		if (labelValue == null) {
			labelValue = new HashMap<>();
			languageLabelValueMap.put(languageCode, labelValue);
		}

		labelValue.put(label, value);
	}

	public String getNearestMatch(String languageCode, String label) {

		HashMap<String, String> labelValue = languageLabelValueMap.get(languageCode);

		if (labelValue == null) {

			String shortCode = languageCode.substring(0, 2);

			for (Entry<String, HashMap<String, String>> entry : languageLabelValueMap.entrySet()) {
				String key = entry.getKey();

				if (key.startsWith(shortCode)) {
					labelValue = entry.getValue();
					break;
				}
			}

			if (labelValue == null) {
				if (defaultLanguageCode.equals(languageCode)) {
					return "";
				}
				return getNearestMatch(defaultLanguageCode, label);
			}
		}

		String value = labelValue.get(label);

		if (value == null) {
			if (defaultLanguageCode.equals(languageCode)) {
				return "";
			}
			return getNearestMatch(defaultLanguageCode, label);
		}
		return value;
	}
}
