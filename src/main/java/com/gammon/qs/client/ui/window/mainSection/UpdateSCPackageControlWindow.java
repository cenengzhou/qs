package com.gammon.qs.client.ui.window.mainSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.renderer.AmountRenderer;
import com.gammon.qs.client.ui.renderer.RateRenderer;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

/**
 * modified by matthewlam, 2015-01-29
 * bug fix #79 - Revamp of Power User Administration page
 */
public class UpdateSCPackageControlWindow extends Window {

	private static final String		WINDOW_ID							= "UpdateSCPackageControlWindow";
	private static final String		DEFAULT_CTCLS						= "text-Left-Align";
	private static final String		CLS_TEXTBOX_RIGHT_ALIGN				= "text-Right-Align";

	private SCPackage				scPackage;
	private boolean					isScPackageUpdated					= false;

	private GlobalSectionController	globalSectionController;

	private Label					jobNumber							= new Label();
	private Label					descriptionLabel;
	private Label					jobDescription						= new Label();

	private Label					packageNumber						= new Label();
	private Label					packageDescription					= new Label();

	private Label					packageDescriptionUpdateFieldORG	= new Label();
	private TextField				packageDescriptionUpdateField		= new TextField();

	private TextField				subcontractorId						= new TextField();
	private Label					subcontractorIdORG					= new Label();
	private Label					subcontractorName					= new Label();
	private Label					subcontractorNameORG				= new Label();

	private TextField				originalSCSumField					= new TextField();
	private Label					originalSCSumFieldORG				= new Label();
	private TextField				remeasureSCSumField					= new TextField();
	private Label					remeasureSCSumFieldORG				= new Label();
	private TextField				approvedVOField						= new TextField();
	private Label					approvedVOFieldORG					= new Label();

	private Label					retentionTermsFieldORG				= new Label();
	private ComboBox				retentionTermsComboBox;
	private TextField				maxRetentionField					= new TextField();
	private Label					maxRetentionFieldORG				= new Label();
	private TextField				interimRetentionField				= new TextField();
	private Label					interimRetentionFieldORG			= new Label();
	private TextField				mosRetentionField					= new TextField();
	private Label					mosRetentionFieldORG				= new Label();

	private TextField				retentionAmountField				= new TextField();
	private Label					retentionAmountFieldORG				= new Label();
	private TextField				accumlatedRetentionField			= new TextField();
	private Label					accumlatedRetentionFieldORG			= new Label();
	private TextField				retentionReleasedField				= new TextField();
	private Label					retentionReleasedFieldORG			= new Label();

	private ComboBox				paymentCurrencyComboBox				= new ComboBox();
	private Label					paymentCurrencyFieldORG				= new Label();
	private NumberField				exchangeRateField					= new NumberField();
	private Label					exchangeRateFieldORG				= new Label();
	private ComboBox				paymentTermsComboBox;
	private Label					paymentTermsFieldORG				= new Label();

	private TextField				approvalSubTypeField				= new TextField();
	private Label					approvalSubTypeFieldORG				= new Label();

	private ComboBox				formOfSubcontractComboBox;
	private Label					formOfSubcontractFieldORG			= new Label();
	private TextField				internalJobField					= new TextField();
	private Label					internalJobFieldORG					= new Label();

	private Label					heightControl						= new Label();

	private Label					cpfCalculationORG					= new Label();
	private ComboBox				cpfCalculationComboBox;
	
	private Label					cpfBasePeriodORG					= new Label();
	private NumberField				cpfBasePeriodField					= new NumberField();
	
	private Label					cpfBaseYearORG						= new Label();
	private NumberField				cpfBaseYearField					= new NumberField();
	
	
	private AmountRenderer			amountRenderer;
	private RateRenderer			rateRenderer;

