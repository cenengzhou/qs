
mainApp.controller('EnquirySubcontractorCtrl', ['$scope', '$http', 'modalService', 'subcontractorService', 'GlobalParameter', 'GlobalHelper', 'rootscopeService', 'uiGridConstants', 
                                      function($scope, $http, modalService, subcontractorService, GlobalParameter, GlobalHelper, rootscopeService, uiGridConstants) {
	
	$scope.allWorkScopes = {};
	$scope.searchWorkScopes = null;
	$scope.searchSubcontractorNo = '';
	$scope.GlobalHelper = GlobalHelper;
	rootscopeService.gettingWorkScopes()
	.then(function(response){
		$scope.allWorkScopes = response.workScopes;
	});
	
	var rcmOptions = [
		{label: '--', value:'--'},
        {label: 'Recommended', value:"text-warning fa-thumbs-up"},
        {label: 'Not Recommended', value:"text-danger fa-thumbs-down"}
        ];
	var ynOptions = [
		{label: '--', value:'-'},
        {label: 'Yes', value:"Yes"},
        {label: 'No', value:"No"}	
	];
	var vtOptions = [
		{label: '--', value:'-'},
        {label: 'Supplier', value:"Supplier"},
        {label: 'Subcontractor', value:"Subcontractor"},
        {label: 'Both (Supplier & Subcontractor)', value:"Both (Supplier & Subcontractor)"}
	];
	var vsOptions = [
		{label: '--', value:'-'},
        {label: 'Performance being observed', value:"Performance being observed"},
        {label: 'Suspended', value:"Suspended"}	,
        {label: 'Blacklisted', value:"Blacklisted"}	,
        {label: 'Obsolete', value:"Obsolete"}	,
        {label: 'On HSE League Table', value:"On HSE League Table"}	,
        {label: 'Observed & On HSE League', value:"Observed & On HSE League"}	,
        {label: 'Suspended & On HSE League', value:"Suspended & On HSE League"}	
	];
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			exporterMenuPdf: false,
			enableCellSelection: false,
			rowTemplate: GlobalHelper.addressBookRowTemplate('addressBookName', 'addressBookNumber'),
			columnDefs: [
			             { field: 'addressBookNumber', displayName: "Subcontractor No", enableCellEdit: false },
			             { field: 'addressBookName', displayName: "Subcontractor Name", enableCellEdit: false },
			             { field: 'businessRegistrationNumber', displayName: "Business Registration No", enableCellEdit: false},
			             { field: 'getValueById("subcontractorVenderType", "vendorTypeCode")', displayName: "Vendor Type", enableCellEdit: false,
			            	 headerCellClass:'gridHeaderText', filter: { selectOptions: vtOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}
			             },
			             { field: 'getValueById("subcontractorVendorStatus", "vendorStatusCode")', displayName: "Vendor Status", enableCellEdit: false,
			            	 headerCellClass:'gridHeaderText', filter: { selectOptions: vsOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}
			             },
			             { field: 'getValueById("subcontractorApproval", "subcontractorApprovalCode")', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 if(row.entity.subcontractorApprovalCode.indexOf('N') > -1){
			            			 return 'red';
			            		 }
			            	 },
			            	 displayName: "Approved Subcontractor", enableCellEdit: false,
			            	 headerCellClass:'gridHeaderText', filter: { selectOptions: ynOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}
			             },
			             { field: 'paymentOnHold', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 if(row.entity.paymentOnHold.indexOf('Y') > -1 && row.entity.subcontractorApprovalCode.indexOf('Y') < 0 ){
			            			 return 'red';
			            		 }			            	
			            	 },
			            	 displayName: "Payment on Hold", enableCellEdit: false,
			            	 headerCellClass:'gridHeaderText', filter: { selectOptions: ynOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}
			             },
			             { field: 'getFinanceAlert("holdCode")', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 if(row.entity.holdCode && row.entity.holdCode.trim() !== ''){
			            			 return 'red';
			            		 }			            	
			            	 },
			            	 displayName: "on Hold by Finance", enableCellEdit: false,
			            	 headerCellClass:'gridHeaderText', filter: { selectOptions: ynOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}
			             },
//			             { field: 'getRecommended()', cellTemplate: '<div class="ui-grid-cell-contents text-center"><i class="fa {{COL_FIELD}}"></i></div>', displayName: "Recommended", enableCellEdit: false,
//			            	 headerCellClass:'gridHeaderText', filter: { selectOptions: rcmOptions, type: uiGridConstants.filter.SELECT, condition: uiGridConstants.filter.STARTS_WITH}
//			             }
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
//		if($scope.searchWorkScopes === null && $scope.searchSubcontractorNo === '') {
//			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input work scope or subcontractor to search" ); 
//		} else {
		rootscopeService.gettingAddressBookListOfSubcontractor($scope.searchSubcontractorNo, $scope.searchWorkScopes)
		.then(function(response){			
			if(angular.isArray(response.addressBookListOfSubcontractor)){
				$scope.convertAbbr(response.addressBookListOfSubcontractor);
				$scope.gridOptions.data = response.addressBookListOfSubcontractor;
			}
		});
//		}
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.convertAbbr = function(data){
    	data.forEach(function(d){
    		d.getValueById = $scope.getValueById;
    		d.getFinanceAlert = $scope.getFinanceAlert;
    		d.getRecommended = $scope.getRecommended;
    		if(d.payeeMaster){
    			switch(d.payeeMaster.holdPaymentCode){
    			case '':
    				d.paymentOnHold = '-';
    				break;
    			case 'N':
    				d.paymentOnHold = 'No';
    				break;
    			case 'Y':
    				d.paymentOnHold = 'Yes';
    				break;
    			}
    		} else {
    			d.paymentOnHold = '-';
    		}
    	})
    }
	
	$scope.getValueById = function(arr, id){
		var obj = this;
		if(!id) id ='';
		var param = obj[id] != null ? obj[id].replace(/ /g,'') : obj[id];
		return GlobalParameter.getValueById(GlobalParameter[arr], param);
	}
	
	$scope.getFinanceAlert = function(status){
		var obj = this;
		if(obj[status] != null && obj[status].trim().length > 0){
			return "Yes";
		} else {
			return "No";
		}
	}
	
	$scope.getRecommended = function(){
		var obj = this;
		if(obj['subcontractorApprovalCode'].indexOf('Y') > -1 &&
				(obj['paymentOnHold'] && obj['paymentOnHold'].indexOf('N') > -1) &&
				obj['holdCode'].trim() == ''){
			return 'text-warning fa-thumbs-up';
		} else if(obj['subcontractorApprovalCode'].indexOf('N') > -1 &&
				(obj['paymentOnHold'] && obj['paymentOnHold'].indexOf('Y') > -1)){
			console.log('Down');
			return 'text-danger fa-thumbs-down';
		} else {
			console.log('NA');
			return '-';
		}
	}
	
}]);