package com.gammon.pcms.model.adl;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "FACT_JDE_FORECAST")
public class JDEForecast implements java.io.Serializable {


	
	private static final long serialVersionUID = 1436109146198384278L;

	public static final String AA = "AA";
	public static final String CTD = "CTD";
	
	private String ID;
	private String businessUnit;
	private String accLedgerType;
	private String latestForecastLedger;
	private Double amount;
	private String desc;
	private String figureType;
	private Integer year;
	private Integer month;
	
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
	
	
	@Column(name = "ACCOUNT_TYPE_LEDGER")
	public String getAccLedgerType() {
		return accLedgerType;
	}
	public void setAccLedgerType(String accLedgerType) {
		this.accLedgerType = accLedgerType;
	}
	
	
	@Column(name = "LATEST_FORECAST_LEDGER")
	public String getLatestForecastLedger() {
		return latestForecastLedger;
	}
	public void setLatestForecastLedger(String latestForecastLedger) {
		this.latestForecastLedger = latestForecastLedger;
	}
	
	@Column(name = "AMOUNT")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name = "QSRF_DESC")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Column(name = "FIGURE_TYPE")
	public String getFigureType() {
		return figureType;
	}
	public void setFigureType(String figureType) {
		this.figureType = figureType;
	}
	
	@Column(name = "YEAR")
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	@Column(name = "MONTH")
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	

	@Override
	public String toString() {
		return "AccountLedger [businessUnit=" + businessUnit + ", year=" + year + ", month=" + month + ", accLedgerType=" + accLedgerType + ", latestForecastLedger=" + latestForecastLedger + ", desc=" + desc + ", figureType=" + figureType + ", amount=" + amount + "]";
	}
	
	

}
