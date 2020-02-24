mainApp.controller("JobVariationKpiAddCtrl", ['$scope', '$uibModalInstance', 'variationKpiService', 'modalService', '$cookies', '$state', 'roundUtil',
                                   function ($scope, $uibModalInstance, variationKpiService, modalService, $cookies, $state, roundUtil) {

	$scope.jobNo = $cookies.get("jobNo");
	
	//Save Function
	$scope.save = function () {
		$scope.disableButtons = true;
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault(); 
			$scope.disableButtons = false;
			return;
		}

		
		$scope.resourceSummary.excludeLevy = $scope.excludes.levySelected;
		$scope.resourceSummary.excludeDefect = $scope.excludes.defectSelected;
		$scope.resourceSummary.unit = $scope.units.selected;
		$scope.resourceSummary.resourceType = $scope.types.selected;
		

		addResourceSummary();
	};

	
	
	function addResourceSummary() {
		resourceSummaryService.addResourceSummary($scope.jobNo, $scope.repackagingId, $scope.resourceSummary)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
						$scope.disableButtons = false;
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "New Resource has been added.");
						$uibModalInstance.close();
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
	
	
}]);

