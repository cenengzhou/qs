mainApp.controller('AdminRevisionsRocCtrl',
    ['$scope', 'modalService',  'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'rocService', 'confirmService',  '$q', '$timeout',
        function ($scope,  modalService, rootscopeService, GlobalHelper, GlobalParameter, rocService, confirmService,  $q, $timeout) {
            $scope.GlobalParameter = GlobalParameter;

            $scope.RocSearch = {};

            $scope.person = {};

            initOptions();

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
                rowHeight: 50,
                columnDefs: [
                    {field: 'id', width: '120', displayName: "ID", enableCellEdit: false, visible: false},
                    {field: 'itemNo', width: '120', displayName: "Item No", enableCellEdit: false},
                    {field: 'projectRef', width: '120', displayName: "Project R&O Ref"},
                    {field: 'rocCategory', width: '120', displayName: "Category",
                        editableCellTemplate: 'ui-grid/dropdownEditor'},
                    {field: 'classification', width: '120', displayName: "Classification",
                        editableCellTemplate: 'ui-grid/dropdownEditor'},
                    {field: 'impact', width: '120', displayName: "Impact",
                        editableCellTemplate: 'ui-grid/dropdownEditor'},
                    {field: 'description', displayName: "Description"},
                    {field: 'rocOwner', width: '180', displayName: "ROC Owner",
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
                            '</md-autocomplete>'},
                    {field: 'openDate', width: '120', displayName: "Open Date",
                        type: 'date',
                        cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'
                    },
                    {field: 'closedDate', width: '120', displayName: "Closed Date",
                        type: 'date',
                        cellFilter: 'date:"' + GlobalParameter.DATE_FORMAT +'"'
                    },
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

            var self = this;

            rootscopeService.gettingAllUser()
                .then(function (data) {
                    self.repos = data;
                });

            rootscopeService.gettingWorkScopes()
                .then(function (response) {
                    $scope.allWorkScopes = response.workScopes;
                });

            $scope.onSubmitRocSearch = function () {
                var jobNo = $scope.RocSearch.jobNo;
                var itemNo = $scope.RocSearch.itemNo;
                if (GlobalHelper.checkNull([jobNo])) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and period!");
                } else {
                    rocService.getRocAdmin(jobNo, itemNo).then(
                        function (data) {
                            for (var i=0; i<data.length; i++) {
                                data[i].openDate = data[i].openDate ? new Date(data[i].openDate) : null;
                                data[i].closedDate = data[i].closedDate ? new Date(data[i].closedDate) : null;
                            }
                            $scope.gridOptions.data = data;
                        }, function (data) {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                        });
                }
            };

            $scope.updateGrid = function () {
                var gridRows = $scope.gridApi.rowEdit.getDirtyRows();
                var dataRows = gridRows.map(function (gridRow) {
                    return gridRow.entity;
                });
                if (dataRows.length > 0) {
                    rocService.updateRocAdmin($scope.RocSearch.jobNo, dataRows)
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
                            rocService.deleteRocListAdmin($scope.RocSearch.jobNo, selectedRows)
                                .then(function(data) {
                                    if(data.length>0){
                                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
                                    }else{
                                        $scope.gridApi.rowEdit.setRowsClean(selectedRows);
                                        if (data == '') {
                                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Roc deleted');
                                            $scope.gridApi.selection.clearSelectedRows();
                                            $scope.onSubmitRocSearch();
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
                            else if (col.field === 'status')
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

            /* ROC owner */
            $scope.querySearch = function(query) {
                var results = query ? self.repos.filter(createFilterFor(query)) : self.repos, deferred;
                deferred = $q.defer();
                $timeout(function() {
                    deferred.resolve(results);
                }, 300, false);
                return deferred.promise;
            }

            $scope.searchTextChange = function(text, person) {
                if (!text) $scope.rocOwner = null;
            }

            $scope.selectedItemChange = function(item, row) {
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
