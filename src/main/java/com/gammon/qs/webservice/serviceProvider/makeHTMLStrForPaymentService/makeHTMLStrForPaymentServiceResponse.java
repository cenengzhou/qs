package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentService;

import java.io.Serializable;

public class makeHTMLStrForPaymentServiceResponse  implements Serializable{
	private static final long serialVersionUID = -8647917545694726740L;
	private String htmlStr;

	public makeHTMLStrForPaymentServiceResponse(){
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
