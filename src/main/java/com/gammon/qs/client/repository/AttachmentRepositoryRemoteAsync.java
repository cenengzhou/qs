package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.RepackagingAttachment;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AttachmentRepositoryRemoteAsync {
	
	//Repackaging Attachment
	public void getRepackagingAttachments(Long repackagingEntryID, AsyncCallback<List<RepackagingAttachment>> callback);
	public void addRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String fileName, AsyncCallback<Integer> callback);
	public void saveRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, String content, AsyncCallback<Boolean> callback);
	public void getRepackagingTextAttachment(Long repackagingEntryID, Integer sequenceNo, AsyncCallback<String> callback);
	public void deleteRepackagingAttachment(Long repackagingEntryID, Integer sequenceNo, AsyncCallback<Boolean> callback);
	
	//Main Contract Certificate Attachment
	public void getMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, AsyncCallback<String> asyncCallback);
	public void addMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sqeuenceNo, String fileName, AsyncCallback<Boolean> asyncCallback);
	public void saveMainCertTextAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, String text, AsyncCallback<Boolean> asyncCallback);
	public void deleteMainCertAttachment(String jobNumber, Integer mainCertNumber, Integer sequenceNo, AsyncCallback<Boolean> asyncCallback);
	
	//SC Pacakge Attachment (SC, SC Detail, SC Payment)
	public void uploadTextAttachment(String nameObject, String textKey, Integer sequenceNo, String textContent,String createdUser, AsyncCallback<Boolean> callback);
	public void getAttachmentList(String nameObject, String textKey, AsyncCallback<List<? extends AbstractAttachment>> callback);
	public void deleteAttachment(String nameObject, String textKey, Integer sequenceNumber, AsyncCallback<Boolean> callback);
	public void getTextAttachmentContent(String nameObject, String textKey, Integer sequenceNumber, AsyncCallback<String> callback);
}
