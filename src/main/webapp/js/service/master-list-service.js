mainApp.service('masterListService', ['$http', '$q', 'GlobalHelper', function($http, $q, GlobalHelper){
	// Return public API.
	return({
		getSubcontractor:				getSubcontractor,
		getSubcontractorList:			getSubcontractorList,
		searchObjectList:				searchObjectList,
		searchSubsidiaryList:			searchSubsidiaryList,
		validateAndCreateAccountCode:	validateAndCreateAccountCode,
		getSubcontractorWorkScope:		getSubcontractorWorkScope,
		searchVendorAddressDetails:		searchVendorAddressDetails
	});

	function getSubcontractor(subcontractorNo) {
		var request = $http({
			method: "get",
			url: "service/masterList/getSubcontractor",
			dataType: "application/json;charset=UTF-8",
			params: {
				subcontractorNo: subcontractorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	
	function getSubcontractorList(searchStr) {
		var request = $http({
			method: "get",
			url: "service/masterList/getSubcontractorList",
			dataType: "application/json;charset=UTF-8",
			params: {
				searchStr: searchStr
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	

	function searchObjectList(searchStr) {
		var request = $http({
			method: "POST",
			url: "service/masterList/searchObjectList",
			params: {
				searchStr: searchStr
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function searchSubsidiaryList(searchStr) {
		var request = $http({
			method: "POST",
			url: "service/masterList/searchSubsidiaryList",
			params: {
				searchStr: searchStr
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function validateAndCreateAccountCode(jobNo, objectCode, subsidiaryCode) {
		var request = $http({
			method: "POST",
			url: "service/masterList/validateAndCreateAccountCode",
			params: {
				jobNo: jobNo,
				objectCode: objectCode,
				subsidiaryCode: subsidiaryCode
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function getSubcontractorWorkScope(vendorNo) {
		var request = $http({
			method: "POST",
			url: "service/masterList/getSubcontractorWorkScope",
			params: {
				vendorNo: vendorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}
	
	function searchVendorAddressDetails(vendorNo) {
		var request = $http({
			method: "POST",
			url: "service/masterList/searchVendorAddressDetails",
			params: {
				vendorNo: vendorNo
			}
		});
		return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
	}

}]);

