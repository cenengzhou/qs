package com.gammon.qs.client.ui.panel.mainSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.factory.FieldFactory;
import com.gammon.qs.client.ui.ArrowKeyNavigation;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
//import com.gammon.qs.client.ui.gridView.GammonGridView;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.client.ui.renderer.DateTimeRenderer;
//import com.gammon.qs.client.ui.renderer.EditableColorRenderer;
//import com.gammon.qs.client.ui.util.DateUtil;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
//import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
//import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
//import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
//import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
//import com.gwtext.client.widgets.grid.event.GridListener;
//import com.gwtext.client.widgets.grid.event.GridRowListener;
//import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;


@SuppressWarnings("unchecked")
public class SchedulerEditorGridPanel extends EditorGridPanel implements PreferenceSetting {

	private String screenName = "Scheduler-Editor-Grid";
	private Store dataStore;
	private GlobalSectionController globalSectionController;
	private ColumnConfig[] columns;
	private final RecordDef schedulerTriggerRecordDef = new RecordDef(
			new FieldDef[] {
					new StringFieldDef("triggerName"),
					new StringFieldDef("tiggerGroup"),					
					new StringFieldDef("jobName"),
					new StringFieldDef("jobGroup"),

					new StringFieldDef("description"),					
					new DateFieldDef("nextFireTime"),
					new DateFieldDef("prevFireTime"),
					new StringFieldDef("priority"),					
					new StringFieldDef("triggerState"),

					new StringFieldDef("tiggerType"),
					new DateFieldDef("startTime"),
					new DateFieldDef("endTime"),
					new StringFieldDef("calendarName"),
					new StringFieldDef("misfireInstr"),

					new StringFieldDef("updateCron")
				}
			);
	
	private GlobalMessageTicket globalMessageTicket;
	@SuppressWarnings("unused")
	private ArrowKeyNavigation arrowKeyNavigation;
	
	
	public String getScreenName() {
		return screenName;
	}
	
