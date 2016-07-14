mainApp.service('mainCertService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
        getMainCertList: getMainCertList,

        getPaidMainCertList:	getPaidMainCertList,
        getCertificate: getCertificate,
        updateCertificate: updateCertificate,
        updateMainCertFromF03B14Manually: updateMainCertFromF03B14Manually,
    });
	
    function getMainCertList() {
        var request = $http({
            method: "get",
            url: "data/cert-data.json"
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    function getPaidMainCertList(noJob) {
        var request = $http({
            method: "get",
            url: "service/mainCert/getPaidMainCertList",
            params: {
            	noJob: noJob
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }
    
    
    function updateMainCertFromF03B14Manually(){
    	var request = $http.post("service/mainCert/updateMainCertFromF03B14Manually");
    	return( request.then( handleSuccess, handleError ) );
    }
    
    function getCertificate(jobNo, certificateNumber) {
        var request = $http({
            method: "get",
            url: "service/mainCert/getCertificate",
            params: {
            	jobNo: jobNo,
            	certificateNumber: certificateNumber
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }

    function updateCertificate(mainCert){
    	var request = $http.post("service/mainCert/updateCertificate", mainCert);
    	return( request.then( handleSuccess, handleError ) );
    }
        
    // ---
    // PRIVATE METHODS.
    // ---
    // Transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError( response ) {
        // The API response from the server should be returned in a
        // normalized format. However, if the request was not handled by the
        // server (or what not handles properly - ex. server error), then we
        // may have to normalize it on our end, as best we can.
        if (
            ! angular.isObject( response.data ) ||
            ! response.data.message
            ) {
            return( $q.reject( "An unknown error occurred." ) );
        }
        // Otherwise, use expected error message.
        return( $q.reject( response.data.message ) );
    }
    // Transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess( response ) {
        return( response.data );
    }
}]);

