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
                projectNo: $scope.jobNo,
                status: $scope.statusOptions[0]
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
            rocService.getRocClassDescMap().then(function(data) {
                $scope.rocClassDescMap = data;
                $scope.rocClassOptions = $scope.rocClassDescMap.map(function(x) { return x.classification; });
            })

            $scope.rocCatOptions = [
                "Tender Risk",
                "Tender Opps",
                "Contingency",
                "Risk",
                "Opps"
            ]

            $scope.impactOptions = [
                'Increased Cost',
                'Increased Turnover',
                'Reduced Cost',
                'Reduced Turnover'
            ]

            $scope.statusOptions = [
                'Live',
                'Closed'
            ]
        }
    }]);
