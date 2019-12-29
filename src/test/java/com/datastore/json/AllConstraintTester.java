package com.datastore.json;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AllConstraintTester {

	@Before
	public void setUp() throws IOException {
		Utils.cleanDB();
	}

	@BeforeClass
	public static void setupClass() {
		JsonDataStore.setDsLimit(3000); // 3 MB as dataStore for testing purpose
		JsonDataStore.setKeyExpireTimeout(100);
	}

	@Test
	public void testKeyWithMoreThan32Chars() {
		String key = "1234567890abcdefghijklmnopqrstuvwxyz";
		try {
			JsonDataStore.create(key, "", 0);
		} catch (DataStoreException | IOException e) {
			assertEquals(e.getMessage(), "Key Length " + key.length() + " is more than allowed 32 chars length");
		}
	}

	@Test
	public void testKeyWithMorethanMaxJson() {
		String key = "test123";
		try {
			JsonDataStore.create(key, FileReader.readAsString(".\\max.json"), 0);
		} catch (DataStoreException | IOException e) {
			assertEquals(e.getMessage(), "Value is more than max json size.");
		}
	}

	@Test
	public void testCreateWithExistingKeys() {
		String key = "test123";
		try {
			JsonDataStore.create(key, FileReader.readAsString(".\\sample.json"), 0);
			JsonDataStore.create(key, FileReader.readAsString(".\\sample.json"), 0);
		} catch (DataStoreException | IOException e) {
			assertEquals(e.getMessage(), "Key "+key+" is already exists");
		}
	}
	
	@Test
	public void testCreateWithInValidJson() {
		String key = "test123";
		try {
			JsonDataStore.create(key, "asdfsf", 0);
		} catch (DataStoreException | IOException e) {
			assertEquals(e.getMessage(), "Not a valid Json, Please provide valid json");
		}
	}
	
	@Test
	public void testCreateWithExpiredKey() throws InterruptedException {
		String key = "test123";
		try {
			JsonDataStore.create(key,  FileReader.readAsString(".\\sample.json"), 5);
			Thread.sleep(5000);
			JsonDataStore.get(key);
		} catch (DataStoreException | IOException e) {
			assertEquals(e.getMessage(), "Key "+ key +" is not present");
		}
	}
	
	@AfterClass
	public static void tearDownClass() throws IOException {
		Utils.cleanDB();
		Utils.cleanTemp();
	}
}
