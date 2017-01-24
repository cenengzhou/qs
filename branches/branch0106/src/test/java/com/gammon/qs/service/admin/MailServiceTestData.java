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
public class MailServiceTestData extends TransactionTestCase.TestDataBase {
	
	/**
	 * Test method for {@link com.gammon.qs.service.admin.MailService#sendEmail(com.gammon.qs.wrapper.EmailMessage)}.
	 */
	public TransactionTestCase.TestDataMethod testSendEmail() {
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
