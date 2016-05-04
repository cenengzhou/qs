package com.gammon.qs.client.ui.window.mainSection;

import java.util.ArrayList;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserAccessJobsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.PaymentCertificateEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.TableLayout;

public class JobSelectionWindow extends Window {

	private static final String JOB_SELECTION_ID = "jobSelectionId";

	private Panel mainPanel;

	private Panel searchJobPanel;
	private Panel searchPanel;
	private Button searchJobButton;
	private TextField searchJobNumberTextField;
	// private TextField jobDivisionTextField;
	private ComboBox jobDivisionComboBox = new ComboBox();

	private Store dataStore;

	private Panel selectJobPanel;
	private Button selectJobButton;
	private Button closeWindowButton;
	private GridPanel resultGridPanel;
	private RecordDef jobRecordDef;

	private static final String JOB_DIVISION_VALUE = "jobDivisionValue";
	private static final String JOB_DIVISION_DISPLAY = "jobDivisionDisplay";
	private String[][] jobDivisions = new String[][] {};
	private Store jobDivisionStore = new SimpleStore(new String[] { JOB_DIVISION_VALUE, JOB_DIVISION_DISPLAY }, jobDivisions);

	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;
	private PaymentCertificateEnquiryMainPanel paymentCertificateEnquiryMainPanel;

	// Repository
	private UserAccessJobsRepositoryRemoteAsync userAccessJobsRepository;
	// local job result
	private String jobNumber;
	@SuppressWarnings("unused")
	private List<Job> jobList = new ArrayList<Job>(); 

	public JobSelectionWindow(PaymentCertificateEnquiryMainPanel paymentCertificateEnquiryMainPanel, GlobalSectionController globalSectionController) {
		super();

		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();
		this.paymentCertificateEnquiryMainPanel = paymentCertificateEnquiryMainPanel;
		// Initialize Repositories
		userAccessJobsRepository = globalSectionController.getUserAccessJobsRepository();

		obtainJobDivisionValue();
		setupUI();
	}

