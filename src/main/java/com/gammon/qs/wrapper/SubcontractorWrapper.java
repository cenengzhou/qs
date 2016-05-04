package com.gammon.qs.wrapper;

import java.io.Serializable;

public class SubcontractorWrapper implements Serializable{

	/**
	 * koeyyeung
	 * Jul 23, 2013 1:49:11 PM
	 */
	private static final long serialVersionUID = 9117229589634756382L;

	public static final String VENDOR_TYPE_1 = "Supplier";
	public static final String VENDOR_TYPE_2 = "Subcontractor";
	public static final String VENDOR_TYPE_3 = "Both (Supplier & Subcontractor)";
	
	public static final String VENDOR_STATUS_1 = "Performance being observed";
	public static final String VENDOR_STATUS_2 = "Suspended";
	public static final String VENDOR_STATUS_3 = "Blacklisted";
	public static final String VENDOR_STATUS_4 = "Obsolete";
	public static final String VENDOR_STATUS_5 = "On HSE League Table";
	public static final String VENDOR_STATUS_6 = "Observed & On HSE League";
	public static final String VENDOR_STATUS_7 = "Suspended & On HSE League";

	private String subcontractorNo;
	private String subcontractorName;
	private String businessRegistrationNo;
	private String vendorType;
	private String vendorStatus;
	private String subcontractorApproval;
	private String holdPayment;
	private String scFinancialAlert;
	
	public static String convertVendorType(String vendorType){
		if(vendorType!=null || !"".equals(vendorType)){
			if(vendorType.equals("1")){
				return SubcontractorWrapper.VENDOR_TYPE_1;
			}else if(vendorType.equals("2")){
				return SubcontractorWrapper.VENDOR_TYPE_2;
			}else if(vendorType.equals("3")){
				return SubcontractorWrapper.VENDOR_TYPE_3;
			}
		}
		return "";
	}
	
	public static String convertVendorStatus(String vendorStatus){
		if(vendorStatus!=null || !"".equals(vendorStatus)){
			if(vendorStatus.equals("1")){
				return SubcontractorWrapper.VENDOR_STATUS_1;
			}else if(vendorStatus.equals("2")){
				return SubcontractorWrapper.VENDOR_STATUS_2;
			}else if(vendorStatus.equals("3")){
				return SubcontractorWrapper.VENDOR_STATUS_3;
			}else if(vendorStatus.equals("4")){
				return SubcontractorWrapper.VENDOR_STATUS_4;
			}else if(vendorStatus.equals("5")){
				return SubcontractorWrapper.VENDOR_STATUS_5;
			}else if(vendorStatus.equals("6")){
				return SubcontractorWrapper.VENDOR_STATUS_6;
			}else if(vendorStatus.equals("7")){
				return SubcontractorWrapper.VENDOR_STATUS_7;
			}
		}
		return "";
	}
	
	public static String convertSCFinancialAlertStatus(String status) {
		if (status != null && status.length() > 0)
			return "Yes";
		return "No";
	}
	
	//getter, setter
	public String getSubcontractorNo() {
		return subcontractorNo;
	}
	public void setSubcontractorNo(String subcontractorNo) {
		this.subcontractorNo = subcontractorNo;
	}
	public String getSubcontractorName() {
		return subcontractorName;
	}
	public void setSubcontractorName(String subcontractorName) {
		this.subcontractorName = subcontractorName;
	}
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	public String getVendorStatus() {
		return vendorStatus;
	}
	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}
	public String getSubcontractorApproval() {
		return subcontractorApproval;
	}
	public void setSubcontractorApproval(String subcontractorApproval) {
		this.subcontractorApproval = subcontractorApproval;
	}
	public String getHoldPayment() {
		return holdPayment;
	}
	public void setHoldPayment(String holdPayment) {
		this.holdPayment = holdPayment;
	}
	public void setBusinessRegistrationNo(String businessRegistrationNo) {
		this.businessRegistrationNo = businessRegistrationNo;
	}
	public String getBusinessRegistrationNo() {
		return businessRegistrationNo;
	}

	public void setScFinancialAlert(String scFinancialAlert) {
		this.scFinancialAlert = scFinancialAlert;
	}

	public String getScFinancialAlert() {
		return scFinancialAlert;
	}
	
}
