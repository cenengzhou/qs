package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.DomQuery;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * modified by matthewlam, 2015-01-21
 * Bug Fix #79 revamp of Power user Administration functions
 */
public class PowerUserAdminPanel extends TabPanel {
	private final MessageBox.AlertCallback	alertCallback;
	private static final String[][]			CHOICES_SUBCONTRACT_AWARDS				= new String[][] { 
																					{ "100", "SC Created" },
																					{ "160", "TA Analysis Ready" },
																					{ "330", "Award Request Submitted" },
																					{ "500", "SC Awarded" }	};
	
	private static final String[][]			CHOICES_ADDENDUM						= new String[][] {
																					{ SCPackage.ADDENDUM_SUBMITTED, "Submitted" },
																					{ SCPackage.ADDENDUM_NOT_SUBMITTED, "Not Submitted" }};
	
	private static final String[][]			CHOICES_SPLIT_OR_TERMINATE_SUBCONTRACT	= new String[][] { 
																					{ "0", "Not Submitted" },
																					{ "1", "Split SC Submitted" },
																					{ "2", "Terminate SC Submitted" },
																					{ "3", "Split Approved" },
																					{ "4", "Terminate Approved" },
																					{ "5", "Split Rejected" },
																					{ "6", "Terminate Rejected" } };
	
	private static final String[][]			CHOICES_SUBCONTRACT_PAYMENT				= new String[][] {
																					{ "PND", "Pending" },
																					{ "SBM", "Submitted" },
																					{ "UFR", "Under Review by Finance" },
																					{ "PCS", "AP Not Created" },
																					{ "APR", "AP Created" }};
	
	private static final String[][]			CHOICES_FINAL_PAYMENT					= new String[][]{
																					{"N","Not Submitted"},
																					{"D", "Payment Requisition"}, 
																					{"I","Interim Payment"},
																					{"F","Final Payment"}};

	public static final String				ACTION_UPDATE_SUBCONTRACT_PACKAGE		= "Update Subcontract Package";
	public static final String				ACTION_RESET_SUBCONTRACT_AWARDS			= "Reset Subcontract Awards Status";
	public static final String				ACTION_RESET_ADDENDUM					= "Reset Addendum Status";
	public static final String				ACTION_RESET_SPLIT						= "Reset Split/Terminate Subcontract Status";
	public static final String				ACTION_RESET_SUBCONTRACT_PAYMENT		= "Reset Subcontract Payment Status";
	public static final String				ACTION_RESET_FINAL_PAYMENT				= "Reset Final Payment Status";
	public static final String				ACTION_UPDATE_SUBCONTRACT_PAYMENT_CERT	= "Update Subcontract Payment Certificate";
	public static final String				ACTION_RESET_MAIN_CONTRACT_CERT			= "Reset Main Contract Certificate Status";
	public static final String				ACTION_UPDATE_PACKAGE_POSTED_CERT_AMOUNT= "Recalculate Package Posted Cert Amount";

	private String							action;

	private GlobalSectionController globalSectionController;
	private String jobNumber;
	private String packageNumber;
	
