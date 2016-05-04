package com.gammon.qs.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.MessageBoardAttachmentHBDao;
import com.gammon.qs.dao.MessageBoardHBDao;
import com.gammon.qs.domain.MessageBoard;
import com.gammon.qs.domain.MessageBoardAttachment;
import com.gammon.qs.wrapper.PaginationWrapper;

/**
 * koeyyeung
 * Dec 30, 201311:43:35 AM
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageBoardService {
	private Logger logger = Logger.getLogger(MessageBoardService.class.getName());
	@Autowired
	private AttachmentConfig serviceConfig;
	@Autowired
	private MessageBoardHBDao messageBoardDao;
	@Autowired
	private MessageBoardAttachmentHBDao messageBoardAttachmentHBDao;
	
	private List<MessageBoard> cachedMessageBoardList = new ArrayList<MessageBoard>();
	private static final int RECORDS_PER_PAGE = 50;

	public List<MessageBoard> obtainAllDisplayMessages() throws DatabaseOperationException {
		List<MessageBoard> messageList = messageBoardDao.obtainAllDisplayMessages();
		return messageList;
	}

	public List<MessageBoard> obtainDisplayMessagesByType(MessageBoard messageBoard) throws DatabaseOperationException {
		List<MessageBoard> messageList = messageBoardDao.obtainDisplayMessagesByType(messageBoard);
		return messageList;
	}
	
	public PaginationWrapper<MessageBoard> obtainMessageBoardPaginationWrapper(MessageBoard messageBoard) throws DatabaseOperationException {
		logger.info("obtainMessageBoardPaginationWrapper - STARTED");
		List<MessageBoard> messageList = obtainDisplayMessagesByType(messageBoard);
		
		logger.info("obtainMessageBoardPaginationWrapper size: "+messageList.size());
		cachedMessageBoardList = messageList;
		if (cachedMessageBoardList == null)
			return null;
		else
			return obtainMessageBoardListByPage(0);
	} 
	
	public PaginationWrapper<MessageBoard> obtainMessageBoardListByPage(int pageNum) {
		PaginationWrapper<MessageBoard>  wrapper = new PaginationWrapper<MessageBoard>();
		wrapper.setCurrentPage(pageNum);
		int size = cachedMessageBoardList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedMessageBoardList.size())
			toInd = cachedMessageBoardList.size();
		ArrayList<MessageBoard> MessageBoards = new ArrayList<MessageBoard>();
		MessageBoards.addAll(cachedMessageBoardList.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(MessageBoards);
		
		logger.info("obtainMessageBoardPaginationWrapper - END");
		return wrapper;
	}
	
	public Boolean updateMessages(List<MessageBoard> messageList) throws DatabaseOperationException, ValidateBusinessLogicException {
		boolean updated = false;
		for(MessageBoard message: messageList){
			
			if(GenericValidator.isBlankOrNull(message.getTitle())){
				logger.info("Vallidation 1a: Please enter Title.");
	            throw new ValidateBusinessLogicException("Validation Exception: Please enter Title.");	
			}
			if(message.getTitle().length()>100){
				logger.info("Validation 1b: Title is longer than 100 characters.");
				throw new ValidateBusinessLogicException("Validation Exception: Title is longer than 100 characters.");
			}
			
			if(GenericValidator.isBlankOrNull(message.getDescription())){
				logger.info("Vallidation 2a: Please enter Description.");
	            throw new ValidateBusinessLogicException("Validation Exception: Please enter Description.");	
			}
			if(message.getDescription().length()>3000){
				logger.info("Validation 2b: Description is longer than 3000 characters.");
				throw new ValidateBusinessLogicException("Validation Exception: Description is longer than 3000 characters.");
			}
			
			if(!GenericValidator.isBlankOrNull(message.getRequestor()) && message.getRequestor().length()>255){
				logger.info("Validation 3: Requestor is longer than 3000 characters.");
				throw new ValidateBusinessLogicException("Validation Exception: Requestor is longer than 3000 characters.");
			}
			
			MessageBoard dbMessage = null;
			if(message.getId()!=null)
				dbMessage = messageBoardDao.get(message.getId());
			if(dbMessage!=null){//update the message
				dbMessage.setTitle(message.getTitle());
				dbMessage.setDescription(message.getDescription());
				dbMessage.setRequestor(message.getRequestor());
				dbMessage.setDeliveryDate(message.getDeliveryDate());
				dbMessage.setMessageType(message.getMessageType());
				dbMessage.setIsDisplay(message.getIsDisplay());
				messageBoardDao.update(dbMessage);
				updated=true;
			}else{//insert new message
				messageBoardDao.insert(message);
				updated=true;
			}
		}
		return updated;
	}

	public Boolean deleteMessages(List<Long> messageIDList) throws DatabaseOperationException {
		boolean deleted = false;
		for(Long messageID: messageIDList){
			//Delete message board in database(not inactive)
			MessageBoard messageToDelete = messageBoardDao.get(messageID);
			if(messageToDelete!=null){
				messageBoardDao.delete(messageToDelete);
				deleted = true;
			}
			
			List<MessageBoardAttachment> attachmentList =new ArrayList<MessageBoardAttachment>();
			if(messageToDelete!=null)
				attachmentList = messageBoardAttachmentHBDao.obtainAttachmentListByID(messageToDelete.getId());
			
			//Delete the directory of the attachments
			if(attachmentList.size()>0){
				String messageBoardAttachmentServerPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getMessageBoardDirectory();
				String attachmentPath = messageBoardAttachmentServerPath + "tableID["+attachmentList.get(0).getMessageBoard().getId()+"]\\";
				File file = new File(attachmentPath);
				if(file!=null)
					try {
						FileUtils.forceDelete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return deleted;
	}



}
