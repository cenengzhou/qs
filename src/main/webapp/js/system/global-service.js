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
			return modalInstance;
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
			return $http.post('service/system/GetCurrentSessionId')
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
			return $http.post('service/system/GetSessionList')
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
			return $http.post('service/system/ValidateCurrentSession')
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
				url: "service/system/InvalidateSessionList",
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
		getAddressbookDetailsModal: getAddressbookDetailsModal,
		addSubcontractCatalogGroup: addSubcontractCatalogGroup,
		uiGridFilters : uiGridFilters,
		camelToNormalString: camelToNormalString,
		downloadFile: downloadFile
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
	
	function addressBookRowTemplate(titleField, parameterField, verdorTypeField){
		return '<div style="cursor:pointer" ng-click="grid.appScope.GlobalHelper.getAddressbookDetailsModal(row.entity.' + parameterField + ', row.entity.' + verdorTypeField + ')">' +
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
	
	function getAddressbookDetailsModal(vendorNo){
		var vendor = {'vendorNo': vendorNo};
		modalService.open('lg', 'view/addressbook-details-modal.html', 'AddressbookDetailsModalCtrl', 'Success', vendor ); 
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
		filters["GREATER_THAN"] = {
//				condition: uiGridConstants.filter.GREATER_THAN, 
				condition: function(term, value, row, column){
					if(!term) return true;
					term = term.replace(/\\/, '');
					return parseFloat(value) > parseFloat(term);
				},
				placeholder: 'greater than'
		};
		
		filters["LESS_THAN"] = {
//				condition: uiGridConstants.filter.LESS_THAN,
				condition: function(term, value, row, column){
					if(!term) return true;
					term = term.replace(/\\/, '');
					return parseFloat(value) < parseFloat(term);
					
				},
				placeholder: 'less than'
		};
		
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
	
	function downloadFile(url){
		$http({
 			method:'get',
 			responseType: 'arraybuffer',
 			url:encodeURI(url)
 		})
 		.then(function(response){
 			var type = response.headers('Content-type');
 			var disposition = response.headers('Content-Disposition');
 			var defaultFileName = 'downloadFile';
 			var fileNotFound = true;
 			if(disposition){
 				var match = disposition.match(/.*filename=\"?([^;\"]+)\"?.*/);
 				if(match && match[1]){
 					defaultFileName = match[1];
 					defaultFileName = defaultFileName.replace(/[<>:"\/\\|?*]+/g, '_')
 		 			var blob = new Blob([response.data], {type: type});
 		 			if(blob.size>0){
 		 				fileNotFound = false;
 		 				saveAs(blob, defaultFileName);
 		 			}
 				}
 			}
 			if(fileNotFound){
 				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', 'File not found');
 			}
 		});
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

mainApp.factory('dashboardHelper', ['dateFilter', function(dateFilter) {
	return {
		getDashboardDropdown: function(){
			var result = ['Latest']
			var year =  new Date().getFullYear();
			for(var i=0; i < 20; i++){
				var yearToAdd = year - i;
				if(yearToAdd > 2002){
					result.push(yearToAdd);
				}
			}
			return result;
		},
		getLast12Months: function() {
			var d = new Date()
			var months = []
			for (var i=0; i<12; i++) {
				months.push(dateFilter(d, 'MMM'))
				d.setMonth(d.getMonth()-1)
			}
			return months
		},
		findPrevYear: function(y, m) {
			return (m + 1) > 12 ? y : y - 1
		},
		findPrevMonth: function(m) {
			return (m + 1) > 12 ? m%12 + 1 : m + 1
		},
		getCurrentMonth: function() {
			var today = new Date()
			return today.getMonth() + 1
		},
		getMonthName: function(i){
			var monthNames = ['', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
			return monthNames[i]
		},
		getFullYear: function(yy) {
			return 2000+parseInt(yy)
		},
		getChartLabels: function(dataRange) {
			var result = []
			var currentDate = new Date(this.getFullYear(dataRange.startYear), dataRange.startMonth-1)
			var endDate = new Date(this.getFullYear(dataRange.endYear), dataRange.endMonth-1)
			while (currentDate <= endDate) {
				var y = currentDate.getFullYear()%100
				var m = currentDate.getMonth()+1
				result.push(this.getMonthName(m) + ' ' + y)
				currentDate.setMonth(currentDate.getMonth()+1)
			}
			return result
		},
		plotLineData: function(dataList, dataRange) {
			var result = []
			var currentDate = new Date(this.getFullYear(dataRange.startYear), dataRange.startMonth-1)
			var endDate = new Date(this.getFullYear(dataRange.endYear), dataRange.endMonth-1)
			while (currentDate <= endDate) {
				var y = currentDate.getFullYear()%100
				var m = currentDate.getMonth()+1
				var e = dataList.find(function(x) {
					return (x.year == y && x.month == m)
				})
				result.push(e ? e.amount : null)
				currentDate.setMonth(currentDate.getMonth()+1)
			}
			return result
		}
	}
}]);
