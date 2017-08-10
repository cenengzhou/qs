mainApp.service('systemService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper) {
	// Return public API.
    return({
    	getAuditTableMap:		getAuditTableMap,
    	housekeepAuditTable:	housekeepAuditTable,
    	
    	getAllTriggers:				getAllTriggers,
    	updateQrtzTriggerList:		updateQrtzTriggerList,
    	getSCStandardTermsList:								getSCStandardTermsList,
    	createSystemConstant: 								createSystemConstant,
    	updateMultipleSystemConstants:						updateMultipleSystemConstants,
    	deleteSCStandardTerms:							deleteSCStandardTerms,

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
    
	function createSystemConstant(newRecord){
    	var request = $http.post('service/system/createSystemConstant', newRecord);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
	function updateMultipleSystemConstants(standardTerms){
    	var request = $http.post("service/system/updateMultipleSystemConstants", standardTerms);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
	function deleteSCStandardTerms(standardTerms){
     	var request = $http.post("service/system/deleteSCStandardTerms", standardTerms);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
	
	function getSCStandardTermsList(){
    	var request = $http.post("service/system/getSCStandardTermsList");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
}]);




