mainApp.controller('SubcontractorCtrl', ['$scope', 'modalService', 'subcontractService', '$http', 'masterListService', 'tenderService', 'modalService', '$state',
                                         function($scope, modalService, subcontractService, $http, masterListService, tenderService, modalService, $state) {
	getTenderList();


	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableColumnMoving: false,
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
			}

	};

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	$http.get("http://localhost:8080/pcms/data/vendor-compare.json")
	.success(function(data) {
		$scope.gridOptions.data = data;

	});

	$scope.addVendor = function(){
		if($scope.newVendorNo != null){
			masterListService.getSubcontractor($scope.newVendorNo)
			.then(
					function( data ) {
						if(data.length==0)
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Vendor number is invalid.");
						else{
							createTender();
						}
					});
		}
		else{
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please input vendor number.");
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


	function getTenderList() {
		tenderService.getTenderList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.tenders = data;
				});
	}



}]);



