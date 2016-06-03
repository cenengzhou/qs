package com.gammon.qs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.MainCertificateAttachmentHBDao;
import com.gammon.qs.dao.MainContractCertificateHBDao;
import com.gammon.qs.dao.PackageWSDao;
import com.gammon.qs.dao.RepackagingAttachmentHBDao;
import com.gammon.qs.dao.RepackagingEntryHBDao;
import com.gammon.qs.dao.SCAttachmentHBDao;
import com.gammon.qs.dao.SCDetailAttachmentHBDao;
import com.gammon.qs.dao.SCDetailsHBDao;
import com.gammon.qs.dao.SCPackageHBDao;
import com.gammon.qs.dao.SCPaymentAttachmentHBDao;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.MainCertificateAttachment;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.RepackagingAttachment;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.domain.SCAttachment;
import com.gammon.qs.domain.SCDetails;
import com.gammon.qs.domain.SCDetailsAttachment;
import com.gammon.qs.domain.SCPaymentAttachment;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.scPackage.UploadSCAttachmentResponseObj;

/**
 * koeyyeung
 * Refactored on Mar 21, 201410:45:32 AM
 */
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class)
public class AttachmentService {
	@Autowired
	private AttachmentConfig serviceConfig;
	
	//Repackaging Attachment
	@Autowired
	private RepackagingAttachmentHBDao repackagingAttachmentDao;
	@Autowired
	private RepackagingEntryHBDao repackagingEntryDao;

	private RepackagingEntry repackagingEntry;

	//Main Contract Certificate Attachment
	@Autowired
	private MainContractCertificateHBDao mainContractCertificateHBDaoImpl;
	@Autowired
	private MainCertificateAttachmentHBDao mainCertificateAttachmentHBDaoImpl;
	@Autowired
	private MainContractCertificateService mainContractCertificateRepository;
	
	//SC Pacakge Attachment (SC, SC Detail, SC Payment)
	@Autowired
	private SCAttachmentHBDao scAttachmentDao;
	@Autowired
	private SCPaymentAttachmentHBDao scPaymentAttachmentDao;
	@Autowired
	private SCDetailAttachmentHBDao scDetailAttachmentDao;
	//Package
	@Autowired
	private SCPackageHBDao packageHBDao;
	@Autowired
	private PackageWSDao packageWSDao;
	//Payment
	@Autowired
	private SCPaymentCertHBDao scPaymentCertHBDao;
	//Detail
	@Autowired
	private SCDetailsHBDao scDetailsHBDao;
	
	
	private Logger logger = Logger.getLogger(AttachmentService.class.getName());

