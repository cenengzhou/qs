mainApp.controller("SubcontractVendorFeedbackModalCtrl", ['$scope', '$http', '$location', '$uibModalInstance', 'uiGridConstants', function ($scope, $http, $location, $uibModalInstance, uiGridConstants) {
	$scope.vendorNo= 31347;
	$scope.packageNo= 1004;
	
	
	$scope.currencyCode = {
			options: [
	          "HKD",
	          "USD",
	          "INR",
	          "GBP",
	          "EUR",
	          "AUD",
	          "THB",
	          "TWD",
	          "PHP",
	          "JPY",
	          "SGD",
	          "CAD",
	          "CNY",
	          "MOP"
	          ],
	          selected: ""
		};

	$scope.exchangeRate = 1;
	
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			//enableRowSelection: true,
			//enableFullRowSelection: true,
			//multiSelect: false,
			//showGridFooter : true,
			showColumnFooter : true,
			//fastWatch : true,

			enableCellEditOnFocus : true,


			//Single Filter
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
				
				 gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
					    //Do your REST call here via $http.get or $http.post

					    //Alert to show what info about the edit is available
					    //alert('Column: ' + colDef.name + ' feedbackRate: ' + rowEntity.feedbackRate);
					    rowEntity.feedbackRateHK = rowEntity.feedbackRate * $scope.exchangeRate;
					  });
				
			},
			
			
			columnDefs: [
			             { field: "billNo", displayName:"B/P/I", enableCellEdit: false, width:50},
		            	 { field: "objectCode", enableCellEdit: false, width:60 },
		            	 { field: "subsidiaryCode" , enableCellEdit: false, width:80 },
		            	 { field: "description" , enableCellEdit: false,  width:150 },
		            	 { field: "unit" , enableCellEdit: false, width:50 },
		            	 { field: "quantity" , enableCellEdit: false, aggregationType: uiGridConstants.aggregationTypes.sum, width:80 },
		            	 { field: "budgetRate" , enableCellEdit: false, width:90 },
		            	 { field: "feedbackRate" , enableCellEdit: true, cellClass: "grid-theme-blue", width:120 },
		            	 { field: "feedbackRateHK" ,displayName:"Feedback Rate(HK)", cellClass: "grid-blue", enableCellEdit: false, width:170 }
		    ]


	};

	
	$http.get("http://localhost:8080/pcms/data/vendor-feedback.json")
	.success(function(data) {
		$scope.gridOptions.data = data;
	});

	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};


	//Save Function
	$scope.save = function () {
		$location.path("/subcontract-flow");
		$uibModalInstance.close();
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss("cancel");
	};
	
	 $scope.$on('$locationChangeStart', function(event, $uibModalStack){
	    	console.log("Location changed");
	    	 var confirmed = window.confirm("Are you sure to exit this page?");
	     	console.log("confirmed: "+confirmed);
	    	 if(confirmed){
	    	    //$uibModalStack.dismissAll('closing');
	    		 $uibModalInstance.close();
	    	}
	    	 else{
	    		 // Prevent the browser default action (Going back):
	 		    event.preventDefault();            
	    	 }
	    });
}]);

