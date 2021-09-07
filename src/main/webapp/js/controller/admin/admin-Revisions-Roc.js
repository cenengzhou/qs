mainApp.controller('AdminRevisionsRocCtrl',
    ['$scope', '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'rocService', 'confirmService', '$state',
        function ($scope, $http, modalService, blockUI, rootscopeService, GlobalHelper, GlobalParameter, rocService, confirmService, $state) {
            $scope.GlobalParameter = GlobalParameter;

            $scope.RocSearch = {};

            $scope.rocCategoryOptions = [];

            rocService.getRocCategoryList().then(function(data) {
                $scope.rocCategoryOptions = data;
                if ($scope.rocCategoryOptions && $scope.rocCategoryOptions.length > 0)
                    $scope.RocSearch.rocCategory = $scope.rocCategoryOptions[0];
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

        }]);
