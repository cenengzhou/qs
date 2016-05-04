package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class MakeHTMLStrForPaymentCertServiceResponse  implements Serializable{
	
	/**
	 * koeyyeung
	 * Mar 13, 2014 9:15:20 AM
	 */
	private static final long serialVersionUID = -4213627873476777382L;
	private String htmlStr;

	public MakeHTMLStrForPaymentCertServiceResponse(){
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
