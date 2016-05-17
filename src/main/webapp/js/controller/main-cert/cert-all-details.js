mainApp.controller('CertAllDetailsCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
	 /*$scope.showInfo = function(row) {
	     var modalInstance = $modal.open({
	          controller: 'InfoController',
	          templateUrl: 'ngTemplate/infoPopup.html',
	          resolve: {
	            selectedRow: function () {                    
	                return row.entity;
	            }
	          }
	     });
	     
	     modalInstance.result.then(function (selectedItem) {
	       $log.log('modal selected Row: ' + selectedItem);
	     }, function () {
	       $log.info('Modal dismissed at: ' + new Date());
	     });
	  }*/
	
	
	var today = new Date();
    $scope.gridOptions = {
      enableFiltering: true,
      enableColumnResizing : true,
      enableGridMenu : true,
      enableRowSelection: true,
      enableFullRowSelection: true,
      multiSelect: false,
      //showGridFooter : true,
      //showColumnFooter : true,
      //fastWatch : true,
      
     enableCellEditOnFocus : true,
     
     paginationPageSizes: [50],
      paginationPageSize: 50,
      
      
      //Single Filter
      onRegisterApi: function(gridApi){
        $scope.gridApi = gridApi;
        $scope.gridApi.grid.registerRowsProcessor( $scope.singleFilter, 200 );
      },
      columnDefs: [
        { field: 'certNo', width:80, displayName:"Cert No.",
         cellTemplate: '<div style="text-decoration:underline;color:blue;text-align:right;cursor:pointer" ng-click="grid.appScope.rowClick(row)">{{COL_FIELD}}</div>'},
        //cellTemplate: '<div class="ui-grid-cell-contents"><span><a ng-click="clicker(row)">{{COL_FIELD}}</a></span></div>' },
         { field: 'clientCertNo', enableCellEdit: false , width:100 },
        { field: 'mainContractorAmt'/*, cellFilter: 'mapGender'*/ , width:100 },
        { field: 'nscAmt' , width:100 },
        { field: 'mosAmt' , width:100 },
        { field: 'retention' , width:100 },
        { field: 'retentionReleased' , width:100 },
        { field: 'mosRetention' , width:100 },
        {field: 'mosRetentionReleased' , width:100 },
        {field: 'contraChargeAmt' , width:100 },
        {field: 'adjustmentAmt' , width:100 },
        {field: 'advancedPayment' , width:100 },
        {field: 'cpfAmt' , width:100 },
        {field: 'certStatus' , width:100 },
        {field: 'certIssueDate' , width:150 }
      ],
      rowTemplate: "<div ng-dblclick=\"grid.appScope.onDblClickRow(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",
   	  //rowTemplate: "<div ng-dblclick=\"repackaging></div>"
      

    };
    
    $scope.onDblClickRow = function(rowItem) {
    	var rowCol = $scope.gridApi.cellNav.getFocusedCell();
    	$location.path('/cert/details');
    };
      
   /* $scope.getCurrentFocus = function(){
        var rowCol = $scope.gridApi.cellNav.getFocusedCell();
        if(rowCol !== null) {
            $scope.currentFocused = 'Row Id:' + rowCol.row.entity.id + ' col:' + rowCol.col.colDef.name;
        }
      }
   */
    
    $http.get('http://localhost:8080/QSrevamp2/data/cert-data.json')
      .success(function(data) {
        $scope.gridOptions.data = data;
       // $scope.gridOptions.data[0].age = -5;
   
        /*data.forEach( function addDates( row, index ){
          row.mixedDate = new Date();
          row.mixedDate.setDate(today.getDate() + ( index % 14 ) );
        });*/
      });
      
    $scope.filter = function() {
      $scope.gridApi.grid.refresh();
    };
    
      
    $scope.singleFilter = function( renderableRows ){
      var matcher = new RegExp($scope.filterValue);
      renderableRows.forEach( function( row ) {
        var match = false;
        [ 'retention', 'clientCertNo', 'certStatus'].forEach(function( field ){
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

    $scope.showIPA = function() {
    	$scope.gridOptions.columnDefs[2].visible = true;
    	$scope.gridOptions.columnDefs[3].visible = false;
    	$scope.gridOptions.columnDefs[4].visible = false;
   	    $scope.gridApi.grid.refresh();
	  }
    $scope.showIPC = function() {
    	$scope.gridOptions.columnDefs[2].visible = false;
    	$scope.gridOptions.columnDefs[3].visible = true;
    	$scope.gridOptions.columnDefs[4].visible = false;
   	    $scope.gridApi.grid.refresh();
	  }
    $scope.showPostingAmount = function() {
    	$scope.gridOptions.columnDefs[2].visible = false;
    	$scope.gridOptions.columnDefs[3].visible = false;
    	$scope.gridOptions.columnDefs[4].visible = true;
   	    $scope.gridApi.grid.refresh();
	  }
   
    
	/*$scope.columns = [{ field: 'name' }, { field: 'gender' }];
	  $scope.gridOptions = {
	    enableSorting: true,
	    columnDefs: $scope.columns,
	    onRegisterApi: function(gridApi) {
	      $scope.gridApi = gridApi;
	    }
	  };
	  
	  $scope.remove = function() {
	    $scope.columns.splice({ field: 'company'});
	  }
	  
	  $scope.add = function() {
		$scope.columns.a
	    $scope.columns.push(
	    		{ field: 'company', enableSorting: false },
	    		{ field: 'id', enableSorting: false }
	    );
	  }
	 
	  $scope.splice = function() {
	    $scope.columns.splice(1, 0, { field: 'company', enableSorting: false });
	  }
	 
	  $scope.unsplice = function() {
	    $scope.columns.splice(1, 1);
	  }
	  
	 
	  $http.get('http://localhost:8080/QSrevamp2/data/500_complex.json')
	    .success(function(data) {
	      $scope.gridOptions.data = data;
	    });*/
	}]);



/*mainApp.filter('mapGender', function() {
  var genderHash = {
		    1: 'male',
    2: 'female'
  };
 
  return function(input) {
    if (!input){
      return '';
    } else {
      return genderHash[input];
    }
  };
});*/