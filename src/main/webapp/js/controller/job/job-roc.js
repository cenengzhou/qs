mainApp.controller('JobRocCtrl', ['$scope', 'rocService', '$uibModal', '$cookies', 'modalService', '$sce', '$state', 'GlobalParameter', 'rootscopeService', '$timeout', '$q', 'uiGridConstants', 'uiGridGroupingConstants', 'confirmService', 'GlobalMessage',
    function ($scope, rocService, $uibModal, $cookies, modalService, $sce, $state, GlobalParameter, rootscopeService, $timeout, $q, uiGridConstants, uiGridGroupingConstants, confirmService, GlobalMessage) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.editable = false;

        $scope.jobNo = $cookies.get("jobNo");

        $scope.showPreviousAndMovementAmount = false;
        $scope.numberCellFilter = 'number:0';

        $scope.showClosedItem = false;

        rocService.getCutoffDate().then(function(data) {
            $scope.cutoffDate = data;
            var d = new Date();
            if (d.getDate() <= $scope.cutoffDate) {
                d.setMonth(d.getMonth() - 1);
            }
            $scope.month = d.getMonth() + 1;
            $scope.year = d.getFullYear();
            $scope.monthYear = $scope.year + '-' + $scope.month;
        });

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

        $scope.openEditRocDialog = function (rowEntity) {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            if (dataRows.length > 0) {
                confirmService.show({}, {bodyText: GlobalMessage.leavingUpdatedGrid})
                    .then(function (response) {
                        if (response === 'Yes')
                            modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'UPDATE', rowEntity);
                    });
                return;
            }
            modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'UPDATE', rowEntity);
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

        $scope.displayClosedItem = function() {
            if ($scope.showClosedItem) {
                $scope.gridApi.grid.columns[6].filters[0].term = '';
                $scope.gridOptions.columnDefs[5].visible = true;
            } else {
                $scope.gridApi.grid.columns[6].filters[0].term = 'Live';
                $scope.gridOptions.columnDefs[5].visible = false;
            }
            $scope.gridApi.grid.refresh();

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
            treeRowHeaderAlwaysVisible: false,
            showTreeRowHeader: false,
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

                {field: 'id', type: 'number', displayName: 'ROC Id', width: 100, visible: false, enableCellEdit: false},
                {field: 'assignedNo', type: 'number', displayName: 'Item', width: 100, visible: true, enableCellEdit: false,
                    sort: {priority: 1, direction: uiGridConstants.ASC}},
                {field: 'projectNo', width: 100, visible: false, enableCellEdit: false},
                {field: 'classification', width: 100, visible: false, enableCellEdit: false},
                {field: 'impact', width: 150, visible: false, enableCellEdit: false},
                {field: 'status', width: 100, visible: false, enableCellEdit: false,
                filter: {
                    term: 'Live'
                }},
                {field: 'projectRef', displayName: "Project R&O Ref.", width: 100, enableCellEdit: false, visible: false},
                {field: 'description', displayName: "Description", width: 250, enableCellEdit: false, visible: true,
                    cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',},
                {
                    field: 'rocCategory', displayName: "Category", width: 200, enableCellEdit: false,
                    grouping: {groupPriority: 0}, sort: {priority: 0, direction: uiGridConstants.DESC},
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].groupVal) != -1">{{ row.treeNode.aggregations[0].groupVal }} (Below the Line)</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) != -1">{{ MODEL_COL_FIELD }}</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].groupVal) == -1">{{ row.treeNode.aggregations[0].groupVal }} (Above the Line)</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) == -1">{{ MODEL_COL_FIELD }}</div>'
                },
                {
                    field: 'rocDetail.amountBest',
                    displayName: "Best Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].groupVal) != -1">{{ COL_FIELD | number:0}}</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) != -1">{{ MODEL_COL_FIELD | number:0}}</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].groupVal) == -1">N/A</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) == -1">N/A</div>',
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false
                },
                {
                    field: 'rocDetail.amountExpected',
                    displayName: "Expected Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false
                },
                {
                    field: 'rocDetail.amountWorst',
                    displayName: "Worst Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].groupVal) != -1">{{ COL_FIELD | number:0}}</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) != -1">{{ MODEL_COL_FIELD | number:0}}</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].groupVal) == -1">N/A</div>' +
                        '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) == -1">N/A</div>',
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false
                },
                {
                    field: 'rocDetail.previousAmountBest',
                    displayName: "Previous Best Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellClass: customCellClass,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.previousAmountExpected',
                    displayName: "Previous Expected Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellClass: customCellClass,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.previousAmountWorst',
                    displayName: "Previous Worst Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    cellClass: customCellClass,
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    enableCellEdit: false,
                    visible: $scope.showPreviousAndMovementAmount
                },
                {
                    field: 'rocDetail.movementBest',
                    displayName: "Movement Best Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    visible: $scope.showPreviousAndMovementAmount,
                    enableCellEdit: false
                },
                {
                    field: 'rocDetail.movementExpected',
                    displayName: "Movement Expected Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    visible: $scope.showPreviousAndMovementAmount,
                    enableCellEdit: false
                },
                {
                    field: 'rocDetail.movementWorst',
                    displayName: "Movement Worst Case",
                    width: 120,
                    cellClass: 'text-right',
                    cellFilter: $scope.numberCellFilter,
                    treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
                    customTreeAggregationFinalizerFn: function (aggregation) {
                        aggregation.rendered = aggregation.value;
                    },
                    footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                    footerCellClass: customFooterClass,
                    visible: $scope.showPreviousAndMovementAmount,
                    enableCellEdit: false
                },
                {
                    field: 'rocDetail.remarks',
                    displayName: "Remarks / Actions to be taken",
                    cellClass: 'blue',
                    headerCellClass: 'blue',
                    cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                    editableCellTemplate: '<textarea class="roc-remarks-textarea" ui-grid-editor rows="1" cols="1" maxlength="4000" ng-model="MODEL_COL_FIELD" onfocus="textareaAutosize(event, this)" onkeydown="textareaAutosize(event, this)" onmousedown="textareaAutosize(event, this)" />'
                },
                {
                    name: 'Buttons', displayName: '',  width: 180, enableCellEdit: false, enableFiltering: false, allowCellFocus: false,
                    cellTemplate: '<div class="col-md-12" style="padding: 5px 20px">' +
                        '<div class="row" ng-if="!row.groupHeader"><button class="btn btn-sm icon-btn btn-success" ng-click="grid.appScope.openEditRocDialog(row.entity)"> <span class="fa fa-pencil" style="padding-left:10px;" ></span> Edit</button></div>' +
                        '<div class="row m-t-5" ng-if="!row.groupHeader && (row.entity.createdDate != row.entity.lastModifiedDate) && (row.entity !== null && row.entity.id !== null && row.entity.id >= 0)"><button class="btn btn-sm icon-btn btn-info" ng-click="grid.appScope.viewRocHistory(row.entity)"> <span class="fa fa-history" style="padding-left:10px" ></span> History</button></div>' +
                        '<div class="row m-t-5" ng-if="!row.groupHeader"><button class="btn btn-sm icon-btn btn-warning" ng-click="grid.appScope.editRocSubdetail(row.entity)"> <span class="fa fa-pencil" style="padding-left:10px" ></span> Subdetail</button></div>' +
                        '</div>'
                },
                {field: 'rocDetail.year', displayName: 'Year', width: 100, visible: false},
                {field: 'rocDetail.month', displayName: 'Month', width: 100, visible: false},
                {field: 'rocDetail.id', displayName: 'ROC Detail Id', width: 100, visible: false}

            ]

        };

        $scope.editRocSubdetail = function(entity) {
            modalService.open('xxlg', 'view/job/modal/job-roc-subdetail.html', 'JobRocSubdetailCtrl', 'UPDATE', { 'roc': entity });
        }

        $scope.viewRocHistory = function(entity) {
            if (entity !== null && entity.id !== null && entity.id >= 0) {
                var id = entity.id;
                var detailId = (entity.rocDetail && entity.rocDetail.id)? entity.rocDetail.id : null;
                modalService.open('xxlg', 'view/job/modal/job-roc-history.html', 'JobRocHistoryCtrl', 'READ', {'id': id, 'detailId': detailId});
            } else {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Cannot view roc history.");
            }

        }

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

        $scope.$on("reloadRocList", function(event, data) {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map(function (gridRow) {
                return gridRow.entity;
            });
            $scope.gridApi.rowEdit.setRowsClean(dataRows);
            getData($scope.year, $scope.month);
        });


    }]);

