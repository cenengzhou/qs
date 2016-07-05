
mainApp.controller('AdminManualProceduresCtrl', function($scope, $rootScope, $http, modalService) {
	$scope.provisionGlDate = moment();
	
	$scope.onSubmitProvisionPosting = function(){
		$http.post("service/subcontract/RunProvisionPostingManually")
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Posted.");;
		},function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};
	
	$scope.onSubmitGenerateSubcontractSnapshot = function(){
		$http.post("service/subcontract/GenerateSCPackageSnapshotManually")
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Generate Subcontract Package Snapshot completed.");;
		},function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};

	$scope.onSubmitPostSubcontractPayment = function(){
		$http.post("service/paymentposting/RunPaymentPosting")
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Finished.");;
		},function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};

	$scope.onSubmitF58001 = function(){
		$http.post("service/subcontract/UpdateF58001FromSCPackageManually")
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize F58001 From Subcontract Package completed.");;
		},function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};
	
	$scope.onSubmitF58011 = function(){
		$http.post("service/payment/UpdateF58011FromSCPaymentCertManually")
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize F58011 From Subcontract Payment Certificate completed.");;
		},function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};

	$scope.onSubmitSyncMainCert = function(){
		$http.post("service/mainCert/UpdateMainCertFromF03B14Manually")
		.then(function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize Main Contract Certificate From F03B14 completed.");;
		},function(response){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Status:" + response.statusText );
		});
	};

	
});
