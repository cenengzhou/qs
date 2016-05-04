
package com.gammon.qs.client.ui.panel.mainSection;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.gridView.CustomizedGridView;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;

/**
 * @author koeyyeung
 *	created on 06 June, 2013
 */
public class EmailNotificationInfoGridPanel extends EditorGridPanel{
	private static final String EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID = "EmailNotificationInfoGridPanel_ID";


	private static final String USERNAME_RECORD_NAME = "usernameRecordName";
	private static final String EMAIL_SERVER_RECORD_NAME = "emailServerRecordName";

	private RecordDef recordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef(USERNAME_RECORD_NAME),
					new StringFieldDef(EMAIL_SERVER_RECORD_NAME)
			});

	private Store dataStore;

	private GlobalSectionController globalSectionController;

	public EmailNotificationInfoGridPanel(GlobalSectionController globalSectionController) {
		super();
		this.globalSectionController = globalSectionController;

		setupUI();

	}

	private void setupUI() {
		//setTitle("Email Notification Information");
		setBorder(false);
		setFrame(false);
		setPaddings(0);
		setAutoScroll(true);
		setView(new CustomizedGridView());

		setupGridPanel();

		setId(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID);
	}

	private void setupToolbar() {
		Toolbar toolbar = new Toolbar();

		ToolbarButton updateButton = new ToolbarButton("Update");
		updateButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				update();
			}
		});

		toolbar.addButton(updateButton);

		this.setTopToolbar(toolbar);
	}


	private void setupGridPanel() {
		setupToolbar();

		ColumnConfig recipientEmailAddressValueColumnConfig = new ColumnConfig("Recipient Email Address", USERNAME_RECORD_NAME, 150, false);
		recipientEmailAddressValueColumnConfig.setEditor(new GridEditor(new TextField()));
		ColumnConfig emailServerColumnConfig = new ColumnConfig("", EMAIL_SERVER_RECORD_NAME, 300, false);

		ColumnConfig[] cloumns = new ColumnConfig[]{
				recipientEmailAddressValueColumnConfig,
				emailServerColumnConfig
		};
		setColumnModel(new ColumnModel(cloumns));

		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(recordDef);
		dataStore = new Store(proxy, reader);
		dataStore.load();

		dataStore.setSortInfo(new SortState(USERNAME_RECORD_NAME, SortDir.ASC));
		setStore(dataStore);

		populateGrid();
	}

	private void populateGrid(){
		UIUtil.maskPanelById(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPropertiesRepository().obtainMailReceiverAddress(new AsyncCallback<String>() {
			public void onSuccess(String result) {

				dataStore.removeAll();
				if (result == null) {
					MessageBox.alert("No data was found.");
					return;
				}
				/*Record[] records = new Record[1];
				records[0] = recordDef.createRecord(new Object[] {result.getPropertyValue()});*/
				String username = result.substring(0, result.indexOf("@"));
				String emailServer = result.substring(result.indexOf("@"), result.length());
				Record record = recordDef.createRecord(new Object[] {username, emailServer});

				dataStore.add(record);
				UIUtil.unmaskPanelById(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID);
			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID);
			}
		});
	}

	private void update() {
		Record[] editedRecords = dataStore.getModifiedRecords();

		if(editedRecords.length==0){
			MessageBox.alert("No data has been changed.");
			return;
		}

		for(int i = 0; i < editedRecords.length; i++){
			if (editedRecords[i].getAsString(USERNAME_RECORD_NAME)==null || "".equals(editedRecords[i].getAsString(USERNAME_RECORD_NAME))){
				MessageBox.alert("Please fill in all the values.");
				return;
			}
		}

		String mailAddress = editedRecords[0].getAsString(USERNAME_RECORD_NAME).concat(editedRecords[0].getAsString(EMAIL_SERVER_RECORD_NAME));

		UIUtil.maskPanelById(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPropertiesRepository().updateMailReceiverAddress(mailAddress, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
				UIUtil.unmaskPanelById(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID);
			}
			public void onSuccess(Boolean updated) {
				if(updated){
					dataStore.commitChanges();
					populateGrid();
					MessageBox.alert("Record has been updated successfully.");
				}else
					MessageBox.alert("Update record failed.");

				UIUtil.unmaskPanelById(EMAIL_NOTIFICATION_INFO_GRID_PANEL_ID);
			}
		});
	}

}
