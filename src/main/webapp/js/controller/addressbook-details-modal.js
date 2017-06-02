mainApp.controller('AddressbookDetailsModalCtrl', ['$scope', '$uibModalInstance', '$interval', '$http', '$window', 'modalService', 'confirmService', 'modalStatus', 'modalParam', 'GlobalMessage', 'jdeService', 'subcontractorService', 'GlobalParameter', 'GlobalHelper', 'roundUtil', 'attachmentService', 
                                            function ($scope, $uibModalInstance, $interval, $http, $window, modalService, confirmService, modalStatus, modalParam, GlobalMessage, jdeService, subcontractorService, GlobalParameter, GlobalHelper, roundUtil, attachmentService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.status = modalStatus;
	$scope.modalParam = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});	
	
	$scope.tab = 'info';
	$scope.selectTab = function(setTab){
		$scope.tab = setTab;
	};
	$scope.isSelected = function(checkTab){
		return $scope.tab === checkTab;
	};

	$scope.awardedGridOptions = {};
	$scope.tenderAnalysisGridOptions = {};
	$scope.workscopeGridOptions = {};
	
	$scope.loadVendor = function(){
		jdeService.searchVendorAddressDetails($scope.modalParam.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.vendor = data;
				switch($scope.vendor.addressType){
				case 'V':
				case 'O':
					loadStatistics();
					loadSubcontract();
					loadTenderAnalysis();
					loadWorkScope();
					$scope.showInfoOnly = false;
					break;
				default:
					$scope.showInfoOnly = true;
					break;
				} 
			} else {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No subcontractor found:' + $scope.modalParam.vendorNo);
			}
			
		}, function(error){
//			console.log(error);
		});
	}
	$scope.loadVendor();
	$scope.startYear = '01Jan' + (new Date().getFullYear() - 1);
	$scope.addressLineField = function(address){
		var result = {};
		angular.forEach(address, function(value, key) {
//	        if (key.startsWith('addressLine')) {
			if (key.substring(0, key.length - 1) === 'addressLine') {
	        	result[key] = value;
	        }
	    });
		return result;
	}
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			enableCellEditOnFocus : false,
			allowCellFocus: false,
			enableCellSelection: false,
			exporterMenuPdf: false
	};
	
	$scope.awardedGridOptions = angular.copy($scope.gridOptions);
	$scope.tenderAnalysisGridOptions = angular.copy($scope.gridOptions);
	$scope.workscopeGridOptions = angular.copy($scope.gridOptions);

	$scope.awardedColumnDefs = [
	             { field: 'jobInfo.jobNo', width: 60, displayName: 'Job No', enableCellEdit: false },
	             { field: 'jobInfo.description', width: 180, displayName: 'Job Description', enableCellEdit: false },
	             { field: 'jobInfo.division', width: 80, displayName: 'Job Division', enableCellEdit: false },
	             { field: 'packageNo', width: 65, displayName: 'Package No', enableCellEdit: false },
	             { field: 'description', width: 180, displayName: 'Package Description', enableCellEdit: false },
	             { field: 'paymentTerms', width: 85, displayName: 'Payment Terms', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 if(row.entity.paymentTerms === 'QS0'){
	            			 return 'text-warning';
	            		 }
	            	 }
	             },
	             { field: 'paymentStatusText', width: 85, displayName: 'Payment Type', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 if(row.entity.paymentStatus === 'F'){
	            			 return 'text-success';
	            		 }
	            	 }
	             },
	             { field: 'remeasuredSubcontractSum', width: 110, cellFilter: 'number:2', displayName: 'Remeasured SC Sum', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.remeasuredSubcontractSum);
	            	 }
	             },
	             { field: 'approvedVOAmount', width: 110, cellFilter: 'number:2', displayName: 'Approved VO Amount', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.approvedVOAmount);
	            	 }
	             },
	             { field: 'revisedSCSum', width: 120, cellFilter: 'number:2', displayName: 'Revised SC Sum (Remeasured SC Sum + Approved VO Amount)', enableCellEdit: false, 
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.revisedSCSum);
	            	 }
	             },	             
	             { field: 'balanceToComplete', width: 110, cellFilter: 'number:2', displayName: 'Balance To Complete (Revised SC Sum - Total CumWork Done Amount)', enableCellEdit: false,
	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	            		 return GlobalHelper.numberClass(row.entity.balanceToComplete);
	            	 }
	             }
   			 ];

	$scope.tenderAnalysisColumnDefs = [
	      	           	             { field: 'jobNo', width: 80, displayName: "Job No", enableCellEdit: false },
	    	           	             { field: 'division', width: 80, displayName: "Job Division", enableCellEdit: false },
	    	           	             { field: 'packageNo', width: 80, displayName: "Package No", enableCellEdit: false },
	    	           	             { field: 'budgetAmount', width: 120, displayName: "Budget Amount", cellFilter: 'number:2', enableCellEdit: false,
	    	        	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    	        	            		 return GlobalHelper.numberClass(row.entity.budgetAmount);
	    	        	            	 }
	    	           	             },
	    	           	             { field: 'quotedAmount', width: 120,  displayName: "Quoted. Amount", cellFilter: 'number:2', enableCellEdit: false,
	    	        	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    	        	            		 return GlobalHelper.numberClass(row.entity.quotedAmount);
	    	        	            	 }
	    	           	             },
	    	           	             { field: 'status', width: 110, displayName: "Tender Status", enableCellEdit: false,
	    	        	            	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    	        	            		 if(row.entity.status === 'AWD'){
	    	        	            			 return 'text-success';
	    	        	            		 }
	    	        	            	 }
	    	           	             },
	    	           	             { field: 'awardStatusText', width: 200, displayName: "Tender Status Description", enableCellEdit: false }
	               			 ];

	$scope.workscopeColumnDefs = [
	           	             { field: 'codeWorkscope', displayName: "Work Scope", enableCellEdit: false },
	           	             { field: 'workscopeDescription', displayName: "Description", enableCellEdit: false },
	           	             { field: 'isApprovedText', displayName: "Status", enableCellEdit: false }
	               			 ];

	$scope.awardedGridOptions.columnDefs = $scope.awardedColumnDefs;
	$scope.tenderAnalysisGridOptions.columnDefs = $scope.tenderAnalysisColumnDefs;
	$scope.workscopeGridOptions.columnDefs = $scope.workscopeColumnDefs;
	
	$scope.awardedGridOptions.onRegisterApi = function (gridApi) {
		  $scope.awardedGridApi = gridApi;
		     $interval( function() {
		    	 $scope.awardedGridApi.grid.refresh();
		       }, 500, 20);
	}

	$scope.tenderAnalysisGridOptions.onRegisterApi = function (gridApi) {
		  $scope.tenderAnalysisGridApi = gridApi;
		     $interval( function() {
		    	 $scope.tenderAnalysisGridApi.grid.refresh();
		       }, 500, 20);
	}

	$scope.workscopeGridOptions.onRegisterApi = function (gridApi) {
		  $scope.workscopeGridApi = gridApi;
		     $interval( function() {
		    	 $scope.workscopeGridApi.grid.refresh();
		       }, 500, 20);
	}

	function loadStatistics(){
		subcontractorService.obtainSubconctractorStatistics($scope.modalParam.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.statistics = data;
			}
		});
	}
	
	function loadSubcontract(){
		subcontractorService.obtainPackagesByVendorNo($scope.modalParam.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.subcontracts = data;
				$scope.subcontracts.forEach(function(subcontract){
					subcontract.revisedSCSum = roundUtil.round(subcontract.remeasuredSubcontractSum + subcontract.approvedVOAmount, 2);
					subcontract.balanceToComplete = roundUtil.round(subcontract.revisedSCSum - subcontract.totalCumWorkDoneAmount, 2);
					subcontract.paymentStatusText = GlobalParameter.getValueById(GlobalParameter.intermFinalPayment, subcontract.paymentStatus); 
				})
				$scope.awardedGridOptions.data = $scope.subcontracts;
			}
		})
	}
	
	function loadTenderAnalysis(){
		subcontractorService.obtainTenderAnalysisWrapperByVendorNo($scope.modalParam.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.tenderAnalysis = data;
				$scope.tenderAnalysis.forEach(function(tenderAnalysis){
					tenderAnalysis.awardStatusText = GlobalParameter.getValueById(GlobalParameter.awardStatus, tenderAnalysis.status != undefined ? tenderAnalysis.status : ' ');
				});				
				$scope.tenderAnalysisGridOptions.data = $scope.tenderAnalysis;
			}
		})
	}
	
	function loadWorkScope(){
		subcontractorService.obtainWorkscopeByVendorNo($scope.modalParam.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.workScopes = data;
				$scope.workScopes.forEach(function(workScope){
					workScope.isApprovedText = GlobalParameter.getValueById(GlobalParameter.ApprovalStatus, workScope.statusApproval.trim());
				});
				$scope.workscopeGridOptions.data = $scope.workScopes;
			}
		})
	}
	
	//attachment
	$scope.imageServerAddress = GlobalParameter.imageServerAddress;
	$scope.selectedAttachement = false;
	$scope.isAddTextAttachment = false;
	$scope.noSequence = 0;
	$scope.isUpdatable = true;
	$scope.nameObject = GlobalParameter.AbstractAttachment['VendorNameObject'];
	$scope.textKey = $scope.modalParam.vendorNo;
