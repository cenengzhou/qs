//Return modal window 
mainApp.factory('modalService', ['$uibModal', function( $uibModal) {
	return {
		open: function(size, templateUrl, controller, status, param) {

			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: templateUrl,
				controller: controller,
				size: size,
				keyboard: false,
				//windowClass: 'modal-vertical-centered',
				backdrop: 'static',
				resolve: {
					modalStatus: function () {
						return status;
					},
					modalParam: function () {
						return param;
					}
				}
			});
		}
	};
}]);


mainApp.service('confirmService', ['$uibModal', function( $uibModal) {
	var modalDefaults = {
	        backdrop: 'static',
	        keyboard: false,
	        modalFade: true,
	        templateUrl: 'view/confirm-modal.html'
	    };

	    var modalOptions = {
	        closeButtonText: 'No',
	        actionButtonText: 'Yes',
	        headerText: 'Confirm',
	        bodyText: 'Perform this action?'
	    };

	    this.showModal = function (customModalDefaults, customModalOptions) {
	        if (!customModalDefaults) customModalDefaults = {};
	        customModalDefaults.backdrop = 'static';
	        return this.show(customModalDefaults, customModalOptions);
	    };

	    this.show = function (customModalDefaults, customModalOptions) {
	        //Create temp objects to work with since we're in a singleton service
	        var tempModalDefaults = {};
	        var tempModalOptions = {};

	        //Map angular-ui modal custom defaults to modal defaults defined in service
	        angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

	        //Map modal.html $scope custom properties to defaults defined in service
	        angular.extend(tempModalOptions, modalOptions, customModalOptions);

	        if (!tempModalDefaults.controller) {
	            tempModalDefaults.controller = function ($scope, $uibModalInstance) {
	                $scope.modalOptions = tempModalOptions;
	                $scope.modalOptions.ok = function () {
	                	$uibModalInstance.close("Yes");
	                };
	                $scope.modalOptions.close = function () {
	                	$uibModalInstance.dismiss('cancel');
	                };
	            }
	        }

	        return $uibModal.open(tempModalDefaults).result;
	    };
}]);


mainApp.factory('roundUtil', function() {
	return {
		round: function (value, decimals) {
			return Number(Math.round(value+'e'+decimals)+'e-'+decimals);
		}
    };
});

mainApp.factory('SessionHelper',['$http', '$rootScope', '$q', function SessionHelperFactory($http, $rootScope, $q){
	var defer = $q.defer();
	return{
		getCurrentSessionId: function(){
			return $http.post('service/GetCurrentSessionId')
			.then(function(response){
				if(typeof response.data === 'string'){
					return response.data;
				} else {
					return $q.reject(response.data);
				}
			}, function(response){
				return $q.reject(response.data);
			});
		},
		getSessionList: function(){
			return $http.post('service/GetSessionList')
			.then(function(response){
				if(typeof response.data === 'object'){
					return response.data;
				} else {
					return $q.reject(response.data);
				}
			}, function(response){
				return $q.reject(response.data);
			});
		},
		validateSession: function(){
			return $http.post('service/ValidateCurrentSession')
			.then(function(response){
				if(typeof response.data === 'boolean'){
					return response.data;
				} else {
					return $q.reject(response.data);
				}
			}, function(response){
				return $q.reject(response.data);
			});
		},
		invalidateSessionList: function(sessionIds){
			$http({
				method: "post",
				url: "service/InvalidateSessionList",
				data: sessionIds
			})
			.success(function(data) {
				defer.resolve(data)
			});
			return defer.promise;
		}
	}
}]);

mainApp.factory('GlobalHelper', ['$q', 'modalService', '$sce', function GlobalHelperFactory($q, modalService, $sce){
	return{
		handleError: handleError,
		handleSuccess: handleSuccess,
		checkNull: checkNull,
		containRole: containRole,
		numberClass: numberClass,
		formTemplate: formTemplate
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
        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', response.data.message ); 
//        return( $q.reject( response.data.message ) );
    }
    // Transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess( response ) {
        return( response.data );
    }
    
	function checkNull(objectArray){
		var result = false;
		angular.forEach(objectArray, function(obj){
			if(!result){
				if(obj === undefined) {
					result = true;
				}
			}
		});
		return result;
	}
	
	function containRole(role, roles){
		var result = false;
		angular.forEach(roles, function(r){
			if(r.RoleName === role){
				result = true;
			}
		})
		return result;
	}
	
	function numberClass(n){
		var c = 'text-right';
		 if(n < 0){
			 c +=' red';
		 }
		 return c;
	}
	
	function formTemplate(data){
		data = data.replace(/<!-- PCMS start /g, '').replace(/ PCMS end -->/g, '');
		data = data.replace(/<!-- AP start -->.*<!-- AP end -->/g, '');
		var bodyStart = data.indexOf('<body>');
		var bodyEnd = data.indexOf('</body>');
		var html = $sce.trustAsHtml(data.substring(bodyStart, bodyEnd)).toString();
		return html;
	}
	
}]);

/*mainApp.factory('modalUtils', function ($uibModalStack) {
     return {
       modalsExist: function () {
    	   console.log("modal: HI");
    	   console.log("modal: "+!!$uibModalStack.getTop());

         return !!$uibModalStack.getTop();
       },
       closeAllModals: function () {
    	   $uibModalStack.dismissAll();
       }
     };
   }
 );*/
