package com.gammon.qs.client.controller;

import com.gammon.qs.application.User;
import com.gammon.qs.client.ui.panel.mainSection.PackageEditorFormPanel;
import com.gammon.qs.client.ui.panel.mainSection.TransitCodeMatchingPanel;
import com.gammon.qs.client.ui.panel.mainSection.TransitHeaderPanel;
import com.gammon.qs.client.ui.panel.mainSection.TransitImportPanel;
import com.gammon.qs.client.ui.panel.mainSection.TransitUomMatchingPanel;
import com.gammon.qs.client.ui.toolbar.MainSectionIconToolbar;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.shared.GlobalParameter;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.FitLayout;


public class MainSectionController  {
	
	private GlobalSectionController globalSectionController;
	private Panel mainPanel; 	
	private GridPanel gridPanel;
	private Panel contentPanel;
	
	//UI
	private MainSectionIconToolbar toolbar;
	
	public MainSectionController(GlobalSectionController controller){			
		globalSectionController = controller;
		setupUI();
	}
	
	private void setupUI(){
		this.mainPanel = new Panel();
		this.mainPanel.setLayout(new FitLayout());
		this.mainPanel.setBorder(false);
		this.mainPanel.setFrame(false);
		this.mainPanel.setId(GlobalParameter.MAIN_SECTION_PANEL_ID);
		
		this.toolbar = new MainSectionIconToolbar(getGlobalSectionController());
		this.mainPanel.setTopToolbar(this.toolbar);	
	}

	public Panel getMainPanel() {
		return mainPanel;
	}
	
	/**
	 * To set the corresponding Grid Panel for display, use method populateEditorGridPanel() 
	 * @param mainPanel The Grid Panel for display
	 */
	public void setMainPanel(Panel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	public GridPanel getGridPanel() {
		return gridPanel;
	}
	
	public Panel getContentPanel() {
		return contentPanel;
	}

	public void setGridPanel(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}
	
	public void setContentPanel(Panel panel) {
		this.contentPanel = panel;
	}
	
	
	
	/**
	 * To populate the Grid Panel set(refresh)
	 */
	public void populateMainPanelWithGridPanel(){	
		this.mainPanel.removeAll(true);
		this.mainPanel.add(this.gridPanel);
		this.mainPanel.setAutoScroll(true);
		this.mainPanel.doLayout();
	}
	
	public void populateMainPanelWithContentPanel() {
		this.mainPanel.removeAll(true);
		this.mainPanel.add(this.contentPanel);
		this.mainPanel.setAutoScroll(true);
		this.mainPanel.doLayout();
	}
	
	/**
	 * to clear content in the Main Section
	 */
	public void resetMainPanel(){
		if(this.gridPanel!=null)
			this.gridPanel.destroy();
		
		if(this.contentPanel!=null)
			this.contentPanel.destroy();
		this.gridPanel= null;
		this.contentPanel = null;
		this.mainPanel.removeAll(true);	
	}
	
	/**
	 * @author matthewatc
	 * 13:17:56 9 Jan 2012 (UTC+8)
	 * Replaces populatePackageGrid.
	 */
	 	public void showPackageFormPanel(SCPackage scPackage, SCPaymentCert latestPaymentCert) {
		PackageEditorFormPanel packageEditorFormPanel = new PackageEditorFormPanel(getGlobalSectionController(), scPackage, latestPaymentCert);
		setContentPanel(packageEditorFormPanel);
		populateMainPanelWithContentPanel();
	}
	
	public PackageEditorFormPanel getPackageEditorFormPanel() {
		return (PackageEditorFormPanel)contentPanel;
	}

	
	public void navigateToTransitImport(String type) {
		TransitImportPanel transitImportPanel = new TransitImportPanel(this, type);
		setContentPanel(transitImportPanel);
		populateMainPanelWithContentPanel();
	}
	
	public void navigateToTransitCodeMatching() {
		TransitCodeMatchingPanel transitCodeMatchingPanel = new TransitCodeMatchingPanel(this);
		setContentPanel(transitCodeMatchingPanel);
		populateMainPanelWithContentPanel();
	}
	
	public void navigateToTransitUomMatching() {
		TransitUomMatchingPanel transitUomMatchingPanel = new TransitUomMatchingPanel(this);
		setContentPanel(transitUomMatchingPanel);
		populateMainPanelWithContentPanel();
	}
	
	public void navigateToTransitHeader(TransitHeader header) {
		TransitHeaderPanel transitHeaderPanel = new TransitHeaderPanel(this, header);
		setContentPanel(transitHeaderPanel);
		populateMainPanelWithContentPanel();
	}
	
	public String getCurrentJobNumber() {
		Job job = getGlobalSectionController().getJob();
		if(job == null)
			return null;
		return job.getJobNumber();
	}
	
	public User getCurrentUser(){
		return this.getGlobalSectionController().getUser();
	}
	
	public ColumnConfig[] applyScreenPreferences(String screenName, ColumnConfig[] columns) {
		return getGlobalSectionController().applyScreenPreferences(screenName, columns);
	}
	
	public void populateJobName(String jobName){
		this.toolbar.setOpenJobButton_JobNameDescription(jobName);
	}

	public GlobalSectionController getGlobalSectionController() {
		return globalSectionController;
	}
}
