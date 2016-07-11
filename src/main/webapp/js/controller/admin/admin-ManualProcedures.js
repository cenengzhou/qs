
mainApp.controller('AdminManualProceduresCtrl', 
		['$scope', '$rootScope', '$http', 'modalService', 'subcontractService', 'paymentpostingService', 'mainCertService',
		function($scope, $rootScope, $http, modalService, subcontractService, paymentpostingService, mainCertService) {
	$scope.provisionGlDate = moment();
	
	$scope.onSubmitProvisionPosting = function(){
		subcontractService.runProvisionPostingManually($scope.provisionJobNumber, $scope.provisionGlDate)
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Posted.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	$scope.onSubmitGenerateSubcontractSnapshot = function(){
		subcontractService.generateSCPackageSnapshotManually()
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Generate Subcontract Package Snapshot completed.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onSubmitPostSubcontractPayment = function(){
		paymentpostingService.runPaymentPosting()
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Finished.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onSubmitF58001 = function(){
		subcontractService.updateF58001FromSCPackageManually()
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize F58001 From Subcontract Package completed.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	$scope.onSubmitF58011 = function(){
		subcontractService.updateF58011FromSCPaymentCertManually()
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize F58011 From Subcontract Payment Certificate completed.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onSubmitSyncMainCert = function(){
		mainCertService.updateMainCertFromF03B14Manually()
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize Main Contract Certificate From F03B14 completed.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	
}]);
