package com.gammon.qs.client.ui.panel.treeSection;


import com.gammon.qs.client.controller.DetailSectionController;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.controller.MainSectionController;
import com.gammon.qs.client.controller.TreeSectionController;
import com.gammon.qs.client.repository.PaymentRepositoryRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.SessionTimeoutCheck;
import com.gammon.qs.client.ui.panel.mainSection.PaymentCertificateEnquiryMainPanel;
import com.gammon.qs.client.ui.panel.mainSection.PaymentGridPanel;
import com.gammon.qs.client.ui.util.UIUtil;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;


import java.util.List;


public class PaymentTreePanel extends TreePanel{
	private static final String PAYMENT_CERTIFICATE_ENQUIRY_NODE = "Payment Certificate Enquiry";
	private static final String AWARDED_SUBCONTRACT_NODE = "Awarded Subcontract";
	private static final String NON_AWARDED_SUBCONTRACT_NODE = "Non-Awarded Subcontract";
	
	private static final String NODE_ATTRIBUTE_PACKAGE_NO = "packageNo";
	
	private TreeNode root;
	private TreeNode paymentCertEnquiryNode;
	private TreeNode awardedSubcontractNode;
	private TreeNode nonAwardedSubcontractNode;
	
	private TreeSectionController treeSectionController;
	private GlobalSectionController globalSectionController;
	private MainSectionController mainSectionController;
	private DetailSectionController detailSectionController;
	private GlobalMessageTicket globalMessageTicket;
	
	private PaymentRepositoryRemoteAsync paymentRepository;
	
	public PaymentTreePanel(final TreeSectionController treeSectionController) {
		super();

		this.treeSectionController = treeSectionController;
		this.globalSectionController = treeSectionController.getGlobalSectionController();
		this.mainSectionController = globalSectionController.getMainSectionController();
		this.detailSectionController = globalSectionController.getDetailSectionController();
		globalMessageTicket = new GlobalMessageTicket();
		
		paymentRepository = globalSectionController.getPaymentRepository();
		
		setTitle("Subcontract Payment");
		setBorder(false);
		setAutoScroll(true);

		root = new TreeNode("Package");
		root.setExpandable(true);
		//root.expand();  

		//Payment Certificate Enquiry Node
		paymentCertEnquiryNode = new TreeNode(PAYMENT_CERTIFICATE_ENQUIRY_NODE);
		root.appendChild(paymentCertEnquiryNode);
		
		//Create Awarded Subcontract Node
		awardedSubcontractNode = new TreeNode(AWARDED_SUBCONTRACT_NODE);
		awardedSubcontractNode.setExpandable(true);
		root.appendChild(awardedSubcontractNode);
		
		//Create Non-Awarded Package Node
		nonAwardedSubcontractNode = new TreeNode(NON_AWARDED_SUBCONTRACT_NODE);
		nonAwardedSubcontractNode.setExpandable(true);
		root.appendChild(nonAwardedSubcontractNode);
		
		setRootNode(root);
		setRootVisible(false);
		expandAll();

		addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				globalMessageTicket.refresh();
				if (node.getText().equals(PAYMENT_CERTIFICATE_ENQUIRY_NODE)){
					populatePaymentCertificateEnquiryMainPanel(); 
				}
				else if (node.getAttributeAsObject(NODE_ATTRIBUTE_PACKAGE_NO) == null) {
					treeSectionController.resetMainSectionDetailSection();
				}
				else {
					showPaymentCertificateMainPanel(node.getAttribute(NODE_ATTRIBUTE_PACKAGE_NO));
					treeSectionController.getGlobalSectionController().setSelectedPackageNumber(node.getAttribute(NODE_ATTRIBUTE_PACKAGE_NO));
				}
			}
		});
	}

	public void refreshPaymentTreePanel() {
		// frozen the Payment Tree Panel
		treeSectionController.setPaymentPanelReady(false);
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.obtainPackageListForSCPaymentPanel(globalSectionController.getJob().getJobNumber(), new AsyncCallback<List<SCPackage>>() {
			public void onSuccess(List<SCPackage> scPackages) {
				populateTree(scPackages);
			}

			public void onFailure(Throwable e) {
				UIUtil.throwException(e);
			}
		});
		// defrost the Payment Tree Panel
		treeSectionController.setPaymentPanelReady(true);
	}

	public void clearTree() {
		for (Node awardedNode : awardedSubcontractNode.getChildNodes()) 
			awardedNode.remove();
		
		for (Node nonAwardedNode : nonAwardedSubcontractNode.getChildNodes())
			nonAwardedNode.remove();
	}

	public void populateTree(List<SCPackage> scPackages) {
		clearTree();
		if(scPackages == null)
			return;

		for (SCPackage scPackage : scPackages) {
			TreeNode treeNode = new TreeNode(scPackage.getPackageNo() + "  -  " + scPackage.getDescription());
			treeNode.setAttribute(NODE_ATTRIBUTE_PACKAGE_NO, scPackage.getPackageNo());

			if (scPackage.isAwarded())
				awardedSubcontractNode.appendChild(treeNode);
			else
				nonAwardedSubcontractNode.appendChild(treeNode);


		}
		root.expand();
	}
	
	public void showPaymentCertificateMainPanel(String packageNo) {
		UIUtil.maskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID, GlobalParameter.LOADING_MSG, true);
		mainSectionController.resetMainPanel();
		SessionTimeoutCheck.renewSessionTimer();
		paymentRepository.obtainSCPackagePaymentCertificates(globalSectionController.getJob().getJobNumber(), packageNo, new AsyncCallback<SCPaymentCertsWrapper>() {
			public void onSuccess(SCPaymentCertsWrapper scPaymentCertsWrapper) {
				PaymentGridPanel paymentGridPanel = new PaymentGridPanel(globalSectionController, scPaymentCertsWrapper);
				paymentGridPanel.populateGrid();

				mainSectionController.setGridPanel(paymentGridPanel);
				mainSectionController.populateMainPanelWithGridPanel();
				detailSectionController.resetPanel();
			}

			public void onFailure(Throwable e) {
				UIUtil.unmaskPanelById(GlobalParameter.MAIN_SECTION_PANEL_ID);
			}

		});
	}

	public void populatePaymentCertificateEnquiryMainPanel() {
		globalSectionController.getMainSectionController().resetMainPanel();
		PaymentCertificateEnquiryMainPanel paymentCertificateEnquiryMainPanel = new PaymentCertificateEnquiryMainPanel(globalSectionController);
		globalSectionController.getMainSectionController().setContentPanel(paymentCertificateEnquiryMainPanel);
		globalSectionController.getMainSectionController().populateMainPanelWithContentPanel();
		globalSectionController.getDetailSectionController().resetPanel();
		
	}

}
