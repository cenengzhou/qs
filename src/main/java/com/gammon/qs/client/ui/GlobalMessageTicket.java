package com.gammon.qs.client.ui;

import com.gammon.qs.client.repository.SystemMessageRepositoryRemote;
import com.gammon.qs.client.repository.SystemMessageRepositoryRemoteAsync;
import com.gammon.qs.client.ui.util.UIUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;

public class GlobalMessageTicket extends HTML {
	
	private SystemMessageRepositoryRemoteAsync globalMessageRepository;
	
	public GlobalMessageTicket(){
		super();
		
		globalMessageRepository = (SystemMessageRepositoryRemoteAsync)GWT.create(SystemMessageRepositoryRemote.class);
		((ServiceDefTarget)globalMessageRepository).setServiceEntryPoint(GWT.getModuleBaseURL()+ "systemMessage.smvc");
	}
	
	
	/**
	 * Get the global message from the server side and display/refresh the marquee
	 */
	public void refresh(){
		
		globalMessageRepository.getGlobalAlertMessage(new AsyncCallback<String> (){
			
			public void onSuccess(String result){
				
				GlobalMessageTicket.this.display(result);
			}
			
			public void onFailure(Throwable e) {
				globalMessageRepository.getGlobalAlertMessage(new AsyncCallback<String> (){

					public void onFailure(Throwable e1) {
						UIUtil.checkSessionTimeout(e1, false,null,"GlobalMessageTicket.refresh()");
					}

					public void onSuccess(String result) {
						GlobalMessageTicket.this.display(result);
						
					}
				});
			}
			
		});
	}
	
	

	/**
	 * Display the marquee with the supplied text
	 * @param text
	 */
	public void display(String text){
		
		if(text==null ||"".equals(text.trim()))
			hidePanel();
		else
			populatePanel(text);
	}
	
	/**
	 * To Hide the marquee
	 */
	public void clear(){
		super.setHTML("");
		hidePanel();
	}
	
	public native void populatePanel(String message)/*-{
		
		$wnd.msg = message;
		var msgElement1 = $doc.getElementById('iemarquee');
		var msgElement2 = $doc.getElementById('temp');		
		msgElement1.innerHTML= '<nobr><font face="Arial" color="#FFFFFF" size="3">'+message+'</font></nobr>';
		msgElement2.innerHTML= '<nobr><font face="Arial" color="#FFFFFF" size="3">'+message+'</font></nobr>';
				
		
		if($doc.getElementById("glideDiv")!=null)
			$doc.getElementById("glideDiv").style.visibility="visible";
        
        if($doc.getElementById("relativePos")!=null)
        	$doc.getElementById("relativePos").style.visibility="visible";
			
	}-*/;
	
	public native void hidePanel()/*-{
	
	if($doc.getElementById("glideDiv")!=null)
		$doc.getElementById("glideDiv").style.visibility="hidden";
    
    if($doc.getElementById("relativePos")!=null)
    	$doc.getElementById("relativePos").style.visibility="hidden";
		
}-*/;
	
	
	
	

}
