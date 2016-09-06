mainApp.service('mainCertService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	getCertificateList: 				getCertificateList,

        getPaidMainCertList:				getPaidMainCertList,
        getCertificate: 					getCertificate,
        getMainCertReceiveDateAndAmount:	getMainCertReceiveDateAndAmount,
        getCertificateDashboardData: 		getCertificateDashboardData,
        getRetentionReleaseList: 			getRetentionReleaseList,
        getLatestMainCert:					getLatestMainCert,
        getCumulativeRetentionReleaseByJob:	getCumulativeRetentionReleaseByJob,
        getMainCertContraChargeList:		getMainCertContraChargeList,
        
        createMainCert:						createMainCert,
        insertIPA:							insertIPA,
        confirmIPC:							confirmIPC,
        resetIPC:							resetIPC,
        postIPC:							postIPC,
        submitNegativeMainCertForApproval:	submitNegativeMainCertForApproval,
        getCalculatedRetentionRelease:		getCalculatedRetentionRelease,
        updateRetentionRelease: 			updateRetentionRelease,
        updateMainCertContraChargeList:		updateMainCertContraChargeList,
        deleteMainCertContraCharge:			deleteMainCertContraCharge,
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
    
    
    function getMainCertContraChargeList(noJob, noMainCert) {
        var request = $http({
            method: "get",
            url: "service/mainCert/getMainCertContraChargeList",
            params: {
            	noJob: noJob,
            	noMainCert: noMainCert
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
    
    
    function getCumulativeRetentionReleaseByJob(noJob, noMainCert) {
        var request = $http({
            method: "GET",
            url: "service/mainCert/getCumulativeRetentionReleaseByJob",
            params: {
            	noJob: noJob,
            	noMainCert: noMainCert
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function createMainCert(mainCert) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/createMainCert",
            data: mainCert
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function insertIPA(mainCert) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/insertIPA",
            data: mainCert
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function confirmIPC(mainCert) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/confirmIPC",
            data: mainCert
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function resetIPC(mainCert) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/resetIPC",
            data: mainCert
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function postIPC(noJob, noMainCert) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/postIPC",
            params: {
            	noJob: noJob,
            	noMainCert: noMainCert
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function submitNegativeMainCertForApproval(noJob, noMainCert, certAmount) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/submitNegativeMainCertForApproval",
            params: {
            	noJob: noJob,
            	noMainCert: noMainCert, 
            	certAmount: certAmount
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getCalculatedRetentionRelease(noJob, noMainCert) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/getCalculatedRetentionRelease",
            params: {
            	noJob: noJob,
            	noMainCert: noMainCert 
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function updateRetentionRelease(noJob, retentionReleaseList) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/updateRetentionRelease",
            params: {
            	noJob: noJob
            },
            data: retentionReleaseList 
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function updateMainCertContraChargeList(noJob, noMainCert, contraChargeList) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/updateMainCertContraChargeList",
            params: {
            	noJob: noJob,
            	noMainCert: noMainCert
            },
            data: contraChargeList 
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    
    function deleteMainCertContraCharge(mainCertContraCharge) {
        var request = $http({
            method: "POST",
            url: "service/mainCert/deleteMainCertContraCharge",
            data: mainCertContraCharge 
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateMainCertFromF03B14Manually(){
    	var request = $http.post("service/mainCert/updateMainCertFromF03B14Manually");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function updateCertificate(mainCert){
    	var request = $http.post("service/mainCert/updateCertificate", mainCert);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
        
    

}]);

