package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAddendumService;

import java.io.Serializable;

public class makeHTMLStrForAddendumServiceResponse  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8647917545694726740L;
	private String htmlStr;

	public makeHTMLStrForAddendumServiceResponse(){
		
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
