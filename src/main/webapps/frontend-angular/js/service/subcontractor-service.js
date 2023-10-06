mainApp.service('subcontractorService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	obtainSubcontractorWrappers:		obtainSubcontractorWrappers,
    	obtainClientWrappers:	obtainClientWrappers,
    	obtainSubconctractorStatistics:	obtainSubconctractorStatistics,
    	obtainPackagesByVendorNo: obtainPackagesByVendorNo,
    	obtainTenderAnalysisWrapperByVendorNo: obtainTenderAnalysisWrapperByVendorNo,
    	obtainWorkscopeByVendorNo:	obtainWorkscopeByVendorNo
    });
   
    function obtainSubcontractorWrappers(workScope, subcontractor){
    	var request = $http({
    		url: 'service/subcontractor/obtainSubcontractorWrappers',
    		method: 'POST',
    		params: {
    			workScope: workScope,
    			subcontractor: subcontractor
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainClientWrappers(client){
    	var request = $http({
    		url: 'service/subcontractor/obtainClientWrappers',
    		method: 'POST',
    		params: {
    			client: client
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function obtainSubconctractorStatistics(vendorNo){
    	var request = $http({
    		url: 'service/subcontractor/obtainSubconctractorStatistics',
    		method: 'POST',
    		params: {
    			vendorNo: vendorNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainPackagesByVendorNo(vendorNo){
    	var request = $http({
    		url: 'service/subcontractor/obtainPackagesByVendorNo',
    		method: 'POST',
    		params: {
    			vendorNo: vendorNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainTenderAnalysisWrapperByVendorNo(vendorNo){
    	var request = $http({
    		url: 'service/subcontractor/obtainTenderAnalysisWrapperByVendorNo',
    		method: 'POST',
    		params: {
    			vendorNo: vendorNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function obtainWorkscopeByVendorNo(vendorNo){
    	var request = $http({
    		url: 'service/subcontractor/obtainWorkscopeByVendorNo',
    		method: 'POST',
    		params: {
    			vendorNo: vendorNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




