package com.gammon.qs.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.InsertSingleSignOnKeyF58100Manager.insertSingleSignOnKeyF58100.GetSingleSignOnKeyRequestObj;
import com.gammon.jde.webservice.serviceRequester.InsertSingleSignOnKeyF58100Manager.insertSingleSignOnKeyF58100.GetSingleSignOnKeyResponseObj;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Repository
public class SingleSignOnKeyWSDao{
	@Autowired
	@Qualifier("getSingleSignOnKeyWebserviceTemplate")
	private WebServiceTemplate getSingleSignOnKeyWebserviceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	
	private Logger logger = Logger.getLogger(SingleSignOnKeyWSDao.class.getName());
	
	public String getSingleSignOnKey(String description, String userID) throws Exception {
		GetSingleSignOnKeyRequestObj requestObj = new GetSingleSignOnKeyRequestObj();
		//Generate Encrypt Key
		try{
			Cipher ecipher =Cipher.getInstance("AES");
			SecretKey key = KeyGenerator.getInstance("AES").generateKey();
			ecipher.init(Cipher.ENCRYPT_MODE, key);
            // Encode the string into bytes using utf-8
            byte[] utf8 = description.getBytes("UTF8");
            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
           description = DatatypeConverter.printBase64Binary(enc);
		}
		catch(Exception e){
			logger.log(Level.INFO, e.getLocalizedMessage());
		}
		if (description.length()>30){
			description= description.substring(0,30);
		}
		requestObj.setDescription(description);
		requestObj.setUserID(userID);
		
		GetSingleSignOnKeyResponseObj responseObj = (GetSingleSignOnKeyResponseObj)getSingleSignOnKeyWebserviceTemplate.marshalSendAndReceive(
					requestObj,	new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		if (responseObj != null){
			return responseObj.getDescription();
		}
		else
			return null;
	}

}
