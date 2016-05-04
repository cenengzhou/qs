package com.gammon.qs.service.scDetails;

public class UploadSCDetailByExcelResponse {

	private int numRecordImported = 0;
	private boolean success = false;
	private String message;
	
	public UploadSCDetailByExcelResponse(){
		
	}
	
	
	public int getNumRecordImported() {
		return numRecordImported;
	}
	public void setNumRecordImported(int numRecordImported) {
		this.numRecordImported = numRecordImported;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
