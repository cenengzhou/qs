mainApp.controller('AddendumSelectCtrl', ['$scope', 'modalService', 'colorCode', 'addendumService', 'subcontractService',
                                    function($scope, modalService, colorCode,  addendumService, subcontractService) {

	$scope.maxAddendumNo = 0;
	$scope.latestAddendumStatus = '';
	loadData();

	function loadData(){
		getSubcontract();
		getLatestAddendum();
		getAddendumList();
		getTotalApprovedAddendumAmount();
	}
	
	function getSubcontract(){
		subcontractService.getSubcontract($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.subcontract = data;
				});
	}

	
	function getLatestAddendum(){
		addendumService.getLatestAddendum($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);
					$scope.latestAddendum = data;
				});
	}
	
	function getAddendumList(){
		addendumService.getAddendumList($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					//console.log(data);
					if(data != null && data.length > 0){
						$scope.addendums = data;

						$scope.maxAddendumNo = Math.max.apply(Math,$scope.addendums.map(function(item){return item.no;}));

						var obj = $scope.addendums.filter(function(item){ return item.no == $scope.maxAddendumNo; });
						$scope.latestAddendumStatus = obj[0]['status'];

						getSubcontract();
					}
					prepareCalendar();
					
				});
	}
	
	function getTotalApprovedAddendumAmount(){
		addendumService.getTotalApprovedAddendumAmount($scope.jobNo, $scope.subcontractNo)
		.then(
				function( data ) {
					console.log(data);
					$scope.totalAddendumAmount = data;
				});
	}
	
	
	function prepareCalendar(){
		var myEvent = [];

		for (i in $scope.addendums) {
			curDate = (new Date($scope.addendums[i].dateApproval)).yyyymmdd();

			myEvent.push({
				title:"Addendum. No."+$scope.addendums[i].no,
				allDay: true,
				start:  curDate,
				tooltip: "Approved on "+ curDate
			});
		}

		var myCalendar = $('#calendar').fullCalendar({
			header: {
				left: 'month,agendaWeek,agendaDay',
				center: 'title',
				right: 'prev,today,next '
			},
			selectable: true,
			selectHelper: true,
			eventLimit: true, // allow "more" link when too many events
			events:  myEvent,
			eventColor: colorCode.primary,
			eventTextColor: colorCode.white,
			eventRender: function(event, element) {
				element.attr('title', event.tooltip);
			}
		});
	}

	Date.prototype.yyyymmdd = function() {
		var yyyy = this.getFullYear().toString();
		var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
		var dd  = this.getDate().toString();

		return  yyyy+"-"+(mm.length===2?mm:"0"+mm) +"-"+ (dd.length===2?dd:"0"+dd); // padding
	};


    $scope.progress_bar = function(status){
    	if (status.localeCompare('PENDING') == 0) {
    		return "33%";
    	}else if (status.localeCompare('SUBMITTED') == 0) {
    		return "50%";
    	}else if (status.localeCompare('APPROVED') == 0) {
    		return "100%";
    	}
    }
    
   
    
}]);


