mainApp.controller('SubcontractorDetailsModalCtrl', ['$scope', '$uibModalInstance', '$interval', '$http', '$window', 'modalService', 'confirmService', 'modalStatus', 'modalParam', 'GlobalMessage', 'masterListService', 'subcontractorService', 'GlobalParameter', 'GlobalHelper', 'roundUtil', 'attachmentService', 
                                            function ($scope, $uibModalInstance, $interval, $http, $window, modalService, confirmService, modalStatus, modalParam, GlobalMessage, masterListService, subcontractorService, GlobalParameter, GlobalHelper, roundUtil, attachmentService) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.status = modalStatus;
	$scope.vendorNo = modalParam;
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
		masterListService.searchVendorAddressDetails($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.vendor = data;
				loadStatistics();
				loadSubcontract();
				loadTenderAnalysis();
				loadWorkScope();
			} else {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No subcontractor found:' + $scope.vendorNo);
			}
			
		}, function(error){
			console.log(error);
		});
	}
	$scope.loadVendor();
	$scope.startYear = '01Jan' + (new Date().getFullYear() - 1);
	$scope.addressLineField = function(address){
		var result = {};
		angular.forEach(address, function(value, key) {
	        if (key.startsWith('addressLine')) {
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
	           	             { field: 'workScopeCode', displayName: "Work Scope", enableCellEdit: false },
	           	             { field: 'description', displayName: "Description", enableCellEdit: false },
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
		subcontractorService.obtainSubconctractorStatistics($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.statistics = data;
			}
		});
	}
	
	function loadSubcontract(){
		subcontractorService.obtainPackagesByVendorNo($scope.vendorNo)
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
		subcontractorService.obtainTenderAnalysisWrapperByVendorNo($scope.vendorNo)
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
		masterListService.getSubcontractorWorkScope($scope.vendorNo)
		.then(function(data){
			if(angular.isObject(data)){
				$scope.workScopes = data;
				$scope.workScopes.forEach(function(workScope){
					workScope.isApprovedText = GlobalParameter.getValueById(GlobalParameter.ApprovalStatus, workScope.isApproved.trim());
				});
				$scope.workscopeGridOptions.data = $scope.workScopes;
			}
		})
	}
	
	//attachment
	$scope.imageServerAddress = GlobalParameter.imageServerAddress;
	$scope.selectedAttachement = false;
	$scope.isAddTextAttachment = false;
	$scope.sequenceNo = 0;
	$scope.isUpdatable = true;
	$scope.nameObject = GlobalParameter.AbstractAttachment['VendorNameObject'];
	$scope.textKey = $scope.vendorNo;
	$scope.saveTextAttachmentFacade = attachmentService.uploadTextAttachment;
	$scope.disableRichEditor = true;
	$scope.loadAttachment = function (nameObject, textKey){
		attachmentService.getAttachmentListForPCMS(nameObject, textKey)
		.then(function(data){
			if(angular.isArray(data)){
				$scope.subcontractorAttachments = data;
				$scope.addAttachmentsData($scope.subcontractorAttachments);
			}
		});
	} 
	$scope.loadAttachment($scope.nameObject, $scope.textKey);
	
	$scope.addAttachmentsData = function(d){
		var index = 0;
		angular.forEach(d, function(att){
			att.selected = index;
			$scope.sequenceNo = att.sequenceNo;
			if(att.fileLink === null){
				att.fileIconClass = 'fa fa-2x fa-file-text-o'; 
			} else {
				att.fileName = att.fileName.replace(/ /g,'');
				var fileType = att.fileName.substring(att.fileName.length -4);
				if(att.fileName == 'Text' && fileType == 'Text') fileType = '.txt';
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
		if(this.attach.documentType === 5){
			url = 'service/attachment/downloadScAttachment?nameObject='+GlobalParameter.AbstractAttachment['VendorNameObject']+'&textKey='+$scope.vendorNo+'&sequenceNo='+this.attach.sequenceNo;
			var wnd = $window.open(url, 'Download Attachment', '_blank');
		} else {
			$scope.textAttachment = this.attach;
			$scope.isTextUpdatable = $scope.isUpdatable; 
			modalService.open('lg',  'view/subcontract/attachment/attachment-sc-text.html', 'AttachmentSCTextCtrl', 'Success', $scope);
		}
	}

	$scope.addTextAttachment = function(){
		$scope.isAddTextAttachment = true;
		$scope.textAttachment = {};
		$scope.textAttachment.sequenceNo = $scope.sequenceNo + 1;
		$scope.textAttachment.fileName = "New Text";
		$scope.isTextUpdatable = true; 
		modalService.open('lg', 'view/subcontract/attachment/attachment-sc-text.html', 'AttachmentSCTextCtrl', 'Success', $scope);
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
		formData.append('sequenceNo', $scope.sequenceNo+1);
		attachmentService.uploadSCAttachment(formData)
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
				angular.forEach($scope.subcontractorAttachments, function(att){
					if(att.selectedAttachement === true){
						attachmentService.deleteAttachment($scope.nameObject,$scope.textKey, parseInt(att.sequenceNo))
						.then(function(data){
							$scope.loadAttachment($scope.nameObject, $scope.textKey);
							$scope.selectedAttachement = false;
						});
					}
				});
		});
	}
}]);

mainApp.controller('SubcontractorTextCtrl', ['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'attachmentService', 'modalService', 'GlobalMessage', 'GlobalParameter',
    								function($scope, modalStatus, modalParam, $uibModalInstance, attachmentService, modalService, GlobalMessage, GlobalParameter){
		$scope.status = modalStatus;
		$scope.parentScope = modalParam;
		
		$scope.cancel = function () {
			$uibModalInstance.close();
		};
		
		$scope.saveTextAttachment = function(){
		if(tinyMceCharLimitReached){ 
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', GlobalMessage.maxCharLimitReached);
			return;
		}
		if($scope.parentScope.isAddTextAttachment === false){
			$scope.parentScope.saveTextAttachmentFacade(
				$scope.parentScope.nameObject, 
				$scope.parentScope.textKey, 
				$scope.parentScope.textAttachment.sequenceNo, 
				$scope.parentScope.textAttachment.fileName, 
				$scope.parentScope.textAttachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.nameObject, $scope.parentScope.textKey);
			});
		} else {
			$scope.parentScope.saveTextAttachmentFacade(
				$scope.parentScope.nameObject, 
				$scope.parentScope.textKey, 
				$scope.parentScope.textAttachment.sequenceNo, 
				$scope.parentScope.textAttachment.fileName, 
				$scope.parentScope.textAttachment.textAttachment)
			.then(function(data){
				$scope.parentScope.loadAttachment($scope.parentScope.nameObject, $scope.parentScope.textKey);
			});
		}
			$scope.cancel();
		}
		
		$scope.tinymceOptions = {
			plugins: [
				'advlist autolink lists link textcolor colorpicker charmap', // code
				'print preview hr searchreplace wordcount-maxlength insertdatetime ',
				'nonbreaking save table contextmenu directionality paste textpattern '
			],
			removed_menuitems:'newdocument visualaid ',
			toolbar: 'save | print preview | undo redo | bold italic | forecolor backcolor | alignleft aligncenter alignright | bullist numlist outdent indent | link ', 
			save_onsavecallback: $scope.saveTextAttachment,
			save_enablewhendirty: false,
			statusbar: true,
			height: 350,
			skin: 'tinymce_charcoal',
			readonly: !$scope.parentScope.isTextUpdatable,
			maxLength : GlobalParameter.tinyMceMaxCharLength,
		};
}]);
