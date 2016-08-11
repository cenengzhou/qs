
mainApp.controller('AddendumDetailV3Ctrl', ['$scope', 'resourceSummaryService', 'subcontractService', 'addendumService', 'repackagingService', 'modalService', '$state', '$cookies', '$uibModalInstance',
                                            function($scope, resourceSummaryService, subcontractService, addendumService, repackagingService, modalService, $state, $cookies, $uibModalInstance) {

	var jobNo = $cookies.get('jobNo');
	var subcontractNo = $cookies.get('subcontractNo');
	var addendumNo = $cookies.get('addendumNo');
	var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');

	getLatestRepackaging();

	$scope.editable = true;
	$scope.mySelections=[];


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,


			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No", visible: false},
			             { field: 'objectCode'},
			             { field: 'subsidiaryCode'},
			             { field: 'resourceDescription', displayName: "Description"},
			             { field: 'unit'},
			             { field: 'quantity',  enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'rate',   enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountBudget', displayName: "Amount",    enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'postedIVAmount', displayName: "Posted Amount",  enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2', visible: false},
			             { field: 'resourceType', displayName: "Type"},
			             { field: 'excludeDefect', displayName: "Defect",  cellFilter: 'mapExclude', visible: false},
			             { field: 'excludeLevy', displayName: "Levy",  cellFilter: 'mapExclude', visible: false}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.save = function () {
		if(subcontractNo!="" && subcontractNo!=null){
			var dataRows = $scope.gridApi.selection.getSelectedRows();
			if(dataRows.length == 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to add addendum.");
				return;
			}
			
			addAddendumFromResourceSummaries(dataRows);

		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};


	function loadData(){
		if(subcontractNo!="" && subcontractNo!=null){
			getSubcontract();
			getResourceSummariesForAddendum();
		}
	}

	function getSubcontract(){
		subcontractService.getSubcontract(jobNo, subcontractNo)
		.then(
				function( data ) {
					/*$scope.subcontract = data;
					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;*/
				});
	}

	function getLatestRepackaging(){
		repackagingService.getLatestRepackaging(jobNo)
		.then(
				function( data ) {
					console.log(data);
					if(data.status =="900")
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Repackaging has been locked. Please unlock it first.");
					else
						loadData();
				});
	}

	
	function getResourceSummariesForAddendum() {
		resourceSummaryService.getResourceSummariesForAddendum(jobNo)
		.then(
				function( data ) {
					$scope.gridOptions.data= data;
				});
	}

	function addAddendumFromResourceSummaries(resourceSummaryList) {
		addendumService.addAddendumFromResourceSummaries(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, resourceSummaryList)
		.then(
				function( data ) {
					console.log(data);
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendums have been created.");
						$state.reload();
					}
				});
	}

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}])
.filter('mapExclude', function() {
	var excludeHash = {
			'true': 'Excluded',
			'false': 'Included'
	};

	return function(input) {
		return excludeHash[input];
	};
});