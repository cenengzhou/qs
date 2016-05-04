package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.domain.MessageBoard;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * koeyyeung
 * Dec 30, 201311:45:16 AM
 */
public interface MessageBoardRepositoryRemote extends RemoteService{
	public List<MessageBoard> obtainAllDisplayMessages() throws DatabaseOperationException;
	public PaginationWrapper<MessageBoard> obtainMessageBoardPaginationWrapper(MessageBoard messageBoard) throws DatabaseOperationException;
	public PaginationWrapper<MessageBoard> obtainMessageBoardListByPage(int pageNum);
	
	public Boolean updateMessages(List<MessageBoard> messageList) throws DatabaseOperationException, ValidateBusinessLogicException;
	public Boolean deleteMessages(List<Long> messageIDList) throws DatabaseOperationException;
}
