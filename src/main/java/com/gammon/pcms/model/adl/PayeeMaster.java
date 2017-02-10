package com.gammon.pcms.model.adl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.view.AddressBookView;


/**
 * The persistent class for the DIM_PAYEE_MASTER database table.
 * 
 */
@Entity
@Table(name="DIM_PAYEE_MASTER")
public class PayeeMaster implements Serializable {

	private static final long serialVersionUID = -7074637787653828776L;
	private AddressBook addressBook;
	private String currencyCode;
	private String evaluatedReceipt;
	private String holdOrderCode;
	private String holdOrderReason;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String holdPaymentCode;
	private String paymentCreationLevel;
	private String paymentMethod;
	private String paymentMethodCode;
	private String paymentTermCode;
	private String taxCode;
	private String taxExplanationCode;
	private String taxExplanationDescription;

	public PayeeMaster() {
	}


	@Column(name="CURRENCY_CODE", insertable=false, updatable=false)
	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	@Column(name="EVALUATED_RECEIPT", insertable=false, updatable=false)
	public String getEvaluatedReceipt() {
		return this.evaluatedReceipt;
	}

	public void setEvaluatedReceipt(String evaluatedReceipt) {
		this.evaluatedReceipt = evaluatedReceipt;
	}


	@Column(name="HOLD_ORDER_CODE", insertable=false, updatable=false)
	public String getHoldOrderCode() {
		return this.holdOrderCode;
	}

	public void setHoldOrderCode(String holdOrderCode) {
		this.holdOrderCode = holdOrderCode;
	}


	@Column(name="HOLD_ORDER_REASON", insertable=false, updatable=false)
	public String getHoldOrderReason() {
		return this.holdOrderReason;
	}

	public void setHoldOrderReason(String holdOrderReason) {
		this.holdOrderReason = holdOrderReason;
	}


	@Column(name="HOLD_PAYMENT_CODE", insertable=false, updatable=false)
	public String getHoldPaymentCode() {
		return this.holdPaymentCode;
	}

	public void setHoldPaymentCode(String holdPaymentCode) {
		this.holdPaymentCode = holdPaymentCode;
	}


	@Id
	//bi-directional one-to-one association to AddressBook
	@OneToOne
	@JoinColumn(name="PAYEE_ID", referencedColumnName="ADDRESS_BOOK_NUMBER")
	public AddressBook getAddressBook() {
		return this.addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}


	@Column(name="PAYMENT_CREATION_LEVEL", insertable=false, updatable=false)
	public String getPaymentCreationLevel() {
		return this.paymentCreationLevel;
	}

	public void setPaymentCreationLevel(String paymentCreationLevel) {
		this.paymentCreationLevel = paymentCreationLevel;
	}


	@Column(name="PAYMENT_METHOD", insertable=false, updatable=false)
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Column(name="PAYMENT_METHOD_CODE", insertable=false, updatable=false)
	public String getPaymentMethodCode() {
		return this.paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}


