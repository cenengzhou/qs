/**
 * koeyyeung
 * Jun 6, 20134:21:52 PM
 * 
 * Henry Lai
 * modified on 17-Nov-2014
 */
package com.gammon.qs.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.QuartzConfig;
import com.gammon.qs.client.repository.PropertiesRepositoryRemote;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class PropertiesRepositoryController extends GWTSpringController implements PropertiesRepositoryRemote {

	private static final long serialVersionUID = 2275235568497988368L;
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private QuartzConfig quartzConfig;
	@Autowired
	private ApplicationConfig applicationConfig;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	@Override
	public String obtainMailReceiverAddress() {
		return quartzConfig.getMailReceiverAddress();
	}
	
	@Override
	public Boolean updateMailReceiverAddress(String mailAddress) {
		Boolean updated = false;
		
		//save to property file physically
		File configFile = new File(applicationConfig.getQuartzProperties());
		logger.info("Application Context Properties Path: "+configFile);
		try {
			
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			
			props.setProperty("mail.receiver.address", mailAddress);
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "MODIFIED: mail.receiver.address");
		    writer.close();
			
//		    //set run time
		    quartzConfig.setMailReceiverAddress(mailAddress);
		   
		    logger.info("Modified emailReceiverAddress: "+quartzConfig.getMailReceiverAddress());
		    updated = true;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
				
		updated = true;
		return updated;
	}
	
}
