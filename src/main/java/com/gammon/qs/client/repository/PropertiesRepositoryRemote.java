/**
 * koeyyeung
 * Jun 6, 20134:19:32 PM
 * 
 * Henry Lai
 * modified on 17-Nov-2014
 */
package com.gammon.qs.client.repository;


import com.google.gwt.user.client.rpc.RemoteService;

public interface PropertiesRepositoryRemote extends RemoteService{
	public String obtainMailReceiverAddress();
	public Boolean updateMailReceiverAddress(String mailAddress);
}
