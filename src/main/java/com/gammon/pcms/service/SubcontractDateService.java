package com.gammon.pcms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDate;
import com.gammon.pcms.model.Attachment;
import com.gammon.pcms.model.Comment;
import com.gammon.pcms.model.hr.HrUser;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.SubcontractService;

@Transactional
@Service
public class SubcontractDateService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private HrService hrService;

	public List<SubcontractDate> getScDateList(String jobNumber, String packageNo) {
		Subcontract subcontract;
		try {
			subcontract = subcontractService.obtainSubcontract(jobNumber, packageNo);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("cannout found subcontract:" + jobNumber + "-" + packageNo);
		}
		List<SubcontractDate> dateList = new ArrayList<>();
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_requisitionApprovedDate, subcontract.getRequisitionApprovedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_tenderAnalysisApprovedDate, subcontract.getTenderAnalysisApprovedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_preAwardMeetingDate, subcontract.getPreAwardMeetingDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_loaSignedDate, subcontract.getLoaSignedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scDocScrDate, subcontract.getScDocScrDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scDocLegalDate, subcontract.getScDocLegalDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_workCommenceDate, subcontract.getWorkCommenceDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_onSiteStartDate, subcontract.getOnSiteStartDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scFinalAccDraftDate, subcontract.getScFinalAccDraftDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scFinalAccSignoffDate, subcontract.getScFinalAccSignoffDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scCreatedDate, subcontract.getScCreatedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scAwardApprovalRequestSentDate, subcontract.getScAwardApprovalRequestSentDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_scApprovalDate, subcontract.getScApprovalDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_latestAddendumValueUpdatedDate, subcontract.getLatestAddendumValueUpdatedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_firstPaymentCertIssuedDate, subcontract.getFirstPaymentCertIssuedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_lastPaymentCertIssuedDate, subcontract.getLastPaymentCertIssuedDate()));
		dateList.add(SubcontractDate.getInstance(SubcontractDate.SCDATE_finalPaymentIssuedDate, subcontract.getFinalPaymentIssuedDate()));
		// get attachment
		List<Attachment> attachmentList = attachmentService.obtainSubcontractDateAttachmentList(subcontract.getId());
		// get Comment
		List<Comment> commentList = commentService.obtainCommentList(Attachment.SUBCONTRACT_TABLE, subcontract.getId(), "");
		dateList.forEach(scd -> {
			if(attachmentList != null) scd.setAttachmentList(
					attachmentList.stream().filter(
							attach -> attach.getNameFile().indexOf(scd.getDescription()) > -1
					).collect(Collectors.toList())
				);
			List<Comment> fieldCommentList = commentList.stream().filter(
					comment -> comment.getField().equals(scd.getField())
			).collect(Collectors.toList());
			if(fieldCommentList != null) {
				scd.setCommentList(commentService.SenderObject(fieldCommentList));
			}
		});
		return dateList;
	}
}
