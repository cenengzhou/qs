mainApp.controller('SubcontractFinalFormCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper', 'finalAccountService',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper, finalAccountService) {
	
	// $scope.paymentCertNo = $cookies.get('paymentCertNo');

	$scope.nameObject = $stateParams.nameObject;

	loadData();

	function loadData() {
		finalAccountService.getFinalAccount($scope.jobNo, $scope.addendumNo, $scope.addendum.id)
			.then(function(data) {
				$scope.finalAccount = data;
			} );
	}

	$scope.update =  function(){
		finalAccountService.createFinalAccount($scope.jobNo, $scope.addendumNo, $scope.addendum.id, $scope.finalAccount)
			.then(function(data) {
				if(data != null){
					if(data.length == 0) {
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract Final Account has been updated.");
					} else
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}
			});

		// paymentService.updatePaymentCertificate($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, $scope.paymentTerms, null, null, $scope.payment)
		// 	.then(
		// 		function( data ) {
		// 			if(data != null){
		// 				if(data.length == 0){
		// 					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract Final Account has been updated.");
		// 				}else
		// 					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
		// 			}
		// 		});

	}

}]);