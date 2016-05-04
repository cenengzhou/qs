package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAttachmentListResponseList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	
	private ArrayList<GetAttachmentListResponse> attachmentList;

	public void setAttachmentList(ArrayList<GetAttachmentListResponse> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public ArrayList<GetAttachmentListResponse> getAttachmentList() {
		return attachmentList;
	}

}
