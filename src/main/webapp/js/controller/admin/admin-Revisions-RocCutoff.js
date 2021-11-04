mainApp.controller('AdminRevisionsRocCutoffCtrl',
    ['$scope', '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'rocService', 'confirmService', '$state', '$q', '$timeout',
        function ($scope, $http, modalService, blockUI, rootscopeService, GlobalHelper, GlobalParameter, rocService, confirmService, $state, $q, $timeout) {
            $scope.GlobalParameter = GlobalParameter;

            rocService.getCutoffPeriod().then(function(data) {
                $scope.RocCutoffRecord = data;
            });

            rootscopeService.gettingWorkScopes()
                .then(function (response) {
                    $scope.allWorkScopes = response.workScopes;
                });

            $scope.onSubmitRocCutoffRecord = function () {
                    rocService.updateRocCutoffAdmin($scope.RocCutoffRecord)
                        .then(
                            function (data) {
                                if (data.length != 0)
                                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                                else
                                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "RocCutoff updated.");
                            }, function (message) {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', message);
                            });
            };

        }]);
