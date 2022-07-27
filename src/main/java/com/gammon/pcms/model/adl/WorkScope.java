package com.gammon.pcms.model.adl;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "C59A2_WORKSCOPE")
public class WorkScope implements java.io.Serializable {


	
	private static final long serialVersionUID = 8633173585520354066L;
	private String code;
	private String workScopeDesc;

	

	@Id
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	

	@Column(name = "WORKSCOPE_DESCRIPTION")
	public String getWorkScopeDesc() {
		return workScopeDesc;
	}
	public void setWorkScopeDesc(String workScopeDesc) {
		this.workScopeDesc = workScopeDesc;
	}
	
	

	@Override
	public String toString() {
		return "WorkScope [code=" + code + ", workScopeDesc=" + workScopeDesc + "]";
	}
	
	

}
