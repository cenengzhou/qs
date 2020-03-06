package com.gammon.pcms.dto.rs.provider.response.subcontract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.model.Attachment;
import com.gammon.pcms.model.Comment;

public class SubcontractDate {
	
	private String field;
	private int order;
	private String attachmentGroup;
	private String group;
	private String description;
	private Date date;
	private List<Attachment> attachmentList = new ArrayList<>();
	private List<Comment> commentList = new ArrayList<>();

	public static final String SCDATE_GROUP_CD = "CD";
	public static final String SCDATE_GROUP_STI = "STI";
	public static final String SCDATE_ATTACHMENT_TOP = "TOP";
	public static final String SCDATE_ATTACHMENT_MIDDLE = "MIDDLE";
	public static final String SCDATE_ATTACHMENT_BOTTOM = "BOTTOM";
	public static final String SCDATE_requisitionApprovedDate = "requisitionApprovedDate";
	public static final String SCDATE_tenderAnalysisApprovedDate = "tenderAnalysisApprovedDate";
	public static final String SCDATE_preAwardMeetingDate = "preAwardMeetingDate";
	public static final String SCDATE_loaSignedDate = "loaSignedDate";
	public static final String SCDATE_scDocScrDate = "scDocScrDate";
	public static final String SCDATE_scDocLegalDate = "scDocLegalDate";
	public static final String SCDATE_workCommenceDate = "workCommenceDate";
	public static final String SCDATE_onSiteStartDate = "onSiteStartDate";
	public static final String SCDATE_scFinalAccDraftDate = "scFinalAccDraftDate";
	public static final String SCDATE_scFinalAccSignoffDate = "scFinalAccSignoffDate";
	public static final String SCDATE_scCreatedDate = "scCreatedDate";
	public static final String SCDATE_scAwardApprovalRequestSentDate = "scAwardApprovalRequestSentDate";
	public static final String SCDATE_scApprovalDate = "scApprovalDate";
	public static final String SCDATE_latestAddendumValueUpdatedDate = "latestAddendumValueUpdatedDate";
	public static final String SCDATE_firstPaymentCertIssuedDate = "firstPaymentCertIssuedDate";
	public static final String SCDATE_lastPaymentCertIssuedDate = "lastPaymentCertIssuedDate";
	public static final String SCDATE_finalPaymentIssuedDate = "finalPaymentIssuedDate";

	private SubcontractDate() {}
	
	private SubcontractDate config(String field, int order, String group, String attachmentGroup, String description) {
		this.field = field;
		this.order = order;
		this.attachmentGroup = attachmentGroup;
		this.group = group;
		this.description = description;
		return this;
	}
	
	public static SubcontractDate getInstance(String type, Date date) {
		SubcontractDate scdate = getInstance(type);
		scdate.date = date;
		return scdate;
	}
	
	public static SubcontractDate getInstance(String type) {
		switch(type) {
		case SCDATE_requisitionApprovedDate:
			return new SubcontractDate().config(SCDATE_requisitionApprovedDate, 1, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_MIDDLE, "Subcontract Requisition Approved");
		case SCDATE_tenderAnalysisApprovedDate:
			return new SubcontractDate().config(SCDATE_tenderAnalysisApprovedDate, 2, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_MIDDLE, "Subcontract Tender Analysis Approved");
		case SCDATE_preAwardMeetingDate:
			return new SubcontractDate().config(SCDATE_preAwardMeetingDate, 3, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_MIDDLE, "Pre-Award Finalization Meeting");
		case SCDATE_loaSignedDate:
			return new SubcontractDate().config(SCDATE_loaSignedDate, 4, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_TOP, "Letter of Acceptance Signed by Subcontractor");
		case SCDATE_scDocScrDate:
			return new SubcontractDate().config(SCDATE_scDocScrDate, 5, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_TOP, "Subcontract Document Executed by Subcontractor");
		case SCDATE_scDocLegalDate:
			return new SubcontractDate().config(SCDATE_scDocLegalDate, 6, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_MIDDLE, "Subcontract Document Executed by Legal");
		case SCDATE_workCommenceDate:
			return new SubcontractDate().config(SCDATE_workCommenceDate, 7, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_TOP, "Works Commencement");
		case SCDATE_onSiteStartDate:
			return new SubcontractDate().config(SCDATE_onSiteStartDate, 8, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_TOP, "Subcontractor Start on-site");
		case SCDATE_scFinalAccDraftDate:
			return new SubcontractDate().config(SCDATE_scFinalAccDraftDate, 9, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_MIDDLE, "Subcontract Final Account (Signed by Sub-Contractor)");
		case SCDATE_scFinalAccSignoffDate:
			return new SubcontractDate().config(SCDATE_scFinalAccSignoffDate, 10, SCDATE_GROUP_CD, SCDATE_ATTACHMENT_MIDDLE, "Subcontract Final Account (Formal Agreement)");
		case SCDATE_scCreatedDate:
			return new SubcontractDate().config(SCDATE_scCreatedDate, 11, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "Subcontract Created");
		case SCDATE_scAwardApprovalRequestSentDate:
			return new SubcontractDate().config(SCDATE_scAwardApprovalRequestSentDate, 12, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "Subcontract Award Approval Request Sent out");
		case SCDATE_scApprovalDate:
			return new SubcontractDate().config(SCDATE_scApprovalDate, 13, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "Subcontract Award Approval");
		case SCDATE_latestAddendumValueUpdatedDate:
			return new SubcontractDate().config(SCDATE_latestAddendumValueUpdatedDate, 14, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "Latest Addendum Approval");
		case SCDATE_firstPaymentCertIssuedDate:
			return new SubcontractDate().config(SCDATE_firstPaymentCertIssuedDate, 15, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "1st Payment Certificate Issued");
		case SCDATE_lastPaymentCertIssuedDate:
			return new SubcontractDate().config(SCDATE_lastPaymentCertIssuedDate, 16, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "Latest Payment Certificate Issued");
		case SCDATE_finalPaymentIssuedDate:
			return new SubcontractDate().config(SCDATE_finalPaymentIssuedDate, 17, SCDATE_GROUP_STI, SCDATE_ATTACHMENT_BOTTOM, "Final Payment Certificate Issued");
		default:
			throw new IllegalArgumentException("no SubcontractDate for type:" + type);
		}
	}

	public String getField() {
		return field;
	}
//	public void setField(String field) {
//		this.field = field;
//	}
	public int getOrder() {
		return order;
	}
//	public void setOrder(int order) {
//		this.order = order;
//	}
	public String getAttachmentGroup() {
		return attachmentGroup;
	}
//	public void setAttachmentGroup(String attachmentGroup) {
//		this.attachmentGroup = attachmentGroup;
//	}
	public String getGroup() {
		return group;
	}
//	public void setGroup(String group) {
//		this.group = group;
//	}
	
	public String getDescription() {
		return description;
	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
	public Date getDate() {
		return date;
	}
//	public void setDate(Date date) {
//		this.date = date;
//	}
	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
}
