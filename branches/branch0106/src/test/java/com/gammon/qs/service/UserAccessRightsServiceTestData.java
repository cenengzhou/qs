/**
 * 
 */
package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.gammon.junit.testcase.TransactionTestCase;

/**
 * @author paulyiu
 *
 */
@Configuration
public class UserAccessRightsServiceTestData extends TransactionTestCase.TestDataBase {
	
	/**
	 * Test method for {@link com.gammon.qs.service.UserAccessRightsService#getAccessRights(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testGetAccessRights() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

	/**
	 * Test method for {@link com.gammon.qs.service.UserAccessRightsService#obtainAccessRightsByUserLists(java.util.List, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainAccessRightsByUserLists() {
		return new TransactionTestCase.TestDataMethod() {

			@Override
			public Object obtainObjectFromDB() {
				return null;
			}

			@Override
			public Object obtainDataForCompare() {
				List<Object> data = new ArrayList<Object>();
//				SCDetails tempObj = (SCDetails) obtainObjectFromDB();
//				data.add(tempObj.getCumCertifiedQuantity());
				return data;
			}

			@Override
			public void executeTestMethod() {
				
			}
			
		};
	}

}
