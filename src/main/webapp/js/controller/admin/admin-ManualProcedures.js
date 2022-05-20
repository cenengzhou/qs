
mainApp.controller('AdminManualProceduresCtrl', 
		['$scope', '$http', 'modalService', 'blockUI', 'subcontractService', 'paymentService', 'mainCertService', 'systemService', 'GlobalParameter', 'rocService', 'confirmService',
		function($scope, $http, modalService, blockUI, subcontractService, paymentService, mainCertService, systemService, GlobalParameter, rocService, confirmService) {
	$scope.provisionGlDate = moment().format(GlobalParameter.MOMENT_DATE_FORMAT);
	$scope.auditTables = [];
	$scope.auditTableName = '';
	$scope.GlobalParameter = GlobalParameter;
//	$scope.blockProcedures = blockUI.instances.get('blockProcedures');
	
	$scope.loadAuditTableMap = function(){
		systemService.getAuditTableMap()
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
		systemService.housekeepAuditTable($scope.auditTableName)
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
	
	//ROC
	rocService.getCutoffPeriod().then(function(data) {
        $scope.RocCutoffRecord = data;
    });

   
    $scope.onSubmitRocCutoffRecord = function () {
            rocService.updateRocCutoffAdmin($scope.RocCutoffRecord)
                .then(
                    function (data) {
                        if (data.length != 0)
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                        else
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "RocCutoff updated.");
                    }, function (message) {
                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', message);
                    });
    };
    
    
    $scope.onSubmitCEDApproval = function(){
		subcontractService.updateCEDApprovalManually($scope.CEDApprovalJobNo, $scope.CEDApprovalPackageNo)
		.then(function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Update CED Approval completed.");;
		},function(data){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
		});
	};

	$scope.onGeneratePaymentCertPdf = function(){
		if (!$scope.ScPaymentApprovalPaymentNo) {
			confirmService.show({}, {bodyText: 'It may take a while to generate a number of files, are you sure to proceed?'})
				.then(function (response) {
					if (response === 'Yes') {
						generatePaymentPDF();
					}
				});
		} else {
			generatePaymentPDF();
		}
	};

	function generatePaymentPDF() {
		paymentService.generatePaymentPDFAdmin($scope.ScPaymentApprovalJobNo, $scope.ScPaymentApprovalPackageNo, $scope.ScPaymentApprovalPaymentNo)
			.then(function(data){
				if (data == "")
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Generate Payment Cert PDF completed.");
				else
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
			},function(data){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data );
			});
	}
	
    
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
	$scope.recalculateJobSummary = function(){
		var jobList = [
			11721, 11838, 11997, 12150, 12193, 12199, 12255, 12266, 12288, 12315, 12337, 12376, 12386, 12393, 12395, 12398, 
			12511, 12522, 12613, 12668, 12682, 12688, 12695, 12700, 12706, 12742, 12759, 12761, 12762, 12764, 12769, 12775, 
			12787, 12788, 12793, 12799, 12801, 12804, 12814, 12818, 12830, 12835, 12838, 12843, 12846, 12852, 12854, 12856, 
			12859, 12860, 12861, 12866, 12869, 12872, 12878, 12879, 12880, 12885, 12887, 12888, 12889, 12890, 12893, 12896, 
			12897, 12898, 12899, 12900, 12902, 12903, 12904, 12907, 12908, 12911, 12912, 12913, 12916, 12921, 12922, 12923, 
			12925, 12926, 12927, 12930, 12932, 12933, 12934, 12936, 12938, 12939, 12940, 12942, 12945, 12948, 12950, 12952, 
			12956, 12957, 12958, 12959, 12960, 12961, 12966, 12968, 12969, 12971, 12975, 12976, 12977, 12979, 12980, 12986, 
			12987, 12989, 12991, 12995, 12997, 12998, 13001, 13002, 13006, 13010, 13011, 13015, 13016, 13018, 13024, 13025, 
			13026, 13027, 13028, 13029, 13033, 13034, 13035, 13037, 13039, 13040, 13043, 13045, 13046, 13047, 13048, 13051, 
			13052, 13053, 13055, 13056, 13059, 13063, 13064, 13065, 13066, 13067, 13069, 13071, 13072, 13075, 13079, 13080, 
			13081, 13082, 13083, 13085, 13086, 13089, 13090, 13095, 13096, 13099, 13100, 13103, 13104, 13105, 13108, 13110, 
			13112, 13113, 13115, 13116, 13125, 13127, 13128, 13129, 13131, 13132, 13133, 13134, 13136, 13137, 13138, 13140, 
			13141, 13149, 13150, 13153, 13154, 13156, 13157, 13158, 13159, 13160, 13163, 13168, 13169, 13170, 13173, 13176, 
			13179, 13181, 13182, 13183, 13188, 13189, 13192, 13193, 13194, 13200, 13201, 13203, 13209, 13213, 13220, 13221, 
			13224, 13227, 13228, 13230, 13235, 13238, 13239, 13259, 13262, 13269, 13270, 13272, 13276, 13282, 13286, 13293, 
			13295, 13299, 13301, 13309, 13312, 13318, 13319, 13321, 13323, 13324, 13325, 13338, 13351, 13361, 13362, 13363, 
			13372, 13383, 13389, 13391, 13399, 13400, 13409, 13416, 13417, 13418, 13421, 13422, 13428, 13434, 13437, 13443, 
			13445, 13446, 13455, 13456, 13463, 13466, 13468, 13477, 13478, 13492, 13496, 13498, 13501, 13511, 13515, 13516, 
			13522, 13523, 13531, 13533, 13539, 13545, 13546, 13548, 13570, 14175, 14176, 14183, 14189, 14191, 14194, 14212, 
			14213, 14232, 14250, 14564, 15201, 15204, 15205, 15206, 15208, 15536, 15538, 15542, 15547, 15552, 15553, 15555, 
			15557, 15566, 15568, 15577, 15582, 15591, 15592, 15596, 15603, 15622, 15629, 15634, 15641, 16164, 16237, 16253, 
			16259, 16264, 16266, 16270, 16280, 16283, 16289, 16294, 16295, 16297, 16299, 16303, 16304, 16306, 16307, 16309, 
			16310, 16312, 16316, 16319, 17371, 17373, 17376, 17377, 17851
			]
		
		for (i in jobList){
			subcontractService.calculateTotalWDandCertAmount(jobList[i], '', false)
			.then(
					function( data ) {

					});
		}
			
	}
	
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
