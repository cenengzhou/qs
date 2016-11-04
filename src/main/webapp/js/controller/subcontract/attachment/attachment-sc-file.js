mainApp.controller('AttachmentSCFileCtrl', ['$scope', '$location','attachmentService', 'modalService', 'confirmService', '$cookies', '$http', '$window', '$stateParams', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage', 'subcontractService', 'paymentService',
                                 function($scope, $location, attachmentService, modalService, confirmService, $cookies, $http, $window, $stateParams, GlobalParameter, GlobalHelper, GlobalMessage, subcontractService, paymentService) {
	if(!$scope.nameObject) {
		$scope.inBox = false;
		$scope.nameObject = $stateParams.nameObject;
	} else {		
		$scope.inBox = true;
		$scope.cancel = function () {
			$scope.uibModalInstance.close();
		};
	}
	
	$scope.jobNo = $cookies.get('jobNo');
	$scope.subcontractNo = $cookies.get('subcontractNo');
	$scope.addendumNo = $cookies.get('addendumNo');
	$scope.paymentCertNo = $cookies.get('paymentCertNo');
	
	$scope.offsetTop = $stateParams.offsetTop;
	$scope.insideContent = false;
	$scope.sequenceNo = 0;
	$scope.textKey = $scope.jobNo + '|' + $scope.subcontractNo + '|';
	$scope.hideButton = false;
	
	$scope.loadAttachmentFacade = attachmentService.getAttachmentListForPCMS;
	$scope.uploadAttachmentFacade =  attachmentService.uploadSCAttachment;
	$scope.deleteAttachmentFacade = attachmentService.deleteAttachment;
	$scope.saveTextAttachmentFacade = attachmentService.uploadTextAttachment;
	
	if($scope.nameObject === GlobalParameter['AbstractAttachment'].SCPaymentNameObject){
		$scope.textKey += $scope.paymentCertNo;
		$scope.insideContent = true;
		checkPaymentCertUpdatable();
	} else {
		checkSubcontractUpdatable();
	}
	
    $scope.loadAttachment = function(nameObject, textKey){
    	$scope.loadAttachmentFacade(nameObject, textKey)
    	.then(function(data){
    				if(angular.isArray(data)){
    					processData(data);    					
    				}
    			});
    }
    
	$scope.onSubmitAttachmentUpload = function(f){
		var formData = new FormData();
		angular.forEach(f.files, function(file){
			formData.append('files', file);
		});
		formData.append('nameObject', $scope.nameObject);
		formData.append('textKey', $scope.textKey)
		formData.append('sequenceNo', $scope.sequenceNo+1);
		$scope.uploadAttachmentFacade(formData)
		.then(function(data){
			f.value = null;
			$scope.loadAttachment($scope.nameObject, $scope.textKey);
		});
	}
	
	$scope.deleteAttachment = function(){
		confirmService.show({}, {bodyText:GlobalMessage.deleteAttachment})
		.then(function(response){
			if(response === 'Yes')
			angular.forEach($scope.attachments, function(att){
				if(att.selectedAttachement === true){
					$scope.deleteAttachmentFacade($scope.nameObject,$scope.textKey, parseInt(att.sequenceNo))
					.then(function(data){
						$scope.loadAttachment($scope.nameObject, $scope.textKey);
						$scope.selectedAttachement = false;
					});
				}
			});
		});
	}
	
    $scope.addTextAttachment = function(){
    	$scope.isAddTextAttachment = true;
    	$scope.textAttachment = {};
    	$scope.textAttachment.sequenceNo = $scope.sequenceNo + 1;
    	$scope.textAttachment.fileName = "New Text";
    	$scope.isTextUpdatable = true;
		modalService.open('lg', 'view/subcontract/attachment/attachment-sc-text.html', 'AttachmentSCTextCtrl', 'Success', $scope);
    }
    
    $scope.loadAttachment($scope.nameObject, $scope.textKey);
    
	$scope.checkSelected = function(){
		$scope.selectedAttachement = false;
		angular.forEach($scope.attachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		})
	}
	
    $scope.attachmentClick = function(){
    	$scope.isAddTextAttachment = false;
    	if(this.attach.documentType === 5){
//	    	console.log('file:'+$scope.attachServerPath+this.attach.fileLink);
	    	url = 'service/attachment/downloadScAttachment?nameObject='+$scope.nameObject+'&textKey='+$scope.textKey+'&sequenceNo='+this.attach.sequenceNo;
	    	var wnd = $window.open(url, 'Download Attachment', '_blank');
    	} else {
    		$scope.textAttachment = this.attach;
    		$scope.isTextUpdatable = $scope.isUpdatable; 
    		modalService.open('lg', 'view/subcontract/attachment/attachment-sc-text.html', 'AttachmentSCTextCtrl', 'Success', $scope);
    	}
    }
    
    function checkSubcontractUpdatable(){
    	subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(function( data ) {
			if(angular.isObject(data)){
				$scope.subcontract = data;
				if($scope.subcontract.scStatus !="330" && $scope.subcontract.scStatus !="500")
					$scope.isUpdatable = true;
			}
		})
    }
    
    function checkPaymentCertUpdatable(){
    	paymentService.getPaymentCert($scope.jobNo, $scope.subcontractNo, $scope.paymentCertNo)
		.then(function( data ) {
			if(angular.isObject(data)){
				$scope.payment = data;
				if($scope.payment.paymentStatus == "PND")
					$scope.isUpdatable = true;
			}		
		})
    }

	function getUserByUsername (username){
		return $http.get('service/security/getUserByUsername?username='+username);
	}
    
    function processData(data){
    	$scope.attachments = data;
    	addAttachmentsData($scope.attachments);
    }
   
    function addAttachmentsData (d){
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
    		getUserByUsername(att.createdUser)
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
	
}]);