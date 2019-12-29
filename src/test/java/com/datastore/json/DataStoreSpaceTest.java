package com.datastore.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataStoreSpaceTest {
	
	@BeforeClass
	public static void setupClass() {
		JsonDataStore.setDsLimit(1000); // 1 MB as dataStore for testing purpose
		JsonDataStore.setKeyExpireTimeout(100);
	}
	@Test
	public void testSpaceWithSmall() {
		String key = "key"+Math.random();
		String value = "{\"asfsadfs\" : \"ssfsdfs\"}";
		try {
			JsonDataStore.create(key , value , 0);
			assertEquals(JsonDataStore.get(key), value);
		} catch (DataStoreException | IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSpaceWithLarge() throws DataStoreException, IOException {
		String key = null;
		try {
			for ( int i=0; i < 100; i++) {
				key = "key"+Math.random();
				String value = FileReader.readAsString(".\\sample.json");
				JsonDataStore.create(key, value, 0);
			}
			fail("The file should not be created");
		} catch (DataStoreException | IOException e) {
			assertEquals(e.getMessage(), "No more Space for Key "+key);
		}
	}

}
