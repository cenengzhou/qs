package com.gammon.qs.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.client.repository.IVPostingHistoryRepositoryRemote;
import com.gammon.qs.service.IVPostingHistoryService;
import com.gammon.qs.wrapper.IVHistoryPaginationWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service("ivPostingHistoryController")
public class IVPostingHistoryController extends GWTSpringController implements IVPostingHistoryRepositoryRemote {

	private static final long serialVersionUID = 7041620775345732185L;
	@Autowired
	private IVPostingHistoryService ivPostingHistoryRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public IVHistoryPaginationWrapper obtainIVPostingHistory(String jobNumber,
				String packageNo, String objectCode, String subsidiaryCode,
				Date fromDate, Date toDate) throws Exception {
		return ivPostingHistoryRepository.obtainIVPostingHistory(jobNumber, packageNo, objectCode, subsidiaryCode, fromDate, toDate);
	}
	
	public IVHistoryPaginationWrapper getIVPostingHistoryByPage(int pageNum) throws ValidateBusinessLogicException{
		return ivPostingHistoryRepository.obtainIVPostingHistoryByPage(pageNum);
	}
	
	public Boolean clearCache(){
		return ivPostingHistoryRepository.clearCache();
	}

}
