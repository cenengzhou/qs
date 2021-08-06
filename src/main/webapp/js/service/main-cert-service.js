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
        getMainCertNoList:					getMainCertNoList,
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
        updateCertificateByAdmin:			updateCertificateByAdmin,
        updateMainCertFromF03B14Manually: 	updateMainCertFromF03B14Manually,
        deleteMainCert:						deleteMainCert,
        getLabelCss:						getLabelCss,
        getCertColCss:						getCertColCss,
        getMvmtColCss:						getMvmtColCss,
        calculateMovement:					calculateMovement,
        calculateCertFromMovement:			calculateCertFromMovement,
        checkFieldChanged:			checkFieldChanged
    });
	
    function getLabelCss(isMovementColumnShown) {
        return {'col-md-6' : isMovementColumnShown, 'col-md-7' : !isMovementColumnShown};
    }

    function getCertColCss(isMovementColumnShown) {
        return {'col-md-3' : isMovementColumnShown, 'col-md-5' : !isMovementColumnShown};
    }

    function getMvmtColCss(isMovementColumnShown) {
        return {'col-md-3' : isMovementColumnShown, 'hidden' : !isMovementColumnShown};
    }

    function calculateMovement(certMvmt, currCert, prevCert, filterTerm) {
        if (!currCert || !prevCert || !certMvmt || !filterTerm)
            return
        var result = certMvmt;
        Object.keys(currCert).filter(function(x) {
            return x.substring(0, filterTerm.length) == filterTerm
        }).forEach(function(x) {
            var currKey = x
            var currValue = currCert[currKey]
            var prevKey = x
            var prevValue = prevCert[prevKey]
            result[currKey] = currValue - prevValue
        })
        return result
    }

    function calculateCertFromMovement(cert, prevCert, certMvmt) {
        if (!prevCert || !certMvmt || !cert)
            return
        var result = cert
        Object.keys(certMvmt).forEach(function(x) {
            var certMvmtKey = x
            var certMvmtValue = certMvmt[certMvmtKey]
            var prevKey = x
            var prevValue = prevCert[prevKey]
            result[certMvmtKey] = prevValue + certMvmtValue
        })
        return result
    }

    function checkFieldChanged(newCertObj, originalCertObj, filterTerm) {
        var result = false;
        Object.keys(originalCertObj)
            .filter(function (x) {
                return x.substring(0, filterTerm.length) == filterTerm
            })
            .forEach(function (x) {
                var key = x
                var value = parseFloat(newCertObj[key]).toFixed(2)
                var key2 = x
                var value2 = parseFloat(originalCertObj[key2]).toFixed(2)
                if (value != value2) {
                    result = true;
                }
            })
        return result;
    }
    
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
    
    
    function getMainCertNoList(noJob) {
        var request = $http({
            method: "get",
            url: "service/mainCert/getMainCertNoList",
            params: {
            	noJob: noJob
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    //Asyn Call
    function getCertificateDashboardData(noJob, year){
    	var deferred = $q.defer();
    	$http({
    		method: 'GET',
    		url: 'service/mainCert/getCertificateDashboardData',
    		 params: {
             	 noJob: noJob,
                 year: year
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
        
    
    function updateCertificateByAdmin(mainCert){
    	var request = $http.post("service/mainCert/updateCertificateByAdmin", mainCert);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteMainCert(jobNo, mainCertNo) {
        var request = $http({
            method: "DELETE",
            url: "service/mainCert/deleteMainCert",
            params: {
            	jobNo: jobNo,
            	mainCertNo: mainCertNo
            } 
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    

}]);

