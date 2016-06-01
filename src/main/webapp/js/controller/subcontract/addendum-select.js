mainApp.controller('AddendumCtrl', ['$scope', '$uibModal', '$log', 'modalService', '$animate', 'colorCode',
                                    function($scope, $uibModal, $log, modalService, $animate, colorCode) {
	$scope.paymentTerms = "QS2";
	$scope.expand = true;
	$scope.maxPaymentNo = "5";
	$scope.latestAddendumStatus = "Approved";
	
	var addendumJson = [
		    {
		        "addendumNo": "5",
		        "addendumTitle": "Addendum Title",
		        "addendumStatus": "Submitted",
		        "approvalStatus": "Submitted",
		        "submissionDate": "20/03/2016",
		        "approvedDate": "29/03/2016",
		        "addendumAmount": 12365,
		        "preparedBy": 'Koey'
		    },
		    {
		        "addendumNo": "4",
		        "addendumTitle": "Addendum Title",
		        "addendumStatus": "Approved",
		        "approvalStatus": "Approved",
		        "submissionDate": "20/03/2016",
		        "approvedDate": "20/03/2016",
		        "addendumAmount": 985123,
		        "preparedBy": 'Koey'
		    },
		    {
		        "addendumNo": "3",
		        "addendumTitle": "Addendum Title",
		        "addendumStatus": "Approved",
		        "approvalStatus": "Approved",
		        "submissionDate": "15/03/2016",
		        "approvedDate": "15/03/2016",
		        "addendumAmount": 55698.23,
		        "preparedBy": 'Koey'
		    },
		    {
		        "addendumNo": "2",
		        "addendumTitle": "Addendum Title",
		        "addendumStatus": "Approved",
		        "approvalStatus": "Approved",
		        "submissionDate": "08/03/2016",
		        "approvedDate": "08/03/2016",
		        "addendumAmount": 1136.3,
		        "preparedBy": 'Koey'
		    },
		    {
		        "addendumNo": "1",
		        "addendumTitle": "Addendum Title",
		        "addendumStatus": "Approved",
		        "approvalStatus": "Approved",
		        "submissionDate": "01/03/2016",
		        "approvedDate": "01/03/2016",
		        "addendumAmount": 96512.23,
		        "preparedBy": 'Koey'
		    }
		    
    ];
    $scope.addendums = addendumJson;

    $scope.progress_bar = function(status){
    	if (status.localeCompare('Pending') == 0) {
    		return "33%";
    	}else if (status.localeCompare('Submitted') == 0) {
    		return "50%";
    	}else if (status.localeCompare('Approved') == 0) {
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
            start: currentYear + '-'+ '06' +'-01',
            tooltip: 'Submitted on and Posted to Finance on '
        }, {
            title: 'Certificate No.1',
            start: currentYear + '-'+ currentMonth +'-07',
            end: currentYear + '-'+ currentMonth +'-10'
        },{
            title: 'Conference',
            start: currentYear + '-'+ currentMonth +'-11',
            end: currentYear + '-'+ currentMonth +'-13'
        }, {
            title: 'Meeting',
            start: currentYear + '-'+ currentMonth +'-12T10:30:00',
            end: currentYear + '-'+ currentMonth +'-12T12:30:00'
        }, {
            title: 'Dinner',
            start: currentYear + '-'+ currentMonth +'-12T20:00:00'
        }, {
            title: 'Birthday Party',
            start: currentYear + '-'+ currentMonth +'-13T07:00:00'
        }],
        eventColor: colorCode.lime,
        eventTextColor: colorCode.white,
        eventRender: function(event, element) {
            element.attr('title', event.tooltip);
        }
    });
}]);


