package com.gammon.qs.service;

import com.gammon.pcms.dao.ConsultancyAgreementRepository;
import com.gammon.pcms.model.ConsultancyAgreement;
import com.gammon.pcms.model.hr.HrUser;
import com.gammon.pcms.service.HrService;
import com.gammon.pcms.wrapper.ConsultancyAgreementFormWrapper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.APWebServiceConnectionDao;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.MasterListWSDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.service.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class ConsultancyAgreementService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConsultancyAgreementRepository consultancyAgreementRepository;

    @Autowired
    private SubcontractService subcontractService;

    @Autowired
    private MasterListWSDao masterListWSDao;

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private APWebServiceConnectionDao apWebServiceConnectionDao;

    @Autowired
    private HrService hrService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private AccountCodeWSDao accountCodeWSDao;

    public ConsultancyAgreement getMemo(String jobNo, String subcontractNo) throws DatabaseOperationException {
        Subcontract subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
        if (subcontract == null) {
            return null;
        }
        return consultancyAgreementRepository.findBySubcontractId(subcontract);
    }

    public String saveMemo(String jobNo, String subcontractNo, ConsultancyAgreement ca) {
        try {
        	Subcontract subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
            Integer status = subcontract.getSubcontractStatus();
            if (status != null) {
                String subcontractStatus = status.toString();
                if (subcontractStatus.equals(Subcontract.SCSTATUS_330_AWARD_SUBMITTED)) {
                	 return "Job "+jobNo +" - Subcontract " +subcontractNo +" is Submitted";
                } else if (subcontractStatus.equals(Subcontract.SCSTATUS_500_AWARDED)) {
                	 return "Job "+jobNo +" - Subcontract " +subcontractNo +" is Awarded";
                }
            }

            // check if ca exists
            ConsultancyAgreement existingCA = getMemo(jobNo, subcontractNo);
            if (existingCA == null) {
                ca.setIdSubcontract(subcontract);
                ca.setDateSubmission(new Date());
                ca.setDateApproval(new Date());
                ca.setStatusApproval(ConsultancyAgreement.PENDING);
            }
            consultancyAgreementRepository.save(ca);

        } catch (Exception e) {
            logger.error("Error in saveMemo", e);
            return "Memo cannot be saved";
        }
        return "";
    }

    public ConsultancyAgreementFormWrapper getFormSummary(String jobNo, String subcontractNo) {
        ConsultancyAgreementFormWrapper wrapper = new ConsultancyAgreementFormWrapper();

        try {
            ConsultancyAgreement memo = getMemo(jobNo, subcontractNo);
            Subcontract subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);


            String company = subcontract.getJobInfo().getCompany();
            MasterListVendor companyName = masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()) == null ? new MasterListVendor() : masterListWSDao.getVendorDetailsList((new Integer(company)).toString().trim()).get(0);
            String companyNameStr = companyName.getVendorName() != null ? companyName.getVendorName().trim() : companyName.getVendorName();
            wrapper.setCompanyName(companyNameStr);
            wrapper.setRef(memo.getRef());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            wrapper.setDate(sdf.format(memo.getDateSubmission()));

            // optimise performance with hashmap
            Map<String, String> userHashMap = new HashMap<>();
            List<HrUser> userList = hrService.findByUsernameIsNotNull();
            for (HrUser user : userList) {
                userHashMap.put(user.getUsername(), user.getFullName());
            }

            // convert from username to full name
            if (memo.getFromList() != null) {
                String[] fromList = memo.getFromList().split(";");
                for (int i = 0; i < fromList.length; i++) {
                    String fullname = userHashMap.get(fromList[i]);
                    fromList[i] = fullname;
                }
                wrapper.setFrom(String.join(";", fromList));
            }
            if (memo.getToList() != null) {
                String[] toList = memo.getToList().split(";");
                for (int i = 0; i < toList.length; i++) {
                    String fullname = userHashMap.get(toList[i]);
                    toList[i] = fullname;
                }
                wrapper.setTo(String.join(";", toList));
            }
            if (memo.getCcList() != null) {
                String[] ccList = memo.getCcList().split(";");
                for (int i = 0; i < ccList.length; i++) {
                    String fullname = userHashMap.get(ccList[i]);
                    ccList[i] = fullname;
                }
                wrapper.setCc(String.join(";", ccList));
            }
            wrapper.setSubject(memo.getSubject());
            wrapper.setConsultantName(memo.getConsultantName());
            wrapper.setServiceDescription(memo.getDescription());
            wrapper.setPeriodOfAppointment(memo.getPeriod());
            wrapper.setFeeEstimate(memo.getFeeEstimate());
            wrapper.setBudget(memo.getBudgetAmount());
            wrapper.setExplanation(memo.getExplanation());
            wrapper.setStatusApproval(memo.getStatusApproval());
        } catch (Exception e) {
            logger.error("Error in getFormSummary", e);
        }

        return wrapper;
    }

   

    public String submitCAApproval(String jobNo, String subcontractNo) {
        String result = "";
        try {
            ConsultancyAgreement ca = getMemo(jobNo, subcontractNo);

            if (ca.getStatusApproval().equals(ConsultancyAgreement.SUBMITTED) || ca.getStatusApproval().equals(ConsultancyAgreement.APPROVED)) {
                return "Already submitted";
            }
            if (ca.getFeeEstimate() == null) {
                return "Fee Estimate is not set";
            }
            
           
            Subcontract subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
            Integer status = subcontract.getSubcontractStatus();
            if (status != null) {
                String subcontractStatus = status.toString();
                if (subcontractStatus.equals(Subcontract.SCSTATUS_330_AWARD_SUBMITTED)) {
                	 return "Job "+jobNo +" - Subcontract " +subcontractNo +" is Submitted";
                } else if (subcontractStatus.equals(Subcontract.SCSTATUS_500_AWARDED)) {
                	 return "Job "+jobNo +" - Subcontract " +subcontractNo +" is Awarded";
                }
            }
          

            JobInfo job = jobInfoService.obtainJob(jobNo);
            String currency = accountCodeWSDao.obtainCurrencyCode(jobNo);
            String approvalType = ConsultancyAgreement.CA;
            String approvalSubType = subcontract.getApprovalRoute();
            result = apWebServiceConnectionDao.createApprovalRoute(job.getCompany(), jobNo, subcontractNo, "0", "", approvalType, approvalSubType, ca.getFeeEstimate().doubleValue(), currency, securityService.getCurrentUser().getUsername(), null);
            if (result == null || "".equals(result.trim())) {
                logger.info("Create Approval Route Message: " + result);
                ca.setStatusApproval(ConsultancyAgreement.SUBMITTED);
            }
        } catch (Exception e) {
            logger.error("Error in submitCAApproval", e);
            result = "fail to submit consultancy agreement approval";
        }
        return result;
    }

    public String updateConsultancyAgreementAdmin(String jobNo, String subcontractNo, ConsultancyAgreement ca) {
        try {
            // check if ca exists
            Subcontract subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
            ca.setIdSubcontract(subcontract);
            consultancyAgreementRepository.save(ca);

        } catch (Exception e) {
            logger.error("Error in updateConsultancyAgreementAdmin", e);
            return "Memo cannot be saved";
        }
        return "";
    }

}
