mainApp.controller('RepackagingCtrl', ['$scope', '$location', '$cookies', 'repackagingService', 'modalService', 'attachmentService', '$http', '$window',
                                       function($scope, $location, $cookies, repackagingService, modalService, attachmentService, $http, $window) {

	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	$scope.attachServerPath = '\\\\ERPATH11\\DEV\\QS\\JobAttachments\\';
	
	$scope.repackaging = "";
	$scope.sequenceNo = 0;
	$scope.imageServerAddress = 'http://gammon.gamska.com/PeopleDirectory_Picture/';
	$scope.selectedAttachement = false;
	$scope.isAddTextAttachment = false;
	loadRepacakgingData();
    
	$scope.click = function(view) {
		if(view=="unlock"){
			addRepackaging();
		}else if (view=="reset"){
			deleteRepackaging();
		}else if (view=="snapshot"){
			generateSnapshot();
		}else if (view=="confirm"){
			modalService.open('lg', 'view/repackaging/modal/repackaging-confirm.html', 'RepackagingConfirmModalCtrl');
		}
	};

	$scope.updateRemarks = function(){
		updateRepackaging();
	}
	
    
    function loadRepacakgingData() {
   	 repackagingService.getLatestRepackaging($scope.jobNo)
   	 .then(
   			 function( data ) {
   				 $scope.repackaging = data;
   				 $cookies.put('repackagingId', data.id);
   				 console.log("repackaging status: "+$scope.repackaging.status);
   				$scope.loadAttachment($scope.repackaging.id);
   			 });
    }
    
    $scope.loadAttachment = function(repackagingEntryID){
    	attachmentService.getRepackagingAttachments(repackagingEntryID)
    	.then(
    			function(data){
    				if(angular.isArray(data)){
    					$scope.repackagingAttachments = data;
    					$scope.addAttachmentsData($scope.repackagingAttachments);
    				}
    			})
    }
    
    $scope.addAttachmentsData = function(d){
    	var index = 0;
       	angular.forEach(d, function(att){
       		att.selected = index;
       		$scope.sequenceNo = att.sequenceNo;
       		if(att.fileLink === null){
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
    		att.user = {};
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
    			}
    		});
    		index++;
    	});
    }
    
    $scope.attachmentClick = function(){
    	$scope.isAddTextAttachment = false;
    	if(this.attach.documentType === 5){
//	    	console.log('file:'+$scope.attachServerPath+this.attach.fileLink);
	    	url = 'service/attachment/downloadRepackagingAttachment?repackagingEntryID='+$scope.repackaging.id+'&sequenceNo='+this.attach.sequenceNo;
	    	var wnd = $window.open(url, 'Download Attachment', '_blank');
    	} else {
    		$scope.repackaging.attachment = this.attach;
    		modalService.open('lg', 'view/repackaging/modal/repackaging-textattachment.html', 'RepackagingTextAttachmentCtrl', 'Success', $scope);
    	}
    }
    
    $scope.addTextAttachment = function(){
    	$scope.isAddTextAttachment = true;
    	$scope.repackaging.attachment = {};
    	$scope.repackaging.attachment.sequenceNo = $scope.sequenceNo + 1;
    	$scope.repackaging.attachment.fileName = "New Text";
		modalService.open('lg', 'view/repackaging/modal/repackaging-textattachment.html', 'RepackagingTextAttachmentCtrl', 'Success', $scope);
    }
    
	$scope.getUserByUsername = function(username){
		return $http.get('service/security/getUserByUsername?username='+username);
	}
    
	$scope.onSubmitAttachmentUpload = function(f){
		var formData = new FormData();
		angular.forEach(f.files, function(file){
			formData.append('files', file);
		});
		formData.append('repackagingEntryID', $scope.repackaging.id);
		formData.append('sequenceNo', $scope.sequenceNo+1);
		attachmentService.uploadRepackingAttachment(formData)
		.then(function(data){
			f.value = null;
			$scope.loadAttachment($scope.repackaging.id);
		});
	}
	
	$scope.checkSelected = function(){
		$scope.selectedAttachement = false;
		angular.forEach($scope.repackagingAttachments, function(att){
			if(att.selectedAttachement === true){
				$scope.selectedAttachement = true;
				return;
			}
		})
	}
	
	$scope.deleteAttachment = function(){
		angular.forEach($scope.repackagingAttachments, function(att){
			if(att.selectedAttachement === true){
				attachmentService.deleteRepackagingAttachment(parseInt($scope.repackaging.id), parseInt(att.sequenceNo))
				.then(function(data){
					$scope.loadAttachment($scope.repackaging.id);
					$scope.selectedAttachement = false;
				});
			}
		})
	}
	
    function addRepackaging() {
      	 repackagingService.addRepackaging($scope.jobNo)
      	 .then(
   			 function( data ) {
   				loadRepacakgingData();
   				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been unlocked.");
				}
   			 });
       }
    
    function updateRepackaging() {
     	 repackagingService.updateRepackaging($scope.repackaging)
     	 .then(
  			 function( data ) {
  				loadRepacakgingData();
  				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Remarks has been updated.");
				}
  			 });
      }
    
    
	
    function deleteRepackaging() {
     	 repackagingService.deleteRepackaging($scope.repackaging.id)
     	 .then(
  			 function( data ) {
  				loadRepacakgingData();
  				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been reset.");
				}
  			 });
      }
    
    function generateSnapshot() {
    	 repackagingService.generateSnapshot($scope.repackaging.id, $scope.jobNo)
    	 .then(
 			 function( data ) {
 				loadRepacakgingData();
 				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Snapshot has been generated.");
				}
 			 });
     }
    
    //Attachment
	$scope.partialDownloadLink = 'http://localhost:8080/QSrevamp2/download?filename=';
    $scope.filename = '';

    $scope.uploadFile = function() {
    	console.log("Upload file");
        $scope.processDropzone();
    };

    $scope.reset = function() {
    	console.log("Reset file");
        $scope.resetDropzone();
    };
	
	
	
	
}]);