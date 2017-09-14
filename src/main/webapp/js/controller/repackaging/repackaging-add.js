mainApp.controller("RepackagingAddModalCtrl", ['$scope', '$uibModalInstance', 'resourceSummaryService', 'jdeService', 'modalService', '$cookies', '$state', 'roundUtil',
                                               function ($scope, $uibModalInstance, resourceSummaryService, jdeService, modalService, $cookies, $state, roundUtil) {

	$scope.jobNo = $cookies.get("jobNo");
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
			quantity : 0,
			rate : 1,
			amountBudget : 0
	}

	getUnitOfMeasurementList();

	$scope.calculate = function(field){
		if(field == "totalAmount" && $scope.resourceSummary.amountBudget != null){
			if($scope.resourceSummary.amountBudget.indexOf('.') != $scope.resourceSummary.amountBudget.length -1){
				$scope.resourceSummary.amountBudget = roundUtil.round($scope.resourceSummary.amountBudget, 2);
				$scope.resourceSummary.quantity = roundUtil.round($scope.resourceSummary.amountBudget/$scope.resourceSummary.rate, 4);
			}
		}else if(field == "quantity" && $scope.resourceSummary.quantity != null){
			if($scope.resourceSummary.quantity.indexOf('.') != $scope.resourceSummary.quantity.length -1){
				$scope.resourceSummary.quantity = roundUtil.round($scope.resourceSummary.quantity, 4);
				$scope.resourceSummary.amountBudget = roundUtil.round($scope.resourceSummary.quantity*$scope.resourceSummary.rate, 2);
			}
			
		}else if (field == "rate" && $scope.resourceSummary.rate != null){
			if($scope.resourceSummary.rate.indexOf('.') != $scope.resourceSummary.rate.length -1){
				$scope.resourceSummary.rate = roundUtil.round($scope.resourceSummary.rate, 4);
				$scope.resourceSummary.amountBudget = roundUtil.round($scope.resourceSummary.quantity*$scope.resourceSummary.rate, 2);
			}
		}
	}
	
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

	
	function getUnitOfMeasurementList() {
		jdeService.getUnitOfMeasurementList()
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

