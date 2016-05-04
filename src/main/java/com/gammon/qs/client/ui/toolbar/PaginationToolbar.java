package com.gammon.qs.client.ui.toolbar;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class PaginationToolbar extends Toolbar{
	
	private ToolbarButton firstPageButton; 
	private ToolbarButton prevPageButton;
	
	private NumberField currentPageField;
	private ToolbarTextItem totalPageText;
	
	private ToolbarButton nextPageButton;
	private ToolbarButton lastPageButton;
	
	private int currentPage;
	private int totalPage;
	
	private GoToPageCommandAdapter goToPageCommandAdapter;
	
	public PaginationToolbar()
	{
		super();
				
		firstPageButton = new ToolbarButton();
		firstPageButton.setIconCls("first-page-button");
		
		this.addButton(firstPageButton);
		
		ToolTip firstPageToolTip = new ToolTip("First Page");
		firstPageToolTip.applyTo(firstPageButton);
				
		prevPageButton = new ToolbarButton();
		prevPageButton.setIconCls("prev-page-button");
		
		ToolTip prevPageToolTip = new ToolTip("Prev Page");
		prevPageToolTip.applyTo(prevPageButton);
		this.addButton(prevPageButton);
		
		this.addSeparator();
		this.addItem(new ToolbarTextItem("Page"));
		
		currentPageField = new NumberField();
		currentPageField.setAllowDecimals(false);
		currentPageField.setWidth(30);
		this.addField(currentPageField);
		
		this.addItem(new ToolbarTextItem(" of "));
		totalPageText = new ToolbarTextItem("");
		
		
		this.addItem(totalPageText);
		
		this.addSeparator();
		
		nextPageButton = new ToolbarButton();
		nextPageButton.setIconCls("next-page-button");
		ToolTip nextPageToolTip = new ToolTip("Next Page");
		nextPageToolTip.applyTo(nextPageButton);
		this.addButton(nextPageButton);
		
		lastPageButton = new ToolbarButton();
		lastPageButton.setIconCls("last-page-button");
		ToolTip lastPageToolTip = new ToolTip("Last Page");
		lastPageToolTip.applyTo(lastPageButton);
		this.addButton(lastPageButton);
		
		this.addSeparator();		
	}
	
	
	/**
	 * To set the behaviour of going to page
	 * @param commandAdpater
	 */
	public void setGoToPageAdapter(GoToPageCommandAdapter commandAdpater)
	{
		this.goToPageCommandAdapter = commandAdpater;
		
		
		this.firstPageButton.addListener(new ButtonListenerAdapter()
		{
			public void onClick(Button button, EventObject e)
			{
				PaginationToolbar.this.goToPageCommandAdapter.goToPage(0);
			}
		});
		
		this.nextPageButton.addListener( new ButtonListenerAdapter(){
			
			public void onClick(Button button, EventObject e)
			{
				
				int nextPage = PaginationToolbar.this.currentPage +1 < PaginationToolbar.this.totalPage? PaginationToolbar.this.currentPage +1: PaginationToolbar.this.totalPage-1;
				PaginationToolbar.this.goToPageCommandAdapter.goToPage(nextPage);
				
			}
		} );
		
		this.prevPageButton.addListener( new ButtonListenerAdapter(){
			
			public void onClick(Button button, EventObject e)
			{
				int prevPage = (PaginationToolbar.this.currentPage -1 )>=0? PaginationToolbar.this.currentPage -1:0;
				PaginationToolbar.this.goToPageCommandAdapter.goToPage(prevPage);
			}
		} );
		
		this.lastPageButton.addListener( new ButtonListenerAdapter(){
			
			public void onClick(Button button, EventObject e)
			{
				int lastPage = PaginationToolbar.this.totalPage -1 >=0 ? PaginationToolbar.this.totalPage -1 :0;				
				PaginationToolbar.this.goToPageCommandAdapter.goToPage(lastPage);
			}
		} );
		
		this.currentPageField.addListener(new FieldListenerAdapter(){
			public void onSpecialKey(Field field, EventObject e){			
				if(e.getKey()==13){
					int goToPage =0;
					try{
						goToPage = Integer.parseInt(PaginationToolbar.this.currentPageField.getValueAsString());
						goToPage -=1 ;
						if(goToPage >= PaginationToolbar.this.totalPage)
							goToPage = PaginationToolbar.this.totalPage-1;
						
						if(goToPage <0 )
							goToPage = 0;
						
						PaginationToolbar.this.goToPageCommandAdapter.goToPage(goToPage);
						
					}catch(Exception ex){}
				}
								
			}
		});
		
	}
	

	/**
	 * To set the current Page Number (Start form 0)
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
		this.currentPageField.setValue(currentPage+1);
		
		
		if(currentPage ==0 && currentPage >= this.totalPage-1)
		{
			firstPageButton.setIconCls("first-page-disabled-button");
			firstPageButton.disable();
			prevPageButton.setIconCls("prev-page-disabled-button");
			prevPageButton.disable();
			
			nextPageButton.setIconCls("next-page-disabled-button");
			nextPageButton.disable();
			lastPageButton.setIconCls("last-page-disabled-button");
			lastPageButton.disable();
			
			
		}
		else if(currentPage ==0)
		{
			firstPageButton.setIconCls("first-page-disabled-button");
			firstPageButton.disable();
			prevPageButton.setIconCls("prev-page-disabled-button");
			prevPageButton.disable();
			
			nextPageButton.setIconCls("next-page-button");
			nextPageButton.enable();
			lastPageButton.setIconCls("last-page-button");
			lastPageButton.enable();
			
		}
		else if(currentPage >= this.totalPage-1)
		{
			nextPageButton.setIconCls("next-page-disabled-button");
			nextPageButton.disable();
			lastPageButton.setIconCls("last-page-disabled-button");
			lastPageButton.disable();
			
			firstPageButton.setIconCls("first-page-button");
			firstPageButton.enable();
			prevPageButton.setIconCls("prev-page-button");
			prevPageButton.enable();
		}
		else{
			
			firstPageButton.setIconCls("first-page-button");
			firstPageButton.enable();
			prevPageButton.setIconCls("prev-page-button");
			prevPageButton.enable();
			
			nextPageButton.setIconCls("next-page-button");
			nextPageButton.enable();
			lastPageButton.setIconCls("last-page-button");
			lastPageButton.enable();
			
		}
		
	}
	
	/**
	 * To set the total number of page ()
	 * @param pageSize
	 */
	public void setTotalPage(int pageSize)
	{
		this.totalPage = pageSize;
		this.totalPageText.setText((pageSize)+"");
	}
	
	
	public ToolbarButton getFirstPageButton() {
		return firstPageButton;
	}

	public void setFirstPageButton(ToolbarButton firstPageButton) {
		this.firstPageButton = firstPageButton;
	}

	public ToolbarButton getPrevPageButton() {
		return prevPageButton;
	}

	public void setPrevPageButton(ToolbarButton prevPageButton) {
		this.prevPageButton = prevPageButton;
	}

	public NumberField getCurrentPageField() {
		return currentPageField;
	}

	public void setCurrentPageField(NumberField currentPageField) {
		this.currentPageField = currentPageField;
	}

	public ToolbarTextItem getTotalPageText() {
		return totalPageText;
	}

	public void setTotalPageText(ToolbarTextItem totalPageText) {
		this.totalPageText = totalPageText;
	}

	public ToolbarButton getNextPageButton() {
		return nextPageButton;
	}

	public void setNextPageButton(ToolbarButton nextPageButton) {
		this.nextPageButton = nextPageButton;
	}

	public ToolbarButton getLastPageButton() {
		return lastPageButton;
	}

	public void setLastPageButton(ToolbarButton lastPageButton) {
		this.lastPageButton = lastPageButton;
	}
	
	
	
	

}
