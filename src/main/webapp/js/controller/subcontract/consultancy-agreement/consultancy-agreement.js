mainApp.controller("ConsultancyAgreementCtrl", ['$scope', 'consultancyAgreementService', '$mdConstant', 'jobService', 'subcontractService', '$cookies', '$timeout', 'modalService', 'subcontractRetentionTerms', '$state', 'GlobalParameter', 'paymentService', 'confirmService', 'rootscopeService',
    function ($scope, consultancyAgreementService, $mdConstant, jobService, subcontractService, $cookies, $timeout, modalService, subcontractRetentionTerms, $state, GlobalParameter, paymentService, confirmService, rootscopeService) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.disableButtons = false

        // set job no and subcontract no from cookie
        $scope.jobNo = $cookies.get('jobNo');
        $scope.subcontractNo = $cookies.get('subcontractNo');

        // initialize fields for multi select dropdowns
        var self = this;
        self.fromList = [];
        self.toList = [];
        self.ccList = [];
        self.querySearch = querySearch;
        this.customKeys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA, 186 /*semicolon*/];

        loadData();

        // helper functions for "from" and "to" input field
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

        function loadData() {
            // load subcontract
            subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo).then(function (subcontract) {
                if (subcontract.scStatus == '330' || subcontract.scStatus == '500') {
                    $scope.disableButtons = true;
                }
            });

            // initialize memo
            consultancyAgreementService.getMemo($scope.jobNo, $scope.subcontractNo).then(function (response) {
                $scope.memo = response;

                if ($scope.memo.statusApproval == 'SUBMITTED' || $scope.memo.statusApproval == 'APPROVED') {
                    $scope.disableButtons = true;
                }

                // initialize user list
                rootscopeService.gettingAllUser()
                    .then(function (userList) {
                        if (userList.length > 0) {
                            userList.forEach(function (user) {
                                user.name = user.fullName ? user.fullName : '';
                                user.email = user.email ? user.email : '';
                                // user.image = GlobalParameter.imageServerAddress + user.StaffID + '.jpg'
                                user._lowername = user.name ? user.name.toLowerCase() : '';
                                user._loweremail = user.email ? user.email.toLowerCase() : '';

                                if ($scope.memo.fromList) {
                                    var fromListArray = $scope.memo.fromList.split(';');
                                    if (fromListArray.indexOf(user.username) != -1) {
                                        self.fromList.push(user);
                                    }
                                }
                                if ($scope.memo.toList) {
                                    var toListArray = $scope.memo.toList.split(';');
                                    if (toListArray.indexOf(user.username) != -1) {
                                        self.toList.push(user);
                                    }
                                }
                                if ($scope.memo.ccList) {
                                    var ccListArray = $scope.memo.ccList.split(';');
                                    if (ccListArray.indexOf(user.username) != -1) {
                                        self.ccList.push(user);
                                    }
                                }


                            });
                        }
                        self.allContacts = userList;
                    });
            });

        }


        // save function
        $scope.save = function () {

            $scope.memo.fromList = self.fromList.map(u => u.username).join(';');
            $scope.memo.toList = self.toList.map(u => u.username).join(';');
            $scope.memo.ccList = self.ccList.map(u => u.username).join(';');

            if (false === $('form[name="formValidate"]').parsley().validate()) {
                event.preventDefault();
                return;
            }

            consultancyAgreementService.saveMemo($scope.jobNo, $scope.subcontractNo, $scope.memo).then(function (data) {
                if (data.length == 0) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Memo updated.");
                    $state.reload();
                } else {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                }
            });
        };


    }]);
