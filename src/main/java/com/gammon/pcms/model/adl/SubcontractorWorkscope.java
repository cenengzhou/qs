package com.gammon.pcms.model.adl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.view.AddressBookView;
import com.gammon.pcms.model.adl.id.SubcontractorWorkscopeId;

/**
 * The persistent class for the DIM_SUBCONTRACTOR_WORKSCOPE database table.
 * 
 */
@Entity
@IdClass(SubcontractorWorkscopeId.class)
@Table(name="DIM_SUBCONTRACTOR_WORKSCOPE")
public class SubcontractorWorkscope implements Serializable, Comparable<SubcontractorWorkscope> {
	private static final long serialVersionUID = -1685998807071641221L;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class, workscopeCodeAndDescription.class})
	private String codeWorkscope;
	private Date dateEffectiveFrom;
	private Date dateEffectiveTo;
	@JsonView({workscopeCodeAndDescription.class})
	private String workscopeDescription;
	private AddressBook addressBook;
	@JsonView({statusPlusCodeAndDescription.class})
	private String statusApproval;
	
	public SubcontractorWorkscope() {
	}

	/**
	 * @param codeWorkscope
	 * @param addressBook
	 */
	public SubcontractorWorkscope(String codeWorkscope, AddressBook addressBook) {
		this.codeWorkscope = codeWorkscope;
		this.addressBook = addressBook;
	}

	/**
	 * @param codeWorkscope
	 * @param dateEffectiveFrom
	 * @param dateEffectiveTo
	 * @param workscopeDescription
	 * @param addressBook
	 */
	public SubcontractorWorkscope(String codeWorkscope, Date dateEffectiveFrom, Date dateEffectiveTo,
			String workscopeDescription, AddressBook addressBook) {
		this.codeWorkscope = codeWorkscope;
		this.dateEffectiveFrom = dateEffectiveFrom;
		this.dateEffectiveTo = dateEffectiveTo;
		this.workscopeDescription = workscopeDescription;
		this.addressBook = addressBook;
	}

	@Id
	public String getCodeWorkscope() {
		return this.codeWorkscope;
	}

	public void setCodeWorkscope(String codeWorkscope) {
		this.codeWorkscope = codeWorkscope;
	}

	@Id
	public AddressBook getAddressBook() {
		return this.addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_EFFECTIVE_FROM", insertable=false, updatable=false)
	public Date getDateEffectiveFrom() {
		return this.dateEffectiveFrom;
	}

	public void setDateEffectiveFrom(Date dateEffectiveFrom) {
		this.dateEffectiveFrom = dateEffectiveFrom;
	}


	@Temporal(TemporalType.DATE)
	@Column(name="DATE_EFFECTIVE_TO")
	public Date getDateEffectiveTo() {
		return this.dateEffectiveTo;
	}

	public void setDateEffectiveTo(Date dateEffectiveTo) {
		this.dateEffectiveTo = dateEffectiveTo;
	}


	@Column(name="WORKSCOPE_DESCRIPTION", length=30)
	public String getWorkscopeDescription() {
		return this.workscopeDescription;
	}

	public void setWorkscopeDescription(String workscopeDescription) {
		this.workscopeDescription = workscopeDescription;
	}


	@Column(name="STATUS_APPROVAL", length=10)
	public String getStatusApproval() {
		return statusApproval != null ? statusApproval.trim() : "";
	}

	public void setStatusApproval(String statusApproval) {
		this.statusApproval = statusApproval;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubcontractorWorkscope [codeWorkscope=" + codeWorkscope + ", dateEffectiveFrom=" + dateEffectiveFrom
				+ ", dateEffectiveTo=" + dateEffectiveTo + ", workscopeDescription=" + workscopeDescription
				+ ", addressBook=" + addressBook + ", statusApproval=" + statusApproval + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressBook == null) ? 0 : addressBook.hashCode());
		result = prime * result + ((codeWorkscope == null) ? 0 : codeWorkscope.hashCode());
		result = prime * result + ((dateEffectiveFrom == null) ? 0 : dateEffectiveFrom.hashCode());
		result = prime * result + ((dateEffectiveTo == null) ? 0 : dateEffectiveTo.hashCode());
		result = prime * result + ((statusApproval == null) ? 0 : statusApproval.hashCode());
		result = prime * result + ((workscopeDescription == null) ? 0 : workscopeDescription.hashCode());
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
		if (!(obj instanceof SubcontractorWorkscope)) {
			return false;
		}
		SubcontractorWorkscope other = (SubcontractorWorkscope) obj;
		if (addressBook == null) {
			if (other.addressBook != null) {
				return false;
			}
		} else if (!addressBook.equals(other.addressBook)) {
			return false;
		}
		if (codeWorkscope == null) {
			if (other.codeWorkscope != null) {
				return false;
			}
		} else if (!codeWorkscope.equals(other.codeWorkscope)) {
			return false;
		}
		if (dateEffectiveFrom == null) {
			if (other.dateEffectiveFrom != null) {
				return false;
			}
		} else if (!dateEffectiveFrom.equals(other.dateEffectiveFrom)) {
			return false;
		}
		if (dateEffectiveTo == null) {
			if (other.dateEffectiveTo != null) {
				return false;
			}
		} else if (!dateEffectiveTo.equals(other.dateEffectiveTo)) {
			return false;
		}
		if (statusApproval == null) {
			if (other.statusApproval != null) {
				return false;
			}
		} else if (!statusApproval.equals(other.statusApproval)) {
			return false;
		}
		if (workscopeDescription == null) {
			if (other.workscopeDescription != null) {
				return false;
			}
		} else if (!workscopeDescription.equals(other.workscopeDescription)) {
			return false;
		}
		return true;
	}
	
	public interface workscopeCodeAndDescription {}
	public interface statusPlusCodeAndDescription extends workscopeCodeAndDescription{}

	@Override
	public int compareTo(SubcontractorWorkscope o) {
		int result = this.getCodeWorkscope().compareTo(o.getCodeWorkscope());
		return result;
	}

}