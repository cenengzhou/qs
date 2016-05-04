package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.client.repository.MessageBoardAttachmentRepositoryRemote;
import com.gammon.qs.domain.MessageBoardAttachment;
import com.gammon.qs.service.MessageBoardAttachmentService;

import net.sf.gilead.core.PersistentBeanManager;

/**
 * koeyyeung
 * Feb 7, 2014 2:49:34 PM
 */
@Service
public class MessageBoardAttachmentController extends GWTSpringController implements MessageBoardAttachmentRepositoryRemote{

	private static final long serialVersionUID = -4198688567427848509L;
	@Autowired
	private MessageBoardAttachmentService messageBoardAttachmentRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<MessageBoardAttachment> obtainAttachmentListByMessageID(long messageBoardID) throws DatabaseOperationException {
		return messageBoardAttachmentRepository.obtainAttachmentListByMessageID(messageBoardID);
	}

	public List<MessageBoardAttachment> obtainAttachmentListByID(long messageBoardID) throws DatabaseOperationException {
		return messageBoardAttachmentRepository.obtainAttachmentListByID(messageBoardID);
	}
	
	public Boolean updateAttachments(List<MessageBoardAttachment> attachmentList) throws DatabaseOperationException, ValidateBusinessLogicException {
		return messageBoardAttachmentRepository.updateAttachments(attachmentList);
	}

	public Boolean deleteAttachments(List<Long> attachmentIDList) throws DatabaseOperationException {
		return messageBoardAttachmentRepository.deleteAttachments(attachmentIDList);
	}


}
