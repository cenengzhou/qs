mainApp.controller('CertCtrl', ['$scope', '$http', 'colorCode', function($scope, $http, colorCode) {
    
    $scope.linChartParameters = {
    	    'labels' : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    	    'series' : ['IPA', 'IPC', 'CC(IPA)', 'CC(IPC)'],
    	    'data' : [
    	      [65, 69, 80, 81, 90, 95, 100, 110, 120, 130, 140, 156],
    	      [55, 59, 70, 71, 80, 85, 90, 100, 110, 120, 130, 146],
    	      [19, 29, 32, 40, 47, 48, 49, 59, 60, 61, 63, 68],
    	      [8, 18, 20, 29, 36, 37, 40, 45, 49, 50, 51, 56]
    	    ],
    	    'options' : {
    	        'showScale' : true,
    	        'showTooltips' : true,
    	        'responsive' : false,
    	        'maintainAspectRatio' : true,
    	        'pointDot' : true,
    	        'bezierCurve' : true,
    	        'datasetFill' : false,
    	        'animation' : true,

    	     }
    
    };
    

   /* var today = new Date();
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
      
      paginationPageSizes: [50],
      paginationPageSize: 50,
      
      onRegisterApi: function(gridApi){
        $scope.gridApi = gridApi;
        $scope.gridApi.grid.registerRowsProcessor( $scope.singleFilter, 200 );
      },
      columnDefs: [
        { field: 'id' },
        { field: 'name', enableCellEdit: true },
        { field: 'gender', cellFilter: 'mapGender' },
        { field: 'company' },
        { field: 'email' },
        { field: 'phone' },
        { field: 'age' },
        { field: 'mixedDate' },
        {field: 'balance' }
        
      ]
    };
    
    $scope.gridOptions1 = {
      enableFiltering: true,
      enableColumnResizing : true,
      enableGridMenu : true,
      enableAnimations: true,
      //showGridFooter : true,
      //showColumnFooter : true,
      //fastWatch : true,
      
      //enablePaginationControls: false,
      paginationPageSizes: [50],
      paginationPageSize: 50,
      
      onRegisterApi: function(gridApi){
        $scope.gridApi2 = gridApi;
        $scope.gridApi.grid2.registerRowsProcessor( $scope.singleFilter, 200 );
      },
      columnDefs: [
        { field: 'id' },
        { field: 'name' },
        { field: 'gender', cellFilter: 'mapGender' },
        { field: 'company' },
        { field: 'email' },
        { field: 'phone' },
        { field: 'age' },
        { field: 'mixedDate' },
        {field: 'balance' }
        
      ]
    };
   
    $http.get('http://localhost:8080/QSrevamp2/data/500_complex.json')
      .success(function(data) {
        $scope.gridOptions.data = data;
        $scope.gridOptions1.data = data;
        $scope.gridOptions.data[0].age = -5;
   
        data.forEach( function addDates( row, index ){
          row.mixedDate = new Date();
          row.mixedDate.setDate(today.getDate() + ( index % 14 ) );
         // row.gender = row.gender==='male' ? '1' : '2';
        });
      });*/
      

    
}]);
