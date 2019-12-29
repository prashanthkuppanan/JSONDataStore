package com.datastore.json;

public abstract class DataStore {
	protected static long dsLimit = 1000000; // 1 GB in KB
	protected static int keyExpireTimeout = 300; // 5 minutes
	
	public static long getDsLimit() {
		return dsLimit;
	}

	public static void setDsLimit(long newDSLimit) {
		dsLimit = newDSLimit == 0 ? dsLimit : newDSLimit;
	}

	public static int getKeyExpireTimeout() {
		return keyExpireTimeout;
	}

	public static void setKeyExpireTimeout(int keyExpireTimeoutInSec) {
		keyExpireTimeout = keyExpireTimeoutInSec == 0 ? keyExpireTimeout * 1000
				: keyExpireTimeoutInSec * 1000;
	}
}
