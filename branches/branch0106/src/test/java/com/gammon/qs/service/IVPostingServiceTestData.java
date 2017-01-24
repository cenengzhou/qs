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
public class IVPostingServiceTestData extends TransactionTestCase.TestDataBase {
	
	/**
	 * Test method for {@link com.gammon.qs.service.IVPostingService#postIVAmounts(com.gammon.qs.domain.Job, java.lang.String, boolean)}.
	 */
	public TransactionTestCase.TestDataMethod testPostIVAmountsJobStringBoolean() {
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
	 * Test method for {@link com.gammon.qs.service.IVPostingService#postIVAmounts(com.gammon.qs.domain.Job, java.lang.String, java.util.Date, boolean)}.
	 */
	public TransactionTestCase.TestDataMethod testPostIVAmountsJobStringDateBoolean() {
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
	 * Test method for {@link com.gammon.qs.service.IVPostingService#getNextIndexNumber(com.gammon.qs.domain.Job, java.lang.String, java.lang.Integer)}.
	 */
	public TransactionTestCase.TestDataMethod testGetNextIndexNumber() {
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
