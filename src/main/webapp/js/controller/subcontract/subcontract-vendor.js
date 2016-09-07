mainApp.controller('SubcontractorCtrl', ['$scope', 'subcontractService', '$http', 'masterListService', 'tenderService', 'modalService', 'confirmService', '$state', 'GlobalMessage', 'uiGridConstants',
                                         function($scope, subcontractService, $http, masterListService, tenderService, modalService, confirmService, $state, GlobalMessage, uiGridConstants) {
	
	loadData();

	$scope.gridOptions = {
			enableSorting: false,
			enableFiltering: false,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: false,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			//showGridFooter : true,
			showColumnFooter : false,
			//fastWatch : true,

			exporterMenuPdf: false,

			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
			},
			
	};

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	$scope.addVendor = function(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			if($scope.newVendorNo != null){
				masterListService.getSubcontractor($scope.newVendorNo)
				.then(
						function( data ) {
							if(data.length==0)
								modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Tenderer number is invalid.");
							else{
								if(data.scFinancialAlert !="" && data.scFinancialAlert !=null){
									var modalOptions = {
										bodyText: GlobalMessage.subcontractorHoldMessage
									};
									confirmService.showModal({}, modalOptions).then(function (result) {
										if(result == "Yes"){
											createTender();
										}
									});
								}else
									createTender();
							}
						});
			}
			else{
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input tenderer number.");
			}
		}else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract does not exist.");
		}
	};

	$scope.editSubcontractor = function(subcontractorToUpdate){
		modalService.open('lg', 'view/subcontract/modal/subcontract-vendor-feedback.html', 'SubcontractVendorFeedbackModalCtrl', '', subcontractorToUpdate);
	};

	$scope.deleteSubcontractor = function(subcontractorToDelete){
		tenderService.deleteTender($scope.jobNo, $scope.subcontractNo, subcontractorToDelete)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontractor has been deleted.");
						$state.reload();
					}
				});
	};


	$scope.updateRecommendedTender = function(recommendedSubcontractor){
		if($scope.subcontract.scStatus !="330" && $scope.subcontract.scStatus !="500"){
			tenderService.updateRecommendedTender($scope.jobNo, $scope.subcontractNo, recommendedSubcontractor)
			.then(
					function( data ) {
						if(data.length!=0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						}else{
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Recommended Subcontractor has been updated.");
							$state.reload();
						}
					});
		}
	}

	function loadData(){
		if($scope.subcontractNo!="" && $scope.subcontractNo!=null){
			getSubcontract();
			getTenderList();
			getTenderComparisonList();
		}
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.subcontract = data;
					
					if($scope.subcontract.scStatus =="330" || $scope.subcontract.scStatus =="500")
						$scope.disableButtons = true;
					else
						$scope.disableButtons = false;
				});
	}

	function getTenderList() {
		tenderService.getTenderList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					$scope.tenders = data;
				});
	}

	function createTender(){
		tenderService.createTender($scope.jobNo, $scope.subcontractNo, $scope.newVendorNo)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('lg', 'view/subcontract/modal/subcontract-vendor-feedback.html', 'SubcontractVendorFeedbackModalCtrl', '', $scope.newVendorNo);
					}
				});
	}
	
	function getTenderComparisonList() {
		tenderService.getTenderComparisonList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					Object.keys(data.vendorDetailMap)
				      .sort();
					
					angular.forEach(data.tenderAnalysisDetailWrappers, function(value, key) {
						var vendorList = data.vendorDetailMap[value.sequenceNo];
						
						angular.forEach(vendorList, function(vendor, key) {
							angular.forEach(vendor, function(v, k) {
								var vendorNameString = k.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g,' ');
								value[vendorNameString] = v;
							});
						});
						
					})
					
					$scope.gridOptions.data = data.tenderAnalysisDetailWrappers;
					
				});
	}

	
}]);




