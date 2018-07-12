package com.gammon.pcms.model.hr;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gammon.pcms.model.User;


/**
 * The persistent class for the HP_ORG_USER database table.
 * 
 */
@Entity
@Table(name="HP_ORG_USER")
@NamedQuery(name="HrUser.findAll", query="SELECT h FROM HrUser h")
public class HrUser implements User, Serializable {
	private static final long serialVersionUID = 898364064465303341L;
	@JsonIgnore
	private String guid;
	@JsonIgnore
	private String addrStreet;
	@JsonIgnore
	private String adsPath;
	@JsonIgnore
	private String city;
	@JsonIgnore
	private String ctryCode;
	private String department;
	@JsonIgnore
	private String deptGuid;
	@JsonIgnore
	private String designation;
	private String division;
	@JsonIgnore
	private Timestamp dtCreated;
	@JsonIgnore
	private Timestamp dtLastLogon;
	private String email;
	private String employeeId;
	@JsonIgnore
	private String expertCertificates;
	@JsonIgnore
	private String expertStatus;
	@JsonIgnore
	private String expertSummary;
	@JsonIgnore
	private String faxOffice;
	@JsonIgnore
	private String ipAddr;
	@JsonIgnore
	private String isOnline;
	private String mobile;
	@JsonIgnore
	private Timestamp myProfile_LastUpdate;
	@JsonIgnore
	private String nickname;
	private String fullName;
	@JsonIgnore
	private String pager;
	@JsonIgnore
	private String personalHobbies;
	@JsonIgnore
	private String personalProfiles;
	@JsonIgnore
	private String photoUrl;
	private String physicalDeliveryOfficeName;
	@JsonIgnore
	private Timestamp skillLastupdate;
	@JsonIgnore
	private String state;
	@JsonIgnore
	private String status;
	private String supportBy;
	@JsonIgnore
	private String telHome;
	private String telOffice;
	private String title;
	private String username;
	@JsonIgnore
	private String webPageUrl;
	@JsonIgnore
	private String zipCode;

	public HrUser() {
	}

	public HrUser(String username){
		this.username = username;
	}

	@Id
	@Column(name="GUID")
	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}


	@Column(name="ADDR_STREET")
	public String getAddrStreet() {
		return this.addrStreet;
	}

	public void setAddrStreet(String addrStreet) {
		this.addrStreet = addrStreet;
	}


	public String getAdsPath() {
		return this.adsPath;
	}

	public void setAdsPath(String adsPath) {
		this.adsPath = adsPath;
	}


	@Column(name="CITY")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	@Column(name="CTRY_CODE")
	public String getCtryCode() {
		return this.ctryCode;
	}

	public void setCtryCode(String ctryCode) {
		this.ctryCode = ctryCode;
	}


	@Column(name="DEPARTMENT")
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}


	@Column(name="DEPT_GUID")
	public String getDeptGuid() {
		return this.deptGuid;
	}

	public void setDeptGuid(String deptGuid) {
		this.deptGuid = deptGuid;
	}


	@Column(name="DESIGNATION")
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}


	@Column(name="DIVISION")
	public String getDivision() {
		return this.division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	@Column(name="DT_CREATED")
	public Timestamp getDtCreated() {
		return this.dtCreated;
	}

	public void setDtCreated(Timestamp dtCreated) {
		this.dtCreated = dtCreated;
	}

	@Column(name="DT_LAST_LOGON")
	public Timestamp getDtLastLogon() {
		return this.dtLastLogon;
	}

	public void setDtLastLogon(Timestamp dtLastLogon) {
		this.dtLastLogon = dtLastLogon;
	}


	@Email
	@Column(name="EMAIL")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Column(name="EMPLOYEE_ID")
	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	@Column(name="EXPERT_CERTIFICATES")
	public String getExpertCertificates() {
		return this.expertCertificates;
	}

	public void setExpertCertificates(String expertCertificates) {
		this.expertCertificates = expertCertificates;
	}


	@Column(name="EXPERT_STATUS")
	public String getExpertStatus() {
		return this.expertStatus;
	}

	public void setExpertStatus(String expertStatus) {
		this.expertStatus = expertStatus;
	}


	@Column(name="EXPERT_SUMMARY")
	public String getExpertSummary() {
		return this.expertSummary;
	}

	public void setExpertSummary(String expertSummary) {
		this.expertSummary = expertSummary;
	}


	@Column(name="FAX_OFFICE")
	public String getFaxOffice() {
		return this.faxOffice;
	}

	public void setFaxOffice(String faxOffice) {
		this.faxOffice = faxOffice;
	}

	public String getIpAddr() {
		return this.ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}


	@Column(name="IsOnline")
	public String getIsOnline() {
		return this.isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}


	@Column(name="MOBILE")
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	@Column(name="MyProfile_LastUpdate")
	public Timestamp getMyProfile_LastUpdate() {
		return this.myProfile_LastUpdate;
	}

	public void setMyProfile_LastUpdate(Timestamp myProfile_LastUpdate) {
		this.myProfile_LastUpdate = myProfile_LastUpdate;
	}


	@Column(name="NICKNAME")
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	@Column(name="NM")
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	@Column(name="PAGER")
	public String getPager() {
		return this.pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}


	@Column(name="PERSONAL_HOBBIES")
	public String getPersonalHobbies() {
		return this.personalHobbies;
	}

	public void setPersonalHobbies(String personalHobbies) {
		this.personalHobbies = personalHobbies;
	}


	@Column(name="PERSONAL_PROFILES")
	public String getPersonalProfiles() {
		return this.personalProfiles;
	}

	public void setPersonalProfiles(String personalProfiles) {
		this.personalProfiles = personalProfiles;
	}


	@Column(name="PHOTO_URL")
	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}


	public String getPhysicalDeliveryOfficeName() {
		return this.physicalDeliveryOfficeName;
	}

	public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
		this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
	}


	@Column(name="SKILL_LASTUPDATE")
	public Timestamp getSkillLastupdate() {
		return this.skillLastupdate;
	}

	public void setSkillLastupdate(Timestamp skillLastupdate) {
		this.skillLastupdate = skillLastupdate;
	}


	@Column(name="[STATE]")
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}


	@Column(name="STATUS")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(name="SUPPORT_BY")
	public String getSupportBy() {
		return this.supportBy;
	}

	public void setSupportBy(String supportBy) {
		this.supportBy = supportBy;
	}


	@Column(name="TEL_HOME")
	public String getTelHome() {
		return this.telHome;
	}

	public void setTelHome(String telHome) {
		this.telHome = telHome;
	}


	@Column(name="TEL_OFFICE")
	public String getTelOffice() {
		return this.telOffice;
	}

	public void setTelOffice(String telOffice) {
		this.telOffice = telOffice;
	}


	@Column(name="TITLE")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	@Column(name="[UID]")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	@Column(name="WEB_PAGE_URL")
	public String getWebPageUrl() {
		return this.webPageUrl;
	}

	public void setWebPageUrl(String webPageUrl) {
		this.webPageUrl = webPageUrl;
	}


	@Column(name="ZIP_CODE")
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}