//	$scope.saveTextAttachmentFacade = attachmentService.uploadTextAttachment;
	$scope.loadAttachmentFacade = attachmentService.obtainAttachmentList;
	$scope.uploadAttachmentFacade = attachmentService.uploadAttachment;
	$scope.deleteAttachmentFacade = attachmentService.deleteAttachment;
	$scope.saveTextAttachmentFacade = attachmentService.uploadTextAttachment;
	$scope.disableRichEditor = true;

    $scope.loadAttachment = function(nameObject, textKey){
    	$scope.loadAttachmentFacade(nameObject, textKey)
    	.then(function(data){
    				if(angular.isArray(data)){
    					$scope.attachments = data;
    					$scope.addAttachmentsData($scope.attachments);  					
    				}
    			});
    }
    $scope.loadAttachment($scope.nameObject, $scope.textKey);
	$scope.addAttachmentsData = function(d){
		var index = 0;
		angular.forEach(d, function(att){
			att.selected = index;
			$scope.noSequence = att.noSequence;
			if(att.pathFile === null){
				att.fileIconClass = 'fa fa-2x fa-file-text-o'; 
			} else {
				att.nameFile = att.nameFile.replace(/ /g,'');
				var fileType = att.nameFile.substring(att.nameFile.length -4);
				if(att.nameFile == 'Text' && fileType == 'Text') fileType = '.txt';
				att.fileIconClass = GlobalHelper.attachmentIconClass(fileType);
			}
			att.user = {};
			att.user.userIcon = 'resources/images/profile.png';
			att.user.fullname = 'JDE';
//			$scope.getUserByUsername(att.createdUser)
//			.then(function(response){
//				if(response.data instanceof Object){
//					att.user = response.data;
//					if(att.user.StaffID !== null){
//						att.user.userIcon = GlobalParameter.imageServerAddress+att.user.StaffID+'.jpg';
//					} else {
//						att.user.userIcon = 'resources/images/profile.png';
//					}
//				}
//			});
			index++;
		});
	}
		
	$scope.attachmentClick = function(){
		$scope.isAddTextAttachment = false;
		if(this.attach.typeDocument === "5"){
	    	url = 'service/attachment/obtainFileAttachment?nameObject='+$scope.nameObject+'&textKey='+$scope.textKey+'&sequenceNo='+this.attach.noSequence;
			GlobalHelper.downloadFile(url);
		} else {
			$scope.textAttachment = this.attach;
			$scope.isTextUpdatable = $scope.isUpdatable; 
			modalService.open('lg',  'view/attachment/attachment-text-editor.html', 'AttachmentTextEditorCtrl', 'Success', $scope);
		}
	}

	$scope.addTextAttachment = function(){
		$scope.isAddTextAttachment = true;
		$scope.textAttachment = {};
		$scope.textAttachment.noSequence = $scope.noSequence + 1;
		$scope.textAttachment.nameFile = "Text";
		$scope.isTextUpdatable = true; 
		modalService.open('lg', 'view/attachment/attachment-text-editor.html', 'AttachmentTextEditorCtrl', 'Success', $scope);
	}
		
	$scope.getUserByUsername = function(username){
		return $http.get('service/security/getUserByUsername?username='+username);
	}
		
	$scope.onSubmitAttachmentUpload = function(f){
		var formData = new FormData();
		angular.forEach(f.files, function(file){
			formData.append('files', file);
		});
		formData.append('nameObject', $scope.nameObject);
		formData.append('textKey', $scope.textKey)
		formData.append('sequenceNo', $scope.noSequence+1);
		$scope.uploadAttachmentFacade(formData)
		.then(function(data){
			f.value = null;
			$scope.loadAttachment($scope.nameObject, $scope.textKey);
		});
	}
		
	$scope.checkSelected = function(){
		$scope.selectedAttachement = false;
		angular.forEach($scope.repackagingAttachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		});
	}
		
	$scope.deleteAttachment = function(){
		confirmService.show({}, {bodyText:GlobalMessage.deleteAttachment})
		.then(function(response){
			if(response === 'Yes')
				angular.forEach($scope.attachments, function(att){
					if(att.selectedAttachement === true){
						$scope.deleteAttachmentFacade($scope.nameObject,$scope.textKey, parseInt(att.noSequence))
						.then(function(data){
							$scope.loadAttachment($scope.nameObject, $scope.textKey);
							$scope.selectedAttachement = false;
						});
					}
				});
		});
	}
}]);