	public PowerUserAdminPanel(final GlobalSectionController globalSectionController) {
		super();
		alertCallback = new MessageBox.AlertCallback() {
			public void execute() {
				UIUtil.unmaskMainPanel();
			}
		};

		this.globalSectionController = globalSectionController;
		
		this.setTabPosition(Position.TOP);
		this.setResizeTabs(true);
		this.setTabWidth(180);
		this.setDeferredRender(false); // might be removed
		this.setPaddings(0);

		this.add(createSubcontractPanel());
		this.add(createPaymentPanel());
		this.add(createMainContractCertificatePanel());

		ToolbarButton nextButton = new ToolbarButton("Next", new ButtonListenerAdapter() {
			public void onClick(final Button button, EventObject e) {
				if (action == null) {
					MessageBox.alert("Please choose an action");
					return;
				}

				String panelId = getActiveTab().getId();
				Element jobElement = DOM.getElementById(panelId + "-jobNum");
				Element packageElement = DOM.getElementById(panelId + "-packageNum");
				Element paymentElement = DOM.getElementById(panelId + "-paymentNum");
				Element mainContractCertificateElement = DOM.getElementById(panelId + "-mainContractCertificateNum");

				jobNumber = jobElement.getAttribute("value").trim();

				if ("subcontractPanel".equals(panelId)) {
					packageNumber = packageElement.getAttribute("value").trim();

					if (jobNumber.isEmpty() || packageNumber.isEmpty()) {
						MessageBox.alert("Please enter Job No. and Package No.");
						return;
					}
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPackageRepository().getSCPackage(jobNumber,
							packageNumber,
							new AsyncCallback<SCPackage>() {
								String[][]	choices;
								String		currentValue;

								public void onSuccess(SCPackage scPackage) {
									if (scPackage == null) {
										MessageBox.alert("Error", "The subcontract does not exist.", alertCallback);
										return;
									}

									if (ACTION_UPDATE_SUBCONTRACT_PACKAGE.equals(action)) {
										globalSectionController.showUpdateSCPackageWindows(scPackage);
										return;
									} else if (ACTION_RESET_SUBCONTRACT_AWARDS.equals(action)) {
										choices = CHOICES_SUBCONTRACT_AWARDS;
										currentValue = scPackage.getSubcontractStatus().toString();
									} else if (ACTION_RESET_ADDENDUM.equals(action)) {
										choices = CHOICES_ADDENDUM;
										currentValue = scPackage.getSubmittedAddendum();
									} else if (ACTION_RESET_SPLIT.equals(action)) {
										choices = CHOICES_SPLIT_OR_TERMINATE_SUBCONTRACT;
										currentValue = scPackage.getSplitTerminateStatus();
									} else if (ACTION_RESET_FINAL_PAYMENT.equals(action)) {
										choices = CHOICES_FINAL_PAYMENT;
										currentValue = scPackage.getPaymentStatus();
									}else if (ACTION_UPDATE_PACKAGE_POSTED_CERT_AMOUNT.equals(action)) {
										recalculateScPackagePostedCertAmount();
										return;
									}
									else {
										// sanity check, to avoid system crash/invalid operation
										action = null;
										return;
									}

									globalSectionController.getDetailSectionController().populateByResetStatusPanel(
											action,
											choices,
											jobNumber,
											packageNumber,
											null,
											null,
											currentValue);
								}

								public void onFailure(Throwable e) {
									UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
									UIUtil.unmaskMainPanel();
								}
							});
				} else if ("paymentPanel".equals(panelId)) {
					final String packageNumber = packageElement.getAttribute("value").trim();
					final String paymentNumber = paymentElement.getAttribute("value").trim();

					if (jobNumber.isEmpty() || packageNumber.isEmpty() || paymentNumber.isEmpty()) {
						MessageBox.alert("Please enter Job No., Package No. and Payment Certificate No.");
						return;
					}

					if (ACTION_RESET_SUBCONTRACT_PAYMENT.equals(action)) {
						SessionTimeoutCheck.renewSessionTimer();
						globalSectionController.getPaymentRepository().getSCPaymentCert(
								jobNumber,
								packageNumber,
								paymentNumber,
								new AsyncCallback<SCPaymentCert>() {
									String[][]	choices;
									String		currentValue;

									public void onSuccess(SCPaymentCert scPayment) {
										if (scPayment == null) {
											MessageBox.alert(
													"Error",
													"The subcontract payment does not exist.",
													alertCallback);
											return;
										}
										choices = CHOICES_SUBCONTRACT_PAYMENT;
										currentValue = scPayment.getPaymentStatus();
										globalSectionController.getDetailSectionController().populateByResetStatusPanel(
												action,
												choices,
												jobNumber,
												packageNumber,
												paymentNumber,
												null,
												currentValue);
									}

									public void onFailure(Throwable e) {
										UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
										UIUtil.unmaskMainPanel();
									}
								});
					} else if (ACTION_UPDATE_SUBCONTRACT_PAYMENT_CERT.equals(action)) {
						SessionTimeoutCheck.renewSessionTimer();
						globalSectionController.getPaymentRepository().getSCPaymentCert(
								jobNumber,
								packageNumber,
								paymentNumber,
								new AsyncCallback<SCPaymentCert>() {
									public void onSuccess(SCPaymentCert scPaymentCert) {
										if (scPaymentCert == null) {
											MessageBox.alert(
													"Error",
													"The subcontract payment does not exist.",
													alertCallback);
											return;
										}

										globalSectionController.showUpdateSCPaymentCertWindows(scPaymentCert);
									}

									public void onFailure(Throwable e) {
										UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
										UIUtil.unmaskMainPanel();
									}
								});
					}
				} else if ("mainCertPanel".equals(panelId)) {
					final String mainContractCertificateNumber = mainContractCertificateElement.getAttribute("value").trim();

					if (jobNumber.isEmpty() || mainContractCertificateNumber.isEmpty()) {
						MessageBox.alert("Please enter Job No. and Main Contract Certificate No.");
						return;
					}
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getMainContractCertificateRepository().getMainContractCert(
							jobNumber,
							Integer.parseInt(mainContractCertificateNumber),
							new AsyncCallback<MainContractCertificate>() {

								public void onSuccess(MainContractCertificate mainContractCertificate) {
									if (mainContractCertificate != null) {
										globalSectionController.getDetailSectionController().populateByResetStatusPanel(
												action,
												GlobalParameter.getMainCertficateStatuses(),
												jobNumber,
												null,
												null,
												mainContractCertificateNumber,
												mainContractCertificate.getCertificateStatus());
									} else {
										MessageBox.alert(
												"Error",
												"The Main Contract Certificate does not exist.",
												alertCallback);
									}
								}

								public void onFailure(Throwable e) {
									UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
									UIUtil.unmaskMainPanel();
								}
							});

					return;
				}
			}
		});

		this.addListener(new TabPanelListenerAdapter() {
			public boolean doBeforeTabChange(TabPanel src, Panel newPanel, Panel oldPanel) {
				Element e = DomQuery.selectNode("input[name='" + newPanel.getId() + "-next']:checked~label");
				action = e != null ? e.getInnerText() : null;
				return true;
			}
		});

		Toolbar toolbar = new Toolbar();
		nextButton.setIconCls("next-icon");
		toolbar.addButton(nextButton);
		toolbar.addSeparator();

		this.setTopToolbar(toolbar);
	}

