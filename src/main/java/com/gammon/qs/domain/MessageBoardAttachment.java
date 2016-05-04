package com.gammon.qs.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import com.gammon.qs.application.BasePersistedObject;

@Entity
@Table(name = "QS_MSG_BOARD_ATTACHMENT")
@SequenceGenerator(name = "qs_msg_attach_gen",  sequenceName = "qs_msg_attach_seq", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 10, scale = 0))
@OptimisticLocking(type = OptimisticLockType.NONE)
public class MessageBoardAttachment extends BasePersistedObject{

	/**
	 * koeyyeung
	 * Feb 7, 2014 10:49:14 AM
	 */
	private static final long serialVersionUID = 6290361454572728023L;

	public static final String IMAGE_DOC_TYPE = "IMG";
	
	private MessageBoard messageBoard;
	private Integer sequenceNo;
	private String filename;
	private String docType;
	
	public MessageBoardAttachment() {
	}
	
	@Override
	public String toString() {
		return "MessageBoardAttachment [messageBoard=" + messageBoard + ", sequenceNo=" + sequenceNo + ", filename="
				+ filename + ", docType=" + docType + ", toString()=" + super.toString() + "]";
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_msg_attach_gen")
	public Long getId(){return super.getId();}
	
	@Column(name = "SEQUENCE_NO")
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	@Column(name = "FILENAME", length = 100)
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "DOC_TYPE", length = 10)
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	@ManyToOne
	@JoinColumn(name = "message_board_id", foreignKey = @ForeignKey(name = "FK_msgboard_attachment"))
	public MessageBoard getMessageBoard() {
		return messageBoard;
	}
	public void setMessageBoard(MessageBoard messageBoard) {
		this.messageBoard = messageBoard;
	}
}
