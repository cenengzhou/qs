package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.User;
import com.gammon.qs.client.repository.RepackagingDetailRepositoryRemote;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.service.RepackagingDetailService;
import com.gammon.qs.wrapper.EmailMessage;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class RepackagingDetailRepositoryController extends GWTSpringController
		implements RepackagingDetailRepositoryRemote {

	private static final long serialVersionUID = 5518975030968156171L;
	@Autowired
	private RepackagingDetailService repackagingDetailRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public Double prepareRepackagingDetails(RepackagingEntry repackagingEntry)
			throws Exception {
		return repackagingDetailRepository.prepareRepackagingDetails(repackagingEntry);
	}

	public RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetailsNewSearch(RepackagingEntry repackagingEntry, String packageNo,
			String objectCode, String subsidiaryCode, boolean changesOnly) throws Exception{
		return repackagingDetailRepository.getRepackagingDetailsNewSearch(repackagingEntry, packageNo, objectCode, subsidiaryCode, changesOnly);
	}
	
	public RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetailsByPage(int pageNum){
		return repackagingDetailRepository.getRepackagingDetailsByPage(pageNum);
	}
	
	public Boolean clearCache(){
		return repackagingDetailRepository.clearCache();
	}
	
	public Boolean postRepackagingDetails(RepackagingEntry repackagingEntry,
			String username) throws Exception {
		return repackagingDetailRepository.postRepackagingDetails(repackagingEntry, username);
	}
	
	public String sendRepackagingConfirmationEmail (EmailMessage emailMessage){
		return repackagingDetailRepository.sendRepackagingConfirmationEmail(emailMessage);
	}
	
	public List<User> obtainRepackagingEmailRecipients (){
		return repackagingDetailRepository.obtainRepackagingEmailRecipients();
	}
	
	public List<String> obtainUsernamesByEmails(List<String> emailAddressList){
		return repackagingDetailRepository.obtainUsernamesByEmails(emailAddressList);
	}

	public RepackagingEntry generateResourceSummaries(Job job) throws Exception {
		return repackagingDetailRepository.generateResourceSummaries(job);
	}
}
