mainApp.controller('AdminRevisionsMonthlyMovementCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'forecastService', 'rootscopeService', '$q', '$state', 'confirmService', 'GlobalMessage', 'uiGridConstants',
    function ($scope, modalService, GlobalHelper, GlobalParameter, forecastService, rootscopeService, $q, $state, confirmService, GlobalMessage, uiGridConstants) {

        $scope.onSubmitMonthlyMovementSearch = onSubmitMonthlyMovementSearch;

        $scope.forecastFlagOptions = [
            {id: 'Forecast', value: 'F'},
            {id: 'Rolling Forecast', value: 'RF'}
        ];

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
                {field: 'id', width: '120', displayName: "ID", enableCellEdit: false, sort: { direction: uiGridConstants.DESC}},
                {field: 'noJob', width: '120', displayName: "Job Number", enableCellEdit: false},
                {field: 'year', width: '120', displayName: "Year", enableCellEdit: false},
                {field: 'month', width: '120', displayName: "Month", enableCellEdit: false},
                {field: 'forecastType', width: '120', displayName: "Type", enableCellEdit: false},
                {field: 'forecastDesc', width: '120', displayName: "Description", enableCellEdit: false},
                {
                    field: 'forecastFlag',
                    width: '240',
                    editableCellTemplate: 'ui-grid/dropdownEditor',
                    editDropdownValueLabel: 'id',
                    editDropdownIdLabel: 'value',
                    editDropdownOptionsArray: $scope.forecastFlagOptions,
                    enableCellEdit: $scope.canEdit
                },
                {field: 'amount', width: '240', displayName: "Amount", enableCellEdit: $scope.canEdit},
                {
                    field: 'date',
                    type: 'date',
                    cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"',
                    width: '240',
                    displayName: "Critical Program Date",
                    enableCellEdit: $scope.canEdit
                },
                {
                    name: 'Date Button',
                    width: '100',
                    cellTemplate : `<button ng-click="grid.appScope.clearDate(row)">Clear Date</button>`
                },
                {field: 'explanation', width: '240', displayName: "Explanation", enableCellEdit: $scope.canEdit}
            ]
        }

        $scope.clearDate = function(row) {
            row.entity.date = '';
            $scope.gridApi.rowEdit.setRowsDirty([row.entity]);
        }

        $scope.updateGrid = function () {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            if (dataRows.length > 0) {
                forecastService.updateForecastListAdmin(dataRows)
                    .then(function (data) {
                        $scope.gridApi.rowEdit.setRowsClean(dataRows);
                        if (data == '') {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Records updated');
                        }
                    })
            } else {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', 'No record is modified');
            }
        }

        $scope.deleteRows = function () {
            var selectedRows = $scope.gridApi.selection.getSelectedRows();
            if (selectedRows.length == 0) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select rows to delete.");
                return;
            }
            confirmService.show({}, {bodyText: GlobalMessage.deleteMonthlyMovement})
                .then(function (response) {
                    if (response === 'Yes') {
                        deleteRows(selectedRows);
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

        function deleteRows(rowsToDelete) {
            var apiCalls = [];
            for (var i = 0; i < rowsToDelete.length; i++) {
                var currentRow = rowsToDelete[i];
                var id = currentRow.id;
                var jobNo = currentRow.noJob;
                var apiCall = forecastService.deleteByJobNo(jobNo, id);
                apiCalls.push(apiCall);
            }
            $q.all(apiCalls).then(function (data) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Rows have been deleted.");
                onSubmitMonthlyMovementSearch();
            });
        }

        function onSubmitMonthlyMovementSearch() {
            forecastService.getForecastList($scope.monthlyMovementSearch.jobNo, $scope.monthlyMovementSearch.year, $scope.monthlyMovementSearch.month, $scope.monthlyMovementSearch.forecastFlag)
                .then(function (data) {
                    for (var i=0; i<data.length; i++) {
                        data[i].date = data[i].date ? new Date(data[i].date) : null;
                    }
                    $scope.gridOptions.data = data;
                    $scope.monthlyMovementList = data;
                })
        }

    }]);
