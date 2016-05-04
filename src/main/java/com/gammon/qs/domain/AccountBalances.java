package com.gammon.qs.domain;

import java.io.Serializable;
import java.util.List;

public class AccountBalances implements Serializable {

	private static final long serialVersionUID = -2610210612468263544L;

	private AccountMaster accountMaster;

	private String accountID;
	private Integer century;
	private Integer fiscalYear;
	private String subledger;
	private String subledgerType;

	private Double beginBalance;
	private Double netPosting001;
	private Double netPosting002;
	private Double netPosting003;
	private Double netPosting004;
	private Double netPosting005;
	private Double netPosting006;
	private Double netPosting007;
	private Double netPosting008;
	private Double netPosting009;
	private Double netPosting010;
	private Double netPosting011;
	private Double netPosting012;
	private Double netPosting013;
	private Double netPosting014;

	private List<AccountLedgerWrapper> accountLedgerList;

	public List<AccountLedgerWrapper> getAccountLedgerList() {
		return accountLedgerList;
	}

	public void setAccountLedgerList(List<AccountLedgerWrapper> accountLedgerList) {
		this.accountLedgerList = accountLedgerList;
	}

	public AccountMaster getAccountMaster() {
		return accountMaster;
	}

	public void setAccountMaster(AccountMaster accountMaster) {
		this.accountMaster = accountMaster;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public Integer getCentury() {
		return century;
	}

	public void setCentury(Integer century) {
		this.century = century;
	}

	public Integer getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(Integer fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public String getSubledger() {
		return subledger;
	}

	public void setSubledger(String subledger) {
		this.subledger = subledger;
	}

	public String getSubledgerType() {
		return subledgerType;
	}

	public void setSubledgerType(String subledgerType) {
		this.subledgerType = subledgerType;
	}

	public Double getBeginBalance() {
		return beginBalance;
	}

	public void setBeginBalance(Double beginBalance) {
		this.beginBalance = beginBalance;
	}

	public Double getNetPosting001() {
		return netPosting001;
	}

	public void setNetPosting001(Double netPosting001) {
		this.netPosting001 = netPosting001;
	}

	public Double getNetPosting002() {
		return netPosting002;
	}

	public void setNetPosting002(Double netPosting002) {
		this.netPosting002 = netPosting002;
	}

	public Double getNetPosting003() {
		return netPosting003;
	}

	public void setNetPosting003(Double netPosting003) {
		this.netPosting003 = netPosting003;
	}

	public Double getNetPosting004() {
		return netPosting004;
	}

	public void setNetPosting004(Double netPosting004) {
		this.netPosting004 = netPosting004;
	}

	public Double getNetPosting005() {
		return netPosting005;
	}

	public void setNetPosting005(Double netPosting005) {
		this.netPosting005 = netPosting005;
	}

	public Double getNetPosting006() {
		return netPosting006;
	}

	public void setNetPosting006(Double netPosting006) {
		this.netPosting006 = netPosting006;
	}

	public Double getNetPosting007() {
		return netPosting007;
	}

	public void setNetPosting007(Double netPosting007) {
		this.netPosting007 = netPosting007;
	}

	public Double getNetPosting008() {
		return netPosting008;
	}

	public void setNetPosting008(Double netPosting008) {
		this.netPosting008 = netPosting008;
	}

	public Double getNetPosting009() {
		return netPosting009;
	}

	public void setNetPosting009(Double netPosting009) {
		this.netPosting009 = netPosting009;
	}

	public Double getNetPosting010() {
		return netPosting010;
	}

	public void setNetPosting010(Double netPosting010) {
		this.netPosting010 = netPosting010;
	}

	public Double getNetPosting011() {
		return netPosting011;
	}

	public void setNetPosting011(Double netPosting011) {
		this.netPosting011 = netPosting011;
	}

	public Double getNetPosting012() {
		return netPosting012;
	}

	public void setNetPosting012(Double netPosting012) {
		this.netPosting012 = netPosting012;
	}

	public Double getNetPosting013() {
		return netPosting013;
	}

	public void setNetPosting013(Double netPosting013) {
		this.netPosting013 = netPosting013;
	}

	public Double getNetPosting014() {
		return netPosting014;
	}

	public void setNetPosting014(Double netPosting014) {
		this.netPosting014 = netPosting014;
	}

}
