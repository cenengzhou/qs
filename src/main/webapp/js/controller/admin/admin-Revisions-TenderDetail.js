mainApp.controller('AdminRevisionsTenderDetailCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'tenderService', 'rootscopeService', 'uiGridConstants',
    function ($scope, modalService, GlobalHelper, GlobalParameter, tenderService, rootscopeService, uiGridConstants) {

        $scope.onSubmitTenderDetailSearch = onSubmitTenderDetailSearch;

        rootscopeService.gettingUser()
            .then(function (response) {
                $scope.isEditable = GlobalHelper.containRole('ROLE_QS_QS_ADM', response.user.UserRoles);
            });

        $scope.canEdit = function () {
            return $scope.isEditable;
        }

        $scope.gridOptions = {
            enableFiltering: true,
            enableColumnResizing: true,
            enableGridMenu: true,
            enableRowSelection: true,
            enableSelectAll: true,
            enableFullRowSelection: false,
            multiSelect: true,
            showGridFooter: true,
            showColumnFooter: true,
            enableCellEditOnFocus: true,
            allowCellFocus: false,
            exporterMenuPdf: false,
            enableCellSelection: false,
            rowEditWaitInterval: -1,
            columnDefs: [
                {field: 'id', width: '120', displayName: "ID", enableCellEdit: false, visible: false},
                {field: 'tender.jobNo', width: '120', displayName: "Job Number", enableCellEdit: false},
                {field: 'tender.packageNo', width: '120', displayName: "Subcontract", enableCellEdit: false},
                {field: 'tender.vendorNo', width: '120', displayName: "Vendor", enableCellEdit: false},
                {field: 'sequenceNo', width: '120', displayName: "Sequence Number", enableCellEdit: false},
                {field: 'description', width: '240', displayName: "Description", enableCellEdit: $scope.canEdit},
                {
                    field: "quantity",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:4'
                },
                {
                    field: "rateBudget",
                    displayName: "Budget Rate",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:4'
                },
                {
                    field: "amountBudget",
                    displayName: "Budget Amount",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                },
                {
                    field: "rateSubcontract",
                    displayName: "SC Rate",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:4'
                },
                {
                    field: "amountSubcontract",
                    displayName: "SC Amount",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    aggregationType: uiGridConstants.aggregationTypes.sum,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
                },
                {
                    field: "amountForeign",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    aggregationType: uiGridConstants.aggregationTypes.sum,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
                },
                {field: "objectCode", enableCellEdit: $scope.canEdit, width: 60},
                {field: "subsidiaryCode", enableCellEdit: $scope.canEdit, width: 80},
                {field: 'lineType', width: '120', displayName: "Line Type", enableCellEdit: $scope.canEdit},
                {field: "unit", enableCellEdit: $scope.canEdit, width: 50},
                {field: "billItem", displayName: "B/P/I", enableCellEdit: false, width: 50}
            ]
        }

        $scope.updateGrid = function () {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            if (dataRows.length > 0) {
                tenderService.updateTenderDetailListAdmin(dataRows)
                    .then(function (data) {
                        $scope.gridApi.rowEdit.setRowsClean(dataRows);
                        if (data == '') {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Tender Detail updated');
                        }
                    })
            } else {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No Tender Detail modified');
            }
        }

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                if (newValue != oldValue) {
                    $scope.gridApi.rowEdit.setRowsDirty([rowEntity]);
                }
            });
        }

        function onSubmitTenderDetailSearch() {
            tenderService.getTenderDetailList($scope.tenderDetailSearch.jobNo, $scope.tenderDetailSearch.packageNo, $scope.tenderDetailSearch.vendorNo)
                .then(function (data) {
                    $scope.gridOptions.data = data;
                    $scope.tenderDetailList = data;
                })
        }

    }]);
