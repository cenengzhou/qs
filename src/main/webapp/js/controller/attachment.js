mainApp.controller('AttachmentCtrl', ['$scope', '$location', function($scope, $location) {
	
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