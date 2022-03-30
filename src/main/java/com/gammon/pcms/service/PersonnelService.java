package com.gammon.pcms.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dao.PersonnelMapRepository;
import com.gammon.pcms.dao.PersonnelRepository;
import com.gammon.pcms.dto.rs.provider.response.eform.EFormResponseDTO;
import com.gammon.pcms.model.Attachment;
import com.gammon.pcms.model.Personnel;
import com.gammon.pcms.model.PersonnelMap;
import com.gammon.pcms.task.ApprovalStartTask;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.AttachmentService;
import com.gammon.qs.service.JobInfoService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PersonnelService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EformService eformService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private PersonnelRepository personnelRepository;
	@Autowired
	private PersonnelMapRepository personnelMapRepository;
	@Autowired
	private HTMLService htmlService;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private AttachmentService attachmentService;	
	@Autowired
    private ThreadPoolTaskScheduler taskScheduler;
	
	public void replaceAll(String jobNo, List<Personnel> personnelList) {
		JobInfo jobInfo;
		try {
			jobInfo = jobInfoService.obtainJob(jobNo);
			jobInfo.approvalApproved();
			jobInfo.setNoReference(null);
			jobInfoService.saveOrUpdate(jobInfo);
			personnelList.forEach(personnel -> {
				PersonnelMap personnelMap = personnelMapRepository.findOne(personnel.getPersonnelMap().getId());
				personnel.setJobInfo(jobInfo);
				personnel.setPersonnelMap(personnelMap);
			});
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		List<Personnel> currentList = personnelRepository.findByJobInfo_JobNumberAndStatusAndAction(jobNo, Personnel.STATUS.ACTIVE.name(), null);
		inactiveList(currentList);
		personnelRepository.save(personnelList);
	}
	
	public void delete(Long id) {
		personnelRepository.delete(id);
	}
	
	public void save(String jobNo, Personnel personnel) {
		if(personnel.getStatus() == null) personnel.statusActive();
			PersonnelMap personnelMap = null;
			JobInfo jobInfo = null;
			try {
				jobInfo = jobInfoService.obtainJob(jobNo);
				personnelMap = personnelMapRepository.findOne(personnel.getPersonnelMap().getId());
				personnel.setJobInfo(jobInfo);
				personnel.setPersonnelMap(personnelMap);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
			if(personnelMap != null && jobInfo != null)
			if("Y".equals(personnelMap.getRequiredApproval())) {
				if(StringUtils.isBlank(jobInfo.getStatusApproval()))jobInfo.approvalPending();
				if(personnel.getUserAd() != null && personnel.getUserAd().equals(personnel.getUserAdToBeApproved())) {
					personnel.setUserAdToBeApproved(null);
					personnel.actionNone();
				}
				switch(Personnel.ACTION.valueOf(personnel.getAction())) {
				case DELETE:
					if(StringUtils.isBlank(personnel.getUserAd()) && StringUtils.isBlank(personnel.getUserAdToBeApproved())) {
						personnelRepository.delete(personnel);
					} else {
						personnelRepository.save(personnel);
					}
					break;
				default:
					personnelRepository.save(personnel);	
					break;
				}	
			} else {
				switch(Personnel.ACTION.valueOf(personnel.getAction())) {
				case ADD:
					if(StringUtils.isNotBlank(personnel.getUserAdToBeApproved())) {
						personnel.setUserAd(personnel.getUserAdToBeApproved());
						personnel.setUserAdToBeApproved(null);
						personnel.setUserAdPrevious(null);
						personnel.actionNone();
						personnelRepository.save(personnel);	
					}
					break;
				case UPDATE:
					if(StringUtils.isNotBlank(personnel.getUserAdToBeApproved())) {
						personnel.setUserAdPrevious(personnel.getUserAd());
						personnel.setUserAd(personnel.getUserAdToBeApproved());
						personnel.setUserAdToBeApproved(null);
					}
					personnel.actionNone();
					personnelRepository.save(personnel);
					break;
				case DELETE:
					personnelRepository.delete(personnel);
					break;
				default:
					personnelRepository.save(personnel);
					break;
				}
			}
	}
	
	// create/modify personnel record
	// draft without eform refNo
	public void save(String jobNo, List<Personnel> personnelList) {
		personnelList.forEach((personnel) -> {
			save(jobNo, personnel);
		});
	}

	// reverse all pending personnel record of the job
	public void clearDraft(String jobNo) {
		List<Personnel> pendingChangeList = personnelRepository.findByJobInfo_JobNumberAndStatusAndActionNot(jobNo, Personnel.STATUS.ACTIVE.name(), "NONE");
		if(pendingChangeList.size() > 0) {
			JobInfo jobInfo = pendingChangeList.get(0).getJobInfo();
			if (JobInfo.APPROVAL_STATUS.SUBMITTED.name().equals(jobInfo.getStatusApproval())) {
				eformService.processAbort(jobInfo.getNoReference());
				jobInfo.approvalNull();
				jobInfo.setNoReference(null);
				jobInfoService.saveOrUpdate(jobInfo);
			}
			pendingChangeList.forEach(personnel -> {
				switch(Personnel.ACTION.valueOf(personnel.getAction())) {
				case ADD:
					personnelRepository.delete(personnel);
					break;
				case UPDATE:
					personnel.setUserAdToBeApproved(null);
					personnel.setAction(null);
					break;
				case DELETE:
					personnel.setAction(null);
					break;
				case NONE:
					break;
				}
			});
		}
	}

	// submit change for approval
	public Long submitForApproval(String jobNo) throws Exception {
		List<Personnel> pendingChangeList = personnelRepository.findByJobInfo_JobNumberAndStatusAndActionNot(jobNo, Personnel.STATUS.ACTIVE.name(), "NONE");
		Long refNo = null;
		try{
			if(pendingChangeList.size() > 0 ) {
				
				// call process start to get refNo
				String formCode = webServiceConfig.getWsWfParam(EformService.PERSONNEL_CODE_KEY);
				Map<String, String> requestObject = new HashMap<>();
				requestObject.put(EformService.JOBNO_KEY, jobNo);
				requestObject.put(EformService.REQUESTER_KEY, SecurityContextHolder.getContext().getAuthentication().getName());
				refNo = eformService.processStart(formCode, requestObject);	
				
				// regenerate html / pdf base on pendingChangeList
				generateHtmlPdf(formCode, jobNo, refNo);
				
				// change status to SUBMITTED/refNo
				JobInfo jobInfo = pendingChangeList.get(0).getJobInfo();
				jobInfo.setNoReference(refNo);
				jobInfo.approvalSubmitted();
				jobInfoService.saveOrUpdate(jobInfo);
				
				ApprovalStartTask.createApprovalStartTask(refNo, this, taskScheduler, 0, 0);
			}
		} catch (Exception e) {
			logger.error("cannot submitForApproval:", e);
		}
		return refNo;
	}
	
	public Map<String, Object> approvalStart(Long refNo) throws Exception {
		// call approval start
		logger.info("approvalStart start:" + refNo);
		Map<String, Object> result = eformService.approvalStart(refNo);
		logger.info("approvalStart end:" + refNo + "\r\n" + result.toString());
		return result;
	}

	public EFormResponseDTO completeApproval(Long refNo, String decision, Map<String, Object> returnDecisionMap) {
		JobInfo jobInfo = jobInfoService.getByRefNo(refNo);
		EFormResponseDTO response = new EFormResponseDTO();
		List<Personnel> pendingChangeList = personnelRepository.findByJobInfo_JobNumberAndStatusAndActionNot(jobInfo.getJobNumber(), Personnel.STATUS.ACTIVE.name(), Personnel.ACTION.NONE.name());
		try {
			switch(JobInfo.APPROVAL_STATUS.valueOf(decision)) {
			case APPROVED:
				if(pendingChangeList.size() > 0) {
					pendingChangeList.forEach(personnel -> {
						personnel.setDateApproved(new Date());
						switch(Personnel.ACTION.valueOf(personnel.getAction())) {
						case ADD:
							personnel.setUserAd(personnel.getUserAdToBeApproved());
							personnel.setUserAdToBeApproved(null);
							personnel.actionNone();
							break;
						case UPDATE:
							personnel.setUserAdPrevious(personnel.getUserAd());
							personnel.setUserAd(personnel.getUserAdToBeApproved());
							personnel.setUserAdToBeApproved(null);
							personnel.actionNone();
							break;
						case DELETE:
							personnel.statusInactive();
							break;
						case NONE:
							break;
						}
					});
					jobInfo.approvalApproved();
					String pdfPathString = htmlService.getEformPdfPath((String) returnDecisionMap.get("processName"), refNo, jobInfo.getJobNumber());
					File pdfFile = new File(pdfPathString);
					FileInputStream fis = new FileInputStream(pdfFile);
					attachmentService.uploadAttachment(Attachment.JobInfoNameObject, jobInfo.getJobNumber(), new BigDecimal(refNo), pdfFile.getName(), IOUtils.toByteArray(fis), "SYSTEM");
				}
				response.setMessage("Job personnel of " + jobInfo.getJobNumber() + " has been updated");
				break;
			case REJECTED:
				jobInfo.approvalRejected();
				response.setMessage("update request rejected");
				break;
			default:
				break;
			}
			response.setStatus(EFormResponseDTO.Status.SUCCESS.name());
		} catch (Exception e) {
			logger.error("cannot completeApproval:", e);
			response.setMessage(e.getMessage());
			response.setStatus(EFormResponseDTO.Status.FAILURE.name());
		}
		return response;
	}
	
	public void cancelApproval(Long refNo) {
		eformService.processAbort(refNo);
		JobInfo job = jobInfoService.getByRefNo(refNo);
		job.approvalCancelled();
		jobInfoService.saveOrUpdate(job);
	}
	
	public List<Personnel> getActivePersonnel(String jobNo) {
		return personnelRepository.findByJobInfo_JobNumberAndStatus(jobNo, Personnel.STATUS.ACTIVE.name());
	}
	
	public List<PersonnelMap> getAllPersonnelMap() {
		List<PersonnelMap> personnelMapList = personnelMapRepository.findAll();
//		personnelMapList.forEach(personnelMap -> {
//			Hibernate.initialize(personnelMap.getPersonnels());
//		});
		return personnelMapList.stream().sorted(
			Comparator.comparing(PersonnelMap::getRequiredApproval).reversed()
			.thenComparing(PersonnelMap::getUserGroup)
			.thenComparing(PersonnelMap::getUserSequence)
		)
		.collect(Collectors.toList());
	}
	
	private void inactiveList(List<Personnel> personnelList) {
		personnelList.forEach(personnel -> {
			inactive(personnel);
		});
	}
	
	private void inactive(Personnel personnel) {
		personnel.statusInactive();
		personnelRepository.save(personnel);
	}

	public String getEmailContext(String formCode, String jobNo, Long refNo) throws Exception {
		return htmlService.getEmailContext(formCode, jobNo, refNo);
	}

	public void generateHtmlPdf(String formCode, String jobNo, Long refNo) throws Exception {
		htmlService.generateHtmlPdf(formCode, jobNo, refNo);
	}
	
	public Map<String, Object> returnDecisionMap(Long refNo) {
		return eformService.returnDecisionMap(refNo);
	}
	
	public String returnDecisionMapHtml(Long refNo) {
		return eformService.returnDecisionMapHtml(refNo);
	}

}
