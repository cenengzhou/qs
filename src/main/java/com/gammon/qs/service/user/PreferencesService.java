package com.gammon.qs.service.user;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.application.UserHBDao;
import com.gammon.qs.web.formbean.GeneralPreferencesBean;
@Service
@Transactional(rollbackFor = Exception.class)
public class PreferencesService{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private UserHBDao userDao;
	
	public void saveGeneralPreferences(String username, GeneralPreferencesBean generalPreferencesBean) throws DatabaseOperationException{
		try {
			User user = userDao.getByUsername(username);
			Map<String, String> generalPreferences = user.getGeneralPreferences();
			
			generalPreferences.remove(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
			generalPreferences.remove(GeneralPreferencesKey.DEFAULT_JOB);
			generalPreferences.remove(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
			generalPreferences.remove(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
			userDao.saveOrUpdate(user);
			
			user = userDao.getByUsername(username);
			generalPreferences = user.getGeneralPreferences();
			if (!GenericValidator.isBlankOrNull(generalPreferencesBean.getAmountDecimalPlaces())) {
				generalPreferences.put(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES, generalPreferencesBean.getAmountDecimalPlaces());
			} else {
				generalPreferences.remove(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
			}
			if (!GenericValidator.isBlankOrNull(generalPreferencesBean.getDefaultJob())) {
				generalPreferences.put(GeneralPreferencesKey.DEFAULT_JOB, generalPreferencesBean.getDefaultJob());
			} else {
				generalPreferences.remove(GeneralPreferencesKey.DEFAULT_JOB);
			}
			if (!GenericValidator.isBlankOrNull(generalPreferencesBean.getQuantityDecimalPlaces())) {
				generalPreferences.put(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES, generalPreferencesBean.getQuantityDecimalPlaces());
			} else {
				generalPreferences.remove(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
			}
			if (!GenericValidator.isBlankOrNull(generalPreferencesBean.getRateDecimalPlaces())) {
				generalPreferences.put(GeneralPreferencesKey.RATE_DECIMAL_PLACES, generalPreferencesBean.getRateDecimalPlaces());
			} else {
				generalPreferences.remove(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
			}
	
			userDao.saveOrUpdate(user);
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE, "Database operation failure in saving general preferences for " + username, ex);
		}
		
	}
	

}
