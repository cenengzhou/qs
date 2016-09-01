mainApp.controller('MainCertCtrl', ['$scope', '$uibModal',  'modalService', 'colorCode', 'mainCertService', '$cookies', 
                                   function($scope, $uibModal, modalService, colorCode, mainCertService, $cookies) {

	$scope.maxCertNo = 0;
	$scope.totalCertificateAmount = 0;
	$scope.latestCertStatus = '';
	
	$scope.jobNo = $cookies.get("jobNo");
	$scope.jobDescription = $cookies.get("jobDescription");
	
	loadData();

	function loadData(){
		getCertificateList();
		getLatestMainCert();
	}
	

	function getCertificateList() {
		mainCertService.getCertificateList($scope.jobNo)
		.then(
				function( data ) {
					//console.log(data);
					if(data.length !=0){
						$scope.maxCertNo = Math.max.apply(Math,data.map(function(item){return item.certificateNumber;}));

						var obj = data.filter(function(item){ return item.certificateNumber == $scope.maxCertNo; });
						$scope.latestCertStatus = obj[0]['certificateStatus'];
						
						data.reverse();

						var tempObj;
						angular.forEach(data, function(value, key){
							if(value.certificateNumber==1){
								value.postingAmount = value.certNetAmount;
								value.netGSTReceivable = value.gstReceivable;
								value.netGSTPayable = value.gstPayable;
							}
							else{
								value.postingAmount = value.certNetAmount - tempObj.certNetAmount;
								value.netGSTReceivable = value.gstReceivable - tempObj.gstReceivable;
								value.netGSTPayable = value.gstPayable - tempObj.gstPayable;
							}
							tempObj = value;
							
						});
						$scope.maincerts = data;
					}
					
					prepareCalendar();
				});
	}

	function getLatestMainCert() {
		mainCertService.getLatestMainCert($scope.jobNo, '300')
		.then(
				function( data ) {
					if(data.length !=0){
						$scope.totalCertificateAmount = data.certNetAmount;
					}
				});
	}
	
	function prepareCalendar(){
		var myEvent = [];

		for (i in $scope.maincerts) {
			curDate = (new Date($scope.maincerts[i].certIssueDate)).yyyymmdd();

			myEvent.push({
				title:"Cert. No."+$scope.maincerts[i].certificateNumber,
				allDay: true,
				start:  curDate,
				tooltip: "Posted to Finance on "+ curDate
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
			eventColor: colorCode.purple,
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

	$scope.convertPaymentStatus = function(status){
		if(status!=null){
			if (status.localeCompare('100') == 0) {
				return "Certificate Created";
			}else if (status.localeCompare('120') == 0) {
				return "IPA Sent";
			}else if (status.localeCompare('150') == 0) {
				return { width: "Certificate(IPC) Confirmed" }
			}else if (status.localeCompare('200') == 0) {
				return { width: "Certificate(IPC) Waiting for special approval" }
			}else if (status.localeCompare('300') == 0) {
				return "Certificate Posted to Finance's AR";
			}else if (status.localeCompare('400') == 0) {
				return "Certifited Amount Received";
			}
		}
	}

	$scope.progress_bar = function(status){
		if (status.localeCompare('100') == 0) {
			return "25%";
		}else if (status.localeCompare('120') == 0) {
			return "50%";
		}else if (status.localeCompare('150') == 0) {
			return { width: "50%" }
		}else if (status.localeCompare('200') == 0) {
			return { width: "75%" }
		}else if (status.localeCompare('300') == 0 || status.localeCompare('400') == 0) {
			return "100%";
		}else{
			return "10%";
		}
	}

}]);


