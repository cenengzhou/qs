mainApp.controller('AddendumForm2v2Ctrl', ['$scope', 'modalService', 'addendumService', 'subcontractService', '$stateParams', '$cookies', '$state', 'htmlService', 'GlobalHelper', 'confirmService', 'jobService',
    function ($scope, modalService, addendumService, subcontractService, $stateParams, $cookies, $state, htmlService, GlobalHelper, confirmService, jobService) {

        $scope.addendumNo = $cookies.get('addendumNo');
        var addendumDetailHeaderRef = $cookies.get('addendumDetailHeaderRef');

        loadData();

        $scope.submit = function () {
            $scope.disableButtons = true;

            var zeroSCRateItemExist = false;

            for (var i in $scope.addendumDetails) {
                if ($scope.addendumDetails[i].rateBudget > 0 && $scope.addendumDetails[i].rateAddendum == 0) {
                    zeroSCRateItemExist = true;
                    break;
                }
            }

            if (zeroSCRateItemExist) {
                var modalOptions = {
                    bodyText: 'No work done is allowed with ZERO subcontract rate for budgeted item. Continue?'
                };
                confirmService.showModal({}, modalOptions).then(function (result) {
                    if (result == "Yes") {
                        submitAddendumApproval();
                    } else
                        $scope.disableButtons = false;
                });
            } else
                submitAddendumApproval();
        }

        function loadData() {
            getCompanyName();
            if ($scope.addendumNo != null && $scope.addendumNo.length != 0) {
                getSubcontract();
                // makeHTMLStringForAddendumApproval();
                getForm2Summary();
            } else {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Addendum does not exist. Please create addendum title first.");
            }
        }

        function getCompanyName() {
            jobService.getCompanyName($scope.jobNo)
                .then(
                    function (data) {
                        $scope.companyName = data;
                    });
        }

        function getAddendum() {
            addendumService.getAddendum($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
                .then(
                    function (data) {
                        $scope.addendum = data;
                        if ($scope.addendum.length == 0 || $scope.addendum.status == "PENDING")
                            $scope.disableButtons = false;
                        else
                            $scope.disableButtons = true;
                    });
        }

        function getSubcontract() {
            subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
                .then(
                    function (data) {
                        $scope.subcontract = data;
                        if (data.scStatus < 500 || data.paymentStatus == 'F' || data.splitTerminateStatus == 1 || data.splitTerminateStatus == 2 || data.splitTerminateStatus == 4 || data.submittedAddendum == 1)
                            $scope.disableButtons = true;
                        else
                            $scope.disableButtons = false;

                        getAddendum();
                    });
        }

        function getAllAddendumDetails() {
            addendumService.getAllAddendumDetails($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
                .then(
                    function (data) {
                        $scope.addendumDetails = data;
                    });

        }

        function makeHTMLStringForAddendumApproval() {
            htmlService.makeHTMLStringForAddendumApproval({
                jobNumber: $scope.jobNo,
                packageNo: $scope.subcontractNo,
                addendumNo: $scope.addendumNo,
                htmlVersion: 'W'
            })
                .then(
                    function (data) {
                        $scope.form2Html = GlobalHelper.formTemplate(data);
                    });
        }

        function getForm2Summary(){
            addendumService.getForm2Summary($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
                .then(data => {
                    $scope.summary = data
                });
        }

        function submitAddendumApproval() {
            addendumService.submitAddendumApproval($scope.jobNo, $scope.subcontractNo, $scope.addendumNo)
                .then(
                    function (data) {
                        if (data.length != 0) {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                            $scope.disableButtons = false;
                        } else {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Addendum has been submitted for approval.");
                            $state.reload();
                        }
                    });
        }

    }]);