/**
 * GammonQS-PH3
 * AccountLedger.java
 * @author koeyyeung
 * Created on May 9, 2013 9:05:02 AM
 */
package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.qs.application.BasePersistedObject;
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "JDE_ACCOUNT_LEDGER")
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "JDE_ACCOUNT_LEDGER_GEN",  sequenceName = "JDE_ACCOUNT_LEDGER_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 10, scale = 0))
public class JdeAccountLedger extends BasePersistedObject {

	private static final long serialVersionUID = -1415319038166674699L;

	private String jobNo;
	private String objectCode;
	private String description;
	private String subsidiaryCode;
	private String subledger;
	private String subledgerType;
	private String ledgerType;
	private Integer fiscalYear;
	
	private Double beginBalance=0.0;
	private Double cumToDate=0.0;
	private Double currentYearTotal=0.0;
	private Double period01=0.0;
	private Double period02=0.0;
	private Double period03=0.0;
	private Double period04=0.0;
	private Double period05=0.0;
	private Double period06=0.0;
	private Double period07=0.0;
	private Double period08=0.0;
	private Double period09=0.0;
	private Double period10=0.0;
	private Double period11=0.0;
	private Double period12=0.0;
	private Double period13=0.0;
	private Double period14=0.0;
	
	public JdeAccountLedger() {
	}
	
	@Override
	public String toString() {
		return "JdeAccountLedger [jobNo=" + jobNo + ", objectCode=" + objectCode + ", description=" + description
				+ ", subsidiaryCode=" + subsidiaryCode + ", subledger=" + subledger + ", subledgerType=" + subledgerType
				+ ", ledgerType=" + ledgerType + ", fiscalYear=" + fiscalYear + ", beginBalance=" + beginBalance
				+ ", cumToDate=" + cumToDate + ", currentYearTotal=" + currentYearTotal + ", period01=" + period01
				+ ", period02=" + period02 + ", period03=" + period03 + ", period04=" + period04 + ", period05="
				+ period05 + ", period06=" + period06 + ", period07=" + period07 + ", period08=" + period08
				+ ", period09=" + period09 + ", period10=" + period10 + ", period11=" + period11 + ", period12="
				+ period12 + ", period13=" + period13 + ", period14=" + period14 + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JDE_ACCOUNT_LEDGER_GEN")
	public Long getId(){return super.getId();}
	
	@Column(name = "JOB_NO", nullable = false, length = 20)
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	@Column(name = "OBJECT_CODE", nullable = false, length = 10)
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	
	@Column(name = "DESCRIPTION", length = 60)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "SUBSIDIARY_CODE", length = 10)
	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}
	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}
	
	@Column(name = "SUBLEDGER", length = 10)
	public String getSubledger() {
		return subledger;
	}
	public void setSubledger(String subledger) {
		this.subledger = subledger;
	}
	
	@Column(name = "SUBLEDGER_TYPE", length = 5)
	public String getSubledgerType() {
		return subledgerType;
	}
	public void setSubledgerType(String subledgerType) {
		this.subledgerType = subledgerType;
	}
	
	@Column(name = "LEDGER_TYPE", length = 5)
	public String getLedgerType() {
		return ledgerType;
	}
	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}
	
	@Column(name = "FISCAL_YEAR")
	public Integer getFiscalYear() {
		return fiscalYear;
	}
	public void setFiscalYear(Integer fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
	
	@Column(name = "BEGIN_BALANCE")
	public Double getBeginBalance() {
		return beginBalance;
	}
	public void setBeginBalance(Double beginBalance) {
		this.beginBalance = beginBalance;
	}
	
	@Column(name = "CUM_TO_DATE")
	public Double getCumToDate() {
		return cumToDate;
	}
	public void setCumToDate(Double cumToDate) {
		this.cumToDate = cumToDate;
	}
	
	@Column(name = "CURRENT_YEAR_TOTAL")
	public Double getCurrentYearTotal() {
		return currentYearTotal;
	}
	public void setCurrentYearTotal(Double currentYearTotal) {
		this.currentYearTotal = currentYearTotal;
	}
	
	@Column(name = "PERIOD01")
	public Double getPeriod01() {
		return period01;
	}
	public void setPeriod01(Double period01) {
		this.period01 = period01;
	}
	
	@Column(name = "PERIOD02")
	public Double getPeriod02() {
		return period02;
	}
	public void setPeriod02(Double period02) {
		this.period02 = period02;
	}
	
	@Column(name = "PERIOD03")
	public Double getPeriod03() {
		return period03;
	}
	public void setPeriod03(Double period03) {
		this.period03 = period03;
	}
	
	@Column(name = "PERIOD04")
	public Double getPeriod04() {
		return period04;
	}
	public void setPeriod04(Double period04) {
		this.period04 = period04;
	}
	
	@Column(name = "PERIOD05")
	public Double getPeriod05() {
		return period05;
	}
	public void setPeriod05(Double period05) {
		this.period05 = period05;
	}
	
	@Column(name = "PERIOD06")
	public Double getPeriod06() {
		return period06;
	}
	public void setPeriod06(Double period06) {
		this.period06 = period06;
	}
	
	@Column(name = "PERIOD07")
	public Double getPeriod07() {
		return period07;
	}
	public void setPeriod07(Double period07) {
		this.period07 = period07;
	}
	
	@Column(name = "PERIOD08")
	public Double getPeriod08() {
		return period08;
	}
	public void setPeriod08(Double period08) {
		this.period08 = period08;
	}
	
	@Column(name = "PERIOD09")
	public Double getPeriod09() {
		return period09;
	}
	public void setPeriod09(Double period09) {
		this.period09 = period09;
	}
	
	@Column(name = "PERIOD10")
	public Double getPeriod10() {
		return period10;
	}
	public void setPeriod10(Double period10) {
		this.period10 = period10;
	}
	
	@Column(name = "PERIOD11")
	public Double getPeriod11() {
		return period11;
	}
	public void setPeriod11(Double period11) {
		this.period11 = period11;
	}
	
	@Column(name = "PERIOD12")
	public Double getPeriod12() {
		return period12;
	}
	public void setPeriod12(Double period12) {
		this.period12 = period12;
	}
	
	@Column(name = "PERIOD13")
	public Double getPeriod13() {
		return period13;
	}
	public void setPeriod13(Double period13) {
		this.period13 = period13;
	}
	
	@Column(name = "PERIOD14")
	public Double getPeriod14() {
		return period14;
	}
	public void setPeriod14(Double period14) {
		this.period14 = period14;
	}	
}
