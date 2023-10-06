mainApp.controller('EnquiryMainCertCtrl', ['$scope' , 'modalService', 'mainCertService', 'uiGridConstants', 'GlobalHelper', 'GlobalParameter', '$cookies',
                                  function($scope , modalService, mainCertService, uiGridConstants, GlobalHelper, GlobalParameter, $cookies) {
	
	
	$scope.jobNo = $cookies.get("jobNo");
	getCertificateList($scope.jobNo);
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			showColumnFooter: true,
			enableCellEditOnFocus : false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'jobNo', displayName: "Job No.", width: '60' },
			             { field: 'certificateNumber', displayName: "Cert No.", width: '70' },
			             { field: 'clientCertNo', displayName: "Client Cert No.", width: '100' },
			             { field: 'appliedMainContractorAmount', displayName: "IPA Main Contractor Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.grossAmount);
			            	 } 
			             },
			             { field: 'appliedNSCNDSCAmount', displayName: "IPA NSC/NDSC Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.openAmount);
			            	 }
			             },
			             { field: 'appliedMOSAmount', displayName: "IPA MOS Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignAmount);
			            	 }
			             },
			             { field: 'appliedMainContractorRetention', displayName: "IPA Main Contractor Retention", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedMainContractorRetentionReleased', displayName: "IPA Main Contractor Retention Release", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedRetentionforNSCNDSC', displayName: "IPA Retention for NSC / NDSC", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedRetentionforNSCNDSCReleased', displayName: "IPA Retention for NSC / NDSC Released", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedMOSRetention', displayName: "IPA MOS Retention", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedMOSRetentionReleased', displayName: "IPA MOS Retention Released", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedContraChargeAmount', displayName: "IPA Contra Charge Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedAdjustmentAmount', displayName: "IPA Adjustment Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedAdvancePayment', displayName: "IPA Advance Payment", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'appliedCPFAmount', displayName: "IPA CPF Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			            
			             { field: 'certifiedMainContractorAmount', displayName: "IPC Main ContractorAmount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedNSCNDSCAmount', displayName: "IPC NSC/NDSC Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.openAmount);
			            	 }
			             },
			             { field: 'certifiedMOSAmount', displayName: "IPC MOS Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignAmount);
			            	 }
			             },
			             { field: 'certifiedMainContractorRetention', displayName: "IPC Main Contractor Retention", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedMainContractorRetentionReleased', displayName: "IPC Main Contractor Retention Release", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedRetentionforNSCNDSC', displayName: "IPC Retention for NSC / NDSC", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedRetentionforNSCNDSCReleased', displayName: "IPC Retention for NSC / NDSC Released", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedMOSRetention', displayName: "IPC MOS Retention", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedMOSRetentionReleased', displayName: "IPC MOS Retention Released", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedContraChargeAmount', displayName: "IPC Contra Charge Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedAdjustmentAmount', displayName: "IPC Adjustment Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedAdvancePayment', displayName: "IPC Advance Payment", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'certifiedCPFAmount', displayName: "IPC CPF Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'gstPayable', displayName: "GST Payable", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			             { field: 'gstReceivable', displayName: "GST Receivable", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
			            	 }
			             },
			            
			             { field: 'ipaSubmissionDate', width: '100', displayName: "IPA Submission Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
			            	 filterCellFiltered:true, },
			             { field: 'certIssueDate', width: '100', displayName: "Cert Issue Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
				            	 filterCellFiltered:true, },
			             { field: 'certAsAtDate', width: '100', displayName: "Cert As At Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
					            	 filterCellFiltered:true, },
			             { field: 'certDueDate', width: '100', displayName: "Cert Due Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
						            	 filterCellFiltered:true, },
		            	 { field: 'actualReceiptDate', width: '100', displayName: "Actual Receipt Date", cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
		            		 filterCellFiltered:true, },
	            		 { field: 'totalReceiptAmount', displayName: "Receipt Amount", width: '120', aggregationHideLabel: true, cellFilter: 'number:2', 
	            			 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            				 return GlobalHelper.numberClass(row.entity.foreignOpenAmount);
	            			 }, 
	            			 aggregationHideLabel: true, aggregationType: uiGridConstants.aggregationTypes.sum,
	            			 footerCellTemplate: '<div class="ui-grid-cell-contents">{{col.getAggregationValue() | number:2 }}</div>',
	            			 footerCellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            				 return GlobalHelper.numberClass(col.getAggregationValue());
	            			 } 
	            		 },	 
	            		 { field: 'arDocNumber', displayName: "Doc No.", width: '80' },
	            		 { field: 'getValueById("mainContractCertificateStatus", "certificateStatus")', displayName: "Cert Status", width: '200' },
			             { field: 'remark', displayName: "Remark", width: '100' }
            			 ]
	};
	
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.search = function(){
		
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}
		
		getCertificateList($scope.jobNo);
	};
	
	function convertCertStatus (status){
		if(status!=null){
			if (status.localeCompare('100') == 0) {
				return "Certificate Created";
			}else if (status.localeCompare('120') == 0) {
				return "IPA Sent";
			}else if (status.localeCompare('150') == 0) {
				return "Certificate(IPC) Confirmed";
			}else if (status.localeCompare('200') == 0) {
				return "Certificate(IPC) Waiting for special approval";
			}else if (status.localeCompare('300') == 0) {
				return "Certificate Posted to Finance's AR";
			}else if (status.localeCompare('400') == 0) {
				return "Certifited Amount Received";
			}
		}
	}
	
	$scope.convertAbbr = function(data){
    	data.forEach(function(d){
    		d.getValueById = $scope.getValueById;
    	})
    }
	
	$scope.getValueById = function(arr, id){
		var obj = this;
		return convertCertStatus(obj[id]);
	}

	function getCertificateList(jobNo){
		mainCertService.getCertificateList(jobNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.convertAbbr(data);
				$scope.gridOptions.data = data;
			} else {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Cannot access Job:' + $scope.searchJobNo);
			}
		})
	}
	
}]);