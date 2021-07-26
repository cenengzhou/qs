mainApp.controller('AdminRevisionsTenderCtrl',
    ['$scope', '$http', 'modalService', 'blockUI', 'rootscopeService', 'GlobalHelper', 'GlobalParameter', 'tenderService',
        function ($scope, $http, modalService, blockUI, rootscopeService, GlobalHelper, GlobalParameter, tenderService) {
            $scope.GlobalParameter = GlobalParameter;

            $scope.currencyCode = {
                options: ["HKD",
                    "USD",
                    "INR",
                    "GBP",
                    "EUR",
                    "AUD",
                    "THB",
                    "TWD",
                    "PHP",
                    "JPY",
                    "SGD",
                    "CAD",
                    "CNY",
                    "MOP"
                ]
            };

            rootscopeService.gettingWorkScopes()
                .then(function (response) {
                    $scope.allWorkScopes = response.workScopes;
                });

            $scope.onSubmitTenderSearch = function () {
                var jobNo = $scope.TenderSearch.jobNo;
                var packageNo = $scope.TenderSearch.packageNo;
                var vendorNo = $scope.TenderSearch.vendorNo;
                cleanupTenderRecord();
                if (GlobalHelper.checkNull([jobNo, packageNo, vendorNo])) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number, subcontract number and vendor number!");
                } else {
                    tenderService.getTender(jobNo, packageNo, vendorNo).then(
                        function (data) {
                            if (data instanceof Object) {
                                if (data.workscope)
                                    data.workscope = '' + data.workscope;
                                $scope.TenderRecord = data;
                            } else {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Tender not found");
                            }
                        }, function (data) {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                        });
                }
            };

            $scope.onSubmitTenderRecord = function () {
                if ($scope.RevisionsTenderRecord.$invalid) {
                    return
                }

                if ($scope.TenderRecord.jobNo !== undefined) {
                    tenderService.updateTenderAdmin($scope.TenderRecord)
                        .then(
                            function (data) {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Tender updated.");
                            }, function (message) {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', message);
                            });
                } else {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search tender first!");
                }
                cleanupTenderRecord();
            };

            function cleanupTenderRecord() {
                $scope.RevisionsTenderRecord.$setPristine();
            }

        }]);
