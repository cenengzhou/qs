package com.gammon.qs.client.ui.window.mainSection;

import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.ui.panel.detailSection.TenderAnalysisComparisonPanel;
import com.gammon.qs.wrapper.tenderAnalysis.TenderAnalysisComparisonWrapper;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * @author matthewatc
 * 3/2/12
 * Window to basically contain a TenderAnalysisComparisonPanel, to be used to query tender details for awarded SC
 */
public class TenderAnalysisEnquiryWindow extends Window {
	
	public final int WINDOW_WIDTH = 800;
	public final int WINDOW_HEIGHT = 500;
	
	public TenderAnalysisEnquiryWindow(final DetailSectionController detailSectionController, final TenderAnalysisComparisonWrapper tenderAnalysisComparison, String currency) {
		super();
		this.setLayout(new FitLayout());
		this.setClosable(false);
		
		
		this.setTitle("Tender Analysis Enquiry");
		if(tenderAnalysisComparison != null && tenderAnalysisComparison.getVendorWrappers().size() > 0) {
			this.setWidth(WINDOW_WIDTH);
			this.setHeight(WINDOW_HEIGHT);
			this.add(new TenderAnalysisComparisonPanel(detailSectionController, tenderAnalysisComparison, currency, false));
		} else {
			this.setWidth(400);
			this.setHeight(200);
			this.add(new Label("This package does not have any corresponding tender analysis information"));
		}
		
		Button closeButton = new Button("Close");
		closeButton.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				detailSectionController.getGlobalSectionController().closeCurrentWindow();
			}
		});
		this.addButton(closeButton);
	}
}