	public UpdateSCPackageControlWindow(GlobalSectionController g) {
		super();
		setClosable(false);
		setModal(true);
		
		setId(WINDOW_ID);
		globalSectionController = g;
		setLayout(new FitLayout());
		setWidth(960);
		setHeight(500);
		setVisible(true);
		setCtCls(DEFAULT_CTCLS);
		setTitle("Update Subcontract Package");
		amountRenderer = new AmountRenderer(globalSectionController.getUser());
		rateRenderer = new RateRenderer(globalSectionController.getUser());

		Panel basePanel = new Panel();
		basePanel.setWidth(960);
		basePanel.setFrame(true);
		basePanel.setBorder(false);
		basePanel.setLayout(new RowLayout());

		Label emptyLabel = new Label(" ");
		emptyLabel.setWidth(30);

		Panel upperPanel = new Panel();
		upperPanel.setWidth(960);
		upperPanel.setHeight(50);
		upperPanel.setLayout(new TableLayout(4));
		upperPanel.setCtCls(DEFAULT_CTCLS);
		upperPanel.setBorder(false);

		Label jobNoLabel = new Label("Job Number: ");
		jobNoLabel.setCtCls(DEFAULT_CTCLS);
		jobNoLabel.setWidth(80);
		jobNumber.setWidth(50);
		jobNumber.setCls(DEFAULT_CTCLS);
		jobDescription.setCtCls(DEFAULT_CTCLS);
		jobDescription.setWidth(300);

		Label packageNoLabel = new Label("Package No: ");
		packageNoLabel.setCtCls(DEFAULT_CTCLS);
		packageNoLabel.setWidth(80);
		packageNumber.setCls(DEFAULT_CTCLS);
		packageNumber.setWidth(50);
		packageDescription.setWidth(300);

		descriptionLabel = new Label("Description: ");
		descriptionLabel.setCtCls(DEFAULT_CTCLS);
		jobDescription.setCtCls(DEFAULT_CTCLS);
		packageDescription.setCtCls(DEFAULT_CTCLS);

		upperPanel.add(jobNoLabel);
		upperPanel.add(jobNumber);
		upperPanel.add(packageNoLabel);
		upperPanel.add(packageNumber);
		upperPanel.add(descriptionLabel);
		upperPanel.add(jobDescription);
		upperPanel.add(descriptionLabel.cloneComponent());
		upperPanel.add(packageDescription);

		basePanel.add(upperPanel);

		Panel bottomPanel = new Panel();
		bottomPanel.setFrame(false);
		bottomPanel.setLayout(new TableLayout(4));
		bottomPanel.setFrame(true);
		bottomPanel.setAutoScroll(true);

		heightControl.setHeight(40);
		bottomPanel.add(heightControl.cloneComponent());
		Label header = new Label("");
		header.setSize(160, 30);
		bottomPanel.add(header);

		header = new Label("Current Values");
		header.setSize(300, 30);
		header.setCls(DEFAULT_CTCLS);
		bottomPanel.add(header);

		header = new Label("New Values");
		header.setSize(300, 30);
		header.setCls(DEFAULT_CTCLS);
		bottomPanel.add(header);

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Package Description: "));
		bottomPanel.add(packageDescriptionUpdateFieldORG);
		bottomPanel.add(packageDescriptionUpdateField);
		packageDescriptionUpdateField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || newVal.toString() == null) {
					scPackage.setDescription("");
				} else {
					scPackage.setDescription(newVal.toString());
				}

				isScPackageUpdated = true;
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Subcontractor: "));
		bottomPanel.add(subcontractorIdORG);
		bottomPanel.add(subcontractorId);

		subcontractorId.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					final String oldVendor = oldVal.toString();
					if ("".equals(newVal.toString().trim())) {
						subcontractorName.setText("");
						scPackage.setVendorNo(newVal.toString().trim());
						isScPackageUpdated = true;
					} else
						SessionTimeoutCheck.renewSessionTimer();
						globalSectionController.getMasterListRepository().searchVendorList(
								newVal.toString().trim(),
								new AsyncCallback<List<MasterListVendor>>() {
									public void onFailure(Throwable e) {
										subcontractorName.setText("");
										subcontractorId.setValue("");
										UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
									}

									public void onSuccess(List<MasterListVendor> vendorList) {
										MasterListVendor vendor = matchVendorNumber(
												subcontractorId.getValueAsString().trim(),
												vendorList);
										if (vendor != null) {
											subcontractorName.setText(vendor.getVendorName());
											scPackage.setVendorNo(vendor.getVendorNo().toString().trim());
											isScPackageUpdated = true;
										} else {
											MessageBox.alert("Vendor not found!");
											subcontractorId.setValue(oldVendor.trim());
										}
									}

								});
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to Blank");
					scPackage.setVendorNo("");
					subcontractorName.setText("");
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Subcontractor Name:"));
		bottomPanel.add(subcontractorNameORG);
		bottomPanel.add(subcontractorName);

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Original SC Sum:"));
		bottomPanel.add(originalSCSumFieldORG);
		bottomPanel.add(originalSCSumField);

