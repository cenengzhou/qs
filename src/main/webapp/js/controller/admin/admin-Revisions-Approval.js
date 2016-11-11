mainApp.controller('AdminRevisionsApprovalCtrl', ['$scope', 'GlobalHelper', 'GlobalParameter', 'apService',
									function($scope, GlobalHelper, GlobalParameter, apService){
	$scope.GlobalHelper = GlobalHelper;
	ApiParam.prototype.setOptions = setApiOptions;
	window.scope = $scope;
	$scope.toCompleteSubcontractAwardParams = [
			new ApiParam('jobNumber', 'text'),
			new ApiParam('packageNo', 'text'),
			new ApiParam('approvedOrRejected', 'select').setOptions(GlobalParameter.approvedOrRejectedOptions)
	];
	
	$scope.toCompletePaymentParams = [
			new ApiParam('jobNumber', 'text'),
			new ApiParam('packageNo', 'text'),
			new ApiParam('approvedOrRejected', 'select').setOptions(GlobalParameter.approvedOrRejectedOptions)
	];
	
	$scope.toCompleteSplitTerminateParams = [
			new ApiParam('jobNumber', 'text'),
			new ApiParam('packageNo', 'text'),
			new ApiParam('approvedOrRejected', 'select').setOptions(GlobalParameter.approvedOrRejectedOptions),
			new ApiParam('splitOrTerminate', 'select').setOptions(GlobalParameter.splitOrTerminateOptions)
	];
	
	$scope.toCompleteAddendumParams = [
			new ApiParam('jobNumber', 'text'),
			new ApiParam('packageNo', 'text'),
			new ApiParam('username', 'text'),
			new ApiParam('approvedOrRejected', 'select').setOptions(GlobalParameter.approvedOrRejectedOptions)
	];
	
	$scope.toComplateMainCertParams = [
			new ApiParam('jobNumber', 'text'),
			new ApiParam('mainCertNo', 'text'),
			new ApiParam('approvedOrRejected', 'select').setOptions(GlobalParameter.approvedOrRejectedOptions)
	];
	
	var toCompleteSubcontractAwardFn = apService.completeAwardApproval;
	var toCompletePaymentFn = apService.completePaymentApproval;
	var toCompleteSplitTerminateFn = apService.completeSplitTerminateApproval;
	var toCompleteAddendumFn = apService.completeAddendumApproval;
	var toComplateMainCertFn = apService.completeMainCertApproval;
	
	$scope.completeApprovalItemList = [
		new ApiFn('ToCompleteSubcontractAward', toCompleteSubcontractAwardFn, $scope.toCompleteSubcontractAwardParams),
		new ApiFn('ToCompletePayment', toCompletePaymentFn, $scope.toCompletePaymentParams),
		new ApiFn('ToCompleteSplitTerminate', toCompleteSplitTerminateFn, $scope.toCompleteSplitTerminateParams),
		new ApiFn('ToCompleteAddendum', toCompleteAddendumFn, $scope.toCompleteAddendumParams),
		new ApiFn('ToComplateMainCert', toComplateMainCertFn, $scope.toComplateMainCertParams),
	]
	
	$scope.showPanel = function(i){
		$scope.completeApprovalItemList.forEach(function(item){
			item.collapse = '';
		})
		$scope.completeApprovalItemList[i].collapse = 'in';
	}
	
	$scope.showPanel(0);
	
	$scope.onUpdate = function(fn, params){
		var paramsMap = {};
		params.map(function(param){
			paramsMap[param.name] = param.value;
		});
		
		fn(paramsMap)
		.then(function(response){
			console.log(response);
		})
	}

	function ApiFn(name, fn, params){
		this.name = name;
		this.fn = fn;
		this.params = params;
		this.title = GlobalHelper.camelToNormalString(name);
		this.collapse = '';
		return this;
	}
	
	function ApiParam(name, type){
		this.name = name;
		this.type = type;
		this.value = '';
		switch(type){
			case 'select':
			this.options = [];
			break;
		}
		return this;
	}
	
	function setApiOptions(options){
		this.options = options;
		return this;
	}
}]);
