package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.MessageBoardAttachment;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * koeyyeung
 * Feb 7, 2014 2:56:34 PM
 */
public interface MessageBoardAttachmentRepositoryRemoteAsync {
	public void obtainAttachmentListByMessageID(long messageBoardID, AsyncCallback<List<MessageBoardAttachment>> callback);
	public void obtainAttachmentListByID(long messageBoardID, AsyncCallback<List<MessageBoardAttachment>> callback);
	
	public void updateAttachments(List<MessageBoardAttachment> attachmentList, AsyncCallback<Boolean> callback);
	public void deleteAttachments(List<Long> attachmentIDList, AsyncCallback<Boolean> callback);
}
