package com.gammon.qs.client.ui;

import com.gammon.qs.application.User;
import com.gammon.qs.client.repository.UserServiceRemote;
import com.gammon.qs.client.repository.UserServiceRemoteAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class SessionTimeoutCheck {
	private static com.gwtext.client.widgets.Window alertWindow;
	private final static String SESSION_TIMEOUT_WINDOW_TITLE	= "Session timeout alert";
	private final static String SESSION_TIMEOUT_RELOGIN_TEXT 	= "Session timeout, please click below button to login again.";
	private final static String SESSION_TIMEOUT_CONTINUE_TEXT 	= "Session is about to timeout, do you want to continue?";
	private final static String STYLE_CONTINUE_BUTTON 			= "position:absolute; top:50%;left:140px";
	private final static String STYLE_RELOGIN_BUTTON 			= "position:absolute; top:50%;left:160px";
	private final static String STYLE_CONTINUE_LABEL			= "position:absolute; top:35%;left:65px";
	private final static int 	SYNC_BUFFER 					= 1  * (60 * 1000);
	private final static int 	SHOW_REMAIN_TIME 				= 5  * (60 * 1000);
	private final static int 	SESSION_TIMEOUT 				= 90 * (60 * 1000) - SYNC_BUFFER;
	
	private static Timer sessionTimer = new Timer(){
		@Override
		public void run() {
			alertWindow = new SessionTimeoutWindow();
			alertWindow.show();
		}
	};
	
	public static void renewSessionTimer(){
		sessionTimer.cancel();
		sessionTimer.schedule( SESSION_TIMEOUT - SHOW_REMAIN_TIME);
	}
	
	private static class SessionTimeoutWindow extends com.gwtext.client.widgets.Window {
		private Label label;
		private Button button;
		private Panel panel;
		private Timer countDown;
		private long currentRemainTime;
		private boolean timeout;
		private UserServiceRemoteAsync userService;
		
		public SessionTimeoutWindow(){
			this.setSize(400, 150);
			this.setClosable(false);
			this.setResizable(false);
			this.setTitle(SESSION_TIMEOUT_WINDOW_TITLE);
			
			userService = (UserServiceRemoteAsync)GWT.create(UserServiceRemote.class);
			((ServiceDefTarget)userService).setServiceEntryPoint(GWT.getModuleBaseURL()+ "user.smvc");
			
			setupUI();			
		}

		private void setupUI() {
			this.clear();
			this.setModal(true);
			this.setLayout(new FitLayout());
			panel = new Panel();
			panel.setLayout(new VerticalLayout());
			
			label = new Label(SESSION_TIMEOUT_CONTINUE_TEXT);
			label.setStyle(STYLE_CONTINUE_LABEL);	
			button = new Button();
			button.setStyle(STYLE_CONTINUE_BUTTON);
			button.addListener(buttonListener());
			panel.add(label);
			panel.add(button);
			this.add(panel);
		}

		@Override
		public void show() {
			setupUI();
			countDown = countDownTimer();
			currentRemainTime = SHOW_REMAIN_TIME;
			countDown = countDownTimer();
			countDown.schedule(1000);
			setButtonText();
			super.show();
		}
		
		private Timer countDownTimer(){
			return new Timer(){
				@Override
				public void run() {
					currentRemainTime -= 1000;
					if(currentRemainTime <= 0) timeout = true;
					setButtonText();
				}
			};
		}
		
		private void setButtonText() {
			if(timeout){
				label.setText(SESSION_TIMEOUT_RELOGIN_TEXT);
				button.setStyle(STYLE_RELOGIN_BUTTON);
				button.setText("Login");
				countDown.cancel();
			} else {
				button.setText("Continue (" + msToString(currentRemainTime) +")");
				countDown.schedule(1000);
			}	
		}
		private String msToString(long ms){
			if(ms <= 0) {
				return "00:00";
			}else{
				int sec = (int) (ms / 1000 % 60);
				int min = (int) (ms / 1000 / 60);
				return fillZero(min,2,"0") + ":" + fillZero(sec,2,"0");
			}
		}
		
		private String fillZero(int num, int length, String fill){
			String result = String.valueOf(num);
			while(result.length() < length){
				result = fill + result;
			}
			return result;
		}
		
		private ButtonListenerAdapter buttonListener(){
			return new ButtonListenerAdapter(){
				public void onClick(Button button, EventObject e){
					alertWindow.close();
					countDown.cancel();
					if(timeout){
						Window.Location.assign(Window.Location.getHref());
					}else{
						userService.getUserDetails(new AsyncCallback<User>(){
							@Override
							public void onFailure(Throwable caught) {}

							@Override
							public void onSuccess(User result) {}
							
						});
					}
					SessionTimeoutCheck.renewSessionTimer();
				}
			};
		}
	}
}
