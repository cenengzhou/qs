
package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.domain.MessageBoardAttachment;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * koeyyeung
 * Feb 7, 2014 2:55:56 PM
 */
public interface MessageBoardAttachmentRepositoryRemote extends RemoteService{
	public List<MessageBoardAttachment> obtainAttachmentListByMessageID(long messageBoardID)	throws DatabaseOperationException;
	public List<MessageBoardAttachment> obtainAttachmentListByID(long messageBoardID) throws DatabaseOperationException;
	
	public Boolean updateAttachments(List<MessageBoardAttachment> attachmentList) throws DatabaseOperationException, ValidateBusinessLogicException;
	public Boolean deleteAttachments(List<Long> attachmentIDList) throws DatabaseOperationException;
}