	/***************************Repackaging Attachment***************************/
	public Integer addRepackagingFileAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName,
			byte[] bytes) throws Exception{
		logger.info("START - addRepackagingFileAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		if(repackagingEntryID == null || sequenceNo == null || fileName == null 
				|| fileName.length() == 0 || bytes == null || bytes.length == 0)
			return null;
		logger.info("addFileAttachment: repackagingEntryID = " + repackagingEntryID.toString() + ", sequenceNo = " + sequenceNo.toString() + ", fileName = " + fileName);
		if(repackagingEntry == null || !repackagingEntry.getId().equals(repackagingEntryID))
			repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(repackagingEntryID);
		
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
		String fileDirectory = repackagingEntry.getJob().getJobNumber()+ "\\" + repackagingEntry.getRepackagingVersion()+"\\";
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
		
		RepackagingAttachment attachment = new RepackagingAttachment();
		attachment.setRepackagingEntry(repackagingEntry);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(RepackagingAttachment.FILE);
		attachment.setFileLink(fileDirectory+fileName);
		
		repackagingAttachmentDao.saveOrUpdate(attachment);
		
		//Logging
		long end = System.currentTimeMillis();
		long timeInSeconds = (end-start)/1000;
		logger.info("Execution Time - addRepackagingFileAttachment:"+ timeInSeconds+" seconds");
		return sequenceNo;
	}

	public Integer addRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName)
			throws Exception{
		if(repackagingEntry == null || !repackagingEntry.getId().equals(repackagingEntryID))
			repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(repackagingEntryID);
		RepackagingAttachment attachment = new RepackagingAttachment();
		attachment.setRepackagingEntry(repackagingEntry);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(RepackagingAttachment.TEXT);
		repackagingAttachmentDao.saveOrUpdate(attachment);
		return sequenceNo;
	}
	
	public Boolean deleteRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception{
		RepackagingAttachment attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		if(attachment == null)
			return Boolean.FALSE;
		if(RepackagingAttachment.FILE.equals(attachment.getDocumentType())){
			File file = new File(serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
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
		
		RepackagingAttachment attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		File file = new File(serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
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

	public String getRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception {
		RepackagingAttachment attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		return attachment.getTextAttachment();
	}

	public List<RepackagingAttachment> getRepackagingAttachments(
			Long repackagingEntryID) throws Exception {
		if(repackagingEntry == null || !repackagingEntry.getId().equals(repackagingEntryID))
			repackagingEntry = repackagingEntryDao.getRepackagingEntryWithJob(repackagingEntryID);
		return repackagingAttachmentDao.getRepackagingAttachments(repackagingEntry);
	}

	public Boolean saveRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String content) throws Exception {
		RepackagingAttachment attachment = repackagingAttachmentDao.getRepackagingAttachment(repackagingEntryID, sequenceNo);
		if(attachment == null)
			return Boolean.FALSE;
		attachment.setTextAttachment(content);
		repackagingAttachmentDao.saveOrUpdate(attachment);
		return Boolean.TRUE;
	}
	/***************************Repackaging Attachment --END***************************/
	
	
	/***************************Main Contract Certificate Attachment***************************/
	/**
	 * @author tikywong
	 * created on January 18, 2012
	 */
	public String getMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException {
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo);
		MainContractCertificate mainCert = mainContractCertificateRepository.getMainContractCert(jobNumber, mainCertNumber);
		MainCertificateAttachment attachment = mainCertificateAttachmentHBDaoImpl.obtainAttachment(mainCert.getId(), sequenceNo);
		return attachment.getTextAttachment();
	}
	
	/**
	 * @author tikywong
	 * created on January 20, 2012
	 */
	public AttachmentFile getMainCertFileAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws Exception {
		logger.info("START - getMainCertFileAttachment");
		//Timer
		long start = System.currentTimeMillis();
		
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo);
		MainContractCertificate mainCert = mainContractCertificateRepository.getMainContractCert(jobNumber, mainCertNumber);
		MainCertificateAttachment attachment = mainCertificateAttachmentHBDaoImpl.obtainAttachment(mainCert.getId(), sequenceNo);
		
		//prepare file
		File file = new File(serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
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
	public Boolean addMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String fileName) throws DatabaseOperationException{
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo);
		if(jobNumber==null || mainCertNumber==null || sequenceNo==null)
			return false;
		
		MainContractCertificate mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
		if(mainCert==null)
			return false;
		
		MainCertificateAttachment attachment = new MainCertificateAttachment();
		attachment.setMainCertificate(mainCert);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(MainCertificateAttachment.TEXT);
		
		mainCertificateAttachmentHBDaoImpl.saveOrUpdate(attachment);
		mainContractCertificateHBDaoImpl.saveOrUpdate(mainCert);

		logger.info("Text Attachment is created.");
		return true;
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
			
		MainContractCertificate mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
		if(mainCert==null)
			throw new Exception("Job: "+jobNumber+" Main Certificate: "+mainCertNumber+" does not exist.");

		/**
		 * @author koeyyeung
		 * Remove attachment server path in db
		 * modified on 27/03/2014**/
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
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
		MainCertificateAttachment attachment = new MainCertificateAttachment();
		attachment.setMainCertificate(mainCert);
		attachment.setSequenceNo(sequenceNo);
		attachment.setFileName(fileName);
		attachment.setDocumentType(MainCertificateAttachment.FILE);
		attachment.setFileLink(fileDirectory + fileName);

		
		mainContractCertificateHBDaoImpl.saveOrUpdate(mainCert);
		
		logger.info("File Attachment is saved at "+serverPath+fileDirectory + fileName);

		//Logging
		long end = System.currentTimeMillis();
		long timeInSeconds = (end-start)/1000;
		logger.info("Execution Time - addMainCertFileAttachment:"+ timeInSeconds+" seconds");
		return true;
	}

	/**
	 * @author tikywong
	 * created on January 26, 2012
	 */
	public Boolean saveMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String text) throws DatabaseOperationException {
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo+"\ntext="+text);
		MainContractCertificate mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
		if(mainCert==null)
			return false;
		
		MainCertificateAttachment attachment = mainCertificateAttachmentHBDaoImpl.obtainAttachment(mainCert.getId(), sequenceNo);
		if(attachment==null)
			return false;
		
		attachment.setTextAttachment(text);
		
		
		mainCertificateAttachmentHBDaoImpl.saveOrUpdate(attachment);
		logger.info("Text Attachment is saved.");
		return true;
	}
	
	/**
	 * @author tikywong
	 * created on 27 January, 2012
	 */
	public Boolean deleteMainCertAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException {
		logger.info("jobNumber="+jobNumber+", mainCertNumber="+mainCertNumber+", sequenceNo="+sequenceNo);
		
		MainContractCertificate mainCert = mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, mainCertNumber);
		if(mainCert==null)
			return false;
		
		List<MainCertificateAttachment> attachments = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachmentList(mainCert);
		
		for(MainCertificateAttachment attachment:attachments){
			if(attachment!=null && attachment.getSequenceNo().intValue()==sequenceNo.intValue()){
				if(MainCertificateAttachment.FILE.equals(attachment.getDocumentType())){
					File file = new File(serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory()+attachment.getFileLink());
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
	/***************************Main Contract Certificate Attachment --END***************************/
	
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
		if (!AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim()))
			packageNo = splittedTextKey[1].trim();

		try{
			//Job Directory Path 
			String jobDirectoryPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory()+jobNumber+"\\"; 
			File jobDirectory = new File(jobDirectoryPath);
			boolean isJobDirectoryExists = (jobDirectory).exists();			
			
			if(!isJobDirectoryExists){
				logger.info("Job Directory - "+jobDirectoryPath+" does not exist. Job Directory will be created.");
				jobDirectory.mkdir();
			}
			
			//Vendor Directory Path
			String vendorDirectoryPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getVendorAttachmentsDirectory()+vendorNumber+"\\";
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
				SCAttachment scAttachment = new SCAttachment();
				scAttachment.setDocumentType(SCAttachment.FILE);
				scAttachment.setSequenceNo(sequenceNo);
				scAttachment.setFileLink(attachmentPathInDB);
				scAttachment.setFileName(fileName);
				scAttachment.setCreatedUser(createdUser);
				logger.info("Sequence No:"+ sequenceNo);

				scAttachment.setScPackage(packageHBDao.obtainSCPackage(jobNumber, packageNo));
				scAttachmentDao.addSCAttachment(scAttachment);
			}else if (AbstractAttachment.SCPaymentNameObject.equals(nameObject.trim())){
				SCPaymentAttachment scPaymentAttachment =new SCPaymentAttachment();
				scPaymentAttachment.setDocumentType(SCAttachment.FILE);
				scPaymentAttachment.setSequenceNo(sequenceNo);
				scPaymentAttachment.setFileLink(attachmentPathInDB);
				scPaymentAttachment.setFileName(fileName);
				scPaymentAttachment.setCreatedUser(createdUser);

				logger.info("Job: "+jobNumber+" PackageNo: " + packageNo+" PaymentNo: "+splittedTextKey[2]+" SequenceNo: " + sequenceNo);

				SCPaymentCert scPaymentCert;
				scPaymentCert = scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2]));
				scPaymentAttachment.setScPaymentCert(scPaymentCert);

				scPaymentAttachmentDao.addSCAttachment(scPaymentAttachment);
			}else if (AbstractAttachment.SCDetailsNameObject.equals(nameObject.trim())){
				SCDetailsAttachment scDetailAttachment = new SCDetailsAttachment();
				scDetailAttachment.setDocumentType(SCAttachment.FILE);
				scDetailAttachment.setSequenceNo(sequenceNo);
				scDetailAttachment.setFileLink(attachmentPathInDB);
				scDetailAttachment.setFileName(fileName);
				scDetailAttachment.setCreatedUser(createdUser);
				scDetailAttachment.setScDetails(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]));
				scDetailAttachmentDao.addSCAttachment(scDetailAttachment);

			}else if (AbstractAttachment.VendorNameObject.equalsIgnoreCase(nameObject.trim())){
				SCAttachment scAttachment = new SCAttachment();
				scAttachment.setDocumentType(SCAttachment.FILE);
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
			responseObj.setMessage("Fail to upload attachment "+ fileName+".");
		}

		return responseObj;

	}

	public Boolean uploadTextAttachment(String nameObject, String textKey, Integer sequenceNo, String textContent, String user) throws Exception {
		if (SCAttachment.VendorNameObject.equals(nameObject)) {
			return new Boolean(packageWSDao.addUpdateSCTextAttachment(nameObject, textKey, sequenceNo, textContent));
		}
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		if (SCAttachment.SCPackageNameObject.equals(nameObject)){
			SCAttachment dbObj = new SCAttachment();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(SCAttachment.TEXTFileName);
			dbObj.setFileLink(SCAttachment.FileLinkForText);
			dbObj.setDocumentType(SCAttachment.TEXT);
			dbObj.setSequenceNo(sequenceNo);
			dbObj.setScPackage(packageHBDao.obtainSCPackage(jobNumber,packageNo));
			return new Boolean(scAttachmentDao.addUpdateSCTextAttachment(dbObj, user)); 
		}else if (SCAttachment.SCPaymentNameObject.equals(nameObject)){
			SCPaymentAttachment dbObj = new SCPaymentAttachment();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(SCAttachment.TEXTFileName);
			dbObj.setFileLink(SCAttachment.FileLinkForText);
			dbObj.setDocumentType(SCAttachment.TEXT);
			dbObj.setSequenceNo(sequenceNo);

			SCPaymentCert scPaymentCert;
			scPaymentCert = scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2]));

			dbObj.setScPaymentCert(scPaymentCert);

			return new Boolean(scPaymentAttachmentDao.addUpdateSCTextAttachment(dbObj, user)); 
		}else if (SCAttachment.SCDetailsNameObject.equals(nameObject)) {
			SCDetailsAttachment dbObj = new SCDetailsAttachment();
			dbObj.setTextAttachment(textContent);
			dbObj.setFileName(SCAttachment.TEXTFileName);
			dbObj.setFileLink(SCAttachment.FileLinkForText);
			dbObj.setDocumentType(SCAttachment.TEXT);
			dbObj.setSequenceNo(sequenceNo);
			dbObj.setScDetails(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]));
			return new Boolean(scDetailAttachmentDao.addUpdateSCTextAttachment(dbObj, user));
		}else return false;
	}

	@Transactional(readOnly = true)
	public List<? extends AbstractAttachment> getAttachmentList(String nameObject, String textKey) throws Exception{
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
		String splittedTextKey[]=null;
		String jobNumber="";
		String packageNo="";
		if (!AbstractAttachment.VendorNameObject.equals(nameObject)){
			splittedTextKey = textKey.split("\\|");
			jobNumber= splittedTextKey[0].trim();
			packageNo = splittedTextKey[1].trim();
		}
		try{
			if (SCAttachment.SCPackageNameObject.equals(nameObject.trim())){ 
				List<SCAttachment> resultList = scAttachmentDao.getSCAttachment(jobNumber, packageNo);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCAttachment>();
				Collections.sort(resultList, new Comparator<SCAttachment>(){
					public int compare(SCAttachment scAttachment1, SCAttachment scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;

						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}
				});

				//Set the file link with server path 
				for(SCAttachment scAttachment: resultList){
					if(scAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
						scAttachment.setFileLink(serverPath + scAttachment.getFileLink());
				}
				
				return resultList;
			}else if (SCAttachment.SCPaymentNameObject.equals(nameObject)){
				List<SCPaymentAttachment> resultList = scPaymentAttachmentDao.getSCPaymentAttachment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2])));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCPaymentAttachment>();
				
				Collections.sort(resultList, new Comparator<SCPaymentAttachment>(){
					public int compare(SCPaymentAttachment scPaymentAttachment1, SCPaymentAttachment scPaymentAttachment2) {
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
			}else if (SCAttachment.SCDetailsNameObject.equals(nameObject.trim())){;
				List<SCDetailsAttachment> resultList = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo,splittedTextKey[2]));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCDetailsAttachment>();
				Collections.sort(resultList, new Comparator<SCDetailsAttachment>(){
					public int compare(SCDetailsAttachment scDetailAttachment1, SCDetailsAttachment scDetailAttachment2) {
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
			}else if (SCAttachment.VendorNameObject.equals(nameObject.trim())){
				List<SCAttachment> resultList = packageWSDao.getAttachmentList(nameObject, textKey);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCAttachment>();
				Collections.sort(resultList, new Comparator<SCAttachment>(){

					public int compare(SCAttachment scAttachment1, SCAttachment scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;
						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}

				});
				return resultList;
			}else if (SCAttachment.MainCertNameObject.equals(nameObject.trim())){;
			List<MainCertificateAttachment> resultList = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachmentList(mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, Integer.valueOf(packageNo)));
			if (resultList==null || resultList.size()<1)
				return new ArrayList<MainCertificateAttachment>();
			Collections.sort(resultList, new Comparator<MainCertificateAttachment>(){
				public int compare(MainCertificateAttachment mainCertificateAttachment1, MainCertificateAttachment mainCertificateAttachment2) {
					if(mainCertificateAttachment1== null || mainCertificateAttachment2 == null)
						return 0;
					return mainCertificateAttachment1.getSequenceNo().compareTo(mainCertificateAttachment2.getSequenceNo());				
				}
			});
			
			//Set the file link with server path 
			for(MainCertificateAttachment mainCertificateAttachment: resultList){
				if(mainCertificateAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
					mainCertificateAttachment.setFileLink(serverPath + mainCertificateAttachment.getFileLink());
			}
			
			return resultList;
		}else {
				logger.log(Level.SEVERE, "Invalid nameObject:"+nameObject+":found.");
				return new ArrayList<SCAttachment>();

			}
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return new ArrayList<SCAttachment>();
		}
	}
	
	@Transactional(readOnly = true)
	public List<? extends AbstractAttachment> getAttachmentListForPCMS(String nameObject, String textKey) throws Exception{
		@SuppressWarnings("unused")
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
		String splittedTextKey[]=null;
		String jobNumber="";
		String packageNo="";
		if (!AbstractAttachment.VendorNameObject.equals(nameObject)){
			splittedTextKey = textKey.split("\\|");
			jobNumber= splittedTextKey[0].trim();
			packageNo = splittedTextKey[1].trim();
		}
		try{
			if (SCAttachment.SCPackageNameObject.equals(nameObject.trim())){ 
				List<SCAttachment> resultList = scAttachmentDao.getSCAttachment(jobNumber, packageNo);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCAttachment>();
				Collections.sort(resultList, new Comparator<SCAttachment>(){
					public int compare(SCAttachment scAttachment1, SCAttachment scAttachment2) {
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
			}else if (SCAttachment.SCPaymentNameObject.equals(nameObject)){
				List<SCPaymentAttachment> resultList = scPaymentAttachmentDao.getSCPaymentAttachment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2])));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCPaymentAttachment>();
				
				Collections.sort(resultList, new Comparator<SCPaymentAttachment>(){
					public int compare(SCPaymentAttachment scPaymentAttachment1, SCPaymentAttachment scPaymentAttachment2) {
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
			}else if (SCAttachment.SCDetailsNameObject.equals(nameObject.trim())){;
				List<SCDetailsAttachment> resultList = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo,splittedTextKey[2]));
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCDetailsAttachment>();
				Collections.sort(resultList, new Comparator<SCDetailsAttachment>(){
					public int compare(SCDetailsAttachment scDetailAttachment1, SCDetailsAttachment scDetailAttachment2) {
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
			}else if (SCAttachment.VendorNameObject.equals(nameObject.trim())){
				List<SCAttachment> resultList = packageWSDao.getAttachmentList(nameObject, textKey);
				if (resultList==null || resultList.size()<1)
					return new ArrayList<SCAttachment>();
				Collections.sort(resultList, new Comparator<SCAttachment>(){

					public int compare(SCAttachment scAttachment1, SCAttachment scAttachment2) {
						if(scAttachment1== null || scAttachment2 == null)
							return 0;
						return scAttachment1.getSequenceNo().compareTo(scAttachment2.getSequenceNo());				
					}

				});
				return resultList;
			}else if (SCAttachment.MainCertNameObject.equals(nameObject.trim())){;
			List<MainCertificateAttachment> resultList = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachmentList(mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, Integer.valueOf(packageNo)));
			if (resultList==null || resultList.size()<1)
				return new ArrayList<MainCertificateAttachment>();
			Collections.sort(resultList, new Comparator<MainCertificateAttachment>(){
				public int compare(MainCertificateAttachment mainCertificateAttachment1, MainCertificateAttachment mainCertificateAttachment2) {
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
				return new ArrayList<SCAttachment>();

			}
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return new ArrayList<SCAttachment>();
		}
	}

	
	@Transactional(readOnly = true)
	public List<? extends AbstractAttachment> getAddendumAttachmentList(String nameObject, String textKey) throws Exception{
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		try{
			List<SCDetails> scDetailsList = scDetailsHBDao.obtainSCDetails(jobNumber, packageNo);
			List<SCDetailsAttachment> resultList = new ArrayList<SCDetailsAttachment>();
			for (SCDetails scDetails : scDetailsList){
				if(Math.abs(scDetails.getTotalAmount()-scDetails.getToBeApprovedAmount())>0 || (!SCDetails.APPROVED.equals(scDetails.getApproved()) && !SCDetails.SUSPEND.equals(scDetails.getApproved())))
					resultList.addAll(scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo,scDetails.getSequenceNo().toString())));
			}
			Collections.sort(resultList, new Comparator<SCDetailsAttachment>(){
				public int compare(SCDetailsAttachment scDetailAttachment1, SCDetailsAttachment scDetailAttachment2) {
					if(scDetailAttachment1== null || scDetailAttachment2 == null)
						return 0;
					return scDetailAttachment1.getSequenceNo().compareTo(scDetailAttachment2.getSequenceNo());				
				}
			});
			
			//Set the file link with server path 
			String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
			for(SCDetailsAttachment scDetailsAttachment: resultList){
				if(scDetailsAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
					scDetailsAttachment.setFileLink(serverPath + scDetailsAttachment.getFileLink());
			}
			
			return resultList;
		}catch(Exception e){
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return null;
		}
	}

	@Transactional(readOnly = true)
	public List<? extends AbstractAttachment> getPaymentAttachmentList(String nameObject, String textKey) throws Exception{
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		int latestPaymentCertNo = getLatestPaymentCertNo(jobNumber,  packageNo);
		List<SCPaymentAttachment> resultList = scPaymentAttachmentDao.getSCPaymentAttachment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo,latestPaymentCertNo));
		
		Collections.sort(resultList, new Comparator<SCPaymentAttachment>(){
			public int compare(SCPaymentAttachment scPaymentAttachment1, SCPaymentAttachment scPaymentAttachment2) {
				if(scPaymentAttachment1== null || scPaymentAttachment2 == null)
					return 0;
				return scPaymentAttachment1.getSequenceNo().compareTo(scPaymentAttachment2.getSequenceNo());				
			}
		});
		
		//Set the file link with server path
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
		for(SCPaymentAttachment scPaymentAttachment: resultList){
			if(scPaymentAttachment.getDocumentType()!=0) //docType 0 = Text, others = File
				scPaymentAttachment.setFileLink(serverPath + scPaymentAttachment.getFileLink());
		}
		
		return resultList;
	}

	public Integer getLatestPaymentCertNo(String jobNumber, String packageNo) throws Exception{
		int latestPaymentCertNo = 0;
		List<SCPaymentCert> paymentCertList = scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, new Integer(packageNo));
		for(SCPaymentCert curPaymentCert : paymentCertList){
			if(curPaymentCert.getPaymentCertNo()>latestPaymentCertNo)
				latestPaymentCertNo = curPaymentCert.getPaymentCertNo();
		}
		return latestPaymentCertNo;
	}

	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
		String directoryPath="";
		if (SCAttachment.VendorNameObject.equals(nameObject)){
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
			if (SCAttachment.SCPackageNameObject.equalsIgnoreCase(nameObject)){
				SCAttachment scAttachment = scAttachmentDao.obtainSCFileAttachment(splittedTextKey[0], splittedTextKey[1], sequenceNumber);
				directoryPath = serverPath + scAttachment.getFileLink();
				scAttachmentDao.delete(scAttachment);
			}else if (SCAttachment.SCPaymentNameObject.equalsIgnoreCase(nameObject)){
				SCPaymentAttachment scPaymentAttachment = scPaymentAttachmentDao.getSCPaymentAttachment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, new Integer(splittedTextKey[2])), sequenceNumber);
				directoryPath = serverPath + scPaymentAttachment.getFileLink();
				scPaymentAttachmentDao.delete(scPaymentAttachment);
			}else{
				SCDetailsAttachment scDetailAttachment = scDetailAttachmentDao.getSCDetailsAttachment(scDetailsHBDao.obtainSCDetail(jobNumber, packageNo, splittedTextKey[2]), sequenceNumber);
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
		String serverPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getJobAttachmentsDirectory();
		String fileLink;
		if (SCAttachment.VendorNameObject.equals(nameObject)){
			fileLink = packageWSDao.getSCAttachmentFileLink(nameObject, textKey, sequenceNumber);
			logger.info("fileLink(Vendor): "+fileLink);
		}else{
			String splittedTextKey[] = textKey.split("\\|");
			String jobNumber = splittedTextKey[0].trim();
			String packageNo = splittedTextKey[1].trim();

			if (SCAttachment.SCPackageNameObject.equalsIgnoreCase(nameObject)){
				SCAttachment scAttachmentResult = null;
				scAttachmentResult  = scAttachmentDao.obtainSCFileAttachment(jobNumber, packageNo, sequenceNumber);
				if (scAttachmentResult==null)
					throw new Exception("Attachment Type Error");
				fileLink = serverPath + scAttachmentResult.getFileLink();
			}else if (SCAttachment.SCPaymentNameObject.equalsIgnoreCase(nameObject)){
				SCPaymentAttachment scPaymentAttachmentResult = null;
				scPaymentAttachmentResult  = scPaymentAttachmentDao.getSCPaymentAttachment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo,new Integer(splittedTextKey[2])), sequenceNumber);
				if (scPaymentAttachmentResult==null)
					throw new Exception("Attachment Type Error");
				fileLink = serverPath + scPaymentAttachmentResult.getFileLink();
			}else{
				SCDetailsAttachment scDetailAttachmentResult = null;
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
		if (SCAttachment.VendorNameObject.equals(nameObject))
			return packageWSDao.obtainSCTextAttachmentfromJDE(nameObject, textKey, sequenceNumber);
		
		//QS Text Attachment -> GT58010(SCPackage), GT58012(SCPayment), GT58011(SCDetail)
		String splittedTextKey[] = textKey.split("\\|");
		String jobNumber = splittedTextKey[0].trim();
		String packageNo = splittedTextKey[1].trim();
		try{
			if (SCAttachment.SCPackageNameObject.equalsIgnoreCase(nameObject)){
				SCAttachment result = null;
				
				logger.info("SCPackage Attachment - Job: "+jobNumber+" Package: "+packageNo+" SequenceNo: "+sequenceNumber);
				result = scAttachmentDao.obtainSCFileAttachment(jobNumber, packageNo, sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}else if(SCAttachment.SCPaymentNameObject.equalsIgnoreCase(nameObject)){
				SCPaymentAttachment result = null;
				Integer paymentNo = new Integer(splittedTextKey[2].trim());
				
				logger.info("SCPayment Attachment - Job: "+jobNumber+" Package: "+packageNo+" Payment: "+paymentNo+" SequenceNo: "+sequenceNumber);
				result = scPaymentAttachmentDao.getSCPaymentAttachment(scPaymentCertHBDao.obtainPaymentCertificate(jobNumber, packageNo, paymentNo), sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}else if(SCAttachment.MainCertNameObject.equalsIgnoreCase(nameObject)){
				MainCertificateAttachment result = null;
				
				logger.info("Main Certificate Attachment - Job: "+jobNumber+" MainCertNo.: "+packageNo+" SequenceNo: "+sequenceNumber);
				result = mainCertificateAttachmentHBDaoImpl.obtainMainCertAttachment(mainContractCertificateHBDaoImpl.obtainMainContractCert(jobNumber, Integer.valueOf(packageNo)), sequenceNumber);
				if (result!=null)
					return result.getTextAttachment();
			}
			else{
				SCDetailsAttachment result = null;
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
	/***************************SC Package Attachment (SC, SC Detail, SC Payment)--END***************************/
}
