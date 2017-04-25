package com.gammon.qs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.pcms.dao.AttachmentHBDao;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.Attachment;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.AttachMainCertHBDao;
import com.gammon.qs.dao.AttachPaymentHBDao;
import com.gammon.qs.dao.AttachRepackagingHBDao;
import com.gammon.qs.dao.AttachSubcontractDetailHBDao;
import com.gammon.qs.dao.AttachSubcontractHBDao;
import com.gammon.qs.dao.MainCertHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.RepackagingHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.SubcontractWSDao;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.AttachMainCert;
import com.gammon.qs.domain.AttachPayment;
import com.gammon.qs.domain.AttachRepackaging;
import com.gammon.qs.domain.AttachSubcontract;
import com.gammon.qs.domain.AttachSubcontractDetail;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.scPackage.UploadSCAttachmentResponseObj;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.service.transit.TransitService;

/**
 * koeyyeung
 * Refactored on Mar 21, 201410:45:32 AM
 */
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class AttachmentService {
	@Autowired
	private AttachmentConfig serviceConfig;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private AdminService adminService;
	
	//Repackaging Attachment
	@Autowired
	private AttachRepackagingHBDao repackagingAttachmentDao;
	@Autowired
	private RepackagingHBDao repackagingEntryDao;

	private Repackaging repackagingEntry;

	//Main Contract Certificate Attachment
	@Autowired
	private MainCertHBDao mainContractCertificateHBDaoImpl;
	@Autowired
	private AttachMainCertHBDao mainCertificateAttachmentHBDaoImpl;
	@Autowired
	private MainCertService mainContractCertificateRepository;
	
	//SC Pacakge Attachment (SC, SC Detail, SC Payment)
	@Autowired
	private AttachSubcontractHBDao scAttachmentDao;
	@Autowired
	private AttachPaymentHBDao scPaymentAttachmentDao;
	@Autowired
	private AttachSubcontractDetailHBDao scDetailAttachmentDao;
	//Package
	@Autowired
	private SubcontractHBDao packageHBDao;
	@Autowired
	private SubcontractWSDao packageWSDao;
	//Payment
	@Autowired
	private PaymentCertHBDao scPaymentCertHBDao;
	//Detail
	@Autowired
	private SubcontractDetailHBDao scDetailsHBDao;	
	@Autowired
	private AttachmentHBDao attachmentHBDao;;
	@Autowired
	private AddendumService addendumService;
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private TransitService transitService;
	
	
	private Logger logger = Logger.getLogger(AttachmentService.class.getName());

	/***************************Repackaging Attachment***************************/
	public Integer addRepackagingFileAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName,
			byte[] bytes) throws Exception{
		logger.info("START - addRepackagingFileAttachment");
		logger.info("repackagingEntryID:" + repackagingEntryID + " sequenceNo:" + sequenceNo + " fileName:" + fileName);
		//Timer
		long start = System.currentTimeMillis();
		
		if(repackagingEntryID == null || sequenceNo == null || fileName == null || fileName.length() == 0 || bytes == null || bytes.length == 0)
			return null;
		logger.info("addFileAttachment: repackagingEntryID = " + repackagingEntryID.toString() + ", sequenceNo = " + sequenceNo.toString() + ", fileName = " + fileName);
		if(repackagingEntry == null || !repackagingEntry.getId().equals(repackagingEntryID))
			repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(repackagingEntryID);
		
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String fileDirectory = repackagingEntry.getJobInfo().getJobNumber()+ "\\" + repackagingEntry.getRepackagingVersion()+"\\";
		logger.info("fileDirecotry: "+fileDirectory);
		
		File file = new File(serverPath + fileDirectory);
		if(!file.exists()){
			if(!file.mkdirs())
				throw new Exception("Directory could not be created successfully. fileDirectory: "+fileDirectory);
		}
		
		file = new File(serverPath+fileDirectory + fileName);
		logger.info("Attachment Full Path: "+serverPath+fileDirectory + fileName);
		int i=0;
		String tmpFileName=fileName;
		while(file.exists()){ // check if the file exists, append new file if necessary
			i++;
			int extensionPosition = fileName.lastIndexOf(".");
			tmpFileName = fileName.substring(0,extensionPosition)+"("+i+")" +fileName.substring(extensionPosition , fileName.length());
			file = new File(serverPath+ fileDirectory + tmpFileName );				
		}
		fileName = tmpFileName;
		
		FileOutputStream fileOout = new FileOutputStream(file);
		fileOout.write(bytes);
		fileOout.close();
		
		AttachRepackaging attachment = new AttachRepackaging();
		attachment.setRepackaging(repackagingEntry);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(AttachRepackaging.FILE);
		attachment.setFileLink(fileDirectory+fileName);
		
		repackagingAttachmentDao.saveOrUpdate(attachment);
		
		//Logging
		long end = System.currentTimeMillis();
		long timeInSeconds = (end-start)/1000;
		logger.info("Execution Time - addRepackagingFileAttachment:"+ timeInSeconds+" seconds");
		return sequenceNo;
	}

	public Integer addRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName, String textAttachment)
			throws Exception{
		if(repackagingEntry == null || !repackagingEntry.getId().equals(repackagingEntryID))
			repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(repackagingEntryID);
		AttachRepackaging attachment = new AttachRepackaging();
		attachment.setRepackaging(repackagingEntry);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(AttachRepackaging.TEXT);
		attachment.setTextAttachment(textAttachment);
		repackagingAttachmentDao.saveOrUpdate(attachment);
		return sequenceNo;
	}
	
	public Boolean deleteRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception{
		AttachRepackaging attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		if(attachment == null)
			return Boolean.FALSE;
		if(AttachRepackaging.FILE.equals(attachment.getDocumentType())){
			File file = new File(serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
			if(!file.delete())
				logger.severe("Could not delete file at: " + attachment.getFileLink());
		}
		repackagingAttachmentDao.delete(attachment);
		return Boolean.TRUE;
	}

	public AttachmentFile getRepackagingFileAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception{
		logger.info("START - getRepackagingFileAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		AttachRepackaging attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		File file = new File(serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
		if(!file.canRead())
			return null;
		FileInputStream fin = new FileInputStream(file);
		byte[] bytes = new byte[(int)file.length()];
		fin.read(bytes);
		fin.close();
		AttachmentFile attachmentFile = new AttachmentFile();
		attachmentFile.setBytes(bytes);
		attachmentFile.setFileName(attachment.getFileName());
		
		//Logging
		long end = System.currentTimeMillis();
		long timeInSeconds = (end-start)/1000;
		logger.info("Execution Time - getRepackagingFileAttachment:"+ timeInSeconds+" seconds");

		return attachmentFile;
	}

	public List<AttachRepackaging> getRepackagingAttachments(
			Long repackagingEntryID) throws Exception {
		if(repackagingEntry == null || !repackagingEntry.getId().equals(repackagingEntryID))
			repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(repackagingEntryID);
		return repackagingAttachmentDao.getRepackagingAttachments(repackagingEntry);
	}

	public Boolean saveRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName, String textAttachment) throws Exception {
		AttachRepackaging attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		if(attachment == null)
			return Boolean.FALSE;
		attachment.setTextAttachment(textAttachment);
		attachment.setFileName(fileName);
		repackagingAttachmentDao.saveOrUpdate(attachment);
		return Boolean.TRUE;
	}
	/***************************Repackaging Attachment --END***************************/
	
	
	
	/***************************SC Pacakge Attachment (SC, SC Detail, SC Payment)***************************/
	public UploadSCAttachmentResponseObj uploadAttachment(String nameObject, String textKey, Integer sequenceNo, String fileName, byte[] file, String createdUser) throws Exception {
		logger.info("START - uploadAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		UploadSCAttachmentResponseObj responseObj = new UploadSCAttachmentResponseObj();
		String splittedTextKey[] = textKey.split("\\|");
		
		String vendorNumber = splittedTextKey[0].trim();
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = "";
		if (!AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim())){
			adminService.canAccessJob(jobNumber);
			packageNo = splittedTextKey[1].trim();
		}
		try{
			//Job Directory Path 
			String jobDirectoryPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory()+jobNumber+"\\"; 
			File jobDirectory = new File(jobDirectoryPath);
			boolean isJobDirectoryExists = (jobDirectory).exists();			
			
			if(!isJobDirectoryExists){
				logger.info("Job Directory - "+jobDirectoryPath+" does not exist. Job Directory will be created.");
				jobDirectory.mkdir();
			}
			
			//Vendor Directory Path
			String vendorDirectoryPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getVendorAttachmentsDirectory()+vendorNumber+"\\";
			File vendorDirectory = new File(vendorDirectoryPath);
			boolean isVendorDirectoryExists = (vendorDirectory).exists();			
			
			if(!isVendorDirectoryExists){
				logger.info("Vendor Directory - "+vendorDirectoryPath+" does not exist. Vendor Directory will be created.");
				vendorDirectory.mkdir();
			}
			
			//write to file
			File attachment;	
			if (AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim()))
				attachment = new File(vendorDirectoryPath+fileName);
			else
				attachment = new File(jobDirectoryPath+fileName);
			logger.info("Attachment Full Path: "+attachment.getPath());
			
			int i=0;
			String tmpFileName=fileName;
			while(attachment.exists()){ // check if the file exists, append new file if necessary	
				i++;
				int extensionPosition = fileName.lastIndexOf(".");
				tmpFileName = fileName.substring(0,extensionPosition)+"("+i+")" +fileName.substring(extensionPosition , fileName.length());
				
				//Set new name for duplicated filename
				if (AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim()))
					attachment = new File(vendorDirectoryPath+tmpFileName);
				else
					attachment = new File(jobDirectoryPath+tmpFileName);				
			}
			fileName = tmpFileName;
			if (AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim())){
				if(fileName.length() > 50) throw new Exception("File name too long");
			}
			FileOutputStream attachmentOutputStream = new FileOutputStream(attachment);

			attachmentOutputStream.write(file);
			attachmentOutputStream.close();
			logger.info("Vendor/Job: "+splittedTextKey[0].trim()+" TextKey: "+textKey);
			String attachmentPathInDB;
			if (AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim()))
				attachmentPathInDB = attachment.getAbsolutePath();
			else
				attachmentPathInDB = jobNumber+ "\\"+ fileName;

			if (AbstractAttachment.SCPackageNameObject.equals(nameObject.trim())){
				AttachSubcontract scAttachment = new AttachSubcontract();
				scAttachment.setDocumentType(AttachSubcontract.FILE);
				scAttachment.setSequenceNo(sequenceNo);
				scAttachment.setFileLink(attachmentPathInDB);
				scAttachment.setFileName(fileName);
				scAttachment.setCreatedUser(createdUser);
				logger.info("Sequence No:"+ sequenceNo);

				scAttachment.setSubcontract(packageHBDao.obtainSCPackage(jobNumber, packageNo));
				scAttachmentDao.addSCAttachment(scAttachment);
			}else if (AbstractAttachment.SCPaymentNameObject.equals(nameObject.trim())){
				AttachPayment scPaymentAttachment =new AttachPayment();
				scPaymentAttachment.setDocumentType(AttachSubcontract.FILE);
				scPaymentAttachment.setSequenceNo(sequenceNo);
				scPaymentAttachment.setFileLink(attachmentPathInDB);
				scPaymentAttachment.setFileName(fileName);
				scPaymentAttachment.setCreatedUser(createdUser);

				logger.info("Job: "+jobNumber+" PackageNo: " + packageNo+" PaymentNo: "+splittedTextKey[2]+" SequenceNo: " + sequenceNo);

				PaymentCert scPaymentCert;
				scPaymentCert = scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2]));
				scPaymentAttachment.setPaymentCert(scPaymentCert);

				scPaymentAttachmentDao.addSCAttachment(scPaymentAttachment);
			}else if (AbstractAttachment.SCDetailsNameObject.equals(nameObject.trim())){
				AttachSubcontractDetail scDetailAttachment = new AttachSubcontractDetail();
				scDetailAttachment.setDocumentType(AttachSubcontract.FILE);
				scDetailAttachment.setSequenceNo(sequenceNo);
				scDetailAttachment.setFileLink(attachmentPathInDB);
				scDetailAttachment.setFileName(fileName);
				scDetailAttachment.setCreatedUser(createdUser);
				scDetailAttachment.setSubcontractDetail(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]));
				scDetailAttachmentDao.addSCAttachment(scDetailAttachment);

			}else if (AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim())){
				AttachSubcontract scAttachment = new AttachSubcontract();
				scAttachment.setDocumentType(AttachSubcontract.FILE);
				scAttachment.setSequenceNo(sequenceNo);
				scAttachment.setFileLink(attachmentPathInDB);
				scAttachment.setFileName(fileName);
				scAttachment.setCreatedUser(createdUser);
				logger.info("Sequence No:"+ sequenceNo);
				packageWSDao.addUpdateSCAttachmentLink(nameObject, textKey, scAttachment);				
			}
			responseObj.setSuccess(true);
			responseObj.setMessage("Attachment "+ fileName+" upload successfully.");
			
			//Logging
			long end = System.currentTimeMillis();
			long timeInSeconds = (end-start)/1000;
			logger.info("Execution Time - uploadAttachment:"+ timeInSeconds+" seconds");

		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			responseObj.setMessage("Fail to upload attachment "+ fileName+": <br>" + e.getMessage());
		}

		return responseObj;

	}

	public boolean uploadAddendumAttachment(String nameObject, String textKey, BigDecimal sequenceNo, String fileName, byte[] file, String createdUser) throws Exception {
		logger.info("START - uploadAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
		String noJob = paramMap.get(Attachment.TEXTKEY_1);
//		String noSubcontract = paramMap.get(Attachment.TEXTKEY_2);
//		String altParam = paramMap.get(Attachment.TEXTKEY_3);
		String name_table = paramMap.get(Attachment.NAME_TABLE);
		BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
		adminService.canAccessJob(noJob);

		try{
			//Job Directory Path 
			String jobDirectoryPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory()+noJob+"\\"; 
			File jobDirectory = new File(jobDirectoryPath);
			boolean isJobDirectoryExists = (jobDirectory).exists();			
			
			if(!isJobDirectoryExists){
				logger.info("Job Directory - "+jobDirectoryPath+" does not exist. Job Directory will be created.");
				jobDirectory.mkdir();
			}
			
			//write to file
			File attachment = new File(jobDirectoryPath+fileName);
			logger.info("Attachment Full Path: "+attachment.getPath());
			
			int i=0;
			String tmpFileName=fileName;
			while(attachment.exists()){ // check if the file exists, append new file if necessary	
				i++;
				int extensionPosition = fileName.lastIndexOf(".");
				tmpFileName = fileName.substring(0,extensionPosition)+"("+i+")" +fileName.substring(extensionPosition , fileName.length());
				
				//Set new name for duplicated filename
				attachment = new File(jobDirectoryPath+tmpFileName);				
			}
			fileName = tmpFileName;

			FileOutputStream attachmentOutputStream = new FileOutputStream(attachment);

			attachmentOutputStream.write(file);
			attachmentOutputStream.close();
			logger.info("Vendor/Job: "+ noJob +" TextKey: "+textKey);
			String attachmentPathInDB = noJob+ "\\"+ fileName;

			Attachment uploadAttachment = new Attachment();
			uploadAttachment.setTypeDocument(Attachment.FILE);
			uploadAttachment.setNoSequence(sequenceNo);
			uploadAttachment.setPathFile(attachmentPathInDB);
			uploadAttachment.setNameFile(fileName);
			uploadAttachment.setUsernameCreated(createdUser);
			uploadAttachment.setIdTable(id_table);
			uploadAttachment.setNameTable(name_table);
			attachmentHBDao.saveOrUpdate(uploadAttachment);
			
			//Logging
			long end = System.currentTimeMillis();
			long timeInSeconds = (end-start)/1000;
			logger.info("Execution Time - uploadAttachment:"+ timeInSeconds+" seconds");
			return true;
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			throw new IOException("Fail to upload attachment "+ fileName+".");
		}
	}
	
	@SuppressWarnings("resource")
	public AttachmentFile obtainAddendumFileAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		logger.info("START - obtainFileAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		AttachmentFile attachmentFile = new AttachmentFile();
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String fileLink = null;
		Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
		String noJob = paramMap.get(Attachment.TEXTKEY_1);
//		String noSubcontract = paramMap.get(Attachment.TEXT_KEY2);
		String name_table = paramMap.get(Attachment.NAME_TABLE);
		BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
		adminService.canAccessJob(noJob);
		Attachment attachment = null;
		attachment  = attachmentHBDao.obtainAttachment(name_table, id_table, new BigDecimal(sequenceNumber));
		if (attachment==null)
			throw new Exception("Attachment Type Error");
		fileLink = serverPath + attachment.getPathFile();
		
		if(attachment.getTypeDocument().equals(Attachment.TEXT)){
			attachmentFile.setBytes(attachment.getText().getBytes());
		} else {
			try{
				File file = new File(fileLink);
				long length = file.length();
	
				InputStream is = new FileInputStream(file);
	
				byte[] bytes = new byte[(int)length];
	
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
					offset += numRead;
				}
	
				// Ensure all the bytes have been read in
				if (offset < bytes.length) {
					throw new IOException("Could not completely read file "+file.getName());
				}
	
				// Close the input stream and return bytes
				is.close();
	
				int fileNameIndex = fileLink.lastIndexOf("\\");
				String fileName = fileLink.substring(fileNameIndex+1);
	
				attachmentFile.setBytes(bytes);
				attachmentFile.setFileName(fileName);	        
	
				//Logging
				long end = System.currentTimeMillis();
				long timeInSeconds = (end-start)/1000;
				logger.info("Execution Time - obtainFileAttachment:"+ timeInSeconds+" seconds");
				
			}catch(Exception e){
				logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			}
		}
		return attachmentFile;
	}

	public Boolean uploadAddendumTextAttachment(String nameObject, String textKey, Integer sequenceNo, String filename, String textContent) throws Exception {
		Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
		String noJob = paramMap.get(Attachment.TEXTKEY_1);
//		String noSubcontract = paramMap.get(Attachment.TEXTKEY_2);
//		String altParam = paramMap.get(Attachment.TEXTKEY_3);
		String name_table = paramMap.get(Attachment.NAME_TABLE);
		BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
		adminService.canAccessJob(noJob);
		Attachment attachment = attachmentHBDao.obtainAttachment(name_table, id_table, new BigDecimal(sequenceNo));
		if(attachment == null) attachment = new Attachment();
		attachment.setText(textContent);
		attachment.setNameFile(filename);
		attachment.setPathFile(Attachment.FileLinkForText);
		attachment.setTypeDocument(Attachment.TEXT);
		attachment.setNoSequence(new BigDecimal(sequenceNo));
		attachment.setIdTable(id_table);
		attachment.setNameTable(name_table);
		attachmentHBDao.saveOrUpdate(attachment);
		return true;
	}

	public Boolean deleteAddendumAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String directoryPath="";

		Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
		String noJob = paramMap.get(Attachment.TEXTKEY_1);
