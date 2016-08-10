package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.AttachRepackaging;
import com.gammon.qs.service.AttachmentService;

@RestController
@RequestMapping(value = "service/attachment/")
public class AttachmentController {

	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping(value = "getRepackagingAttachments", method = RequestMethod.POST)
	public List<AttachRepackaging> getRepackagingAttachments(@RequestParam Long repackagingEntryID) {
		List<AttachRepackaging> resultList = new ArrayList<AttachRepackaging>();
		try{
			resultList.addAll(attachmentService.getRepackagingAttachments(repackagingEntryID));
		} catch (Exception e){
			e.printStackTrace();
		}
		return resultList;
	}
}
