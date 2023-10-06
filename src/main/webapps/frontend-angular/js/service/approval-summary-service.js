mainApp.service('approvalSummaryService', ['$http', '$q', '$log', 'GlobalHelper', function ($http, $q, $log, GlobalHelper) {
    // Return public API.
    return ({
        obtainApprovalSummary: obtainApprovalSummary,
        updateApprovalSummary: updateApprovalSummary
    });

    function obtainApprovalSummary(nameObject, textKey) {
        var request = $http({
            method: "get",
            url: "service/approvalSummary/obtainApprovalSummary",
            dataType: "application/json;charset=UTF-8",
            params: {
                nameObject: nameObject,
                textKey: textKey
            }

        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

    function updateApprovalSummary(jobNo, nameObject, textKey, approvalSummary) {
        var request = $http({
            method: "post",
            url: "service/approvalSummary/updateApprovalSummary",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                nameObject: nameObject,
                textKey: textKey
            },
            data: approvalSummary
        });
        return (request.then(GlobalHelper.handleSuccess, GlobalHelper.handleError));
    }

}]);
