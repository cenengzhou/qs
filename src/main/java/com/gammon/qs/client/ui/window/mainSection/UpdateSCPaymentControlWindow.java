package com.gammon.qs.client.ui.window.mainSection;

import java.util.Date;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

/**
 * modified by matthewlam, 2015-01-29
 * bug fix #79 - Revamp of Power User Administration page
 */
public class UpdateSCPaymentControlWindow extends Window {
	private static final String		WINDOW_ID					= "UpdateSCPaymentControlWindow";
	private static final String		DEFAULT_CTCLS				= "text-Left-Align";
	private static final String		CLS_TEXTBOX_RIGHT_ALIGN		= "text-Right-Align";
	private static final String		CLS_TEXTBOX_CENTRE_ALIGN	= "text-Centre-Align";

	private int						populateCount				= 0;
	private SCPaymentCert			scPaymentCert;
	private boolean					booleanUpdated				= false;

	private GlobalSectionController	globalSectionController;

	private Label					jobNumberLabel				= new Label("Job No: ");
	private Label					jobNumber					= new Label();
	private Label					jobDescription				= new Label();
	private Label					packageNumberLabel			= new Label("Package No: ");
	private Label					packageNumber				= new Label();
	private Label					packageDescription			= new Label();
	private Label					paymentNumberLabel			= new Label("Payment No: ");
	private Label					paymentNumber				= new Label();

	private TextField				mainContractCertNoField		= new TextField("Main Contract Cert no:", "mainCertNo");
	private Label					mainContractCertNoField_ORG	= new Label();

	private DateField				dueDateField				= new DateField("Due Date : ", GlobalParameter.DATEFIELD_DATEFORMAT);
	private Label					dueDateField_ORG			= new Label();

	private DateField				asAtDateField				= new DateField("As at Date: ", GlobalParameter.DATEFIELD_DATEFORMAT);
	private Label					asAtDateField_ORG			= new Label();

	private DateField				ipaReceiveDateField			= new DateField("IPA Receive Date: ", GlobalParameter.DATEFIELD_DATEFORMAT);
	private Label					ipaReceiveDateField_ORG		= new Label();

	private DateField				certIssueDateField			= new DateField("Cert Issue Date: ", GlobalParameter.DATEFIELD_DATEFORMAT);
	private Label					certIssueDateField_ORG		= new Label();

	private TextField				certAmountField				= new TextField("Cert Amount : ", "certAmount");
	private Label					certAmountField_ORG			= new Label();

	private TextField				remeasuredSCSumField		= new TextField("Remeasured SC Sum : ", "remeasuredSCSum");
	private Label					remeasuredSCSumField_ORG	= new Label();

	private TextField				addendumAmountField			= new TextField("Addendum Amount : ", "addendumAmount");
	private Label					addendumAmountField_ORG		= new Label();

	private ComboBox				paymentTypeComboBox			= new ComboBox("Payment Type : ");
	private Label					paymentTypeField_ORG		= new Label();

	private ComboBox				directPaymentComboBox		= new ComboBox("Direct Payment : ");
	private Label					directPaymentField_ORG		= new Label();

	private AmountRenderer			amountRender;
	private GlobalMessageTicket		globalMessageTicket;