	@Column(name="PAYMENT_TERM_CODE", insertable=false, updatable=false)
	public String getPaymentTermCode() {
		return this.paymentTermCode;
	}

	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}


	@Column(name="TAX_CODE", insertable=false, updatable=false)
	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}


	@Column(name="TAX_EXPLANATION_CODE", insertable=false, updatable=false)
	public String getTaxExplanationCode() {
		return this.taxExplanationCode;
	}

	public void setTaxExplanationCode(String taxExplanationCode) {
		this.taxExplanationCode = taxExplanationCode;
	}


	@Column(name="TAX_EXPLANATION_DESCRIPTION", insertable=false, updatable=false)
	public String getTaxExplanationDescription() {
		return this.taxExplanationDescription;
	}

	public void setTaxExplanationDescription(String taxExplanationDescription) {
		this.taxExplanationDescription = taxExplanationDescription;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PayeeMaster [AddressBookNumber=" + addressBook.getAddressBookNumber() + ", currencyCode=" + currencyCode + ", evaluatedReceipt="
				+ evaluatedReceipt + ", holdOrderCode=" + holdOrderCode + ", holdOrderReason=" + holdOrderReason
				+ ", holdPaymentCode=" + holdPaymentCode + ", paymentCreationLevel=" + paymentCreationLevel
				+ ", paymentMethod=" + paymentMethod + ", paymentMethodCode=" + paymentMethodCode + ", paymentTermCode="
				+ paymentTermCode + ", taxCode=" + taxCode + ", taxExplanationCode=" + taxExplanationCode
				+ ", taxExplanationDescription=" + taxExplanationDescription + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressBook == null) ? 0 : addressBook.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((evaluatedReceipt == null) ? 0 : evaluatedReceipt.hashCode());
		result = prime * result + ((holdOrderCode == null) ? 0 : holdOrderCode.hashCode());
		result = prime * result + ((holdOrderReason == null) ? 0 : holdOrderReason.hashCode());
		result = prime * result + ((holdPaymentCode == null) ? 0 : holdPaymentCode.hashCode());
		result = prime * result + ((paymentCreationLevel == null) ? 0 : paymentCreationLevel.hashCode());
		result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = prime * result + ((paymentMethodCode == null) ? 0 : paymentMethodCode.hashCode());
		result = prime * result + ((paymentTermCode == null) ? 0 : paymentTermCode.hashCode());
		result = prime * result + ((taxCode == null) ? 0 : taxCode.hashCode());
		result = prime * result + ((taxExplanationCode == null) ? 0 : taxExplanationCode.hashCode());
		result = prime * result + ((taxExplanationDescription == null) ? 0 : taxExplanationDescription.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PayeeMaster)) {
			return false;
		}
		PayeeMaster other = (PayeeMaster) obj;
		if (addressBook == null) {
			if (other.addressBook != null) {
				return false;
			}
		} else if (!addressBook.equals(other.addressBook)) {
			return false;
		}
		if (currencyCode == null) {
			if (other.currencyCode != null) {
				return false;
			}
		} else if (!currencyCode.equals(other.currencyCode)) {
			return false;
		}
		if (evaluatedReceipt == null) {
			if (other.evaluatedReceipt != null) {
				return false;
			}
		} else if (!evaluatedReceipt.equals(other.evaluatedReceipt)) {
			return false;
		}
		if (holdOrderCode == null) {
			if (other.holdOrderCode != null) {
				return false;
			}
		} else if (!holdOrderCode.equals(other.holdOrderCode)) {
			return false;
		}
		if (holdOrderReason == null) {
			if (other.holdOrderReason != null) {
				return false;
			}
		} else if (!holdOrderReason.equals(other.holdOrderReason)) {
			return false;
		}
		if (holdPaymentCode == null) {
			if (other.holdPaymentCode != null) {
				return false;
			}
		} else if (!holdPaymentCode.equals(other.holdPaymentCode)) {
			return false;
		}
		if (paymentCreationLevel == null) {
			if (other.paymentCreationLevel != null) {
				return false;
			}
		} else if (!paymentCreationLevel.equals(other.paymentCreationLevel)) {
			return false;
		}
		if (paymentMethod == null) {
			if (other.paymentMethod != null) {
				return false;
			}
		} else if (!paymentMethod.equals(other.paymentMethod)) {
			return false;
		}
		if (paymentMethodCode == null) {
			if (other.paymentMethodCode != null) {
				return false;
			}
		} else if (!paymentMethodCode.equals(other.paymentMethodCode)) {
			return false;
		}
		if (paymentTermCode == null) {
			if (other.paymentTermCode != null) {
				return false;
			}
		} else if (!paymentTermCode.equals(other.paymentTermCode)) {
			return false;
		}
		if (taxCode == null) {
			if (other.taxCode != null) {
				return false;
			}
		} else if (!taxCode.equals(other.taxCode)) {
			return false;
		}
		if (taxExplanationCode == null) {
			if (other.taxExplanationCode != null) {
				return false;
			}
		} else if (!taxExplanationCode.equals(other.taxExplanationCode)) {
			return false;
		}
		if (taxExplanationDescription == null) {
			if (other.taxExplanationDescription != null) {
				return false;
			}
		} else if (!taxExplanationDescription.equals(other.taxExplanationDescription)) {
			return false;
		}
		return true;
	}

}