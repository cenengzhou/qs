/**
 * 
 */
package com.gammon.qs.client.ui.window.mainSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.PowerUserAdminPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;

/**
 * modified by matthewlam, 2015-01-21
 * Bug Fix #79 revamp of Power User Administration page
 */
public class ResetStatusPanel extends Panel {
	private ComboBox						newStatusComboBox;
	public static final String				PANEL_ID	= "ResetStatusPanel";
	private static AsyncCallback<Boolean>	resetStatusCallback;
	private final Label						currentStatusValue;

	/**
	 * Creates the panel for reseting statuses, it should be used to populate the detail section via the DetailSectionController
	 * 
	 * @param detailSectionController
	 *        the global detailSectionController,
	 *        it is passed to the panel for "Cancel" button to reset the detail section
	 * @param action
	 *        the string for identifying what should be done when the Save button is pressed, also acts as the title
	 * @param choices
	 *        list of choices to be chosen in the combo box
	 * @param jobNumber
	 * @param packageNumber
	 * @param paymentNumber
	 * @param mainCertNumber
	 * @param currentValue
	 *        the current status of corresponding enquiry
	 */
	public ResetStatusPanel(final GlobalSectionController globalSectionController,
							final String action,
							String[][] choices,
							final String jobNumber,
							final String packageNumber,
							final String paymentNumber,
							final String mainCertNumber,
							String currentValue) {
		super();
		setId(PANEL_ID);
		setLayout(new FitLayout());
		setFrame(true);
		setBorder(false);
		setPaddings(10);

		Panel basePanel = new Panel();
		basePanel.setFrame(true);
		basePanel.setBorder(false);
		basePanel.setLayout(new RowLayout());

		Panel upperPanel = new FormPanel();
		upperPanel.setLayout(new HorizontalLayout(10));
		upperPanel.setBorder(false);

		Panel jobNumberPanel = new Panel();
		jobNumberPanel.setPaddings(10);
		jobNumberPanel.setLayout(new HorizontalLayout(10));
		jobNumberPanel.setBorder(false);

		Label jobNumberLabel = new Label("Job No.");
		jobNumberLabel.setStyle("font-weight:bold");
		jobNumberLabel.setWidth(100);
		jobNumberLabel.setCtCls("table-cell");
		jobNumberPanel.add(jobNumberLabel);

		Label jobNumberField = new Label();
		jobNumberField.setWidth(80);
		jobNumberField.setText(jobNumber);
		jobNumberField.setCtCls("table-cell");
		jobNumberPanel.add(jobNumberField);
		upperPanel.add(jobNumberPanel);

		if (packageNumber != null && !packageNumber.isEmpty()) {
			Panel packageNoPanel = new Panel();
			packageNoPanel.setPaddings(10);
			packageNoPanel.setBorder(false);

			Label packageNoLabel = new Label("Package No.");
			packageNoLabel.setStyle("font-weight:bold");
			packageNoLabel.setCtCls("table-cell");
			packageNoLabel.setWidth(100);
			packageNoPanel.add(packageNoLabel);

			Label packageNumberValue = new Label();
			packageNumberValue.setCtCls("table-cell");
			packageNumberValue.setWidth(80);
			packageNumberValue.setText(packageNumber);
			packageNoPanel.add(packageNumberValue);

			upperPanel.add(packageNoPanel);
		}

		if (mainCertNumber != null && !mainCertNumber.isEmpty()) {
			Panel mainCertNoPanel = new Panel();
			mainCertNoPanel.setPaddings(10);
			mainCertNoPanel.setBorder(false);

			Label mainCertNoLabel = new Label("Main Contract Certificate");
			mainCertNoLabel.setStyle("font-weight:bold");
			mainCertNoLabel.setCtCls("table-cell");
			mainCertNoLabel.setWidth(180);
			mainCertNoPanel.add(mainCertNoLabel);

			Label mainCertNumberValue = new Label();
			mainCertNumberValue.setCtCls("table-cell");
			mainCertNumberValue.setWidth(80);
			mainCertNumberValue.setText(mainCertNumber);
			mainCertNoPanel.add(mainCertNumberValue);

			upperPanel.add(mainCertNoPanel);
		}

		if (paymentNumber != null && !"".equals(paymentNumber.trim())) {
			Panel paymentNumberPanel = new Panel();
			paymentNumberPanel.setPaddings(10);
			paymentNumberPanel.setBorder(false);
			Label paymentNoLabel = new Label("Payment No.");
			paymentNoLabel.setStyle("font-weight:bold");
			paymentNoLabel.setCtCls("table-cell");
			paymentNoLabel.setWidth(100);
			paymentNumberPanel.add(paymentNoLabel);

			Label paymentNumberValue = new Label();
			paymentNumberValue.setWidth(40);
			paymentNumberValue.setCtCls("table-cell");

			paymentNumberValue.setText(paymentNumber);
			paymentNumberPanel.setVisible(true);
			paymentNumberPanel.add(paymentNumberValue);
			upperPanel.add(paymentNumberPanel);
		}

		basePanel.add(upperPanel);

		Panel bottomPanel = new Panel();
		bottomPanel.setBorder(false);
		bottomPanel.setLayout(new HorizontalLayout(10));

		Panel currentStatusPanel = new Panel();
		currentStatusPanel.setBorder(false);

		Label currentStatusLabel = new Label("Current Status");
		currentStatusLabel.setStyle("font-weight:bold");
		currentStatusLabel.setCtCls("table-cell");
		currentStatusLabel.setWidth(120);
		currentStatusPanel.add(currentStatusLabel);

		currentStatusValue = new Label();
		currentStatusValue.setCtCls("table-cell");
		currentStatusValue.setText(currentValue);
		currentStatusValue.setWidth(80);
		currentStatusPanel.add(currentStatusValue);
		bottomPanel.add(currentStatusPanel);

		Panel newStatusPanel = new Panel();
		newStatusPanel.setLayout(new HorizontalLayout(10));
		newStatusPanel.setPaddings(10);
		newStatusPanel.setBorder(false);

		Label newStatusLabel = new Label("New Status");
		newStatusLabel.setStyle("font-weight:bold");
		newStatusLabel.setCls("table-cell");
		newStatusLabel.setWidth(160);
		newStatusPanel.add(newStatusLabel);

		Store store = new SimpleStore(new String[] { "status", "Description" }, desc2StatusAndDesc(choices));
		newStatusComboBox = new ComboBox();

		newStatusComboBox.setStore(store);
		newStatusComboBox.setDisplayField("Description");
		newStatusComboBox.setValueField("status");
		newStatusComboBox.setMode(ComboBox.REMOTE);
		newStatusComboBox.setValue(currentValue);
		newStatusComboBox.setCtCls("table-cell");
		newStatusComboBox.setWidth(250);
		newStatusPanel.add(newStatusComboBox);
		bottomPanel.add(newStatusPanel);

		basePanel.add(bottomPanel);
		add(basePanel);
		SessionTimeoutCheck.renewSessionTimer();
		resetStatusCallback = new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean success) {
				MessageBox.alert("Update " + (success ? "Succeeded" : "Failed"));
				updateCurrentStatusValue(newStatusComboBox.getValueAsString());
				UIUtil.unmaskPanelById(PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
				UIUtil.unmaskMainPanel();
			}
		};

