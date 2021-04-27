mainApp.service('commercialActionService', ['$http', '$q', '$log', 'GlobalHelper',  function($http, $q, $log, GlobalHelper){
    // Return public API.
    return({
        getCommercialActionList: getCommercialActionList,
        addCommercialAction: addCommercialAction,
        saveById: saveById,
        saveList: saveList,
        deleteById: deleteById
    });

    function getCommercialActionList(jobNo, year, month) {
        var deferred = $q.defer();
        $http({
            method: 'GET',
            url: 'service/commercialAction/getCommercialActionList/'+jobNo+'/'+year+'/'+month
        }).success(function(data) {
            deferred.resolve(data);
        }).error(function(msg, code) {
            deferred.reject(msg);
            $log.error(msg, code);
        });
        return deferred.promise;
    }

    function addCommercialAction(jobNo, commercialAction) {
        var request = $http({
            method: "post",
            url: "service/commercialAction/addCommercialAction/" + jobNo,
            data: commercialAction
        });
        return request;
    }

    function saveById(id, ca) {
        var request = $http({
            method: "post",
            url: "service/commercialAction/saveById/" + id,
            data: ca
        });
        return request;
    }

    function saveList(jobNo, caList) {
        var request = $http({
            method: "post",
            url: "service/commercialAction/saveList/" + jobNo,
            data: caList
        });
        return request;
    }

    function deleteById(id) {
        var request = $http({
            method: "delete",
            url: "service/commercialAction/deleteById/" + id
        });
        return( request.then( GlobalHelper.handleSuccess, GlobalHelper.handleError ) );
    }

}]);