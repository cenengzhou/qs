package com.gammon.qs.wrapper.paymentCertView;

import java.io.Serializable;

public class PaymentCertViewWrapper implements Serializable{
	
	private static final long serialVersionUID = -2687645440369068091L;
	private String jobNumber;
	private String jobDescription;
	private Integer paymentCertNo;
	private Integer mainCertNo;
	private String subContractNo;
	private String subContractDescription;
	
	private String paymentType;
	private String subcontractorNo;
	private String subcontractorDescription;
	private Double subcontractSum;
	private String approvalStatus;
	
	private Double addendum;
	private Double revisedValue;
	private Double measuredWorksMovement;
	private Double measuredWorksTotal;
	private Double dayWorkSheetMovement;
	
	private Double dayWorkSheetTotal;
	private Double variationMovement;
	private Double variationTotal;
	private Double otherMovement;
	private Double otherTotal;
	
	private Double subMovement1;
	private Double subTotal1;
	private Double lessRetentionMovement1;
	private Double lessRetentionTotal1;
	private Double subMovement2;
	
	private Double subTotal2;
	private Double materialOnSiteMovement;
	private Double materialOnSiteTotal;
	private Double lessRetentionMovement2;
	private Double lessRetentionTotal2;
	
	private Double subMovement3;
	private Double subTotal3;
	private Double adjustmentMovement;
	private Double adjustmentTotal;
	private Double gstPayableMovement;
	
	private Double gstPayableTotal;
	private Double subMovement4;
	private Double subTotal4;
	private Double lessContraChargesMovement;
	private Double lessContraChargesTotal;
	
	private Double lessGSTReceivableMovement;
	private Double lessGSTReceivableTotal;
	private Double subMovement5;
	private Double subTotal5;
	private Double lessPreviousCertificationsMovement; //prev Cert Total
	
	//For printing main Cert
	private String asAtDate;
	private String dueDate;
	
	private String subcontractorNature;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String currency;
	private String phone;
	private String companyName;
	
	private Double amountDueMovement;

	private String companyBaseCurrency;
	private Double exchangeRate;
	
	public PaymentCertViewWrapper(){
		paymentCertNo = new Integer (0);
		mainCertNo = new Integer (0);
		subcontractSum = new Double (0);
		addendum = new Double (0);
		revisedValue = new Double (0);
		measuredWorksMovement = new Double (0);
		measuredWorksTotal = new Double (0);
		dayWorkSheetMovement = new Double (0);
		dayWorkSheetTotal = new Double (0);
		variationMovement = new Double (0);
		variationTotal = new Double (0);
		otherMovement = new Double (0);
		otherTotal = new Double (0);
		subMovement1 = new Double (0);
		subTotal1 = new Double (0);
		lessRetentionMovement1 = new Double (0);
		lessRetentionTotal1 = new Double (0);
		subMovement2 = new Double (0);
		subTotal2 = new Double (0);
		materialOnSiteMovement = new Double (0);
		materialOnSiteTotal = new Double (0);
		lessRetentionMovement2 = new Double (0);
		lessRetentionTotal2 = new Double (0);
		subMovement3 = new Double (0);
		subTotal3 = new Double (0);
		adjustmentMovement = new Double (0);
		adjustmentTotal = new Double (0);
		gstPayableMovement = new Double (0);
		gstPayableTotal = new Double (0);
		subMovement4 = new Double (0);
		subTotal4 = new Double (0);
		lessContraChargesMovement = new Double (0);
		lessContraChargesTotal = new Double (0);
		lessGSTReceivableMovement = new Double (0);
		lessGSTReceivableTotal = new Double (0);
		subMovement5 = new Double (0);
		subTotal5 = new Double (0);
		lessPreviousCertificationsMovement = new Double (0);
		amountDueMovement = new Double (0);
		
		// last modified: brian
		companyBaseCurrency = "";
		exchangeRate = new Double(1);
	}
	
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public Integer getPaymentCertNo() {
		return paymentCertNo;
	}

	public void setPaymentCertNo(Integer paymentCertNo) {
		this.paymentCertNo = paymentCertNo;
	}

	public String getSubContractNo() {
		return subContractNo;
	}

	public void setSubContractNo(String subContractNo) {
		this.subContractNo = subContractNo;
	}

	public String getSubContractDescription() {
		return subContractDescription;
	}

