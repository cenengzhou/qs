package com.gammon.qs.client.ui.window;

import java.util.List;

import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.JobRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessJobsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Job;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
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

/**
 * @author tikywong
 * Refactored on May 29, 2013
 */
public class OpenJobWindow extends Window {

	private static final String OPEN_JOB_PANEL_ID = "openJobPanelId";

	private Panel mainPanel;

	private Panel searchJobPanel;
	private Panel searchPanel;
	private Button searchJobButton;
	private TextField searchJobNumberTextField;

	private Panel selectJobPanel;
	private Button openJobButton;
	private Button closeWindowButton;
	private GridPanel resultGridPanel;
	private RecordDef jobRecordDef;
	private GlobalSectionController globalSectionController;
	private GlobalMessageTicket globalMessageTicket;

	// Repository
	private JobRepositoryRemoteAsync jobRepository;
	private UserAccessJobsRepositoryRemoteAsync userAccessJobsRepository;
	// local job result
	private List<Job> jobs;

	public OpenJobWindow(final GlobalSectionController globalSectionController) {
		super();

		this.globalSectionController = globalSectionController;
		globalMessageTicket = new GlobalMessageTicket();

		// Initialize Repositories
		jobRepository = globalSectionController.getJobRepository();
		userAccessJobsRepository = globalSectionController.getUserAccessJobsRepository();

		setupUI();
	}

	private void setupUI() {
		setTitle("Job List");
		setPaddings(5);
		setWidth(660);
		setHeight(430);
		setLayout(new FitLayout());
		setClosable(false);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new RowLayout());
		mainPanel.setBorder(false);
		mainPanel.setId(OPEN_JOB_PANEL_ID);

		// Search Panel
		setupSearchPanel();
		mainPanel.add(searchPanel);

		// Grid Panel
		setupGridPanel();
		mainPanel.add(this.selectJobPanel);

