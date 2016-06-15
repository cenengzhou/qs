package com.gammon.qs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "V_CONTRACT_RECEIVABLE_RPT")
public class ContractReceivableWrapper implements Serializable{

	/**
	 * koeyyeung
	 * Nov 2, 201510:39:14 AM
	 */
	private static final long serialVersionUID = 7108478159092229090L;

	private String division;
	private String company;
	private String jobNo;
	private String jobDescription;
	private String clientNo;	
	private String clientName;
	private String certNo;
	private String clientCertNo;
	private String currency;
	private String receivableAmount;
	private String certStatus;
	private String certAsAtDate;
	private String ipaDate;
	private String certIssueDate;
	private String certDueDate;
	private String actualReceiptDate;
	private String totalReceiptAmount;
	private String benchmark1;//#of days: Cert Date against As At Date (Average of last 6 certs)
	private String benchmark2;//#of days: Cert Due Date against Cert Date (Average of last 6 certs)
	
	public ContractReceivableWrapper() {
	}
	
	@Override
	public String toString() {
		return "ContractReceivableWrapper [division=" + division + ", company=" + company + ", jobNo=" + jobNo
				+ ", jobDescription=" + jobDescription + ", clientNo=" + clientNo + ", clientName=" + clientName
				+ ", certNo=" + certNo + ", clientCertNo=" + clientCertNo + ", currency=" + currency
				+ ", receivableAmount=" + receivableAmount + ", certStatus=" + certStatus + ", certAsAtDate="
				+ certAsAtDate + ", ipaDate=" + ipaDate + ", certIssueDate=" + certIssueDate + ", certDueDate="
				+ certDueDate + ", actualReceiptDate=" + actualReceiptDate + ", totalReceiptAmount="
				+ totalReceiptAmount + ", benchmark1=" + benchmark1 + ", benchmark2=" + benchmark2 + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "JOBNO")
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	
	@Column(name = "DIVISION", length = 10)
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	
	@Column(name = "COMPANY", length = 12)
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	@Column(name = "JOBDESCRIPTION", length = 255)
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	
	@Column(name = "CLIENTNO", length = 12)
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	@Transient
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	@Column(name = "CERTNO")
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	
	@Column(name = "CLIENTCERTNO")
	public String getClientCertNo() {
		return clientCertNo;
	}
	public void setClientCertNo(String clientCertNo) {
		this.clientCertNo = clientCertNo;
	}
	
	@Column(name = "CURRENCY", length = 10)
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Column(name = "RECEIVABLEAMOUNT", length = 19)
	public String getReceivableAmount() {
		return receivableAmount;
	}
	public void setReceivableAmount(String receivableAmount) {
		this.receivableAmount = receivableAmount;
	}
	
	@Column(name = "CERTSTATUS", length = 10)
	public String getCertStatus() {
		return certStatus;
	}
	public void setCertStatus(String certStatus) {
		this.certStatus = certStatus;
	}
	
	@Column(name = "CERTASATDATE", length = 10)
	public String getCertAsAtDate() {
		return certAsAtDate;
	}
	public void setCertAsAtDate(String certAsAtDate) {
		this.certAsAtDate = certAsAtDate;
	}
	
	@Column(name = "IPADATE", length = 10)
	public String getIpaDate() {
		return ipaDate;
	}
	public void setIpaDate(String ipaDate) {
		this.ipaDate = ipaDate;
	}
	
	@Column(name = "CERTISSUEDATE", length = 10)
	public String getCertIssueDate() {
		return certIssueDate;
	}
	public void setCertIssueDate(String certIssueDate) {
		this.certIssueDate = certIssueDate;
	}
	
	@Column(name = "CERTDUEDATE", length = 10)
	public String getCertDueDate() {
		return certDueDate;
	}
	public void setCertDueDate(String certDueDate) {
		this.certDueDate = certDueDate;
	}
	
	@Column(name = "ACTUALRECEIPTDATE", length = 10)
	public String getActualReceiptDate() {
		return actualReceiptDate;
	}
	public void setActualReceiptDate(String actualReceiptDate) {
		this.actualReceiptDate = actualReceiptDate;
	}
	
	@Column(name = "TOTALRECEIPTAMOUNT", length = 19)
	public String getTotalReceiptAmount() {
		return totalReceiptAmount;
	}
	public void setTotalReceiptAmount(String totalReceiptAmount) {
		this.totalReceiptAmount = totalReceiptAmount;
	}
	
	@Column(name = "BENCHMARK1", length = 40)
	public String getBenchmark1() {
		return benchmark1;
	}
	public void setBenchmark1(String benchmark1) {
		this.benchmark1 = benchmark1;
	}
	
	@Column(name = "BENCHMARK2", length = 40)
	public String getBenchmark2() {
		return benchmark2;
	}
	public void setBenchmark2(String benchmark2) {
		this.benchmark2 = benchmark2;
	}
	
}
