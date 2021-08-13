mainApp.controller('JobRocCtrl', ['$scope', 'rocService', '$uibModal', '$cookies', 'modalService', '$sce', '$state', 'GlobalParameter', 'rootscopeService', '$timeout', '$q', 'uiGridConstants', 'uiGridGroupingConstants', 'confirmService', 'GlobalMessage',
    function ($scope, rocService, $uibModal, $cookies, modalService, $sce, $state, GlobalParameter, rootscopeService, $timeout, $q, uiGridConstants, uiGridGroupingConstants, confirmService, GlobalMessage) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.editable = false;

        $scope.jobNo = $cookies.get("jobNo");

        var today = new Date();
        $scope.month = today.getUTCMonth() + 1; //months from 1-12
        $scope.year = today.getUTCFullYear();
        $scope.monthYear = $scope.year + '-' + $scope.month;

        $scope.showPreviousAndMovementAmount = false;

        function getData(year, month) {
            var rocListApi = rocService.getRocListSummary($scope.jobNo, year, month);

            $q.all([rocListApi])
                .then(function (data) {
                    $scope.data = {};
                    var rocList = data[0];

                    $scope.gridOptions.data = rocList;
                    for (var i = 0; i < rocList.length; i++) {
                        var curr = rocList[i].rocDetail;
                        if (curr != null) {
                            curr.movementBest = curr.amountBest - curr.previousAmountBest;
                            curr.movementExpected = curr.amountExpected - curr.previousAmountExpected;
                            curr.movementWorst = curr.amountWorst - curr.previousAmountWorst;
                        }

                    }

                });
        }

        function customCellClass(grid, row, col, rowRenderIndex, colRenderIndex) {
            return customAmountClass(row.entity[col.field]);
        }

        function customFooterClass(grid, row, col, rowRenderIndex, colRenderIndex) {
            return customAmountClass(col.getAggregationValue());
        }

        function customAmountClass(value) {
            return value < 0 ? 'text-right red' : 'text-right';
        }

        $scope.openAddRocDialog = function () {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            if (dataRows.length > 0) {
                confirmService.show({}, {bodyText: GlobalMessage.leavingUpdatedGrid})
                    .then(function (response) {
                        if (response === 'Yes')
                            modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'ADD');
                    });
                return;
            }
            modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'ADD');
        }

        $scope.openEditRocDialog = function () {
            var dataRows = $scope.gridApi.selection.getSelectedRows();
            if (dataRows.length == 0 || dataRows.length > 1) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please select 1 row to update roc.");
                return;
            }
            modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'UPDATE', dataRows[0]);
        }

        $scope.saveRocDetails = function () {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            rocService.saveRocDetails($scope.jobNo, dataRows)
                .then(function (data) {
                    if (data.length != 0) {
                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                    } else {
                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ROC Details have been updated.");
                        $scope.gridDirtyRows = null;
                        $scope.gridApi.rowEdit.setRowsClean(dataRows);
                        getData($scope.year, $scope.month);
                    }
                });
        }

        $scope.gridOptions = {
            enableSorting: true,
            enableFiltering: true,
            enableColumnResizing: true,
            enableGridMenu: true,
            enableColumnMoving: true,
            enableCellEditOnFocus: true,
            allowCellFocus: false,
            showGridFooter: false,
            showColumnFooter: true,
            treeRowHeaderAlwaysvisible: false,
            exporterMenuPdf: false,
            groupingNullLabel: '',
            rowEditWaitInterval: -1,
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.grid.registerDataChangeCallback(function () {
                    $scope.gridApi.treeBase.expandAllRows();
                });
                $scope.gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                    if (newValue != oldValue) {
                        switch (colDef.field) {
                            case 'rocDetail.amountBest':
                                if (rowEntity.rocDetail.previousAmountBest !== null)
                                    rowEntity.rocDetail.movementBest = parseInt(newValue) - parseInt(rowEntity.rocDetail.previousAmountBest);
                                break;
                            case 'rocDetail.amountExpected':
                                if (rowEntity.rocDetail.previousAmountExpected !== null)
                                    rowEntity.rocDetail.movementExpected = parseInt(newValue) - parseInt(rowEntity.rocDetail.previousAmountExpected);
                                break;
                            case 'rocDetail.amountWorst':
                                if (rowEntity.rocDetail.previousAmountWorst !== null)
                                    rowEntity.rocDetail.movementWorst = parseInt(newValue) - parseInt(rowEntity.rocDetail.previousAmountWorst);
                                break;
                            case 'rocDetail.movementBest':
                                if (rowEntity.rocDetail.previousAmountBest !== null)
                                    rowEntity.rocDetail.amountBest = parseInt(newValue) + parseInt(rowEntity.rocDetail.previousAmountBest);
                                break;
                            case 'rocDetail.movementExpected':
                                if (rowEntity.rocDetail.previousAmountExpected !== null)
                                    rowEntity.rocDetail.amountExpected = parseInt(newValue) + parseInt(rowEntity.rocDetail.previousAmountExpected);
                                break;
                            case 'rocDetail.movementWorst':
                                if (rowEntity.rocDetail.previousAmountWorst !== null)
                                    rowEntity.rocDetail.amountWorst = parseInt(newValue) + parseInt(rowEntity.rocDetail.previousAmountWorst);
                                break;
                            default:
                        }
                        $scope.gridApi.rowEdit.setRowsDirty([rowEntity]);
                    }
                });
            },

            columnDefs: [
                {field: 'id', displayName: 'ROC Id', width: 100, visible: false},
                {
                    field: 'rocCategory', displayName: "Category", width: 160, enableCellEdit: false,
                    grouping: {groupPriority: 0}, sort: {priority: 0, direction: 'desc'}
                },
                {field: 'projectNo', width: 100, visible: false},
                {field: 'classification', width: 100, visible: false},
                {field: 'impact', width: 150, visible: false},
                {field: 'status', width: 100, visible: false},
                {field: 'projectRef', displayName: "Project R&O Ref.", width: 100, enableCellEdit: false},
                {field: 'description', displayName: "Description", width: 250, enableCellEdit: false},
                {
                    field: 'rocDetail.amountBest',
                    displayName: "Best Case",
                    width: 120,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    headerCellClass: 'blue',
                    footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: true
                },
                {
                    field: 'rocDetail.amountExpected',
                    displayName: "Expected Case",
                    width: 120,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    headerCellClass: 'blue',
                    footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass
                },
                {
                    field: 'rocDetail.amountWorst',
                    displayName: "Worst Case",
                    width: 120,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    headerCellClass: 'blue',
                    footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass
                },
                {
                    field: 'rocDetail.previousAmountBest',
                    displayName: "Previous Best Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellClass: customCellClass,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.previousAmountExpected',
                    displayName: "Previous Expected Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellClass: customCellClass,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.previousAmountWorst',
                    displayName: "Previous Worst Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellClass: customCellClass,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.movementBest',
                    displayName: "Movement Best Case",
                    width: 120,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    headerCellClass: 'blue',
                    footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.movementExpected',
                    displayName: "Movement Expected Case",
                    width: 120,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    headerCellClass: 'blue',
                    footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.movementWorst',
                    displayName: "Movement Worst Case",
                    width: 120,
                    cellClass: 'text-right blue',
                    cellFilter: 'number:2',
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    headerCellClass: 'blue',
                    footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:2 }}</div>',
                    footerCellClass: customFooterClass,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.remarks',
                    displayName: "Remarks",
                    cellClass: 'blue',
                    headerCellClass: 'blue'
                },
                {field: 'rocDetail.year', displayName: 'Year', width: 100, visible: false},
                {field: 'rocDetail.month', displayName: 'Month', width: 100, visible: false}
            ]

        };

        $scope.$watch('monthYear', function (newValue, oldValue) {
            if (oldValue != newValue) {
                var period = $scope.monthYear.split("-");
                $scope.year = period[0];
                $scope.month = period[1];

                getData($scope.year, $scope.month);

                $scope.period = moment().month($scope.month - 1).format('MMM');
                $scope.lastPeriod = moment().month($scope.month - 2).format('MMM');

            }

        }, true);


    }]);
