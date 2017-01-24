/**
 * 
 */
package com.gammon.qs.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.gammon.junit.testcase.TransactionTestCase;

/**
 * @author paulyiu
 *
 */
@Configuration
public class PreferencesServiceTestData extends TransactionTestCase.TestDataBase {
	
	/**
	 * Test method for {@link com.gammon.qs.service.user.PreferencesService#saveGeneralPreferences(java.lang.String, com.gammon.qs.web.formbean.GeneralPreferencesBean)}.
	 */
	public TransactionTestCase.TestDataMethod testSaveGeneralPreferences() {
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
