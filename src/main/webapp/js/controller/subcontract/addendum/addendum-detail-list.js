mainApp.controller('AddendumDetailListCtrl', ['$scope' , 'modalService', 'addendumService', '$cookies', '$location',
                                              function($scope , modalService, addendumService, $cookies, $location) {

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
			//showGridFooter : true,
			//showColumnFooter : true,
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
			             { field: 'amtAddendum' , displayName:"SC Amount", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'rateBudget', displayName:"Budget Rate", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             { field: 'amtBudget' , displayName:"Budget Amount", width:100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:2'  },
			             {field: 'codeObject' ,  width:100 },
			             {field: 'codeSubsidiary' ,  width:100 },
			             {field: 'unit' ,  width:100 },
			             {field: 'remarks' ,  width:100 },
			             {field: 'idHeaderRef',width:80, visible: true},
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


		/*var arrayLength = $scope.selectedRows.length;
		for (var i = 0; i < arrayLength; i++) {
			console.log($scope.selectedRows[i]['lineType']);
		}*/
	}


	function getAllAddendumDetails(){
		addendumService.getAllAddendumDetails($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
		.then(
				function( data ) {
					$scope.gridOptions.data = data;
				});
	}
	
	

}]);