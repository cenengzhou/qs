package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.Date;

public class PORecord implements Serializable {
	
	private static final long serialVersionUID = 8203287370143960069L;

	// DOCO
	private Integer documentOrderInvoiceE;
	
	// DCTO
	private String orderType;
	
	// LNID
	private Double lineNumber;
	
	// MCU
	private String costCenter;
	
	// AN8
	private Integer addressNumber;
	
	// SHAN
	private Integer addressNumberShipTo;
	
	// DRQJ
	private Date dateRequestedJulian;
	
	// TRDJ
	private Date dateTransactionJulian;
	
	// DGL
	private Date dtForGLAndVouch1;
	
	//LITM
	private String identifier2ndItem;
	
	// DSC1
	private String descriptionLine1;
	
	// DSC2
	private String descriptionLine2;
	
	// UOM
	private String unitOfMeasureAsInput;
	
	// UOPN
	private Double unitsOpenQuantity;
	
	// PRRC
	private Double purchasingUnitPrice;
	
	// AEXP
	private Double amountExtendedPrice;
	
	// AOPN
	private Double amountOpen1;
	
	// ANI
	private String acctNoInputMode;
	
	// OBJ
	private String objectAccount;
	
	// SUB
	private String subsidiary;
	
	// SBLT
	private String subledgerType;
	
	// SBL
	private String subledger;
	
	// CRCD
	private String currencyCodeFrom;

	public void setDocumentOrderInvoiceE(Integer documentOrderInvoiceE) {
        this.documentOrderInvoiceE = documentOrderInvoiceE;
    }

    public Integer getDocumentOrderInvoiceE() {
        return documentOrderInvoiceE;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setLineNumber(Double lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Double getLineNumber() {
        return lineNumber;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setAddressNumber(Integer addressNumber) {
        this.addressNumber = addressNumber;
    }

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumberShipTo(Integer addressNumberShipTo) {
        this.addressNumberShipTo = addressNumberShipTo;
    }

    public Integer getAddressNumberShipTo() {
        return addressNumberShipTo;
    }

    public void setDateRequestedJulian(Date dateRequestedJulian) {
        this.dateRequestedJulian = dateRequestedJulian;
    }

    public Date getDateRequestedJulian() {
        return dateRequestedJulian;
    }

    public void setDateTransactionJulian(Date dateTransactionJulian) {
        this.dateTransactionJulian = dateTransactionJulian;
    }

    public Date getDateTransactionJulian() {
        return dateTransactionJulian;
    }

    public void setDtForGLAndVouch1(Date dtForGLAndVouch1) {
        this.dtForGLAndVouch1 = dtForGLAndVouch1;
    }

    public Date getDtForGLAndVouch1() {
        return dtForGLAndVouch1;
    }

    public void setIdentifier2ndItem(String identifier2ndItem) {
        this.identifier2ndItem = identifier2ndItem;
    }

    public String getIdentifier2ndItem() {
        return identifier2ndItem;
    }

    public void setDescriptionLine1(String descriptionLine1) {
        this.descriptionLine1 = descriptionLine1;
    }

    public String getDescriptionLine1() {
        return descriptionLine1;
    }

    public void setDescriptionLine2(String descriptionLine2) {
        this.descriptionLine2 = descriptionLine2;
    }

    public String getDescriptionLine2() {
        return descriptionLine2;
    }

    public void setUnitOfMeasureAsInput(String unitOfMeasureAsInput) {
        this.unitOfMeasureAsInput = unitOfMeasureAsInput;
    }

    public String getUnitOfMeasureAsInput() {
        return unitOfMeasureAsInput;
    }

    public void setUnitsOpenQuantity(Double unitsOpenQuantity) {
        this.unitsOpenQuantity = unitsOpenQuantity;
    }

    public Double getUnitsOpenQuantity() {
        return unitsOpenQuantity;
    }

    public void setPurchasingUnitPrice(Double purchasingUnitPrice) {
        this.purchasingUnitPrice = purchasingUnitPrice;
    }

    public Double getPurchasingUnitPrice() {
        return purchasingUnitPrice;
    }

    public void setAmountExtendedPrice(Double amountExtendedPrice) {
        this.amountExtendedPrice = amountExtendedPrice;
    }

    public Double getAmountExtendedPrice() {
        return amountExtendedPrice;
    }

    public void setAmountOpen1(Double amountOpen1) {
        this.amountOpen1 = amountOpen1;
    }

    public Double getAmountOpen1() {
        return amountOpen1;
    }

    public void setAcctNoInputMode(String acctNoInputMode) {
        this.acctNoInputMode = acctNoInputMode;
    }

    public String getAcctNoInputMode() {
        return acctNoInputMode;
    }

    public void setObjectAccount(String objectAccount) {
        this.objectAccount = objectAccount;
    }

    public String getObjectAccount() {
        return objectAccount;
    }

    public void setSubsidiary(String subsidiary) {
        this.subsidiary = subsidiary;
    }

    public String getSubsidiary() {
        return subsidiary;
    }

    public void setSubledgerType(String subledgerType) {
        this.subledgerType = subledgerType;
    }

    public String getSubledgerType() {
        return subledgerType;
    }

    public void setSubledger(String subledger) {
        this.subledger = subledger;
    }

    public String getSubledger() {
        return subledger;
    }

    public void setCurrencyCodeFrom(String currencyCodeFrom) {
        this.currencyCodeFrom = currencyCodeFrom;
    }

    public String getCurrencyCodeFrom() {
        return currencyCodeFrom;
    }

}
