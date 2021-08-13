mainApp.controller('JobRocAddCtrl', ['$scope', 'rocService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q', 'modalStatus', 'modalParam',
    function ($scope, rocService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q, modalStatus, modalParam) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.editable = false;

        $scope.jobNo = $cookies.get("jobNo");

        initOptions();

        $scope.displayMode = modalStatus;

        if ($scope.displayMode == 'UPDATE') {
            $scope.roc = modalParam;
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
                    if (data.length != 0) {
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
                        if (data.length != 0) {
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

                    $scope.roc.status = $scope.statusOptions[0];
                });

        }
    }]);
