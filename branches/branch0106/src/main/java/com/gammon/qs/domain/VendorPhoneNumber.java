package com.gammon.qs.domain;

import java.io.Serializable;

public class VendorPhoneNumber implements Serializable{
	
    private static final long serialVersionUID = -6735854208712051143L;
	private Integer addressNumber = null;
    private Integer lineNumberID = null;
    private Integer sequenceNumber70 = null;
    private Integer contactPersonalID = null;
    private String phoneNumberType = null;
    private String phoneAreaCode1 = null;
    private String phoneNumber = null;
    
	public Integer getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(Integer addressNumber) {
		this.addressNumber = addressNumber;
	}
	public Integer getLineNumberID() {
		return lineNumberID;
	}
	public void setLineNumberID(Integer lineNumberID) {
		this.lineNumberID = lineNumberID;
	}
	public Integer getSequenceNumber70() {
		return sequenceNumber70;
	}
	public void setSequenceNumber70(Integer sequenceNumber70) {
		this.sequenceNumber70 = sequenceNumber70;
	}
	public Integer getContactPersonalID() {
		return contactPersonalID;
	}
	public void setContactPersonalID(Integer contactPersonalID) {
		this.contactPersonalID = contactPersonalID;
	}
	public String getPhoneNumberType() {
		return phoneNumberType;
	}
	public void setPhoneNumberType(String phoneNumberType) {
		this.phoneNumberType = phoneNumberType;
	}
	public String getPhoneAreaCode1() {
		return phoneAreaCode1;
	}
	public void setPhoneAreaCode1(String phoneAreaCode1) {
		this.phoneAreaCode1 = phoneAreaCode1;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
    
    

}
