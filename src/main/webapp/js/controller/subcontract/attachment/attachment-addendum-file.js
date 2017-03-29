mainApp.controller('AttachmentAddendumFileCtrl', ['$scope', '$location','attachmentService', 'modalService', 'confirmService', '$cookies', '$http', '$window', '$stateParams', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage', 'addendumService', 'subcontractService',
                                         function($scope, $location, attachmentService, modalService, confirmService, $cookies, $http, $window, $stateParams, GlobalParameter, GlobalHelper, GlobalMessage, addendumService, subcontractService ) {
	$scope.GlobalParameter = GlobalParameter;
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
	$scope.isUpdatable = false;
	$scope.loadAttachmentFacade = attachmentService.obtainAttachmentList;
	$scope.uploadAttachmentFacade = attachmentService.uploadAddendumAttachment;
	$scope.deleteAttachmentFacade = attachmentService.deleteAddendumAttachment;
	$scope.saveTextAttachmentFacade = attachmentService.uploadAddendumTextAttachment;
	
	switch($scope.nameObject){
	case GlobalParameter['AbstractAttachment'].SCDetailsNameObject:
		$scope.textKey += $scope.addendumNo ? $scope.addendumNo : '-1';
		$scope.insideContent = true;
		checkAddendumUpdatable();
		break;
	case GlobalParameter['AbstractAttachment'].SCPaymentNameObject:
		$scope.textKey += $scope.paymentCertNo;
		$scope.insideContent = true;
		checkPaymentCertUpdatable();
		break;
	case GlobalParameter['AbstractAttachment'].SplitNameObject:
		checkSplitTerminateStatus();
		break;
	case GlobalParameter['AbstractAttachment'].TerminateNameObject:
		checkSplitTerminateStatus();
		break;
	default:
		checkSubcontractUpdatable();
		break;
	}
	
    $scope.loadAttachment = function(nameObject, textKey){
    	if(textKey.split('|')[2] === '-1'){
    		$scope.hideButton = true;
    		modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'Addendum does not exist. Please create addendum title first.');
    		return;
    	}
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
					$scope.deleteAttachmentFacade($scope.nameObject,$scope.textKey, parseInt(att.noSequence))
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
    	$scope.textAttachment.noSequence = $scope.sequenceNo + 1;
    	$scope.textAttachment.nameFile = "New Text";
    	$scope.isTextUpdatable = true;
		modalService.open('lg', 'view/subcontract/attachment/attachment-addendum-text.html', 'AttachmentAddendumTextCtrl', 'Success', $scope);
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
    	if(this.attach.typeDocument === '5'){
//	    	console.log('file:'+$scope.attachServerPath+this.attach.fileLink);
	    	url = 'service/attachment/obtainAddendumFileAttachment?nameObject='+$scope.nameObject+'&textKey='+$scope.textKey+'&sequenceNo='+this.attach.noSequence;
//	    	var wnd = $window.open(encodeURI(url), 'Download Attachment', '_blank');
	    	GlobalHelper.downloadFile(encodeURI(url));
    	} else {
    		$scope.textAttachment = this.attach;
    		$scope.isTextUpdatable = $scope.isUpdatable; 
    		modalService.open('lg', 'view/subcontract/attachment/attachment-addendum-text.html', 'AttachmentAddendumTextCtrl', 'Success', $scope);
    	}
    }
    
	function checkAddendumUpdatable(){
		if($scope.addendumNo)
		addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.addendum = data;
					if($scope.addendum && $scope.addendum.status == "PENDING")
						$scope.isUpdatable = true;
					else
						$scope.isUpdatable = false;
				});
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
    
//	public static final String SPLITTERMINATE_DEFAULT = "0";
//	public static final String SPLIT_SUBMITTED = "1";
//	public static final String TERMINATE_SUBMITTED = "2";
//	public static final String SPLIT_APPROVED = "3";
//	public static final String TERMINATE_APPROVED = "4";
//	public static final String SPLIT_REJECTED = "5";
//	public static final String TERMINATE_REJECTED = "6";
    
    function checkSplitTerminateStatus(){
    	subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(function( data ) {
			if(angular.isObject(data)){
				$scope.subcontract = data;
				if($scope.subcontract.splitTerminateStatus == 0 || $scope.subcontract.splitTerminateStatus >= 5 )
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
       		$scope.sequenceNo = att.noSequence;
       		if(att.pathFile === null || att.pathFile === ' '){
       			att.fileIconClass = 'fa fa-2x fa-file-text-o'; 
       		} else {
       			var fileType = att.nameFile.substring(att.nameFile.length -4);
       			att.fileIconClass = GlobalHelper.attachmentIconClass(fileType);
       		}
    		att.user = {fullname: att.usernameCreated};
    		att.user.userIcon = 'resources/images/profile.png';
    		getUserByUsername(att.usernameCreated)
    		.then(function(response){
    			if(response.data instanceof Object){
    				att.user = response.data;
    				if(att.user.StaffID !== null){
    					att.user.userIcon = GlobalParameter.imageServerAddress+att.user.StaffID+'.jpg';
    				} else {
    					att.user.userIcon = 'resources/images/profile.png';
    				}
    				if(!att.user.fullname) att.user.fullname = att.usernameCreated;
    			}
    		});
    		index++;
    	});
    }
	
}]);