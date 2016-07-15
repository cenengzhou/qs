mainApp.service('paymentService', ['$http', '$q',  function($http, $q){
	// Return public API.
    return({
    	getPaymentCertList: 	getPaymentCertList,
    	getLatestPaymentCert:	getLatestPaymentCert,
    	getPaymentCert: 		getPaymentCert,
    	getPaymentDetailList: 	getPaymentDetailList,
    	getPaymentCertSummary: 	getPaymentCertSummary, 
    	createPayment:			createPayment,
    	calculatePaymentDueDate:calculatePaymentDueDate,
    	updatePaymentCert:		updatePaymentCert,
    	updateF58011FromSCPaymentCertManually: updateF58011FromSCPaymentCertManually,
    	runPaymentPosting:		runPaymentPosting
    	
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
        return( request.then( handleSuccess, handleError ) );
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
        return( request.then( handleSuccess, handleError ) );
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
        return( request.then( handleSuccess, handleError ) );
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
        return( request.then( handleSuccess, handleError ) );
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
        return( request.then( handleSuccess, handleError ) );
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
        return( request.then( handleSuccess, handleError ) );
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
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updatePaymentCert(paymentCert) {
        var request = $http.post('service/payment/updatePaymentCert', paymentCert)
        return( request.then( handleSuccess, handleError ) );
    }
    
    function updateF58011FromSCPaymentCertManually(){
    	var request = $http.post("service/payment/updateF58011FromSCPaymentCertManually");
    	return( request.then( handleSuccess, handleError ) );
    }

    function runPaymentPosting(){
    	var request = $http.post("service/payment/runPaymentPosting");
    	return( request.then(handleSuccess, handleError));
    }
    
   // ---
    // PRIVATE METHODS.
    // ---
    // Transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError( response) {
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




