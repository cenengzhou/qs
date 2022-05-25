mainApp.service('consultancyAgreementService', ['$http', '$q', 'GlobalHelper', function ($http, $q, GlobalHelper) {
    // Return public API.
    return ({
        getMemo: getMemo,
        saveMemo: saveMemo,
        getFormSummary: getFormSummary,
        submitCAApproval: submitCAApproval,
        updateConsultancyAgreementAdmin: updateConsultancyAgreementAdmin
    });

    function getMemo(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/consultancyAgreement/getMemo",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            }
        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

    function getFormSummary(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/consultancyAgreement/getFormSummary",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            }
        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

    function saveMemo(jobNo, subcontractNo, memo) {
        var request = $http({
            method: "post",
            url: "service/consultancyAgreement/saveMemo",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            },
            data: memo
        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

    function submitCAApproval(jobNo, subcontractNo) {
        var request = $http({
            method: "post",
            url: "service/consultancyAgreement/submitCAApproval",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            }
        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

    function updateConsultancyAgreementAdmin(jobNo, subcontractNo, ca) {
        var request = $http({
            method: "post",
            url: "service/consultancyAgreement/updateConsultancyAgreementAdmin",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            },
            data: ca
        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

}]);




