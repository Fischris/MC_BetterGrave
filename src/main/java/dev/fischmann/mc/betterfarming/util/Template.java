package dev.fischmann.mc.betterfarming.util;

import dev.fischmann.mc.betterfarming.constants.Direction;
import dev.fischmann.mc.betterfarming.constants.Setting;

public enum Template {
	LANGUAGEFILE(
			"#First language will be fallback default language\n"
					+ "#If a language code is missing - for example en_gb, the plugin tries to finde a code starting with en.\n"
					+ "#Example: language_code:languagelabel=valueOfLanguage\n"
					+ "\n"
					+ "en_us:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=Alignment of the tool is now horizontal.\n"
					+ "en_us:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=Alignment of the tool is now vertical.\n"
					+ "en_us:" + Direction.TOOL_NONE.getLanguageLabel() + "=Single block farming.\n"
					+ "en_us:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Tree cutting mode active.\n"
					+ "\n"
					+ "de_de:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=Ausrichtung des Werkzeuges ist jetzt waagerecht.\n"
					+ "de_de:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=Ausrichtung des Werkzeuges ist jetzt vertikal.\n"
					+ "de_de:" + Direction.TOOL_NONE.getLanguageLabel() + "=Einfacher Block Abbau.\n"
					+ "de_de:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Baumfällmodus aktiv.\n"
					+ "\n"
					+ "es_es:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=La alineación de la herramienta es ahora horizontal.\n"
					+ "es_es:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=La alineación de la herramienta es ahora vertical.\n"
					+ "es_es:" + Direction.TOOL_NONE.getLanguageLabel() + "=Desmontaje de bloques individuales."
					+ "es_es:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Modo de tala de árboles activado.\n"
					+ "\n"
					+ "fr_fr:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=L'orientation de l'outil est maintenant horizontale.\n"
					+ "fr_fr:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=L'orientation de l'outil est désormais verticale.\n"
					+ "fr_fr:" + Direction.TOOL_NONE.getLanguageLabel() + "=Bloc unique.\n"
					+ "fr_fr:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Mode d'abattage des arbres activé.\n"
					+ "\n"
					+ "it_it:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=L'allineamento dell'utensile è ora orizzontale.\n"
					+ "it_it:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=L'orientamento dello strumento è ora verticale.\n"
					+ "it_it:" + Direction.TOOL_NONE.getLanguageLabel() + "=Smontaggio dei singoli blocchi.\n"
					+ "it_it:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Impostazione per l'abbattimento degli alberi.\n"
					+ "\n"
					+ "pt_pt:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=A orientação da ferramenta é agora horizontal.\n"
					+ "pt_pt:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=A orientação da ferramenta é agora vertical.\n"
					+ "pt_pt:" + Direction.TOOL_NONE.getLanguageLabel() + "=Desmantelamento de blocos individuais.\n"
					+ "pt_pt:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Modo de abate de árvores activado.\n"
					+ "\n"
					+ "ru_ru:" + Direction.TOOL_HORIZONTAL.getLanguageLabel() + "=Ориентация инструмента теперь горизонтальная.\n"
					+ "ru_ru:" + Direction.TOOL_VERTICAL.getLanguageLabel() + "=Ориентация инструмента теперь вертикальная.\n"
					+ "ru_ru:" + Direction.TOOL_NONE.getLanguageLabel() + "=Демонтаж отдельных блоков.\n"
					+ "ru_ru:" + Direction.TOOL_TREE_FARMING.getLanguageLabel() + "=Активирован режим валки деревьев.\n"
	),

	SETTINGSFILE(Setting.PROBABILITY_PICKAXE.getLanguageLabel() + "=10\n"
			+ Setting.OBSIDIAN_VALID_BLOCK.getLanguageLabel() + "=false"),
	
	GENEARLSETTINGINFO(
			"""
					#Lines starting with # will be ignored
					#Value always after first '='
					#multi-keys devided by ':'
					""");
	

	private final String value;

	Template(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
