/**
 * 
 */
package com.gammon.qs.client.ui.panel.mainSection;


import java.util.Date;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.panel.PreferenceSetting;
import com.gammon.qs.domain.TransitHeader;
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
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;

/**
 * @author briantse
 * @Create_Date Feb 28, 2011
 */
@SuppressWarnings("unchecked")
public class TransitStatusGridPanel extends EditorGridPanel implements PreferenceSetting{

	private String screenName = "transit-status-editor-grid";
	
	private GlobalSectionController globalSectionController;
	
	private Store dataStore;
	
	@SuppressWarnings("unused")
	private GlobalMessageTicket globalMessageTicket;
	
	private final RecordDef transitStatusRecordDef = new RecordDef(
			new FieldDef[]{
					new StringFieldDef("jobNumber"),
					new StringFieldDef("estimateNumber"),
					new StringFieldDef("jobDescription"),
					new StringFieldDef("company"),
					new StringFieldDef("status"),
					new StringFieldDef("matchingCode"),
					new StringFieldDef("lastModifiedUser"),
					new StringFieldDef("lastModifiedDate")
			}
	);

	// Toolbar
	private Toolbar toolbar;
	private Checkbox filterCompletedCheckbox;
	private ToolbarButton filterButton;
	private ToolbarTextItem filterLabel;
	
	// constructor
	public TransitStatusGridPanel(final GlobalSectionController globalSectionController, boolean checked){
		super();
		
		globalMessageTicket = new GlobalMessageTicket();
		this.globalSectionController = globalSectionController;
		
		toolbar = new Toolbar();
		toolbar.setHeight(30);
		
		filterCompletedCheckbox = new Checkbox();
		filterCompletedCheckbox.setChecked(checked);
		toolbar.addField(filterCompletedCheckbox);
		filterLabel = new ToolbarTextItem("Show all Status");
		toolbar.addItem(filterLabel);
		
		//Filter's Button
		filterButton = new ToolbarButton();
		filterButton.setIconCls("filter-icon");
		filterButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				filter();
			}
		});
		toolbar.addButton(filterButton);
		
		//Toolbar - Filter Button's Tips
		ToolTip filterButtonToolTip = new ToolTip();
		filterButtonToolTip.setTitle("Filter");
		filterButtonToolTip.setHtml("Show all Status including completed");
		filterButtonToolTip.applyTo(filterButton);
		
		this.setTopToolbar(toolbar);
		
		ColumnConfig jobNumberColumn = new ColumnConfig("Job Number", "jobNumber", 80, false);
		ColumnConfig estimateNumberColumn = new ColumnConfig("Estimate Number", "estimateNumber", 90, false);
		ColumnConfig jobDescirptionColumn = new ColumnConfig("Job Description", "jobDescription", 180, false);
		ColumnConfig companyColumn = new ColumnConfig("Company", "company", 90, false);
		ColumnConfig statusColumn = new ColumnConfig("Status", "status", 120, false);
		ColumnConfig matchingCodeColumn = new ColumnConfig("Matching Code", "matchingCode", 90, false);
		ColumnConfig lastmodifiedUserColumn = new ColumnConfig("Last Modified User", "lastModifiedUser", 100, false);
		ColumnConfig lastmodifiedDateColumn = new ColumnConfig("Last Modified Date", "lastModifiedDate", 100, false);
		
		ColumnConfig[] columns = new ColumnConfig[]{
				jobNumberColumn,
				estimateNumberColumn,
				jobDescirptionColumn,
				companyColumn,
				statusColumn,
				matchingCodeColumn,
				lastmodifiedUserColumn,
				lastmodifiedDateColumn	
		};
		
		ColumnConfig[] customizedColumns = globalSectionController.applyScreenPreferences(this.getScreenName(), columns);
		this.setColumnModel(new ColumnModel(customizedColumns));
		
		
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		ArrayReader reader = new ArrayReader(this.transitStatusRecordDef);
		this.dataStore = new Store(proxy, reader);
		this.dataStore.load();
		this.dataStore.setSortInfo(new SortState("jobNumber",SortDir.ASC));
		this.setStore(this.dataStore);
	}
	
	
	public void populateGrid(List<TransitHeader> headersList){
		this.dataStore.removeAll();
		
		if(headersList == null){
			MessageBox.alert("No Transit was found!");
			return;
		}
		
		Record[] data = new Record[headersList.size()];
		TransitHeader currentHeader = new TransitHeader();
		
		for(int i = 0; i < headersList.size(); i++){
			currentHeader = headersList.get(i);
			// for debug - start
//			UIUtil.alert(currentHeader.getCompany());
//			UIUtil.alert(currentHeader.getJobDescription());
			// for debug - end
			data[i] = this.transitStatusRecordDef.createRecord(
					new Object[]{
							preventNullString(currentHeader.getJobNumber()),
							preventNullString(currentHeader.getEstimateNo()),
							preventNullString(currentHeader.getJobDescription()),
							preventNullString(currentHeader.getCompany()),
							preventNullString(currentHeader.getStatus()),
							preventNullString(currentHeader.getMatchingCode()),
							preventNullString(currentHeader.getLastModifiedUser()),
							preventNullString(currentHeader.getLastModifiedDate())
					}
			);
		}
		this.dataStore.add(data);
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	private String preventNullString(String input){
		if(input == null)
			return "";
		else
			return input;
	}
	
	private String preventNullString(Date date){
		return date2String(date);
	}
	
	// filter all completed job
	private void filter(){
		if(filterCompletedCheckbox.getValue())
			globalSectionController.navigateToTransitStatus("", true);
		else
			globalSectionController.navigateToTransitStatus(TransitHeader.TRANSIT_COMPLETED, false);
	}
}
