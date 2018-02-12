mainApp.controller('TransitEnquiryCtrl', ['$scope', '$state', '$cookies', 'transitService', 'modalService', '$uibModalInstance', 'GlobalParameter',
                                            function($scope, $state, $cookies, transitService, modalService, $uibModalInstance, GlobalParameter) {

	
	getIncompleteTransitList();


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableRowHeaderSelection : false,
//			enableSelectAll: true,
			multiSelect: false,
			enableCellEditOnFocus : true,
			exporterMenuPdf: false,
			rowTemplate :
			'<div ng-mouseover="rowClass=\'active\'" ng-click="grid.appScope.goToJob(row)" ng-mouseleave="rowClass=\'\'">\
		    <div  ng-class="{\'bg-grey-light\': rowClass.length>0}" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\
		        class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="{{col.isRowHeader ? \'rowheader\' : \'gridcell\'}}" ui-grid-cell>\
		        </div>\
		    </div>',

			columnDefs: [
			             { field: 'jobNumber'},
			             { field: 'estimateNo'},
			             { field: 'jobDescription'},
			             { field: 'company'},
			             { field: 'status'},
			             { field: 'matchingCode'},
			             { field: 'lastModifiedUser'},
			             { field: 'lastModifiedDate', enableFiltering: false, cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
	}

	$scope.goToJob = function(row){
    	$cookies.put('jobNo', row.entity.jobNumber);
    	$cookies.put('jobDescription', row.entity.jobDescription);
    	$state.go('job.dashboard');
	}

	function getIncompleteTransitList() {
		transitService.getIncompleteTransitList()
		.then(
				function( data ) {
					$scope.gridOptions.data= data;
				});
	}


	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};

	//Listen for location changes and call the callback
	$scope.$on('$locationChangeStart', function(event){
		$uibModalInstance.close();
	});

}]);