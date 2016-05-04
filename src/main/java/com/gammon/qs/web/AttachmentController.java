package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.AttachmentRepositoryRemote;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.RepackagingAttachment;
import com.gammon.qs.service.AttachmentService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class AttachmentController extends GWTSpringController implements AttachmentRepositoryRemote{
	/**
	 * koeyyeung
	 * Refactored on Mar 21, 201410:45:32 AM
	 */
	private static final long serialVersionUID = -1314128961366293855L;
	@Autowired
	private AttachmentService attachmentRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	//Repackaging Attachment
	public Integer addRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo,
			String fileName) throws Exception {
		return attachmentRepository.addRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName);
	}

	public Boolean deleteRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception {
		return attachmentRepository.deleteRepackagingAttachment(repackagingEntryID, sequenceNo);
	}

	public String getRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception {
		return attachmentRepository.getRepackagingTextAttachment(repackagingEntryID, sequenceNo);
	}

	public List<RepackagingAttachment> getRepackagingAttachments(Long repackagingEntryID) throws Exception {
		return attachmentRepository.getRepackagingAttachments(repackagingEntryID);
	}

	public Boolean saveRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String content)
			throws Exception {
		return attachmentRepository.saveRepackagingTextAttachment(repackagingEntryID, sequenceNo, content);
	}
	
	//Main Contract Certificate Attachment
	/**
	 * @author tikywong
	 * created on January 20, 2012
	 */
	public String getMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException {
		return attachmentRepository.getMainCertTextAttachment(jobNumber, mainCertNumber, sequenceNo);
	}

	/**
	 * @author tikywong
	 * created on January 26, 2012
	 */
	public Boolean addMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String fileName) throws DatabaseOperationException {
		return attachmentRepository.addMainCertTextAttachment(jobNumber, mainCertNumber, sequenceNo, fileName);
	}

	/**
	 * @author tikywong
	 * created on January 26, 2012
	 */
	public Boolean saveMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String text) throws DatabaseOperationException {
		return attachmentRepository.saveMainCertTextAttachment(jobNumber, mainCertNumber, sequenceNo, text);
	}

	//SC Pacakge Attachment (SC, SC Detail, SC Payment)
	/**
	 * @author tikywong
	 * created on January 26, 2012
	 */
	public Boolean deleteMainCertAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException {
		return attachmentRepository.deleteMainCertAttachment(jobNumber, mainCertNumber, sequenceNo);
	}
	
	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception{
		return attachmentRepository.deleteAttachment(nameObject, textKey, sequenceNumber);
	}

	public List<? extends AbstractAttachment> getAttachmentList(String nameObject,String textKey) throws Exception{
		return attachmentRepository.getAttachmentListForPCMS(nameObject, textKey);
	}

	public String getTextAttachmentContent(String nameObject,String textKey, Integer sequenceNumber) throws Exception{
		return attachmentRepository.obtainTextAttachmentContent(nameObject, textKey, sequenceNumber);
	}

	public Boolean uploadTextAttachment(String nameObject, String textKey, Integer sequenceNo, String textContent, String createdUser) throws Exception{
		return attachmentRepository.uploadTextAttachment(nameObject, textKey, sequenceNo, textContent,createdUser);
	}

}
