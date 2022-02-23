mainApp.controller('EnquiryAddendumDetailsCtrl', ['$scope', '$timeout', '$state', 'modalStatus', 'modalParam', '$uibModalInstance', 'addendumService', 'uiGridConstants', 'GlobalParameter', 'GlobalHelper', 'GlobalMessage', 'confirmService',
    function ($scope, $timeout, $state, modalStatus, modalParam, $uibModalInstance, addendumService, uiGridConstants, GlobalParameter, GlobalHelper, GlobalMessage, confirmService) {
        $scope.status = modalStatus;
        $scope.parentScope = modalParam;
        $scope.cancel = function () {
            $uibModalInstance.close();
        };
        $scope.GlobalParameter = GlobalParameter;

        $scope.loadData = function () {
            addendumService.getAllAddendumDetails($scope.parentScope.noJob, $scope.parentScope.noSubcontract, $scope.parentScope.no)
                .then(
                    function (data) {
                        $scope.gridOptions.data = data;
                    });
        }

        $scope.columnDefs = [
            {field: 'id', width: 80, visible: false},
            {field: 'typeHd', width: 80, visible: false},
            {field: 'typeVo', displayName: "Type", width: 50},
            {field: 'bpi', width: 100},
            {field: 'description', width: 100},
            {field: 'quantity', width: 100, enableFiltering: false, cellClass: 'text-right', cellFilter: 'number:4'},
            {
                field: 'rateAddendum',
                displayName: "SC Rate",
                width: 100,
                enableFiltering: false,
                cellClass: 'text-right',
                cellFilter: 'number:4'
            },
            {
                field: 'amtAddendum', displayName: "SC Amount", width: 100, enableFiltering: false,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    var c = 'text-right';
                    if (row.entity.amtAddendum < 0) {
                        c += ' red';
                    }
                    return c;
                },
                cellFilter: 'number:2',
            },
            {
                field: 'rateBudget',
                displayName: "Budget Rate",
                width: 100,
                enableFiltering: false,
                cellClass: 'text-right',
                cellFilter: 'number:4'
            },
            {
                field: 'amtBudget', displayName: "Budget Amount", width: 100, enableFiltering: false,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    var c = 'text-right';
                    if (row.entity.amtBudget < 0) {
                        c += ' red';
                    }
                    return c;
                },
                cellFilter: 'number:2',
            },
            {field: 'codeObject', displayName: "Object Code", width: 90},
            {field: 'codeSubsidiary', displayName: "Subsidiary Code", width: 100},
            {field: 'noSubcontractChargedRef', displayName: "Corr. Subcontract No.", width: 80},
            {field: 'codeObjectForDaywork', displayName: "Alt. Object Code", width: 80},
            {field: 'unit', width: 60},
            {field: 'remarks', width: 100},
            {field: 'idHeaderRef', width: 80, displayName: "Header Group", visible: true},
            {field: 'idResourceSummary', width: 80, displayName: "Resource No.", visible: true},
            {field: 'typeAction', displayName: "Action", width: 80, visible: true},
            {
                field: 'typeRecoverable', displayName: "Recoverable", width: 120,
                cellTemplate: "<div class='ui-grid-cell-contents'>{{row.entity.typeRecoverable ? row.entity.typeRecoverable == 'R' ? 'Recoverable' : 'Non-Recoverable' : null}}</div>"
            }
        ];

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
            enableCellEditOnFocus: false,
            totalServerItems: 0,
            allowCellFocus: false,
            enableCellSelection: false,
            exporterMenuPdf: false,
            columnDefs: $scope.columnDefs
        };

        $scope.loadData();

    }]);