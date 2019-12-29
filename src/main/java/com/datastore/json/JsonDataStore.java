package com.datastore.json;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 	This key-value data store will store the key and Json value. 
 *	This supports the create, read and update the key atomically meaning the operations are consistent.
 *	Below are the features as following.
 *	1. JSON Data store will have default 1 GB as the space limit. Although, you can configure the default Space limit as you required.
 *	2. The key will have default 5 minutes for key expiration time. 
	   You cannot access the key after 5 minutes, along you can change while configuring the data store.
	3. The file system path is relative to this project. *DB* folder for storing the key-value pairs, *temp* folder 
		will be used to store the file temporarily to check the space in the Datasource. 
	4. The key must be string with length not more than 32 length.
	5. The value must be JSON and should not exceed 16 KB size.
	6. The following messages will reported to the end user if any operations leads to error:
		a. Inserting the same key - ***Key {} is already exists
		b. Inserting the Non-Json value - ***Not a valid Json, Please provide valid json
		c. Inserting large JSON value or value if inserts exceed the data store max size - 	***No more Space for Key {}
		d. Inserting the JSON value exceed the capped size 16 KB - ***Value is more than max json size.
		e. Getting or deleting expired key or key not exists - ***Key {} is not present
 * 
 * @author prashanth kuppanan
 *
 */
public class JsonDataStore extends DataStore{
	private static Lock writeRead = new ReentrantLock();
	private static final long MAX_JSON_LIMIT = 16000; // 16 KB
	private static final long MAX_KEY_LEN = 32; // 32 chars

	static {
		try {
			Utils.cleanDB();
			Utils.cleanTemp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void create(String key, String value, int timeoutSeconds) throws DataStoreException, IOException {
		if (key.length() > MAX_KEY_LEN) {
			throw new DataStoreException("Key Length {} is more than allowed 32 chars length",
					String.valueOf(key.length()));
		}
		writeRead.lock();
		try {
			if (Utils.isKeyExists(key)) {
				throw new DataStoreException("Key {} is already exists", key);
			}
			File tempFile = Utils.createTempFile(key, value.getBytes());
			if (tempFile.length() > MAX_JSON_LIMIT) {
				throw new DataStoreException("Value is more than max json size.");
			}
			if (!Utils.validateJson(value)) {
				throw new DataStoreException("Not a valid Json, Please provide valid json", key);
			}
			if (!Utils.haveSpaceForFile(tempFile, dsLimit)) {
				throw new DataStoreException("No more Space for Key {}", key);
			}
			Utils.moveFileFromTemp(key);
			Utils.cleanTemp();
			new Timer().schedule(new EvictTask(key),
					timeoutSeconds == 0 ? keyExpireTimeout : timeoutSeconds * 1000);

		} finally {
			writeRead.unlock();
		}

	}

	public static boolean delete(String key) throws DataStoreException {
		boolean deleteSuccess;
		writeRead.lock();
		try {
			File keyValue = new File(Utils.folderPath + key + Utils.extn);
			if (keyValue.exists()) {
				deleteSuccess = Utils.deleteFile(keyValue);
			} else {
				throw new DataStoreException("Key {} is not present", key);
			}
		} finally {
			writeRead.unlock();
		}
		return deleteSuccess;
	}

	public static String get(String key) throws DataStoreException, IOException {
		writeRead.lock();
		try {
			File file = new File(Utils.folderPath + key + Utils.extn);
			if (file.exists()) {
				return Utils.readFile(key);
			} else {
				throw new DataStoreException("Key {} is not present", key);
			}
		} finally {
			writeRead.unlock();
		}
	}
}
