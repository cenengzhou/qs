package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.Date;

public class VendorAddress implements Serializable{
	private static final long serialVersionUID = -5150672606275173607L;
	/**
	 * Java Doc comments for Value Object here
	 */
		
	private Integer addressNumber = null;
	private Date dateBeginningEffective = null;
	private String effectiveDateExistence10 = null;
	private String addressLine1 = null;
	private String addressLine2 = null;
	private String addressLine3 = null;
	private String addressLine4 = null;
	private String zipCodePostal = null;
	private String city = null;
	private String countryAddress = null;
	private String state = null;
	private String country = null;


	public Integer getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(Integer addressNumber) {
		this.addressNumber = addressNumber;
	}
	public Date getDateBeginningEffective() {
		return dateBeginningEffective;
	}
	public void setDateBeginningEffective(Date dateBeginningEffective) {
		this.dateBeginningEffective = dateBeginningEffective;
	}
	public String getEffectiveDateExistence10() {
		return effectiveDateExistence10;
	}
	public void setEffectiveDateExistence10(String effectiveDateExistence10) {
		this.effectiveDateExistence10 = effectiveDateExistence10;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	public String getAddressLine4() {
		return addressLine4;
	}
	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}
	public String getZipCodePostal() {
		return zipCodePostal;
	}
	public void setZipCodePostal(String zipCodePostal) {
		this.zipCodePostal = zipCodePostal;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountyAddress() {
		return countryAddress;
	}
	public void setCountyAddress(String countyAddress) {
		this.countryAddress = countyAddress;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
