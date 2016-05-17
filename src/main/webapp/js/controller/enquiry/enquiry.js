
mainApp.controller('EnquiryCtrl', ['$scope' , '$http', 'colorCode', function($scope , $http, colorCode) {
	$scope.gridOptions = {
			//enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			//enableFullRowSelection: true,
			//multiSelect: true,
			showGridFooter : true,
			//showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,

			paginationPageSizes: [50],
			paginationPageSize: 50,


			//Single Filter
			/*onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
				$scope.gridApi.grid.registerRowsProcessor( $scope.singleFilter);
			},*/
			columnDefs: [
			             { field: 'packageNo', enableCellEdit: false, width:80, displayName:"Package No."},
			             { field: 'objectCode', enableCellEdit: false , width:100},
			             { field: 'subsidiaryCode',enableCellEdit: false},
			             { field: 'description', enableCellEdit: false },
			             { field: 'unit', enableCellEdit: false},
			             { field: 'quantity', enableCellEdit: false ,enableFiltering: false},
			             { field: 'rate', enableCellEdit: false },
			             {field: 'amount', enableCellEdit: false },
			             {field: 'cumIvAmount', 
		            	 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
		            	 {field: 'ivMovement',
	            		 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
	            		 {field: 'postedIvAmount',
            			 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
            			 {field: 'levyExcluded', enableCellEdit: false },
            			 {field: 'defectExcluded', enableCellEdit: false}
            			 ]
			
			

	};

	
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
		  $scope.gridApi.grid.registerRowsProcessor( $scope.packagaNoFilter);
		  $scope.gridApi.grid.registerRowsProcessor( $scope.objectCodeFilter);
		  $scope.gridApi.grid.registerRowsProcessor( $scope.subsiCodeFilter);
		  //$scope.gridApi.grid.registerRowsProcessor( $scope.descriptionFilter);
		  /*
		  $scope.gridApi.core.on.filterChanged( $scope, function() {

		        if($scope.gridApi.pagination.getPage() > 1){
		            $scope.gridApi.pagination.seek(1);
		        }
		      });*/
	}
	
	
	
	$http.get('http://localhost:8080/QSrevamp2/data/iv.json')
	.success(function(data) {
		$scope.gridOptions.data = data;
	});

	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	$scope.packagaNoFilter = function( renderableRows ){
		var matcher = new RegExp($scope.packageNofilterValue);
		console.log("packageNofilterValue: "+$scope.packageNofilterValue);
		var newRenderableRows = new Array();
		newRenderableRows= renderableRows;
		renderableRows.forEach( function( row ) {
			var match = false;
			[ 'packageNo'].forEach(function( field ){
				if ( row.entity[field].match(matcher) ){
					match = true;
				}
			});
			if ( !match ){
				row.visible = false;
			}
		});
		return newRenderableRows;
	};
	
	$scope.objectCodeFilter = function( renderableRows ){
		var matcher = new RegExp($scope.objectCodefilterValue);
		renderableRows.forEach( function( row ) {
			var match = false;
			[ 'objectCode'].forEach(function( field ){
				if ( row.entity[field].match(matcher) ){
					match = true;
				}
			});
			if ( !match ){
				row.visible = false;
			}
		});
		return renderableRows;
	};
	
	$scope.subsiCodeFilter = function( renderableRows ){
		var matcher = new RegExp($scope.subsiCodefilterValue);
		//console.log("$scope.subsiCodeFilter: "+$scope.subsiCodefilterValue);
		var newRenderableRows = new Array();
		newRenderableRows= renderableRows;
		renderableRows.forEach( function( row ) {
			var match = false;
			['subsidiaryCode'].forEach(function( field ){
				if ( row.entity[field].match(matcher) ){
					match = true;
					//newRenderableRows.indexOf(row);
					//newRenderableRows.splice(newRenderableRows.indexOf(row), 1);
				}
			});
			if ( !match ){
				row.visible = false;
			}
		});
		return newRenderableRows;
	};
	
	/*$scope.descriptionFilter = function( renderableRows ){
		var matcher = new RegExp($scope.filterValue);
		renderableRows.forEach( function( row ) {
			var match = false;
			[ 'packageNo', 'objectCode', 'subsidiaryCode'].forEach(function( field ){
				if ( row.entity[field].match(matcher) ){
					match = true;
				}
			});
			if ( !match ){
				row.visible = false;
			}
		});
		return renderableRows;
	};*/

	
}]);