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

mainApp.factory('Base64', function() {
	var keyStr = 'ABCDEFGHIJKLMNOP' +
	'QRSTUVWXYZabcdef' +
	'ghijklmnopqrstuv' +
	'wxyz0123456789+/' +
	'=';
	return {
		encode: function (input) {
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;

			do {
				chr1 = input.charCodeAt(i++);
				chr2 = input.charCodeAt(i++);
				chr3 = input.charCodeAt(i++);

				enc1 = chr1 >> 2;
				enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
				enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
				enc4 = chr3 & 63;

				if (isNaN(chr2)) {
					enc3 = enc4 = 64;
				} else if (isNaN(chr3)) {
					enc4 = 64;
				}

				output = output +
				keyStr.charAt(enc1) +
				keyStr.charAt(enc2) +
				keyStr.charAt(enc3) +
				keyStr.charAt(enc4);
				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";
			} while (i < input.length);

			return output;
		},

		decode: function (input) {
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;

			// remove all characters that are not A-Z, a-z, 0-9, +, /, or =
			var base64test = /[^A-Za-z0-9\+\/\=]/g;
			if (base64test.exec(input)) {
				alert("There were invalid base64 characters in the input text.\n" +
						"Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
				"Expect errors in decoding.");
			}
			input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

			do {
				enc1 = keyStr.indexOf(input.charAt(i++));
				enc2 = keyStr.indexOf(input.charAt(i++));
				enc3 = keyStr.indexOf(input.charAt(i++));
				enc4 = keyStr.indexOf(input.charAt(i++));

				chr1 = (enc1 << 2) | (enc2 >> 4);
				chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
				chr3 = ((enc3 & 3) << 6) | enc4;

				output = output + String.fromCharCode(chr1);

				if (enc3 != 64) {
					output = output + String.fromCharCode(chr2);
				}
				if (enc4 != 64) {
					output = output + String.fromCharCode(chr3);
				}

				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";

			} while (i < input.length);

			return output;
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

mainApp.factory('GlobalHelper', function GlobalHelperFactory(){
	return{
		handleError: handleError,
		handleSuccess: handleSuccess,
		checkNull: checkNull,
		containRole: containRole,
		numberClass: numberClass
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
})

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
