package com.datastore.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	
	public static final String folderPath = ".\\db\\";
	public static final String extn = ".json";
	public static final String tempStore = ".\\temp\\";
	
	public static boolean haveSpaceForFile(File tempFile, long dbLimit) throws IOException {
		double tempFileSize = Math.ceil(tempFile.length() + getSize())/1024;
		return tempFileSize <= dbLimit;
	}
	
	public static long getSize() throws IOException {
		Path folder = Paths.get(folderPath);
		return Files.walk(folder).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum();
	}
	
	public static void moveFileFromTemp(String key) throws IOException {
		Files.move(Paths.get(tempStore, key+extn), Paths.get(folderPath, key+extn)); 
	}
	
	public static File createTempFile(String key, byte[] bytes) throws IOException {
		File temp = new File(tempStore + key + extn);
		temp.createNewFile();
		OutputStream outputStream = new FileOutputStream(tempStore + key + extn);
		outputStream.write(bytes);
		outputStream.close();
		return temp;
	}
	
	public static String readFile(String key) throws IOException {
		InputStream inputStream = new FileInputStream(Utils.folderPath + key + Utils.extn);
		Reader reader = new InputStreamReader(inputStream);
		int read = reader.read();
		StringBuilder builder = new StringBuilder();
		while (read != -1) {
			builder.append((char) read);
			read = reader.read();
		}
		reader.close();
		return builder.toString();
	}
	
	public static boolean deleteFile(File file) {
		return file.delete();
	}
	
	public static boolean isKeyExists(String key) {
		return new File(folderPath+ key + extn).exists();
	}

	public static void cleanDB() throws IOException {
		if (Files.exists(Paths.get(folderPath))) {
			Files.walk(Paths.get(folderPath)).forEach(p -> p.toFile().delete());
		} 
		if (!Files.exists(Paths.get(folderPath))) {
			Files.createDirectory(Paths.get(folderPath));
		}
	}
	
	public static boolean validateJson(String value) {
		ObjectMapper objMapper = new ObjectMapper();
		try {
			objMapper.readTree(value);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static void cleanTemp() throws IOException {
		if (Files.exists(Paths.get(tempStore))) {
			Files.walk(Paths.get(tempStore)).forEach(p -> p.toFile().delete());
		}
		if (!Files.exists(Paths.get(tempStore))) {
			Files.createDirectory(Paths.get(tempStore));
		}
	}
}
