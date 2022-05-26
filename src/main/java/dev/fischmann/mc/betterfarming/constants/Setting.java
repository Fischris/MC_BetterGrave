package dev.fischmann.mc.betterfarming.constants;

public enum Setting {
	PROBABILITY_PICKAXE("probability_for_pickaxe_percent"),
	OBSIDIAN_VALID_BLOCK("obsidian_valid_farmer_block");

	private final String languageLabel;

	Setting(String languageLabel) {
		this.languageLabel = languageLabel;
	}

	public String getLanguageLabel() {
		return languageLabel;
	}
}