mainApp.service('mainCertService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	getCertificateList: 				getCertificateList,

        getPaidMainCertList:				getPaidMainCertList,
        getCertificate: 					getCertificate,
        getMainCertReceiveDateAndAmount:	getMainCertReceiveDateAndAmount,
        getCertificateDashboardData: 		getCertificateDashboardData,
        getRetentionReleaseList: 			getRetentionReleaseList,
        getLatestMainCert:			getLatestMainCert,
        
        updateCertificate: 					updateCertificate,
        updateMainCertFromF03B14Manually: 	updateMainCertFromF03B14Manually,
    });
	
    
    
    function getCertificateList(noJob) {
    	var request = $http({
            method: "get",
            url: "service/mainCert/getCertificateList",
            params: {
            	noJob: noJob
            }
        });
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getPaidMainCertList(noJob) {
        var request = $http({
            method: "get",
            url: "service/mainCert/getPaidMainCertList",
            params: {
            	noJob: noJob
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
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
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getRetentionReleaseList(noJob) {
        var request = $http({
            method: "get",
            url: "service/mainCert/getRetentionReleaseList",
            params: {
            	noJob: noJob
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getLatestMainCert(noJob, status) {
    	var request = $http({
            method: "get",
            url: "service/mainCert/getLatestMainCert",
            params: {
            	noJob: noJob,
            	status: status
            }
        });
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    //Asyn Call
    function getCertificateDashboardData(noJob, type, year, month){
    	var deferred = $q.defer();
    	$http({
    		method: 'GET',
    		url: 'service/mainCert/getCertificateDashboardData',
    		 params: {
             	noJob: noJob,
             	type: type,
             	year: year,
             	month: month
             }
    	}).success(function(data) { 
    		deferred.resolve(data);
    	}).error(function(msg, code) {
    		deferred.reject(msg);
    		$log.error(msg, code);
    	});
    	return deferred.promise;
    }
    
    function getMainCertReceiveDateAndAmount(company, refDocNo) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/getMainCertReceiveDateAndAmount",
            params: {
            	company: company,
            	refDocNo: refDocNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateMainCertFromF03B14Manually(){
    	var request = $http.post("service/mainCert/updateMainCertFromF03B14Manually");
    	return( request.then( handleSuccess, handleError ) );
    }
    
    
    function updateCertificate(mainCert){
    	var request = $http.post("service/mainCert/updateCertificate", mainCert);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
        
   

//    // ---
//    // PRIVATE METHODS.
//    // ---
//    // Transform the error response, unwrapping the application dta from
//    // the API response payload.
//    function handleError( response ) {
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