	private Panel createSubcontractPanel() {
		String panelId = "subcontractPanel";
		Panel res = new Panel();
		res.setId(panelId);
		res.setTitle("Subcontract Package");
		res.setIconCls("award-icon");
		res.setLayout(new HorizontalLayout(10));
		res.setFrame(true);
		res.setPaddings(0);

		TextField jobNumberField = new TextField("Job No.", "jobNum", 80);
		jobNumberField.setId(panelId + "-" + jobNumberField.getName());

		TextField packageNumberField = new TextField("Package No.", "packageNum", 80);
		packageNumberField.setId(panelId + "-" + packageNumberField.getName());

		FormPanel formPanel = new FormPanel();
		formPanel.setPaddings(5, 5, 10, 0);
		formPanel.setHeight(150);
		formPanel.add(jobNumberField);
		formPanel.add(packageNumberField);

		List<RadioButton> radioButtons = new ArrayList<RadioButton>();
		String groupName = panelId + "-next";
		radioButtons.add(new RadioButton(panelId + "-next", ACTION_RESET_SUBCONTRACT_AWARDS));
		radioButtons.add(new RadioButton(groupName, ACTION_RESET_ADDENDUM));
		radioButtons.add(new RadioButton(groupName, ACTION_RESET_SPLIT));
		radioButtons.add(new RadioButton(groupName, ACTION_UPDATE_SUBCONTRACT_PACKAGE));
		radioButtons.add(new RadioButton(groupName, ACTION_RESET_FINAL_PAYMENT));
		radioButtons.add(new RadioButton(groupName, ACTION_UPDATE_PACKAGE_POSTED_CERT_AMOUNT));
		
		res.add(formPanel);
		res.add(createActionFieldSet(formPanel, radioButtons));

		return res;
	}

	private Panel createPaymentPanel() {
		String panelId = "paymentPanel";
		Panel res = new Panel();
		res.setId(panelId);
		res.setLayout(new HorizontalLayout(10));
		res.setTitle("Subcontract Payment");
		res.setIconCls("payment-icon");
		res.setFrame(true);

		FormPanel formPanel = new FormPanel();
		formPanel.setLabelWidth(140);
		formPanel.setPaddings(5, 5, 10, 0);

		TextField jobNumberField = new TextField("Job No.", "jobNum", 80);
		jobNumberField.setId(panelId + "-" + jobNumberField.getName());

		TextField packageNumberField = new TextField("Package No.", "packageNum", 80);
		packageNumberField.setId(panelId + "-" + packageNumberField.getName());

		TextField paymentNumberField = new TextField("Payment Certificate No.", "paymentNum", 80);
		paymentNumberField.setId(panelId + "-" + paymentNumberField.getName());

		formPanel.add(jobNumberField);
		formPanel.add(packageNumberField);
		formPanel.add(paymentNumberField);

		List<RadioButton> radioButtons = new ArrayList<RadioButton>();
		String groupName = panelId + "-next";
		radioButtons.add(new RadioButton(groupName, ACTION_RESET_SUBCONTRACT_PAYMENT));
		//radioButtons.add(new RadioButton(groupName, ACTION_RESET_FINAL_PAYMENT));
		radioButtons.add(new RadioButton(groupName, ACTION_UPDATE_SUBCONTRACT_PAYMENT_CERT));

		res.add(formPanel);
		res.add(createActionFieldSet(formPanel, radioButtons));

		return res;
	}

