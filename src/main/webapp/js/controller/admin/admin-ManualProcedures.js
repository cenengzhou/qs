
mainApp.controller('AdminManualProceduresCtrl', 
		['$scope', '$rootScope', '$http', 'modalService', 'blockUI', 'subcontractService', 'paymentService', 'mainCertService', 'audithousekeepService',
		function($scope, $rootScope, $http, modalService, blockUI, subcontractService, paymentService, mainCertService, audithousekeepService) {
	$scope.provisionGlDate = moment();
	$scope.auditTables = [];
	$scope.auditTableName = '';
//	$scope.blockProcedures = blockUI.instances.get('blockProcedures');
	
	$scope.loadAuditTableMap = function(){
		audithousekeepService.getAuditTableMap()
		.then(function(data){
			angular.forEach(data, function(value, key){
				  $scope.auditTables.push({
				    name: key,
				    auditInfo: value
				  });
			});
			$scope.auditTableName = $scope.auditTables[0].name;
		});
	}
	$scope.loadAuditTableMap();
	
	$scope.onSubmitAuditHousekeep = function(){
		var tableName = $scope.auditTableName;
		audithousekeepService.housekeepAuditTable($scope.auditTableName)
		.then(function(data){
			if(data > -1){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Removed ' + data + ' records from ' + tableName);
			}
		})
	}
	
	$scope.onSubmitProvisionPosting = function(){
//		$scope.blockProcedures.start({hideMessage:true, hideAnimate:true});
		subcontractService.runProvisionPostingManually($scope.provisionJobNumber, $scope.provisionGlDate)
		.then(function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Posted.");
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	$scope.onSubmitGenerateSubcontractSnapshot = function(){
//		$scope.blockProcedures.start({hideMessage:true, hideAnimate:true});
		subcontractService.generateSCPackageSnapshotManually()
		.then(function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Generate Subcontract Package Snapshot completed.");;
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onSubmitPostSubcontractPayment = function(){
//		$scope.blockProcedures.start({hideMessage:true, hideAnimate:true});
		paymentService.runPaymentPosting()
		.then(function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Finished.");;
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onSubmitF58001 = function(){
//		$scope.blockProcedures.start({hideMessage:true, hideAnimate:true});
		subcontractService.updateF58001FromSCPackageManually()
		.then(function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize F58001 From Subcontract Package completed.");;
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	$scope.onSubmitF58011 = function(){
//		$scope.blockProcedures.start({hideMessage:true, hideAnimate:true});
		paymentService.updateF58011FromSCPaymentCertManually()
		.then(function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize F58011 From Subcontract Payment Certificate completed.");;
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onSubmitSyncMainCert = function(){
//		$scope.blockProcedures.start({hideMessage:true, hideAnimate:true});
		mainCertService.updateMainCertFromF03B14Manually()
		.then(function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize Main Contract Certificate From F03B14 completed.");;
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	
}]);
