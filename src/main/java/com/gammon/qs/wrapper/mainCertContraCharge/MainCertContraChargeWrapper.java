package com.gammon.qs.wrapper.mainCertContraCharge;

import java.io.Serializable;
import java.util.Date;


public class MainCertContraChargeWrapper implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2244982469085619303L;
		private String costCenter = null;
	    private Integer orderNumber02 = null;
	    private String objectAccount = null;
	    private String subsidiary = null;
	    private Double amountNetPosting001 = null;
	    private Double amountNetPosting002 = null;
	    private String programId = null;
	    private String transactionOriginator = null;
	    private String userId = null;
	    private String workStationId = null;
	    private Date dateUpdated = null;
	    private Integer timeLastUpdated = null;
		public String getCostCenter() {
			return costCenter;
		}
		public void setCostCenter(String costCenter) {
			this.costCenter = costCenter;
		}
		public Integer getOrderNumber02() {
			return orderNumber02;
		}
		public void setOrderNumber02(Integer orderNumber02) {
			this.orderNumber02 = orderNumber02;
		}
		public String getObjectAccount() {
			return objectAccount;
		}
		public void setObjectAccount(String objectAccount) {
			this.objectAccount = objectAccount;
		}
		public String getSubsidiary() {
			return subsidiary;
		}
		public void setSubsidiary(String subsidiary) {
			this.subsidiary = subsidiary;
		}
		public Double getAmountNetPosting001() {
			return amountNetPosting001;
		}
		public void setAmountNetPosting001(Double amountNetPosting001) {
			this.amountNetPosting001 = amountNetPosting001;
		}
		public Double getAmountNetPosting002() {
			return amountNetPosting002;
		}
		public void setAmountNetPosting002(Double amountNetPosting002) {
			this.amountNetPosting002 = amountNetPosting002;
		}
		public String getProgramId() {
			return programId;
		}
		public void setProgramId(String programId) {
			this.programId = programId;
		}
		public String getTransactionOriginator() {
			return transactionOriginator;
		}
		public void setTransactionOriginator(String transactionOriginator) {
			this.transactionOriginator = transactionOriginator;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getWorkStationId() {
			return workStationId;
		}
		public void setWorkStationId(String workStationId) {
			this.workStationId = workStationId;
		}
		public Integer getTimeLastUpdated() {
			return timeLastUpdated;
		}
		public void setTimeLastUpdated(Integer timeLastUpdated) {
			this.timeLastUpdated = timeLastUpdated;
		}
		public void setDateUpdated(Date dateUpdated) {
			this.dateUpdated = dateUpdated;
		}
		public Date getDateUpdated() {
			return dateUpdated;
		}
	    
}
