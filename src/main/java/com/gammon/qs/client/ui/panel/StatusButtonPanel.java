package com.gammon.qs.client.ui.panel;

import java.util.ArrayList;

import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * @author matthewatc
 * Panel designed to display a left-aligned status message alongside a series of right-aligned buttons.
 * The width of each button must be specified in order to perform the kludge that emulates actual right-alignment
 * (if you know how to actually right-align a button in gwt-ext, please do modify this panel to use that method).
 * The content of the status message is automatically truncated to a specified number of characters,
 * and the full message is included in a tooltip(on the truncated message). Buttons can be hidden and shown 
 * independently, and the StatusButtonPanel will adjust accordingly. Will throw an exception if there is an attempt to
 * show or hide a button that has not been added.
 * 
 * IMPORTANT: In order for the panel to behave correctly when resized, the resize() method must be called whenever
 * the actual width of the panel is changed (i.e. by adding a WindowListenerAdapter to listen for an onResize event
 * firing on the parent window).
 */
public class StatusButtonPanel extends Panel {
	private Label statusLabel;
	private String truncationIndicator;
	private int buttonMargin = 0;
	private int maxStatusLength;
	private ToolTip statusTip = null;
	private ArrayList<ButtonInfo> buttons;
	
	/**
	 * @param maxStatusLength the maximum number of characters the status label (including truncation indicator characters) will be allowed to take
	 * @param truncationIndicator the string to append to the status label to indicate that its contents have been truncated
	 */
	public StatusButtonPanel(int maxStatusLength, String truncationIndicator) {
		super();
		this.maxStatusLength = maxStatusLength;
		this.truncationIndicator = truncationIndicator;
		
		this.setLayout(new HorizontalLayout(3));
		this.setPaddings(4);
		
		buttons = new ArrayList<ButtonInfo>();
		
		statusLabel = new Label();
		statusLabel.addClass("left-align");
		statusLabel.setWidth(this.getWidth());
		this.add(statusLabel);
	}
	
	public StatusButtonPanel(int maxStatusLength) {
		this(maxStatusLength, "...");
	}
	
	public StatusButtonPanel() {
		this(100);
	}
	
	/**
	 * add a button to the panel (to the right of the previously added button)
	 * @param button the Button to add
	 * @param width the width to allow for the button
	 * @param active true iff the button should be initially visible
	 */
	public void addButton(Button button, int width, boolean active) {
		ButtonInfo buttonInfo = new ButtonInfo(button, width, false);
		buttons.add(buttonInfo);
		this.add(buttonInfo.button);
		if(active) {
			showButton(buttonInfo.button);
		} else {
			buttonInfo.button.setVisible(false);
		}
	}
	
	public void addButton(Button button, int width) {
		addButton(button, width, true);
	}
	
	public void addButton(Button button) {
		addButton(button, 100);
	}
	
	/**
	 * this method must be called whenever the actual width of the panel is changed
	 */
	public void resize() {
		statusLabel.setWidth(this.getWidth() - buttonMargin);
	}
	
	private ButtonInfo getButtonInfo(Button button) throws IllegalArgumentException {
		ButtonInfo buttonInfo = null;
		for(ButtonInfo b : buttons) {
			if(b.button != null && b.button.equals(button)) {
				buttonInfo = b;
				break;
			}
		}
		if(buttonInfo == null) {
			throw new IllegalArgumentException("the specified Button has not been added to this StatusButtonPanel");
		}
		
		return buttonInfo;
	}
	
	/**
	 * hide the specified button
	 * @param button the button to hide; must have previously been added with addButton
	 * @throws IllegalArgumentException if the button has not been added via the addButton method
	 */
	public void hideButton(Button button) throws IllegalArgumentException {
		ButtonInfo buttonInfo = getButtonInfo(button);
		
		if(buttonInfo.active) {
			buttonInfo.button.setVisible(false);
			
			buttonMargin -= buttonInfo.width;
			
			resize();
			
			buttonInfo.active = false;
		}
	}
	
	/**
	 * show the specified button
	 * @param button the button to show
	 * @throws IllegalArgumentException if the button has not been added via the addButton method
	 */
	public void showButton(Button button) throws IllegalArgumentException {
		ButtonInfo buttonInfo = getButtonInfo(button);
		
		if(!buttonInfo.active) {
			buttonInfo.button.setVisible(true);
			
			buttonMargin += buttonInfo.width;
			
			resize();
			
			buttonInfo.active = true;
		}
	}
	
	/**
	 * display a status message to the left of the buttons 
	 * @param status the massage to display
	 */
	public void showStatus(String status) {
		String display;
		if(status.length() > maxStatusLength - truncationIndicator.length()) {
			display = status.substring(0, maxStatusLength + 1 - truncationIndicator.length()) + truncationIndicator;
		} else {
			display = status;
		}
		
		statusLabel.setText(display);
		
		if (statusTip != null) {
		   try {
			   statusTip.getBody().update(status);
		   } catch (Throwable e) {}
		} else {
			statusTip = new ToolTip();
			statusTip.setCtCls("toolbar-button");
			statusTip.setDismissDelay(15000);
			statusTip.setWidth(200);
			statusTip.setTrackMouse(true);
			statusTip.setHeader(true);
			statusTip.setHtml(status);
			statusTip.applyTo(statusLabel);
		}

	}
	
	public String getTruncationIndicator() {
		return truncationIndicator;
	}

	public void setTruncationIndicator(String truncationIndicator) {
		this.truncationIndicator = truncationIndicator;
	}

	public int getMaxStatusLength() {
		return maxStatusLength;
	}

	public void setMaxStatusLength(int maxStatusLength) {
		this.maxStatusLength = maxStatusLength;
	}
	
	private class ButtonInfo {
		public Button button;
		public int width;
		public boolean active;
		
		public ButtonInfo(Button button, int width, boolean active) {
			this.button = button;
			this.width = width;
			this.active = active;
		}
	}
	
}
