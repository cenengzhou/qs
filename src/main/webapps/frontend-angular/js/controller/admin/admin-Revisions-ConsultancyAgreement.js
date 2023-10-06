mainApp.controller('AdminRevisionsConsultancyAgreementCtrl',
    ['$scope', '$mdConstant', '$http', 'modalService', 'GlobalParameter', 'blockUI', 'GlobalHelper', 'consultancyAgreementService', 'rootscopeService',
        function ($scope, $mdConstant, $http, modalService, GlobalParameter, blockUI, GlobalHelper, consultancyAgreementService, rootscopeService) {

            $scope.GlobalParameter = GlobalParameter;
            $scope.ConsultancyAgreementSearch = {};

            // load data
            rootscopeService.gettingAllUser()
                .then(function (userList) {
                    if (userList.length > 0) {
                        userList.forEach(function (user) {
                            user.name = user.fullName ? user.fullName : '';
                            user.email = user.email ? user.email : '';
                            // user.image = GlobalParameter.imageServerAddress + user.StaffID + '.jpg'
                            user._lowername = user.name ? user.name.toLowerCase() : '';
                            user._loweremail = user.email ? user.email.toLowerCase() : '';
                        });
                    }
                    self.allContacts = userList;
                });

            // multiple users
            var self = this;
            self.fromList = [];
            self.toList = [];
            self.ccList = [];
            self.querySearch = querySearch;
            this.customKeys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA, 186 /*semicolon*/];

            function querySearch(criteria) {
                return self.allContacts.filter(createFilterFor(criteria));
            }

            function createFilterFor(query) {
                var lowercaseQuery = angular.lowercase(query);

                return function filterFn(contact) {
                    var fullNameRegExp = new RegExp(lowercaseQuery.split(' ').join('.*'));
                    var fullNameReverseRegExp = new RegExp(lowercaseQuery.split(' ').reverse().join('.*'));
                    return lowercaseQuery.length > 3
                        && (
                            (fullNameRegExp.test(contact._lowername.toLowerCase()) || fullNameReverseRegExp.test(contact._lowername.toLowerCase()))
                            || contact._loweremail.indexOf(lowercaseQuery) != -1
                        );
                    ;
                };
            }

            // search
            $scope.onSubmitConsultancyAgreementSearch = function () {
                var jobNo = $scope.ConsultancyAgreementSearch.jobNo;
                var subcontractNo = $scope.ConsultancyAgreementSearch.subcontractNo;
                cleanupConsultancyAgreementRecord();
                if (GlobalHelper.checkNull([jobNo, subcontractNo])) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please enter job number and subcontract number!");
                } else {
                    consultancyAgreementService.getMemo(jobNo, subcontractNo)
                        .then(function (data) {
                            if (data instanceof Object) {
                                $scope.ConsultancyAgreementRecord = data;
                                self.fromList = []
                                self.toList = []
                                self.ccList = []

                                self.allContacts.forEach(function (user) {

                                    if ($scope.ConsultancyAgreementRecord.fromList) {
                                        var fromListArray = $scope.ConsultancyAgreementRecord.fromList.split(';');
                                        if (fromListArray.indexOf(user.username) != -1) {
                                            self.fromList.push(user);
                                        }
                                    }
                                    if ($scope.ConsultancyAgreementRecord.toList) {
                                        var toListArray = $scope.ConsultancyAgreementRecord.toList.split(';');
                                        if (toListArray.indexOf(user.username) != -1) {
                                            self.toList.push(user);
                                        }
                                    }
                                    if ($scope.ConsultancyAgreementRecord.ccList) {
                                        var ccListArray = $scope.ConsultancyAgreementRecord.ccList.split(';');
                                        if (ccListArray.indexOf(user.username) != -1) {
                                            self.ccList.push(user);
                                        }
                                    }
                                });
                            } else {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "ConsultancyAgreement not found!");
                            }
                        }, function (data) {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', "Please search consultancyAgreement first!");
                        });
                }
            };

            $scope.onSubmitConsultancyAgreementRecord = function () {
                if ($scope.RevisionsConsultancyAgreementRecord.$invalid) {
                    return
                }
                if ($scope.ConsultancyAgreementRecord.id !== undefined) {
                    $scope.ConsultancyAgreementRecord.fromList = self.fromList.map(u => u.username).join(';');
                    $scope.ConsultancyAgreementRecord.toList = self.toList.map(u => u.username).join(';');
                    $scope.ConsultancyAgreementRecord.ccList = self.ccList.map(u => u.username).join(';');

                    consultancyAgreementService.updateConsultancyAgreementAdmin($scope.ConsultancyAgreementSearch.jobNo, $scope.ConsultancyAgreementSearch.subcontractNo, $scope.ConsultancyAgreementRecord)
                        .then(function (data) {
                            if (data === '') {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "ConsultancyAgreement updated.");
                            } else {
                                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                            }
                        }, function (data) {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                        });
                } else {
                    $scope.blockConsultancyAgreement.stop();
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please search consultancyAgreement first!");
                }
                cleanupConsultancyAgreementRecord();
            };

            function cleanupConsultancyAgreementRecord() {
                $scope.RevisionsConsultancyAgreementRecord.$setPristine();
//		$scope.ConsultancyAgreementRecord = {};
            }

            // date picker
            angular.element('input[name$=".singleDate"').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                autoApply: true,
                locale: {
                    format: GlobalParameter.MOMENT_DATE_FORMAT
                },

            })
            $scope.openDropdown = function( $event){
                angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
            }

        }]);
