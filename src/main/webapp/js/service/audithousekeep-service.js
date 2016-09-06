mainApp.service('audithousekeepService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper) {
	// Return public API.
    return({
    	getAuditTableMap:		getAuditTableMap,
    	housekeepAuditTable:	housekeepAuditTable
    });
   
    function getAuditTableMap(){
    	var request = $http.post('service/audithousekeep/getAuditTableMap');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function housekeepAuditTable(tableName){
    	var request = $http.post('service/audithousekeep/housekeepAuditTable?tableName='+tableName);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);




