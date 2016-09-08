mainApp.controller('AttachmentSCFileCtrl', ['$scope', '$location','attachmentService', 'modalService', '$cookies', '$http', '$window', '$stateParams', 'GlobalParameter',
                                         function($scope, $location, attachmentService, modalService, $cookies, $http, $window, $stateParams, GlobalParameter) {
	
	$scope.jobNo = $cookies.get('jobNo');
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo = $cookies.get('addendumNo');
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	$scope.nameObject = $stateParams.nameObject;
	$scope.offsetTop = $stateParams.offsetTop;
	$scope.insideContent = false;
	$scope.sequenceNo = 0;
	$scope.textKey = $scope.jobNo + '|' + $scope.subcontractNo + '|';
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
       			switch(fileType){
       			case '.pdf':
       				att.fileIconClass = 'fa fa-2x fa-file-pdf-o';
       				break;
       			case '.xls':
       				att.fileIconClass = 'fa fa-2x fa-file-excel-o';
       				break;
       			case '.csv':
       				att.fileIconClass = 'fa fa-2x fa-file-excel-o';
       				break;
   				default:
   					att.fileIconClass = 'fa fa-2x fa-file-o';
       			}
       		}
    		att.user = {UserName: att.createdUser};
    		att.user.userIcon = 'resources/images/profile.png';
    		$scope.getUserByUsername(att.createdUser)
    		.then(function(response){
    			if(response.data instanceof Object){
    				att.user = response.data;
    				if(att.user.StaffID !== null){
    					att.user.userIcon = $scope.imageServerAddress+att.user.StaffID+'.jpg';
    				} else {
    					att.user.userIcon = 'resources/images/profile.png';
    				}
    				if(!att.user.UserName) att.user.UserName = att.createdUser;
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
	    	var wnd = $window.open(url, 'Download Attachment', '_blank');
    	} else {
    		$scope.textAttachment = this.attach;
    		modalService.open('lg', 'view/subcontract/attachment/attachment-sc-text.html', 'AttachmentSCTextCtrl', 'Success', $scope);
    	}
    }
    
    $scope.addTextAttachment = function(){
    	$scope.isAddTextAttachment = true;
    	$scope.textAttachment = {};
    	$scope.textAttachment.sequenceNo = $scope.sequenceNo + 1;
    	$scope.textAttachment.fileName = "New Text";
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
		angular.forEach($scope.attachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		})
	}
	
	$scope.deleteAttachment = function(){
		angular.forEach($scope.attachments, function(att){
			if(att.selectedAttachement === true){
				attachmentService.deleteAttachment($scope.nameObject,$scope.textKey, parseInt(att.sequenceNo))
				.then(function(data){
					$scope.loadAttachment($scope.nameObject, $scope.textKey);
					$scope.selectedAttachement = false;
				});
			}
		})
	}
	
}]);