package dev.fischmann.mc.betterfarming.constants;

public enum Direction {
	TOOL_NONE("tool_direction_none"), TOOL_HORIZONTAL("tool_direction_horizontal"),
	TOOL_VERTICAL("tool_direction_vertical"), TOOL_TREE_FARMING("tool_direction_tree_farming");

	private final String languageLabel;

	Direction(String languageLabel) {
		this.languageLabel = languageLabel;
	}

	public String getLanguageLabel() {
		return languageLabel;
	}
}