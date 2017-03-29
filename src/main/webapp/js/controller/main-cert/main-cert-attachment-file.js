mainApp.controller('AttachmentMainCertFileCtrl', ['$scope', '$location','attachmentService', 'modalService', 'modalStatus', 'confirmService', 'modalParam', '$uibModalInstance', '$cookies', '$http', '$window', '$stateParams', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage',
                                         function($scope, $location, attachmentService, modalService, modalStatus, confirmService, modalParam, $uibModalInstance, $cookies, $http, $window, $stateParams, GlobalParameter, GlobalHelper, GlobalMessage) {
	$scope.GlobalParameter = GlobalParameter;
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	$scope.jobNo = $cookies.get('jobNo');
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo = $cookies.get('addendumNo');
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.mainCertNo = $cookies.get('mainCertNo');
	$scope.nameObject = GlobalParameter['AbstractAttachment'].MainCertNameObject;
	$scope.isUpdatable = !$scope.parentScope.disableButtons;
	$scope.sequenceNo = 0;
	$scope.textKey = $scope.jobNo + '|' + $scope.mainCertNo + '|';
	if($scope.nameObject === GlobalParameter['AbstractAttachment'].SCDetailsNameObject){
		$scope.textKey += $scope.addendumNo;
		$scope.insideContent = true;
	} else if($scope.nameObject === GlobalParameter['AbstractAttachment'].SCPaymentNameObject){
		$scope.textKey += $scope.paymentCertNo;
		$scope.insideContent = true;
	}
	
    $scope.loadAttachment = function(nameObject, textKey){
    	attachmentService.getAttachmentListForPCMS(nameObject, textKey)
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
       		$scope.sequenceNo = att.sequenceNo;
       		if(att.fileLink === null || att.fileLink === ' '){
       			att.fileIconClass = 'fa fa-2x fa-file-text-o'; 
       		} else {
       			var fileType = att.fileName.substring(att.fileName.length -4);
       			att.fileIconClass = GlobalHelper.attachmentIconClass(fileType);
       		}
    		att.user = {fullname: att.createdUser};
    		att.user.userIcon = 'resources/images/profile.png';
    		$scope.getUserByUsername(att.createdUser)
    		.then(function(response){
    			if(response.data instanceof Object){
    				att.user = response.data;
    				if(att.user.StaffID !== null){
    					att.user.userIcon = GlobalParameter.imageServerAddress+att.user.StaffID+'.jpg';
    				} else {
    					att.user.userIcon = 'resources/images/profile.png';
    				}
    				if(!att.user.fullname) att.user.fullname = att.createdUser;
    			}
    		});
    		index++;
    	});
    }
    
    $scope.attachmentClick = function(){
    	$scope.isAddTextAttachment = false;
    	if(this.attach.documentType === 5){
//	    	console.log('file:'+$scope.attachServerPath+this.attach.fileLink);
	    	url = 'service/attachment/downloadScAttachment?nameObject='+$scope.nameObject+'&textKey='+$scope.textKey+'&sequenceNo='+this.attach.sequenceNo;
//	    	var wnd = $window.open(url, 'Download Attachment', '_blank');
	    	GlobalHelper.downloadFile(url);
    	} else {
    		$scope.textAttachment = this.attach;
    		$scope.isTextUpdatable = $scope.isUpdatable; 
    		modalService.open('lg', 'view/main-cert/modal/main-cert-attachment-text.html', 'AttachmentMainCertTextCtrl', 'Success', $scope);
    	}
    }
    
    $scope.addTextAttachment = function(){
    	$scope.isAddTextAttachment = true;
    	$scope.textAttachment = {};
    	$scope.textAttachment.sequenceNo = $scope.sequenceNo + 1;
    	$scope.textAttachment.fileName = "New Text";
    	$scope.isTextUpdatable = true;
		modalService.open('lg', 'view/main-cert/modal/main-cert-attachment-text.html', 'AttachmentMainCertTextCtrl', 'Success', $scope);
    }
    
	$scope.getUserByUsername = function(username){
		return $http.get('service/security/getUserByUsername?username='+username);
	}
    
	$scope.onSubmitAttachmentUpload = function(f){
		var formData = new FormData();
		angular.forEach(f.files, function(file){
			formData.append('files', file);
		});
		formData.append('noJob', $scope.jobNo);
		formData.append('noMainCert', $scope.mainCertNo)
		formData.append('noSequence', $scope.sequenceNo+1);
		attachmentService.addMainCertFileAttachment(formData)
		.then(function(data){
			f.value = null;
			$scope.loadAttachment($scope.nameObject, $scope.textKey);
		});
	}
	
	$scope.checkSelected = function(){
		$scope.selectedAttachement = false;
		angular.forEach($scope.attachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		})
	}
	
	$scope.deleteAttachment = function(){
		confirmService.show({}, {bodyText:GlobalMessage.deleteAttachment})
		.then(function(response){
			if(response === 'Yes')
			angular.forEach($scope.attachments, function(att){
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