mainApp.controller('SubcontractPaymentCtrl', ['$scope', '$uibModal', '$log', 'modalService', '$animate', function($scope, $uibModal, $log, modalService, $animate) {
	$scope.packageno = "1004";
	$scope.paymentTerms = "QS2";
	$scope.expand = true;
	$scope.maxPaymentNo = "5";
	var paymentJson = [
		    {
		        "paymentNo": "5",
		        "maincertNo": "16",
		        "status": "Submitted",
		        "submittedDate": "20/03/2016",
		        "dueDate": "29/03/2016",
		        "asAtDate": "29/03/2016",
		        "invoiceReceivedDate": "29/03/2016",
		        "certIssueDate": null,
		        "paymentAmount": 12365,
		        "paymentType": 'Interim',
		        "requisition": 'No'
		    },
		    {
		        "paymentNo": "4",
		        "maincertNo": "15",
		        "status": "Posted to Finance",
		        "submittedDate": "20/03/2016",
		        "dueDate": "20/03/2016",
		        "asAtDate": "20/03/2016",
		        "invoiceReceivedDate": null,
		        "certIssueDate": "20/03/2016",
		        "paymentAmount": 985123,
		        "paymentType": 'Interim',
		        "requisition": 'No'
		    },
		    {
		        "paymentNo": "3",
		        "maincertNo": "14",
		        "status": "Posted to Finance",
		        "submittedDate": "15/03/2016",
		        "dueDate": "15/03/2016",
		        "asAtDate": "15/03/2016",
		        "invoiceReceivedDate": null,
		        "certIssueDate": "15/03/2016",
		        "paymentAmount": 55698.23,
		        "paymentType": 'Interim',
		        "requisition": 'No'
		    },
		    {
		        "paymentNo": "2",
		        "maincertNo": "13",
		        "status": "Posted to Finance",
		        "submittedDate": "08/03/2016",
		        "dueDate": "08/03/2016",
		        "asAtDate": "08/03/2016",
		        "invoiceReceivedDate": null,
		        "certIssueDate": "08/03/2016",
		        "paymentAmount": 1136.3,
		        "paymentType": 'Interim',
		        "requisition": 'No'
		    },
		    {
		        "paymentNo": "1",
		        "maincertNo": "12",
		        "status": "Posted to Finance",
		        "submittedDate": "01/03/2016",
		        "dueDate": "01/03/2016",
		        "asAtDate": "01/03/2016",
		        "invoiceReceivedDate": null,
		        "certIssueDate": "01/03/2016",
		        "paymentAmount": 96512.23,
		        "paymentType": 'Interim',
		        "requisition": 'No'
		    }
		    
    ];
    $scope.payments = paymentJson;

    $scope.progress_bar = function(status){
    	if (status.localeCompare('Pending') == 0) {
    		return "25%";
    	}else if (status.localeCompare('Submitted') == 0) {
    		return "50%";
    	}else if (status.localeCompare('Under Finance Review') == 0) {
    		return { width: "50%" }
    	}else if (status.localeCompare('Waiting for Posting') == 0) {
    		return { width: "75%" }
    	}else if (status.localeCompare('Posted to Finance') == 0) {
    		return "100%";
    	}else{
    		return "10%";
    	}
    }
    
    
    $scope.searchquery = '';
    
    $scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    

    
}]);


