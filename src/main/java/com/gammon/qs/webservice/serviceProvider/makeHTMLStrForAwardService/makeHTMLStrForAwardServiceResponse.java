package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService;

import java.io.Serializable;

public class makeHTMLStrForAwardServiceResponse  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	private String htmlStr;

	public makeHTMLStrForAwardServiceResponse(){
		
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
