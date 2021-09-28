mainApp.controller('AdminRevisionsRocCtrl',
    ['$scope', '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'rocService', 'confirmService', '$state', '$q', '$timeout',
        function ($scope, $http, modalService, blockUI, rootscopeService, GlobalHelper, GlobalParameter, rocService, confirmService, $state, $q, $timeout) {
            $scope.GlobalParameter = GlobalParameter;

            $scope.RocSearch = {};

            $scope.rocCategoryOptions = [];

            $scope.person = {};

            initOptions();

            var self = this;

            rocService.getRocCategoryList().then(function(data) {
                $scope.rocCategoryOptions = data;
                if ($scope.rocCategoryOptions && $scope.rocCategoryOptions.length > 0)
                    $scope.RocSearch.rocCategory = $scope.rocCategoryOptions[0];
            });

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
                var rocCategory = $scope.RocSearch.rocCategory;
                var description = $scope.RocSearch.description;
                cleanupRocRecord();
                if (GlobalHelper.checkNull([jobNo, rocCategory, description])) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number, roc category and description!");
                } else {
                    rocService.getRocAdmin(jobNo, rocCategory, description).then(
                        function (data) {
                            if (data instanceof Object) {
                                if (data.workscope) {
                                    data.workscope = '' + data.workscope;
                                }
                                $scope.RocRecord = data;
                            } else {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Roc not found");
                            }

                            $scope.person.searchText = $scope.RocRecord.rocOwner;
                        }, function (data) {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                        });
                }
            };

            $scope.onSubmitRocRecord = function () {
                if ($scope.RevisionsRocRecord.$invalid) {
                    return;
                }
                if ($scope.RocRecord.projectNo !== undefined) {
                    rocService.updateRocAdmin($scope.RocSearch.jobNo, $scope.RocRecord)
                        .then(
                            function (data) {
                                if (data.length != 0)
                                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                                else
                                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Roc updated.");
                            }, function (message) {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', message);
                            });
                } else {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search roc first!");
                }
                cleanupRocRecord();
            };

            $scope.deleteRoc = function () {
                var roc = $scope.RocRecord;
                confirmService.show({}, {bodyText: 'ROC will be removed, are you sure to proceed?'})
                    .then(function (response) {
                        if (response === 'Yes') {
                            rocService.deleteRocAdmin($scope.RocSearch.jobNo, roc)
                                .then(function(data) {
                                    if(data.length>0){
                                        modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', data);
                                    }else{
                                        if (data == '') {
                                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', 'Roc deleted');
                                            $state.reload();
                                        }
                                    }
                                });
                        }
                    });
            }

            function cleanupRocRecord() {
                $scope.RevisionsRocRecord.$setPristine();
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

                        $scope.impactOptions = data[2];
                        $scope.classificationOptions = $scope.rocClassOptions;
                        $scope.statusOptions = data[3];
                        $scope.rocCategoryOptions = data[1];


                    });

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

            $scope.selectedItemChange = function(item, person) {
                if (!item) $scope.rocOwner = null;
                if (item && item.username)
                    $scope.RocRecord.rocOwner = item.username;
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
