package com.gammon.pcms.model.adl.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gammon.pcms.model.adl.AddressBook;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Embeddable
public class SubcontractorWorkscopeId implements Serializable {

	private static final long serialVersionUID = -5174280214409806884L;
	private String codeWorkscope;
	private AddressBook addressBook;

	public SubcontractorWorkscopeId() {
	}

	/**
	 * @param codeWorkscope
	 * @param addressBook
	 */
	public SubcontractorWorkscopeId(String codeWorkscope, AddressBook addressBook) {
		this.codeWorkscope = codeWorkscope;
		this.addressBook = addressBook;
	}

	@Id
	@Column(name="CODE_WORKSCOPE", length=10)
	public String getCodeWorkscope() {
		return this.codeWorkscope;
	}

	public void setCodeWorkscope(String codeWorkscope) {
		this.codeWorkscope = codeWorkscope;
	}

	//bi-directional many-to-one association to AddressBook
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name="ENTITY_SUBCONTRACTOR_KEY", referencedColumnName="ADDRESS_BOOK_NUMBER", nullable = true)
	@NotFound(action = NotFoundAction.IGNORE)
	public AddressBook getAddressBook() {
		return this.addressBook;
	}

	public void setAddressBook(AddressBook AddressBook) {
		this.addressBook = AddressBook;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubcontractorWorkscopeId [codeWorkscope=" + codeWorkscope + ", AddressBook=" + addressBook + "]";
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
		if (!(obj instanceof SubcontractorWorkscopeId)) {
			return false;
		}
		SubcontractorWorkscopeId other = (SubcontractorWorkscopeId) obj;
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
		return true;
	}

}
