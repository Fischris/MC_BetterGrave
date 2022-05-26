package dev.fischmann.mc.betterfarming.settings;

import java.util.function.Predicate;

public class Storage {

	private static boolean isComment(String line) {
		return line.startsWith("#");
	}
	private static boolean isEmpty (String line) {
		return line.trim().isEmpty();
	}
	
	public void addRecord(String record) {
		record.lines().filter(Predicate.not(Storage::isComment)).filter(Predicate.not(Storage::isEmpty)).forEach(line -> {
			try {
				putRecord(line);
			} catch (Exception e1) {
				System.err.println("[BetterFarming] Corrupt data in line:' " + line + "'\n");
				e1.printStackTrace();
			}
		});
	}

	public void reset() {}
	
	protected void putRecord(String line) throws Exception {
	}

}
