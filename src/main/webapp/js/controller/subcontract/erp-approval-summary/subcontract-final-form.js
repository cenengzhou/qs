mainApp.controller('SubcontractFinalFormCtrl',
		['$scope' , '$state', '$stateParams', '$cookies', 'paymentService', 'modalService', 'confirmService', 'roundUtil', 'htmlService', 'GlobalHelper',
        function($scope , $state, $stateParams, $cookies, paymentService, modalService, confirmService, roundUtil, htmlService, GlobalHelper) {
	
	$scope.paymentCertNo = $cookies.get('paymentCertNo');

	$scope.update =  function(){

		$scope.paymentTermsDesc = $cookies.get('paymentTermsDesc');
		$scope.paymentTerms = $scope.paymentTermsDesc.substring(0, 3);

		paymentService.updatePaymentCertificate($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo, $scope.paymentTerms, null, null, $scope.payment)
			.then(
				function( data ) {
					if(data != null){
						if(data.length == 0){
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Subcontract Final Account has been updated.");
						}else
							modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}
				});

	}

}]);