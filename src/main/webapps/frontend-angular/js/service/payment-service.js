mainApp.service('paymentService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	getPaymentCertList: 					getPaymentCertList,
    	getLatestPaymentCert:					getLatestPaymentCert,
    	getPaymentCert: 						getPaymentCert,
    	getPaymentDetailList: 					getPaymentDetailList,
    	getPaymentCertSummary: 					getPaymentCertSummary, 
    	getGSTAmount:							getGSTAmount,
    	getTotalPostedCertAmount: 				getTotalPostedCertAmount,
    	getPaymentResourceDistribution:			getPaymentResourceDistribution,
    	
    	createPayment:							createPayment,
    	updatePaymentCertificate: 				updatePaymentCertificate,
    	calculatePaymentDueDate:				calculatePaymentDueDate,
    	updatePaymentDetails:					updatePaymentDetails,
    	updatePaymentCert:						updatePaymentCert,
    	submitPayment:							submitPayment,
    	updateF58011FromSCPaymentCertManually: 	updateF58011FromSCPaymentCertManually,
    	runPaymentPosting:						runPaymentPosting,
    	obtainPaymentCertificateList:			obtainPaymentCertificateList,
    	deletePendingPaymentAndDetails:			deletePendingPaymentAndDetails,

		generatePaymentPDFAdmin:				generatePaymentPDFAdmin
    });
	
    function getPaymentCertList(jobNo, subcontractNo) {
    	var myHeaders = {
    	        "Accept": "application/json",
    	        "Content-Type": "application/json",
    	        //'Authorization': 'Basic ' + Base64.encode("peer" + ":" + "bkend-srv-1234")
    	    };
    	
        var request = $http({
            method: "get",
            //headers: myHeaders,
            url: "service/payment/getPaymentCertList",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
    
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getLatestPaymentCert(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/payment/getLatestPaymentCert",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            }
    
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getPaymentCert(jobNo, subcontractNo, paymentCertNo) {
        var request = $http({
            method: "get",
            url: "service/payment/getPaymentCert",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	paymentCertNo: paymentCertNo
            	
            }
    
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getPaymentDetailList(jobNo, subcontractNo, paymentCertNo) {
        var request = $http({
            method: "get",
            url: "service/payment/getPaymentDetailList",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	paymentCertNo: paymentCertNo
            	
            }
    
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getPaymentCertSummary(jobNo, subcontractNo, paymentCertNo) {
        var request = $http({
            method: "get",
            url: "service/payment/getPaymentCertSummary",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	paymentCertNo: paymentCertNo
            	
            }
    
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getGSTAmount(jobNo, subcontractNo, paymentCertNo, lineType) {
        var request = $http({
            method: "get",
            url: "service/payment/getGSTAmount",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	paymentCertNo: paymentCertNo,
            	lineType: lineType
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getTotalPostedCertAmount(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/payment/getTotalPostedCertAmount",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getPaymentResourceDistribution(jobNo, subcontractNo, lineType, dataType, paymentCertNo){
    	var deferred = $q.defer();
    	$http({
    		method: 'get',
    		url: 'service/payment/getPaymentResourceDistribution',
    		params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	lineType: lineType,
            	dataType: dataType,
            	paymentCertNo: paymentCertNo
            }
    	}).success(function(data) { 
    		deferred.resolve(data);
    	}).error(function(msg, code) {
    		deferred.reject(msg);
    		$log.error(msg, code);
    	});
    	return deferred.promise;
    }
    
    
    function calculatePaymentDueDate(jobNo, subcontractNo, mainCertNo, asAtDate, ipaOrInvoiceDate, dueDate) {
        var request = $http({
            method: "get",
            url: "service/payment/calculatePaymentDueDate",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	mainCertNo: mainCertNo,
            	asAtDate: asAtDate,
            	ipaOrInvoiceDate: ipaOrInvoiceDate,
            	dueDate: dueDate
            }
    
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function createPayment(jobNo, subcontractNo) {
        var request = $http({
            method: "post",
            url: "service/payment/createPayment",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo:subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updatePaymentCertificate(jobNo, subcontractNo, paymentCertNo, paymentTerms, gstPayable, gstReceivable, paymentCert) {
		var request = $http({
			method: "post",
			url: "service/payment/updatePaymentCertificate",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				paymentCertNo: paymentCertNo,
				paymentTerms: paymentTerms,
				gstPayable: gstPayable,
				gstReceivable: gstReceivable
			},
			data: paymentCert
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function updatePaymentDetails(jobNo, subcontractNo, paymentCertNo, paymentType, paymentDetails) {
		var request = $http({
			method: "post",
			url: "service/payment/updatePaymentDetails",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				paymentCertNo: paymentCertNo,
				paymentType: paymentType
			},
			data: paymentDetails
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function submitPayment(jobNo, subcontractNo, paymentCertNo) {
		var request = $http({
			method: "post",
			url: "service/payment/submitPayment",
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				subcontractNo: subcontractNo,
				paymentCertNo: paymentCertNo,
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
    
    function updatePaymentCert(paymentCert) {
        var request = $http.post('service/payment/updatePaymentCert', paymentCert)
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateF58011FromSCPaymentCertManually(){
    	var request = $http.post("service/payment/updateF58011FromSCPaymentCertManually");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function runPaymentPosting(){
    	var request = $http.post("service/payment/runPaymentPosting");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainPaymentCertificateList(paymentCertWrapper, dueDateType) {
		var request = $http({
			method: "post",
			url: "service/payment/obtainPaymentCertificateList",
			params: {
				dueDateType: dueDateType
			},
			data: paymentCertWrapper
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
  
    function deletePendingPaymentAndDetails(paymentCertId) {
		var request = $http({
			method: "post",
			url: "service/payment/deletePendingPaymentAndDetails",
			params: {
				paymentCertId: paymentCertId
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

	function generatePaymentPDFAdmin(jobNo, packageNo, paymentNo){
		var request = $http({
			method: 'post',
			url: 'service/payment/generatePaymentPDFAdmin',
			dataType: "application/json;charset=UTF-8",
			params: {
				jobNo: jobNo,
				packageNo: packageNo,
				paymentNo: paymentNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

}]);




