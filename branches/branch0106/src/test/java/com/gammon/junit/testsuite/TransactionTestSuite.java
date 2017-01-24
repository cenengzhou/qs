package com.gammon.junit.testsuite;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ActiveProfiles;

import com.gammon.junit.testcase.TransactionTestCase;

@RunWith(Suite.class)
@SuiteClasses(value = {  TransactionTestCase.class }) 
@ActiveProfiles("junit")
public class TransactionTestSuite {
	@SuppressWarnings("rawtypes")
	public static Collection getTestData() {
		return Arrays.asList(new Object[][] { 
//			{PackageServiceTestData.class, "testUpdateWDandCertQuantity"},
		});
	}
}
