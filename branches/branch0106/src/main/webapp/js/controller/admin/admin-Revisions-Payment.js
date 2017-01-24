mainApp.controller('AdminRevisionsPaymentCtrl',
		['$scope', '$http', 'modalService', 'GlobalParameter', 'blockUI', 'GlobalHelper', 'paymentService',
		function($scope, $http, modalService, GlobalParameter, blockUI, GlobalHelper, paymentService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.PaymentCertSearch = {};
	$scope.onSubmitPaymentCertSearch = function() {
		var jobNo = $scope.PaymentCertSearch.jobNo;
		var packageNo = $scope.PaymentCertSearch.packageNo;
		var paymentCertNo = $scope.PaymentCertSearch.paymentCertNo;
		cleanupPaymentCertRecord();
		if(GlobalHelper.checkNull([jobNo, packageNo, paymentCertNo])){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number, subcontract number and payment certificate number!");
		} else {
		paymentService.getPaymentCert(jobNo, packageNo, paymentCertNo)
		.then(function(data) {
				if(data instanceof Object){
					$scope.PaymentCertRecord = data;
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Payment certificate not found!");
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Please search payment certificate first!");
			});
		}
	};
	
	$scope.onSubmitPaymentCertRecord = function() {
		if($scope.RevisionsPaymentCertRecord.$invalid) {
			return
		}
		if ($scope.PaymentCertRecord.paymentCertNo !== undefined) {
			paymentService.updatePaymentCert($scope.PaymentCertRecord)
	        .then(function(data) {
				if(data === ''){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment Certificate updated.");
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}
			}, function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
			});
		} else {
			$scope.blockPaymentCert.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search payment certificate first!");
		}
		cleanupPaymentCertRecord();
	};
	
	function cleanupPaymentCertRecord(){
		$scope.RevisionsPaymentCertRecord.$setPristine();
//		$scope.PaymentCertRecord = {};
	}
	
}]);