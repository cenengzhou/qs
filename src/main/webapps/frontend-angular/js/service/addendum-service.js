mainApp.service('addendumService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getLatestAddendum: 					getLatestAddendum,
    	getAddendum:						getAddendum,
    	getAddendumList:					getAddendumList,
    	getTotalApprovedAddendumAmount: 	getTotalApprovedAddendumAmount,
    	getAddendumDetailHeader:			getAddendumDetailHeader,
    	getAddendumDetailsByHeaderRef:		getAddendumDetailsByHeaderRef,
    	getAllAddendumDetails:				getAllAddendumDetails,
    	getForm2Summary:		            getForm2Summary,
        getAddendumFinalForm:				getAddendumFinalForm,
        getAddendumDetailsWithoutHeaderRef:	getAddendumDetailsWithoutHeaderRef,
    	getDefaultValuesForAddendumDetails:	getDefaultValuesForAddendumDetails,
    	createAddendum:						createAddendum,
    	updateAddendum:						updateAddendum,
        updateAddendumAdmin:                updateAddendumAdmin,
    	updateAddendumDetailHeader:			updateAddendumDetailHeader,
    	deleteAddendumDetailHeader:			deleteAddendumDetailHeader,
    	addAddendumDetail:					addAddendumDetail,
    	addAddendumFromResourceSummaries: 	addAddendumFromResourceSummaries,
        addAddendumFromResourceSummaryAndAddendumDetail:    addAddendumFromResourceSummaryAndAddendumDetail,
    	updateAddendumDetail:				updateAddendumDetail,
    	updateAddendumDetailList:			updateAddendumDetailList,
        updateAddendumDetailListAdmin:		updateAddendumDetailListAdmin,
    	deleteAddendumDetail:				deleteAddendumDetail,
    	deleteAddendumFromSCDetails:		deleteAddendumFromSCDetails,
    	submitAddendumApproval:				submitAddendumApproval,
        enquireAddendumList:			enquireAddendumList
    });

    function enquireAddendumList(jobNo, commonKeyValue) {
        var request = $http({
            method: "post",
            url: "service/addendum/enquireAddendumList",
            params: {
                jobNo: jobNo
            },
            data: commonKeyValue
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
	
    function getLatestAddendum(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getLatestAddendum",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    

    function getAddendum(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendum",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAddendumList(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumList",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getTotalApprovedAddendumAmount(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getTotalApprovedAddendumAmount",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAddendumDetailHeader(addendumDetailHeaderRef) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumDetailHeader",
            dataType: "application/json;charset=UTF-8",
            params: {
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAddendumDetailsByHeaderRef(addendumDetailHeaderRef) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumDetailsByHeaderRef",
            dataType: "application/json;charset=UTF-8",
            params: {
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAllAddendumDetails(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAllAddendumDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getForm2Summary(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getForm2Summary",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                addendumNo: addendumNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getAddendumFinalForm(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumFinalForm",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                addendumNo: addendumNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getAddendumDetailsWithoutHeaderRef(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getAddendumDetailsWithoutHeaderRef",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getDefaultValuesForAddendumDetails(jobNo, subcontractNo, addendumNo, lineType, nextSeqNo) {
        var request = $http({
            method: "get",
            url: "service/addendum/getDefaultValuesForAddendumDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo,
            	lineType: lineType, 
            	nextSeqNo: nextSeqNo
            	
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function createAddendum(addendum) {
        var request = $http({
            method: "post",
            url: "service/addendum/createAddendum",
            dataType: "application/json;charset=UTF-8",
            data: addendum
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateAddendumAdmin(addendum) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendumAdmin",
            dataType: "application/json;charset=UTF-8",
            data: addendum
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function updateAddendum(addendum) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendum",
            dataType: "application/json;charset=UTF-8",
            data: addendum
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateAddendumDetailHeader(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, description) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendumDetailHeader",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo,
            	addendumDetailHeaderRef: addendumDetailHeaderRef,
            	description: description
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteAddendumDetailHeader(addendumDetailHeaderRef) {
        var request = $http({
            method: "post",
            url: "service/addendum/deleteAddendumDetailHeader",
            dataType: "application/json;charset=UTF-8",
            params: {
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function addAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetail) {
        var request = $http({
            method: "post",
            url: "service/addendum/addAddendumDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            },
            data: addendumDetail
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function addAddendumFromResourceSummaries(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, resourceSummaryList) {
        var request = $http({
            method: "post",
            url: "service/addendum/addAddendumFromResourceSummaries",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                addendumNo: addendumNo,
                addendumDetailHeaderRef: addendumDetailHeaderRef
            },
            data: resourceSummaryList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function addAddendumFromResourceSummaryAndAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, resourceSummary, addendumDetail) {
        var request = $http({
            method: "post",
            url: "service/addendum/addAddendumFromResourceSummaryAndAddendumDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                addendumNo: addendumNo,
                addendumDetailHeaderRef: addendumDetailHeaderRef
            },
            data: {
                resourceSummary: resourceSummary,
                addendumDetail: addendumDetail
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }


    function updateAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetail) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendumDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            },
            data: addendumDetail
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
   
    function updateAddendumDetailListAdmin(addendumDetailList) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendumDetailListAdmin",
            dataType: "application/json;charset=UTF-8",
            data: addendumDetailList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function updateAddendumDetailList(addendumDetailList) {
        var request = $http({
            method: "post",
            url: "service/addendum/updateAddendumDetailList",
            dataType: "application/json;charset=UTF-8",
            data: addendumDetailList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    
    function deleteAddendumDetail(jobNo, subcontractNo, addendumNo, addendumDetailList) {
        var request = $http({
            method: "post",
            url: "service/addendum/deleteAddendumDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo
            },
            data: addendumDetailList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function deleteAddendumFromSCDetails(jobNo, subcontractNo, addendumNo, addendumDetailHeaderRef, subcontractDetailList) {
        var request = $http({
            method: "post",
            url: "service/addendum/deleteAddendumFromSCDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo,
            	addendumDetailHeaderRef: addendumDetailHeaderRef
            },
            data: subcontractDetailList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function submitAddendumApproval(jobNo, subcontractNo, addendumNo) {
        var request = $http({
            method: "post",
            url: "service/addendum/submitAddendumApproval",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	addendumNo: addendumNo,
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);
