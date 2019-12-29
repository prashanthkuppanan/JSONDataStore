package com.datastore.json;

public class DataStoreException extends Exception {

	private static final long serialVersionUID = 3196557633396480672L;

	public DataStoreException(String message, String value) {
		super(message.replaceFirst("\\{\\}", value));
	}

	public DataStoreException(String message) {
		super(message);
	}
}
