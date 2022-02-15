package com.gammon.pcms.model.adl;
// Generated Jul 25, 2016 6:30:47 PM by Hibernate Tools 4.3.4.Final

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.view.AddressBookView;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * DimAddressBook generated by hbm2java
 */
@Entity
@Table(name = "DIM_ADDRESS_BOOK")
public class AddressBook implements java.io.Serializable {

	private static final long serialVersionUID = -395660491296207763L;
	
	public static final String TYPE_VENDOR = "V  ";
	public static final String TYPE_CLIENT = "C  ";
	public static final String TYPE_COMPANY = "O  ";
	
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private BigDecimal addressBookNumber;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class, AddressBookView.Name.class})
	private String addressBookName;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String businessRegistrationNumber;
	private String securityBu;
	private String vendorApprStatusCode;
	private String vendorApprovalStatus;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String addressBookTypeCode;
	private String addrBookType;
	private String supplierApprovalCode;
	private String supplierApproval;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String subcontractorApprovalCode;
	private String subcontractorApproval;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String vendorTypeCode;
	private String vendorType;
	private String clientGroupCode;
	private String clientGroup;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String vendorStatusCode;
	private String vendorStatus;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private String holdCode;
	private String hold;
	private String vmsExemptedCode;
	private String vmsExempted;
	private String proposedVendorCode;
	private String proposedVendor;
	private String businessSectorCode;
	private String businessSector;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private PayeeMaster payeeMaster;
	@JsonView({AddressBookView.SubcontractorAndClientEnquiry.class})
	private List<SubcontractorWorkscope> subcontractorWorkscopes;
	
	public AddressBook() {
	}

	public AddressBook(BigDecimal addressBookNumber, String addressBookName, String businessRegistrationNumber,
			String securityBu, String vendorApprStatusCode, String vendorApprovalStatus, String addressBookTypeCode,
			String addrBookType, String supplierApprovalCode, String supplierApproval, String subcontractorApprovalCode,
			String subcontractorApproval, String vendorTypeCode, String vendorType, String clientGroupCode,
			String clientGroup, String vendorStatusCode, String vendorStatus, String holdCode, String hold,
			String vmsExemptedCode, String vmsExempted, String proposedVendorCode, String proposedVendor,
			String businessSectorCode, String businessSector) {
		this.addressBookNumber = addressBookNumber;
		this.addressBookName = addressBookName;
		this.businessRegistrationNumber = businessRegistrationNumber;
		this.securityBu = securityBu;
		this.vendorApprStatusCode = vendorApprStatusCode;
		this.vendorApprovalStatus = vendorApprovalStatus;
		this.addressBookTypeCode = addressBookTypeCode;
		this.addrBookType = addrBookType;
		this.supplierApprovalCode = supplierApprovalCode;
		this.supplierApproval = supplierApproval;
		this.subcontractorApprovalCode = subcontractorApprovalCode;
		this.subcontractorApproval = subcontractorApproval;
		this.vendorTypeCode = vendorTypeCode;
		this.vendorType = vendorType;
		this.clientGroupCode = clientGroupCode;
		this.clientGroup = clientGroup;
		this.vendorStatusCode = vendorStatusCode;
		this.vendorStatus = vendorStatus;
		this.holdCode = holdCode;
		this.hold = hold;
		this.vmsExemptedCode = vmsExemptedCode;
		this.vmsExempted = vmsExempted;
		this.proposedVendorCode = proposedVendorCode;
		this.proposedVendor = proposedVendor;
		this.businessSectorCode = businessSectorCode;
		this.businessSector = businessSector;
	}

	@Id
	@Column(name = "ADDRESS_BOOK_NUMBER", insertable=false, updatable=false, precision = 22, scale = 0)
	public BigDecimal getAddressBookNumber() {
		return this.addressBookNumber;
	}

	public void setAddressBookNumber(BigDecimal addressBookNumber) {
		this.addressBookNumber = addressBookNumber;
	}

	@Column(name = "ADDRESS_BOOK_NAME", insertable=false, updatable=false, length = 160)
	public String getAddressBookName() {
		return this.addressBookName;
	}

	public void setAddressBookName(String addressBookName) {
		this.addressBookName = addressBookName;
	}

	@Column(name = "BUSINESS_REGISTRATION_NUMBER", insertable=false, updatable=false, length = 80)
	public String getBusinessRegistrationNumber() {
		return this.businessRegistrationNumber;
	}

	public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
		this.businessRegistrationNumber = businessRegistrationNumber;
	}

	@Column(name = "SECURITY_BU", insertable=false, updatable=false, length = 48)
	public String getSecurityBu() {
		return this.securityBu;
	}

	public void setSecurityBu(String securityBu) {
		this.securityBu = securityBu;
	}

	@Column(name = "VENDOR_APPR_STATUS_CODE", insertable=false, updatable=false, length = 40)
	public String getVendorApprStatusCode() {
		return this.vendorApprStatusCode;
	}

	public void setVendorApprStatusCode(String vendorApprStatusCode) {
		this.vendorApprStatusCode = vendorApprStatusCode;
	}

	@Column(name = "VENDOR_APPROVAL_STATUS", insertable=false, updatable=false, length = 244)
	public String getVendorApprovalStatus() {
		return this.vendorApprovalStatus;
	}

	public void setVendorApprovalStatus(String vendorApprovalStatus) {
		this.vendorApprovalStatus = vendorApprovalStatus;
	}

	@Column(name = "ADDRESS_BOOK_TYPE_CODE", insertable=false, updatable=false, length = 12)
	public String getAddressBookTypeCode() {
		return this.addressBookTypeCode;
	}

	public void setAddressBookTypeCode(String addressBookTypeCode) {
		this.addressBookTypeCode = addressBookTypeCode;
	}

	@Column(name = "ADDR_BOOK_TYPE", insertable=false, updatable=false, length = 244)
	public String getAddrBookType() {
		return this.addrBookType;
	}

	public void setAddrBookType(String addrBookType) {
		this.addrBookType = addrBookType;
	}

	@Column(name = "SUPPLIER_APPROVAL_CODE", insertable=false, updatable=false, length = 12)
	public String getSupplierApprovalCode() {
		return this.supplierApprovalCode;
	}

	public void setSupplierApprovalCode(String supplierApprovalCode) {
		this.supplierApprovalCode = supplierApprovalCode;
	}

	@Column(name = "SUPPLIER_APPROVAL", insertable=false, updatable=false, length = 244)
	public String getSupplierApproval() {
		return this.supplierApproval;
	}

	public void setSupplierApproval(String supplierApproval) {
		this.supplierApproval = supplierApproval;
	}

	@Column(name = "SUBCONTRACTOR_APPROVAL_CODE", insertable=false, updatable=false, length = 12)
	public String getSubcontractorApprovalCode() {
		return this.subcontractorApprovalCode;
	}

	public void setSubcontractorApprovalCode(String subcontractorApprovalCode) {
		this.subcontractorApprovalCode = subcontractorApprovalCode;
	}

	@Column(name = "SUBCONTRACTOR_APPROVAL", insertable=false, updatable=false, length = 244)
	public String getSubcontractorApproval() {
		return this.subcontractorApproval;
	}

	public void setSubcontractorApproval(String subcontractorApproval) {
		this.subcontractorApproval = subcontractorApproval;
	}

	@Column(name = "VENDOR_TYPE_CODE", insertable=false, updatable=false, length = 12)
	public String getVendorTypeCode() {
		return this.vendorTypeCode;
	}

	public void setVendorTypeCode(String vendorTypeCode) {
		this.vendorTypeCode = vendorTypeCode;
	}

	@Column(name = "VENDOR_TYPE", insertable=false, updatable=false, length = 244)
	public String getVendorType() {
		return this.vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	@Column(name = "CLIENT_GROUP_CODE", insertable=false, updatable=false, length = 12)
	public String getClientGroupCode() {
		return this.clientGroupCode;
	}

	public void setClientGroupCode(String clientGroupCode) {
		this.clientGroupCode = clientGroupCode;
	}

	@Column(name = "CLIENT_GROUP", insertable=false, updatable=false, length = 244)
	public String getClientGroup() {
		return this.clientGroup;
	}

	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
	}

	@Column(name = "VENDOR_STATUS_CODE", insertable=false, updatable=false, length = 12)
	public String getVendorStatusCode() {
		return this.vendorStatusCode;
	}

	public void setVendorStatusCode(String vendorStatusCode) {
		this.vendorStatusCode = vendorStatusCode;
	}

	@Column(name = "VENDOR_STATUS", insertable=false, updatable=false, length = 244)
	public String getVendorStatus() {
		return this.vendorStatus;
	}

	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	@Column(name = "HOLD_CODE", insertable=false, updatable=false, length = 12)
	public String getHoldCode() {
		return this.holdCode;
	}

	public void setHoldCode(String holdCode) {
		this.holdCode = holdCode;
	}

	@Column(name = "HOLD", insertable=false, updatable=false, length = 244)
	public String getHold() {
		return this.hold;
	}

	public void setHold(String hold) {
		this.hold = hold;
	}

	@Column(name = "VMS_EXEMPTED_CODE", insertable=false, updatable=false, length = 12)
	public String getVmsExemptedCode() {
		return this.vmsExemptedCode;
	}

	public void setVmsExemptedCode(String vmsExemptedCode) {
		this.vmsExemptedCode = vmsExemptedCode;
	}

	@Column(name = "VMS_EXEMPTED", insertable=false, updatable=false, length = 244)
	public String getVmsExempted() {
		return this.vmsExempted;
	}

	public void setVmsExempted(String vmsExempted) {
		this.vmsExempted = vmsExempted;
	}

	@Column(name = "PROPOSED_VENDOR_CODE", insertable=false, updatable=false, length = 12)
	public String getProposedVendorCode() {
		return this.proposedVendorCode;
	}

	public void setProposedVendorCode(String proposedVendorCode) {
		this.proposedVendorCode = proposedVendorCode;
	}

	@Column(name = "PROPOSED_VENDOR", insertable=false, updatable=false, length = 244)
	public String getProposedVendor() {
		return this.proposedVendor;
	}

	public void setProposedVendor(String proposedVendor) {
		this.proposedVendor = proposedVendor;
	}

	@Column(name = "BUSINESS_SECTOR_CODE", insertable=false, updatable=false, length = 12)
	public String getBusinessSectorCode() {
		return this.businessSectorCode;
	}

	public void setBusinessSectorCode(String businessSectorCode) {
		this.businessSectorCode = businessSectorCode;
	}

	@Column(name = "BUSINESS_SECTOR", insertable=false, updatable=false, length = 244)
	public String getBusinessSector() {
		return this.businessSector;
	}

	public void setBusinessSector(String businessSector) {
		this.businessSector = businessSector;
	}

	//bi-directional many-to-one association to SubcontractorWorkscope
	@OneToMany(mappedBy="addressBook")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<SubcontractorWorkscope> getSubcontractorWorkscopes() {
		return this.subcontractorWorkscopes;
	}

	public void setSubcontractorWorkscopes(List<SubcontractorWorkscope> subcontractorWorkscopes) {
		this.subcontractorWorkscopes = subcontractorWorkscopes;
	}

	//bi-directional one-to-one association to PayeeMaster
	@OneToOne (mappedBy="addressBook")
	public PayeeMaster getPayeeMaster() {
		return this.payeeMaster;
	}

	public void setPayeeMaster(PayeeMaster payeeMaster) {
		this.payeeMaster = payeeMaster;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AddressBook [addressBookNumber=" + addressBookNumber + ", addressBookName=" + addressBookName
				+ ", businessRegistrationNumber=" + businessRegistrationNumber + ", securityBu=" + securityBu
				+ ", vendorApprStatusCode=" + vendorApprStatusCode + ", vendorApprovalStatus=" + vendorApprovalStatus
				+ ", addressBookTypeCode=" + addressBookTypeCode + ", addrBookType=" + addrBookType
				+ ", supplierApprovalCode=" + supplierApprovalCode + ", supplierApproval=" + supplierApproval
				+ ", subcontractorApprovalCode=" + subcontractorApprovalCode + ", subcontractorApproval="
				+ subcontractorApproval + ", vendorTypeCode=" + vendorTypeCode + ", vendorType=" + vendorType
				+ ", clientGroupCode=" + clientGroupCode + ", clientGroup=" + clientGroup + ", vendorStatusCode="
				+ vendorStatusCode + ", vendorStatus=" + vendorStatus + ", holdCode=" + holdCode + ", hold=" + hold
				+ ", vmsExemptedCode=" + vmsExemptedCode + ", vmsExempted=" + vmsExempted + ", proposedVendorCode="
				+ proposedVendorCode + ", proposedVendor=" + proposedVendor + ", businessSectorCode="
				+ businessSectorCode + ", businessSector=" + businessSector + ", PayeeMaster.holdPaymentCode=" 
				+ (payeeMaster != null && payeeMaster.getHoldPaymentCode() != null ? payeeMaster.getHoldPaymentCode() : "null")
				+ ", SubcontractorWorkscopes=" + subcontractorWorkscopes + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payeeMaster == null) ? 0 : payeeMaster.hashCode());
		result = prime * result + ((subcontractorWorkscopes == null) ? 0 : subcontractorWorkscopes.hashCode());
		result = prime * result + ((addrBookType == null) ? 0 : addrBookType.hashCode());
		result = prime * result + ((addressBookName == null) ? 0 : addressBookName.hashCode());
		result = prime * result + ((addressBookNumber == null) ? 0 : addressBookNumber.hashCode());
		result = prime * result + ((addressBookTypeCode == null) ? 0 : addressBookTypeCode.hashCode());
		result = prime * result + ((businessRegistrationNumber == null) ? 0 : businessRegistrationNumber.hashCode());
		result = prime * result + ((businessSector == null) ? 0 : businessSector.hashCode());
		result = prime * result + ((businessSectorCode == null) ? 0 : businessSectorCode.hashCode());
		result = prime * result + ((clientGroup == null) ? 0 : clientGroup.hashCode());
		result = prime * result + ((clientGroupCode == null) ? 0 : clientGroupCode.hashCode());
		result = prime * result + ((hold == null) ? 0 : hold.hashCode());
		result = prime * result + ((holdCode == null) ? 0 : holdCode.hashCode());
		result = prime * result + ((proposedVendor == null) ? 0 : proposedVendor.hashCode());
		result = prime * result + ((proposedVendorCode == null) ? 0 : proposedVendorCode.hashCode());
		result = prime * result + ((securityBu == null) ? 0 : securityBu.hashCode());
		result = prime * result + ((subcontractorApproval == null) ? 0 : subcontractorApproval.hashCode());
		result = prime * result + ((subcontractorApprovalCode == null) ? 0 : subcontractorApprovalCode.hashCode());
		result = prime * result + ((supplierApproval == null) ? 0 : supplierApproval.hashCode());
		result = prime * result + ((supplierApprovalCode == null) ? 0 : supplierApprovalCode.hashCode());
		result = prime * result + ((vendorApprStatusCode == null) ? 0 : vendorApprStatusCode.hashCode());
		result = prime * result + ((vendorApprovalStatus == null) ? 0 : vendorApprovalStatus.hashCode());
		result = prime * result + ((vendorStatus == null) ? 0 : vendorStatus.hashCode());
		result = prime * result + ((vendorStatusCode == null) ? 0 : vendorStatusCode.hashCode());
		result = prime * result + ((vendorType == null) ? 0 : vendorType.hashCode());
		result = prime * result + ((vendorTypeCode == null) ? 0 : vendorTypeCode.hashCode());
		result = prime * result + ((vmsExempted == null) ? 0 : vmsExempted.hashCode());
		result = prime * result + ((vmsExemptedCode == null) ? 0 : vmsExemptedCode.hashCode());
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
		if (!(obj instanceof AddressBook)) {
			return false;
		}
		AddressBook other = (AddressBook) obj;
		if (payeeMaster == null) {
			if (other.payeeMaster != null) {
				return false;
			}
		} else if (!payeeMaster.equals(other.payeeMaster)) {
			return false;
		}
		if (subcontractorWorkscopes == null) {
			if (other.subcontractorWorkscopes != null) {
				return false;
			}
		} else if (!subcontractorWorkscopes.equals(other.subcontractorWorkscopes)) {
			return false;
		}
		if (addrBookType == null) {
			if (other.addrBookType != null) {
				return false;
			}
		} else if (!addrBookType.equals(other.addrBookType)) {
			return false;
		}
		if (addressBookName == null) {
			if (other.addressBookName != null) {
				return false;
			}
		} else if (!addressBookName.equals(other.addressBookName)) {
			return false;
		}
		if (addressBookNumber == null) {
			if (other.addressBookNumber != null) {
				return false;
			}
		} else if (!addressBookNumber.equals(other.addressBookNumber)) {
			return false;
		}
		if (addressBookTypeCode == null) {
			if (other.addressBookTypeCode != null) {
				return false;
			}
		} else if (!addressBookTypeCode.equals(other.addressBookTypeCode)) {
			return false;
		}
		if (businessRegistrationNumber == null) {
			if (other.businessRegistrationNumber != null) {
				return false;
			}
		} else if (!businessRegistrationNumber.equals(other.businessRegistrationNumber)) {
			return false;
		}
		if (businessSector == null) {
			if (other.businessSector != null) {
				return false;
			}
		} else if (!businessSector.equals(other.businessSector)) {
			return false;
		}
		if (businessSectorCode == null) {
			if (other.businessSectorCode != null) {
				return false;
			}
		} else if (!businessSectorCode.equals(other.businessSectorCode)) {
			return false;
		}
		if (clientGroup == null) {
			if (other.clientGroup != null) {
				return false;
			}
		} else if (!clientGroup.equals(other.clientGroup)) {
			return false;
		}
		if (clientGroupCode == null) {
			if (other.clientGroupCode != null) {
				return false;
			}
		} else if (!clientGroupCode.equals(other.clientGroupCode)) {
			return false;
		}
		if (hold == null) {
			if (other.hold != null) {
				return false;
			}
		} else if (!hold.equals(other.hold)) {
			return false;
		}
		if (holdCode == null) {
			if (other.holdCode != null) {
				return false;
			}
		} else if (!holdCode.equals(other.holdCode)) {
			return false;
		}
		if (proposedVendor == null) {
			if (other.proposedVendor != null) {
				return false;
			}
		} else if (!proposedVendor.equals(other.proposedVendor)) {
			return false;
		}
		if (proposedVendorCode == null) {
			if (other.proposedVendorCode != null) {
				return false;
			}
		} else if (!proposedVendorCode.equals(other.proposedVendorCode)) {
			return false;
		}
		if (securityBu == null) {
			if (other.securityBu != null) {
				return false;
			}
		} else if (!securityBu.equals(other.securityBu)) {
			return false;
		}
		if (subcontractorApproval == null) {
			if (other.subcontractorApproval != null) {
				return false;
			}
		} else if (!subcontractorApproval.equals(other.subcontractorApproval)) {
			return false;
		}
		if (subcontractorApprovalCode == null) {
			if (other.subcontractorApprovalCode != null) {
				return false;
			}
		} else if (!subcontractorApprovalCode.equals(other.subcontractorApprovalCode)) {
			return false;
		}
		if (supplierApproval == null) {
			if (other.supplierApproval != null) {
				return false;
			}
		} else if (!supplierApproval.equals(other.supplierApproval)) {
			return false;
		}
		if (supplierApprovalCode == null) {
			if (other.supplierApprovalCode != null) {
				return false;
			}
		} else if (!supplierApprovalCode.equals(other.supplierApprovalCode)) {
			return false;
		}
		if (vendorApprStatusCode == null) {
			if (other.vendorApprStatusCode != null) {
				return false;
			}
		} else if (!vendorApprStatusCode.equals(other.vendorApprStatusCode)) {
			return false;
		}
		if (vendorApprovalStatus == null) {
			if (other.vendorApprovalStatus != null) {
				return false;
			}
		} else if (!vendorApprovalStatus.equals(other.vendorApprovalStatus)) {
			return false;
		}
		if (vendorStatus == null) {
			if (other.vendorStatus != null) {
				return false;
			}
		} else if (!vendorStatus.equals(other.vendorStatus)) {
			return false;
		}
		if (vendorStatusCode == null) {
			if (other.vendorStatusCode != null) {
				return false;
			}
		} else if (!vendorStatusCode.equals(other.vendorStatusCode)) {
			return false;
		}
		if (vendorType == null) {
			if (other.vendorType != null) {
				return false;
			}
		} else if (!vendorType.equals(other.vendorType)) {
			return false;
		}
		if (vendorTypeCode == null) {
			if (other.vendorTypeCode != null) {
				return false;
			}
		} else if (!vendorTypeCode.equals(other.vendorTypeCode)) {
			return false;
		}
		if (vmsExempted == null) {
			if (other.vmsExempted != null) {
				return false;
			}
		} else if (!vmsExempted.equals(other.vmsExempted)) {
			return false;
		}
		if (vmsExemptedCode == null) {
			if (other.vmsExemptedCode != null) {
				return false;
			}
		} else if (!vmsExemptedCode.equals(other.vmsExemptedCode)) {
			return false;
		}
		return true;
	}


}
