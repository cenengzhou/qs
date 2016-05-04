package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.TransitRepositoryRemote;
import com.gammon.qs.domain.TransitBQ;
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.domain.TransitUomMatch;
import com.gammon.qs.service.transit.TransitService;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.TransitHeaderResultWrapper;
import com.gammon.qs.wrapper.TransitResourceWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class TransitRepositoryController extends GWTSpringController implements
		TransitRepositoryRemote {

	private static final long serialVersionUID = 6158912276759955857L;
	//	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	private TransitService transitRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public TransitHeaderResultWrapper createOrUpdateTransitHeader(String jobNumber,
			String estimateNo, String matchingCode, boolean newJob) throws Exception {
		return transitRepository.createOrUpdateTransitHeader(jobNumber, estimateNo, matchingCode, newJob);
	}

	public TransitHeader getTransitHeader(String jobNumber) throws Exception {
		return transitRepository.getTransitHeader(jobNumber);
	}
	
	public PaginationWrapper<TransitBQ> getTransitBQItemsNewSearch(
			String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String description) throws Exception {
		return transitRepository.getTransitBQItemsNewSearch(jobNumber, billNo, subBillNo, pageNo, itemNo, description);
	}
	
	public PaginationWrapper<TransitBQ> getTransitBQItemsByPage(int pageNum) {
		return transitRepository.getTransitBQItemsByPage(pageNum); 
	}

	public PaginationWrapper<TransitResourceWrapper> searchTransitResourcesByPage(
			String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String resourceCode, String objectCode,
			String subsidiaryCode, String description, int pageNum)
			throws Exception {
		return transitRepository.searchTransitResourcesByPage(jobNumber, billNo, subBillNo, pageNo, itemNo, resourceCode, objectCode, subsidiaryCode, description, pageNum);
	}

	public String saveTransitResources(String jobNumber,
			List<TransitResource> resources) throws Exception {
		return transitRepository.saveTransitResources(jobNumber, resources);
	}

	public String confirmResourcesAndCreatePackages(String jobNumber)
			throws Exception {
		return transitRepository.confirmResourcesAndCreatePackages(jobNumber);
	}

	public String completeTransit(String jobNumber) throws Exception {
		return transitRepository.completeTransit(jobNumber);
	}

	// last modified: Brian Tse
	public Boolean allowPrint(String jobNumber){
		return transitRepository.allowPrint(jobNumber);
	}

	public PaginationWrapper<TransitCodeMatch> searchTransitCodeMatches(
			String matchingType, String resourceCode, String objectCode,
			String subsidiaryCode) throws Exception {
		return transitRepository.searchTransitCodeMatches(matchingType, resourceCode, objectCode, subsidiaryCode);
	}

	public PaginationWrapper<TransitCodeMatch> searchTransitCodeMatchesByPage(
			int pageNum) {
		return transitRepository.searchTransitCodeMatchesByPage(pageNum);
	}

	public PaginationWrapper<TransitUomMatch> searchTransitUomMatches(
			String causewayUom, String jdeUom) throws Exception {
		return transitRepository.searchTransitUomMatches(causewayUom, jdeUom);
	}

	public PaginationWrapper<TransitUomMatch> searchTransitUomMatchesByPage(
			int pageNum) {
		return transitRepository.searchTransitUomMatchesByPage(pageNum);
	}

	// added by brian on 20110301
	public List<TransitHeader> getAllTransitHeaders(String status) {
		return transitRepository.getAllTransitHeaders(status);
	}
}