//		String noSubcontract = paramMap.get(Attachment.TEXTKEY_2);
//		String altParam = paramMap.get(Attachment.TEXTKEY_3);
		String name_table = paramMap.get(Attachment.NAME_TABLE);
		BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
		adminService.canAccessJob(noJob);
		Attachment attachment;
			attachment = attachmentHBDao.obtainAttachment(name_table, id_table, new BigDecimal(sequenceNumber));
			directoryPath = serverPath + attachment.getPathFile();
			attachmentHBDao.delete(attachment);
		try{
			if (!directoryPath.isEmpty()){
				File deleteFile = new File(directoryPath);
				if(deleteFile != null)
					deleteFile.delete();
			}
			return true;
		}catch (Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return false;
		}
	}

	public Boolean uploadTextAttachment(String nameObject, String textKey, Integer sequenceNo, String filename, String textContent) throws Exception {
		if (AttachSubcontract.VendorNameObject.equals(nameObject)) {
			return new Boolean(packageWSDao.addUpdateSCTextAttachment(nameObject, textKey, sequenceNo, textContent));
		}
		String user = securityService.getCurrentUser().getUsername();
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		adminService.canAccessJob(jobNumber);
		if (AttachSubcontract.SCPackageNameObject.equals(nameObject)){
			AttachSubcontract dbObj = new AttachSubcontract();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(filename);
			dbObj.setFileLink(AttachSubcontract.FileLinkForText);
			dbObj.setDocumentType(AttachSubcontract.TEXT);
			dbObj.setSequenceNo(sequenceNo);
			dbObj.setSubcontract(packageHBDao.obtainSCPackage(jobNumber,packageNo));
			return new Boolean(scAttachmentDao.addUpdateSCTextAttachment(dbObj, user)); 
		}else if (AttachSubcontract.SCPaymentNameObject.equals(nameObject)){
			AttachPayment dbObj = new AttachPayment();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(filename);
			dbObj.setFileLink(AttachSubcontract.FileLinkForText);
			dbObj.setDocumentType(AttachSubcontract.TEXT);
			dbObj.setSequenceNo(sequenceNo);

			PaymentCert scPaymentCert;
			scPaymentCert = scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2]));

			dbObj.setPaymentCert(scPaymentCert);

			return new Boolean(scPaymentAttachmentDao.addUpdateSCTextAttachment(dbObj, user)); 
		}else if (AttachSubcontract.SCDetailsNameObject.equals(nameObject)) {
			AttachSubcontractDetail dbObj = new AttachSubcontractDetail();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(filename);
			dbObj.setFileLink(AttachSubcontract.FileLinkForText);
			dbObj.setDocumentType(AttachSubcontract.TEXT);
			dbObj.setSequenceNo(sequenceNo);
			dbObj.setSubcontractDetail(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]));
			return new Boolean(scDetailAttachmentDao.addUpdateSCTextAttachment(dbObj, user));
		}else if(AttachMainCert.MainCertNameObject.equals(nameObject)){
			Integer noMainCert = Integer.valueOf(splittedTextKey[1].trim());
			MainCert mainCert = mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, noMainCert);
			AttachMainCert dbObj = new AttachMainCert();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(filename);
			dbObj.setFileLink(AttachMainCert.FileLinkForText);
			dbObj.setDocumentType(AttachMainCert.TEXT);
			dbObj.setSequenceNo(sequenceNo);
			dbObj.setMainCert(mainCert);
			dbObj.setLastModifiedUser(user);
			mainCertificateAttachmentHBDaoImpl.saveOrUpdate(dbObj);
			return true;
		} else {
			return false;
		}
	}

	@Transactional(readOnly = true, value = "transactionManager")
	public List<? extends AbstractAttachment> getAttachmentList(String nameObject, String textKey) throws Exception{
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String splittedTextKey[]=null;
		String jobNumber="";
		String packageNo="";
		if (!AbstractAttachment.VendorNameObject.equals(nameObject)){
			splittedTextKey = textKey.split("\\|");
			jobNumber= splittedTextKey[0].trim();
			packageNo = splittedTextKey[1].trim();
			adminService.canAccessJob(jobNumber);
		}
		try{
			if(Attachment.SplitNameObject.equals(nameObject) || Attachment.TerminateNameObject.equals(nameObject)){
				Subcontract subcontract = subcontractService.obtainSCPackage(jobNumber, packageNo);
				List<Attachment> attachmentList = attachmentHBDao.obtainAttachmentList(nameObject, new BigDecimal(subcontract.getId()));
				List<AttachSubcontract> resultList = new ArrayList<>();
				for(Attachment attachment : attachmentList){
					AttachSubcontract scAttachment = new AttachSubcontract(attachment);
					String path = Attachment.FILE.equals(attachment.getTypeDocument()) ? serverPath + attachment.getPathFile() : attachment.getPathFile();
					scAttachment.setFileLink(path);
					resultList.add(scAttachment);
				}
				return resultList;
			} else if (AttachSubcontract.SCPackageNameObject.equals(nameObject.trim())){ 
				List<AttachSubcontract> resultList = scAttachmentDao.getSCAttachment(jobNumber, packageNo);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachSubcontract>();
				Collections.sort(resultList, new Comparator<AttachSubcontract>(){
					public int compare(AttachSubcontract scAttachment1, AttachSubcontract scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;

						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}
				});

				//Set the file link with server path 
				for(AttachSubcontract scAttachment: resultList){
					if(scAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
						scAttachment.setFileLink(serverPath + scAttachment.getFileLink());
				}
				
				return resultList;
			}else if (AttachSubcontract.SCPaymentNameObject.equals(nameObject)){
				List<AttachPayment> resultList = scPaymentAttachmentDao.getAttachPayment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2])));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachPayment>();
				
				Collections.sort(resultList, new Comparator<AttachPayment>(){
					public int compare(AttachPayment scPaymentAttachment1, AttachPayment scPaymentAttachment2) {
						if(scPaymentAttachment1== null || scPaymentAttachment2 == null)
							return 0;

						return scPaymentAttachment1.getSequenceNo().compareTo(scPaymentAttachment2.getSequenceNo());				
					}
				});
				
				//Set the file link with server path 
