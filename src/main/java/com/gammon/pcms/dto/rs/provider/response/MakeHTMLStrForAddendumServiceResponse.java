package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class MakeHTMLStrForAddendumServiceResponse  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	private String htmlStr;

	public MakeHTMLStrForAddendumServiceResponse(){
		
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
