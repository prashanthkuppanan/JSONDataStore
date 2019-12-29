package com.datastore.json;

import java.io.IOException;
import java.util.Scanner;

public class Client {
	private static Scanner stdin = new Scanner(System.in);

	public static void main(String[] args) {
		configureDataStore();
		int choice = displayChoice();
		while (choice <= 3 && choice > 0) {
			String key;
			String value;
			int timeoutSeconds;
			switch (choice) {
			case 1:
				System.out.println(" Enter a Key :");
				key = stdin.next();
				stdin.nextLine();
				System.out.println(" Enter a Value (JSON):");
				value = stdin.nextLine();
				System.out.println(" Enter the expiry time in seconds (Optional): ");
				timeoutSeconds = stdin.nextInt();
				try {
					JsonDataStore.create(key, value, timeoutSeconds);
					System.out.println("Key is created ");
				} catch (DataStoreException | IOException e) {
					System.out.println("EXCEPTION " + e.getMessage());
				}
				break;
			case 2:
				System.out.println(" Enter a Key :");
				key = stdin.next();
				try {
					System.out.println(JsonDataStore.get(key));
				} catch (DataStoreException | IOException e) {
					System.out.println("Exception while getting " + e.getMessage());
				}
				break;
			case 3:
				System.out.println(" Enter a Key :");
				key = stdin.next();
				try {
					if (JsonDataStore.delete(key)) {
						System.out.println(key + " is successfully deleted");
					} 
				} catch (DataStoreException e) {
					System.out.println("Exception while deleting " + e.getMessage());
				}
				break;
			default:
				break;
			}
			choice = displayChoice();
		}
	}

	private static void configureDataStore() {
		System.out.println("Please Enter the Maximum Space limit in KB (Empty for Default 1 GB): ");
		JsonDataStore.setDsLimit(stdin.nextLong());
		System.out.println("Please Enter the key expiration time in seconds (Empty for Default 5 mins): ");
		JsonDataStore.setKeyExpireTimeout(stdin.nextInt());
	}

	private static int displayChoice() {
		System.out.println("Please enter your choice");
		System.out.println("1. Create a Key");
		System.out.println("2. Get value for a key");
		System.out.println("3. Delete a key");
		System.out.println("4. Exit");
		return stdin.nextInt();
	}
}
