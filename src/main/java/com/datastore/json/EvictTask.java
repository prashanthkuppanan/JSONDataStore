package com.datastore.json;

import java.util.TimerTask;

public class EvictTask extends TimerTask{

	private String key;
	
	EvictTask(String key) {
		this.key = key;
	}
	
	@Override
	public void run() {
		if(key != null && key.length() > 0) {
			try {
				JsonDataStore.delete(key);
			} catch (DataStoreException e) {
				e.printStackTrace();
			}
		}
	}

}
