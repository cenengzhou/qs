//Return modal window 
mainApp.factory('modalService', ['$uibModal', function( $uibModal) {
	return {
		open: function(size, templateUrl, controller, type, message) {

			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: templateUrl,
				controller: controller,
				size: size,
				keyboard: false,
				//windowClass: 'modal-vertical-centered',
				backdrop: false,
				resolve: {
					modalType: function () {
						return type;
					},
					modalMessage: function () {
						return message;
					}
				}
			});

		}
	};
}]);

/*mainApp.factory('modalMessageService', ['$uibModal', function( $uibModal) {
	return {
		open: function(templateUrl, controller, message) {
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: templateUrl,
				controller: controller,
				size: 'md',
				keyboard: true,
				backdrop: true,
				resolve: {
					items: function () {
						return message;
					}
				}
			});
		}
	};
}]);*/

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




/*//$scope.items = ['item1', 'item2', 'item3'];

$scope.animationsEnabled = true;

$scope.open = function (size) {

  var modalInstance = $uibModal.open({
    animation: $scope.animationsEnabled,
    templateUrl: 'view/subcontract/subcontract-create.html',
    controller: 'SubcontractCreateModalCtrl',
    size: size,
    backdrop: false,
    resolve: {
      items: function () {
        return $scope.items;
      }
    }
  });

  modalInstance.result.then(function (selectedItem) {
    $scope.selected = selectedItem;
  }, function () {
    $log.info('Modal dismissed at: ' + new Date());
    //window.alert('Hello');
  });
};

$scope.toggleAnimation = function () {
  $scope.animationsEnabled = !$scope.animationsEnabled;
};*/