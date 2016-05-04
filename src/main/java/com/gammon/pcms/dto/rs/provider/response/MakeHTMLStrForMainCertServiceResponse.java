package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

/**
 * koeyyeung
 * Mar 20, 2015 3:24:44 PM
 */
public class MakeHTMLStrForMainCertServiceResponse implements Serializable{
	private static final long	serialVersionUID	= -2019050117615700361L;
	private String htmlStr;
 
	public MakeHTMLStrForMainCertServiceResponse() {
		
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}

	public String getHtmlStr() {
		return htmlStr;
	}

}
