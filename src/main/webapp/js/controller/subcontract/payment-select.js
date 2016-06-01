mainApp.controller('SubcontractPaymentCtrl', ['$scope', '$uibModal', '$log', 'modalService', '$animate', 'colorCode',
                                              function($scope, $uibModal, $log, modalService, $animate, colorCode) {
	$scope.packageno = "1004";
	$scope.paymentTerms = "QS2";
	$scope.expand = true;
	$scope.maxPaymentNo = "5";
	$scope.latestPaymentStatus = "Posted to Finance";
	
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
    

    var date = new Date();
    var currentYear = date.getFullYear();
    var currentMonth = date.getMonth() + 1;
        currentMonth = (currentMonth < 10) ? '0' + currentMonth : currentMonth;
    
    $('#calendar').fullCalendar({
        header: {
            left: 'month,agendaWeek,agendaDay',
            center: 'title',
            right: 'prev,today,next '
        },
        selectable: true,
        selectHelper: true,
        eventLimit: true, // allow "more" link when too many events
        events: [{
            title: 'Certificate No.1',
            start: currentYear + '-'+ '05' +'-01',
            tooltip: 'Submitted on and Posted to Finance on '
        }, {
            title: 'Certificate No.1',
            start: currentYear + '-'+ currentMonth +'-07',
            end: currentYear + '-'+ currentMonth +'-10'
        }, {
            id: 999,
            title: 'Certificate No.1',
            start: currentYear + '-'+ currentMonth +'-09T16:00:00'
        }, {
            id: 999,
            title: 'Certificate No.1',
            start: currentYear + '-'+ currentMonth +'-16T16:00:00'
        }, {
            title: 'Conference',
            start: currentYear + '-'+ currentMonth +'-11',
            end: currentYear + '-'+ currentMonth +'-13'
        }, {
            title: 'Meeting',
            start: currentYear + '-'+ currentMonth +'-12T10:30:00',
            end: currentYear + '-'+ currentMonth +'-12T12:30:00'
        }, {
            title: 'Lunch',
            start: currentYear + '-'+ currentMonth +'-12T12:00:00'
        }, {
            title: 'Meeting',
            start: currentYear + '-'+ currentMonth +'-12T14:30:00'
        }, {
            title: 'Happy Hour',
            start: currentYear + '-'+ currentMonth +'-12T17:30:00'
        }, {
            title: 'Dinner',
            start: currentYear + '-'+ currentMonth +'-12T20:00:00'
        }, {
            title: 'Birthday Party',
            start: currentYear + '-'+ currentMonth +'-13T07:00:00'
        }],
        eventColor: colorCode.primary,
        eventTextColor: colorCode.white,
        eventRender: function(event, element) {
            element.attr('title', event.tooltip);
        }
    });
    
}]);