	public SchedulerEditorGridPanel(final GlobalSectionController globalSectionCtr){
		globalSectionController= globalSectionCtr;
		globalMessageTicket = new GlobalMessageTicket();
		this.arrowKeyNavigation = new ArrowKeyNavigation(this);
		globalMessageTicket = new GlobalMessageTicket();
		this.setTrackMouseOver(true);
		this.setBorder(false);
		this.setAutoScroll(true);
		
		@SuppressWarnings("unused")
		ColumnConfig tiggerGroupColumn = new ColumnConfig("Trigger Group","tiggerGroup",70,false);
		@SuppressWarnings("unused")
		ColumnConfig jobGroupColumn = new ColumnConfig("Job Group","jobGroup",70,false);
		@SuppressWarnings("unused")
		ColumnConfig isVolatileColumn = new ColumnConfig("Volatile","isVolatile",70,false);
		@SuppressWarnings("unused")
		ColumnConfig priorityColumn = new ColumnConfig("Priority","priority",70,false);
		@SuppressWarnings("unused")
		ColumnConfig tiggerTypeColumn = new ColumnConfig("Trigger Type","tiggerType",70,false);
		@SuppressWarnings("unused")
		ColumnConfig calendarNameColumn = new ColumnConfig("Calendar Name","calendarName",70,false);
		@SuppressWarnings("unused")
		ColumnConfig misfireInstrColumn = new ColumnConfig("Miss Fired","misfireInstr",70,false);

		ColumnConfig triggerNameColumn = new ColumnConfig("Trigger Name","triggerName",215,true);
		triggerNameColumn.setCss("overflow:hidden !Important;");
		ColumnConfig jobNameColumn = new ColumnConfig("Job Name","jobName",230,false);
		jobNameColumn.setCss("overflow:hidden !Important;");
		ColumnConfig descriptionColumn = new ColumnConfig("Description","description",230,false);
		descriptionColumn.setCss("overflow:hidden !Important;");
		ColumnConfig nextFireTimeColumn = new ColumnConfig("Next Fire","nextFireTime",75,false);
		nextFireTimeColumn.setCss("overflow:hidden !Important;");
		ColumnConfig prevFireTimeColumn = new ColumnConfig("Previous Fire","prevFireTime",75,false,new DateTimeRenderer());
		prevFireTimeColumn.setCss("overflow:hidden !Important;");
		ColumnConfig triggerStateColumn = new ColumnConfig("Trigger State","triggerState",75,false);
		triggerStateColumn.setCss("overflow:hidden !Important;");
		ColumnConfig startTimeColumn = new ColumnConfig("Start Time","startTime",75,false,new DateTimeRenderer());
		startTimeColumn.setCss("overflow:hidden !Important;");
		ColumnConfig endTimeColumn = new ColumnConfig("End Time","endTime",75,false,new DateTimeRenderer());
		endTimeColumn.setCss("overflow:hidden !Important;");
		ColumnConfig updateCronColumn = new ColumnConfig("Update Cron","updateCron",75,false);
		updateCronColumn.setCss("overflow:hidden !Important;");
		SimpleStore triggerStateStore = new SimpleStore("triggerState", QrtzTriggers.TRIGGER_STATE_LIST);
		triggerStateStore.load();

        ComboBox triggerStateComboBox = new ComboBox();
        triggerStateComboBox.setDisplayField("triggerState");
        triggerStateComboBox.setStore(triggerStateStore);
		triggerStateColumn.setEditor(new GridEditor(triggerStateComboBox));
        
		nextFireTimeColumn.setRenderer(new DateTimeRenderer());
		nextFireTimeColumn.setEditor(new GridEditor(FieldFactory.createDateTimeFieldAllowNull()));
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(schedulerTriggerRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		this.setStore(this.dataStore);
		columns = new ColumnConfig[] {
				triggerNameColumn,
				descriptionColumn,
				triggerStateColumn,
				jobNameColumn,
				startTimeColumn,
				prevFireTimeColumn,
				nextFireTimeColumn,
				endTimeColumn,
				updateCronColumn
		};
		/*final*/ ColumnConfig[] customizedColumns = globalSectionCtr.applyScreenPreferences(this.getScreenName(), columns);
		this.setColumnModel(new ColumnModel(customizedColumns));	
//		GridView view = new GammonGridView();
//		view.setAutoFill(false);
//		view.setForceFit(false);
//		this.setView(view);
		addEditorGridListener(new EditorGridListenerAdapter(){
			public boolean doBeforeEdit(GridPanel grid, Record record,
					String field, Object value, int rowIndex, int colIndex) {
				globalMessageTicket.refresh();
				if ("nextFireTime".equals(field))
					return true;
				if ("triggerState".equals(field))
					return true;
				return false;
			}
		});
		Toolbar toolbar = new Toolbar();
		ToolbarButton saveButton = new ToolbarButton("Save");
		saveButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				globalMessageTicket.refresh();
				List<QrtzTriggers> updatedQrtzList = new ArrayList<QrtzTriggers>();
				for (Record record :dataStore.getRecords())
					if (record.isDirty()){
						QrtzTriggers updateQrtz = new QrtzTriggers();
						updateQrtz.setTriggerName(record.getAsString("triggerName"));
						updateQrtz.setTriggerGroup(record.getAsString("tiggerGroup"));
						updateQrtz.setJobName(record.getAsString("jobName"));
						updateQrtz.setJobGroup(record.getAsString("jobGroup"));

						updateQrtz.setDescription(record.getAsString("description"));
						if (record.getAsString("nextFireTime")!=null && !"".equals(record.getAsString("nextFireTime").trim()))
							updateQrtz.setNextFireTime(record.getAsDate("nextFireTime").getTime());
						if (record.getAsString("prevFireTime")!=null && !"".equals(record.getAsString("prevFireTime").trim()))
							updateQrtz.setPrevFireTime(record.getAsDate("prevFireTime").getTime());
						if (record.getAsString("priority")!=null && !"".equals(record.getAsString("priority").trim()))
							updateQrtz.setPriority(Long.parseLong(record.getAsString("priority")));
						updateQrtz.setTriggerState(record.getAsString("triggerState"));

						updateQrtz.setTiggerType(record.getAsString("tiggerType"));
						if (record.getAsString("startTime")!=null && !"".equals(record.getAsString("startTime").trim()))
							updateQrtz.setStartTime(record.getAsDate("startTime").getTime());
						if (record.getAsString("endTime")!=null && !"".equals(record.getAsString("endTime").trim()))
							updateQrtz.setEndTime(record.getAsDate("endTime").getTime());
						updateQrtz.setCalendarName(record.getAsString("calendarName"));
						if (record.getAsString("misfireInstr")!=null && !"".equals(record.getAsString("misfireInstr").trim()))
							updateQrtz.setMisfireInstr(Long.parseLong(record.getAsString("misfireInstr")));
						updatedQrtzList.add(updateQrtz);
					}
				if (updatedQrtzList.size()>0){
					SessionTimeoutCheck.renewSessionTimer();
					globalSectionCtr.getQrtzTriggerServiceRepository().updateQrtzTriggerList(updatedQrtzList,new AsyncCallback<String>(){

						public void onFailure(Throwable e) {
							UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
						}

						public void onSuccess(String errorMsg) {
							if (errorMsg!=null && errorMsg.trim().length()>0)
								MessageBox.alert(errorMsg);
							else{
								MessageBox.alert("Update Successfully!");
								dataStore.commitChanges();
							}
							
						}
						
					});
				}
			}
		});
		addGridCellListener(new GridCellListenerAdapter(){

			public void onCellDblClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
				if ("updateCron".equals(grid.getColumnModel().getDataIndex(colindex)))
					globalSectionCtr.openCronTriggerWindows(dataStore.getRecordAt(rowIndex).getAsString("triggerName"),dataStore.getRecordAt(rowIndex).getAsString("tiggerGroup"));
			}
		});
		ToolTip saveToolTip = new ToolTip();
		saveToolTip.setTitle("Save Changes");
		saveToolTip.setHtml("Save the changes made on the grid");
		saveToolTip.setDismissDelay(15000);
		saveToolTip.setWidth(200);
		saveToolTip.setTrackMouse(true);
		saveToolTip.applyTo(saveButton);
		toolbar.addButton(saveButton);
		setTopToolbar(toolbar);
	}
	
	public void populate(List<QrtzTriggers> qrtz){
		dataStore.removeAll();
		globalMessageTicket.refresh();
		Record[] records = new Record[qrtz.size()];
		for (int i = 0; i < qrtz.size() ; i ++){
			Date nextFireDate = new Date();
			nextFireDate.setTime(qrtz.get(i).getNextFireTime()!=null?(qrtz.get(i).getNextFireTime()):0);
			Date prevFireDate = new Date();
			prevFireDate.setTime(qrtz.get(i).getPrevFireTime()!=null?(qrtz.get(i).getPrevFireTime()):0);
			Date startDate = new Date();
			startDate.setTime(qrtz.get(i).getStartTime()!=null?(qrtz.get(i).getStartTime()):0);
			Date endDate = new Date();
			endDate.setTime(qrtz.get(i).getEndTime()!=null?(qrtz.get(i).getEndTime()):0);

			records[i] = this.schedulerTriggerRecordDef.createRecord(new Object[]{
					(qrtz.get(i).getTriggerName()!=null?qrtz.get(i).getTriggerName():""),
					(qrtz.get(i).getTriggerGroup()!=null?qrtz.get(i).getTriggerGroup():""),
					(qrtz.get(i).getJobName()!=null?qrtz.get(i).getJobName():""),
					(qrtz.get(i).getJobGroup()!=null?qrtz.get(i).getJobGroup():""),
					(qrtz.get(i).getDescription()!=null?qrtz.get(i).getDescription():""),
					qrtz.get(i).getNextFireTime()!=null?nextFireDate:"",
					qrtz.get(i).getPrevFireTime()!=null?prevFireDate:"",
					(qrtz.get(i).getPriority()!=null?qrtz.get(i).getPriority():""),
					(qrtz.get(i).getTriggerState()!=null?qrtz.get(i).getTriggerState():""),
					(qrtz.get(i).getTiggerType()!=null?qrtz.get(i).getTiggerType():""),
					qrtz.get(i).getStartTime()!=null?startDate:"",
					qrtz.get(i).getEndTime()!=null?endDate:"",
					(qrtz.get(i).getCalendarName()!=null?qrtz.get(i).getCalendarName():""),
					(qrtz.get(i).getMisfireInstr()!=null?qrtz.get(i).getMisfireInstr():""),
					"Click Here"
			});
		}
		this.dataStore.add(records);
	}
}
