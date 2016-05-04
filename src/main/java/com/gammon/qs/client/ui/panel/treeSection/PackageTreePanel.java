
package com.gammon.qs.client.ui.panel.treeSection;

import java.util.List;

import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.repository.PackageRepositoryRemoteAsync;
import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.ContraChargeEnquiryMainPanel;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SystemConstant;
import com.gammon.qs.shared.RoleSecurityFunctions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class PackageTreePanel extends TreePanel{
	public static final String CONTRACHARGE_ENQUIRY_NODE_NAME = "Contra Charge Enquiry";
	private static final String AWARDED_PACKAGE_NODE = "Awarded Subcontract";
	private static final String NON_AWARDED_PACKAGE_NODE = "Non-Awarded Subcontract";

	//Config to determine Contra Charge Enquiry showing
	private boolean showingContraCharge = false;
	
	private TreeSectionController treeSectionController;
	private GlobalSectionController globalSectionController;
	private TreeNode root;
	@SuppressWarnings("unused")
	private TreeNode contraChargeEnquiryNode;
	private TreeNode awardedPackageNode;
	private TreeNode nonAwardedPackageNode;
	private SCPackage scPackage;
	private GlobalMessageTicket globalMessageTicket;

	private UserAccessRightsRepositoryRemoteAsync userAccessRightsRepository;
	private PackageRepositoryRemoteAsync packageRepository;
	private List<String> accessRightsList;
	
	private TreeNode newPackageNode;

	/*
	 * modified by matthewlam, 20150210
	 * Bug fix #44 - Migrate functions from "Awarded Subcontract" to Subcontract Enquiry
	 * TreeNodeListener on "Awarded Package" removed
	 */
	public PackageTreePanel(final TreeSectionController treeSectionController) {
		super();
		
		this.treeSectionController = treeSectionController;
		this.globalSectionController = treeSectionController.getGlobalSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		
		userAccessRightsRepository = globalSectionController.getUserAccessRightsRepository();
		packageRepository = globalSectionController.getPackageRepository();
		
		setTitle("Subcontract Package");
		setBorder(false);
		setAutoScroll(true);
		
		root = new TreeNode("Subcontract Package");

		//TODO: Reviewing by Commercial 2015-10-14
		contraChargeEnquiryNode = new TreeNode(CONTRACHARGE_ENQUIRY_NODE_NAME);
		//root.appendChild(contraChargeEnquiryNode);

		//Create Awarded Package Node
		awardedPackageNode = new TreeNode(AWARDED_PACKAGE_NODE);
		awardedPackageNode.setExpandable(true);
		root.appendChild(awardedPackageNode);

		//Create Non-Awarded Package Node
		nonAwardedPackageNode = new TreeNode(NON_AWARDED_PACKAGE_NODE);
		nonAwardedPackageNode.setExpandable(true);
		root.appendChild(nonAwardedPackageNode);
		
		setRootNode(root);
		setRootVisible(false);		//not showing the root node as folder
		expandAll();
		
		addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				if(node.getText().equals(CONTRACHARGE_ENQUIRY_NODE_NAME)){
					showContraChargeEnquiryMainPanel();
					showingContraCharge = true;
				}
				else{
					if(showingContraCharge){
						showingContraCharge = false;
						refreshPackageTreePanel();	//switch the grip panel
					}
					if (node.getAttributeAsObject("packageNo") == null) {
						if (!node.getText().equals(AWARDED_PACKAGE_NODE))
							treeSectionController.resetMainSectionDetailSection();
						if (node.getId().equals("newPackageNode")) {
							openNewPackageWindow();
						}
					} else {
						treeSectionController.requestNavigateToPackage(node.getAttribute("packageNo"));
						globalSectionController.setSelectedPackageNumber(node.getAttribute("packageNo"));
					}
				}
			}
		});
	}
	
	public void refreshPackageTreePanel() {
		// frozen the Package Tree Panel
		treeSectionController.setPackagePanelReady(false);
		SessionTimeoutCheck.renewSessionTimer();
		packageRepository.getSCPackages(globalSectionController.getJob(), new AsyncCallback<List<SCPackage>>() {
			public void onSuccess(List<SCPackage> scPackages) {
				populateTree(scPackages);
			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});

		// defrost the Package Tree Panel
		treeSectionController.setPackagePanelReady(true);
	}
	
	public void openNewPackageWindow(){
		//set defaults
		scPackage = new SCPackage();
		SessionTimeoutCheck.renewSessionTimer();
		globalSectionController.getPackageRepository().getSystemConstant("59", "00000", new AsyncCallback<SystemConstant>(){

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}

			public void onSuccess(SystemConstant result) {
				scPackage.setPackageType("S");
				scPackage.setPaymentTerms(result.getScPaymentTerm());
				scPackage.setMaxRetentionPercentage(result.getScMaxRetentionPercent()!=null?result.getScMaxRetentionPercent():0.00);
				scPackage.setMosRetentionPercentage(result.getScMOSRetentionPercent()!=null?result.getScMOSRetentionPercent():0.00);
				scPackage.setInterimRentionPercentage(result.getScInterimRetentionPercent()!=null?result.getScInterimRetentionPercent():0.00);
				globalSectionController.showEditPackageHeaderWindow(scPackage);
			}
			
		});
	}
	
	public void clearTree() {
		Node[] scPackagesNodes = awardedPackageNode.getChildNodes();
		for (int i=0; i<scPackagesNodes.length;i++) 
			scPackagesNodes[i].remove();
		
		for (Node nonAwardedNode : nonAwardedPackageNode.getChildNodes())
			nonAwardedNode.remove();
	}
		
	public void populateTree(List<SCPackage> scPackages) {
		clearTree();
		if (scPackages == null)
			return;

		for (SCPackage scPackage : scPackages) {
			if("S".equals(scPackage.getPackageType())
				||(scPackage.getPackageNo()!=null && scPackage.getPackageNo().length()==4 && scPackage.getPackageNo().startsWith("1"))
				||(scPackage.getPackageNo()!=null && scPackage.getPackageNo().length()==4 && scPackage.getPackageNo().startsWith("2")) 
				||(scPackage.getPackageNo()!=null && scPackage.getPackageNo().length()==4 && scPackage.getPackageNo().startsWith("3"))){
				TreeNode treeNode = new TreeNode(scPackage.getPackageNo() + "  -  " + scPackage.getDescription());
				treeNode.setAttribute("packageNo", scPackage.getPackageNo());
				if (scPackage.isAwarded())
					awardedPackageNode.appendChild(treeNode);
				else
					nonAwardedPackageNode.appendChild(treeNode);
			}
		}
		newPackageNode = new TreeNode("<b>Create New Package</b>");
		newPackageNode.setId("newPackageNode");
		
		securitySetup();
		root.expand();
	}

	private void securitySetup() {
		SessionTimeoutCheck.renewSessionTimer();
		userAccessRightsRepository.getAccessRights(globalSectionController.getUser().getUsername(), RoleSecurityFunctions.M010002_SCPACKAGE_TREEPANEL, new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> accessRightsReturned) {
				accessRightsList = accessRightsReturned;
				if (accessRightsList.contains("WRITE"))
					nonAwardedPackageNode.appendChild(newPackageNode);
			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
	}
	
	public void showContraChargeEnquiryMainPanel(){
		globalSectionController.getMainSectionController().resetMainPanel();
		ContraChargeEnquiryMainPanel contraChargeEnquiryMainPanel = new ContraChargeEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(contraChargeEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
		if(!globalSectionController.getDetailSectionController().getMainPanel().isCollapsed())
			globalSectionController.getDetailSectionController().getMainPanel().collapse();
		if(!globalSectionController.getMasterListSectionController().getMainPanel().isCollapsed())
			globalSectionController.getMasterListSectionController().getMainPanel().collapse();
	}

}
