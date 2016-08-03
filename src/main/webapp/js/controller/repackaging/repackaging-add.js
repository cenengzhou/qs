mainApp.controller("RepackagingAddModalCtrl", ['$scope', '$uibModalInstance', 'resourceSummaryService', 'unitService', 'modalService', '$cookies', '$state', 'roundUtil',
                                               function ($scope, $uibModalInstance, resourceSummaryService, unitService, modalService, $cookies, $state, roundUtil) {

	$scope.repackagingId = $cookies.get("repackagingId");

	$scope.units=[];
	$scope.units.selected = "AM";
	
	$scope.excludes = {
			options: {
				"false": "Included" , 
				"true": "Excluded"
			},
			levySelected: "false",
			defectSelected: "false"
			
	};
	
	$scope.types = {
			options: [
				"OI", 
				"VO"
			],
			selected: "VO",
			
	};
	
	$scope.resourceSummary = {
			unit : "AM",
			quantity : 100,
			rate : 1,
			amountBudget : 100
	}

	getUnitOfMeasurementList();

	$scope.calculate = function(field){
		if(field == "totalAmount" && $scope.resourceSummary.amountBudget != null){
			$scope.resourceSummary.amountBudget = roundUtil.round($scope.resourceSummary.amountBudget, 2);
			$scope.resourceSummary.quantity = roundUtil.round($scope.resourceSummary.amountBudget/$scope.resourceSummary.rate, 4);
			$scope.resourceSummary.rate = roundUtil.round($scope.resourceSummary.amountBudget/$scope.resourceSummary.quantity, 2);
		}else if(field == "quantity" && $scope.resourceSummary.quantity != null){
			$scope.resourceSummary.quantity = roundUtil.round($scope.resourceSummary.quantity, 4);
			$scope.resourceSummary.amountBudget = roundUtil.round($scope.resourceSummary.quantity*$scope.resourceSummary.rate, 2);
			$scope.resourceSummary.rate = roundUtil.round($scope.resourceSummary.amountBudget/$scope.resourceSummary.quantity, 2);
		}else if (field == "rate" && $scope.resourceSummary.rate != null){
			$scope.resourceSummary.rate = roundUtil.round($scope.resourceSummary.rate, 2);
			$scope.resourceSummary.amountBudget = roundUtil.round($scope.resourceSummary.quantity*$scope.resourceSummary.rate, 2);
			$scope.resourceSummary.quantity = roundUtil.round($scope.resourceSummary.amountBudget/$scope.resourceSummary.rate, 4);
		}
	}
	
	//Save Function
	$scope.save = function () {

		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();  
			return;
		}

		
		$scope.resourceSummary.excludeLevy = $scope.excludes.levySelected;
		$scope.resourceSummary.excludeDefect = $scope.excludes.defectSelected;
		$scope.resourceSummary.unit = $scope.units.selected;
		$scope.resourceSummary.resourceType = $scope.types.selected;
		

		addResourceSummary();
	};

	
	
	function addResourceSummary() {
		resourceSummaryService.addResourceSummary($cookies.get("jobNo"), $scope.repackagingId, $scope.resourceSummary)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "New Resource has been added.");
						$uibModalInstance.close();
						$state.reload();
					}
				});
	}

	
	function getUnitOfMeasurementList() {
		unitService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push(value.unitCode.trim());
					});
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

