package com.gammon.qs.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import com.gammon.qs.util.FileUtil;

/**
 * koeyyeung
 * Feb 7, 2014 2:49:34 PM
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageBoardAttachmentService{
	private Logger logger = Logger.getLogger(MessageBoardAttachmentService.class.getName());
	@Autowired
	private AttachmentConfig serviceConfig;
	@Autowired
	private MessageBoardHBDao messageBoardDao;
	@Autowired
	private MessageBoardAttachmentHBDao messageBoardAttachmentDao;
	
	public List<MessageBoardAttachment> obtainAttachmentListByMessageID(long messageBoardID)throws DatabaseOperationException {
		List<MessageBoardAttachment> imageIDList = messageBoardAttachmentDao.obtainAttachmentListByMessageID(messageBoardID);
		return imageIDList;
	}
	
	public List<MessageBoardAttachment> obtainAttachmentListByID(long messageBoardID) throws DatabaseOperationException {
		List<MessageBoardAttachment> attachmentList= messageBoardAttachmentDao.obtainAttachmentListByID(messageBoardID);
		return attachmentList;
	}

	public Boolean updateAttachments(List<MessageBoardAttachment> attachmentList) throws DatabaseOperationException, ValidateBusinessLogicException {
		boolean updated = false;
		for(MessageBoardAttachment attachment: attachmentList){
			
			if(attachment.getSequenceNo()==null){
				logger.info("Vallidation 1a: Please enter Sequence Number.");
	            throw new ValidateBusinessLogicException("Validation Exception: Please enter Sequence Number.");	
			}
			
			if(GenericValidator.isBlankOrNull(attachment.getDocType())){
				logger.info("Vallidation 3a: Please enter Doc. Type.");
	            throw new ValidateBusinessLogicException("Validation Exception: Please enter Doc. Type.");	
			}
			if(attachment.getDocType().length()>10){
				logger.info("Validation 3b: Doc. Type is longer than 10 characters.");
				throw new ValidateBusinessLogicException("Validation Exception: Doc. Type is longer than 10 characters.");
			}
			
			MessageBoardAttachment dbMessageBoardAttachment = messageBoardAttachmentDao.get(attachment.getId());
			if(dbMessageBoardAttachment!=null){
				dbMessageBoardAttachment.setSequenceNo(attachment.getSequenceNo());
				dbMessageBoardAttachment.setDocType(attachment.getDocType());
				messageBoardAttachmentDao.update(dbMessageBoardAttachment);
				updated=true;
			}
		}
		return updated;
	}

	public Boolean deleteAttachments(List<Long> attachmentIDList) throws DatabaseOperationException {
		boolean deleted = false;
		for(Long attachmentID: attachmentIDList){
			MessageBoardAttachment attachmentToDelete = messageBoardAttachmentDao.get(attachmentID);
			//Delete database record
			if(attachmentToDelete!=null){
				messageBoardAttachmentDao.delete(attachmentToDelete);
				deleted = true;
			}
			
			//Remove file in directory
			String messageBoardAttachmentServerPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getMessageBoardDirectory();
			String attachmentPath = messageBoardAttachmentServerPath + "tableID["+attachmentToDelete.getMessageBoard().getId()+"]\\";
			File file = new File(attachmentPath+attachmentToDelete.getFilename());
			file.delete();
		}
		return deleted;
	}

	public String uploadAttachment(byte[] file, String filename, String messageBoardID, String docType) throws IOException {
		logger.info("Upload Attachment For Message Board...");
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("jpg");
		fileTypeList.add("png");
		fileTypeList.add("gif");
		fileTypeList.add("ico");
		fileTypeList.add("bmp");

		String message = null;
		try {
			if(MessageBoardAttachment.IMAGE_DOC_TYPE.equals(docType) && !fileTypeList.contains(filename.substring(filename.indexOf(".")+1)))
				return message = "Please import image with the file type: .jpg, .png, .gif, .ico, .bmp";

			String messageBoardAttachmentServerPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getMessageBoardDirectory();
			String attachmentPath = messageBoardAttachmentServerPath + "tableID["+messageBoardID+"]\\";
		
			MessageBoardAttachment existingAttachment = messageBoardAttachmentDao.obtainAttachmentByFilename(Long.valueOf(messageBoardID), filename);
			if(existingAttachment!=null)
				return message = "The file: "+filename+" is already existed in the system. The file cannot be saved.";
			
			/*-----------------Save file to directory--------------*/
			File processedFolder = new File(attachmentPath);
			if (!processedFolder.exists())
				processedFolder.mkdirs();
			
			 //convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream(attachmentPath+filename); 
			fileOuputStream.write(file);
			fileOuputStream.close();

			/*-----------------Insert record to database------------*/
			MessageBoardAttachment newAttachment = new MessageBoardAttachment();
			if(filename.length()>100)
				filename = filename.substring(0, 100);
			newAttachment.setFilename(filename);
			newAttachment.setDocType(docType);

			MessageBoard messageBoard = null;
			Integer seqNo = 0;
			if(messageBoardID!=null && !"".equals(messageBoardID)){
				messageBoard = messageBoardDao.get(Long.valueOf(messageBoardID));
				seqNo = messageBoardAttachmentDao.obtainAttachmentSeqNoByID(Long.valueOf(messageBoardID));
			}
			
			newAttachment.setMessageBoard(messageBoard);
			if(seqNo==null)
				newAttachment.setSequenceNo(1);
			else
				newAttachment.setSequenceNo(seqNo+1);
			
			messageBoardAttachmentDao.insert(newAttachment);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Fail to Upload Attachment For Message Board. Error: "+e);
			return message = "Fail to Upload Attachment.";
		}
		return message;
	}

	public byte[] generateBytesForImage(Long imageID) {
		byte[] fileBytes = null;
		try {
			MessageBoardAttachment attachment = messageBoardAttachmentDao.get(imageID);
			if (attachment != null) {
				String fileName = attachment.getFilename();
				
				String messageBoardAttachmentServerPath = serviceConfig.getAttachmentServerPath()+serviceConfig.getMessageBoardDirectory();
				String attachmentPath = messageBoardAttachmentServerPath + "tableID["+attachment.getMessageBoard().getId()+"]\\" + fileName;
				logger.info("attachmentPath : " + attachmentPath);

				// grab image from attachment server
				File image = new File(attachmentPath);
				if (!image.exists())
					logger.info("File does not exist. Path: " + attachmentPath);

				// convert to bytes
				fileBytes = FileUtil.readFile(image);
			}
		} catch (Exception e) {
			logger.info("Error occurs in generating the image.");
			e.printStackTrace();
		}
		return fileBytes;
	}

}
