mainApp.controller('AttachmentMainCertFileCtrl', ['$scope', 'attachmentService', 'modalService', 'confirmService', '$cookies', '$http', '$stateParams', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage', 'mainCertService',
                                         function($scope, attachmentService, modalService, confirmService, $cookies, $http,  $stateParams, GlobalParameter, GlobalHelper, GlobalMessage, mainCertService) {
	$scope.GlobalParameter = GlobalParameter;
	
	$scope.jobNo = $cookies.get('jobNo');
	$scope.mainCertNo = $cookies.get('mainCertNo');
	$scope.nameObject = $stateParams.nameObject;
	$scope.disableButton = true;
	$scope.isUpdatable = false;
	$scope.sequenceNo = 0;
	$scope.textKey = $scope.jobNo + '|' + $scope.mainCertNo + '|';
	
	$scope.loadAttachment = function(nameObject, textKey){
    	attachmentService.getAttachmentListForPCMS(nameObject, textKey)
    	.then(function(data){
    				if(angular.isArray(data)){
    					$scope.attachments = data;
    					$scope.addAttachmentsData($scope.attachments);
    				}
    			});
    }
	
	if($scope.mainCertNo != null && $scope.mainCertNo.length > 0){
		getCertificate($scope.mainCertNo);
		$scope.loadAttachment($scope.nameObject, $scope.textKey);
	}
	
	

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
		if($scope.isUpdatable && $scope.selectedAttachement ){
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
	}
	
	function getCertificate(mainCertNo){
		mainCertService.getCertificate($scope.jobNo, mainCertNo)
		.then(
				function( cert ) {
					if(cert != null){
						$scope.disableButton = false;
						if(cert.certificateStatus < 300)
							$scope.isUpdatable = true;
						else
							$scope.isUpdatable = false;
					}else
						$scope.disableButton = true;
				});
	}

	
}]);