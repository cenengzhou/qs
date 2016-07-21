
mainApp.controller('EnquirySubcontractorCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'subcontractorService', 'unitService', 'GlobalParameter',
                                      function($scope , $rootScope, $http, modalService, blockUI, subcontractorService, unitService, GlobalParameter) {
	
	$scope.allWorkScopes = {};
	$scope.searchWorkScopes = null;
	$scope.searchSubcontractorNo = '';
	$scope.blockEnquirySubcontractor = blockUI.instances.get('blockEnquirySubcontractor');
	$scope.loadWorkScope = function(){
		unitService.getAllWorkScopes()
		.then(function(data){
			$scope.allWorkScopes = data;
		})
	}
	$scope.loadWorkScope();
	
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
			paginationPageSizes : [ 25, 50, 100, 150, 200 ],
			paginationPageSize : 25,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'subcontractorNo', displayName: "Subcontractor No", enableCellEdit: false },
			             { field: 'subcontractorName', displayName: "Subcontractor Name", enableCellEdit: false },
			             { field: 'businessRegistrationNo', displayName: "Business Registration No", enableCellEdit: false},
			             { field: 'getValueById("subcontractorVenderType", "vendorType")', displayName: "Vendor Type", enableCellEdit: false},
			             { field: 'getValueById("subcontractorVendorStatus", "vendorStatus")', displayName: "Vendor Status", enableCellEdit: false},
			             { field: 'getValueById("subcontractorApproval", "subcontractorApproval")', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 if(row.entity.subcontractorApproval === 'N'){
			            			 return 'red';
			            		 }
			            	 },
			            	 displayName: "Approved Subcontractor", enableCellEdit: false
			             },
			             { field: 'getValueById("subcontractorHoldPayment","holdPayment")', 
			            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			            		 if(row.entity.holdPayment === 'Y' && row.entity.subcontractorApproval !== 'Y'){
			            			 return 'red';
			            		 }			            	
			            	 },
			            	 displayName: "Payment on Hold", enableCellEdit: false
			             },
			             { field: 'getFinanceAlert(scFinancialAlert)', displayName: "on Hold by Finance", enableCellEdit: false},
			             { field: 'getRecommended()', cellTemplate: '<div class="ui-grid-cell-contents text-center"><i class="fa {{COL_FIELD}}"></i></div>', displayName: "Recommended", enableCellEdit: false},
            			 ]
	};
	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		if($scope.searchWorkScopes === null && $scope.searchSubcontractorNo === '') {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input work scope or subcontractor to search" ); 
		} else {
		$scope.blockEnquirySubcontractor.start('Loading...');
		subcontractorService.obtainSubcontractorWrappers($scope.searchWorkScopes, $scope.searchSubcontractorNo !== '' ? $scope.searchSubcontractorNo + '*' : '')
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.convertAbbr(data);
					$scope.gridOptions.data = data;
					$scope.blockEnquirySubcontractor.stop();
				} 
			}, function(data){
				$scope.blockEnquirySubcontractor.stop();
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			});
		}
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	
	$scope.convertAbbr = function(data){
    	data.forEach(function(d){
    		d.getValueById = $scope.getValueById;
    		d.getFinanceAlert = $scope.getFinanceAlert;
    		d.getRecommended = $scope.getRecommended;
    	})
    }
	
	$scope.getValueById = function(arr, id){
		var obj = this;
		return GlobalParameter.getValueById(GlobalParameter[arr], obj[id]);
	}
	
	$scope.getFinanceAlert = function(status){
		if(status != null && status.length > 0){
			return "Yes";
		} else {
			return "No";
		}
	}
	
	$scope.getRecommended = function(){
		var obj = this;
		if(obj['subcontractorApproval'].indexOf('Y') > -1 &&
				obj['holdPayment'].indexOf('N') > -1 &&
				obj['scFinancialAlert'] === ''){
			return 'text-warning fa-thumbs-up';
		} else if(obj['subcontractorApproval'].indexOf('N') > -1 &&
				obj['holdPayment'].indexOf('Y') > -1){
			return 'text-danger fa-thumbs-down';
		}
	}
	
}]);