mainApp.controller('PaymentCtrl', ['$scope', '$uibModal',  'modalService', '$animate', 'colorCode', 'paymentService',
                                              function($scope, $uibModal, modalService, $animate, colorCode, paymentService) {
	$scope.paymentTerms = "QS2";
	$scope.maxPaymentNo = "5";
	$scope.latestPaymentStatus = "APR";
	
	
	loadPaymentCertList();
    
    function loadPaymentCertList() {
    	paymentService.getSCPaymentCertList($scope.jobNo, $scope.subcontractNo)
   	 .then(
			 function( data ) {
				 $scope.payments = data;
			 });
    }
    
    $scope.progress_bar = function(status){
    	if (status.localeCompare('PND') == 0) {
    		return "25%";
    	}else if (status.localeCompare('SBM') == 0) {
    		return "50%";
    	}else if (status.localeCompare('UFR') == 0) {
    		return { width: "50%" }
    	}else if (status.localeCompare('PCS') == 0) {
    		return { width: "75%" }
    	}else if (status.localeCompare('APR') == 0) {
    		return "100%";
    	}else{
    		return "10%";
    	}
    }
    
    
    $scope.removeDefaultAnimation = function (){
        $animate.enabled(false);
    };
    

    //Calendar Setting
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


