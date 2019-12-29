package com.datastore.json;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileReader {

	public static String readAsString(String path) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(path));
		int read = reader.read();
		StringBuilder builder = new StringBuilder();
		while (read != -1) {
			builder.append((char) read);
			read = reader.read();
		}
		reader.close();
		return builder.toString();
	}

}

