package com.gammon.qs.client.ui.util;


import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gammon.qs.application.User;
import com.gammon.qs.client.repository.UIErrorMessageLogRemote;
import com.gammon.qs.client.repository.UIErrorMessageLogRemoteAsync;
import com.gammon.qs.client.ui.exception.JDEErrorException;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;

public class UIUtil {
	private static Logger logger = Logger.getLogger(UIUtil.class.getSimpleName());
	
	public static void throwException(Throwable e){
		if(e instanceof com.google.gwt.user.client.rpc.InvocationException){
			UIUtil.alert("Session Timeout.");
			Window.Location.assign(GWT.getHostPageBaseURL()+"index.htm");
		}		
		else if (e instanceof Exception){
			if (e.getMessage().contains("Database Operation Exception:")){
				MessageBox.alert(	"Database Operation Exception",
									e.getMessage().substring(e.getMessage().indexOf("Database Operation Exception:")+"Database Operation Exception:".length()));			
			}else if (e.toString().contains ("Validation Exception:")){
				MessageBox.alert(	"Validate Exception",
									e.getMessage().substring(e.getMessage().indexOf("Validation Exception:")+"Validation Exception:".length()));			
			}
			else
				UIUtil.alert(e);		
		}
		else
			UIUtil.alert(e);		
	}
	
	public static void checkSessionTimeout(Throwable e, boolean isAlterException, User user){
		checkSessionTimeout(e, isAlterException, user, "");
	}
	
	public static void checkSessionTimeout(Throwable e, boolean isAlterException, User user, String errorLocat) {
		String userName = "--No USER--";
		String errorLocation = "";
		if (!"".equals(errorLocat))
			errorLocation = "@ " + errorLocat;
		if (user != null && user.getUsername() != null)
			userName = user.getUsername().trim();
		if (e instanceof com.google.gwt.user.client.rpc.InvocationException) {
			UIErrorMessageLogRemoteAsync uiErrorMeessageLog = (UIErrorMessageLogRemoteAsync) GWT.create(UIErrorMessageLogRemote.class);
			((ServiceDefTarget) uiErrorMeessageLog).setServiceEntryPoint(GlobalParameter.UI_ERROR_MESSAGE_LOG_URL);
			String errorMsg = "User:" + userName + errorLocation + " UI Session Timeout Message Prompt at " + DateUtil.formatDate(new Date(), GlobalParameter.DATETIME_FORMAT) + " **Exception:";
			if (e.getMessage().contains("To begin, please enter your Windows username and password."))
				errorMsg += " Redirected to login page.";
			else
				errorMsg += e.getMessage();
			
			logger.log(Level.SEVERE, "checkSessionTimeout:"+errorMsg);
			uiErrorMeessageLog.logError(errorMsg, e, new AsyncCallback<Boolean>() {

				public void onFailure(Throwable arg0) {
					UIUtil.alert("Session Timeout. Log failure.");
					Window.Location.assign(GWT.getHostPageBaseURL() + "index.htm");
				}

				public void onSuccess(Boolean arg0) {
					UIUtil.alert("Session Timeout.");
					Window.Location.assign(GWT.getHostPageBaseURL() + "index.htm");
				}

			});

		}

		else if (e instanceof Exception) {
			if (e.getMessage().contains("DatabaseOperationException:")) {
				MessageBox.alert("DatabaseOperationException",
						e.getMessage().substring(
								e.getMessage().indexOf("DatabaseOperationException:") + "DatabaseOperationException:".length()));
			} else if (e.toString().contains("ValidateBusinessLogicException:")) {
				MessageBox.alert("Validate Exception",
						e.getMessage().substring(
								e.getMessage().indexOf("Validation Exception:") + "Validation Exception:".length()));
			} else if (e.getMessage().contains("JDEErrorException")) {
				String errorString = e.getMessage();
				String errorCode = "";

				if (errorString != null) {
					errorCode = errorString.substring(errorString.indexOf(JDEErrorException.ERROR_CODE_STR));
				}

				UIUtil.alert("JDE Exception occured! Please contact your administrator.\n" + errorCode);

			} else {
				UIUtil.alert(e);
			}

		} else if (isAlterException) {
			UIUtil.alert(e);
		}

	}
	
	public static void alert(String msg) {
		Window.alert(msg);
	}
	
	public static void alert(int msg) {
		Window.alert(new Integer(msg).toString());
	}
	
	public static void alert(Object msg) {
		UIErrorMessageLogRemoteAsync uiErrorMeessageLog = (UIErrorMessageLogRemoteAsync) GWT.create(UIErrorMessageLogRemote.class);
		((ServiceDefTarget) uiErrorMeessageLog).setServiceEntryPoint(GlobalParameter.UI_ERROR_MESSAGE_LOG_URL);
		uiErrorMeessageLog.logError("Window Alert Message:" + msg, new Exception(), new AsyncCallback<Boolean>() {

			public void onFailure(Throwable arg0) {

			}

			public void onSuccess(Boolean arg0) {

			}
		});
		Window.alert(msg.toString());
	}

	public static void alert(double msg) {
		Window.alert(new Double(msg).toString());
	}

	public static void showMainLayout() {
		Panel mainLayout = (Panel) Ext.getCmp("main-layout");
		mainLayout.show();
		mainLayout.doLayout();
	}

	public static void hideMainLayout() {
		Panel mainLayout = (Panel) Ext.getCmp("main-layout");
		mainLayout.hide();
	}

	public static void maskMainPanel() {
		Panel mainPanel = (Panel) Ext.getCmp(GlobalParameter.MAIN_PANEL_ID);
		mainPanel.getEl().mask();
	}

	public static void maskMainPanel(String msg) {
		Panel mainPanel = (Panel) Ext.getCmp(GlobalParameter.MAIN_PANEL_ID);
		mainPanel.getEl().mask(msg);
	}

	public static void unmaskMainPanel() {
		Panel mainPanel = (Panel) Ext.getCmp(GlobalParameter.MAIN_PANEL_ID);
		mainPanel.getEl().unmask();
	}

	public static void maskPanelById(String id, String msg, boolean annimatedIcon) {
		Panel panel = (Panel) Ext.getCmp(id);
		if (panel != null)
			panel.getEl().mask(msg, annimatedIcon);

	}

	public static void unmaskPanelById(String id) {
		Panel panel = (Panel) Ext.getCmp(id);
		if (panel != null)
			panel.getEl().unmask();
	}

}
