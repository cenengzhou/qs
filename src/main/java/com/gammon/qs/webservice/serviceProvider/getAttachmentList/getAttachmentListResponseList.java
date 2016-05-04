package com.gammon.qs.webservice.serviceProvider.getAttachmentList;

import java.io.Serializable;
import java.util.ArrayList;

public class getAttachmentListResponseList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	
	private ArrayList<getAttachmentListResponse> attachmentList;

	public void setAttachmentList(ArrayList<getAttachmentListResponse> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public ArrayList<getAttachmentListResponse> getAttachmentList() {
		return attachmentList;
	}

}
