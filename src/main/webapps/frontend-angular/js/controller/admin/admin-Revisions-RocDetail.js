mainApp.controller('AdminRevisionsRocDetailCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'rocService', 'rootscopeService', 'uiGridConstants', 'confirmService',
    function ($scope, modalService, GlobalHelper, GlobalParameter, rocService, rootscopeService, uiGridConstants, confirmService) {

        $scope.onSubmitRocDetailSearch = onSubmitRocDetailSearch;

        $scope.RocDetailSearch = {};

        $scope.rocCategoryOptions = [];

        rocService.getRocCategoryList().then(function(data) {
            $scope.rocCategoryOptions = data;
            if ($scope.rocCategoryOptions && $scope.rocCategoryOptions.length > 0)
                $scope.RocDetailSearch.rocCategory = $scope.rocCategoryOptions[0];
        });

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
                {field: 'itemNo', width: '120', displayName: "Item No", enableCellEdit: false},
                {field: 'year', width: '120', displayName: "Year", enableCellEdit: false},
                {field: 'month', width: '120', displayName: "Month", enableCellEdit: false},
                {
                    field: "amountBest",
                    displayName: "Best Case",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    aggregationType: uiGridConstants.aggregationTypes.sum,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
                },
                {
                    field: "amountRealistic",
                    displayName: "Realistic",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    aggregationType: uiGridConstants.aggregationTypes.sum,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
                },
                {
                    field: "amountWorst",
                    displayName: "Worst Case",
                    enableCellEdit: $scope.canEdit,
                    width: 110,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    aggregationType: uiGridConstants.aggregationTypes.sum,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" style="text-align:right;"  >{{col.getAggregationValue() | number:2 }}</div>'
                },
                {field: 'remarks', displayName: "Remarks", enableCellEdit: $scope.canEdit},
                {field: 'status', width: '120', displayName: "Status",
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'value',
                    editDropdownOptionsArray: [{id: 'Live', value: 'Live'}, {id: 'Closed', value: 'Closed'}]
                },
                {field: 'systemStatus', width: '120', displayName: "System Status",
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'value',
                    editDropdownOptionsArray: [{id: 'ACTIVE', value: 'ACTIVE'}, {id: 'INACTIVE', value: 'INACTIVE'}]
                }
            ]
        }

        $scope.updateGrid = function () {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            if (dataRows.length > 0) {
                rocService.updateRocDetailListAdmin($scope.RocDetailSearch.jobNo, dataRows)
                    .then(function (data) {
                        if(data.length>0){
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
                        }else{
                            $scope.gridApi.rowEdit.setRowsClean(dataRows);
                            if (data == '') {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Roc Detail updated');
                            }
                        }
                    })
            } else {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No Roc Detail modified');
            }
        }

        $scope.deleteRows = function () {
            var selectedRows = $scope.gridApi.selection.getSelectedRows();
            if (selectedRows.length == 0) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to delete.");
                return;
            }
            confirmService.show({}, {bodyText: 'Selected row will be removed, are you sure to proceed?'})
                .then(function (response) {
                    if (response === 'Yes') {
                        rocService.deleteRocDetailListAdmin($scope.RocDetailSearch.jobNo, selectedRows)
                            .then(function(data) {
                                if(data.length>0){
                                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
                                }else{
                                    $scope.gridApi.rowEdit.setRowsClean(selectedRows);
                                    if (data == '') {
                                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Roc Detail deleted');
                                        $scope.gridApi.selection.clearSelectedRows();
                                        onSubmitRocDetailSearch();
                                    }
                                }
                            });
                    }
                });
        }

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                if (newValue != oldValue) {
                    $scope.gridApi.rowEdit.setRowsDirty([rowEntity]);
                }
            });
        }

        function onSubmitRocDetailSearch() {
            if (GlobalHelper.checkNull([$scope.RocDetailSearch.jobNo, $scope.RocDetailSearch.period])) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and period!");
                return;
            }
            rocService.getRocDetailListAdmin($scope.RocDetailSearch.jobNo, $scope.RocDetailSearch.itemNo, $scope.RocDetailSearch.period)
                .then(function (data) {
                    $scope.gridOptions.data = data;
                    $scope.rocDetailList = data;
                })
        }

    }]);
