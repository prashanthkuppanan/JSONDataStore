package com.datastore.json;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllConstraintTester.class, ConcurrencyTest.class, DataStoreSpaceTest.class })
public class AllTests {

}
