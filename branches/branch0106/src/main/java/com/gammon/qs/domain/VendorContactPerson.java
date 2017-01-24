package com.gammon.qs.domain;

import java.io.Serializable;

public class VendorContactPerson implements Serializable{
		
    private static final long serialVersionUID = -2714539545497168597L;
	private Integer addressNumber = null;
    private Integer lineNumberID = null;
    private Integer sequenceNumber52Display = null;
    private String nameMailing = null;
    private String contactTitle = null;
    private String nameAlpha = null;
    private String descripCompressed = null;
    private String nameGiven = null;
    private String nameMiddle = null;
    private String nameSurname = null;
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
	public Integer getSequenceNumber52Display() {
		return sequenceNumber52Display;
	}
	public void setSequenceNumber52Display(Integer sequenceNumber52Display) {
		this.sequenceNumber52Display = sequenceNumber52Display;
	}
	public String getNameMailing() {
		return nameMailing;
	}
	public void setNameMailing(String nameMailing) {
		this.nameMailing = nameMailing;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}
	public String getNameAlpha() {
		return nameAlpha;
	}
	public void setNameAlpha(String nameAlpha) {
		this.nameAlpha = nameAlpha;
	}
	public String getDescripCompressed() {
		return descripCompressed;
	}
	public void setDescripCompressed(String descripCompressed) {
		this.descripCompressed = descripCompressed;
	}
	public String getNameGiven() {
		return nameGiven;
	}
	public void setNameGiven(String nameGiven) {
		this.nameGiven = nameGiven;
	}
	public String getNameMiddle() {
		return nameMiddle;
	}
	public void setNameMiddle(String nameMiddle) {
		this.nameMiddle = nameMiddle;
	}
	public String getNameSurname() {
		return nameSurname;
	}
	public void setNameSurname(String nameSurname) {
		this.nameSurname = nameSurname;
	}

}
