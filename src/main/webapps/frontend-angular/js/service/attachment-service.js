mainApp.service('attachmentService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
      	obtainAttachmentList:	obtainAttachmentList,
    	deleteAttachment:		deleteAttachment,
			deleteAttachmentAdmin:		deleteAttachmentAdmin,
    	uploadTextAttachment:	uploadTextAttachment,
       	uploadAttachment:		uploadAttachment,
  });
   
    function obtainAttachmentList(nameObject, textKey){
    	var request = $http({
    		method: 'POST',
    		url: 'service/attachment/obtainAttachmentList',
    		params:{
    			nameObject: nameObject,
    			textKey: textKey
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteAttachment(nameObject, textKey, sequenceNumber){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/deleteAttachment',
            params: {
            	nameObject: nameObject,
            	textKey: textKey,
            	sequenceNumber: sequenceNumber
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteAttachmentAdmin(nameObject, textKey, sequenceNumber){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/deleteAttachmentAdmin',
            params: {
            	nameObject: nameObject,
            	textKey: textKey,
            	sequenceNumber: sequenceNumber
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
    function uploadTextAttachment(nameObject, textKey, sequenceNo, fileName, textAttachment){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/uploadTextAttachment',
            params: {
            	nameObject: nameObject,
            	textKey: textKey,
            	sequenceNo: sequenceNo,
            	fileName: fileName,
            	textAttachment: textAttachment
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function uploadAttachment(formData){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/uploadAttachment',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);
