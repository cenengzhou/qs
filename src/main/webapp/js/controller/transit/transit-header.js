mainApp.controller("TransitHeaderModalCtrl", ['$scope', '$rootScope', '$uibModalInstance', 'modalService', '$cookies', '$state', 'transitService', 
                                               function ($scope, $rootScope, $uibModalInstance, modalService, $cookies, $state, transitService) {
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.matchingCodes = {
			options: {
				"EX": "EX - Expanded", 
				"BS": "BS - Basic",
				"FD": "FD - Foundation",
				"SG": "SG - Singapore"
			},
			selected: "BS",
			
	};
	
	getTransitHeader();
	
	//Save Function
	$scope.save = function () {
		$scope.disableButton = true;
		
		if (false === $('form[name="form-validate"]').parsley().validate()) {
			event.preventDefault();
			$scope.disableButton = false;
			return;
		}

		
		$scope.transit.matchingCodes = $scope.matchingCodes.selected;

		//console.log($scope.transit.jobNumber +" - "+ $scope.transit.estimateNo+"  - "+$scope.matchingCodes.selected);
		
		createOrUpdateTransitHeader();
	};

	
	
	function createOrUpdateTransitHeader() {
		var jobNo = $scope.isNewJob ? $scope.transit.jobNumber : $scope.jobNo;
		transitService.createOrUpdateTransitHeader(jobNo, $scope.transit.estimateNo, $scope.matchingCodes.selected, $scope.isNewJob)
		.then(
				function( data ) {
					if(data.length!=0){
						$scope.disableButton = false;
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						var action = $scope.transitAction == 'Add' ? 'Added' : 'Updated'
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Transit header has been " + action + ".");
						$uibModalInstance.close();
						$state.reload();
					}
				});
	}

	function getTransitHeader(){
		if ($scope.jobNo != null && $scope.jobNo.length >0){
			transitService.getTransit($scope.jobNo)
			.then(
					function (data){
						$rootScope.transitLocked = data == undefined || data.systemStatus == 'LOCKED';
						$scope.transit = data;
						$scope.isNewJob = false;
						$scope.transitAction = 'Update';
						$scope.transitTitle = 'Transit Header';
						$scope.matchingCodes.selected = data.matchingCode;
					});
		} else {
			$scope.isNewJob = true;
			$scope.transitAction = 'Add';
			$scope.transitTitle = 'Create New Job';
		}
	}


	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});
	
	
}]);

