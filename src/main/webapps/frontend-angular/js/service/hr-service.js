mainApp.service('hrService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper) {
	// Return public API.
    return({
    	findAll:	findAll,
    	findByEmail:	findByEmail,
    	findByEmployeeId:	findByEmployeeId,
    	findByUsername:	findByUsername,
    	findByUsernameIsNotNull: findByUsernameIsNotNull
    });
   
    function findAll(){
    	var request = $http.get('service/hr/findAll');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function findByEmail(){
    	var request = $http.get('service/hr/findByEmail');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function findByEmployeeId(){
    	var request = $http.get('service/hr/findByEmployeeId');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function findByUsername(){
    	var request = $http.get('service/hr/findByUsername');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function findByUsernameIsNotNull(){
    	var request = $http.get('service/hr/findByUsernameIsNotNull');
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);




