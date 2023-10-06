mainApp.service('transitService', ['$http', '$q', 'GlobalHelper', 'modalService', '$log',
							function($http, $q, GlobalHelper, modalService, $log){
	// Return public API.
    return({
    	getTransitTotalAmount: 				getTransitTotalAmount,
    	getBQResourceGroupByObjectCode:		getBQResourceGroupByObjectCode,
    	getIncompleteTransitList:			getIncompleteTransitList,
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
			createOrUpdateTransitHeader: 		createOrUpdateTransitHeader,
			unlockTransitAdmin:				unlockTransitAdmin
    });
   
  //Asyn Call
    function getTransitTotalAmount(jobNo, type){
    	var deferred = $q.defer();
    	$http({
    		method: 'GET',
    		url: 'service/transit/getTransitTotalAmount',
    		params:{
    			jobNo: jobNo,
    			type: type
    		}
    	}).success(function(data) { 
    		deferred.resolve(data);
    	}).error(function(msg, code) {
    		deferred.reject(msg);
    		$log.error(msg, code);
    	});
    	return deferred.promise;
    }
    
    function getBQResourceGroupByObjectCode(jobNo){
    	var request = $http({
    		method: 'get',
    		url: 'service/transit/getBQResourceGroupByObjectCode',
    		params:{
    			jobNo: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getIncompleteTransitList(){
    	var request = $http.get("service/transit/getIncompleteTransitList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getIncompleteTransitList(){
    	var request = $http.get("service/transit/getIncompleteTransitList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function obtainTransitCodeMatcheList(){
    	var request = $http.get("service/transit/obtainTransitCodeMatcheList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function obtainTransitUomMatcheList(){
    	var request = $http.get("service/transit/obtainTransitUomMatcheList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function transitUpload(formData){
    	var request = $http({
			method : 'post',
			url : 'service/transit/transitUpload',
			data : formData,
			headers : {
				'Content-Type' : undefined
			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getTransit(jobNo){
    	var request = $http({
    			method: 'get',
    			url:	'service/transit/getTransit',
    			params: {
    				jobNumber: jobNo
    			}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getTransitBQItems(jobNo){
    	var request = $http({
    		method: 'get',
    		url: 'service/transit/getTransitBQItems',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getTransitResources(jobNo){
    	var request = $http({
    		method: 'get',
    		url: 'service/transit/getTransitResources',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function confirmResourcesAndCreatePackages(jobNo, createPackage){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/confirmResourcesAndCreatePackages',
    		params:{
    			jobNumber: jobNo,
    			createPackage: createPackage
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function completeTransit(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/completeTransit',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function saveTransitResources(jobNo, resources){
    	var deferred = $q.defer();
    	$http({
    		method: 'POST',
    		url: 'service/transit/saveTransitResources',
    		params:{
    			jobNumber: jobNo,
    		}, 
    		data: resources
    	}).success(function(data) {
    		if(data == null || data.length==0)
    			deferred.resolve(data);
    		else{
    			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
    			deferred.reject(data);
    		}
    	}).error(function(msg, code) {
    		deferred.reject(msg);
    	});
    	return deferred.promise;
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

    function unlockTransitAdmin(jobNo){
    	var request = $http({
    		method: 'POST',
    		url: 'service/transit/unlockTransitAdmin',
    		params:{
    			jobNumber: jobNo
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);




