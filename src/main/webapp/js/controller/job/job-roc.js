mainApp.controller('JobRocCtrl', ['$scope', 'rocService', 'forecastService', '$uibModal', '$cookies', 'modalService', '$sce', '$state', 'GlobalParameter', 'rootscopeService', '$timeout', '$q', 'uiGridConstants', 'uiGridGroupingConstants', 'confirmService', 'GlobalMessage', 'GlobalHelper', '$interval', '$window',
    function ($scope, rocService, forecastService, $uibModal, $cookies, modalService, $sce, $state, GlobalParameter, rootscopeService, $timeout, $q, uiGridConstants, uiGridGroupingConstants, confirmService, GlobalMessage, GlobalHelper, $interval, $window) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.editable = false;

        $scope.jobNo = $cookies.get("jobNo");

        $scope.showPreviousAndMovementAmount = false;
        $scope.numberCellFilter = 'number:0';

        $scope.showClosedItem = false;

        $scope.buttonTimer = 0;

        $scope.person = {};

        initGrid();

        initOptions();

        rocService.getCutoffPeriod().then(function(data) {
            $scope.cutoffDate = data;
            $scope.cutoffDate.periodInFormat = moment(data.period, 'YYYY-MM').format('MMM, YYYY');
            $scope.monthYear = $scope.cutoffDate.period;
        });

        rootscopeService.gettingAllUser()
            .then(function (data) {
                self.repos = data;
            });

        $scope.downloadRocReport = function(format = "xlsx") {
            var url = 'service/report/downloadRocExcel?jobNumber=' + $scope.jobNo + '&year=' + $scope.year + '&month=' + $scope.month + "&format=" + format;
 		    GlobalHelper.downloadFile(url);
        }

        function getData(year, month) {
            var rocListApi = rocService.getRocListSummary($scope.jobNo, year, month);
            var tenderRisk = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.TenderRisks);
            var tenderOpps = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.TenderOpps);
            var others = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Contingency, GlobalParameter.forecast.Others);
            var risk = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Risks, GlobalParameter.forecast.Risks);
            var opps = forecastService.getByTypeDesc($scope.jobNo, year, month, GlobalParameter.forecast.Risks, GlobalParameter.forecast.Opps);

            $q.all([rocListApi, tenderRisk, tenderOpps, others, risk, opps])
                .then(function (data) {
                    $scope.data = {};
                    var rocList = data[0];
                    $scope.gridOptions.data = rocList;
                    $scope.displayClosedItem();
                    for (var i = 0; i < rocList.length; i++) {
                        $scope.gridOptions.data[i].person = { searchText: $scope.gridOptions.data[i].rocOwner };
                        $scope.gridOptions.data[i].group = $scope.gridOptions.data[i].rocCategory;
                        var curr = rocList[i].rocDetail;
                        if (curr != null) {
                            curr.movementBest = curr.amountBest - curr.previousAmountBest;
                            curr.movementExpected = curr.amountExpected - curr.previousAmountExpected;
                            curr.movementWorst = curr.amountWorst - curr.previousAmountWorst;
                        }
                    }
                    $scope.data.tenderRisk = data[1];
                    $scope.data.tenderOpps = data[2];
                    $scope.data.others = data[3];
                    $scope.data.risk = data[4];
                    $scope.data.opps = data[5];
                });
        }

        function isEditable() {
            return $scope.editable;
        }

        function editableStyle(grid, row, col, rowRenderIndex, colRenderIndex) {
            return $scope.editable ? 'blue' : '';
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

        // $scope.openAddRocDialog = function () {
        //     var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
        //     var dataRows = gridRows.map(function (gridRow) {
        //         return gridRow.entity;
        //     });
        //     var userInput = { 'year': $scope.year, 'month': $scope.month };
        //     if (dataRows.length > 0) {
        //         confirmService.show({}, {bodyText: GlobalMessage.leavingUpdatedGrid})
        //             .then(function (response) {
        //                 if (response === 'Yes')
        //                     modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'ADD', userInput);
        //             });
        //         return;
        //     }
        //     modalService.open('lg', 'view/job/modal/job-roc-add.html', 'JobRocAddCtrl', 'ADD', userInput);
        // }

        $scope.newGroup = "ӿ";
        $scope.isNewGroup = function (grid, row) {
            // return row.entity.group == grid.appScope.newGroup;
            return row.treeNode.aggregations[10].groupVal == grid.appScope.newGroup;
        }
        $scope.isAddingRow = false;
        $scope.addRoc = function () {
            if (!$scope.isAddingRow) {
                var newRow = {
                    // "createdUser": "michaelkll",
                    // "createdDate": "2021-09-14T07:40:59.000+0000",
                    // "lastModifiedUser": "michaelkll",
                    // "lastModifiedDate": "2021-09-14T10:24:20.000+0000",
                    // "systemStatus": "ACTIVE",
                    // "id": 1,
                    "group": $scope.newGroup,
                    "projectNo": $scope.jobNo,
                    "projectRef": null,
                    "rocCategory": null,
                    "classification": null,
                    "impact": null,
                    "description": null,
                    "status": "Live",
                    "rocOwner": null,
                    "openDate": new Date($scope.year, $scope.month-1),
                    "closedDate": null,
                    "rocDetail": {
                        // "createdUser": "michaelkll",
                        // "createdDate": "2021-09-14T08:13:58.000+0000",
                        // "lastModifiedUser": "michaelkll",
                        // "lastModifiedDate": "2021-09-14T10:24:20.000+0000",
                        // "systemStatus": "ACTIVE",
                        // "id": 3,
                        // "amountBest": 0,
                        // "amountExpected": 20,
                        // "amountWorst": 0,
                        // "previousAmountBest": 0,
                        // "previousAmountExpected": 20,
                        // "previousAmountWorst": 0,
                        "remarks": null,
                        "year": 2021,
                        "month": 9,
                        "status": "Live",
                        // "movementBest": 0,
                        // "movementExpected": 0,
                        // "movementWorst": 0
                    },
                    "assignedNo": null
                };
                newRow.updateType = 'ADD';
                $scope.gridOptions.data.splice(0, 0, newRow);
                $scope.isAddingRow = true;
                $timeout(function() {
                    $scope.gridApi.cellNav.scrollToFocus(
                        $scope.gridOptions.data[0],
                        $scope.gridOptions.columnDefs[3]
                    );
                }, 100);
            }
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
                gridRow.entity.rocOwner = gridRow.entity.person && gridRow.entity.person.selectedItem != null && gridRow.entity.person.searchText
                                        ? gridRow.entity.person.selectedItem.username
                                        : null;
                return gridRow.entity;
            });

            var changeList = [];
            for (var i=0; i<dataRows.length; ++i) {
                var item = angular.copy(dataRows[i]);
                switch(item.updateType) {
                    case 'ADD':
                        break;
                    case 'DELETE':
                        break;
                    case 'UPDATE':
                    default:
                        item.updateType = 'UPDATE';
                        break;
                }
                changeList.push(item);
            }
            rocService.saveRocDetails($scope.jobNo, changeList, $scope.year, $scope.month)
                .then(function (data) {
                    if (data.length != 0) {
                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                    } else {
                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ROC Details have been updated.");
                        $scope.gridDirtyRows = null;
                        $scope.gridApi.rowEdit.setRowsClean(dataRows);
                        getData($scope.year, $scope.month);
                        $scope.gridApi.core.refresh();
                        $scope.isAddingRow = false;
                        // $window.location.reload();
                    }
                });
        }

        $scope.displayClosedItem = function() {
            var statusColumnDefId = 6;
            if ($scope.showClosedItem) {
                $scope.gridOptions.columnDefs[statusColumnDefId].visible = true;
            } else {
                $scope.gridOptions.columnDefs[statusColumnDefId].visible = false;
            }
            $scope.gridApi.grid.refresh();
            $timeout(function () {
                if ($scope.showClosedItem) {
                    $scope.gridApi.grid.columns[statusColumnDefId + 1].filters[0].term = '';
                } else {
                    $scope.gridApi.grid.columns[statusColumnDefId + 1].filters[0].term = 'Live';
                }
                $scope.gridApi.grid.refresh();
            }, 100);

        }

        function initGrid() {
            $scope.gridOptions = {
                enableSorting: true,
                enableFiltering: true,
                enableColumnResizing: true,
                enableGridMenu: true,
                enableColumnMoving: true,
                enableCellEditOnFocus: true,
                allowCellFocus: false,
                // showGridFooter: false,
                showColumnFooter: false,
                treeRowHeaderAlwaysVisible: false,
                showTreeRowHeader: false,
                exporterMenuPdf: false,
                groupingNullLabel: '',
                rowEditWaitInterval: -1,
                enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.grid.registerDataChangeCallback(function () {
                        $scope.gridApi.treeBase.expandAllRows();
                    });
                    $scope.gridApi.edit.on.afterCellEdit($scope, function (rowEntity, colDef, newValue, oldValue) {
                        if (newValue != oldValue) {
                            switch (colDef.field) {
                                case 'rocDetail.status':
                                    if (rowEntity.rocDetail.status !== null)
                                        rowEntity.status = rowEntity.rocDetail.status;
                                    break;
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
                    {field: 'id', type: 'number', displayName: 'ROC Id', width: 100, visible: false, enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false},
                    {field: 'itemNo', type: 'number', displayName: 'Item', width: 50, visible: true, enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false
                        // sort: {priority: 1, direction: uiGridConstants.ASC}
                    },
                    { field: 'projectNo', width: 100, visible: false, enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false },
                    // {
                    //     field: 'rocCategory', displayName: "Group", width: 100,
                    //     enableCellEdit : false,
                    //     grouping: { groupPriority: 0 }, sort: { priority: 0, direction: 'asc' },
                    //     cellTemplate: '<b>{{row.entity.rocCategory}}</b>'
                    // },
                    {
                        field: 'rocCategory', displayName: "Category", width: 120, enableCellEdit: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable,
                        editableCellTemplate: 'ui-grid/dropdownEditor',
                        // grouping: {groupPriority: 0},
                        // sort: {priority: 0, direction: uiGridConstants.DESC},
                        treeAggregationType: uiGridGroupingConstants.aggregation.MAX,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        footerCellTemplate: '<div class="ui-grid-cell-contents" >&nbsp;</div>',
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="row.groupHeader && !grid.appScope.isNewGroup(grid, row) && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[10].groupVal) != -1">{{ COL_FIELD }}<br>(Below the Line)</div>' +
                            '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="row.groupHeader && !grid.appScope.isNewGroup(grid, row) && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[10].groupVal) == -1">{{ COL_FIELD }}<br>(Above the Line)</div>' +
                            '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="row.groupHeader && grid.appScope.isNewGroup(grid, row)">{{grid.appScope.newGroup}}</div>' +
                            '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="!row.groupHeader">{{ MODEL_COL_FIELD }}</div>'
                    },
                    {field: 'classification', width: 200, visible: true, enableCellEdit: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        editableCellTemplate: 'ui-grid/dropdownEditor',
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable,
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="row.groupHeader"></div>' +
                        '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="!row.groupHeader">{{ MODEL_COL_FIELD }}</div>' 
                    },
                    {field: 'impact', width: 120, visible: true, enableCellEdit: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        editableCellTemplate: 'ui-grid/dropdownEditor',
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable
                    },
                    {field: 'rocDetail.status', displayName: 'Status', width: 60, visible: true, enableCellEdit: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        // filter: {term: 'Live'},
                        editableCellTemplate: 'ui-grid/dropdownEditor',
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable
                    },
                    {field: 'projectRef', displayName: "Project R&O Ref.", width: 100, enableCellEdit: true, visible: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable
                    },
                    {field: 'description', displayName: "Description", width: 150, enableCellEdit: true, visible: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable,
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        editableCellTemplate: '<textarea class="roc-remarks-textarea" ui-grid-editor rows="1" cols="1" maxlength="4000" ng-model="MODEL_COL_FIELD" onfocus="textareaAutosize(event, this)" onkeydown="textareaAutosize(event, this)" onmousedown="textareaAutosize(event, this)" />'
                    },
                    {
                        field: 'rocOwner',
                        displayName: 'Owner',
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable,
                        width: 180, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        editableCellTemplate: '' +
                        '<md-autocomplete style="text-align:center" ng-blur="grid.appScope.autocompleteBlur(row)"' +
                            'md-require-match="true" ' +
                            'md-delay="300" ' +
                            'md-autoselect="true" ' +
                            'md-min-length="1" ' +
                            'md-clear-button="!row.entity.person.disabled" ' +
                            'ng-disabled="row.entity.person.disabled" ' +
                            'md-selected-item="row.entity.person.selectedItem" ' +
                            'md-search-text-change="grid.appScope.searchTextChange(row.entity.person.searchText, row.entity.person)" ' +
                            'md-search-text="row.entity.person.searchText" ' +
                            'md-selected-item-change="grid.appScope.selectedItemChange(item, row)" ' +
                            'md-items="item in grid.appScope.querySearch(row.entity.person.searchText)" ' +
                            'md-item-text="item.username" ' +
                            'md-min-length="3" ' +
                            'placeholder="Input Username..." ' +
                            'md-menu-class="autocomplete-custom-template" ' +
                            'md-menu-container-class="custom-container" ' +
                            '>' +
                            '<md-item-template>' +
                                '<span class="item-title">' +
                                    '<span><strong>{{item.fullName}}</strong></span>' +
                                '</span>' +
                                '<span class="item-metadata">' +
                                    '<span>' +
                                        '<small style="font-weight: 300 !important; position: relative; top: -10px">{{item.email}}</small>' +
                                    '</span>' +
                                '</span>' +
                            '</md-item-template>' +
                        '</md-autocomplete>'
                    },
                    {
                        field: 'rocDetail.amountBest',
                        displayName: "Best Case",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        cellTemplate: '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[10].groupVal) != -1">{{ COL_FIELD | number:0}}</div>' +
                            '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) != -1">{{ MODEL_COL_FIELD | number:0}}</div>' +
                            '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && row.treeNode.aggregations[10].groupVal && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[10].groupVal) == -1">N/A</div>' +
                            '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) == -1">N/A</div>',
                        footerCellClass: customFooterClass,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        enableCellEdit: false
                    },
                    {
                        field: 'rocDetail.amountExpected',
                        displayName: "Realistic",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        footerCellClass: customFooterClass,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        enableCellEdit: false
                    },
                    {
                        field: 'rocDetail.amountWorst',
                        displayName: "Worst Case",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        cellTemplate: '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[10].groupVal) != -1">{{ COL_FIELD | number:0}}</div>' +
                            '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) != -1">{{ MODEL_COL_FIELD | number:0}}</div>' +
                            '<div class="ui-grid-cell-contents" ng-if="row.groupHeader && row.treeNode.aggregations[10].groupVal && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[10].groupVal) == -1">N/A</div>' +
                            '<div class="ui-grid-cell-contents" ng-if="!row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.entity.rocCategory) == -1">N/A</div>',
                        footerCellClass: customFooterClass,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        enableCellEdit: false
                    },
                    {
                        field: 'rocDetail.previousAmountBest',
                        displayName: "Previous Best Case",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        cellClass: customCellClass,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: customFooterClass,
                        enableCellEdit: false,
                        visible: $scope.showPreviousAndMovementAmount
                    },
                    {
                        field: 'rocDetail.previousAmountExpected',
                        displayName: "Previous Realistic",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        cellClass: customCellClass,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: customFooterClass,
                        enableCellEdit: false,
                        visible: $scope.showPreviousAndMovementAmount
                    },
                    {
                        field: 'rocDetail.previousAmountWorst',
                        displayName: "Previous Worst Case",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        cellClass: customCellClass,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: customFooterClass,
                        enableCellEdit: false,
                        visible: $scope.showPreviousAndMovementAmount
                    },
                    {
                        field: 'rocDetail.movementBest',
                        displayName: "Movement Best Case",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: customFooterClass,
                        visible: $scope.showPreviousAndMovementAmount,
                        enableCellEdit: false
                    },
                    {
                        field: 'rocDetail.movementExpected',
                        displayName: "Movement Realistic",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: customFooterClass,
                        visible: $scope.showPreviousAndMovementAmount,
                        enableCellEdit: false
                    },
                    {
                        field: 'rocDetail.movementWorst',
                        displayName: "Movement Worst Case",
                        width: 100,
                        cellClass: 'text-right',
                        cellFilter: $scope.numberCellFilter,
                        treeAggregationType: uiGridGroupingConstants.aggregation.SUM, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        customTreeAggregationFinalizerFn: function (aggregation) {
                            aggregation.rendered = aggregation.value;
                        },
                        // footerCellTemplate: '<div class="ui-grid-cell-contents" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: customFooterClass,
                        visible: $scope.showPreviousAndMovementAmount,
                        enableCellEdit: false
                    },
                    {
                        field: 'rocDetail.remarks',
                        displayName: "Remarks / Actions to be taken",
                        width: 150,
                        headerCellClass: editableStyle, cellClass: editableStyle, cellEditableCondition: isEditable,
                        groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        editableCellTemplate: '<textarea class="roc-remarks-textarea" ui-grid-editor rows="1" cols="1" maxlength="4000" ng-model="MODEL_COL_FIELD" onfocus="textareaAutosize(event, this)" onkeydown="textareaAutosize(event, this)" onmousedown="textareaAutosize(event, this)" />'
                    },
                    {
                        name: 'Buttons', displayName: '',  width: 100, enableCellEdit: false, enableFiltering: false, allowCellFocus: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="col-md-12" style="padding: 5px 20px" ng-if="row.entity.id >= 0">' +
                            // '<div class="row" ng-if="!row.groupHeader"><button class="btn btn-sm icon-btn btn-success" ng-click="grid.appScope.openEditRocDialog(row.entity)"> <span class="fa fa-pencil" style="padding-left:10px;" ></span> Edit Owner</button></div>' +
                            '<div class="row m-t-5" ng-if="!row.groupHeader && (row.entity.createdDate != row.entity.lastModifiedDate) && (row.entity !== null && row.entity.id !== null && row.entity.id >= 0)"><button class="btn btn-sm icon-btn btn-info" ng-click="grid.appScope.viewRocHistory(row.entity)"> <span class="fa fa-history" style="padding-left:10px" ></span> History</button></div>' +
                            '<div class="row m-t-5" ng-if="!row.groupHeader"><button class="btn btn-sm icon-btn btn-warning" ng-click="grid.appScope.editRocSubdetail(row.entity)"> <span class="fa fa-pencil" style="padding-left:10px" ></span> Subdetail</button></div>' +
                            '</div>'
                    },
                    {field: 'rocDetail.year', displayName: 'Year', width: 100, visible: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false},
                    {field: 'rocDetail.month', displayName: 'Month', width: 100, visible: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false},
                    { field: 'rocDetail.id', displayName: 'ROC Detail Id', width: 100, visible: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false },
                    {
                        field: 'group',
                        displayName: "Group",
                        enableCellEdit: false,
                        visible: false,
                        grouping: {groupPriority: 0}, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        sort: { priority: 0, direction: uiGridConstants.DESC },
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].col.aggregationValue) != -1">{{ COL_FIELD }}<br>(Below the Line)' +
                        '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="row.groupHeader && [\'Risk\', \'Opps\'].indexOf(row.treeNode.aggregations[0].col.aggregationValue) == -1">{{ COL_FIELD }}<br>(Above the Line)' +
                        '<div class="ui-grid-cell-contents ui-grid-cell-contents-break" ng-if="!row.groupHeader">{{MODEL_COL_FIELD}}</div>'
                    },

                ]

            };
        }

        $scope.recalculateRoc = function() {
            var seconds = 30;
            $scope.buttonTimer = seconds*1000;
            $interval(function() {
                $scope.buttonTimer -= 1000;
            }, 1000, seconds);
            rocService.recalculateRoc($scope.jobNo).then(function(data) {
                if (data.length != 0) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                } else {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ROC has been recalculated.");
                }
            });
        }

        $scope.editRocSubdetail = function(entity) {
            modalService.open('xxlg', 'view/job/modal/job-roc-subdetail.html', 'JobRocSubdetailCtrl', 'UPDATE', { 'roc': entity, 'year': $scope.year, 'month': $scope.month, 'editable' : $scope.editable });
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

                $scope.editable = ($scope.monthYear == $scope.cutoffDate.period);

                $timeout(function() {
                    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
                }, 300)


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

        function initOptions() {
            var getRocClassDescMap = rocService.getRocClassDescMap();
            var getRocCategoryList = rocService.getRocCategoryList();
            var getImpactList = rocService.getImpactList();
            var getStatusList = rocService.getStatusList();

            $q.all([getRocClassDescMap, getRocCategoryList, getImpactList, getStatusList])
                .then(function(data) {
                    $scope.rocClassDescMap = data[0];
                    $scope.rocClassOptions = $scope.rocClassDescMap.map(function(x) { return x.classification; });

                    for(var i=0; i<$scope.gridOptions.columnDefs.length; ++i) {
                        var col = $scope.gridOptions.columnDefs[i];
                        if (col.field === 'impact')
                            col.editDropdownOptionsArray = strArrToObjArr(data[2]);
                        else if (col.field === 'classification')
                            col.editDropdownOptionsArray = strArrToObjArr($scope.rocClassOptions);
                        else if (col.field === 'rocDetail.status')
                            col.editDropdownOptionsArray = strArrToObjArr(data[3]);
                        else if (col.field === 'rocCategory')
                            col.editDropdownOptionsArray = strArrToObjArr(data[1]);
                    }
                });

        }

        function strArrToObjArr(strArray) {
            var result = [];
            for (var i=0; i < strArray.length; ++i) {
                var obj = {'value':strArray[i],'id':strArray[i]};
                result.push(obj);
            }
            return result;
        }

        /* for ROC Owner */
        var self = this;
        self.querySearch   = $scope.querySearch = querySearch;
        self.selectedItemChange = $scope.selectedItemChange = selectedItemChange;
        self.searchTextChange = $scope.searchTextChange = searchTextChange;
        self.createFilterFor = $scope.createFilterFor = createFilterFor;
        
        $scope.autocompleteBlur = function (row) {
            if (row.entity.person && row.entity.person.searchText) {
                row.entity.rocOwner = row.entity.person.selectedItem ? row.entity.person.selectedItem.username : null;
            } else {
                row.entity.rocOwner = null;
            }
            $timeout(function () {
                $scope.$broadcast("uiGridEventEndCellEdit");
            }, 100);
        }

        function querySearch(query) {
            var results = query ? self.repos.filter(self.createFilterFor(query)) : self.repos, deferred;
            deferred = $q.defer();
            $timeout(function() {
                deferred.resolve(results);
            }, 300, false);
            return deferred.promise;
        }

        function searchTextChange(text, person) {
            if (!text) $scope.rocOwner = null;
        }

        function selectedItemChange(item, row) {
            if (!item) $scope.rocOwner = null;
            if (item && item.username)
                $scope.rocOwner = item.username;
            if (row) {
                row.entity.rocOwner = item != null ? item.username : null;
                $scope.gridApi.rowEdit.setRowsDirty([row.entity]);
            }
        }
        
        function createFilterFor(query) {
            var lowercaseQuery = (query || "").toLowerCase();
            return function filterFn(item) {
                var fullNameRegExp = new RegExp(lowercaseQuery.split(' ').join('.*'));
                var fullNameReverseRegExp = new RegExp(lowercaseQuery.split(' ').reverse().join('.*'));
                return lowercaseQuery.length > 3 && (
                    (item.username && item.username.indexOf(lowercaseQuery) === 0) ||
                    (item.employeeId && item.employeeId.indexOf(lowercaseQuery) === 0) ||
                    (item.fullName && (fullNameRegExp.test(item.fullName.toLowerCase()) || fullNameReverseRegExp.test(item.fullName.toLowerCase()))) ||
                    (item.email && item.email.toLowerCase().indexOf(lowercaseQuery) === 0)
                );
            };
        }

    }]);
