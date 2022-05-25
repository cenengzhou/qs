package com.gammon.qs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gammon.pcms.model.ConsultancyAgreement;
import com.gammon.pcms.model.ROC_SUBDETAIL;
import com.gammon.pcms.service.HTMLService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.pcms.dao.AttachmentHBDao;
import com.gammon.pcms.model.Addendum;
import com.gammon.pcms.model.Attachment;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.dao.RepackagingHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.SubcontractWSDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.io.AttachmentFile;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.transit.TransitService;

/**
 * koeyyeung Refactored on Mar 21, 201410:45:32 AM
 */
@Service
// SpringSession workaround: change "session" to "request"
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class AttachmentService {
	@Autowired
	private AttachmentConfig serviceConfig;
	@Autowired
	private AdminService adminService;

	// Repackaging Attachment
	@Autowired
	private RepackagingHBDao repackagingEntryDao;

	// Main Contract Certificate Attachment
	@Autowired
	private MainCertService mainContractCertificateRepository;
	// Package
	@Autowired
	private SubcontractHBDao packageHBDao;
	@Autowired
	private SubcontractWSDao packageWSDao;
	// Payment
	@Autowired
	private PaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private AttachmentHBDao attachmentHBDao;;
	@Autowired
	private AddendumService addendumService;
	@Autowired
	private TransitService transitService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private ConsultancyAgreementService consultancyAgreementService;
	@Autowired
	private HTMLService htmlService;
	

	private Logger logger = Logger.getLogger(AttachmentService.class.getName());

	private String obtainNameTable(String nameObject){
		String nameTable = "";
		switch(nameObject){
		case Attachment.JobInfoNameObject:
			nameTable = Attachment.JOBINFO_TABLE;
			break;
		case Attachment.AddendumNameObject:
			nameTable = Attachment.ADDENDUM_TABLE;
			break;
		case Attachment.SCPaymentNameObject:
			nameTable = Attachment.PAYMENT_TABLE;
			break;
		case Attachment.MainCertNameObject:
			nameTable = Attachment.MAIN_CERT_TABLE;
			break;
		case Attachment.SCPackageNameObject:
			nameTable = Attachment.SUBCONTRACT_TABLE;
			break;
		case Attachment.VendorNameObject:
			nameTable = Attachment.VENDOR_TABLE;
			break;
		case Attachment.SplitNameObject:
			nameTable = Attachment.SPLIT_TABLE;
			break;
		case Attachment.TerminateNameObject:
			nameTable = Attachment.TERMINATE_TABLE;
			break;
		case Attachment.RepackagingNameObject:
			nameTable = Attachment.REPACKAGING_TABLE;
			break;
		case Attachment.TransitNameObject:
			nameTable = Attachment.TRANSIT_TABLE;
			break;
		case Attachment.RocSubdetailNameObject:
			nameTable = Attachment.ROC_SUBDETAIL_TABLE;
			break;
		case Attachment.ConsultancyNameObject:
			nameTable = Attachment.CONSULTANCY_TABLE;
			break;
		default:
			throw new IllegalArgumentException("nameObject not defined:" + nameObject);
		}
		return nameTable;
	}
	
	private Map<String, String> obtainAttachmentTableNameAndId(String nameObject, String textKey) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		String splittedTextKey[] = textKey.split("\\|");
		String noJob = splittedTextKey[0];
		String noSubcontract = splittedTextKey.length > 1 ? splittedTextKey[1] : "0";
		String altParam = splittedTextKey.length > 2 && !splittedTextKey[2].isEmpty() && !"--".equals(splittedTextKey[2])? splittedTextKey[2] : "0";
		String nameTable = obtainNameTable(nameObject);
		resultMap.put(Attachment.TEXTKEY_1, noJob);
		resultMap.put(Attachment.TEXTKEY_2, noSubcontract);
		resultMap.put(Attachment.TEXTKEY_3, altParam);
		resultMap.put(Attachment.NAME_TABLE, nameTable);
		try {
			switch (nameObject) {
			case Attachment.ConsultancyNameObject:
				ConsultancyAgreement consultancyAgreement = consultancyAgreementService.getMemo(noJob, noSubcontract);
				resultMap.put(Attachment.ID_TABLE, consultancyAgreement.getId().toString());
				break;
			case Attachment.RocSubdetailNameObject:
				resultMap.put(Attachment.ID_TABLE, altParam);
				break;
			case Attachment.JobInfoNameObject:
				JobInfo jobInfo = jobInfoService.obtainJob(noJob);
				resultMap.put(Attachment.ID_TABLE, jobInfo.getId().toString());
				break;
			case Attachment.AddendumNameObject:
				Addendum addendum = addendumService.getAddendum(noJob, noSubcontract, new Long(altParam));
				resultMap.put(Attachment.ID_TABLE, addendum.getId().toString());
				break;
			case Attachment.SCPackageNameObject:
			case Attachment.SplitNameObject:
			case Attachment.TerminateNameObject:
				Subcontract subcontract = packageHBDao.obtainSubcontract(noJob, noSubcontract);
				resultMap.put(Attachment.ID_TABLE, subcontract.getId().toString());
				break;
			case Attachment.SCPaymentNameObject:
				PaymentCert paymentCert;
				if(altParam.equals("0")){
					paymentCert = paymentService.obtainPaymentLatestCert(noJob, noSubcontract);
				} else {
					paymentCert = scPaymentCertHBDao.obtainPaymentCertificate(noJob, noSubcontract, new Integer(altParam));
				}
				resultMap.put(Attachment.ID_TABLE, paymentCert.getId().toString());
				break;
			case Attachment.MainCertNameObject:
				int mainCertNo = Integer.parseInt(altParam) != 0 ? new Integer(altParam) : new Integer(resultMap.get(Attachment.TEXTKEY_2));
				MainCert mainCert = mainContractCertificateRepository.getCertificate(noJob, mainCertNo);
				resultMap.put(Attachment.ID_TABLE, mainCert.getId().toString());
				break;
			case Attachment.RepackagingNameObject:
				Repackaging repackaging = repackagingEntryDao.getRepackagingEntryWithJob(new Long(altParam));
				resultMap.put(Attachment.ID_TABLE, repackaging.getId().toString());
				break;
			case Attachment.VendorNameObject:
				resultMap.put(Attachment.ID_TABLE,"0");
				break;
			case Attachment.TransitNameObject:
				Transit transit = transitService.getTransitHeader(noJob);
				resultMap.put(Attachment.NAME_TABLE, Attachment.TRANSIT_TABLE);
				resultMap.put(Attachment.ID_TABLE, transit.getId().toString());
				break;
			}
			return resultMap;
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"Job " + noJob + " subcontract " + noSubcontract + " " + nameTable + " " + altParam + " not found");
		}
	}

	public List<Attachment> obtainSubcontractDateAttachmentList(Long subcontractId) {
		return attachmentHBDao.obtainSubcontractDateAttachmentList(subcontractId);
	}
	
	public List<Attachment> obtainAttachmentList(String nameObject, String textKey) throws Exception{
		List<Attachment> attachmentList = new ArrayList<>();
		switch (nameObject) {
		case Attachment.VendorNameObject:
			attachmentList = packageWSDao.getAttachmentList(nameObject, textKey);
			if (attachmentList == null || attachmentList.size() < 1)
				return new ArrayList<Attachment>();
			Collections.sort(attachmentList, new Comparator<Attachment>() {
				public int compare(Attachment scAttachment1, Attachment scAttachment2) {
					if (scAttachment1 == null || scAttachment2 == null)
						return 0;
					return scAttachment1.getNoSequence().compareTo(scAttachment2.getNoSequence());
				}
			});
			break;
		default:
			Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
			attachmentList = attachmentHBDao.obtainAttachmentList(paramMap.get(Attachment.NAME_TABLE),
					new BigDecimal(paramMap.get(Attachment.ID_TABLE)));
		}
		return attachmentList;
	}

	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception {
		switch (nameObject) {
		case Attachment.VendorNameObject:
			String directoryPath = packageWSDao.getSCAttachmentFileLink(nameObject, textKey, sequenceNumber);
			if (directoryPath == null || "".equals(directoryPath.trim())) {
				return packageWSDao.deleteSCTextAttachment(nameObject, textKey, sequenceNumber);
			}
			try {
				return packageWSDao.deleteSCAttachmentLink(nameObject, textKey, sequenceNumber);
			} catch (Exception e) {
				return true;
			}
		default:
			Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
			String noJob = paramMap.get(Attachment.TEXTKEY_1);
			// String noSubcontract = paramMap.get(Attachment.TEXTKEY_2);
			// String altParam = paramMap.get(Attachment.TEXTKEY_3);
			String name_table = paramMap.get(Attachment.NAME_TABLE);
			BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
			adminService.canAccessJob(noJob);
			Attachment attachment = attachmentHBDao.obtainAttachment(name_table, id_table, new BigDecimal(sequenceNumber));
			return deleteAttachment(attachment);
		}
	}

	public Boolean deleteAttachment(Attachment attachment){
		try {
			String serverPath = serviceConfig.getAttachmentServer("PATH") + serviceConfig.getJobAttachmentsDirectory();
			String directoryPath = serverPath + attachment.getPathFile();
			attachmentHBDao.delete(attachment);
			if (attachment.getTypeDocument().equals(Attachment.FILE)) {
				File deleteFile = new File(directoryPath);
				if (deleteFile != null)
					deleteFile.delete();
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			return false;
		}
	}
	
	public Boolean uploadTextAttachment(String nameObject, String textKey, Integer sequenceNo, String filename,
			String textContent) throws Exception {
		
		switch (nameObject) {
		case Attachment.VendorNameObject:
			return packageWSDao.addUpdateSCTextAttachment(nameObject, textKey, sequenceNo, textContent);
		default:
			Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
			String noJob = paramMap.get(Attachment.TEXTKEY_1);
			// String noSubcontract = paramMap.get(Attachment.TEXTKEY_2);
			// String altParam = paramMap.get(Attachment.TEXTKEY_3);
			String name_table = paramMap.get(Attachment.NAME_TABLE);
			BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
			adminService.canAccessJob(noJob);
			Attachment attachment = attachmentHBDao.obtainAttachment(name_table, id_table, new BigDecimal(sequenceNo));
			if (attachment == null)
				attachment = new Attachment();
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
	}

	public boolean uploadAttachment(String nameObject, String textKey, BigDecimal sequenceNo, String fileName,
			byte[] file, String createdUser) throws Exception {
		logger.info("START - uploadAttachment");
		// Timer
		long start = System.currentTimeMillis();
		Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
		String TEXTKEY_1 = paramMap.get(Attachment.TEXTKEY_1);
		String name_table = paramMap.get(Attachment.NAME_TABLE);
		BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
		String dirPath = "";
		String attachmentPathInDB = "";
		try {
			
			switch (nameObject) {
			case Attachment.VendorNameObject:
				// Vendor Directory Path
				String vendorDirectoryPath = serviceConfig.getAttachmentServer("PATH")
						+ serviceConfig.getVendorAttachmentsDirectory() + TEXTKEY_1 + "\\";
				File vendorDirectory = new File(vendorDirectoryPath);
				boolean isVendorDirectoryExists = (vendorDirectory).exists();

				if (!isVendorDirectoryExists) {
					logger.info("Vendor Directory - " + vendorDirectoryPath
							+ " does not exist. Vendor Directory will be created.");
					vendorDirectory.mkdir();
				}
				dirPath = vendorDirectoryPath;
				if(fileName.length() > 50) throw new Exception("File name too long");
				
				break;
			default:
				adminService.canAccessJob(TEXTKEY_1);
				// Job Directory Path
				String jobDirectoryPath = serviceConfig.getAttachmentServer("PATH")
						+ serviceConfig.getJobAttachmentsDirectory() + TEXTKEY_1 + "\\";
				File jobDirectory = new File(jobDirectoryPath);
				boolean isJobDirectoryExists = (jobDirectory).exists();

				if (!isJobDirectoryExists) {
					logger.info(
							"Job Directory - " + jobDirectoryPath + " does not exist. Job Directory will be created.");
					jobDirectory.mkdir();
				}
				dirPath = jobDirectoryPath;
			}

			// write to file
			fileName = filenameLimit(fileName);
			File attachment = new File(dirPath + fileName);
			logger.info("Attachment Full Path: " + attachment.getPath());

			int i = 0;
			String tmpFileName = fileName;
			while (attachment.exists()) { // check if the file exists, append
											// new file if necessary
				i++;
//				int extensionPosition = fileName.lastIndexOf(".");
//				tmpFileName = fileName.substring(0, extensionPosition) + "(" + i + ")"
//						+ fileName.substring(extensionPosition, fileName.length());
				tmpFileName = filenameLimit(fileName, i);
				// Set new name for duplicated filename
				attachment = new File(dirPath + tmpFileName);
			}
			fileName = tmpFileName;
			FileOutputStream attachmentOutputStream = new FileOutputStream(attachment);

			attachmentOutputStream.write(file);
			attachmentOutputStream.close();
			logger.info("Vendor/Job: " + TEXTKEY_1 + " TextKey: " + textKey);
			Attachment uploadAttachment;
			switch(nameObject){
			case Attachment.VendorNameObject:
				attachmentPathInDB = attachment.getAbsolutePath();
				uploadAttachment = new Attachment();
				uploadAttachment.setTypeDocument(Attachment.FILE);
				uploadAttachment.setNoSequence(sequenceNo);
				uploadAttachment.setPathFile(attachmentPathInDB);
				uploadAttachment.setNameFile(fileName);
				uploadAttachment.setUsernameCreated(createdUser);
				logger.info("Sequence No:"+ sequenceNo);
				packageWSDao.addUpdateSCAttachmentLink(nameObject, textKey, uploadAttachment);
				break;
			default:
				attachmentPathInDB = TEXTKEY_1 + "\\" + fileName;
				uploadAttachment = new Attachment();
				uploadAttachment.setTypeDocument(Attachment.FILE);
				uploadAttachment.setNoSequence(sequenceNo);
				uploadAttachment.setPathFile(attachmentPathInDB);
				uploadAttachment.setNameFile(fileName);
				uploadAttachment.setUsernameCreated(createdUser);
				uploadAttachment.setIdTable(id_table);
				uploadAttachment.setNameTable(name_table);
				attachmentHBDao.saveOrUpdate(uploadAttachment);
			}
			
			// Logging
			long end = System.currentTimeMillis();
			long timeInSeconds = (end - start) / 1000;
			logger.info("Execution Time - uploadAttachment:" + timeInSeconds + " seconds");
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
			throw new IOException("Fail to upload attachment " + fileName + ": " + e.getMessage());
		}
	}
	
	public String filenameLimit(String filename) {
		return filenameLimit(filename, 0);
	}
	public String filenameLimit(String filename, int append) {
		return filenameLimit(filename, append, 100);
	}
	public String filenameLimit(String filename, int append, int limit) {
		int extIndex = filename.lastIndexOf(".");
		String ext = extIndex > 0 ? filename.substring(extIndex) : "";
		String appendText = append > 0 ? "(" + append + ")" : "";
		int filenameLimit = limit - appendText.length() - ext.length();
		filename = StringUtils.left(filename, filenameLimit > extIndex ? extIndex : filenameLimit) + appendText + ext;
		return filename;
	}

	public AttachmentFile obtainFileAttachment(String nameObject, String textKey, Integer sequenceNumber)
			throws Exception {
		logger.info("START - obtainFileAttachment");
		// Timer
		long start = System.currentTimeMillis();
		String fileLink = "";
		Attachment attachment = null;
		AttachmentFile attachmentFile = new AttachmentFile();
		
		switch(nameObject){
		case Attachment.VendorNameObject:
			fileLink = packageWSDao.getSCAttachmentFileLink(nameObject, textKey, sequenceNumber);
			break;
		default:
			Map<String, String> paramMap = obtainAttachmentTableNameAndId(nameObject, textKey);
			String noJob = paramMap.get(Attachment.TEXTKEY_1);
			// String noSubcontract = paramMap.get(Attachment.TEXT_KEY2);
			String name_table = paramMap.get(Attachment.NAME_TABLE);
			BigDecimal id_table = new BigDecimal(paramMap.get(Attachment.ID_TABLE));
			adminService.canAccessJob(noJob);
			attachment = attachmentHBDao.obtainAttachment(name_table, id_table, new BigDecimal(sequenceNumber));
			if (attachment == null) throw new Exception("Attachment Type Error");
			String serverPath = serviceConfig.getAttachmentServer("PATH") + serviceConfig.getJobAttachmentsDirectory();
			fileLink = serverPath + attachment.getPathFile();
			if (attachment.getTypeDocument().equals(Attachment.TEXT)) {
				attachmentFile.setBytes(attachment.getText().getBytes());
				return attachmentFile;
			}
		}
		fileLink = fileLink.trim();
		logger.info("fileLink: " + fileLink);

		try {
			File file = new File(fileLink);
			long length = file.length();

			InputStream is = new FileInputStream(file);

			byte[] bytes = new byte[(int) length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				is.close();
				throw new IOException("Could not completely read file " + file.getName());
			}

			// Close the input stream and return bytes
			is.close();

			int fileNameIndex = fileLink.lastIndexOf("\\");
			String fileName = fileLink.substring(fileNameIndex + 1);

			attachmentFile.setBytes(bytes);
			attachmentFile.setFileName(fileName);

			// Logging
			long end = System.currentTimeMillis();
			long timeInSeconds = (end - start) / 1000;
			logger.info("Execution Time - obtainFileAttachment:" + timeInSeconds + " seconds");

		} catch (Exception e) {
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", e);
		}
		return attachmentFile;
	}
	
	public void deleteAttachmentList(List<Attachment> attachmentList) throws Exception{
		for(Attachment attachment : attachmentList){
			deleteAttachment(attachment);
		}
	}

	public void deleteAttachmentByPaymentCert(PaymentCert latestPaymentCert) throws Exception {
		String textKey = latestPaymentCert.getJobNo() + "|"+latestPaymentCert.getPackageNo()+"|"+latestPaymentCert.getPaymentCertNo();
		List<Attachment> attachmentList = obtainAttachmentList(Attachment.SCPaymentNameObject, textKey);
		deleteAttachmentList(attachmentList);
	}

	public void deleteAttachmentByMainCert(MainCert mainCert) throws Exception {
		String textKey = mainCert.getJobNo() + "|--|" + mainCert.getCertificateNumber();
		List<Attachment> attachmentList = obtainAttachmentList(Attachment.MainCertNameObject, textKey);
		deleteAttachmentList(attachmentList);
	}

	public void deleteAttachmentByRocSubdetail(ROC_SUBDETAIL rocSubdetail) throws Exception {
		String textKey = rocSubdetail.getRoc().getProjectNo() + "|--|" + rocSubdetail.getId();
		List<Attachment> attachmentList = obtainAttachmentList(Attachment.RocSubdetailNameObject, textKey);
		deleteAttachmentList(attachmentList);
	}

    @SuppressWarnings("deprecation")
	public String mergePaymentPdf(String jobNo, String subcontractNo, Integer paymentCertNo) throws Exception {
    	String errorMsg = "";
    	
    	String[] scpaymentMergeJoblist = serviceConfig.getScpaymentMergeJoblist();

		if (!Arrays.stream(scpaymentMergeJoblist).anyMatch(jobNo::equals)) {
			errorMsg = "Job "+jobNo+" is not in the white list";
			return errorMsg;
		}
		
		List<Attachment> attachmentList = obtainAttachmentList(Attachment.SCPaymentNameObject, jobNo +"|"+ subcontractNo +"|"+ paymentCertNo);
		/*if (attachmentList.size() != 1) {
			errorMsg = "No. of Attachment is not equal to 1";
			return errorMsg;
		}*/

		
		logger.info("Merge PDF for Job: "+ jobNo +"- SC "+ subcontractNo +"- Payment Cert "+ paymentCertNo);
		
		String tmpFileName = "tmp_" + jobNo + "-" + subcontractNo + "-" + String.format("%04d", paymentCertNo) + ".pdf";
		String fileName = jobNo + "-" + subcontractNo + "-" + String.format("%04d", paymentCertNo) + ".pdf";
		String serverPath = serviceConfig.getAttachmentServer("PATH") + serviceConfig.getJobAttachmentsDirectory();
		File jobDir = new File(serverPath, jobNo);
		File subcontractDir = new File(jobDir, "Subcontract");
		File subcontractNoDir = new File(subcontractDir, subcontractNo);
		File mergedFile = new File(subcontractNoDir, fileName);

		if (mergedFile.exists()) {
			logger.info("Merged file already exists. Skip.");
			return "";
		}


		if (!jobDir.exists()) {
			logger.info("Job Directory does not exist. Job Directory will be created.");
			jobDir.mkdir();
		}
		if (!subcontractDir.exists()) {
			logger.info("Subcontract Directory does not exist. Subcontract Directory will be created.");
			subcontractDir.mkdir();
		}
		if (!subcontractNoDir.exists()) {
			logger.info(subcontractNo + " Directory does not exist. "+ subcontractNo +" Directory will be created.");
			subcontractNoDir.mkdir();
		}

		// prepare payment pdf
		File paymentFile = new File(subcontractNoDir, tmpFileName);
		String HTML = htmlService.makeHTMLStringForSCPaymentCert(jobNo, subcontractNo, String.valueOf(paymentCertNo), "PDF");
		PdfDocument pdf = new PdfDocument(new PdfWriter(new FileOutputStream(paymentFile)));
		pdf.setDefaultPageSize(new PageSize(612.0F, 1190.0F));
		HtmlConverter.convertToPdf(HTML, pdf, new ConverterProperties());
		pdf.close();

		// merge payment pdf and attachment pdf
		PDFMergerUtility PDFmerger = new PDFMergerUtility();
		PDFmerger.setDestinationFileName(mergedFile.getAbsolutePath());
		PDFmerger.addSource(paymentFile);
		
		// prepare attachment pdf
		for (Attachment att: attachmentList){
			String attachmentFileType = FilenameUtils.getExtension(att.getPathFile()).toUpperCase();
			if (attachmentFileType.equals("PDF")){
				File attachmentFile = new File(serverPath, att.getPathFile());
				if (attachmentFile.exists()){
					PDFmerger.addSource(attachmentFile);
				} else {
					logger.warning("Attachment File does not exist. "+ attachmentFile.getAbsolutePath());
				}
			}
		}

		PDFmerger.mergeDocuments();

		// clean up temp file
		if (paymentFile.exists()) {
			paymentFile.delete();
		}

		return errorMsg;
	}

    /***************************SC Package Attachment (SC, SC Detail, SC Payment)--END***************************/

}
