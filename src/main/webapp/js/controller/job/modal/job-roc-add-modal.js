mainApp.controller('JobRocAddCtrl', ['$scope', 'rocService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q', 'modalStatus', 'modalParam', '$rootScope', '$timeout', 'rootscopeService',
    function ($scope, rocService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q, modalStatus, modalParam, $rootScope, $timeout, rootscopeService) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.editable = false;

        $scope.jobNo = $cookies.get("jobNo");

        var self = this;

        $scope.person = {};

        rootscopeService.gettingAllUser()
            .then(function (data) {
                self.repos = data;
                var findUser = self.repos.filter(function(x) { return x.username == $scope.roc.rocOwner});
                if (findUser.length > 0 && findUser[0])
                    $scope.person.selectedItem = findUser[0];
            });

        initOptions();

        $scope.querySearch = function(query) {
            var results = query ? self.repos.filter(createFilterFor(query)) : self.repos, deferred;
            deferred = $q.defer();
            $timeout(function() {
                deferred.resolve(results);
            }, 300, false);
            return deferred.promise;
        }

        $scope.searchTextChange = function(text, person) {
            if (!text) $scope.roc.rocOwner = null;
        }

        $scope.selectedItemChange = function(item, person) {
            if (!item) $scope.roc.rocOwner = null;
            if (item && item.username)
                $scope.roc.rocOwner = item.username;
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

        $scope.displayMode = modalStatus;

        if ($scope.displayMode === 'UPDATE') {
            $scope.roc = angular.copy(modalParam);
        } else {
            $scope.roc = {
                projectNo: $scope.jobNo
            }
        }


        $scope.addRoc = function () {
            if (false === $('form[name="form-validate"]').parsley().validate()) {
                event.preventDefault();
                return;
            }

            rocService.addRoc($scope.jobNo, $scope.roc)
                .then(function (data) {
                    if (data.length !== 0) {
                        Error(data);
                    } else {
                        Success();
                    }
                });
        }

        $scope.updateRoc = function () {
            if (false === $('form[name="form-validate"]').parsley().validate()) {
                event.preventDefault();
                return;
            }
            $scope.editable = false;
            rocService.updateRoc($scope.jobNo, $scope.roc)
                .then(
                    function (data) {
                        if (data.length !== 0) {
                            Error(data);
                        } else {
                            Success();
                        }
                    }
                );

        }

        $scope.cancel = function () {
            $uibModalInstance.dismiss("cancel");
        };

        $scope.onRocClassSelected = function(rocClass) {
            var findObj = $scope.rocClassDescMap.find(function(x){ return x.classification == rocClass });
            if (findObj) {
                $scope.rocClassDescription = findObj.description;
            }
        }

        function Success() {
            $scope.cancel();
            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");

            if ($scope.displayMode === 'UPDATE')
                $rootScope.$broadcast('reloadRocList', {});
            else
                $state.reload();
        }

        function Error(msg) {
            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', msg)
                .closed.then(function () {
                $state.reload();
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

                    $scope.rocCatOptions = data[1];
                    $scope.impactOptions = data[2];
                    $scope.statusOptions = data[3];

                    if (!$scope.roc.status)
                        $scope.roc.status = $scope.statusOptions[0];
                });

        }
    }]);
