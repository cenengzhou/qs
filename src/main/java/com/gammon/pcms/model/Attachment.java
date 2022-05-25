package com.gammon.pcms.model;
// Generated Jun 14, 2016 11:29:35 AM by Hibernate Tools 4.3.1.Final

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.gammon.pcms.application.PcmsPersistedAuditObject;

/**
 * Attachment generated by hbm2java
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ATTACHMENT")
@SequenceGenerator(name = "ATTACHMENT_GEN", sequenceName = "ATTACHMENT_SEQ", allocationSize = 1)
public class Attachment extends PcmsPersistedAuditObject {

	private static final long serialVersionUID = 5560045256198537931L;
	public static final String TEXT = "0";
	public static final String FILE = "5";
	public static final String FileLinkForText = " ";

	public static final String AddendumNameObject = "GT58011";
	public static final String SCPaymentNameObject = "GT58012";
	public static final String MainCertNameObject = "GT59026";
	public static final String SCPackageNameObject = "GT58010";
//	public static final String TenderAnalysisNameObject = "GT58023"; //Never used
	public static final String VendorNameObject = "GT58024";
	public static final String SplitNameObject = "SPLIT";
	public static final String TerminateNameObject = "TERMINATE";
	public static final String RepackagingNameObject = "REPACKAGING";
	public static final String TransitNameObject = "TRANSIT";
	public static final String RocSubdetailNameObject = "ROC_SUBDETAIL";
	public static final String ConsultancyNameObject = "CONSULTANCY_AGREEMENT";
	public static final String JobInfoNameObject = "JOBINFO";
	public static final String ADDENDUM_TABLE = "ADDENDUM";
	public static final String PAYMENT_TABLE = "PAYMENT";
	public static final String MAIN_CERT_TABLE = "MAIN_CERT";
	public static final String SUBCONTRACT_TABLE = "SUBCONTRACT";
	public static final String VENDOR_TABLE = "VENDOR";
	public static final String SPLIT_TABLE = "SPLIT";
	public static final String TERMINATE_TABLE = "TERMINATE";
	public static final String REPACKAGING_TABLE = "REPACKAGING";
	public static final String TRANSIT_TABLE = "TRANSIT";
	public static final String ROC_SUBDETAIL_TABLE = "ROC_SUBDETAIL";
	public static final String CONSULTANCY_TABLE = "CONSULTANCY_AGREEMENT";
	public static final String JOBINFO_TABLE = "JOBINFO";
	public static final String NAME_TABLE = "NAME_TABLE";
	public static final String ID_TABLE = "ID_TABLE";
	public static final String TEXTKEY_1 = "noJob";
	public static final String TEXTKEY_2 = "noSubcontract";
	public static final String TEXTKEY_3 = "altParam";
	
	private BigDecimal id;
	private BigDecimal idTable;
	private String nameTable;
	private BigDecimal noSequence;
	private String typeDocument;
	private String nameFile;
	private String pathFile;
	private String text;

	public Attachment() {
	}

	public Attachment(BigDecimal id, BigDecimal idTable, String nameTable, BigDecimal noSequence, String typeDocument,
			String usernameCreated, Date dateCreated) {
		this.id = id;
		this.idTable = idTable;
		this.nameTable = nameTable;
		this.noSequence = noSequence;
		this.typeDocument = typeDocument;
		this.usernameCreated = usernameCreated;
		this.dateCreated = dateCreated;
	}

	public Attachment(BigDecimal id, BigDecimal idTable, String nameTable, BigDecimal noSequence, String typeDocument,
			String nameFile, String pathFile, String text, String usernameCreated, Date dateCreated,
			String usernameLastModified, Date dateLastModified) {
		this.id = id;
		this.idTable = idTable;
		this.nameTable = nameTable;
		this.noSequence = noSequence;
		this.typeDocument = typeDocument;
		this.nameFile = nameFile;
		this.pathFile = pathFile;
		this.text = text;
		this.usernameCreated = usernameCreated;
		this.dateCreated = dateCreated;
		this.usernameLastModified = usernameLastModified;
		this.dateLastModified = dateLastModified;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTACHMENT_GEN")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "ID_TABLE", nullable = false, scale = 0)
	public BigDecimal getIdTable() {
		return this.idTable;
	}

	public void setIdTable(BigDecimal idTable) {
		this.idTable = idTable;
	}

	@Column(name = "NAME_TABLE", nullable = false, length = 60)
	public String getNameTable() {
		return this.nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	@Column(name = "NO_SEQUENCE", nullable = false, scale = 0)
	public BigDecimal getNoSequence() {
		return this.noSequence;
	}

	public void setNoSequence(BigDecimal noSequence) {
		this.noSequence = noSequence;
	}

	@Column(name = "TYPE_DOCUMENT", nullable = false, length = 20)
	public String getTypeDocument() {
		return this.typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	@Column(name = "NAME_FILE", length = 200)
	public String getNameFile() {
		return this.nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	@Column(name = "PATH_FILE", length = 1000)
	public String getPathFile() {
		return this.pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	@Column(name = "TEXT", length = 4000)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
