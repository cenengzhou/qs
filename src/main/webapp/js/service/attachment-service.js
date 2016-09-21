mainApp.service('attachmentService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({

    	uploadRepackingAttachment:		uploadRepackingAttachment,
    	deleteRepackagingAttachment:	deleteRepackagingAttachment,
    	getRepackagingAttachments:		getRepackagingAttachments,
    	saveRepackagingTextAttachment:	saveRepackagingTextAttachment,
    	addRepackagingTextAttachment:	addRepackagingTextAttachment,
    
    	uploadSCAttachment:				uploadSCAttachment,
    	deleteAttachment:				deleteAttachment,
    	getAttachmentListForPCMS:		getAttachmentListForPCMS,
    	uploadTextAttachment:			uploadTextAttachment,
    	
    	getMainCertFileAttachment:		getMainCertFileAttachment,
    	addMainCertFileAttachment:		addMainCertFileAttachment,
    	
    	obtainAttachmentList:			obtainAttachmentList,
    	uploadAddendumAttachment:		uploadAddendumAttachment,
    	deleteAddendumAttachment:		deleteAddendumAttachment,
    	uploadAddendumTextAttachment:	uploadAddendumTextAttachment
    });
   
    function uploadRepackingAttachment(formData){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/uploadRepackingAttachment',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function deleteRepackagingAttachment(repackagingEntryID, sequenceNo){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/deleteRepackagingAttachment',
            params: {
            	repackagingEntryID: repackagingEntryID,
            	sequenceNo: sequenceNo
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getRepackagingAttachments(repackagingEntryID){
    	var request = $http({
    		method: 'POST',
    		url: 'service/attachment/getRepackagingAttachments',
    		params:{
    			repackagingEntryID: repackagingEntryID
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function saveRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/saveRepackagingTextAttachment',
            params: {
            	repackagingEntryID: repackagingEntryID,
            	sequenceNo: sequenceNo,
            	fileName: fileName,
            	textAttachment: textAttachment
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
   
    function addRepackagingTextAttachment(repackagingEntryID, sequenceNo, fileName, textAttachment){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/addRepackagingTextAttachment',
            params: {
            	repackagingEntryID: repackagingEntryID,
            	sequenceNo: sequenceNo,
            	fileName: fileName,
            	textAttachment: textAttachment
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
   
    function uploadSCAttachment(formData){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/uploadSCAttachment',
			data : formData,
			headers : {
				'Content-Type' : undefined
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

    function getAttachmentListForPCMS(nameObject, textKey){
    	var request = $http({
    		method: 'POST',
    		url: 'service/attachment/getAttachmentListForPCMS',
    		params:{
    			nameObject: nameObject,
    			textKey: textKey
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
   
    function getMainCertFileAttachment(noJob, noMainCert, noSequence){
    	var request = $http({
    		method: 'POST',
    		url: 'service/attachment/getMainCertFileAttachment',
    		params:{
    			noJob: noJob,
    			noMainCert: noMainCert,
    			noSequence: noSequence    			
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function addMainCertFileAttachment(formData){
    	var request = $http({
    		method: 'POST',
    		url: 'service/attachment/addMainCertFileAttachment',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
   
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
    
    function uploadAddendumAttachment(formData){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/uploadAddendumAttachment',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteAddendumAttachment(nameObject, textKey, sequenceNumber){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/deleteAddendumAttachment',
            params: {
            	nameObject: nameObject,
            	textKey: textKey,
            	sequenceNumber: sequenceNumber
            }
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function uploadAddendumTextAttachment(nameObject, textKey, sequenceNo, fileName, textAttachment){
    	var request = $http({
			method : 'POST',
			url : 'service/attachment/uploadAddendumTextAttachment',
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

}]);




