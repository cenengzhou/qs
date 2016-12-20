mainApp.service('systemService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper) {
	// Return public API.
    return({
    	getAuditTableMap:		getAuditTableMap,
    	housekeepAuditTable:	housekeepAuditTable,
    	
    	getAllTriggers:				getAllTriggers,
    	updateQrtzTriggerList:		updateQrtzTriggerList
    });
   
    function getAuditTableMap(){
    	var request = $http.post('service/system/getAuditTableMap');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function housekeepAuditTable(tableName){
    	var request = $http.post('service/system/housekeepAuditTable?tableName='+tableName);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAllTriggers(){
    	var request = $http.post('service/system/getAllTriggers');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateQrtzTriggerList(triggers){
    	var request = $http({
            method: "post",
            url: "service/system/updateQrtzTriggerList",
            dataType: "application/json;charset=UTF-8",
            data:    	triggers
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
}]);




