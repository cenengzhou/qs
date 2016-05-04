/**
 * koeyyeung
 * Jun 6, 20134:19:57 PM
 * 
 * Henry Lai
 * modified on 17-Nov-2014
 */
package com.gammon.qs.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PropertiesRepositoryRemoteAsync {
	public void obtainMailReceiverAddress(AsyncCallback<String> callback); 
	public void updateMailReceiverAddress(String mailAddress, AsyncCallback<Boolean> callback);
}
