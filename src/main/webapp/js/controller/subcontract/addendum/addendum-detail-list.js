mainApp.controller('AddendumDetailListCtrl', ['$scope' , 'modalService', 'addendumService', '$cookies', '$location', 'uiGridConstants',
                                              function($scope , modalService, addendumService, $cookies, $location, uiGridConstants) {

	$scope.addendumNo = $cookies.get('addendumNo');
	var addendumDetailHeaderRef = "";


	getAllAddendumDetails();


	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSorting: false,
			//enableSelectAll: true,
			//enableFullRowSelection: true,
			multiSelect: false,
			showGridFooter : false,
			showColumnFooter : false,
			//fastWatch : true,
			exporterMenuPdf: false,

			columnDefs: [
			             { field: 'id', width:80, visible: false},
			             { field: 'typeHd', width:80, visible: false},
			             { field: 'typeVo', displayName:"Type", width:50},
			             { field: 'bpi',  width:100 },
			             {field: 'description' ,  width:100 },
			             { field: 'quantity' ,  width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'rateAddendum' , displayName:"SC Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'amtAddendum' , displayName:"SC Amount", width:100, enableFiltering: false, 
		            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.amtAddendum < 0) {
		            				c += ' red';
		            			}
		            			return c;
		            		},
		            		cellFilter : 'number:2',
			             },
			             { field: 'rateBudget', displayName:"Budget Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'amtBudget' , displayName:"Budget Amount", width:100, enableFiltering: false, 
		            	 cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		            			var c = 'text-right';
		            			if (row.entity.amtBudget < 0) {
		            				c += ' red';
		            			}
		            			return c;
		            		},
		            		cellFilter : 'number:2',
			             },
			             {field: 'codeObject' , displayName:"Object Code", width:90 },
			             {field: 'codeSubsidiary' ,displayName:"Subsidiary Code",  width:100 },
			             {field: 'noSubcontractChargedRef', displayName:"Corr. Subcontract No.", width:80 },
		            	 {field: 'codeObjectForDaywork', displayName:"Alt. Object Code", width:80 },
			             {field: 'unit' ,  width:60 },
			             {field: 'remarks' ,  width:100 },
			             {field: 'idHeaderRef',width:80, displayName:"Header Group", visible: true},
			             {field: 'typeAction',displayName:"Action", width:80, visible: true}

			             ],
			             rowTemplate: '<div ng-class="{\'red\':row.entity.typeAction==\'DELETE\', \'blue\':row.entity.typeAction==\'UPDATE\' }"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>'
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		$scope.gridApi = gridApi;

		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			if(row.entity.typeHd == 'HEADER')
				addendumDetailHeaderRef = row.entity.id;
			else{
				if(row.entity.idHeaderRef != null){
					addendumDetailHeaderRef = row.entity.idHeaderRef;
				}
				else{
					addendumDetailHeaderRef = 'Empty';
				}
			}
		});

	}



	$scope.edit = function(){
		$scope.selectedRows  = $scope.gridApi.selection.getSelectedRows();

		if($scope.selectedRows.length == 0){
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select a row to edit.");
			return;
		}

		$cookies.put('addendumDetailHeaderRef', addendumDetailHeaderRef);

		$location.path('/subcontract/addendum/tab/details');
	}


	function getAllAddendumDetails(){
		if($scope.addendumNo != null && $scope.addendumNo.length != 0){
			addendumService.getAllAddendumDetails($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
			.then(
					function( data ) {
						$scope.gridOptions.data = data;
					});
		}else
			modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Addendum does not exist. Please create addendum title first.");


	}



}]);