package com.gammon.pcms.dto.rs.provider.response;

import java.io.Serializable;

public class MakeHTMLStrForPaymentServiceResponse  implements Serializable{
	private static final long serialVersionUID = -8647917545694726740L;
	private String htmlStr;

	public MakeHTMLStrForPaymentServiceResponse(){
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
