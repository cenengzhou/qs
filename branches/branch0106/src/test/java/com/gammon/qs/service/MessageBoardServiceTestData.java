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
public class MessageBoardServiceTestData extends TransactionTestCase.TestDataBase {
	
	/**
	 * Test method for {@link com.gammon.qs.service.MessageBoardService#obtainAllDisplayMessages()}.
	 */
	public TransactionTestCase.TestDataMethod testObtainAllDisplayMessages() {
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
	 * Test method for {@link com.gammon.qs.service.MessageBoardService#obtainDisplayMessagesByType(com.gammon.qs.domain.MessageBoard)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainDisplayMessagesByType() {
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
	 * Test method for {@link com.gammon.qs.service.MessageBoardService#obtainMessageBoardPaginationWrapper(com.gammon.qs.domain.MessageBoard)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainMessageBoardPaginationWrapper() {
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
	 * Test method for {@link com.gammon.qs.service.MessageBoardService#obtainMessageBoardListByPage(int)}.
	 */
	public TransactionTestCase.TestDataMethod testObtainMessageBoardListByPage() {
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
	 * Test method for {@link com.gammon.qs.service.MessageBoardService#updateMessages(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testUpdateMessages() {
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
	 * Test method for {@link com.gammon.qs.service.MessageBoardService#deleteMessages(java.util.List)}.
	 */
	public TransactionTestCase.TestDataMethod testDeleteMessages() {
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
