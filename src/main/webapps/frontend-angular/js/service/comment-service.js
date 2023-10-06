mainApp.service('commentService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
	// Return public API.
    return({
    	find: find,
    	save: save
    });
	
    function find(nameTable, idTable, field) {
		var request = $http({
			method: "get",
			url: "service/comment/" + nameTable + "/" + idTable + "/" + field
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function save(comment) {
		var request = $http({
			method: "post",
			url: "service/comment/",
			data: comment
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);