/**
 * 
 */
package com.gammon.qs.wrapper.currencyCode;

import java.io.Serializable;

/**
 * @author briantse
 * @Create_Date Mar 22, 2011
 * @Purpose Get the currency code from JDE web services
 */
public class CurrencyCodeWrapper implements Serializable {
		
	private static final long serialVersionUID = 980728994113923324L;

	private String currencyCode;
	
	private String currencyDescription;

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyDescription() {
		return currencyDescription;
	}

	public void setCurrencyDescription(String currencyDescription) {
		this.currencyDescription = currencyDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
