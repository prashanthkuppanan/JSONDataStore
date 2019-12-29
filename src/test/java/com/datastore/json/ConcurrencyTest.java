package com.datastore.json;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConcurrencyTest {
	public static Vector<String> list = new Vector<>();
	private static Lock testAtomically = new ReentrantLock();
	
	@BeforeClass
	public static void setup() {
		JsonDataStore.setDsLimit(3000); // 3 MB as dataStore
		JsonDataStore.setKeyExpireTimeout(60); // 1 min
	}
	
	@Test
	public void testCreateOperationWith10Threads() {
		testAtomically.lock();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for(int i=0; i < 10; i++) {
			executor.execute(new CreateRunnable());
		}
		executor.shutdown();
		while (!executor.isTerminated()) {}
		testAtomically.unlock();
		assertEquals(list.size(), 10);
	}
	
	@Test
	public void testGetOperationWith10Threads() {
		testAtomically.lock();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for(int i=0; i < list.size(); i++) {
			GetRunnable getThread = new GetRunnable();
			getThread.setKey(list.get(i));
			executor.execute(getThread);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {}
		testAtomically.unlock();
		assertEquals(list.size(), 10);
	}
	
	@AfterClass
	public static void tearDownClass() throws IOException {
		Utils.cleanDB();
		Utils.cleanTemp();
	}
}

class CreateRunnable implements Runnable {
	
	@Override
	public void run() {
		try {
			String key = "Key"+Math.random();
			ConcurrencyTest.list.add(key);
			JsonDataStore.create(key , ""+Math.random(), 0);
		} catch (DataStoreException | IOException e) {
			e.printStackTrace();
		}
	}
	
}

class GetRunnable implements Runnable {
	private String key;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void run() {
		try {
			JsonDataStore.get(key);
		} catch (DataStoreException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
