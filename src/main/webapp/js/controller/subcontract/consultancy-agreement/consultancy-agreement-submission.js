mainApp.controller("ConsultancyAgreementSubmissionCtrl", ['$scope', 'GlobalHelper', 'consultancyAgreementService', 'jobService', 'subcontractService', '$cookies', '$timeout', 'modalService', 'subcontractRetentionTerms', '$state', 'GlobalParameter', 'paymentService', 'confirmService', 'rootscopeService',
    function ($scope, GlobalHelper, consultancyAgreementService, jobService, subcontractService, $cookies, $timeout, modalService, subcontractRetentionTerms, $state, GlobalParameter, paymentService, confirmService, rootscopeService) {
        $scope.GlobalParameter = GlobalParameter;
        $scope.disableButtons = false
        $scope.customPrint = GlobalHelper.customPrint;

        $scope.jobNo = $cookies.get('jobNo');
        $scope.subcontractNo = $cookies.get('subcontractNo');

        consultancyAgreementService.getFormSummary($scope.jobNo, $scope.subcontractNo).then(function (data) {
            $scope.form = data;
            if ($scope.form.statusApproval == 'SUBMITTED' || $scope.form.statusApproval == 'APPROVED') {
                $scope.disableButtons = true;
            }
        });

        subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo).then(function (subcontract) {
            if (subcontract.scStatus == '330' || subcontract.scStatus == '500') {
                $scope.disableButtons = true;
            }
        });

        // submit function
        $scope.submit = function () {
            consultancyAgreementService.submitCAApproval($scope.jobNo, $scope.subcontractNo).then(function (data) {
                if (data.length != 0) {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
                } else {
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Consultancy Agreement has been submitted.");
                    $state.reload();

                }
            });
        };
    }]);
