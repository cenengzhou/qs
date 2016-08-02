mainApp.controller('RepackagingCtrl', ['$scope', '$location', '$cookieStore', 'repackagingService', 'modalService',
                                       function($scope, $location, $cookieStore, repackagingService, modalService) {

	$scope.jobNo = $cookieStore.get("jobNo");
	$scope.jobDescription = $cookieStore.get("jobDescription");
	
	$scope.repackaging = "";
	loadRepacakgingData();
    
	$scope.click = function(view) {
		if(view=="unlock"){
			addRepackaging();
		}else if (view=="reset"){
			deleteRepackaging();
		}else if (view=="snapshot"){
			generateSnapshot();
		}else if (view=="confirm"){
			modalService.open('lg', 'view/repackaging/modal/repackaging-confirm.html', 'RepackagingConfirmModalCtrl');
		}
	};

	$scope.updateRemarks = function(){
		updateRepackaging();
	}
	
    
    function loadRepacakgingData() {
   	 repackagingService.getLatestRepackaging($scope.jobNo)
   	 .then(
   			 function( data ) {
   				 $scope.repackaging = data;
   				 console.log("repackaging status: "+$scope.repackaging.status);
   			 });
    }
    
    function addRepackaging() {
      	 repackagingService.addRepackaging($scope.jobNo)
      	 .then(
   			 function( data ) {
   				loadRepacakgingData();
   				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been unlocked.");
				}
   			 });
       }
    
    function updateRepackaging() {
     	 repackagingService.updateRepackaging($scope.repackaging)
     	 .then(
  			 function( data ) {
  				loadRepacakgingData();
  				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Remarks has been updated.");
				}
  			 });
      }
    
    
	
    function deleteRepackaging() {
     	 repackagingService.deleteRepackaging($scope.repackaging.id)
     	 .then(
  			 function( data ) {
  				loadRepacakgingData();
  				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Repackaging has been reset.");
				}
  			 });
      }
    
    function generateSnapshot() {
    	 repackagingService.generateSnapshot($scope.repackaging.id, $scope.jobNo)
    	 .then(
 			 function( data ) {
 				loadRepacakgingData();
 				if(data.length!=0){
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
				}else{
					modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Snapshot has been generated.");
				}
 			 });
     }
    
    //Attachment
	$scope.partialDownloadLink = 'http://localhost:8080/QSrevamp2/download?filename=';
    $scope.filename = '';

    $scope.uploadFile = function() {
    	console.log("Upload file");
        $scope.processDropzone();
    };

    $scope.reset = function() {
    	console.log("Reset file");
        $scope.resetDropzone();
    };
	
	
	
	
}]);