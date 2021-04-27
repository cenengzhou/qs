mainApp.controller('JobCommercialActionCtrl', ['$scope', 'commercialActionService', 'modalService', '$cookies', '$q', '$state', 'rootscopeService', '$timeout',
    function ($scope, commercialActionService, modalService, $cookies, $q, $state, rootscopeService, $timeout) {

        // init
        var today = new Date();
        $scope.month = today.getUTCMonth() + 1; //months from 1-12
        $scope.year = today.getUTCFullYear();
        $scope.monthYear = $scope.year + '-' + $scope.month;
        $scope.jobNo = $cookies.get("jobNo");
        $scope.editable = false;
        $scope.commercialAction = { };

        var self = this;
        self.querySearch = querySearch;
        self.searchTextChange = searchTextChange;
        self.selectedItemChange = selectedItemChange;

        function querySearch(query) {
            var results = query ? self.repos.filter(createFilterFor(query)) : self.repos, deferred;
            deferred = $q.defer();
            $timeout(function() {
                deferred.resolve(results);
            }, 300, false);
            return deferred.promise;
        }

        function searchTextChange(pSearchText, person, ca) {
            ca.actionResp = '';
        }

        function selectedItemChange(item, person, ca) {
            ca.actionResp = '';
            if (item && item.username) {
                ca.actionResp = item.username;
            }
        }

        function createFilterFor(query) {
            var lowercaseQuery = (query || "").toLowerCase();
            return function filterFn(item) {
                var fullNameRegExp = new RegExp(lowercaseQuery.split(' ').join('.*'));
                var fullNameReverseRegExp = new RegExp(lowercaseQuery.split(' ').reverse().join('.*'));
                return lowercaseQuery.length > 3 && (
                    (item.username && item.username.indexOf(lowercaseQuery) === 0) ||
                    (item.employeeId && item.employeeId.indexOf(lowercaseQuery) === 0) ||
                    (item.fullName && (fullNameRegExp.test(item.fullName.toLowerCase()) || fullNameReverseRegExp.test(item.fullName.toLowerCase()))) ||
                    (item.email && item.email.toLowerCase().indexOf(lowercaseQuery) === 0)
                );
            };
        }

        function Success(){
            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Records have been updated.");
            $state.reload();
        }

        function Error(error){
            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', error.data.message )
                .closed.then(function(){
                $state.reload();
            });
        }

        function getData(year, month) {
            var commercialAction = commercialActionService.getCommercialActionList($scope.jobNo, year, month);
            var allUsers = rootscopeService.gettingAllUser();
            $q.all([commercialAction, allUsers])
                .then(function (data){
                    $scope.data = {};
                    $scope.data.commercialActionList = data[0];
                    self.repos = data[1];
                    $scope.data.commercialActionList.forEach(ca => {
                        ca.person = self.repos.filter(p => p.username == ca.actionResp)[0]
                    })
                });
        }

        $scope.toggleEdit = function(){
            $scope.editable = !$scope.editable;
        }

        $scope.add = function() {
            // validate
            if (!$scope.commercialAction || !$scope.commercialAction.itemNo || !$scope.commercialAction.actionDesc
                || !$scope.commercialAction.actionStatus || !$scope.commercialAction.actionDate || !$scope.commercialAction.actionResp) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Please check your input.");
                return
            }

            if (self.repos.filter(x => x.username == $scope.commercialAction.actionResp).length === 0) {
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "User name is not valid. Please check.");
                return
            }

            // add
            commercialActionService.addCommercialAction($scope.jobNo, $scope.commercialAction)
                .then(
                    function (result) {
                        if (result.data != "") {
                            modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', result.data);
                        } else {
                            Success();
                        }
                    }, function (error) {
                        Error(error)
                    });
        }

        $scope.delete = function(id){
            commercialActionService.deleteById(id)
                .then(function(){
                    Success();
                }, function(error){
                    Error(error);
                });
        }

        $scope.update = function(id, ca){
            $scope.editable = false;

            commercialActionService.saveById(id, ca)
                .then(data => {
                        if(data == 'true'){
                            Success();
                        }
                    }, error => {
                        Error(error);
                    }
                );

        }

        $scope.updateAll = function(caList){
            $scope.editable = false;

            commercialActionService.saveList($scope.jobNo, caList)
                .then(data => {
                        if(data == 'true'){
                            Success();
                        }
                    }, error => {
                        Error(error);
                    }
                );

        }

        $scope.$watch('monthYear', function (newValue, oldValue) {
            if (oldValue != newValue) {
                var period = $scope.monthYear.split("-");
                $scope.year = period[0];
                $scope.month = period[1];
                getData($scope.year, $scope.month);
            }
        }, true);
    }]);

