mainApp.controller('AdminRevisionsRocSubdetailCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'rocService', 'rootscopeService', 'uiGridConstants', 'confirmService',
    function ($scope, modalService, GlobalHelper, GlobalParameter, rocService, rootscopeService, uiGridConstants, confirmService) {

        $scope.onSubmitRocSubdetailSearch = onSubmitRocSubdetailSearch;

        $scope.RocSubdetailSearch = {};

        $scope.rocCategoryOptions = [];

        rocService.getRocCategoryList().then(function(data) {
            $scope.rocCategoryOptions = data;
            if ($scope.rocCategoryOptions && $scope.rocCategoryOptions.length > 0)
                $scope.RocSubdetailSearch.rocCategory = $scope.rocCategoryOptions[0];
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
                {field: 'itemNo', displayName: "Item No", width: '120', enableCellEdit: false},
                {field: 'description', displayName: "Secondary Detail", width: '120', enableCellEdit: $scope.canEdit},
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
                {field: 'year', width: '120', displayName: "Year", enableCellEdit: false},
                {field: 'month', width: '120', displayName: "Month", enableCellEdit: false},
                {field: 'hyperlink', width: '120', displayName: "Hyperlink", enableCellEdit: true},
                {field: 'remarks', displayName: "Remarks", enableCellEdit: $scope.canEdit},
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
                rocService.updateRocSubdetailListAdmin($scope.RocSubdetailSearch.jobNo, dataRows)
                    .then(function (data) {
                        if(data.length>0){
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
                        }else{
                            $scope.gridApi.rowEdit.setRowsClean(dataRows);
                            if (data == '') {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Roc Subdetail updated');
                            }
                        }
                    })
            } else {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No Roc Subdetail modified');
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
                        rocService.deleteRocSubdetailListAdmin($scope.RocSubdetailSearch.jobNo, selectedRows)
                            .then(function(data) {
                                if(data.length>0){
                                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
                                }else{
                                    $scope.gridApi.rowEdit.setRowsClean(selectedRows);
                                    if (data == '') {
                                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Roc Subdetail deleted');
                                        $scope.gridApi.selection.clearSelectedRows();
                                        onSubmitRocSubdetailSearch();
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

        function onSubmitRocSubdetailSearch() {
            if (GlobalHelper.checkNull([$scope.RocSubdetailSearch.jobNo, $scope.RocSubdetailSearch.period])) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and period!");
                return;
            }
            rocService.getRocSubdetailListAdmin($scope.RocSubdetailSearch.jobNo, $scope.RocSubdetailSearch.itemNo, $scope.RocSubdetailSearch.period)
                .then(function (data) {
                    for (var i=0; i<data.length; i++) {
                        data[i].inputDate = data[i].inputDate ? new Date(data[i].inputDate) : null;
                    }
                    $scope.gridOptions.data = data;
                })
        }

    }]);
