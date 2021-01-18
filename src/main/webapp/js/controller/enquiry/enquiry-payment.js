
mainApp.controller('EnquiryPaymentCtrl', ['$scope', '$http', 'modalService', 'blockUI', 'GlobalParameter', 'paymentService', 'GlobalParameter', 'uiGridConstants', 'GlobalHelper',
                                function($scope, $http, modalService, blockUI, GlobalParameter, paymentService, GlobalParameter, uiGridConstants, GlobalHelper) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.searchDueDateType = false;
//	$scope.blockEnquiryPayment = blockUI.instances.get('blockEnquiryPayment');
	$scope.searchJobNo = $scope.jobNo;
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
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false,
			columnDefs: [
			             { field: 'jobInfo.company', displayName: 'Company', enableCellEdit: false },
			             { field: 'jobNo', displayName: 'Job Number', enableCellEdit: false },
			             { field: 'packageNo', displayName: 'Subcontract Number', enableCellEdit: false},
			             { field: 'subcontract.vendorNo', displayName: 'Subcontractor Number', enableCellEdit: false},
			             { field: 'paymentCertNo', displayName: 'Payment Number', enableCellEdit: false},
			             { field: 'mainContractPaymentCertNo', displayName: 'Main Certificate Number', enableCellEdit: false},
			             { field: 'subcontract.paymentTerms', displayName: 'Payment Terms', enableCellEdit: false},
			             { field: 'getValueById("paymentStatus","paymentStatus")', displayName: 'Status', enableCellEdit: false},
			             { field: 'getValueById("directPayment","directPayment")', displayName: 'Direct Payment', enableCellEdit: false},
			             { field: 'getValueById("intermFinalPayment","intermFinalPayment")', displayName: 'Interim / Final Payment', enableCellEdit: false},
			             { field: 'certAmount', displayName: 'Certificate Amount', 
			            	 filters: GlobalHelper.uiGridFilters(['GREATER_THAN', 'LESS_THAN']),
			            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (row.entity.certAmount < 0) {
			            				c += ' red';
			            			}
			            			return c;
			            		},
			            		aggregationHideLabel : true,
			            		aggregationType : uiGridConstants.aggregationTypes.sum,
			            		footerCellTemplate : '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
			            		footerCellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            			var c = 'text-right';
			            			if (col.getAggregationValue() < 0) {
			            				c += ' red';
			            			}
			            			return c;
			            		},
			            		cellFilter : 'number:2',
			            	 enableCellEdit: false
			            },
			             { field: 'dueDate', displayName: 'Due Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
			            	 filterCellFiltered:true, enableCellEdit: false},
			             { field: 'asAtDate', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
				            	 filterCellFiltered:true, displayName: 'As at Date', enableCellEdit: false},
			             { field: 'scIpaReceivedDate', displayName: 'SC IPA Received Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
					            	 filterCellFiltered:true, enableCellEdit: false},
			             { field: 'certIssueDate', displayName: 'Certificate Issue Date', cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"', 
						            	 filterCellFiltered:true,  enableCellEdit: false},
		            	 { field: 'bypassPaymentTerms', displayName: 'Early Release of Payment',  filterCellFiltered:true,  enableCellEdit: false}	
						           
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		if(!$scope.searchJobNo && !$scope.searchCompany){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Please enter Job No. or Company' ); 
			return;
		}
		var paymentCertWrapper = {};
		paymentCertWrapper.jobInfo = {}
		paymentCertWrapper.jobInfo.jobNumber = $scope.searchJobNo;
		paymentCertWrapper.jobNo = $scope.searchJobNo;
		paymentCertWrapper.jobInfo.company = $scope.searchCompany;
		paymentCertWrapper.subcontract = {};
		paymentCertWrapper.subcontract.packageNo = $scope.searchPackage;
		paymentCertWrapper.subcontract.vendorNo = $scope.searchVendorNo;
		paymentCertWrapper.subcontract.paymentTerms = $scope.searchPaymentTerms;
		paymentCertWrapper.paymentStatus = $scope.searchPaymentStatus;
		paymentCertWrapper.intermFinalPayment = $scope.searchIntermFinalPayment;
		paymentCertWrapper.directPayment = $scope.searchDirectPayment;
		paymentCertWrapper.dueDate = $scope.searchDueDate !== undefined && $scope.searchDueDate != '' ? new moment($scope.searchDueDate) : null;
		paymentCertWrapper.certIssueDate = $scope.searchCertIssueDate;
		var dueDateType = $scope.searchDueDateType !== undefined ? ($scope.searchDueDateType?'onOrBefore':'exactDate'): 'ignore';

		paymentService.obtainPaymentCertificateList(paymentCertWrapper, dueDateType)
		.then(function(data){
				if(angular.isArray(data)){
					$scope.convertAbbr(data);
					$scope.gridOptions.data = data;
				} 
		}, function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
		});

	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
	$scope.convertAbbr = function(data){
    	data.forEach(function(d){
    		d.getValueById = $scope.getValueById;
    	})
    }
	
	$scope.getValueById = function(arr, id){
		var obj = this;
		return obj[id] + ' - ' + GlobalParameter.getValueById(GlobalParameter[arr], obj[id]);
	}
	
}]);