//				for(SCPaymentAttachment scPaymentAttachment: resultList){
//					if(scPaymentAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
//						scPaymentAttachment.setFileLink(serverPath + scPaymentAttachment.getFileLink());
//				}
				
				return resultList;
			}else if (AttachSubcontract.SCDetailsNameObject.equals(nameObject.trim())){;
				List<AttachSubcontractDetail> resultList = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo,splittedTextKey[2]));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachSubcontractDetail>();
				Collections.sort(resultList, new Comparator<AttachSubcontractDetail>(){
					public int compare(AttachSubcontractDetail scDetailAttachment1, AttachSubcontractDetail scDetailAttachment2) {
						if(scDetailAttachment1== null || scDetailAttachment2 == null)
							return 0;
						return scDetailAttachment1.getSequenceNo().compareTo(scDetailAttachment2.getSequenceNo());				
					}
				});
				
				//Set the file link with server path 
//				for(SCDetailsAttachment scDetailsAttachment: resultList){
//					if(scDetailsAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
//						scDetailsAttachment.setFileLink(serverPath + scDetailsAttachment.getFileLink());
//				}
				
				return resultList;
			}else if (AttachSubcontract.VendorNameObject.equals(nameObject.trim())){
				List<AttachSubcontract> resultList = packageWSDao.getAttachmentList(nameObject, textKey);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachSubcontract>();
				Collections.sort(resultList, new Comparator<AttachSubcontract>(){

					public int compare(AttachSubcontract scAttachment1, AttachSubcontract scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;
						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}

				});
				return resultList;
			}else if (AttachSubcontract.MainCertNameObject.equals(nameObject.trim())){;
			List<AttachMainCert> resultList = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachmentList(mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(packageNo)));
			if (resultList==null || resultList.size()<1)
				return new ArrayList<AttachMainCert>();
			Collections.sort(resultList, new Comparator<AttachMainCert>(){
				public int compare(AttachMainCert mainCertificateAttachment1, AttachMainCert mainCertificateAttachment2) {
					if(mainCertificateAttachment1== null || mainCertificateAttachment2 == null)
						return 0;
					return mainCertificateAttachment1.getSequenceNo().compareTo(mainCertificateAttachment2.getSequenceNo());				
				}
			});
			
			//Set the file link with server path 
			for(AttachMainCert mainCertificateAttachment: resultList){
				if(mainCertificateAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
					mainCertificateAttachment.setFileLink(serverPath + mainCertificateAttachment.getFileLink());
			}
			
			return resultList;
		}else {
				logger.log(Level.SEVERE, "Invalid nameObject:"+nameObject+":found.");
				return new ArrayList<AttachSubcontract>();

			}
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return new ArrayList<AttachSubcontract>();
		}
	}
	
	@Transactional(readOnly = true, value = "transactionManager")
	public List<? extends AbstractAttachment> getPaymentAttachmentList(String nameObject, String textKey) throws Exception{
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		adminService.canAccessJob(jobNumber);
		int latestPaymentCertNo = getLatestPaymentCertNo(jobNumber,  packageNo);
		List<AttachPayment> resultList = scPaymentAttachmentDao.getAttachPayment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo,latestPaymentCertNo));
		
		Collections.sort(resultList, new Comparator<AttachPayment>(){
			public int compare(AttachPayment scPaymentAttachment1, AttachPayment scPaymentAttachment2) {
				if(scPaymentAttachment1== null || scPaymentAttachment2 == null)
					return 0;
				return scPaymentAttachment1.getSequenceNo().compareTo(scPaymentAttachment2.getSequenceNo());				
			}
		});
		
		//Set the file link with server path
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		for(AttachPayment scPaymentAttachment: resultList){
			if(scPaymentAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
				scPaymentAttachment.setFileLink(serverPath + scPaymentAttachment.getFileLink());
		}
		
		return resultList;
	}

	public Integer getLatestPaymentCertNo(String jobNumber, String packageNo) throws Exception{
		int latestPaymentCertNo = 0;
		List<PaymentCert> paymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo);
		for(PaymentCert curPaymentCert : paymentCertList){
			if(curPaymentCert.getPaymentCertNo()>latestPaymentCertNo)
				latestPaymentCertNo = curPaymentCert.getPaymentCertNo();
		}
		return latestPaymentCertNo;
	}

	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String directoryPath="";
		if (AttachSubcontract.VendorNameObject.equals(nameObject)){
			directoryPath =packageWSDao.getSCAttachmentFileLink(nameObject, textKey, sequenceNumber);
			if (directoryPath==null || "".equals(directoryPath.trim())){
				return packageWSDao.deleteSCTextAttachment(nameObject, textKey, sequenceNumber);
			}
			try {
				return packageWSDao.deleteSCAttachmentLink(nameObject, textKey, sequenceNumber);
			} catch (Exception e) {
				return true;
			}
		}else{
			String splittedTextKey[] = textKey.split("\\|");
			String jobNumber = splittedTextKey[0].trim();
			String packageNo = splittedTextKey[1].trim();
			adminService.canAccessJob(jobNumber);
			if (AttachSubcontract.SCPackageNameObject.equalsIgnoreCase(nameObject)){
				AttachSubcontract scAttachment = scAttachmentDao.obtainSCFileAttachment(splittedTextKey[0], splittedTextKey[1], sequenceNumber);
				directoryPath = serverPath + scAttachment.getFileLink();
				scAttachmentDao.delete(scAttachment);
			}else if (AttachSubcontract.SCPaymentNameObject.equalsIgnoreCase(nameObject)){
				AttachPayment scPaymentAttachment = scPaymentAttachmentDao.getAttachPayment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2])), sequenceNumber);
				directoryPath = serverPath + scPaymentAttachment.getFileLink();
				scPaymentAttachmentDao.delete(scPaymentAttachment);
			} else if(AttachMainCert.MainCertNameObject.equalsIgnoreCase(nameObject)){
				Integer noMainCert = Integer.valueOf(splittedTextKey[1].trim());
				MainCert mainCert = mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, noMainCert);
				AttachMainCert attachMainCert = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachment(mainCert, sequenceNumber);
				directoryPath = serverPath + attachMainCert.getFileLink();
				mainCertificateAttachmentHBDaoImpl.delete(attachMainCert);
			} else{
				AttachSubcontractDetail scDetailAttachment = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]), sequenceNumber);
				directoryPath = serverPath + scDetailAttachment.getFileLink();
				scDetailAttachmentDao.delete(scDetailAttachment);
			}
			try{
				if (directoryPath!=null && !"".equals(directoryPath.trim())){
					File deleteFile = new File(directoryPath);
					if(deleteFile != null)
						deleteFile.delete();
				}
				return true;
			}catch (Exception e){
				logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
				return false;
			}
		}
	}

	@SuppressWarnings("resource")
	public AttachmentFile obtainFileAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		logger.info("START - obtainFileAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		AttachmentFile attachmentFile = new AttachmentFile();
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String fileLink;
		if (AttachSubcontract.VendorNameObject.equals(nameObject)){
			fileLink = packageWSDao.getSCAttachmentFileLink(nameObject, textKey, sequenceNumber);
			logger.info("fileLink(Vendor): "+fileLink);
		}else{
			String splittedTextKey[] = textKey.split("\\|");
			String jobNumber = splittedTextKey[0].trim();
			String packageNo = splittedTextKey[1].trim();
			adminService.canAccessJob(jobNumber);
			if (AttachSubcontract.SCPackageNameObject.equalsIgnoreCase(nameObject)){
				AttachSubcontract scAttachmentResult = null;
				scAttachmentResult  = scAttachmentDao.obtainSCFileAttachment(jobNumber, packageNo, sequenceNumber);
				if (scAttachmentResult==null)
					throw new Exception("Attachment Type Error");
				fileLink = serverPath + scAttachmentResult.getFileLink();
			}else if (AttachSubcontract.SCPaymentNameObject.equalsIgnoreCase(nameObject)){
				AttachPayment scPaymentAttachmentResult = null;
				scPaymentAttachmentResult  = scPaymentAttachmentDao.getAttachPayment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo,new Integer(splittedTextKey[2])), sequenceNumber);
				if (scPaymentAttachmentResult==null)
					throw new Exception("Attachment Type Error");
				fileLink = serverPath + scPaymentAttachmentResult.getFileLink();
			} else if (AttachMainCert.MainCertNameObject.equalsIgnoreCase(nameObject)){
				AttachMainCert attachMainCert = null;
				Integer noMainCert = Integer.valueOf(splittedTextKey[1].trim());
				MainCert mainCert = mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, noMainCert);
				attachMainCert = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachment(mainCert, sequenceNumber);
				if(attachMainCert == null){
					throw new Exception("Attachment Type Error");
				}
				fileLink = serverPath + attachMainCert.getFileLink();
			} else{
				AttachSubcontractDetail scDetailAttachmentResult = null;
				scDetailAttachmentResult  = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]), sequenceNumber);
				if (scDetailAttachmentResult==null)
					throw new Exception("Attachment Type Error");
				fileLink = serverPath + scDetailAttachmentResult.getFileLink();
			}
		}
		
		try{
			File file = new File(fileLink);
			long length = file.length();

			InputStream is = new FileInputStream(file);

			byte[] bytes = new byte[(int)length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "+file.getName());
			}

			// Close the input stream and return bytes
			is.close();

			int fileNameIndex = fileLink.lastIndexOf("\\");
			String fileName = fileLink.substring(fileNameIndex+1);

			attachmentFile.setBytes(bytes);
			attachmentFile.setFileName(fileName);	        

			//Logging
			long end = System.currentTimeMillis();
			long timeInSeconds = (end-start)/1000;
			logger.info("Execution Time - obtainFileAttachment(Package/Detail/Payment/Vendor):"+ timeInSeconds+" seconds");
			
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
		}

		return attachmentFile;
	}

	/**
	 * obtain text attachment which is located at JDE System or QS System
	 * For Subcontractor Attachment-GT58024-@JDE, SCPackage Attachment-GT58012@QS System, SCPayment Attachment-GT58012@QS System & SCDetail Attachment-GT58011@QS System
	 * @author tikywong
	 * refactored on 21 Oct, 2013
	 */
	public String obtainTextAttachmentContent(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		//JDE Text Attachment -> nameObject = GT58024(Vendor)
		if (AttachSubcontract.VendorNameObject.equals(nameObject))
			return packageWSDao.obtainSCTextAttachmentfromJDE(nameObject, textKey, sequenceNumber);
		
		//QS Text Attachment -> GT58010(SCPackage), GT58012(SCPayment), GT58011(SCDetail)
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		adminService.canAccessJob(jobNumber);
		try{
			if (AttachSubcontract.SCPackageNameObject.equalsIgnoreCase(nameObject)){
				AttachSubcontract result = null;
				
				logger.info("SCPackage Attachment - Job: "+jobNumber+" Package: "+packageNo+" SequenceNo: "+sequenceNumber);
				result = scAttachmentDao.obtainSCFileAttachment(jobNumber, packageNo, sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}else if(AttachSubcontract.SCPaymentNameObject.equalsIgnoreCase(nameObject)){
				AttachPayment result = null;
				Integer paymentNo = new Integer(splittedTextKey[2].trim());
				
				logger.info("SCPayment Attachment - Job: "+jobNumber+" Package: "+packageNo+" Payment: "+paymentNo+" SequenceNo: "+sequenceNumber);
				result = scPaymentAttachmentDao.getAttachPayment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, paymentNo), sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}else if(AttachSubcontract.MainCertNameObject.equalsIgnoreCase(nameObject)){
				AttachMainCert result = null;
				
				logger.info("Main Certificate Attachment - Job: "+jobNumber+" MainCertNo.: "+packageNo+" SequenceNo: "+sequenceNumber);
				result = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachment(mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, Integer.valueOf(packageNo)), sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}
			else{
				AttachSubcontractDetail result = null;
				String scDetailSequenceNo = splittedTextKey[2].trim();
				
				logger.info("SCDetail Attachment - Job: "+jobNumber+" Package: "+packageNo+" SCDetailSequenceNo: "+scDetailSequenceNo+" SequenceNo: "+sequenceNumber);
				result = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, scDetailSequenceNo), sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}
		}catch (Exception e){
			logger.info("Error: Unable to obtain Text Attachment - Job: "+jobNumber+" Package: "+packageNo+"\n"+e);
			return "";
		}
		return "";
	}
	
	@Transactional(readOnly = true, value = "transactionManager")
	public List<? extends AbstractAttachment> getAttachmentListForPCMS(String nameObject, String textKey) throws Exception{
		@SuppressWarnings("unused")
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String splittedTextKey[]=null;
		String jobNumber="";
		String packageNo="";
		if (!AbstractAttachment.VendorNameObject.equals(nameObject)){
			splittedTextKey = textKey.split("\\|");
			if(splittedTextKey == null || splittedTextKey.length < 2) {
				throw new IllegalArgumentException("textKey wrong format:" + textKey);
			}
			jobNumber= splittedTextKey[0].trim();
			packageNo = splittedTextKey[1].trim();
			adminService.canAccessJob(jobNumber);
		}
		try{
			if (AttachSubcontract.SCPackageNameObject.equals(nameObject.trim())){ 
				List<AttachSubcontract> resultList = scAttachmentDao.getSCAttachment(jobNumber, packageNo);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachSubcontract>();
				Collections.sort(resultList, new Comparator<AttachSubcontract>(){
					public int compare(AttachSubcontract scAttachment1, AttachSubcontract scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;

						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}
				});

				//Set the file link with server path 
				/*for(SCAttachment scAttachment: resultList){
					if(scAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
						scAttachment.setFileLink(serverPath + scAttachment.getFileLink());
				}*/
				
				return resultList;
			}else if (AttachSubcontract.SCPaymentNameObject.equals(nameObject)){
				List<AttachPayment> resultList = scPaymentAttachmentDao.getAttachPayment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2])));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachPayment>();
				
				Collections.sort(resultList, new Comparator<AttachPayment>(){
					public int compare(AttachPayment scPaymentAttachment1, AttachPayment scPaymentAttachment2) {
						if(scPaymentAttachment1== null || scPaymentAttachment2 == null)
							return 0;

						return scPaymentAttachment1.getSequenceNo().compareTo(scPaymentAttachment2.getSequenceNo());				
					}
				});
				
				//Set the file link with server path 
