package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.List;

import com.gammon.qs.wrapper.WorkScopeWrapper;

public class MasterListVendor implements Serializable{
	
	private static final long serialVersionUID = 9053721552584623181L;
	private String vendorNo;
	private String vendorName;
	private String vendorRegistrationNo;
	private String approvalStatus;
	private List<VendorAddress> vendorAddress;
	private List<VendorContactPerson> vendorContactPerson;
	private List<VendorPhoneNumber> vendorPhoneNumber;
	private List<WorkScopeWrapper> workScope;
	private String vendorType;//reportCodeAddBook003
	private String addressType; //addressType1
	private String costCenter;//costCenter
	private String vendorStatus;
	private String scFinancialAlert;
	
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public List<VendorAddress> getVendorAddress() {
		return vendorAddress;
	}
	public void setVendorAddress(List<VendorAddress> vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	public List<VendorContactPerson> getVendorContactPerson() {
		return vendorContactPerson;
	}
	public void setVendorContactPerson(List<VendorContactPerson> vendorContactPerson) {
		this.vendorContactPerson = vendorContactPerson;
	}
	public List<VendorPhoneNumber> getVendorPhoneNumber() {
		return vendorPhoneNumber;
	}
	public void setVendorPhoneNumber(List<VendorPhoneNumber> vendorPhoneNumber) {
		this.vendorPhoneNumber = vendorPhoneNumber;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorRegistrationNo() {
		return vendorRegistrationNo;
	}
	public void setVendorRegistrationNo(String vendorRegistrationNo) {
		this.vendorRegistrationNo = vendorRegistrationNo;
	}
	public void setWorkScope(List<WorkScopeWrapper> workScope) {
		this.workScope = workScope;
	}
	public List<WorkScopeWrapper> getWorkScope() {
		return workScope;
	}
	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}
	public String getVendorStatus() {
		return vendorStatus;
	}
	public void setScFinancialAlert(String scFinancialAlert) {
		this.scFinancialAlert = scFinancialAlert;
	}
	public String getScFinancialAlert() {
		return scFinancialAlert;
	}
	
	

}
