package com.gammon.qs.client.ui.panel.treeSection;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.repository.BQRepositoryRemoteAsync;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.BQItemEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Page;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class BQTreePanel extends Panel {
	//Remote service
	private BQRepositoryRemoteAsync bqRepository;

	//note attribute
	private final static String NODE_ATTRIBUTE_DESCRIPTION="description";
	private final static String NODE_ATTRIBUTE_BILL_NO="bill";
	private final static String NODE_ATTRIBUTE_SUB_BILL="subBill";
	private final static String NODE_ATTRIBUTE_SECTION="session";
	private final static String NODE_ATTRIBUTE_PAGE="page";
	private final static String NODE_ATTRIBUTE_TYPE="type";
	
	//value
	private final static String NODE_ATTRIBUTE_TYPE_VALUE_PAGE="PAGE_NODE";
	private final static String NODE_ATTRIBUTE_TYPE_VALUE_BILL="BILL_NODE";
	
	//UI Controller 
	private GlobalSectionController globalSectionController;
	private TreeSectionController treeViewController;
	
	public BQTreePanel(GlobalSectionController globalController, TreeSectionController treeViewController){
		super();
		this.setTitle("Bill of Quantities");
		this.setBorder(false);
		this.setLayout(new FitLayout());
				
		this.globalSectionController = globalController;		
		this.treeViewController = treeViewController;
		
		bqRepository = globalSectionController.getBqRepository();
		
	}
	
	
	public void refreshTree(Job job){
		treeViewController.setBqPanelReady(false);			
		this.removeAll(true);
		this.clear();
		SessionTimeoutCheck.renewSessionTimer();
		bqRepository.getBillListWithPagesByJob(job, new AsyncCallback<List<Bill>>(){	
			public void onSuccess(List<Bill> result){

				renderTree(result);
				treeViewController.setBqPanelReady(true);
			}
			
			public void onFailure(Throwable e){
				UIUtil.checkSessionTimeout(e, true,globalSectionController.getUser());
			}
		});
		
		this.doLayout();
	}
	
	
	private void renderTree(List<Bill> billList) {
		this.clear();
		this.removeAll();
		
		TreePanel treePanel = new TreePanel();
		treePanel.setBorder(false);
		treePanel.setAutoScroll(true);
		
		
		Job curJob = this.globalSectionController.getJob();
		
		
		TreeNode rootNode = new TreeNode();
		rootNode.setText(curJob.getJobNumber()+" - " +curJob.getDescription());
		
		if(billList != null && billList.size()>0) {	
			for(final Bill curBill: billList)	{	
				
				final TreeNode billNode = new TreeNode("Bill "+ curBill.getBillNo().trim() 
												+(curBill.getSubBillNo()!=null && !"".equals(curBill.getSubBillNo().trim())? "/"+curBill.getSubBillNo().trim():"")
												+ (curBill.getSectionNo()!=null && !"".equals(curBill.getSectionNo().trim()) ?"/"+ curBill.getSectionNo().trim():""));
				billNode.setAttribute(NODE_ATTRIBUTE_DESCRIPTION, (curBill.getDescription()!=null?curBill.getDescription():"") );
				billNode.setAttribute(NODE_ATTRIBUTE_TYPE, NODE_ATTRIBUTE_TYPE_VALUE_BILL);
				UIUtil.maskMainPanel();
				try {
					globalSectionController.getPageRepository().obtainPageListByBill(curBill, new AsyncCallback<List<Page>>(){

						@Override
						public void onFailure(Throwable e) {
							UIUtil.throwException(e);
							UIUtil.unmaskMainPanel();
						}

						@Override
						public void onSuccess(List<Page> result) {
							for(Page curPage : result) {	
								String pageNo = curPage.getPageNo()!=null?curPage.getPageNo():"";
								TreeNode pageNode = new TreeNode("Page "+ pageNo);
												
								pageNode.setAttribute(NODE_ATTRIBUTE_DESCRIPTION, (curPage.getDescription()!=null?curPage.getDescription():""));
								pageNode.setAttribute(NODE_ATTRIBUTE_BILL_NO,curBill.getBillNo());
								pageNode.setAttribute(NODE_ATTRIBUTE_PAGE, pageNo);
								pageNode.setAttribute(NODE_ATTRIBUTE_SUB_BILL, curBill.getSubBillNo());
								pageNode.setAttribute(NODE_ATTRIBUTE_SECTION, curBill.getSectionNo());
								pageNode.setAttribute(NODE_ATTRIBUTE_TYPE, NODE_ATTRIBUTE_TYPE_VALUE_PAGE);
								
								billNode.appendChild(pageNode);
							}
							UIUtil.unmaskMainPanel();
						}
						
					});
				} catch (DatabaseOperationException e1) {
					e1.printStackTrace();
				}
				rootNode.appendChild(billNode);
			}
		}
		treePanel.setRootNode(rootNode);
		
		treePanel.addListener(new TreePanelListenerAdapter(){
			public void onClick(TreeNode node, EventObject e){
				if(NODE_ATTRIBUTE_TYPE_VALUE_PAGE.equals(node.getAttribute(NODE_ATTRIBUTE_TYPE))){
					globalSectionController.getMainSectionController().resetMainPanel();
					BQItemEnquiryMainPanel bqItemMainPanel = new BQItemEnquiryMainPanel(globalSectionController, 
																						node.getAttribute(NODE_ATTRIBUTE_BILL_NO),
																						node.getAttribute(NODE_ATTRIBUTE_SUB_BILL),
																						node.getAttribute(NODE_ATTRIBUTE_PAGE));
					globalSectionController.getMainSectionController().setGridPanel(bqItemMainPanel);
					globalSectionController.getMainSectionController().populateMainPanelWithGridPanel();
					globalSectionController.getDetailSectionController().resetPanel();
				}
				else					
					globalSectionController.resetMainSectionDetailSection();					
			}
			
			public void onContextMenu(TreeNode node, EventObject e)	{	
				while(node != null)	{
					node.collapse();
					node = (TreeNode)node.getParentNode();
				}
			}
		});
		this.add(treePanel);
		this.doLayout();
	}
	
}
