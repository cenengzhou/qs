/**
 * GammonQS-PH3
 * SupplierMasterWrapper.java
 * @author tikywong
 * created on Mar 21, 2013 9:17:29 AM
 * 
 */
package com.gammon.qs.wrapper.supplierMaster;

import java.io.Serializable;

public class SupplierMasterWrapper implements Serializable {

	private static final long serialVersionUID = -3967994155639478475L;

	private Integer addressNumber;
	private String holdPaymentCode;
	private String paymentTermsAP;
	
	public Integer getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(Integer addressNumber) {
		this.addressNumber = addressNumber;
	}
	public String getHoldPaymentCode() {
		return holdPaymentCode;
	}
	public void setHoldPaymentCode(String holdPaymentCode) {
		this.holdPaymentCode = holdPaymentCode;
	}
	public String getPaymentTermsAP() {
		return paymentTermsAP;
	}
	public void setPaymentTermsAP(String paymentTermsAP) {
		this.paymentTermsAP = paymentTermsAP;
	}

}
