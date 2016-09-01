mainApp.service('transitService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	obtainTransitCodeMatcheList:		obtainTransitCodeMatcheList,
    	obtainTransitUomMatcheList:			obtainTransitUomMatcheList,
    	transitUpload:						transitUpload,
    	getTransit:							getTransit,
    	getTransitBQItems:					getTransitBQItems,
    	getTransitResources:				getTransitResources,
    	confirmResourcesAndCreatePackages: 	confirmResourcesAndCreatePackages,
    	completeTransit:					completeTransit,
    	saveTransitResources:				saveTransitResources,
    	saveTransitResourcesList:			saveTransitResourcesList,
    	createOrUpdateTransitHeader: 		createOrUpdateTransitHeader
    });
   
    function obtainTransitCodeMatcheList(){
    	var request = $http.post("service/transit/obtainTransitCodeMatcheList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function obtainTransitUomMatcheList(){
    	var request = $http.post("service/transit/obtainTransitUomMatcheList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function transitUpload(formData){
    	var request = $http({
			method : 'POST',
			url : 'gammonqs/transitUpload.smvc',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getTransit(jobNo){
    	var request = $http({
    			method: 'POST',
    			url:	'service/transit/getTransit',
    			params: {
    				jobNumber: jobNo
    			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getTransitBQItems(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/getTransitBQItems',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getTransitResources(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/getTransitResources',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function confirmResourcesAndCreatePackages(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/confirmResourcesAndCreatePackages',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function completeTransit(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/confirmResourcesAndCreatePackages',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function saveTransitResources(jobNo, resources){
    	var defer = $q.defer();
			$http({
				method: 'POST',
				url: 'service/transit/saveTransitResources',
	    		params:{
	    			jobNumber: jobNo,
	    		}, 
	    		data: resources
			})
			.then(function(response) {
				defer.resolve(response.data)
			}, function(response){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.statusText+':'+response.data);
				defer.reject(response.data);
			});
			return defer.promise;
    }

    function saveTransitResourcesList(jobNo, resourcesList){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/saveTransitResourcesList',
    		params:{
    			jobNumber: jobNo
    		},
    		data: resourcesList
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function createOrUpdateTransitHeader(jobNo, estimateNo, matchingCode, newJob){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/createOrUpdateTransitHeader',
    		params:{
    			jobNo: jobNo,
    			estimateNo: estimateNo,
    			matchingCode: matchingCode,
    			newJob: newJob
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
//        return( $q.reject( response.data.message ) );
//    }
//    // Transform the successful response, unwrapping the application data
//    // from the API response payload.
//    function handleSuccess( response ) {
//        return( response.data );
//    }
}]);