	public UpdateSCPaymentControlWindow(GlobalSectionController g) {
		super();
		setClosable(false);

		dueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		asAtDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		ipaReceiveDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		certIssueDateField.addListener(FieldFactory.updateDatePickerWidthListener());
		
		setId(WINDOW_ID);
		globalSectionController = g;
		setLayout(new FitLayout());
		setWidth(960);
		setHeight(600);
		setVisible(true);
		setCtCls(DEFAULT_CTCLS);
		setTitle("SC Payment Cert Control Window");
		amountRender = new AmountRenderer(globalSectionController.getUser());
		globalMessageTicket = new GlobalMessageTicket();

		Panel basePanel = new Panel();
		basePanel.setWidth(960);
		basePanel.setFrame(true);
		basePanel.setBorder(false);
		basePanel.setLayout(new RowLayout());

		Label emptyLabel = new Label(" ");
		emptyLabel.setWidth(30);

		Panel upperPanel = new Panel();
		upperPanel.setSize(960, 60);
		upperPanel.setLayout(new TableLayout(6));
		upperPanel.setCtCls(DEFAULT_CTCLS);
		upperPanel.setBorder(false);

		jobNumberLabel.setWidth(90);
		jobNumberLabel.setCls(DEFAULT_CTCLS);
		jobNumber.setCls(CLS_TEXTBOX_RIGHT_ALIGN);
		jobDescription.setCtCls(DEFAULT_CTCLS);
		jobDescription.setWidth(180);

		packageNumberLabel.setCls(DEFAULT_CTCLS);
		packageNumberLabel.setWidth(100);
		packageNumber.setCls(CLS_TEXTBOX_RIGHT_ALIGN);
		packageDescription.setWidth(200);
		packageDescription.setCtCls(DEFAULT_CTCLS);

		paymentNumberLabel.setCls(CLS_TEXTBOX_CENTRE_ALIGN);
		paymentNumberLabel.setWidth(180);
		paymentNumber.setCtCls(DEFAULT_CTCLS);

		upperPanel.add(jobNumberLabel);
		upperPanel.add(jobNumber);
		upperPanel.add(packageNumberLabel);
		upperPanel.add(packageNumber);
		upperPanel.add(paymentNumberLabel);
		upperPanel.add(paymentNumber);

		Label descriptionLabel = new Label("Description: ");
		descriptionLabel.setCtCls(DEFAULT_CTCLS);
		upperPanel.add(descriptionLabel);
		upperPanel.add(jobDescription);
		upperPanel.add(descriptionLabel.cloneComponent());
		upperPanel.add(packageDescription, new TableLayoutData(3));

		Label headerHeightControl = new Label();
		headerHeightControl.setHeight(50);
		upperPanel.add(headerHeightControl);
		basePanel.add(upperPanel);

		Panel bottomPanel = new Panel();
		bottomPanel.setFrame(false);
		bottomPanel.setLayout(new TableLayout(6));
		bottomPanel.setFrame(true);
		bottomPanel.setPaddings(1);// 10
		bottomPanel.setCtCls(DEFAULT_CTCLS);
		bottomPanel.setAutoScroll(true);
		Label heightControllerLabel = new Label("");
		heightControllerLabel.setHeight(60);

		bottomPanel.add(new Label());
		bottomPanel.add(emptyLabel.cloneComponent());
		Label currentLabel = new Label("Current Values");
		currentLabel.setCtCls(CLS_TEXTBOX_CENTRE_ALIGN);
		bottomPanel.add(currentLabel);
		bottomPanel.add(emptyLabel.cloneComponent());
		Label newValLabel = new Label("New Values");
		newValLabel.setCtCls(CLS_TEXTBOX_CENTRE_ALIGN);
		bottomPanel.add(newValLabel);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label mainContractCertNoLabel = new Label("Main Contract Cert no : ");
		mainContractCertNoLabel.setWidth(140);
		mainContractCertNoLabel.setCtCls(DEFAULT_CTCLS);
		mainContractCertNoField_ORG.setWidth(150);
		mainContractCertNoField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		mainContractCertNoField.setWidth(150);
		mainContractCertNoField.setCls(CLS_TEXTBOX_CENTRE_ALIGN);
		mainContractCertNoField.addListener(new FieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if (newVal == null || "".equals(newVal.toString().trim()))
						scPaymentCert.setMainContractPaymentCertNo(null);
					else
						scPaymentCert.setMainContractPaymentCertNo(Integer.valueOf(newVal.toString()));
					booleanUpdated = true;

				} catch (NumberFormatException e) {
					MessageBox.alert("Error", "Invalid data! Please input the Numbers!");
					field.setValue(oldVal.toString());
				}
			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(mainContractCertNoLabel);
		bottomPanel.add(mainContractCertNoField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(mainContractCertNoField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label dueDateLabel = new Label("Due Date : ");
		dueDateLabel.setWidth(140);
		dueDateLabel.setCtCls(DEFAULT_CTCLS);
		dueDateField_ORG.setWidth(150);
		dueDateField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		dueDateField.setAltFormats(GlobalParameter.DATEFIELD_ALTDATEFORMAT);
		dueDateField.setCls(CLS_TEXTBOX_CENTRE_ALIGN);
		dueDateField.setWidth(150);
		dueDateField.addListener(new TextFieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || "".equals(newVal.toString()))
					scPaymentCert.setDueDate(null);
				else
					scPaymentCert.setDueDate((Date) newVal);

				booleanUpdated = true;
			}

			public void onValid(Field field) {
			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(dueDateLabel);
		bottomPanel.add(dueDateField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(dueDateField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label asAtDateLabel = new Label("As At Date : ");
		asAtDateLabel.setWidth(140);
		asAtDateLabel.setCtCls(DEFAULT_CTCLS);
		asAtDateField_ORG.setWidth(150);
		asAtDateField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		asAtDateField.setAltFormats(GlobalParameter.DATEFIELD_ALTDATEFORMAT);
		asAtDateField.setCls(CLS_TEXTBOX_CENTRE_ALIGN);
		asAtDateField.setWidth(150);
		asAtDateField.addListener(new TextFieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {
				if (asAtDateField.getValue() == null || "".equals(asAtDateField.getValueAsString().trim())) {
					scPaymentCert.setAsAtDate(null);
				} else
					scPaymentCert.setAsAtDate((Date) newVal);
				booleanUpdated = true;
			}

			public void onValid(Field field) {
			}
		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(asAtDateLabel);
		bottomPanel.add(asAtDateField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(asAtDateField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label ipaReceiveDateLabel = new Label("IPA Receive Date : ");
		ipaReceiveDateLabel.setWidth(140);
		ipaReceiveDateLabel.setCtCls(DEFAULT_CTCLS);
		ipaReceiveDateField_ORG.setWidth(150);
		ipaReceiveDateField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		ipaReceiveDateField.setAltFormats(GlobalParameter.DATEFIELD_ALTDATEFORMAT);
		ipaReceiveDateField.setCls(CLS_TEXTBOX_CENTRE_ALIGN);
		ipaReceiveDateField.setWidth(150);
		ipaReceiveDateField.addListener(new TextFieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || "".equals(newVal.toString()))
					scPaymentCert.setIpaOrInvoiceReceivedDate(null);
				else
					scPaymentCert.setIpaOrInvoiceReceivedDate((Date) newVal);
				booleanUpdated = true;
			}

			public void onValid(Field field) {
			}
		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(ipaReceiveDateLabel);
		bottomPanel.add(ipaReceiveDateField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(ipaReceiveDateField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label certIssueDateLabel = new Label("Cert Issue Date : ");
		certIssueDateLabel.setWidth(140);
		certIssueDateLabel.setCtCls(DEFAULT_CTCLS);
		certIssueDateField_ORG.setWidth(150);
		certIssueDateField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		certIssueDateField.setAltFormats(GlobalParameter.DATEFIELD_ALTDATEFORMAT);
		certIssueDateField.setCls(CLS_TEXTBOX_CENTRE_ALIGN);
		certIssueDateField.setWidth(150);
		certIssueDateField.addListener(new TextFieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || "".equals(newVal.toString()))
					scPaymentCert.setCertIssueDate(null);
				else
					scPaymentCert.setCertIssueDate((Date) newVal);
				booleanUpdated = true;
			}

			public void onValid(Field field) {
			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(certIssueDateLabel);
		bottomPanel.add(certIssueDateField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(certIssueDateField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label certAmountLabel = new Label("Certificate Amount : ");
		certAmountLabel.setWidth(140);
		certAmountLabel.setCtCls(DEFAULT_CTCLS);
		certAmountField_ORG.setWidth(150);
		certAmountField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		certAmountField.setWidth(150);
		certAmountField.setCls(CLS_TEXTBOX_RIGHT_ALIGN);
		certAmountField.addListener(new TextFieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {

				try {
					if (newVal == null || "".equals(newVal.toString().trim()))
						scPaymentCert.setCertAmount(0.0);
					else
						scPaymentCert.setCertAmount(Double.valueOf(newVal.toString().replaceAll(",", "")));
					field.setValue(amountRender.render(newVal.toString()));
					booleanUpdated = true;
				} catch (NumberFormatException e) {
					MessageBox.alert("Error", "Invalid data! Please input the Numbers!");
					field.setValue(oldVal.toString());
				}
			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(certAmountLabel);
		bottomPanel.add(certAmountField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(certAmountField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label remeasuredSCSumLabel = new Label("Remeasured SC Sum : ");
		remeasuredSCSumLabel.setWidth(140);
		remeasuredSCSumLabel.setCtCls(DEFAULT_CTCLS);
		remeasuredSCSumField_ORG.setWidth(150);
		remeasuredSCSumField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		remeasuredSCSumField.setWidth(150);
		remeasuredSCSumField.setCls(CLS_TEXTBOX_RIGHT_ALIGN);
		remeasuredSCSumField.addListener(new TextFieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {

				try {
					if (newVal == null || "".equals(newVal.toString().trim()))
						scPaymentCert.setRemeasureContractSum(0.0);
					else
						scPaymentCert.setRemeasureContractSum(Double.valueOf(newVal.toString().replaceAll(",", "")));
					field.setValue(amountRender.render(newVal.toString()));
					booleanUpdated = true;
				} catch (NumberFormatException e) {
					MessageBox.alert("Error", "Invalid data! Please input the Numbers!");
					field.setValue(oldVal.toString());
				}
			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(remeasuredSCSumLabel);
		bottomPanel.add(remeasuredSCSumField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(remeasuredSCSumField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label addendumAmountLabel = new Label("Addendum Amount : ");
		addendumAmountLabel.setWidth(140);
		addendumAmountLabel.setCtCls(DEFAULT_CTCLS);
		addendumAmountField_ORG.setWidth(150);
		addendumAmountField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		addendumAmountField.setWidth(150);
		addendumAmountField.setCls(CLS_TEXTBOX_RIGHT_ALIGN);
		addendumAmountField.addListener(new FieldListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {

				try {
					if (newVal == null || "".equals(newVal.toString().trim()))
						scPaymentCert.setAddendumAmount(0.0);
					else
						scPaymentCert.setAddendumAmount(Double.valueOf(newVal.toString().replaceAll(",", "")));
					field.setValue(amountRender.render(newVal.toString()));
					booleanUpdated = true;

				} catch (NumberFormatException e) {
					MessageBox.alert("Error", "Invalid data! Please input the Numbers!");
					field.setValue(oldVal.toString());
				}
			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(addendumAmountLabel);
		bottomPanel.add(addendumAmountField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(addendumAmountField);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label paymentTypeLabel = new Label("Payment Type : ");
		paymentTypeLabel.setWidth(140);
		paymentTypeLabel.setCtCls(DEFAULT_CTCLS);
		paymentTypeField_ORG.setWidth(150);
		paymentTypeField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		Store paymentTypeStore = GlobalParameter.SCPAYMENTCERT_INTRIM_FINAL_PAYMENT_STORE;
		paymentTypeComboBox.setWidth(150);
		paymentTypeComboBox.setCls(DEFAULT_CTCLS);
		paymentTypeComboBox.setStore(paymentTypeStore);
		paymentTypeComboBox.setForceSelection(true);
		paymentTypeComboBox.setDisplayField("description");
		paymentTypeComboBox.setValueField("value");
		paymentTypeComboBox.setListWidth(200);
		paymentTypeStore.load();

		paymentTypeComboBox.addListener(new ComboBoxListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {

				if (newVal == null || "".equals(newVal.toString().trim()))
					MessageBox.alert("Error", "Invalid Status!");
				else
					scPaymentCert.setIntermFinalPayment(paymentTypeComboBox.getValue());
				booleanUpdated = true;

			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(paymentTypeLabel);
		bottomPanel.add(paymentTypeField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(paymentTypeComboBox);
		bottomPanel.add(emptyLabel.cloneComponent());

		Label directPaymentLabel = new Label("Direct Payment : ");
		directPaymentLabel.setWidth(140);
		directPaymentLabel.setCtCls(DEFAULT_CTCLS);
		directPaymentField_ORG.setWidth(150);
		directPaymentField_ORG.setCls(CLS_TEXTBOX_CENTRE_ALIGN);

		Store directPaymentStore = GlobalParameter.SCPAYMENTCERT_DIRECTPAYMENT_STORE;
		directPaymentComboBox.setWidth(150);
		directPaymentComboBox.setCls(DEFAULT_CTCLS);
		directPaymentComboBox.setStore(directPaymentStore);
		directPaymentComboBox.setForceSelection(true);
		directPaymentComboBox.setDisplayField("description");
		directPaymentComboBox.setValueField("value");
		directPaymentComboBox.setListWidth(200);
		directPaymentStore.load();

		directPaymentComboBox.addListener(new ComboBoxListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {

				if (newVal == null || "".equals(newVal.toString().trim()))
					MessageBox.alert("Error", "Invalid Status!");
				else
					scPaymentCert.setDirectPayment(directPaymentComboBox.getValue());
				booleanUpdated = true;

			}

		});

		bottomPanel.add(heightControllerLabel.cloneComponent());
		bottomPanel.add(directPaymentLabel);
		bottomPanel.add(directPaymentField_ORG);
		bottomPanel.add(emptyLabel.cloneComponent());
		bottomPanel.add(directPaymentComboBox);
		bottomPanel.add(emptyLabel.cloneComponent());

		basePanel.add(bottomPanel);
		this.add(basePanel);
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (booleanUpdated)
					MessageBox.confirm("Warning", "Close the windows without save the changes?", new ConfirmCallback() {

						public void execute(String btnID) {
							if ("y".equalsIgnoreCase(btnID.substring(0, 1)))
								globalSectionController.closeCurrentWindow();
						}
					});
				else
					globalSectionController.closeCurrentWindow();

			}

		});

		Button saveButton = new Button("Save");
		saveButton.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				if (booleanUpdated) {
					UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.SAVING_MSG, true);
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionController.getPaymentRepository().updateSCPaymentCertAdmin(
							scPaymentCert,
							new AsyncCallback<String>() {

								public void onFailure(Throwable e) {
									UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
									UIUtil.unmaskPanelById(WINDOW_ID);
								}

								public void onSuccess(String errMsg) {
									if (errMsg == null || "".equals(errMsg.trim())) {
										MessageBox.alert("Information", "Update Successfully.");
										globalSectionController.closeCurrentWindow();
									} else
										MessageBox.alert("Error", errMsg);
									UIUtil.unmaskPanelById(WINDOW_ID);
								}

							});
				} else
					MessageBox.alert("No change found!");
			}

		});

		basePanel.addButton(saveButton);
		basePanel.addButton(closeButton);
	}

	public void populateWindows(SCPaymentCert scPaymentCert) {
		UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.LOADING_MSG, true);
		populateCount = 0;
		jobNumber.setText(scPaymentCert.getJobNo());
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().obtainJob(scPaymentCert.getJobNo(), new AsyncCallback<Job>() {

			public void onSuccess(Job aJob) {
				jobDescription.setText(aJob.getDescription());
				populateSC_Finished_Unmask();
			}

			public void onFailure(Throwable e) {
				UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
			}
		});
		packageNumber.setText(scPaymentCert.getPackageNo());
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getSCPackage(
				scPaymentCert.getJobNo(),
				scPaymentCert.getPackageNo(),
				new AsyncCallback<SCPackage>() {

					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
					}

					public void onSuccess(SCPackage scPackage) {
						packageDescription.setText(scPackage.getDescription());
						populateSC_Finished_Unmask();
					}

				});
		paymentNumber.setText(scPaymentCert.getPaymentCertNo().toString());
		if (scPaymentCert.getMainContractPaymentCertNo() != null)
			mainContractCertNoField_ORG.setText(scPaymentCert.getMainContractPaymentCertNo().toString());
		else
			mainContractCertNoField_ORG.setText("");
		mainContractCertNoField.setValue(mainContractCertNoField_ORG.getText());

		if (scPaymentCert.getDueDate() != null) {
			dueDateField_ORG.setText(DateUtil.formatDate(scPaymentCert.getDueDate(), GlobalParameter.DATE_FORMAT));
			dueDateField.setValue(scPaymentCert.getDueDate());
		} else {
			dueDateField_ORG.setText("");
			dueDateField.setValue("");
		}

		if (scPaymentCert.getAsAtDate() != null) {
			asAtDateField_ORG.setText(DateUtil.formatDate(scPaymentCert.getAsAtDate(), GlobalParameter.DATE_FORMAT));
			asAtDateField.setValue(scPaymentCert.getAsAtDate());
		} else {
			asAtDateField_ORG.setText("");
			asAtDateField.setValue("");
		}

		if (scPaymentCert.getIpaOrInvoiceReceivedDate() != null) {
			ipaReceiveDateField_ORG.setText(DateUtil.formatDate(
					scPaymentCert.getIpaOrInvoiceReceivedDate(),
					GlobalParameter.DATE_FORMAT));
			ipaReceiveDateField.setValue(scPaymentCert.getIpaOrInvoiceReceivedDate());
		} else {
			ipaReceiveDateField_ORG.setText("");
			ipaReceiveDateField.setValue("");
		}

		if (scPaymentCert.getCertIssueDate() != null) {
			certIssueDateField_ORG.setText(DateUtil.formatDate(
					scPaymentCert.getCertIssueDate(),
					GlobalParameter.DATE_FORMAT));
			certIssueDateField.setValue(scPaymentCert.getCertIssueDate());
		} else {
			certIssueDateField_ORG.setText("");
			certIssueDateField.setValue("");
		}

		if (scPaymentCert.getCertAmount() != null)
			certAmountField_ORG.setText(amountRender.render(scPaymentCert.getCertAmount().toString()));
		else
			certAmountField_ORG.setText("");
		certAmountField.setValue(certAmountField_ORG.getText());

		if (scPaymentCert.getRemeasureContractSum() != null)
			remeasuredSCSumField_ORG.setText(amountRender.render(scPaymentCert.getRemeasureContractSum().toString()));
		else
			remeasuredSCSumField_ORG.setText("");
		remeasuredSCSumField.setValue(remeasuredSCSumField_ORG.getText());

		if (scPaymentCert.getAddendumAmount() != null)
			addendumAmountField_ORG.setText(amountRender.render(scPaymentCert.getAddendumAmount().toString()));
		else
			addendumAmountField_ORG.setText("");
		addendumAmountField.setValue(addendumAmountField_ORG.getText());

		if (scPaymentCert.getIntermFinalPayment() != null && "F".equals(scPaymentCert.getIntermFinalPayment().trim()))
			paymentTypeField_ORG.setText(SCPaymentCert.FINAL_PAYMENT);
		else
			paymentTypeField_ORG.setText(SCPaymentCert.INTERIM_PAYMENT);
		paymentTypeComboBox.setValue(scPaymentCert.getIntermFinalPayment());

		if (scPaymentCert.getDirectPayment() != null) {
			directPaymentComboBox.setValue(scPaymentCert.getDirectPayment());
			if ("Y".equals(scPaymentCert.getDirectPayment()))
				directPaymentField_ORG.setText("Direct Payment");
			else
				directPaymentField_ORG.setText("Normal Payment");
		} else {
			directPaymentComboBox.setValue(SCPaymentCert.NON_DIRECT_PAYMENT);
			directPaymentField_ORG.setText("Normal Payment");
		}
		this.scPaymentCert = scPaymentCert;
		populateSC_Finished_Unmask();
	}

	private void populateSC_Finished_Unmask() {
		populateCount++;
		if (populateCount == 3) {
			UIUtil.unmaskPanelById(WINDOW_ID);
			doLayout();
		}
	}

	public void close() {
		UIUtil.unmaskMainPanel();
		super.close();
	}

}
