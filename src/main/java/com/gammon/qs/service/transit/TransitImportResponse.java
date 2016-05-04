package com.gammon.qs.service.transit;

public class TransitImportResponse {
	private int numRecordImported = 0;
	private boolean success = false;
	private String message;
	
	// added by brian on 20110224
	private boolean haveWarning = false;
	
	public TransitImportResponse() {
		
	}
	
	public int getNumRecordImported() {
		return this.numRecordImported;
	}

	public void setNumRecordImported(int numRecordImported) {
		this.numRecordImported = numRecordImported;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// added by brian on 20110224
	public boolean isHaveWarning() {
		return haveWarning;
	}

	// added by brian on 20110224
	public void setHaveWarning(boolean haveWarning) {
		this.haveWarning = haveWarning;
	}
	
	
}
