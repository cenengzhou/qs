mainApp.controller('RepackagingUpdateCtrl', ['$scope' , '$http', 'modalService', 'repackagingService', '$cookieStore', '$stateParams',
                                             function($scope , $http, modalService, repackagingService, $cookieStore, $stateParams) {
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");
	
	if($stateParams.repackagingEntryId){
		$cookieStore.put('repackagingEntryId', $stateParams.repackagingEntryId);
	}
	
	$scope.repackagingEntryId = $cookieStore.get("repackagingEntryId");
	
	$scope.editable = true;
	$scope.mySelections=[];
	
	
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,
			
			columnDefs: [
			             { field: 'packageNo', cellClass: "grid-theme-blue", enableCellEdit: true},
			             { field: 'objectCode', enableCellEdit: false},
			             { field: 'subsidiaryCode', enableCellEdit: false},
			             { field: 'resourceDescription', displayName: "Description", enableCellEdit: false},
			             { field: 'unit'},
			             { field: 'quantity', enableCellEdit: false, enableFiltering: false},
			             { field: 'rate', enableCellEdit: false, enableCellEdit: false, enableFiltering: false},
			             { field: 'amount', enableCellEdit: false, enableCellEdit: false, enableFiltering: false},
			             { field: 'postedIVAmount', displayName: "Posted Amount", enableCellEdit: false, enableFiltering: false},
			             { field: 'resourceType', displayName: "Type", enableCellEdit: false},
			             { field: 'excludeDefect', displayName: "Defect"},
			             { field: 'excludeLevy', displayName: "Levy"}
			             ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {

			//Alert to show what info about the edit is available
			//alert('Column: ' + colDef.name + ' feedbackRate: ' + rowEntity.feedbackRate);
			//rowEntity.feedbackRateHK = rowEntity.feedbackRate * $scope.exchangeRate;
		});

	}

	
     loadRepacakgingData();
     
     function loadRepacakgingData() {
    	 repackagingService.getResourceSummaries($scope.jobNo, "", "")
    	 .then(
			 function( data ) {
				 //console.log(data);
				 $scope.gridOptions.data= data;
			 });
     }
    
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};

	
	//Open Window
	$scope.open = function(view){
	
		if(view=="split"){
			console.log("mySelections: "+$scope.mySelections);
			modalService.open('lg', 'view/repackaging/modal/repackaging-split.html', 'RepackagingSplitModalCtrl');
		}else if (view=="merge"){
			modalService.open('lg', 'view/repackaging/modal/repackaging-merge.html', 'RepackagingMergeModalCtrl');
		}else if (view=="add"){
			modalService.open('md', 'view/repackaging/modal/repackaging-add.html', 'RepackagingAddModalCtrl', '', $scope.repackagingEntryId);
		}
	};
	
	
	/*$scope.gridOptions = {
	enableFiltering: true,
	enableColumnResizing : true,
	enableGridMenu : true,
	enableRowSelection: true,
	enableSelectAll: true,
	//enableFullRowSelection: true,
	//multiSelect: true,
	showGridFooter : true,
	//showColumnFooter : true,
	//fastWatch : true,

	//enableCellEditOnFocus : true,
	selectedItems: $scope.mySelections,
	
	exporterMenuPdf: false,
	exporterCsvFilename: 'ResourceSummaries.csv',
	exporterPdfDefaultStyle: {fontSize: 9},
	exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
	exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
	exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
	exporterPdfFooter: function ( currentPage, pageCount ) {
		return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
	},
	exporterPdfCustomFormatter: function ( docDefinition ) {
		docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
		docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
		return docDefinition;
	},
	exporterPdfOrientation: 'landscape',//portrait
	exporterPdfPageSize: 'LETTER',
	exporterPdfMaxGridWidth: 500,
	//exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
	
	//paginationPageSizes: [50],
	//paginationPageSize: 50,

	afterSelectionChange: function(rowItem) {
		 console.log('rowItem.entity: '+rowItem.entity);
          if (rowItem.selected) {
            //write code to execute only when selected.
            //alert(rowItem.entity );  //rowItem.entity is the "data" here
        	  console.log('rowItem.entity: '+rowItem.entity);
        	  
            var detailData = [];
            angular.forEach(rowItem.entity, function(key, value){
              var dataRow = { col1: key, col2: value };
              detailData.push(dataRow);
            });

            $scope.detailsGridData = detailData;
          } else {
            //write code on deselection.
          }
        },
        
	//Single Filter
	onRegisterApi: function(gridApi){
		$scope.gridApi = gridApi;
		$scope.gridApi.grid.registerRowsProcessor( $scope.singleFilter);
	},
	columnDefs: [
	             { field: 'packageNo', enableCellEdit: true, width:80, displayName:"Package No.", cellEditableCondition: function ($scope) { return $scope.row.entity.showRemoved;},
	             { field: 'packageNo', enableCellEdit: true, width:80, displayName:"Package No.",
            	 cellTemplate: '<div class="ui-grid-cell-contents" style="color:blue;text-align:right;">{{COL_FIELD}}</div>'},
	             { field: 'objectCode', enableCellEdit: false , width:100},
	             { field: 'subsidiaryCode',enableCellEdit: false},
	             { field: 'description', enableCellEdit: false },
	             { field: 'unit', enableCellEdit: false, enableFiltering: false},
	             { field: 'quantity', enableCellEdit: false ,enableFiltering: false},
	             { field: 'rate', enableCellEdit: false, enableFiltering: false },
	             {field: 'amount', enableCellEdit: false, enableFiltering: false },
        		 {field: 'postedIvAmount', enableFiltering: false},
    			 {field: 'levyExcluded', enableCellEdit: false, enableFiltering: false },
    			 {field: 'defectExcluded', enableCellEdit: false, enableFiltering: false}
    			 ],
		 rowTemplate: "<div ng-dblclick=\"grid.appScope.onDblClickRow(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",
	
	

		};
		
		
		$scope.gridOptions.onRegisterApi = function(gridApi){
		 //set gridApi on scope
		 $scope.gridApi = gridApi;
		 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
		   console.log('Column: ' + colDef.name + ' packageNo: ' + rowEntity.packageNo);
		 });
		};
		
		
		$scope.onDblClickRow = function(rowItem) {
		var rowCol = $scope.gridApi.cellNav.getFocusableCols;
		console.log('ON ROW CLICK: '+rowCol);
		};
		
		$scope.getCurrentFocus = function(){
		var rowCol = $scope.gridApi.cellNav.getFocusedCell();
		if(rowCol !== null) {
		    $scope.currentFocused = 'Row Id:' + rowCol.row.entity.id + ' col:' + rowCol.col.colDef.name;
		}
		}
		*/
			
}]);