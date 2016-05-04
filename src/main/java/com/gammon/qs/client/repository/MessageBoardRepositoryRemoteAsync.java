package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.MessageBoard;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * koeyyeung
 * Dec 30, 201311:45:16 AM
 */
public interface MessageBoardRepositoryRemoteAsync {
	public void obtainAllDisplayMessages(AsyncCallback<List<MessageBoard>> callback);
	public void obtainMessageBoardPaginationWrapper(MessageBoard messageBoard, AsyncCallback<PaginationWrapper<MessageBoard>> callback);
	public void obtainMessageBoardListByPage(int pageNum, AsyncCallback<PaginationWrapper<MessageBoard>> callback);
	
	public void updateMessages(List<MessageBoard> messageList, AsyncCallback<Boolean> callback);
	public void deleteMessages(List<Long> messageIDList, AsyncCallback<Boolean> callback);
}
