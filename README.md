# Json Data Store 
	This key-value data store will store the key and Json value. 
	This supports the create, read and update the key atomically meaning the operations are consistent.
	Below are the features as following.
	1. JSON Data store will have default 1 GB as the space limit. Although, you can configure the default Space limit as you required.
	2. The key will have default 5 minutes for key expiration time. 
	   You cannot access the key after 5 minutes, along you can change while configuring the data store.
	3. The file system path is relative to this project. *DB* folder for storing the key-value pairs, *temp* folder 
		will be used to store the file temporarily to check the space in the Datasource. 
	4. The key must be string with length not more than 32 length.
	5. The value must be JSON and should not exceed 16 KB size.
	6. The following messages will reported to the end user if any operations leads to error:
		a. Inserting the same key - ***Key {} is already exists***
		b. Inserting the Non-Json value - ***Not a valid Json, Please provide valid json***
		c. Inserting large JSON value or value if inserts exceed the data store max size - 	***No more Space for Key {}***
		d. Inserting the JSON value exceed the capped size 16 KB - ***Value is more than max json size.***
		e. Getting or deleting expired key or key not exists - ***Key {} is not present***

# Pre-requistics
	* Please make sure, you installed the Maven 3.0+
	* Need JDK 8

# Running the application
You can run the client for using this application. Below is the command to run the client
>	mvn exec:java -Dexec.mainClass="com.datastore.json.Client"

# Running the Junit suite
You can run the test suites for testing the data store. This test suite has following test cases.

	1. AllConstraintTester
	2. ConcurrencyTest
	3. DataStoreSpaceTest
	
>   mvn clean install test -Dtest=\*\*\/AllTests -DfailIfNoTests=false