//				for(SCPaymentAttachment scPaymentAttachment: resultList){
//					if(scPaymentAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
//						scPaymentAttachment.setFileLink(serverPath + scPaymentAttachment.getFileLink());
//				}
				
				return resultList;
			}else if (AttachSubcontract.SCDetailsNameObject.equals(nameObject.trim())){;
				List<AttachSubcontractDetail> resultList = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo,splittedTextKey[2]));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachSubcontractDetail>();
				Collections.sort(resultList, new Comparator<AttachSubcontractDetail>(){
					public int compare(AttachSubcontractDetail scDetailAttachment1, AttachSubcontractDetail scDetailAttachment2) {
						if(scDetailAttachment1== null || scDetailAttachment2 == null)
							return 0;
						return scDetailAttachment1.getSequenceNo().compareTo(scDetailAttachment2.getSequenceNo());				
					}
				});
				
				//Set the file link with server path 
//				for(SCDetailsAttachment scDetailsAttachment: resultList){
//					if(scDetailsAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
//						scDetailsAttachment.setFileLink(serverPath + scDetailsAttachment.getFileLink());
//				}
				
				return resultList;
			}else if (AttachSubcontract.VendorNameObject.equals(nameObject.trim())){
				List<AttachSubcontract> resultList = packageWSDao.getAttachmentList(nameObject, textKey);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<AttachSubcontract>();
				Collections.sort(resultList, new Comparator<AttachSubcontract>(){

					public int compare(AttachSubcontract scAttachment1, AttachSubcontract scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;
						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}

				});
				return resultList;
			}else if (AttachMainCert.MainCertNameObject.equals(nameObject.trim())){;
			Integer noMainCert = Integer.valueOf(splittedTextKey[1].trim());
			List<AttachMainCert> resultList = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachmentList(mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, noMainCert));
			if (resultList==null || resultList.size()<1)
				return new ArrayList<AttachMainCert>();
			Collections.sort(resultList, new Comparator<AttachMainCert>(){
				public int compare(AttachMainCert mainCertificateAttachment1, AttachMainCert mainCertificateAttachment2) {
					if(mainCertificateAttachment1== null || mainCertificateAttachment2 == null)
						return 0;
					return mainCertificateAttachment1.getSequenceNo().compareTo(mainCertificateAttachment2.getSequenceNo());				
				}
			});
			
			//Set the file link with server path 
			/*for(MainCertificateAttachment mainCertificateAttachment: resultList){
				if(mainCertificateAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
					mainCertificateAttachment.setFileLink(serverPath + mainCertificateAttachment.getFileLink());
			}*/
			
			return resultList;
		}else {
				logger.log(Level.SEVERE, "Invalid nameObject:"+nameObject+":found.");
				return new ArrayList<AttachSubcontract>();

			}
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return new ArrayList<AttachSubcontract>();
		}
	}

	/***************************Main Contract Certificate Attachment***************************/
	/**
	 * @author tikywong
	 * created on January 20, 2012
	 */
	public AttachmentFile getMainCertFileAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws Exception {
		logger.info("START - getMainCertFileAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo);
		MainCert mainCert = mainContractCertificateRepository.getCertificate(jobNumber, mainCertNumber);
		AttachMainCert attachment = mainCertificateAttachmentHBDaoImpl.obtainAttachment(mainCert.getId(), sequenceNo);
		
		//prepare file
		File file = new File(serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
		if(!file.canRead())
			return null;
		
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] bytes = new byte[(int)file.length()];
		fileInputStream.read(bytes);
		fileInputStream.close();
		
		AttachmentFile attachmentFile = new AttachmentFile();
		attachmentFile.setBytes(bytes);
		attachmentFile.setFileName(attachment.getFileName());

		//Logging
		long end = System.currentTimeMillis();
		long timeInSeconds = (end-start)/1000;
		logger.info("Execution Time - getMainCertFileAttachment:"+ timeInSeconds+" seconds");
		return attachmentFile;
	}
		
	/**
	 * @author tikywong
	 * created on January 26, 2012
	 */
	public Boolean addMainCertFileAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String fileName, byte[] bytes) throws Exception{
		logger.info("START - addMainCertFileAttachment");
		//Timer
		long start = System.currentTimeMillis();

		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber.toString()+", sequenceNo="+sequenceNo.toString()+", fileName="+fileName);
		if(jobNumber==null || mainCertNumber==null || sequenceNo==null || fileName==null || fileName.length() == 0 || bytes == null || bytes.length == 0)
			throw new Exception(jobNumber==null?"Job Number is null":
								mainCertNumber==null?"Main Certificate Number is null":
								sequenceNo==null?"Sequence No is null":
								fileName==null||fileName.length()==0?"File name is null or without name":"File is null or empty");
			
		MainCert mainCert = mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, mainCertNumber);
		if(mainCert==null)
			throw new Exception("Job: "+jobNumber+" Main Certificate: "+mainCertNumber+" does not exist.");

		/**
		 * @author koeyyeung
		 * Remove attachment server path in db
		 * modified on 27/03/2014**/
		String serverPath = serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory();
		String fileDirectory = mainCert.getJobNo() + "\\" + "MainCert_" + mainCert.getCertificateNumber().toString()+"\\";
		logger.info("fileDirectory: "+fileDirectory);
		
		File file = new File(serverPath + fileDirectory);
		if(!file.exists()){
			if(!file.mkdirs())
				throw new Exception("Directory could not be created successfully. fileDirectory: "+fileDirectory);
		}
		
		file = new File(serverPath+fileDirectory + fileName);
		logger.info("Attachment Full Path: "+serverPath+fileDirectory + fileName);
		int i=0;
		String tmpFileName=fileName;
		while(file.exists()){ // check if the file exists, append new file if necessary
			i++;
			int extensionPosition = fileName.lastIndexOf(".");
			tmpFileName = fileName.substring(0,extensionPosition)+"("+i+")" +fileName.substring(extensionPosition , fileName.length());
			file = new File(serverPath+ fileDirectory + tmpFileName );				
		}
		fileName = tmpFileName;
		
		FileOutputStream fileOout = new FileOutputStream(file);
		fileOout.write(bytes);
		fileOout.close();
		
		//String fileLink = fileDirectory + fileName;
		AttachMainCert attachment = new AttachMainCert();
		attachment.setMainCert(mainCert);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(AttachMainCert.FILE);
		attachment.setFileLink(fileDirectory + fileName);

		
		mainCertificateAttachmentHBDaoImpl.saveOrUpdate(attachment);
		
		logger.info("File Attachment is saved at "+serverPath+fileDirectory + fileName);

		//Logging
		long end = System.currentTimeMillis();
		long timeInSeconds = (end-start)/1000;
		logger.info("Execution Time - addMainCertFileAttachment:"+ timeInSeconds+" seconds");
		return true;
	}

	/**
	 * @author tikywong
	 * created on 27 January, 2012
	 */
	public Boolean deleteMainCertAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException {
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo);
		
		MainCert mainCert = mainContractCertificateHBDaoImpl.findByJobNoAndCertificateNo(jobNumber, mainCertNumber);
		if(mainCert==null)
			return false;
		
		List<AttachMainCert> attachments = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachmentList(mainCert);
		
		for(AttachMainCert attachment:attachments){
			if(attachment!=null && attachment.getSequenceNo().intValue()==sequenceNo.intValue()){
				if(AttachMainCert.FILE.equals(attachment.getDocumentType())){
					File file = new File(serviceConfig.getAttachmentServer("PATH")+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
					if(!file.delete()){
						logger.info("Could not delete file at: " + attachment.getFileLink());
						return false;
					}
				}
				
				attachments.remove(attachment);
				break;
			}
		}
		
		mainContractCertificateHBDaoImpl.saveOrUpdate(mainCert);
		
		logger.info("Attachment is deleted.");
		return true;
	}
	/***************************Main Contract Certificate Attachment --END ***************************/

	public List<Attachment> obtainAttachmentList(String nameObject, String textKey) throws DatabaseOperationException{
		Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
		List<Attachment> attachmentList = attachmentHBDao.obtainAttachmentList(
												paramMap.get(Attachment.NAME_TABLE), 
												new BigDecimal(paramMap.get(Attachment.ID_TABLE))
											);
		return attachmentList;
	}
	
	private Map<String, String> obtainAttachmentTableNameAndId(String nameObject, String textKey) throws DatabaseOperationException{
		Map<String, String> resultMap = new HashMap<>();
		String splittedTextKey[] = textKey.split("\\|");
		String noJob = splittedTextKey[0].trim();
		String noSubcontract = splittedTextKey[1].trim();
		String altParam = splittedTextKey.length > 2 && !splittedTextKey[2].isEmpty()?splittedTextKey[2]:"0";

		resultMap.put(Attachment.TEXTKEY_1, noJob);
		resultMap.put(Attachment.TEXTKEY_2, noSubcontract);
		resultMap.put(Attachment.TEXTKEY_3, altParam);
		
		switch(nameObject){
		case Attachment.AddendumNameObject:
			Addendum addendum = addendumService.getAddendum(noJob, noSubcontract, new Long(altParam));
			resultMap.put(Attachment.NAME_TABLE, Attachment.ADDENDUM_TABLE);
			resultMap.put(Attachment.ID_TABLE, addendum.getId().toString());
			break;
		case Attachment.SPLIT_TABLE:
		case Attachment.TERMINATE_TABLE:
			Subcontract subcontract = packageHBDao.obtainSubcontract(noJob, noSubcontract);
			if(subcontract == null) throw new IllegalArgumentException("Job " + noJob + " subcontract " + noSubcontract + " not found");
			resultMap.put(Attachment.NAME_TABLE, Attachment.SPLIT_TABLE.equals(nameObject) ? Attachment.SPLIT_TABLE : Attachment.TERMINATE_TABLE);
			resultMap.put(Attachment.ID_TABLE, subcontract.getId().toString());
			break;
		case Attachment.TRANSIT_TABLE:
			Transit transit = transitService.getTransitHeader(noJob);
			resultMap.put(Attachment.NAME_TABLE, Attachment.TRANSIT_TABLE);
			resultMap.put(Attachment.ID_TABLE, transit.getId().toString());
			break;
		}
		return resultMap;
	}
	
	/***************************SC Package Attachment (SC, SC Detail, SC Payment)--END***************************/
}

