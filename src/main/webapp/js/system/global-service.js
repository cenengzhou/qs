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
	                $scope.modalOptions.yes = function () {
	                	$uibModalInstance.close("Yes");
	                };
	                $scope.modalOptions.no = function () {
	                	$uibModalInstance.close("No");
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

mainApp.factory('SessionHelper',['$http', '$q', function SessionHelperFactory($http, $q){
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

mainApp.factory('GlobalHelper', ['$q', 'modalService', '$sce', '$http', 'uiGridConstants',
	function GlobalHelperFactory($q, modalService, $sce, $http, uiGridConstants){
	return{
		handleError: handleError,
		handleSuccess: handleSuccess,
		checkNull: checkNull,
		containRole: containRole,
		numberClass: numberClass,
		formTemplate: formTemplate,
		addressBookRowTemplate: addressBookRowTemplate,
		attachmentIconClass: attachmentIconClass,
		getSubcontractorDetailsModal: getSubcontractorDetailsModal,
		addSubcontractCatalogGroup: addSubcontractCatalogGroup,
		uiGridFilters : uiGridFilters,
		camelToNormalString: camelToNormalString,
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
	
	/**
	 * 1. remove comment to show content only for PCMS
	 * eg: <!-- PCMS start <table><tr><td>PCMS content</td></tr></table> PCMS end -->
	 * 2. remove content only for AP
	 * eg: <!-- AP start -->AP content<!-- AP end -->
	 * 3. show only content within body tag
	 * eg: <body>Some content</body>
	 */
	function formTemplate(data){
		var htmlStr = data.htmlStr
		htmlStr = htmlStr.replace(/<!-- PCMS start /g, '').replace(/ PCMS end -->/g, '').replace(/Arial/g, 'Nunito');
		htmlStr = htmlStr.replace(/<!-- AP start -->([\s\S]*?)<!-- AP end -->/gmi, '');
		var bodyStart = htmlStr.indexOf('<body');
		var bodyEnd = htmlStr.indexOf('</body>');
		var html = $sce.trustAsHtml(htmlStr.substring(bodyStart, bodyEnd)).toString();
		return html;
	}
	
	function addressBookRowTemplate(titleField, parameterField){
		return '<div style="cursor:pointer" ng-click="grid.appScope.GlobalHelper.getSubcontractorDetailsModal(row.entity.' + parameterField + ')">' +
        '  <div ng-if="row.entity.merge">{{row.entity.title}}</div>' +
        '  <div title="Click to view more details of {{row.entity.' + titleField + '}}" ng-if="!row.entity.merge" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
        '</div>';
	}
	
	function attachmentIconClass(fileType){
		var iconClass = 'fa fa-2x fa-file-o';
		switch(fileType.toLowerCase()){
		case '.jpg':
		case '.png':
		case '.bmp':
		case '.gif':
			iconClass = 'fa fa-2x fa-file-image-o';
			break;
		case '.pdf':
			iconClass = 'fa fa-2x fa-file-pdf-o';
			break;
		case '.xls':
		case '.csv':
			iconClass = 'fa fa-2x fa-file-excel-o';
			break;
		case '.txt':
			iconClass = 'fa fa-2x fa-file-text-o';
			break;
		default:
			iconClass = 'fa fa-2x fa-file-o';
		}
		return iconClass;
	}
	
	function getSubcontractorDetailsModal(vendorNo){		
		modalService.open('lg', 'view/subcontractor-details-modal.html', 'SubcontractorDetailsModalCtrl', 'Success', vendorNo ); 
	}
	
	function addSubcontractCatalogGroup(data, groupBy){
		data.forEach(function(item){
			switch(item[groupBy]){
			case 'BQ':
			case 'B1':
				item.catalog = 'BQ Item';
				break;
			case 'V1':
			case 'V2':
			case 'V3':
			case 'L1':
			case 'L2':
			case 'D1':
			case 'D2':
			case 'CF':
				item.catalog = 'Addendum';
				break;
			case 'RR':
			case 'RA':
				item.catalog = 'Retention'
				break;
			case 'C1':
			case 'C2':
			case 'AP':
			case 'MS':
			case 'OA':
			default:
				item.catalog = 'Others';
				break;
			}
		})
	}
	
	function uiGridFilters(filterNameList){
		var filters = [];
		filters["GREATER_THAN"] = {condition: uiGridConstants.filter.GREATER_THAN, placeholder: 'greater than'};
		filters["LESS_THAN"] = {condition: uiGridConstants.filter.LESS_THAN, placeholder: 'less than'};
		var resultList = [];
		filterNameList.forEach(function(filterName){
			if(filters[filterName]){
				resultList.push(filters[filterName]);
			}
		})
		return resultList;
	}
	
	function camelToNormalString (text){
		return text.replace(/([A-Z])/g, ' $1')
	    .replace(/^./, function(str){ return str.toUpperCase(); });
	}
}]);

mainApp.factory('$exceptionHandler', ['$log', '$injector', function($log, $injector) {
  return function myExceptionHandler(exception, cause) {
//	  var http = $injector.get('$http');
//	  http({
//          method: "post",
//          url: "service/logging/logToBackend",
//          dataType: "application/json;charset=UTF-8",
//          data:    	{exception: exception.stack.substring(0,255), cause: cause}
//      }).then(function(response){
//    	  $log.warn('logToBackend  status:' + response.status);
		  $log.warn(exception, cause);
//	  });
    
  };
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
