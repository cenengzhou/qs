package com.gammon.pcms.dto.rs.provider.response.ap;

import java.io.Serializable;
import java.util.List;

public class GetAttachmentListResponseList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	
	private List<GetAttachmentListResponse> attachmentList;

	public void setAttachmentList(List<GetAttachmentListResponse> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<GetAttachmentListResponse> getAttachmentList() {
		return attachmentList;
	}

}