	private void setupUI() {
		setTitle("Job List");
		setPaddings(5);
		setWidth(660);
		setHeight(430);
		setLayout(new FitLayout());

		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setBorder(false);
		mainPanel.setId(JOB_SELECTION_ID);

		// Search Panel
		setupSearchPanel();
		mainPanel.add(searchPanel);

		// Grid Panel
		setupGridPanel();
		mainPanel.add(this.selectJobPanel);

		// Open Button
		selectJobButton = new Button("Select");
		selectJobButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				SessionTimeoutCheck.renewSessionTimer();
				userAccessJobsRepository.canAccessJob(globalSectionController.getUser().getUsername(), jobNumber, new AsyncCallback<Boolean>() {

					public void onSuccess(Boolean result) {
						if (result) {
							paymentCertificateEnquiryMainPanel.getJobNoTextField().setValue(jobNumber);
							JobSelectionWindow.this.close();
						} else {
							UIUtil.alert("User does not have access to this job");
							UIUtil.unmaskPanelById(JOB_SELECTION_ID);
						}
					}

					public void onFailure(Throwable caught) {
						UIUtil.alert("Failed: Unable to authorize User: " + globalSectionController.getUser().getUsername() + " Job: " + jobNumber + ".");
						UIUtil.throwException(caught);
						UIUtil.unmaskPanelById(JOB_SELECTION_ID);
					}
				});

			};
		});
		addButton(selectJobButton);

		// Close Button
		closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				JobSelectionWindow.this.close();
				// globalSectionController.showMessageBoardMainPanel();
			};
		});
		addButton(closeWindowButton);

		add(mainPanel);

		addListener(new WindowListenerAdapter() {
			public void onActivate(Window source) {
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						JobSelectionWindow.this.show();
						searchJobNumberTextField.focus();
						if (searchJobNumberTextField.getText().length() > 0) {
							searchJobButton.fireEvent("click");
						}
					};
				});

			}

		});

	}

	private void setupSearchPanel() {
		searchPanel = new Panel();
		searchPanel.setLayout(new HorizontalLayout(3));

		searchJobPanel = new FormPanel();
		searchJobPanel.setPaddings(10);
		searchJobPanel.setBorder(false);
		searchJobPanel.setLayout(new TableLayout(4));

		FieldListenerAdapter fieldListenerAdapter = new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					if ((searchJobNumberTextField.getText() == null && jobDivisionComboBox.getValue() == null) || (searchJobNumberTextField.getText().trim().length() == 0 && jobDivisionComboBox.getValue() == null))
						MessageBox.alert("Tips <br/>" + "- Blank searching keyword is NOT allowed <br/>" + "- \"*\" letter can be used to define wildcards both before and after pattern (e.g. *slan* results \"Island\") <br/>" + "- Single \"*\" letter can be used to load all available jobs");
					else
						searchJob(searchJobNumberTextField.getText(), jobDivisionComboBox.getValue() != null ? jobDivisionComboBox.getValue().toUpperCase() : null);
				}
			}
		};

		Label jobNumberLabel = new Label("Job Number/Name");
		jobNumberLabel.setCtCls("table-cell");

		searchJobNumberTextField = new TextField("Job Number/Name", "job_number", 100);
		// searchJobNumberTextField.setValue(globalSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.DEFAULT_JOB));
		searchJobNumberTextField.addListener(fieldListenerAdapter);

		searchJobPanel.add(jobNumberLabel);
		searchJobPanel.add(this.searchJobNumberTextField);

		Label jobDivisionLabel = new Label("Job Division");
		jobDivisionLabel.setCtCls("table-cell");

		jobDivisionComboBox.setCtCls("table-cell");
		jobDivisionComboBox.setForceSelection(true);
		jobDivisionComboBox.setDisplayField(JOB_DIVISION_DISPLAY);
		jobDivisionComboBox.setValueField(JOB_DIVISION_VALUE);
		jobDivisionComboBox.setStore(jobDivisionStore);
		jobDivisionComboBox.setWidth(100);
		jobDivisionComboBox.setEmptyText("ALL");
		jobDivisionComboBox.setSelectOnFocus(true);
		jobDivisionComboBox.setTypeAhead(true);
		jobDivisionComboBox.addListener(fieldListenerAdapter);

		searchJobPanel.add(jobDivisionLabel);
		// searchJobPanel.add(this.jobDivisionTextField);
		searchJobPanel.add(this.jobDivisionComboBox);

		searchJobButton = new Button("Search");
		ButtonListenerAdapter searchJobButtonActionListener = new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();

				if ((searchJobNumberTextField.getText() == null && jobDivisionComboBox.getValue() == null) || (searchJobNumberTextField.getText().trim().length() == 0 && jobDivisionComboBox.getValue() == null))
					MessageBox.alert("Tips <br/>" + "- Blank searching keyword is NOT allowed <br/>" + "- \"*\" letter can be used to define wildcards both before and after pattern (e.g. *slan* results \"Island\") <br/>" + "- Single \"*\" letter can be used to load all available jobs");
				else
					searchJob((searchJobNumberTextField.getText() != null ? searchJobNumberTextField.getText() : "*"), (jobDivisionComboBox.getValue() != null ? jobDivisionComboBox.getValue().toUpperCase() : null));

			};
		};
		searchJobButton.addListener(searchJobButtonActionListener);

		searchPanel.add(searchJobPanel);
		searchPanel.add(searchJobButton);
	}

	private void setupGridPanel() {
		selectJobPanel = new Panel();
		selectJobPanel.setBorder(true);
		selectJobPanel.setFrame(true);
		selectJobPanel.setPaddings(5);
		selectJobPanel.setHeight(300);
		selectJobPanel.setLayout(new FitLayout());

		jobRecordDef = new RecordDef(new FieldDef[] { new StringFieldDef("index"), new StringFieldDef("jobNumber"), new StringFieldDef("description"), new StringFieldDef("jobDivision") });

		ColumnConfig[] jobResultGridCoulmnConfigs = new ColumnConfig[] { new ColumnConfig("Job Number", "jobNumber", 20, true), new ColumnConfig("Description", "description", 80, true), new ColumnConfig("Job Division", "jobDivision", 20, true) };

		resultGridPanel = new GridPanel();
		resultGridPanel.setFrame(true);
		resultGridPanel.setColumnModel(new ColumnModel(jobResultGridCoulmnConfigs));

		resultGridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				jobNumber = dataStore.getAt(rowIndex).getAsString("jobNumber");
				paymentCertificateEnquiryMainPanel.getJobNoTextField().setValue(jobNumber);
				JobSelectionWindow.this.close();
			}

			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				jobNumber = dataStore.getAt(rowIndex).getAsString("jobNumber");
			}
		});

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(jobRecordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();
		resultGridPanel.setStore(dataStore);

		GridView gridView = new GridView();
		gridView.setForceFit(true);
		resultGridPanel.setView(gridView);

		selectJobPanel.add(resultGridPanel);
	}

	public void populateGrid(List<Job> returnedJobs) {
		resultGridPanel.getStore().removeAll();
		
		Record[] records = new Record[returnedJobs.size()];
		if (returnedJobs != null && returnedJobs.size() > 0) {
			for (int i = 0; i < returnedJobs.size(); i++) {
				Job curJob = returnedJobs.get(i);
				records[i] = jobRecordDef.createRecord(new Object[] { i + "", curJob.getJobNumber().toString(), curJob.getDescription(), curJob.getDivision() });
			}

			resultGridPanel.getStore().add(records);
		}
	}

	public void searchJob(String jobNumber, String division) {
		UIUtil.maskPanelById(JOB_SELECTION_ID, "Fetching item...", true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().obtainJobListByDivision(jobNumber, division, new AsyncCallback<List<Job>>() {
			public void onSuccess(List<Job> returnedJobs) {
				jobList = returnedJobs;
				populateGrid(returnedJobs);
				UIUtil.unmaskPanelById(JOB_SELECTION_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(JOB_SELECTION_ID);
			}

		});
	}

	private void obtainJobDivisionValue() {
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getJobRepository().obtainAllJobDivision(new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> result) {
				jobDivisionStore.removeAll();

				if (result == null)
					return;

				RecordDef jobRecordDef = new RecordDef(new FieldDef[] { new StringFieldDef(JOB_DIVISION_VALUE), new StringFieldDef(JOB_DIVISION_DISPLAY) });

				jobDivisions = new String[result.size() + 1][2];
				jobDivisions[0][0] = null;
				jobDivisions[0][1] = "ALL";
				jobDivisionStore.add(jobRecordDef.createRecord(jobDivisions[0]));

				for (int i = 0; i < result.size(); i++) {
					jobDivisions[i + 1][0] = (String) result.get(i);
					jobDivisions[i + 1][1] = (String) result.get(i);
					jobDivisionStore.add(jobRecordDef.createRecord(jobDivisions[i + 1]));
				}

				jobDivisionComboBox.setListWidth(300);
				jobDivisionStore.commitChanges();
			}

			public void onFailure(Throwable caught) {
				UIUtil.throwException(caught);
			}
		});
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

}
