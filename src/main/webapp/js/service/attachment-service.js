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
    	uploadTextAttachment:			uploadTextAttachment
    	
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
   
//    // ---
//    // PRIVATE METHODS.
//    // ---
//    // Transform the error response, unwrapping the application dta from
//    // the API response payload.
//    function handleError( response) {
//        // The API response from the server should be returned in a
//        // normalized format. However, if the request was not handled by the
//        // server (or what not handles properly - ex. server error), then we
//        // may have to normalize it on our end, as best we can.
//        if (
//            ! angular.isObject( response.data ) ||
//            ! response.data.message
//            ) {
//            return( $q.reject( "An unknown error occurred." ) );
//        }
//        // Otherwise, use expected error message.
//        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.data.message );
////        return( $q.reject( response.data.message ) );
//    }
//    // Transform the successful response, unwrapping the application data
//    // from the API response payload.
//    function handleSuccess( response ) {
//        return( response.data );
//    }
}]);




