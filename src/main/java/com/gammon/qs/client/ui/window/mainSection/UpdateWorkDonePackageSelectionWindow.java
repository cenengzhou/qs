package com.gammon.qs.client.ui.window.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.core.EventObject;

/**
 * @author Henry Lai
 * Created on 28-10-2014 
 */
public class UpdateWorkDonePackageSelectionWindow extends Window {
	
	private GlobalSectionController globalSectionController;
	private String packageNumber;
	
	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}

	public void setGlobalSectionController(
			GlobalSectionController globalSectionController) {
		this.globalSectionController = globalSectionController;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	TextField packageNumberTextField = new TextField();

	public UpdateWorkDonePackageSelectionWindow(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;
		setupUI();
	}
	
	private void setupUI() {
		setTitle("Select Package");
		setWidth(200);
		setHeight(135);
		setModal(true);
		setClosable(false);	
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("200px", "135px");
		packageNumberTextField.setWidth("100px");	
		packageNumberTextField.addListener(new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					select();
				}
			}
		});
				
		Button selectButton = new Button("Select");
		Button cencelButton = new Button("Cancel");

		selectButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				select();
			}
		});
		
		cencelButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalSectionController.closeCurrentWindow();
				globalSectionController.setCurrentWindow(null);
			}
		});
		
		selectButton.enable();
		cencelButton.enable();
		
		absolutePanel.add(new Label("Please enter the package number:") ,5, 15);
		absolutePanel.add(packageNumberTextField, 5, 40);
		absolutePanel.add(selectButton, 65, 70);
		absolutePanel.add(cencelButton, 125, 70);
		add(absolutePanel);
	}
	
	private void select(){
		packageNumber=packageNumberTextField.getText().trim();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getSCPackages(globalSectionController.getJob(), new AsyncCallback<List<SCPackage>>() {
			public void onSuccess(List<SCPackage> scPackages) {
				if(checkPackageExist(scPackages,packageNumber)){
					globalSectionController.setSelectedPackageNumber(packageNumber);
					globalSectionController.closeCurrentWindow();
					globalSectionController.setCurrentWindow(null);
					globalSectionController.showWorkDoneUpdateWindow("Cum WD Qty");
				}else
					MessageBox.alert("Invalid package number: "+packageNumber);
			}
			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				globalSectionController.closeCurrentWindow();
				globalSectionController.setCurrentWindow(null);
			}
		});
	}
	
	//Return true only when the package is found and it is an AWARDED Package
	private boolean checkPackageExist(List<SCPackage> scPackages, String packageNumber) {
		if (scPackages == null)
			return false;

		for (SCPackage scPackage : scPackages) {
			if("S".equals(scPackage.getPackageType())
				||(scPackage.getPackageNo()!=null && scPackage.getPackageNo().length()==4 && scPackage.getPackageNo().startsWith("1"))
				||(scPackage.getPackageNo()!=null && scPackage.getPackageNo().length()==4 && scPackage.getPackageNo().startsWith("2")) 
				||(scPackage.getPackageNo()!=null && scPackage.getPackageNo().length()==4 && scPackage.getPackageNo().startsWith("3"))){
				if (scPackage.isAwarded() && packageNumber.equals(scPackage.getPackageNo())){
					return true;
				}
			}
		}
		return false;
	}
	
}

