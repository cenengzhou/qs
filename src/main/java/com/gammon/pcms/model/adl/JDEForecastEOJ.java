package com.gammon.pcms.model.adl;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "FACT_JDE_FORECAST_EOJ")
public class JDEForecastEOJ implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3506756071158900173L;
	
	private String ID;
	private String businessUnit;
	private String accLedgerType;
	private String latestForecastLedger;
	private Double amount;
	private String desc;
	
	@Id
	@Column(name = "ID")
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name = "BUSINESS_UNIT_CODE_TRIMMED")
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
	
	@Column(name = "LATEST_FORECAST_LEDGER")
	public String getAccLedgerType() {
		return accLedgerType;
	}
	public void setAccLedgerType(String accLedgerType) {
		this.accLedgerType = accLedgerType;
	}
	
	
	@Column(name = "ACCOUNT_TYPE_LEDGER")
	public String getLatestForecastLedger() {
		return latestForecastLedger;
	}
	public void setLatestForecastLedger(String latestForecastLedger) {
		this.latestForecastLedger = latestForecastLedger;
	}
	
	
	@Column(name = "QSRF_DESC")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Column(name = "AMOUNT")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "JDEForecast  [businessUnit=" + businessUnit + ", latestForecastLedger=" + latestForecastLedger + ", accLedgerType=" + accLedgerType + ", desc=" + desc  + ", amount=" + amount + "]";
	}
	
	

}
