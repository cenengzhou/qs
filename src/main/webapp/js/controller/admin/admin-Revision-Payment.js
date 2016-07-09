mainApp.controller('AdminRevisionPaymentCertCtrl',
		function($scope, $http, modalService, GlobalParameter) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.PaymentCertSearch = {};

	$scope.onSubmitPaymentCertSearch = function() {
		var jobNo = $scope.PaymentCertSearch.jobNo;
		var packageNo = $scope.PaymentCertSearch.packageNo;
		var paymentCertNo = $scope.PaymentCertSearch.paymentCertNo;
		if(checkNull([jobNo, packageNo, paymentCertNo])){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number, package number and payment certificate number!");
		} else {
		$http.get('service/payment/getPaymentCert?jobNo='+ jobNo + '&subcontractNo='+ packageNo + '&paymentCertNo=' + paymentCertNo).then(
			function(response) {
				if(response.data instanceof Object){
					$scope.PaymentCertRecord = response.data;
				}
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Please search payment certificate first!");
			});
		}
	};
function checkNull(objectArray){
	var result = false;
	angular.forEach(objectArray, function(obj){
		if(!result){
			if(obj === undefined) {
				result = true;
			}
		}
	});
	return result;
}
	$scope.onSubmitPaymentCertRecord = function() {
		if ($scope.PaymentCertRecord.paymentCertNo !== undefined) {
			$http.post('service/payment/UpdatePaymentCert', $scope.PaymentCertRecord)
	        .then(
			function(response) {
				if(response.data === ''){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Payment Certificate updated.");
				} else {
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.data);
				}
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status: " + response.statusText);
			});
			$scope.RevisionsPaymentCertRecord.$setPristine();
			$scope.PaymentCertRecordRecord = {};
		} else {
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search payment certificate first!");
		}
	};
	
});