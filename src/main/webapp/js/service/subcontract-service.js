mainApp.service('subcontractService', ['$http', '$q', 'GlobalHelper',  function($http, $q, GlobalHelper){
	// Return public API.
    return({
    	getSubcontract: 									getSubcontract,
    	getSubcontractList: 								getSubcontractList,
    	getWorkScope: 										getWorkScope,
    	getSCDetails:										getSCDetails,
    	getSubcontractDetailForWD:							getSubcontractDetailForWD,
    	getSubcontractDetailByID:							getSubcontractDetailByID,
    	getOtherSubcontractDetails:							getOtherSubcontractDetails,
    	getSubcontractDetailsWithBudget:					getSubcontractDetailsWithBudget,
    	getSubcontractDetailsDashboardData:					getSubcontractDetailsDashboardData,
    	getSubcontractDashboardData:						getSubcontractDashboardData,
    	getAwardedSubcontractNos:							getAwardedSubcontractNos,
    	getUnawardedSubcontractNosUnderPaymentRequisition: 	getUnawardedSubcontractNosUnderPaymentRequisition,
    	getSubcontractDetailTotalNewAmount:					getSubcontractDetailTotalNewAmount,
    	getFinalizedSubcontractNos:							getFinalizedSubcontractNos,
    	getCompanyBaseCurrency:								getCompanyBaseCurrency,
    		
    	upateSubcontract: 									upateSubcontract,
    	upateSubcontractDates: 								upateSubcontractDates,
    	updateWDandIV:										updateWDandIV,
    	updateWDandIVList:									updateWDandIVList,
    	updateFilteredWDandIVByPercent:						updateFilteredWDandIVByPercent,
    	submitAwardApproval:								submitAwardApproval,
    	recalculateResourceSummaryIV:						recalculateResourceSummaryIV,
    	calculateTotalWDandCertAmount:						calculateTotalWDandCertAmount,
    	addAddendumToSubcontractDetail:						addAddendumToSubcontractDetail,
    	updateSubcontractDetailAddendum:					updateSubcontractDetailAddendum,
    	deleteSubcontractDetailAddendum:					deleteSubcontractDetailAddendum,
    	updateSCDetailsNewQuantity:							updateSCDetailsNewQuantity,
    	submitSplitTerminateSC:								submitSplitTerminateSC,
    	generateSCDetailsForPaymentRequisition:				generateSCDetailsForPaymentRequisition,
    		
    	runProvisionPostingManually:						runProvisionPostingManually,
    	generateSCPackageSnapshotManually: 					generateSCPackageSnapshotManually,
    	updateF58001FromSCPackageManually: 					updateF58001FromSCPackageManually,
    	searchSystemConstants:								searchSystemConstants,
    	updateMultipleSystemConstants:						updateMultipleSystemConstants,
    	inactivateSystemConstant:							inactivateSystemConstant,
    	createSystemConstant: 								createSystemConstant,
    	updateSubcontractAdmin:								updateSubcontractAdmin,
    	
    	getSubcontractSnapshotList:							getSubcontractSnapshotList,
    	getProvisionPostingHistList:						getProvisionPostingHistList,
    	getSCDetailList:									getSCDetailList,
    	getSCDetailForAddendumUpdate:						getSCDetailForAddendumUpdate,
    	getDefaultValuesForSubcontractDetails:				getDefaultValuesForSubcontractDetails,
    	getPerforamceAppraisalsList:						getPerforamceAppraisalsList,
    });
	
    function getSubcontractList(jobNo, awardedOnly) {
    	var myHeaders = {
    	        "Accept": "application/json",
    	        "Content-Type": "application/json",
    	        //'Authorization': 'Basic ' + Base64.encode("peer" + ":" + "bkend-srv-1234")
    	    };
    	
        var request = $http({
            method: "get",
            //headers: myHeaders,
            url: "service/subcontract/getSubcontractList",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
    	/*var request = $http({
            method: "get",
            url: "/gammonqs/subcontractReportExport.rpt",
            params: {
            	jobNumber: "13362",
            	subcontractNumber:"",
            	subcontractorNumber:"",
            	subcontractorNature:"",
            	paymentType:"",
            	workScope:"",
            	clientNo:"",
            	includeJobCompletionDate:"",
            	splitTerminateStatus:"",
            	month:"",
            	year:""
            },
            responseType: 'arraybuffer'
            */
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSubcontract(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontract",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSCDetails(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSCDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSubcontractDetailForWD(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailForWD",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSubcontractDetailByID(id) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailByID",
            dataType: "application/json;charset=UTF-8",
            params: {
            	id: id
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getOtherSubcontractDetails(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getOtherSubcontractDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getSubcontractDetailsWithBudget(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailsWithBudget",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSubcontractDashboardData(jobNo, subcontractNo, year) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDashboardData",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo, 
            	year: year
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSubcontractDetailsDashboardData(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailsDashboardData",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getAwardedSubcontractNos(jobNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getAwardedSubcontractNos",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
     
    function getSubcontractDetailTotalNewAmount(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSubcontractDetailTotalNewAmount",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getUnawardedSubcontractNosUnderPaymentRequisition(jobNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getUnawardedSubcontractNosUnderPaymentRequisition",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getFinalizedSubcontractNos(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getFinalizedSubcontractNos",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo, 
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getCompanyBaseCurrency(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getCompanyBaseCurrency",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo 
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getWorkScope(workScopeCode) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getWorkScope",
            dataType: "application/json;charset=UTF-8",
            params: {
            	workScopeCode: workScopeCode
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function getDefaultValuesForSubcontractDetails(jobNo, subcontractNo, lineType) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getDefaultValuesForSubcontractDetails",
            dataType: "application/json;charset=UTF-8",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo,
            	lineType: lineType
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function upateSubcontract(jobNo, subcontract) {
        var request = $http({
            method: "post",
            url: "service/subcontract/upateSubcontract",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo
            },
            data: subcontract
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function upateSubcontractDates(jobNo, subcontract) {
        var request = $http({
            method: "post",
            url: "service/subcontract/upateSubcontractDates",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo
            },
            data: subcontract
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateWDandIV(jobNo, subcontractNo, scDetail) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateWDandIV",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            },
            data: scDetail
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateWDandIVList(jobNo, subcontractNo, scDetailList) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateWDandIVList",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            },
            data: scDetailList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateFilteredWDandIVByPercent(jobNo, subcontractNo, filteredIds, percent) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateFilteredWDandIVByPercent",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                percent: percent
            },
            data:filteredIds
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function recalculateResourceSummaryIV(jobNo, subcontractNo, recalculateFinalizedPackage) {
        var request = $http({
            method: "post",
            url: "service/subcontract/recalculateResourceSummaryIV",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                recalculateFinalizedPackage: recalculateFinalizedPackage
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function calculateTotalWDandCertAmount(jobNo, subcontractNo, recalculateRententionAmount) {
        var request = $http({
            method: "post",
            url: "service/subcontract/calculateTotalWDandCertAmount",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                recalculateRententionAmount: recalculateRententionAmount
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function addAddendumToSubcontractDetail(jobNo, subcontractNo, subcontractDetail) {
        var request = $http({
            method: "post",
            url: "service/subcontract/addAddendumToSubcontractDetail",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            },
            data: subcontractDetail
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateSubcontractDetailAddendum(subcontractDetail) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateSubcontractDetailAddendum",
            dataType: "application/json;charset=UTF-8",
            data: subcontractDetail
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    
    function deleteSubcontractDetailAddendum(jobNo, subcontractNo, sequenceNo, lineType) {
        var request = $http({
            method: "post",
            url: "service/subcontract/deleteSubcontractDetailAddendum",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo,
                sequenceNo: sequenceNo,
                lineType:lineType
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function updateSCDetailsNewQuantity(subcontractDetailList) {
        var request = $http({
            method: "post",
            url: "service/subcontract/updateSCDetailsNewQuantity",
            dataType: "application/json;charset=UTF-8",
            data: subcontractDetailList
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function submitAwardApproval(jobNo, subcontractNo) {
        var request = $http({
            method: "post",
            url: "service/subcontract/submitAwardApproval",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function submitSplitTerminateSC(jobNo, subcontractNo, splitTerminate) {
        var request = $http({
            method: "post",
            url: "service/subcontract/submitSplitTerminateSC",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo, 
                splitTerminate: splitTerminate
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    
    function generateSCDetailsForPaymentRequisition(jobNo, subcontractNo) {
        var request = $http({
            method: "post",
            url: "service/subcontract/generateSCDetailsForPaymentRequisition",
            dataType: "application/json;charset=UTF-8",
            params: {
                jobNo: jobNo,
                subcontractNo: subcontractNo 
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function runProvisionPostingManually(jobNumber, glDate){
    	var request = $http({
    		method: 'post',
    		url: 'service/subcontract/runProvisionPostingManually',
    		dataType: "application/json;charset=UTF-8",
    		params: {
    			jobNumber: jobNumber,
    			glDate: glDate
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function generateSCPackageSnapshotManually(){
    	var request = $http.post("service/subcontract/generateSCPackageSnapshotManually");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateF58001FromSCPackageManually(){
    	var request = $http.post("service/subcontract/updateF58001FromSCPackageManually");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

	function searchSystemConstants(){
    	var request = $http.post("service/subcontract/searchSystemConstants");
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
	function updateMultipleSystemConstants(systemConstants){
    	var request = $http.post("service/subcontract/updateMultipleSystemConstants", systemConstants);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
	function inactivateSystemConstant(systemConstants){
    	var request = $http.post('service/subcontract/inactivateSystemConstant', systemConstants);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
	function createSystemConstant(newRecord){
    	var request = $http.post('service/subcontract/createSystemConstant', newRecord);
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function updateSubcontractAdmin(subcontract){
       	var request = $http.post('service/subcontract/updateSubcontractAdmin', subcontract);
       	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSubcontractSnapshotList(year, month, awardOnly, commonKeyValue){
    	var request = $http({
    		method: 'post',
    		url: 'service/subcontract/getSubcontractSnapshotList',
    		params:{
    			year: year,
    			month: month,
    			awardOnly: awardOnly
    		},
    		data: commonKeyValue
    	});    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getProvisionPostingHistList(jobNo, subcontractNo, year, month){
    	var request = $http({
    		method: 'get',
    		url: 'service/subcontract/getProvisionPostingHistList',
    		params: {
    			jobNo: jobNo,
    			subcontractNo: subcontractNo,
    			year: year,
    			month: month
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    
    function getSCDetailList(commonKeyValue) {
        var request = $http({
            method: "POST",
            url: "service/subcontract/getSCDetailList",
            data: commonKeyValue
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }
    

    function getSCDetailForAddendumUpdate(jobNo, subcontractNo) {
        var request = $http({
            method: "get",
            url: "service/subcontract/getSCDetailForAddendumUpdate",
            params: {
            	jobNo: jobNo,
            	subcontractNo: subcontractNo
            }
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

    function getPerforamceAppraisalsList(jobNumber, vendorNumber, subcontractNumber, groupCode, status){
    	var request = $http({
    		method: 'post',
    		url: 'service/subcontract/getPerforamceAppraisalsList',
    		params: {
    			jobNumber: jobNumber,
    			vendorNumber: vendorNumber,
    			subcontractNumber: subcontractNumber,
    			groupCode: groupCode,
    			status: status
    		}
    	});
    	return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }   

}]);




