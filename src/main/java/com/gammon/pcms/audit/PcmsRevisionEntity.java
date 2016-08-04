package com.gammon.pcms.audit;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "REVISION")
@RevisionEntity(PcmsRevisionListener.class)
@SequenceGenerator(name = "REVISION_GEN", sequenceName = "REVISION_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
public class PcmsRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = 3216754429831450361L;
	
	private String username;
	private Set<String> modifiedEntityNames = new HashSet<String>();
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVISION_GEN")
	public int getId(){return super.getId();}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the modifiedEntityNames
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "REVCHANGES", joinColumns = @JoinColumn(name = "REV"))
	@Column(name = "ENTITYNAME")
	@Fetch(FetchMode.JOIN)
	@ModifiedEntityNames
	public Set<String> getModifiedEntityNames() {
		return modifiedEntityNames;
	}
	/**
	 * @param modifiedEntityNames the modifiedEntityNames to set
	 */
	public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
		this.modifiedEntityNames = modifiedEntityNames;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((modifiedEntityNames == null) ? 0 : modifiedEntityNames.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PcmsRevisionEntity other = (PcmsRevisionEntity) obj;
		if (modifiedEntityNames == null) {
			if (other.modifiedEntityNames != null)
				return false;
		} else if (!modifiedEntityNames.equals(other.modifiedEntityNames))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PcmsRevisionEntity [username=" + username + ", modifiedEntityNames=" + modifiedEntityNames
				+ ", toString()=" + super.toString() + "]";
	}
	
}
