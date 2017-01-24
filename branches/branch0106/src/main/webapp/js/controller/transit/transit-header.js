mainApp.controller("TransitHeaderModalCtrl", ['$scope', '$uibModalInstance', 'modalService', '$cookies', '$state', 'transitService', 
                                               function ($scope, $uibModalInstance, modalService, $cookies, $state, transitService) {
	
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
		transitService.createOrUpdateTransitHeader($scope.transit.jobNumber, $scope.transit.estimateNo, $scope.matchingCodes.selected, true)
		.then(
				function( data ) {
					if(data.length!=0){
						$scope.disableButton = false;
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Transit header has been added.");
						$uibModalInstance.close();
						$state.reload();
					}
				});
	}

	function getTransitHeader(){
		if ($scope.jobNo != null && $scope.jobNo.length >0){
			transitService.getTransitHeader($scope.jobNo)
			.then(
					function (data){
						$scope.transit = data;
						$scope.matchingCodes.selected = data.matchingCode;
					});
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

