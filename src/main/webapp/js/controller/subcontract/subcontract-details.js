mainApp.controller('SubcontractDetailsCtrl', ['$scope' , '$http', 'colorCode', function($scope , $http, colorCode) {
    $scope.bq = 53895000;
    $scope.vo = 100256;
    $scope.cc = 0;
    $scope.retention = 0;
    
      
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
    	      },
    	      columnDefs: [
    	        { field: 'certNo', width:80, displayName:"Cert No.",
    	         cellTemplate: '<div style="text-decoration:underline;color:blue;text-align:right;cursor:pointer" ng-click="grid.appScope.rowClick(row)">{{COL_FIELD}}</div>'},
    	        //cellTemplate: '<div class="ui-grid-cell-contents"><span><a ng-click="clicker(row)">{{COL_FIELD}}</a></span></div>' },
    	         { field: 'clientCertNo', enableCellEdit: false , width:100 },
    	        { field: 'mainContractorAmt'/*, cellFilter: 'mapGender'*/ },
    	        { field: 'nscAmt' },
    	        { field: 'mosAmt' },
    	        { field: 'retention' },
    	        { field: 'mosRetention' },
    	        {field: 'contraChargeAmt' },
    	        {field: 'adjustmentAmt' },
    	        {field: 'advancedPayment' },
    	        {field: 'cpfAmt' },
    	      ]

    	    };
    	    
    	    $http.get('http://localhost:8080/QSrevamp2/data/cert-data.json')
    	      .success(function(data) {
    	        $scope.gridOptions.data = data;
    	      });
    	      
    	    $scope.filter = function() {
    	      $scope.gridApi.grid.refresh();
    	    };
    	      
      
}]);