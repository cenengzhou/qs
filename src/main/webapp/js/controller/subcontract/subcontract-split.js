mainApp.controller('SubcontractSplitCtrl', ['$scope' , 'subcontractService', function($scope, subcontractService) {


	getSCDetails();

	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: true,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,

			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
			},
	
	
			columnDefs: [
			             { field: 'lineType', width: 50},
			             { field: 'billItem', width: 100},
			             { field: 'description', width: 100},
			             { field: 'quantity', width: 100},

			             {field: 'costRate', width: 80},
			             {field: 'scRate', width: 80},
			             {field: 'objectCode', width: 100},
			             {field: 'subsidiaryCode', width: 100},

			             {field: 'amountBudget', displayName: "Budget Amount", width: 150},
			             {field: 'amountSubcontract', displayName: "SC Amount", width: 150},

			             { field: 'amountCumulativeWD', displayName: "Cum WD Amount", width: 150},
	
			             { field: 'amountCumulativeCert', displayName: "Cum Certified Amount", width: 150},
			             { field: 'newQuantity', displayName: "Posted WD Amount", width: 150},


			             {field: 'unit', width: 100},
			             {field: 'sequenceNo', width: 100},

			             {field: 'approved', width: 100},
			             {field: 'remark', width: 100}
			             ]

			
	};
	function getSCDetails() {
		subcontractService.getSCDetails($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.gridOptions.data = data;
				});
	}


}]);