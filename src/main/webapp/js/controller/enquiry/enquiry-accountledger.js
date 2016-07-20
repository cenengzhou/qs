
mainApp.controller('EnquiryAccountLedgerCtrl', ['$scope' , '$rootScope', '$http', 'modalService', 'blockUI', 'adlService', 
                                        function($scope , $rootScope, $http, modalService, blockUI, adlService) {
	
	$scope.blockAccountLedger = blockUI.instances.get('blockAccountLedger');
	$scope.gridOptions = {
			enableFiltering: true,
			enableColumnResizing : true,
			enableGridMenu : true,
			enableRowSelection: true,
			enableSelectAll: true,
			enableFullRowSelection: false,
			multiSelect: true,
			showGridFooter : true,
			enableCellEditOnFocus : false,
			paginationPageSizes : [ 25, 50, 100, 150, 200 ],
			paginationPageSize : 25,
			allowCellFocus: false,
			enableCellSelection: false,
			columnDefs: [
			             { field: 'entityDocCompanyKey', width:'100', displayName: "Entity Doc Company Key", enableCellEdit: false },
			             { field: 'typeDocument', width:'100', displayName: "Document Type", enableCellEdit: false },
			             { field: 'numberDocument', width:'100', displayName: "Document", enableCellEdit: false },
			             { field: 'numberJournalEntryLine', width:'100', displayName: "Journal Entry Line", enableCellEdit: false },
			             { field: 'numberJeLineExtension', width:'100', displayName: "Je Line Extension", enableCellEdit: false },
			             { field: 'accountTypeLedger', width:'100', displayName: "Account Type Ledger", enableCellEdit: false },
			             { field: 'accountPeriod', width:'100', displayName: "Account Period", enableCellEdit: false },
			             { field: 'typeDocumentDescription', width:'100', displayName: "Document Type Description", enableCellEdit: false },
			             { field: 'dateGl', width:'100', displayName: "GL Date", cellFilter: 'date', enableCellEdit: false },
			             { field: 'statusPost', width:'100', displayName: "Post Status", enableCellEdit: false },
			             { field: 'statusPostDescription', width:'100', displayName: "Post Status Description", enableCellEdit: false },
			             { field: 'numberBatch', width:'100', displayName: "Batch Number", enableCellEdit: false },
			             { field: 'typeBatch', width:'100', displayName: "Batch Type", enableCellEdit: false },
			             { field: 'typeBatchDescription', width:'100', displayName: "Batch Type Description", enableCellEdit: false },
			             { field: 'dateBatch', width:'100', displayName: "Batch Date", cellFilter: 'date', enableCellEdit: false },
			             { field: 'dateInput', width:'100', displayName: "Input Date", cellFilter: 'date', enableCellEdit: false },
			             { field: 'entityCompanyKey', width:'100', displayName: "Entity Company Key", enableCellEdit: false },
			             { field: 'accountGl', width:'100', displayName: "Account GL", cellClass: 'text-right', cellFilter: 'number:4', enableCellEdit: false },
			             { field: 'accountKey', width:'100', displayName: "Account Key", enableCellEdit: false },
			             { field: 'entityBusinessUnitKey', width:'100', displayName: "Entity Business Unit Key", enableCellEdit: false },
			             { field: 'accountObject', width:'100', displayName: "Object Code", enableCellEdit: false },
			             { field: 'accountSubsidiary', width:'100', displayName: "Subsidiary Code", enableCellEdit: false },
			             { field: 'accountSubLedger', width:'100', displayName: "Sub Ledger", enableCellEdit: false },
			             { field: 'accountTypeSubLedger', width:'100', displayName: "Sub Ledger Type", enableCellEdit: false },
			             { field: 'accountTypeLedgerDescr', width:'100', displayName: "Ledger Type Description", enableCellEdit: false },
			             { field: 'accountFiscalYear', width:'100', displayName: "Fiscal Year", enableCellEdit: false },
			             { field: 'currencyCode', width:'100', displayName: "Currency", enableCellEdit: false },
			             { field: 'amount', width:'100', displayName: "Amount", cellClass: 'text-right', cellFilter: 'number:4', enableCellEdit: false },
			             { field: 'itemKey', width:'100', displayName: "Item Key", enableCellEdit: false },
			             { field: 'quantity', width:'100', displayName: "Quantity", enableCellEdit: false },
			             { field: 'quantityUnitOfMeasurement', width:'100', displayName: "Unit of Measurement", enableCellEdit: false },
			             { field: 'accountGlClass', width:'100', displayName: "GL Class", enableCellEdit: false },
			             { field: 'typeReverseOrVoid', width:'100', displayName: "Reverss or Void", enableCellEdit: false },
			             { field: 'explanationAddressBook', width:'100', displayName: "Explanation Address Book", enableCellEdit: false },
			             { field: 'explanationRemark', width:'100', displayName: "Explanation Remark", enableCellEdit: false },
			             { field: 'reference1', width:'100', displayName: "Reference 1", enableCellEdit: false },
			             { field: 'reference2', width:'100', displayName: "Reference 2", enableCellEdit: false },
			             { field: 'reference3', width:'100', displayName: "Reference 3", enableCellEdit: false },
			             { field: 'numberPayItem', width:'100', displayName: "Pay Item", enableCellEdit: false },
			             { field: 'entityAddressBookKey', width:'100', displayName: "Entity Address Book Key", enableCellEdit: false },
			             { field: 'recordKeyVoucher', width:'100', displayName: "Record Key Voucher", enableCellEdit: false },
			             { field: 'recordKeyMatchedPo', width:'100', displayName: "Record Key Matched PO", enableCellEdit: false },
			             { field: 'numberPaymentOrCheck', width:'100', displayName: "Payment or Check", enableCellEdit: false },
			             { field: 'typeReconciliation', width:'100', displayName: "Reconciliation", enableCellEdit: false },
			             { field: 'numberAssetIdentification', width:'100', displayName: "Asset ID", enableCellEdit: false },
			             { field: 'statusFixedAssetPosted', width:'100', displayName: "Fixed Asset Posted Status", enableCellEdit: false },
			             { field: 'numberInvoice', width:'100', displayName: "Invoice", enableCellEdit: false },
			             { field: 'dateInvoice', width:'100', displayName: "Invoice Date", cellFilter: 'date', enableCellEdit: false },
			             { field: 'entityInputBy', width:'100', displayName: "Input by", enableCellEdit: false },
			             { field: 'recordKeyPaymentId', width:'100', displayName: "Record Key Payment ID", enableCellEdit: false },
			             { field: 'entityGlPostedBy', width:'100', displayName: "GL Posted by", enableCellEdit: false },
			             { field: 'datePosted', width:'100', displayName: "Posted Date", cellFilter: 'date', enableCellEdit: false },
			             { field: 'taxRateCode', width:'100', displayName: "Tax Rate", enableCellEdit: false },
			             { field: 'taxExplCode', width:'100', displayName: "Tax Explanation", enableCellEdit: false },
			             { field: 'taxExplanationDescription', width:'100', displayName: "Tax Explanation Description", enableCellEdit: false }
            			 ]
	};

	$scope.gridOptions.onRegisterApi = function (gridApi) {
		  $scope.gridApi = gridApi;
	}
	
	$scope.loadGridData = function(){
		$scope.blockAccountLedger.start('Locading...')
		adlService.getAccountLedgerList($scope.jobNo, $scope.searchTypeLedger, $scope.searchYearStart, $scope.searchYearEnd, $scope.searchMonthStart, 
				$scope.searchMonthEnd, $scope.searchTypeDocument, $scope.searchSubcontractNo, $scope.searchCodeObject, $scope.searchCodeSubsidiary)
		    .then(function(data) {
				if(angular.isArray(data)){
					$scope.gridOptions.data = data;
				} 
				$scope.blockAccountLedger.stop()
			}, function(data){
				$scope.blockAccountLedger.stop();
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data ); 
			});	
	}
	
	$scope.filter = function() {
		$scope.gridApi.grid.refresh();
	};
	$scope.loadGridData();
	
}]);