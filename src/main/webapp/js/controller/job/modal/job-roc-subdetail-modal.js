mainApp.controller('JobRocSubdetailCtrl', ['$scope', 'rocService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q', 'modalStatus', 'modalParam', '$timeout', 'uiGridConstants', '$rootScope',
    function ($scope, rocService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q, modalStatus, modalParam, $timeout, uiGridConstants, $rootScope) {
        $scope.GlobalParameter = GlobalParameter;

        $scope.jobNo = $cookies.get("jobNo");

        $scope.roc = modalParam.roc;
        $scope.year = parseInt(modalParam.year);
        $scope.month = parseInt(modalParam.month);

        initGrid();

        handleRiskAndOpps();

        getData();


        function handleRiskAndOpps() {
            if (!isRiskAndOpps()) {
                for (var i=0; i<$scope.gridOptions.columnDefs.length; i++) {
                    if (['amountBest', 'amountWorst'].indexOf($scope.gridOptions.columnDefs[i].field) != -1)
                        $scope.gridOptions.columnDefs.splice(i,1);
                }
            }
        }

        function isRiskAndOpps() {
            var rocCategory = $scope.roc.rocCategory;
            return ['Risk', 'Opps'].indexOf(rocCategory) != -1;
        }

        function getData() {
            rocService.getRocSubdetailList($scope.jobNo, $scope.roc.id, $scope.year, $scope.month).then(function (data) {
                if (data && data.length >= 0) {
                    $scope.gridOptions.data = data;
                }
                else
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', 'Failed to get roc subdetail history');
            });
        }

        $scope.addItem = function() {
            var newRow = { 'rocDetailId' : null, 'id' : null, 'assignedNo': null, 'description' : '', 'amountBest' : 0, 'amountExpected' : 0,'amountWorst' : 0, 'year' : null, 'month': null, 'remarks' : '', 'hyperlink' : ''};
            newRow.editable = true;
            newRow.updateType = 'ADD';
            if ($scope.year && $scope.month) {
                newRow.year = $scope.year;
                newRow.month = $scope.month;
            }

            $scope.gridOptions.data.splice(0,0,newRow);

            $timeout(function() {
                $scope.gridApi.cellNav.scrollToFocus(
                    $scope.gridOptions.data[0],
                    $scope.gridOptions.columnDefs[3]
                );
            });
        }

        $scope.deleteItem = function(rowRenderIndex) {
            var index = rowRenderIndex;
            var item = $scope.gridOptions.data[rowRenderIndex];
            switch(item.updateType) {
                case 'ADD':
                    if (item.assignedNo == null) {
                        $scope.gridApi.rowEdit.setRowsClean([item]);
                        $scope.gridOptions.data.splice(index, 1);
                    }
                    break;
                case 'DELETE':
                    break;
                case 'UPDATE':
                default:
                    if (item.assignedNo != null) {
                        item.amountBest = 0;
                        item.amountExpected = 0;
                        item.amountWorst = 0;
                        item.updateType = 'DELETE';
                        $scope.gridApi.rowEdit.setRowsDirty([item]);
                        $scope.gridApi.grid.refresh();
                    }
            }
        }

        $scope.saveAll = function() {
            var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
            var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });

            // data cleansing
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

            var rocId = $scope.roc.id;
            rocService.saveSubdetailList($scope.jobNo, rocId, changeList, $scope.year, $scope.month).then(function(data) {
                var isError;
                var result = parseInt(data);
                if (data.length == 0) {
                    isError = false;
                } else if (!isNaN(result)) {
                    $scope.roc.rocDetail.id = result;
                    isError = false;
                } else {
                    isError = true;
                }

                if (!isError) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ROC Subdetails have been updated.");
                    $scope.gridApi.rowEdit.setRowsClean(dataRows);
                    $rootScope.$broadcast('reloadRocList', {});
                    getData();
                } else {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                }
            });
        }

        function customRowTemplate() {
            return '<div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'text-black\' : !row.entity.editable }" role="{{col.isRowHeader ? \'rowheader\' : \'gridcell\'}}" ui-grid-cell></div>';
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
                showGridFooter: false,
                showColumnFooter: false,
                treeRowHeaderAlwaysVisible: false,
                enableRowHeaderSelection: false,
                exporterMenuPdf: false,
                rowEditWaitInterval: -1,
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                },

                isRowSelectable: function(row) {
                    return row.entity.editable && row.entity.updateType != 'DELETE';
                },

                cellEditableCondition: function($scope) {
                    var row = $scope.row;
                    return row.entity.editable && row.entity.updateType != 'DELETE';
                },

                rowTemplate: customRowTemplate(),

                columnDefs: [
                    {field: 'assignedNo', type: 'number', displayName: 'Item', width: 50, visible: true, enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP" ng-if="[\'ADD\',\'DELETE\'].indexOf(row.entity.updateType) == -1">{{COL_FIELD CUSTOM_FILTERS}}</div>' +
                            '<div class="ui-grid-cell-contents" title="TOOLTIP" ng-if="[\'ADD\',\'DELETE\'].indexOf(row.entity.updateType) != -1">({{row.entity.updateType}})</div>'
                    },
                    {field: 'id', displayName: 'Id', width: 100, visible: false, enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false},
                    {field: 'rocDetailId', displayName: 'Id', width: 100, visible: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false},
                    {field: 'description', displayName: 'Secondary Detail', width: 200, visible: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        headerCellClass: 'blue', cellClass: 'blue',
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        editableCellTemplate: '<textarea class="roc-remarks-textarea" ui-grid-editor rows="1" cols="1" maxlength="4000" ng-model="MODEL_COL_FIELD" onfocus="textareaAutosize(event, this)" onkeydown="textareaAutosize(event, this)" onmousedown="textareaAutosize(event, this)" />'
                    },
                    {
                        field: 'amountBest',
                        displayName: "Best Case",
                        width: 120,
                        headerCellClass: 'blue',
                        cellClass: 'text-right blue',
                        cellFilter: 'number:0',
                        aggregationType: uiGridConstants.aggregationTypes.sum, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: 'text-right'
                    },
                    {
                        field: 'amountExpected',
                        displayName: "Realistic",
                        width: 120,
                        headerCellClass: 'blue',
                        cellClass: 'text-right blue',
                        cellFilter: 'number:0',
                        aggregationType: uiGridConstants.aggregationTypes.sum, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: 'text-right'
                    },
                    {
                        field: 'amountWorst',
                        displayName: "Worst Case",
                        width: 120,
                        headerCellClass: 'blue',
                        cellClass: 'text-right blue',
                        cellFilter: 'number:0',
                        aggregationType: uiGridConstants.aggregationTypes.sum, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        // footerCellTemplate: '<div class="ui-grid-cell-contents blue" >{{col.getAggregationValue() | number:0 }}</div>',
                        footerCellClass: 'text-right'
                    },
                    {field: 'hyperlink', displayName: 'Attachment', visible: true, width: 120, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        headerCellClass: 'blue', cellClass: 'blue',
                        cellTemplate: '<div class="ui-grid-cell-contents"><a ng-if="row.entity.hyperlink" ng-href="{{row.entity.hyperlink}}" target="_blank">(View attachment)</a></div>'
                    },
                    {field: 'remarks', displayName: 'Remarks', visible: true, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        headerCellClass: 'blue', cellClass: 'blue',
                        cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',
                        editableCellTemplate: '<textarea class="roc-remarks-textarea" ui-grid-editor rows="1" cols="1" maxlength="4000" ng-model="MODEL_COL_FIELD" onfocus="textareaAutosize(event, this)" onkeydown="textareaAutosize(event, this)" onmousedown="textareaAutosize(event, this)" />'
                    },
                    {
                        field: 'year',
                        displayName: 'Year',
                        width: 100,
                        visible: true,
                        enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP" ng-if="[\'ADD\'].indexOf(row.entity.updateType) == -1">{{COL_FIELD CUSTOM_FILTERS}}</div>' +
                            '<div class="ui-grid-cell-contents" title="TOOLTIP" ng-if="[\'ADD\'].indexOf(row.entity.updateType) != -1">{{row.entity.year}}</div>'
                    },
                    {
                        field: 'month',
                        displayName: 'Month',
                        width: 100,
                        visible: true,
                        enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP" ng-if="[\'ADD\'].indexOf(row.entity.updateType) == -1">{{COL_FIELD CUSTOM_FILTERS}}</div>' +
                            '<div class="ui-grid-cell-contents" title="TOOLTIP" ng-if="[\'ADD\'].indexOf(row.entity.updateType) != -1">{{row.entity.month}}</div>'
                    },
                    {field: 'lastModifiedUser', displayName: 'Last Modified By', width: 150, visible: true, enableCellEdit: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false},
                    {
                        name: 'Buttons', displayName: '',  width: 50, enableCellEdit: false, enableFiltering: false, allowCellFocus: false, groupingShowAggregationMenu: false, groupingShowGroupingMenu: false,
                        cellTemplate: '<div class="col-md-12" style="padding: 5px 20px">' +
                            '<div class="row"><button class="btn btn-sm icon-btn btn-success" ng-if="row.entity.editable" ng-click="grid.appScope.deleteItem(rowRenderIndex)"> <span class="fa fa-remove" style="padding-left:10px;" ></span></button></div>' +
                            '</div>'
                    }
                ]

            };
        }


        $scope.cancel = function () {
            $uibModalInstance.dismiss("cancel");
        };

    }]);
