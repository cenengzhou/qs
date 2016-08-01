mainApp.controller('RepackagingUpdateCtrl', ['$scope' ,'modalService', 'resourceSummaryService', 'unitService', '$cookieStore', '$stateParams', '$state', 'uiGridConstants',
                                             function($scope, modalService, resourceSummaryService, unitService, $cookieStore, $stateParams, $state, uiGridConstants) {
	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");

	if($stateParams.repackagingEntryId){
		$cookieStore.put('repackagingEntryId', $stateParams.repackagingEntryId);
	}

	$scope.repackagingEntryId = $cookieStore.get("repackagingEntryId");

	
	loadResourceSummaries();
	getUnitOfMeasurementList();
	
	$scope.units=[];

	var optionList = [{ id: 'true', value: 'Excluded' },
                   { id: 'false', value: 'Included' }
	];

	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			multiSelect: true,
			enableCellEditOnFocus : true,
			showColumnFooter : true,
			showGridFooter : false,
			//showColumnFooter : true,
			exporterMenuPdf: false,

			rowEditWaitInterval :-1,
			
			columnDefs: [
			             { field: 'packageNo', displayName: "Subcontract No.", enableCellEdit: false},
			             { field: 'objectCode', cellClass: "grid-blue"},
			             { field: 'subsidiaryCode', cellClass: "grid-blue"},
			             { field: 'resourceDescription', displayName: "Description", cellClass: "grid-blue"},
			             { field: 'unit', cellClass: "grid-blue", enableFiltering: false, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 editDropdownValueLabel: 'value', editDropdownOptionsArray: $scope.units
			             },
			             { field: 'quantity', enableCellEdit: false, enableFiltering: false, 
			            	cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'rate', enableCellEdit: false, enableCellEdit: false, enableFiltering: false, 
			            		cellClass: 'text-right', cellFilter: 'number:2'},
			             { field: 'amountBudget', displayName: "Amount", enableCellEdit: false, enableCellEdit: false, enableFiltering: false, 
			            	cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
			             { field: 'postedIVAmount', displayName: "Posted Amount", enableCellEdit: false, enableFiltering: false, 
			            	cellClass: 'text-right', cellFilter: 'number:2',
			            	 aggregationType: uiGridConstants.aggregationTypes.sum,
			            	 footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
			             },
			             { field: 'resourceType', displayName: "Type", enableCellEdit: false},
			             { field: 'excludeDefect', displayName: "Defect", cellClass: "grid-blue", 
			            	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-dropdown></div></div>', 
			                 filter: { 
			                   term: '',
			                   options: optionList
			                 }, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
			             },
			             { field: 'excludeLevy', displayName: "Levy", cellClass: "grid-blue", 
			            	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-dropdown></div></div>', 
			                 filter: { 
			                   term: '',
			                   options: optionList
			                 }, 
			            	 editableCellTemplate: 'ui-grid/dropdownEditor',
			            	 cellFilter: 'mapExclude', editDropdownValueLabel: 'value',  editDropdownOptionsArray: optionList
			             }
			           ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;
		
		gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(rowEntity.packageNo !=null && rowEntity.packageNo.length > 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources with assigned subcontract cannot be edited here.");
				return;
			}
			if(rowEntity.postedIVAmount != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected field cannot be edited - resource has posted IV amount.");
				return;
			}
        });
	
		gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
			if(colDef.name == "objectCode" && rowEntity.objectCode != null && rowEntity.objectCode.length < 6){
				rowEntity.objectCode = oldValue;
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Object code should be in 6 digits.");
				return;
			}
			if(colDef.name == "subsidiaryCode"){
				if(rowEntity.subsidiaryCode != null && rowEntity.subsidiaryCode.length < 6){
					rowEntity.subsidiaryCode = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code should be in 8 digits.");
					return;
				}
				if(rowEntity.resourceType == "VO" 
					&& rowEntity.subsidiaryCode.substring(0, 1) != "4" 
						&& rowEntity.subsidiaryCode.substring(2, 4) != "80"){
					rowEntity.subsidiaryCode = oldValue;
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subsidiary code of VO should start with [4X80XXXX].");
					return;
				}
			}
			
		});

	}


	//Open Window
	$scope.open = function(view){

		if(view=="split"){
			var valid = validate(view);
			if(valid){
				var selectedRows = $scope.gridApi.selection.getSelectedRows();
				modalService.open('lg', 'view/repackaging/modal/repackaging-split.html', 'RepackagingSplitModalCtrl', 'Split', selectedRows);
			}
		}else if (view=="merge"){
			var valid = validate(view);
			if(valid){
				var selectedRows = $scope.gridApi.selection.getSelectedRows();
				modalService.open('lg', 'view/repackaging/modal/repackaging-split.html', 'RepackagingSplitModalCtrl' , 'Merge', selectedRows);
			}
		}else if (view=="add"){
			modalService.open('md', 'view/repackaging/modal/repackaging-add.html', 'RepackagingAddModalCtrl', '', $scope.repackagingEntryId);
		}
	};

	var validate = function(action){
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(action == 'split' && selectedRows.length != 1){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please only select 1 row to split.");
			return false;
		}
		else if(action == 'merge' && selectedRows.length < 2){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select at least 2 rows to merge.");
			return false;
		}
		
		var resourceType = '-';
		for (i in selectedRows){
			if(resourceType == '-'){
				resourceType = selectedRows[i]['resourceType'];
			}
			if(selectedRows[i]['resourceType'] != resourceType){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources cannot be merged - resources must have the same type.");
				return false;
			}
			if (selectedRows[i]['packageNo'] != null && selectedRows[i]['packageNo'].length > 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Resources with assigned subcontract cannot be edited here.");
				return false;
			}
			if (selectedRows[i]['postedIVAmount'] != 0){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Selected resource has posted IV amount.");
				return false;
			}
		}
		
		return true;
	}
	
	$scope.deleteResources = function(){
		var selectedRows = $scope.gridApi.selection.getSelectedRows();
		console.log(selectedRows);
		if(selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select resources to delete.");
			return;
		}
		deleteResources(selectedRows);
	}

	$scope.update = function() {
		var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
		var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
		
		if(dataRows.length==0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "No record has been modified");
			return;
		}
		
		
		updateResourceSummaries(dataRows);
	}
	
	function deleteResources(rowsToDelete) {
		resourceSummaryService.deleteResources(rowsToDelete)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources have been deleted.");
						$state.reload();
					}
				});
	}
	
	function loadResourceSummaries() {
		resourceSummaryService.getResourceSummaries($scope.jobNo, "", "")
		.then(
				function( data ) {
					$scope.gridOptions.data= data;
				});
	}
	
	function getUnitOfMeasurementList() {
		unitService.getUnitOfMeasurementList()
		.then(
				function( data ) {
					angular.forEach(data, function(value, key){
						$scope.units.push({'id': value.unitCode.trim(), 'value': value.unitCode.trim()});
					});
				});
	}
	
	function updateResourceSummaries(resourceSummaryList) {
		resourceSummaryService.updateResourceSummaries($scope.jobNo, resourceSummaryList)
		.then(
				function( data ) {
					if(data.length!=0){
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
					}else{
						modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Resources have been updated.");
						$state.reload();
					}
				});
	}
	
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

}])
.filter('mapExclude', function() {
  var excludeHash = {
    'true': 'Excluded',
    'false': 'Included'
  };

  return function(input) {
      return excludeHash[input];
  };
})
.directive('myCustomDropdown', function() {
  return {
    template: '<select class="form-control input-sm" ng-model="colFilter.term" ng-options="option.id as option.value for option in colFilter.options"></select>'
  };
});