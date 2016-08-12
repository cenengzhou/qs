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
			             { field: 'typeHd', width:80},
			             { field: 'typeVo', width:80},
			             { field: 'bpi',  width:100 },
			             {field: 'description' ,  width:100 },
			             { field: 'rateBudgetTba',  width:100 },
			             { field: 'rateAddendumTba' ,  width:100 },
			             { field: 'quantityTba' ,  width:100 },
			             { field: 'amtBudgetTba' ,  width:100 },
			             { field: 'amtAddendumTba' ,  width:100 },
			             {field: 'codeObject' ,  width:100 },
			             {field: 'codeSubsidiary' ,  width:100 },
			             {field: 'unit' ,  width:100 },
			             {field: 'remarks' ,  width:100 },
			             {field: 'idHeaderRef' ,  width:100 }
			             ]
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
					console.log(data);
					$scope.gridOptions.data = data;
				});
	}

}]);