		// Open Button
		openJobButton = new Button("Open");
		openJobButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				openSelectedJob();
			};
		});
		addButton(openJobButton);

		// Close Button
		closeWindowButton = new Button("Close");
		closeWindowButton.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalSectionController.closeCurrentWindow();
				globalSectionController.showMessageBoardMainPanel();
			};
		});
		addButton(closeWindowButton);

		add(mainPanel);

		addListener(new WindowListenerAdapter() {
			public void onActivate(Window source) {
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						OpenJobWindow.this.show();
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

		searchJobNumberTextField = new TextField("Job Number", "job_number", 200);
		searchJobNumberTextField.setValue(globalSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.DEFAULT_JOB));
		searchJobNumberTextField.addListener(new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					if (searchJobNumberTextField.getText().trim().length() == 0)
						MessageBox.alert("Tips <br/>"
								+ "- Blank searching keyword is NOT allowed <br/>"
								+ "- \"*\" letter can be used to define wildcards both before and after pattern (e.g. *slan* results \"Island\") <br/>"
								+ "- Single \"*\" letter can be used to load all available jobs");
					else
						populateGrid(searchJobNumberTextField.getText());
				}
			}
		});

		searchJobPanel.add(this.searchJobNumberTextField);

		searchJobButton = new Button("Search");
		ButtonListenerAdapter searchJobButtonActionListener = new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				globalMessageTicket.refresh();

				if (searchJobNumberTextField.getText().trim().length() == 0)
					MessageBox.alert("Tips <br/>"
							+ "- Blank searching keyword is NOT allowed <br/>"
							+ "- \"*\" letter can be used to define wildcards both before and after pattern (e.g. *slan* results \"Island\") <br/>"
							+ "- Single \"*\" letter can be used to load all available jobs");
				else
					populateGrid(searchJobNumberTextField.getText());

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

		jobRecordDef = new RecordDef(new FieldDef[] { new StringFieldDef("index"),
														new StringFieldDef("jobNumber"),
														new StringFieldDef("description") });

		ColumnConfig[] jobResultGridCoulmnConfigs = new ColumnConfig[] {
				new ColumnConfig("Job Number", "jobNumber", 20, true),
				new ColumnConfig("Description", "description", 100, true) };

		resultGridPanel = new GridPanel();
		resultGridPanel.setFrame(true);
		resultGridPanel.setColumnModel(new ColumnModel(jobResultGridCoulmnConfigs));

		resultGridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				openSelectedJob();
			}
		});

		MemoryProxy proxy = new MemoryProxy(new Object[][] {});
		ArrayReader reader = new ArrayReader(jobRecordDef);
		Store store = new Store(proxy, reader);
		store.load();
		resultGridPanel.setStore(store);

		GridView gridView = new GridView();
		gridView.setForceFit(true);
		resultGridPanel.setView(gridView);

		selectJobPanel.add(resultGridPanel);
	}

	public void populateGrid(String jobNumber) {
		UIUtil.maskPanelById(OPEN_JOB_PANEL_ID, "Fetching item...", true);
		resultGridPanel.getStore().removeAll();
		SessionTimeoutCheck.renewSessionTimer();
		jobRepository.getJobListByJobNumber(jobNumber, new AsyncCallback<List<Job>>() {
			public void onSuccess(List<Job> returnedJobs) {

				jobs = returnedJobs;
				Record[] records = new Record[returnedJobs.size()];

				for (int i = 0; i < returnedJobs.size(); i++){
					Job curJob = returnedJobs.get(i);
					records[i] = jobRecordDef.createRecord(new Object[] { i + "", curJob.getJobNumber().toString(), curJob.getDescription() });
				}

				resultGridPanel.getStore().add(records);
				UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				jobs = null;
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
			}

		});

	}
	
	private void openSelectedJob() {
		UIUtil.maskPanelById(OPEN_JOB_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		globalMessageTicket.refresh();

		String rateDecimal = globalSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		String amountDecimal = globalSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
		String qtyDecimal = globalSectionController.getUser().getGeneralPreferences().get(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);

		globalSectionController.getUser().getGeneralPreferences().put(GeneralPreferencesKey.RATE_DECIMAL_PLACES, rateDecimal == null || rateDecimal.trim().equals("") ? "2" : rateDecimal);
		globalSectionController.getUser().getGeneralPreferences().put(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES, amountDecimal == null || amountDecimal.trim().equals("") ? "2" : amountDecimal);
		globalSectionController.getUser().getGeneralPreferences().put(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES, qtyDecimal == null || qtyDecimal.trim().equals("") ? "3" : qtyDecimal);

		globalSectionController.saveGeneralPreference();
		if (resultGridPanel.getSelectionModel().getSelected() != null) {
			int index = resultGridPanel.getSelectionModel().getSelected().getAsInteger("index");

			if (jobs != null && jobs.size() > 0) {
				final Job job = jobs.get(index);
				SessionTimeoutCheck.renewSessionTimer();
				userAccessJobsRepository.canAccessJob(globalSectionController.getUser().getUsername(), job.getJobNumber(), new AsyncCallback<Boolean>() {
					public void onFailure(Throwable e) {
						UIUtil.alert("Failed: Unable to authorize User: " + globalSectionController.getUser().getUsername() + " Job: " + job.getJobNumber() + ".");
						UIUtil.throwException(e);
						UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
					}

					public void onSuccess(Boolean canAccessJob) {
						if (canAccessJob) {
							// Get Current Repackaging Status
							SessionTimeoutCheck.renewSessionTimer();
							jobRepository.getCurrentRepackagingStatusByJobNumber(job.getJobNumber(), new AsyncCallback<String>() {
								public void onSuccess(String result) {
									globalSectionController.setCurrentRepackagingStatus(result);
								}

								public void onFailure(Throwable e) {
									UIUtil.alert("Failed: Unable to obtain Repackaging Type of Job: " + job.getJobNumber());
									UIUtil.throwException(e);
								}
							});
							SessionTimeoutCheck.renewSessionTimer();
							jobRepository.obtainJob(job.getJobNumber(), new AsyncCallback<Job>() {
								public void onSuccess(Job returnedJob) {
									globalSectionController.setJob(returnedJob);
									
									globalSectionController.refreshUnawardedPackageStore();
									globalSectionController.refreshAwardedPackageStore();
									globalSectionController.refreshUneditablePackageNos();
									globalSectionController.refreshAwardedPackageNos();
////
									globalSectionController.refreshTreeSectionPanels(returnedJob);
//									
									UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
									globalSectionController.closeCurrentWindow();
									globalSectionController.showMessageBoardMainPanel();
								}

								public void onFailure(Throwable e) {
									UIUtil.alert("Failed: Uable to obtain Job Header Information for Job: " + job.getJobNumber());
									UIUtil.throwException(e);
									UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
								}
							});
						}
						else { // User does not have access to job
							UIUtil.alert("User does not have access to this job");
							UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
						}
					}
				});

			} else
				UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);
		} else
			UIUtil.unmaskPanelById(OPEN_JOB_PANEL_ID);

	}

}