		Toolbar toolbar = new Toolbar();
		ToolbarButton saveButton = new ToolbarButton("Save", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				String newStatus = newStatusComboBox.getValue();

				UIUtil.maskPanelById(PANEL_ID, "Updating", true);

				if (PowerUserAdminPanel.ACTION_RESET_SUBCONTRACT_AWARDS.equals(action)) {
					globalSectionController.getPackageRepository().updateSCStatus(
							jobNumber,
								packageNumber,
								newStatus,
								resetStatusCallback);
				} else if (PowerUserAdminPanel.ACTION_RESET_ADDENDUM.equals(action)) {
					globalSectionController.getPackageRepository().updateAddendumStatus(
							jobNumber,
								packageNumber,
								newStatus,
								resetStatusCallback);
				} else if (PowerUserAdminPanel.ACTION_RESET_SPLIT.equals(action)) {
					globalSectionController.getPackageRepository().updateSCSplitTerminateStatus(
							jobNumber,
								packageNumber,
								newStatus,
								resetStatusCallback);
				} else if (PowerUserAdminPanel.ACTION_RESET_SUBCONTRACT_PAYMENT.equals(action)) {
					globalSectionController.getPaymentRepository().updateSCPaymentCertStatus(
							jobNumber,
								packageNumber,
								paymentNumber,
								newStatus,
								resetStatusCallback);
				} else if (PowerUserAdminPanel.ACTION_RESET_FINAL_PAYMENT.equals(action)) {
					globalSectionController.getPackageRepository().updateSCFinalPaymentStatus(
							jobNumber,
								packageNumber,
								newStatus,
								resetStatusCallback);
				} else if (PowerUserAdminPanel.ACTION_RESET_MAIN_CONTRACT_CERT.equals(action)) {
					globalSectionController.getMainContractCertificateRepository().updateMainCertificateStatus(
							jobNumber,
								Integer.parseInt(mainCertNumber),
								newStatus,
								resetStatusCallback);
				}

				UIUtil.unmaskPanelById(PANEL_ID);
			}
		});
		saveButton.setIconCls("save-button-icon");
		;

		ToolbarButton cancelButton = new ToolbarButton("Cancel", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				globalSectionController.getDetailSectionController().resetPanel();
			}
		});
		cancelButton.setIconCls("cancel-icon");

		toolbar.addButton(saveButton);
		toolbar.addSeparator();
		toolbar.addButton(cancelButton);
		toolbar.addSeparator();
		this.setTopToolbar(toolbar);

	}

	private String[][] desc2StatusAndDesc(String[][] descList) {
		String resultStr[][] = new String[descList.length][];

		for (int i = 0; i < descList.length; i++)
			resultStr[i] = new String[] { descList[i][0], descList[i][0] + " - " + descList[i][1] };

		return resultStr;
	}

	private void updateCurrentStatusValue(String status) {
		currentStatusValue.setText(status);
	}
}
