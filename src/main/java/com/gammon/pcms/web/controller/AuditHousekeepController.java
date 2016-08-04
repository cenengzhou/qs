package com.gammon.pcms.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.config.AuditConfig;
import com.gammon.pcms.config.AuditConfig.AuditInfo;
import com.gammon.pcms.scheduler.service.AuditHousekeepService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.SubcontractService;

@RestController
@RequestMapping(value = "service/audithousekeep/")
public class AuditHousekeepController {

	@Autowired
	private AuditConfig auditConfig;
	@Autowired
	private AuditHousekeepService auditHousekeepService;
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private PaymentService paymentService;
			
	@RequestMapping(value = "getAuditTableMap", method = RequestMethod.POST)
	public Map<String, AuditInfo> getAuditTableMap(){
		return auditConfig.getAuditInfoMap();
	}

	@RequestMapping(value = "housekeepAuditTable", method = RequestMethod.POST)
	public int housekeepAuditTable(@RequestParam String tableName){
		try {
			return auditHousekeepService.housekeekpByAuditTableName(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@RequestMapping(value = "findEntityByIdRevision", method = RequestMethod.POST)
	public @ResponseBody Object findEntityByIdRevision(
			@RequestParam(defaultValue = "com.gammon.qs.domain.Subcontract") String clazz, 
			@RequestParam(defaultValue = "8243") long id, 
			@RequestParam(defaultValue = "1") int rev,
			@RequestParam(defaultValue = "Return specific entity instance of corresponding revision") String description){
		try {
			return auditHousekeepService.findEntityByIdRevision(Class.forName(clazz) , id, rev);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "findRevisionsByEntity", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> findRevisionsByEntity(
			@RequestParam(defaultValue = "com.gammon.qs.domain.Subcontract") String clazz,
			@RequestParam(defaultValue = "Return all entity instance, revision entity, type of the revision corresponding to the revision at which the entity was modified.") String description){
		try {
			return auditHousekeepService.findRevisionsByEntity(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "findByRevision", method = RequestMethod.POST)
	public @ResponseBody List<Object> findByRevision(
			@RequestParam(defaultValue = "1") Number revision, 
			@RequestParam(defaultValue = "Return snapshots of all audited entities changed in a given revision") String description){
		return auditHousekeepService.findByRevision(revision);
	}
	
	@RequestMapping(value = "testModifySubcontractAndDetail", method = RequestMethod.POST)
	public Object[] testModifySubcontractAndDetail(
			@RequestParam(defaultValue = "13389") String jobNo,
			@RequestParam(defaultValue = "1001") String subcontractNo,
			@RequestParam(defaultValue = "Change Subcontract and details's description") String description){
		Object[] results = null;
		try {
			results = subcontractService.testModifySubcontractAndDetail(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@RequestMapping(value = "testModifyPaymentCertAndDetail", method = RequestMethod.POST)
	public Object[] testModifyPaymentCertAndDetail(
			@RequestParam(defaultValue = "13389") String jobNo,
			@RequestParam(defaultValue = "1001") String subcontractNo,
			@RequestParam(defaultValue = "1") Integer paymentCertNo,
			@RequestParam(defaultValue = "Change Cert Amount and details's description") String description){
		Object[] results = null;
		try {
			results = paymentService.testModifyPaymentCertAndDetail(jobNo, subcontractNo, paymentCertNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

}
