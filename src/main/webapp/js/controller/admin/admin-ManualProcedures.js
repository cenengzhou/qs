
mainApp.controller('AdminManualProceduresCtrl', 
		['$scope', '$http', 'modalService', 'blockUI', 'subcontractService', 'paymentService', 'mainCertService', 'audithousekeepService', 'GlobalParameter',
		function($scope, $http, modalService, blockUI, subcontractService, paymentService, mainCertService, audithousekeepService, GlobalParameter) {
	$scope.provisionGlDate = moment().format(GlobalParameter.MOMENT_DATE_FORMAT);
	$scope.auditTables = [];
	$scope.auditTableName = '';
	$scope.GlobalParameter = GlobalParameter;
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
			if(data)
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Synchronize Main Contract Certificate From F03B14 completed.");
			else 
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Synchronize Main Contract Certificate From F03B14 failed.");
		},function(data){
//			$scope.blockProcedures.stop();
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};
	
	//Temporary function to recalculate Job Summary affected by OA items.
	/*$scope.recalculateJobSummary = function(){
		var jobList = [14175, 13262, 14213, 13333, 13178, 13483, 13601, 13545,13351, 13231, 14180, 13216, 12903, 13127, 13388, 13465, 14250, 13518, 13509, 13588, 14268, 
		               13613, 14183, 13230, 14189, 14178, 13177, 13238, 13506, 13496, 13599, 13533, 13590, 12729, 14235, 13495, 13563, 13534, 14193, 13305, 15205, 13183, 
		               13215, 13439, 13485, 13318, 14182, 13154, 13385, 13416, 13452, 13462, 13470, 13472, 13513, 13543, 13282, 13115, 12930, 13390, 13451, 13505, 13530, 
		               13208, 14181, 13173, 13285, 13455, 14232, 15634, 13489, 14254, 13548]
		
		for (i in jobList){
			subcontractService.calculateTotalWDandCertAmount(jobList[i], '', false)
			.then(
					function( data ) {

					});
		}
			
	}*/
	
	angular.element('input[name$=".singleDate"').daterangepicker({
	    singleDatePicker: true,
	    showDropdowns: true,
	    autoApply: true,
		locale: {
		      format: GlobalParameter.MOMENT_DATE_FORMAT
		    },

	})
	$scope.openDropdown = function( $event){
		angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
	}

	
}]);
