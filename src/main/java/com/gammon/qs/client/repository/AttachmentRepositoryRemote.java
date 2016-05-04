package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.RepackagingAttachment;
import com.google.gwt.user.client.rpc.RemoteService;

public interface AttachmentRepositoryRemote extends RemoteService {
	//Repackaging Attachment
	public List<RepackagingAttachment> getRepackagingAttachments(Long repackagingEntryID) throws Exception;
	public Integer addRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName) throws Exception;
	public Boolean saveRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String content) throws Exception;
	public String getRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception;
	public Boolean deleteRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo) throws Exception;
	
	//Main Contract Certificate Attachment
	public String getMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException;
	public Boolean addMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String fileName) throws DatabaseOperationException;
	public Boolean saveMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String text) throws DatabaseOperationException;
	public Boolean deleteMainCertAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo) throws DatabaseOperationException;
	
	//SC Pacakge Attachment (SC, SC Detail, SC Payment)
	public Boolean uploadTextAttachment(String nameObject, String textKey, Integer sequenceNo, String textContent, String createdUser) throws Exception;
	public List<? extends AbstractAttachment> getAttachmentList(String nameObject, String textKey) throws Exception;
	public Boolean deleteAttachment(String nameObject, String textKey, Integer sequenceNumber) throws Exception;
	public String getTextAttachmentContent(String nameObject, String textKey, Integer sequenceNumber) throws Exception;
}
