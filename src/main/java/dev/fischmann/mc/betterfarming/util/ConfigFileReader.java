package dev.fischmann.mc.betterfarming.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigFileReader {
	
	public static String read(String pluginName, String fileName, String defaultText) throws IOException {
		File dir = new File("./plugins/" + pluginName);
				
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		Path path = Paths.get(dir.getPath() + "/" + fileName);

		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			writeExampleFile(path, defaultText);
		}
		return Files.readString(path);
	}

	private static void writeExampleFile(Path path, String defaultText) throws IOException {
		Files.writeString(path, defaultText, StandardOpenOption.CREATE);
	}
	
}
