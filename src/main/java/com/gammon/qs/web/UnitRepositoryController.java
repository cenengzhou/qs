package com.gammon.qs.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.UnitRepositoryRemote;
import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.service.UnitService;
import com.gammon.qs.wrapper.UDC;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class UnitRepositoryController extends GWTSpringController implements UnitRepositoryRemote {

	private static final long serialVersionUID = 4652926052445643133L;
	
//	private Logger logger = Logger.getLogger(this.getClass().getName());	
	@Autowired
	private UnitService unitRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<UnitOfMeasurement> getUnitOfMeasurementList() throws Exception{
		return unitRepository.getUnitOfMeasurementList();
	}

	public Map<String, String> getAppraisalPerformanceGroupMap() {
		return unitRepository.getAppraisalPerformanceGroupMap();
	}
	
	public Map<String, String> getUDCMap(String productCode, String userDefinedCodes) {
		return unitRepository.getUDCMap(productCode, userDefinedCodes);
	}
	
	public Map<String, String> getSCStatusCodeMap() {
		return unitRepository.getSCStatusCodeMap();
	}

	public List<UDC> getAllWorkScopes() throws DatabaseOperationException {
		return unitRepository.getAllWorkScopes();
	}
}