	private Panel createMainContractCertificatePanel() {
		String panelId = "mainCertPanel";
		Panel res = new Panel();
		res.setId(panelId);
		res.setLayout(new HorizontalLayout(10));
		res.setTitle("Main Contract Certificate");
		res.setIconCls("certificate-icon");
		res.setFrame(true);

		TextField jobNumberField = new TextField("Job No.", "jobNum", 80);
		jobNumberField.setId(panelId + "-" + jobNumberField.getName());

		TextField mainContractCertificateNumberField = new TextField("Main Contract Certificate No.", "mainContractCertificateNum", 80);
		mainContractCertificateNumberField.setId(panelId + "-" + mainContractCertificateNumberField.getName());

		FormPanel formPanel = new FormPanel();
		formPanel.add(jobNumberField);
		formPanel.add(mainContractCertificateNumberField);
		formPanel.setPaddings(5, 5, 10, 0);
		formPanel.setLabelWidth(170);

		List<RadioButton> radioButtons = new ArrayList<RadioButton>();
		String groupName = panelId + "-next";
		radioButtons.add(new RadioButton(groupName, ACTION_RESET_MAIN_CONTRACT_CERT));

		res.add(formPanel);
		res.add(createActionFieldSet(formPanel, radioButtons));

		return res;
	}

	private FieldSet createActionFieldSet(FormPanel formPanel, List<RadioButton> radiobuttons) {
		FieldSet actionFieldSet = new FieldSet("Actions");
		Panel container = new Panel();
		//container.setLayout(new TableLayout(radiobuttons.size() / formPanel.getComponents().length));
		container.setLayout(new TableLayout(1));
		
		for (int i = 0; i < radiobuttons.size(); i++) {
			final RadioButton r = radiobuttons.get(i);
			r.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					action = r.getText();
				}
			});

			DOM.setStyleAttribute(r.getElement(), "font-size", "12px");
			container.add(r);
		}
		
		actionFieldSet.add(container);

		return actionFieldSet;
	}
	
	private void recalculateScPackagePostedCertAmount(){
		final Window window = new Window();
		window.setTitle("Recalculate Subcontract Package Posted Cert Amount Window");
		window.setHeight(200);
		window.setWidth(350);
		window.setLayout(new RowLayout());
		window.setModal(true);
		window.setId("recalculateWindowID");

		Panel basePanel = new Panel();
		//basePanel.setFrame(true);
		basePanel.setLayout(new RowLayout());

		String message = "Subcontract Package Certified Posted Amount and Subcontract Details Posted Certified Quantity/Amount will be recalculated from Payment Details.";				
		
		Label label = new Label();
		label.setCtCls("table-cell");
		label.setText(message);
		
		basePanel.add(label);
		
		Button recalculateButton = new Button("Recalculate");
		recalculateButton.setIconCls("calculator-icon");
		recalculateButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				UIUtil.maskPanelById("recalculateWindowID", GlobalParameter.LOADING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().recalculateSCDetailPostedCertQty(jobNumber, packageNumber, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable e) {
						UIUtil.unmaskPanelById("recalculateWindowID");
						UIUtil.throwException(e);
						window.close();
					}
					public void onSuccess(Boolean updated) {
						UIUtil.unmaskPanelById("recalculateWindowID");
						if(updated){
							MessageBox.alert("Subcontract Package Certified Posted Amount has been recalculated.");
						}else{
							MessageBox.alert("No data has been updated.");
						}
						window.close();
					}
				});
			}
		});
		window.add(basePanel);
		window.addButton(recalculateButton);
		
		window.show();
	}
}