/**
 * 
 */
package com.gammon.qs.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.gammon.junit.testcase.TransactionTestCase;

/**
 * @author paulyiu
 *
 */
@Configuration
public class ActiveSessionServiceTestData extends TransactionTestCase.TestDataBase {
	
	
	/**
	 * Test method for {@link com.gammon.qs.service.admin.ActiveSessionService#cleanUpLastShutdown()}.
	 */
	public TransactionTestCase.TestDataMethod testCleanUpLastShutdown() {
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
	 * Test method for {@link com.gammon.qs.service.admin.ActiveSessionService#addOrUpdateActiveSession(com.gammon.qs.application.ActiveSession)}.
	 */
	public TransactionTestCase.TestDataMethod testAddOrUpdateActiveSession() {
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
	 * Test method for {@link com.gammon.qs.service.admin.ActiveSessionService#removeActiveSession(java.lang.String, java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRemoveActiveSessionStringString() {
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
	 * Test method for {@link com.gammon.qs.service.admin.ActiveSessionService#removeActiveSession(java.lang.String)}.
	 */
	public TransactionTestCase.TestDataMethod testRemoveActiveSessionString() {
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
	 * Test method for {@link com.gammon.qs.service.admin.ActiveSessionService#getActiveSessions()}.
	 */
	public TransactionTestCase.TestDataMethod testGetActiveSessions() {
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