	public void setSubContractDescription(String subContractDescription) {
		this.subContractDescription = subContractDescription;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSubcontractorNo() {
		return subcontractorNo;
	}

	public void setSubcontractorNo(String subcontractorNo) {
		this.subcontractorNo = subcontractorNo;
	}

	public String getSubcontractorDescription() {
		return subcontractorDescription;
	}

	public void setSubcontractorDescription(String subcontractorDescription) {
		this.subcontractorDescription = subcontractorDescription;
	}

	public Double getSubcontractSum() {
		return subcontractSum;
	}

	public void setSubcontractSum(Double subcontractSum) {
		this.subcontractSum = subcontractSum;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Double getAddendum() {
		return addendum;
	}

	public void setAddendum(Double addendum) {
		this.addendum = addendum;
	}

	public Double getRevisedValue() {
		return revisedValue;
	}

	public void setRevisedValue(Double revisedValue) {
		this.revisedValue = revisedValue;
	}

	public Double getMeasuredWorksMovement() {
		return measuredWorksMovement;
	}

	public void setMeasuredWorksMovement(Double measuredWorksMovement) {
		this.measuredWorksMovement = measuredWorksMovement;
	}

	public Double getMeasuredWorksTotal() {
		return measuredWorksTotal;
	}

	public void setMeasuredWorksTotal(Double measuredWorksTotal) {
		this.measuredWorksTotal = measuredWorksTotal;
	}

	public Double getDayWorkSheetMovement() {
		return dayWorkSheetMovement;
	}

	public void setDayWorkSheetMovement(Double dayWorkSheetMovement) {
		this.dayWorkSheetMovement = dayWorkSheetMovement;
	}

	public Double getDayWorkSheetTotal() {
		return dayWorkSheetTotal;
	}

	public void setDayWorkSheetTotal(Double dayWorkSheetTotal) {
		this.dayWorkSheetTotal = dayWorkSheetTotal;
	}

	public Double getVariationMovement() {
		return variationMovement;
	}

	public void setVariationMovement(Double variationMovement) {
		this.variationMovement = variationMovement;
	}

	public Double getVariationTotal() {
		return variationTotal;
	}

	public void setVariationTotal(Double variationTotal) {
		this.variationTotal = variationTotal;
	}

	public Double getOtherMovement() {
		return otherMovement;
	}

	public void setOtherMovement(Double otherMovement) {
		this.otherMovement = otherMovement;
	}

	public Double getOtherTotal() {
		return otherTotal;
	}

	public void setOtherTotal(Double otherTotal) {
		this.otherTotal = otherTotal;
	}

	public Double getSubMovement1() {
		return subMovement1;
	}

	public void setSubMovement1(Double subMovement1) {
		this.subMovement1 = subMovement1;
	}

	public Double getSubTotal1() {
		return subTotal1;
	}

	public void setSubTotal1(Double subTotal1) {
		this.subTotal1 = subTotal1;
	}

	public Double getLessRetentionMovement1() {
		return lessRetentionMovement1;
	}

	public void setLessRetentionMovement1(Double lessRetentionMovement1) {
		this.lessRetentionMovement1 = lessRetentionMovement1;
	}

	public Double getLessRetentionTotal1() {
		return lessRetentionTotal1;
	}

	public void setLessRetentionTotal1(Double lessRetentionTotal1) {
		this.lessRetentionTotal1 = lessRetentionTotal1;
	}

	public Double getSubMovement2() {
		return subMovement2;
	}

	public void setSubMovement2(Double subMovement2) {
		this.subMovement2 = subMovement2;
	}

	public Double getSubTotal2() {
		return subTotal2;
	}

	public void setSubTotal2(Double subTotal2) {
		this.subTotal2 = subTotal2;
	}

	public Double getMaterialOnSiteMovement() {
		return materialOnSiteMovement;
	}

	public void setMaterialOnSiteMovement(Double materialOnSiteMovement) {
		this.materialOnSiteMovement = materialOnSiteMovement;
	}

	public Double getMaterialOnSiteTotal() {
		return materialOnSiteTotal;
	}

	public void setMaterialOnSiteTotal(Double materialOnSiteTotal) {
		this.materialOnSiteTotal = materialOnSiteTotal;
	}

	public Double getLessRetentionMovement2() {
		return lessRetentionMovement2;
	}

	public void setLessRetentionMovement2(Double lessRetentionMovement2) {
		this.lessRetentionMovement2 = lessRetentionMovement2;
	}

	public Double getLessRetentionTotal2() {
		return lessRetentionTotal2;
	}

	public void setLessRetentionTotal2(Double lessRetentionTotal2) {
		this.lessRetentionTotal2 = lessRetentionTotal2;
	}

	public Double getSubMovement3() {
		return subMovement3;
	}

	public void setSubMovement3(Double subMovement3) {
		this.subMovement3 = subMovement3;
	}

	public Double getSubTotal3() {
		return subTotal3;
	}

	public void setSubTotal3(Double subTotal3) {
		this.subTotal3 = subTotal3;
	}

	public Double getAdjustmentMovement() {
		return adjustmentMovement;
	}

	public void setAdjustmentMovement(Double adjustmentMovement) {
		this.adjustmentMovement = adjustmentMovement;
	}

	public Double getAdjustmentTotal() {
		return adjustmentTotal;
	}

	public void setAdjustmentTotal(Double adjustmentTotal) {
		this.adjustmentTotal = adjustmentTotal;
	}

	public Double getGstPayableMovement() {
		return gstPayableMovement;
	}

	public void setGstPayableMovement(Double gstPayableMovement) {
		this.gstPayableMovement = gstPayableMovement;
	}

	public Double getGstPayableTotal() {
		return gstPayableTotal;
	}

	public void setGstPayableTotal(Double gstPayableTotal) {
		this.gstPayableTotal = gstPayableTotal;
	}

	public Double getSubMovement4() {
		return subMovement4;
	}

	public void setSubMovement4(Double subMovement4) {
		this.subMovement4 = subMovement4;
	}

	public Double getSubTotal4() {
		return subTotal4;
	}

	public void setSubTotal4(Double subTotal4) {
		this.subTotal4 = subTotal4;
	}

	public Double getLessContraChargesMovement() {
		return lessContraChargesMovement;
	}

	public void setLessContraChargesMovement(Double lessContraChargesMovement) {
		this.lessContraChargesMovement = lessContraChargesMovement;
	}

	public Double getLessContraChargesTotal() {
		return lessContraChargesTotal;
	}

	public void setLessContraChargesTotal(Double lessContraChargesTotal) {
		this.lessContraChargesTotal = lessContraChargesTotal;
	}

	public Double getLessGSTReceivableMovement() {
		return lessGSTReceivableMovement;
	}

	public void setLessGSTReceivableMovement(Double lessGSTReceivableMovement) {
		this.lessGSTReceivableMovement = lessGSTReceivableMovement;
	}

	public Double getLessGSTReceivableTotal() {
		return lessGSTReceivableTotal;
	}

	public void setLessGSTReceivableTotal(Double lessGSTReceivableTotal) {
		this.lessGSTReceivableTotal = lessGSTReceivableTotal;
	}

	public Double getSubMovement5() {
		return subMovement5;
	}

	public void setSubMovement5(Double subMovement5) {
		this.subMovement5 = subMovement5;
	}

	public Double getSubTotal5() {
		return subTotal5;
	}

	public void setSubTotal5(Double subTotal5) {
		this.subTotal5 = subTotal5;
	}

	public Double getLessPreviousCertificationsMovement() {
		return lessPreviousCertificationsMovement;
	}

	public void setLessPreviousCertificationsMovement(
			Double lessPreviousCertificationsMovement) {
		this.lessPreviousCertificationsMovement = lessPreviousCertificationsMovement;
	}

	public Double getAmountDueMovement() {
		return amountDueMovement;
	}

	public void setAmountDueMovement(Double amountDueMovement) {
		this.amountDueMovement = amountDueMovement;
	}

	public void setSubcontractorNature(String subcontractorNature) {
		this.subcontractorNature = subcontractorNature;
	}

	public String getSubcontractorNature() {
		return subcontractorNature;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAsAtDate(String asAtDate) {
		this.asAtDate = asAtDate;
	}

	public String getAsAtDate() {
		return asAtDate;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyBaseCurrency() {
		return companyBaseCurrency;
	}

	public void setCompanyBaseCurrency(String companyBaseCurrency) {
		this.companyBaseCurrency = companyBaseCurrency;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getMainCertNo() {
		return mainCertNo;
	}

	public void setMainCertNo(Integer mainCertNo) {
		this.mainCertNo = mainCertNo;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
}
