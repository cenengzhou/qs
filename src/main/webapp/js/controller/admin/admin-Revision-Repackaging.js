mainApp.controller('AdminRevisionRepackagingCtrl', ['$scope', 'resourceSummaryService', 'modalService',
										function($scope, resourceSummaryService, modalService){
	$scope.onSubmitResourceSummarySearch = onSubmitResourceSummarySearch;
	$scope.onUpdateResourceSummaryRecord = onUpdateResourceSummaryRecord;

	$scope.booleanOptions = {
			true: 'True',
			false: 'False'
	}
	
	$scope.zeroOneOptions = {
			1: 'True',
			0: 'False'
	}
	
	$scope.systemStatuOptions = {
			'ACTIVE': 'ACTIVE',
			'INACTIVE': 'INACTIVE'
	}
	
	$scope.fieldList = [
			{
				name: 'id',
				display: 'Resource Summary Id',
				order: 1,
				type: 'text',
				readOnly: true
			},
			{
				name: 'jobInfo',
				isObject: true,
				display: 'JobInfo Id',
				order: 2,
				type: 'number',
				readOnly: true
			},
			{
				name: 'systemStatus',
				order: 3,
				readOnly: true,
				type: 'boolean',
				options: $scope.systemStatuOptions
			},
			{
				name: 'packageNo',
				display: 'Subcontract No.',
				order: 4,
				type: 'text'
			},
			{
				name: 'subsidiaryCode',
				order: 5,
				type: 'text'
			},
			{
				name: 'objectCode',
				order: 6,
				type: 'text'
			},
			{
				name: 'resourceDescription',
				order: 7,
				type: 'text'
			},
			{
				name: 'unit',
				order: 8,
				type: 'text'
			},
			{
				name: 'quantity',
				order: 9,
				type: 'number'
			},
			{
				name: 'rate',
				order: 10,
				type: 'number'
			},
			{
				name: 'postedIVAmount',
				display: 'Posted IV Amount',
				order: 11,
				type: 'number'
			},
			{
				name: 'currIVAmount',
				display: 'Current IV Amount',
				order: 12,
				type: 'number'
			},
			{
				name: 'splitForm.id',
				display: 'Split From Id',
				isObject: true,
				order: 13,
				type: 'number',
				readOnly: 'readOnly'
			},
			{
				name: 'mergeTo.id',
				display: 'Merage To Id',
				isObject: true,
				order: 14,
				type: 'number',
				readOnly: 'readOnly'
			},
			{
				name: 'resourceType',
				order: 15,
				type: 'text'
			},
			{
				name: 'excludeLevy',
				order: 16,
				type: 'boolean',
				options: $scope.booleanOptions
			},
			{
				name: 'excludeDefect',
				order: 17,
				type: 'boolean',
				options: $scope.booleanOptions
			},
			{
				name: 'forIVRollbackOnly',
				display: 'For IV Rollback Only',
				order: 18,
				type: 'boolean',
				options: $scope.zeroOneOptions
			},
			{
				name: 'finalized',
				order: 19,
				type: 'text'
			},
			{
				name: 'amountBudget',
				order: 20,
				type: 'number'
			},
			{
				name: 'cumQuantity',
				display: 'Cumulative Quantity',
				order: 21,
				type: 'number'
			}
	];
	
	checkFieldList();

	$scope.repackagingHtml = '\
		<div>\
			<div class="row well">\
				<form name="RevisionsResourceSummarySearch"\
					ng-submit="onSubmitResourceSummarySearch()" class="form">\
					<div class="col-md-4">\
						<input type="number" class="form-control" placeholder="Job Number:"\
							id="ResourceSummarySearch_jobNo" ng-model="resourceSummarySearch.jobNo">\
					</div>\
					<div class="col-md-4">\
						<input type="number" class="form-control"\
							placeholder="Subcontract Number:"\
							id="MainCertSearch_certificateNumber"\
							ng-model="resourceSummarySearch.subcontractNo">\
					</div>\
					<div class="col-md-4">\
						<button type="submit" class="btn btn-block btn-primary"\
							id="resourceSummarySearch_submit">Search</button>\
					</div>\
				</form>\
			</div>\
			<form name="RevisionsResourceSummaryRecord"\
				ng-submit="onUpdateResourceSummaryRecord(resourceSummary)" ng-repeat="resourceSummary in resourceSummaryList">\
				<div class="row">\
					<fieldset>\
						<div ng-repeat="field in fieldList" class="col-md-6" style="padding: 5px">\
							<div class="form-group">\
								<label for="ResourceSummary_{{field.name}}"\
									class="control-label">{{field.display}}</label>\
								<div ng-if="!field.isObject" >\
									<input ng-if="field.type==\'text\'" type="text" class="form-control"\
										id="ResourceSummary_{{field.name}}" ng-model="resourceSummary[field.name]" ng-disabled={{field.readOnly}}>\
									<input ng-if="field.type==\'number\'" type="number" step="1.000" class="form-control"\
									id="ResourceSummary_{{field.name}}" ng-model="resourceSummary[field.name]" ng-disabled={{field.readOnly}}>\
									<select ng-if="field.type==\'boolean\'" ng-model="resourceSummary[field.name]" ng-options="key as value for (key, value) in field.options" class="form-control"></select>\
								</div>\
								<div ng-if="field.isObject">\
									<input ng-if="field.isObject" type="text" class="form-control"\
									id="ResourceSummary_{{field.name}}" ng-model="resourceSummary[field.name][\'id\']" ng-disabled={{field.readOnly}}>\
								</div>\
							</div>\
						</div>\
					</fieldset>\
				</div>\
				<div class="row">\
					<button ng-show="showQSAdmin" type="submit" class="btn btn-block btn-primary">Update Resource Summary ({{resourceSummary[\'id\']}})</button>\
				</div>\
			<hr/>\
			<div class="clearfix"></div>\
		</form>\
		';
	
	function onSubmitResourceSummarySearch(){
		resourceSummaryService.getResourceSummariesBySC(
				$scope.resourceSummarySearch.jobNo, 
				$scope.resourceSummarySearch.subcontractNo)
		.then(function(data){
			$scope.resourceSummaryList = data;
			$scope.resourceSummaryList.forEach(function(resourceSummary){
				$scope.fieldList.forEach(function(field){
					if(field.type == 'boolean' && typeof resourceSummary[field.name] == 'boolean'){
						resourceSummary[field.name] = resourceSummary[field.name] ? 'true' : 'false';
					}
				});
			});
		})
	}
	
	function onUpdateResourceSummaryRecord(resourceSummary){
		resourceSummaryService.updateResourceSummaries(resourceSummary.jobInfo.jobNo, [resourceSummary])
		.then(function(data){
			if(data == ''){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Resource summary updated');
			}
			
		})
	}
	
	function checkFieldList(){
		$scope.fieldList.forEach(function(field){
			if(!field.display) field.display = camelToNormalString(field.name);
			field.readOnly = field.readOnly ? true : false; 
		});
	}
	
	function camelToNormalString (text){
		return text.replace(/([A-Z])/g, ' $1')
	    .replace(/^./, function(str){ return str.toUpperCase(); });
	}

}]);