mainApp.controller('AdminRevisionsAttachmentCtrl', ['$q', '$scope', '$filter', 'GlobalHelper', 'GlobalParameter', 'attachmentService', 'repackagingService','rootscopeService',
										function($q, $scope, $filter, GlobalHelper, GlobalParameter, attachmentService, repackagingService,rootscopeService){
	$scope.GlobalParameter = GlobalParameter;
	$scope.attachmentSearch = {};
	$scope.attachmentSearch.nameObject = 'GT58010';
	$scope.showJobParam = true;
	$scope.showSubParam = true;
	$scope.showAltParam = false;
	$scope.showVendor = false;
	showCols(6);
	$scope.onSubmitAttachmentSearch = onSubmitAttachmentSearch;
	$scope.onDeleteAttachmentRecord = onDeleteAttachmentRecord;
	$scope.cols = [
        { field: 'id', width: '60', displayName: "ID", enableCellEdit: false, showInVendor:false },
        { field: 'idTable', width: '60', displayName: "Table Id", enableCellEdit: false, showInVendor:false },
        { field: 'nameTable', width: '100', displayName: "Table Name", enableCellEdit: false, showInVendor:false},
        { field: 'noSequence', width: '100', displayName: "Sequence No.", enableCellEdit: false, showInVendor:true },
        { field: 'typeDocument', width: '100', displayName: "Docment Type", enableCellEdit: false, showInVendor:false },
        { field: 'nameFile', width: '200', displayName: "File Name", enableCellEdit: false, showInVendor:true},
        { field: 'pathFile', width: '200', displayName: "File Path", enableCellEdit: false, showInVendor:true},
        { field: 'text', width: '200', displayName: "Text", enableCellEdit: false, showInVendor:true},
        { field: 'usernameCreated', width: '100', displayName: "Created User", enableCellEdit: false, showInVendor:false},
        { field: 'dateCreated', width: '100', displayName: "Created Date", enableCellEdit: false, showInVendor:false},
        { field: 'usernameLastModified', width: '100', displayName: "Last Modified User", enableCellEdit: false, showInVendor:false},
        { field: 'dateLastModified', width: '100', displayName: "Loast Modified Date", enableCellEdit: false, showInVendor:false},
        ];
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			showColumnFooter: true,
			enableCellEditOnFocus : true,
			allowCellFocus: false,
			exporterMenuPdf: false,
			enableCellSelection: false,
			rowEditWaitInterval :-1,
			columnDefs: $scope.cols
	}
	
	$scope.gridOptions.onRegisterApi = function(gridApi){
		$scope.gridApi = gridApi;
		gridApi.selection.on.rowSelectionChanged($scope,function(row){
			 if($scope.gridApi.selection.getSelectedCount() > 0) {
				 $scope.selected = true;
			 } else {
				 $scope.selected = false;
			 }
	      });
	 
	      gridApi.selection.on.rowSelectionChangedBatch($scope,function(rows){
			 if($scope.gridApi.selection.getSelectedCount() > 0) {
				 $scope.selected = true;
			 } else {
				 $scope.selected = false;
			 }
	      });
	}
	
	$scope.checkParam = function(){
		clearSelectedRows();
		$scope.gridOptions.data = [];
		switch($scope.attachmentSearch.nameObject){
		case 'GT58024':
			$scope.showJobParam = false;
			$scope.showSubParam = false;
			$scope.showAltParam = false;
			$scope.showVendor = true;
			showCols(8);
			break;
		case 'TRANSIT':
			$scope.showJobParam = true;
			$scope.showSubParam = false;
			$scope.showAltParam = false;
			$scope.showVendor = false;
			showCols(8);
			break;
		case 'GT59026':
			$scope.showJobParam = true;
			$scope.showSubParam = false;
			$scope.showAltParam = true;
			$scope.showVendor = false;
			$scope.altParamString = 'MAIN_CERT Number';
			showCols(6);
			break;
		case 'REPACKAGING':
			$scope.showJobParam = true;
			$scope.showSubParam = false;
			$scope.showAltParam = false;
			$scope.showVendor = false;
			$scope.altParamString = 'REPACKAGING Version';
			showCols(8);
			break;
		default:
			$scope.showJobParam = true;
			$scope.showSubParam = true;
			$scope.showVendor = false;
			var altParamList = ['ADDENDUM', 'PAYMENT', 'MAIN_CERT']; 
			var nameObjectIndex = GlobalParameter.findWithAttr(GlobalParameter.nameObjectTable, 'value', $scope.attachmentSearch.nameObject);
			var nameObjectValue = GlobalParameter.nameObjectTable[nameObjectIndex];
			var altParamIndex = nameObjectValue != null ? altParamList.indexOf(nameObjectValue.display) : -1;
			if( altParamIndex > -1){
				showCols(4);
				$scope.showAltParam = true;
				$scope.altParamString = altParamIndex == 3 ? altParamList[altParamIndex] + ' ID' : altParamList[altParamIndex] + ' Number';
			} else {
				showCols(6);
				$scope.showAltParam = false;
				$scope.altParamString = '';
			}
			break;
		}
	}
	
	function showCols(num){
		$scope.show4Col = false;
		$scope.show6Col = false;
		$scope.show8Col = false;
		switch(num){
		case 4:
			$scope.show4Col = true;
			break;
		case 6:
			$scope.show6Col = true;
			break;
		case 8:
			$scope.show8Col = true;
			break;
		}
	}
	function clearSelectedRows(){
		$scope.gridApi.selection.clearSelectedRows($scope.gridApi.selection.getSelectedRows());
		$scope.selected = false;
	}
	
	function onSubmitAttachmentSearch(){
		$scope.gridOptions.data = [];
		clearSelectedRows();
		getTextKey()
		.then(function(response){
			attachmentService.obtainAttachmentList($scope.attachmentSearch.nameObject, response.textKey)
			.then(function(data){
				$scope.gridOptions.data = data;
			})
		})
	}
	
	function getTextKey(){
		var deferral = $q.defer();
		switch($scope.attachmentSearch.nameObject){
		case 'GT58024':
			$scope.gridOptions.columnDefs = $filter('filter')($scope.cols, {showInVendor: true});
			deferral.resolve({
				textKey : $scope.attachmentSearch.vendorNo
			});
			break;
		case 'TRANSIT':
			deferral.resolve({
				textKey : $scope.attachmentSearch.jobNo + '|--|'
			});
			break;
		case 'GT59026':
			deferral.resolve({
				textKey : $scope.attachmentSearch.jobNo + '|--|' + $scope.attachmentSearch.altParam
			});
			break;
		case 'REPACKAGING':
			repackagingService.getLatestRepackaging($scope.attachmentSearch.jobNo)
			.then(function(response){
				deferral.resolve({
					textKey : $scope.attachmentSearch.jobNo + '|--|' + response.id
				});
			})
			break;
		default:
			$scope.gridOptions.columnDefs = $scope.cols;
			deferral.resolve({
				textKey : $scope.attachmentSearch.jobNo + '|' + $scope.attachmentSearch.subcontractNo + '|' + $scope.attachmentSearch.altParam
			});
			break;
		}
		return deferral.promise;
	}
	
	function onDeleteAttachmentRecord(){
		var rows = $scope.gridApi.selection.getSelectedRows();
		rows.forEach(function(row){
			getTextKey()
			.then(function(response){
				attachmentService.deleteAttachmentAdmin($scope.attachmentSearch.nameObject, response.textKey, parseInt(row.noSequence))
				.then(function(data){
					onSubmitAttachmentSearch();
				});
			})
		})
	}
	
}]);