		originalSCSumField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setOriginalSubcontractSum(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
					field.setValue(amountRenderer.render(newVal.toString()));
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setOriginalSubcontractSum(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Remeasured SC Sum: "));
		bottomPanel.add(remeasureSCSumFieldORG);
		bottomPanel.add(remeasureSCSumField);

		remeasureSCSumField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setRemeasuredSubcontractSum(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
					field.setValue(amountRenderer.render(newVal.toString()));
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setRemeasuredSubcontractSum(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Approved VO:"));
		bottomPanel.add(approvedVOFieldORG);
		bottomPanel.add(approvedVOField);

		approvedVOField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setApprovedVOAmount(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
					field.setValue(amountRenderer.render(newVal.toString()));
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setApprovedVOAmount(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		Store retentionTermsStore = GlobalParameter.RETENTION_TERMS_STORE;
		retentionTermsComboBox = new ComboBox("retentionTerms");
		retentionTermsComboBox.setStore(retentionTermsStore);
		retentionTermsComboBox.setForceSelection(true);
		retentionTermsComboBox.setDisplayField("termDescription");
		retentionTermsComboBox.setValueField("termsValue");
		retentionTermsComboBox.setWidth(150);
		retentionTermsComboBox.setListWidth(250);
		retentionTermsStore.load();
		retentionTermsComboBox.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (retentionTermsComboBox.getValue() == null || "".equals(retentionTermsComboBox.getValue()))
					scPackage.setRetentionTerms(null);
				else
					scPackage.setRetentionTerms(retentionTermsComboBox.getValue());
				isScPackageUpdated = true;
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Retention Terms: "));
		bottomPanel.add(retentionTermsFieldORG);
		bottomPanel.add(retentionTermsComboBox);

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Max Retention %: "));
		bottomPanel.add(maxRetentionFieldORG);
		bottomPanel.add(maxRetentionField);
		maxRetentionField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setMaxRetentionPercentage(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setMaxRetentionPercentage(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Interim Retention %: "));
		bottomPanel.add(interimRetentionFieldORG);
		bottomPanel.add(interimRetentionField);
		interimRetentionField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setInterimRentionPercentage(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setInterimRentionPercentage(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("MOS Retention %: "));
		bottomPanel.add(mosRetentionFieldORG);
		bottomPanel.add(mosRetentionField);
		mosRetentionField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setMosRetentionPercentage(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setMosRetentionPercentage(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Retention Amount: "));
		bottomPanel.add(retentionAmountFieldORG);
		bottomPanel.add(retentionAmountField);
		retentionAmountField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setRetentionAmount(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
					field.setValue(amountRenderer.render(newVal.toString()));
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setRetentionAmount(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Accumlated Retention: "));
		bottomPanel.add(accumlatedRetentionFieldORG);
		bottomPanel.add(accumlatedRetentionField);
		accumlatedRetentionField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setAccumlatedRetention(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
					field.setValue(amountRenderer.render(newVal.toString()));
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setAccumlatedRetention(0.0);
					isScPackageUpdated = true;
				}
			}

		});
		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Retention Released:"));
		bottomPanel.add(retentionReleasedFieldORG);
		bottomPanel.add(retentionReleasedField);
		retentionReleasedField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setRetentionReleased(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
					field.setValue(amountRenderer.render(newVal.toString()));
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setRetentionReleased(0.0);
					isScPackageUpdated = true;
				}
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Payment Currency: "));
		bottomPanel.add(paymentCurrencyFieldORG);

		Store currencyStore = globalSectionController.getCurrencyCodeStore();
		paymentCurrencyComboBox.setStore(currencyStore);
		paymentCurrencyComboBox.setDisplayField("currencyDescription");
		paymentCurrencyComboBox.setValueField("currencyCode");
		paymentCurrencyComboBox.setForceSelection(true);
		paymentCurrencyComboBox.setListWidth(170);
		paymentCurrencyComboBox.setWidth(150);
		currencyStore.load();
		paymentCurrencyComboBox.addListener(new ComboBoxListenerAdapter() {

			public void onChange(Field field, Object newVal, Object oldVal) {
				scPackage.setPaymentCurrency(paymentCurrencyComboBox.getValue());
				isScPackageUpdated = true;
			}

		});
		bottomPanel.add(paymentCurrencyComboBox);

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Exchange Rate: "));
		bottomPanel.add(exchangeRateFieldORG);
		bottomPanel.add(exchangeRateField);
		exchangeRateField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {
					if ("".equals(newVal.toString().trim()))
						newVal = "0";
					scPackage.setExchangeRate(Double.valueOf(newVal.toString().replaceAll(",", "")));
					isScPackageUpdated = true;
				} catch (NumberFormatException e) {
					MessageBox.alert("Invalid Value!");
					field.setValue(oldVal.toString());
				} catch (NullPointerException npe) {
					MessageBox.alert("Value was set to 0");
					scPackage.setExchangeRate(0.0);
					isScPackageUpdated = true;
				}
			}
		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Payment Terms: "));
		bottomPanel.add(paymentTermsFieldORG);
		Store paymentTermsStore = GlobalParameter.SC_PAYMENT_TERMS_STORE;
		paymentTermsStore.load();
		paymentTermsComboBox = new ComboBox("Payment Terms");
		paymentTermsComboBox.setWidth(150);
		paymentTermsComboBox.setListWidth(280);
		paymentTermsComboBox.setStore(paymentTermsStore);
		paymentTermsComboBox.setDisplayField("termsDisplay");
		paymentTermsComboBox.setValueField("termsValue");
		paymentTermsComboBox.setForceSelection(true);
		paymentTermsComboBox.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				scPackage.setPaymentTerms(paymentTermsComboBox.getValue());
				isScPackageUpdated = true;
			}
		});
		bottomPanel.add(paymentTermsComboBox);

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Approval Sub-Type: "));
		bottomPanel.add(approvalSubTypeFieldORG);
		bottomPanel.add(approvalSubTypeField);
		approvalSubTypeField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || newVal.toString() == null || "".equals(newVal.toString()))
					scPackage.setApprovalRoute(null);
				else
					scPackage.setApprovalRoute(newVal.toString());

				isScPackageUpdated = true;
			}

		});

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Form of Subcontract: "));
		bottomPanel.add(formOfSubcontractFieldORG);
		formOfSubcontractComboBox = new ComboBox("Form of subcontract: ");
		formOfSubcontractComboBox.setForceSelection(true);
		formOfSubcontractComboBox.setWidth(150);
		formOfSubcontractComboBox.setListWidth(170);
		formOfSubcontractComboBox.setDisplayField("description");
		formOfSubcontractComboBox.setValueField("value");
		Store formOfSubcontractStore = GlobalParameter.FORM_OF_SUBCONTRACT_STORE;
		formOfSubcontractComboBox.setStore(formOfSubcontractStore);
		formOfSubcontractStore.load();
		formOfSubcontractComboBox.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				scPackage.setFormOfSubcontract(formOfSubcontractComboBox.getValue());
				isScPackageUpdated = true;
			}

		});
		bottomPanel.add(formOfSubcontractComboBox);

		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("Internal Job: "));
		bottomPanel.add(internalJobFieldORG);
		bottomPanel.add(internalJobField);
		internalJobField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || newVal.toString() == null || "".equals(newVal.toString()))
					scPackage.setInternalJobNo(null);
				else
					scPackage.setInternalJobNo(newVal.toString().trim());
				isScPackageUpdated = true;
			}

		});

		
		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("CPF Calculation: "));
		bottomPanel.add(cpfCalculationORG);
		String[][] cpfCalculation = new String[][]{
					new String[]{SCPackage.CPF_SUBJECT_TO, 		SCPackage.CPF_SUBJECT_TO},
					new String[]{SCPackage.CPF_NOT_SUBJECT_TO, 	SCPackage.CPF_NOT_SUBJECT_TO}
			};
		Store cpfCalculationStore = new SimpleStore(new String[]{"cpfCalculationValue", "cpfCalculationDisplay"}, cpfCalculation);
		
		cpfCalculationStore.load();
		cpfCalculationComboBox = new ComboBox("CPF Calculation");
		cpfCalculationComboBox.setWidth(150);
		cpfCalculationComboBox.setListWidth(280);
		cpfCalculationComboBox.setStore(cpfCalculationStore);
		cpfCalculationComboBox.setDisplayField("cpfCalculationDisplay");
		cpfCalculationComboBox.setValueField("cpfCalculationValue");
		cpfCalculationComboBox.setForceSelection(true);
		cpfCalculationComboBox.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				scPackage.setCpfCalculation(newVal.toString());
				
				if(SCPackage.CPF_SUBJECT_TO.equals(newVal.toString())){
					cpfBasePeriodField.enable();
					cpfBaseYearField.enable();
				}else{
					cpfBasePeriodField.setValue("");
					cpfBaseYearField.setValue("");
					cpfBasePeriodField.disable();
					cpfBaseYearField.disable();
					
					scPackage.setCpfBasePeriod(null);
					scPackage.setCpfBaseYear(null);
				}
				isScPackageUpdated = true;
			}
		});
		bottomPanel.add(cpfCalculationComboBox);
		
		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("CPF Base Period: "));
		bottomPanel.add(cpfBasePeriodORG);
		
		cpfBasePeriodField.setAllowDecimals(false);
		cpfBasePeriodField.setAllowNegative(false);
		cpfBasePeriodField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || newVal.toString() == null || "".equals(newVal.toString()))
					scPackage.setCpfBasePeriod(null);
				else{
					if(newVal.toString().trim().length()==0 
							|| ((Integer) newVal).intValue()>12 
							|| ((Integer) newVal).intValue()<1){
						MessageBox.alert("Please input 1 - 12 for CPF Base Period.");
						return;
					}
					scPackage.setCpfBasePeriod(Integer.valueOf(newVal.toString().trim()));
				}
				isScPackageUpdated = true;
			}

		});
		cpfBasePeriodField.disable();
		bottomPanel.add(cpfBasePeriodField);
		
		
		bottomPanel.add(heightControl.cloneComponent());
		bottomPanel.add(new Label("CPF Base Year: "));
		bottomPanel.add(cpfBaseYearORG);
		
		cpfBaseYearField.setAllowDecimals(false);
		cpfBaseYearField.setAllowNegative(false);
		cpfBaseYearField.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || newVal.toString() == null || "".equals(newVal.toString()))
					scPackage.setCpfBaseYear(null);
				else{
					if(newVal.toString().trim().length()!=4){
						MessageBox.alert("Please input 4 digits for CPF Base Year.");
						return;
					}
					scPackage.setCpfBaseYear(Integer.valueOf(newVal.toString().trim()));
				}
				isScPackageUpdated = true;
			}

		});
		cpfBaseYearField.disable();
		bottomPanel.add(cpfBaseYearField);
		
		for (Component component : bottomPanel.getComponents()) {
			if (component instanceof TextField) {
				component.setWidth("200px");
				if (!(component instanceof ComboBox))
					component.setCls(CLS_TEXTBOX_RIGHT_ALIGN);
			} else if (component instanceof Label) {
				component.setCtCls(DEFAULT_CTCLS);
			}
		}

		basePanel.add(bottomPanel);
		this.add(basePanel);

		basePanel.addButton(new Button("Save", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (!isScPackageUpdated) {
					MessageBox.alert("No change found!");
					return;
				}

				String errorMsg = validateCPFCalculation();
				if(errorMsg.length()>0){
					MessageBox.alert(errorMsg);
					return;
				}
				
				UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.SAVING_MSG, true);
				SessionTimeoutCheck.renewSessionTimer();
				globalSectionController.getPackageRepository().saveOrUpdateSCPackageAdmin(
						scPackage,
						new AsyncCallback<String>() {
							public void onFailure(Throwable e) {
								UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
								UIUtil.unmaskPanelById(WINDOW_ID);
							}

							public void onSuccess(String errMsg) {
								if (errMsg == null || "".equals(errMsg.trim())) {
									MessageBox.alert("Information", "Update Successfully.");
									globalSectionController.closeCurrentWindow();
								} else {
									MessageBox.alert("Error", errMsg);
								}
								UIUtil.unmaskPanelById(WINDOW_ID);
							}

						});
			}
		}));

		basePanel.addButton(new Button("Close", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (isScPackageUpdated) {
					MessageBox.confirm("Warning", "Close the windows without save the changes?", new ConfirmCallback() {

						public void execute(String btnID) {
							if ("y".equalsIgnoreCase(btnID.substring(0, 1)))
								globalSectionController.closeCurrentWindow();
						}
					});
				} else {
					globalSectionController.closeCurrentWindow();
				}
			}
		}));
	}

	public void populateWindows(SCPackage scPackage) {
		UIUtil.maskPanelById(WINDOW_ID, GlobalParameter.LOADING_MSG, true);
		jobNumber.setText(scPackage.getJob().getJobNumber());
		packageNumber.setText(scPackage.getPackageNo());
		packageDescription.setText(scPackage.getDescription());
		packageDescriptionUpdateField.setValue(scPackage.getDescription());
		packageDescriptionUpdateFieldORG.setText(scPackage.getDescription());
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().obtainJob(
				scPackage.getJob().getJobNumber(),
				new AsyncCallback<Job>() {

					public void onSuccess(Job aJob) {
						jobDescription.setText(aJob.getDescription());
					}

					public void onFailure(Throwable e) {
						UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
					}
				});

		if (scPackage.getVendorNo() != null) {
			subcontractorId.setValue(scPackage.getVendorNo().trim());
			subcontractorIdORG.setText(scPackage.getVendorNo().trim());
			SessionTimeoutCheck.renewSessionTimer();
			globalSectionController.getMasterListRepository().searchVendorList(
					scPackage.getVendorNo().trim(),
					new AsyncCallback<List<MasterListVendor>>() {

						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true, globalSectionController.getUser());
						}

						public void onSuccess(List<MasterListVendor> vendorList) {
							MasterListVendor matchedVendor = matchVendorNumber(
									subcontractorIdORG.getText(),
									vendorList);
							String vendorName = matchedVendor != null ? matchedVendor.getVendorName() : "";
							subcontractorName.setText(vendorName);
							subcontractorNameORG.setText(vendorName);
						}

					});
		} else {
			subcontractorId.setValue("");
			subcontractorIdORG.setText("");
		}

		if (scPackage.getOriginalSubcontractSum() != null)
			originalSCSumFieldORG.setText(amountRenderer.render(scPackage.getOriginalSubcontractSum().toString()));
		else
			originalSCSumFieldORG.setText("");

		originalSCSumField.setValue(originalSCSumFieldORG.getText());

		if (scPackage.getRemeasuredSubcontractSum() != null)
			remeasureSCSumFieldORG.setText(amountRenderer.render(scPackage.getRemeasuredSubcontractSum().toString()));
		else
			remeasureSCSumFieldORG.setText("");

		remeasureSCSumField.setValue(remeasureSCSumFieldORG.getText());

		if (scPackage.getApprovedVOAmount() != null)
			approvedVOFieldORG.setText(amountRenderer.render(scPackage.getApprovedVOAmount().toString()));
		else
			approvedVOFieldORG.setText("");

		approvedVOField.setValue(approvedVOFieldORG.getText());

		if (scPackage.getRetentionTerms() != null) {
			retentionTermsFieldORG.setText(scPackage.getRetentionTerms());
			retentionTermsComboBox.setValue(scPackage.getRetentionTerms());
		} else {
			retentionTermsFieldORG.setText("No Retention");
			retentionTermsComboBox.setValue("");
		}

		if (scPackage.getMaxRetentionPercentage() != null) {
			maxRetentionFieldORG.setText(scPackage.getMaxRetentionPercentage().toString());
			maxRetentionField.setValue(scPackage.getMaxRetentionPercentage().toString());
		} else {
			maxRetentionFieldORG.setText("");
			maxRetentionField.setValue("");
		}

		if (scPackage.getInterimRentionPercentage() != null) {
			interimRetentionFieldORG.setText(scPackage.getInterimRentionPercentage().toString());
			interimRetentionField.setValue(scPackage.getInterimRentionPercentage().toString());
		} else {
			interimRetentionFieldORG.setText("");
			interimRetentionField.setValue("");
		}

		if (scPackage.getMosRetentionPercentage() != null) {
			mosRetentionFieldORG.setText(scPackage.getMosRetentionPercentage().toString());
			mosRetentionField.setValue(scPackage.getMosRetentionPercentage().toString());
		} else {
			mosRetentionFieldORG.setText("");
			mosRetentionField.setValue("");
		}

		if (scPackage.getRetentionAmount() != null)
			retentionAmountFieldORG.setText(amountRenderer.render(scPackage.getRetentionAmount().toString()));
		else
			retentionAmountFieldORG.setText("");

		retentionAmountField.setValue(retentionAmountFieldORG.getText());

		if (scPackage.getRetentionReleased() != null)
			retentionReleasedFieldORG.setText(amountRenderer.render(scPackage.getRetentionReleased().toString()));
		else
			retentionReleasedFieldORG.setText("");

		retentionReleasedField.setValue(retentionReleasedFieldORG.getText());

		if (scPackage.getAccumlatedRetention() != null)
			accumlatedRetentionFieldORG.setText(amountRenderer.render(scPackage.getAccumlatedRetention().toString()));
		else
			accumlatedRetentionFieldORG.setText("");

		accumlatedRetentionField.setValue(accumlatedRetentionFieldORG.getText());

		if (scPackage.getPaymentCurrency() != null) {
			paymentCurrencyFieldORG.setText(scPackage.getPaymentCurrency());
			paymentCurrencyComboBox.setValue(scPackage.getPaymentCurrency());
		} else {
			paymentCurrencyFieldORG.setText("");
			paymentCurrencyComboBox.setValue("");
		}

		if (scPackage.getExchangeRate() != null) {
			exchangeRateFieldORG.setText(rateRenderer.render(scPackage.getExchangeRate().toString()));
			exchangeRateField.setValue(rateRenderer.render(scPackage.getExchangeRate().toString()));
		} else {
			exchangeRateFieldORG.setText("");
		}

		if (scPackage.getPaymentTerms() != null) {
			paymentTermsFieldORG.setText(scPackage.getPaymentTerms());
			paymentTermsComboBox.setValue(scPackage.getPaymentTerms());
		} else {
			paymentTermsFieldORG.setText("");
			paymentTermsComboBox.setValue("");
		}

		if (scPackage.getApprovalRoute() != null)
			approvalSubTypeFieldORG.setText(scPackage.getApprovalRoute());
		else
			approvalSubTypeFieldORG.setText("");

		approvalSubTypeField.setValue(approvalSubTypeFieldORG.getText());

		if (scPackage.getFormOfSubcontract() != null)
			formOfSubcontractFieldORG.setText(scPackage.getFormOfSubcontract());
		else
			formOfSubcontractFieldORG.setText("");

		formOfSubcontractComboBox.setValue(formOfSubcontractFieldORG.getText());

		if (scPackage.getInternalJobNo() != null)
			internalJobFieldORG.setText(scPackage.getInternalJobNo());
		else
			internalJobFieldORG.setText("");

		internalJobField.setValue(internalJobFieldORG.getText());
		
		cpfCalculationORG.setText(scPackage.getCpfCalculation()!=null?scPackage.getCpfCalculation():"");
		cpfBasePeriodORG.setText(scPackage.getCpfBasePeriod()!=null?scPackage.getCpfBasePeriod().toString():"");
		cpfBaseYearORG.setText(scPackage.getCpfBaseYear()!=null?scPackage.getCpfBaseYear().toString():"");
		
		cpfCalculationComboBox.setValue(scPackage.getCpfCalculation());
		cpfBasePeriodField.setValue(scPackage.getCpfBasePeriod());
		cpfBaseYearField.setValue(scPackage.getCpfBaseYear());
		
		if(SCPackage.CPF_SUBJECT_TO.equals(scPackage.getCpfCalculation())){
			cpfBasePeriodField.enable();
			cpfBaseYearField.enable();
		}
		
		this.scPackage = scPackage;
		isScPackageUpdated = false;

		UIUtil.unmaskPanelById(WINDOW_ID);
		doLayout();
	}

	private MasterListVendor matchVendorNumber(String vendorNo, List<MasterListVendor> vendorList) {
		if (vendorList != null && vendorList.size() > 0)
			for (int i = 0; i < vendorList.size(); i++)
				if (vendorNo.trim().equals(vendorList.get(i).getVendorNo().trim()))
					return vendorList.get(i);

		return null;

	}
	
	private String validateCPFCalculation(){
		String errorMsg = "";
		if(SCPackage.CPF_SUBJECT_TO.equals(cpfCalculationComboBox.getValue())){
			if(cpfBasePeriodField.getValueAsString().trim().length()==0 || cpfBaseYearField.getValueAsString().trim().length()==0){
				errorMsg = "Please input CPF Base Period and CPF Base Year for enabling CPF Calculation.";
			}
			
			if(cpfBaseYearField.getValueAsString().trim().length()!=4){
				errorMsg = "Please input 4 digits for CPF Base Year.";
			}
			if(cpfBasePeriodField.getValueAsString().trim().length()==0 
					|| cpfBasePeriodField.getValue().intValue()>12 
					|| cpfBasePeriodField.getValue().intValue()<1){
				errorMsg = "Please input 1 - 12 for CPF Base Period.";
			}
		}
		
		return errorMsg;
	}

	public void close() {
		super.close();
	}

}
