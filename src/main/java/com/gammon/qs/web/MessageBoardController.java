package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.client.repository.MessageBoardRepositoryRemote;
import com.gammon.qs.domain.MessageBoard;
import com.gammon.qs.service.MessageBoardService;
import com.gammon.qs.wrapper.PaginationWrapper;

import net.sf.gilead.core.PersistentBeanManager;

/**
 * koeyyeung
 * Dec 30, 201311:46:27 AM
 */
@Service
public class MessageBoardController extends GWTSpringController implements MessageBoardRepositoryRemote {
	@Autowired
	private MessageBoardService messageBoardRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	private static final long serialVersionUID = 8444264613424218959L;

	public List<MessageBoard> obtainAllDisplayMessages() throws DatabaseOperationException {
		return messageBoardRepository.obtainAllDisplayMessages();
	}

	public PaginationWrapper<MessageBoard> obtainMessageBoardPaginationWrapper(MessageBoard messageBoard) throws DatabaseOperationException {
		return messageBoardRepository.obtainMessageBoardPaginationWrapper(messageBoard);
	}

	public PaginationWrapper<MessageBoard> obtainMessageBoardListByPage(int pageNum) {
		return messageBoardRepository.obtainMessageBoardListByPage(pageNum);
	}

	
	public Boolean updateMessages(List<MessageBoard> messageList) throws DatabaseOperationException, ValidateBusinessLogicException {
		return messageBoardRepository.updateMessages(messageList);
	}

	public Boolean deleteMessages(List<Long> messageIDList) throws DatabaseOperationException {
		return messageBoardRepository.deleteMessages(messageIDList);
	}

}
