mainApp.controller('JobRocHistoryCtrl', ['$scope', 'rocService', '$uibModalInstance', '$cookies', 'modalService', '$state', 'GlobalParameter', '$q', 'modalStatus', 'modalParam',
    function ($scope, rocService, $uibModalInstance, $cookies, modalService, $state, GlobalParameter, $q, modalStatus, modalParam) {
        $scope.GlobalParameter = GlobalParameter;

        $scope.jobNo = $cookies.get("jobNo");

        $scope.selection = modalParam;

        rocService.getRocHistory($scope.selection.id).then(function (data) {
            if (data && data.length > 0)
                $scope.gridOptions.data = data;
            else
                modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', 'Failed to get roc history');
        });

        if ($scope.selection.detailId) {
            rocService.getRocDetailHistory($scope.selection.detailId).then(function (data) {
                if (data && data.length > 0)
                    $scope.detailGridOptions.data = data;
                else
                    modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', 'Failed to get roc detail history');
            });
        }

        $scope.active = 1;


        $scope.gridOptions = {
            enableSorting: true,
            enableFiltering: true,
            enableColumnResizing: true,
            enableGridMenu: true,
            enableColumnMoving: true,
            enableCellEditOnFocus: false,
            allowCellFocus: false,
            showGridFooter: false,
            showColumnFooter: true,
            treeRowHeaderAlwaysVisible: false,
            enableRowHeaderSelection: false,
            exporterMenuPdf: false,
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
            },

            columnDefs: [
                {
                    field: 'lastModifiedDate',
                    displayName: 'Last Modified Date',
                    cellFilter: 'date:"yyyy-MMM-dd HH:mm:ss"',
                    width: 150,
                    visible: true
                },
                {field: 'lastModifiedUser', displayName: 'Last Modified By', width: 120, visible: true},
                {field: 'systemStatus', displayName: 'System Status', width: 120, visible: true},
                {field: 'id', displayName: 'Id', width: 100, visible: false},
                {field: 'projectNo', displayName: 'Project No', width: 100, visible: false},
                {field: 'projectRef', displayName: 'Project R&O Ref.', width: 100, visible: true},
                {field: 'rocCategory', displayName: 'ROC Category', width: 100, visible: true},
                {field: 'classification', displayName: 'Classification', width: 100, visible: true},
                {field: 'impact', displayName: 'Impact', width: 150, visible: true},
                {field: 'status', displayName: 'Status', width: 100, visible: true},
                {field: 'createdUser', displayName: 'Created By', width: 100, visible: false},
                {
                    field: 'createdDate',
                    displayName: 'Created Date',
                    cellFilter: 'date:"yyyy-MMM-dd HH:mm:ss"',
                    width: 150,
                    visible: false
                },
                {field: 'description', displayName: 'Description', visible: true,
                    cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>',}

            ]

        };

        $scope.detailGridOptions = {
            enableSorting: true,
            enableFiltering: true,
            enableColumnResizing: true,
            enableGridMenu: true,
            enableColumnMoving: true,
            enableCellEditOnFocus: false,
            allowCellFocus: false,
            showGridFooter: false,
            showColumnFooter: true,
            treeRowHeaderAlwaysVisible: false,
            enableRowHeaderSelection: false,
            exporterMenuPdf: false,
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
            },

            columnDefs: [
                {
                    field: 'lastModifiedDate',
                    displayName: 'Last Modified Date',
                    cellFilter: 'date:"yyyy-MMM-dd HH:mm:ss"',
                    width: 150,
                    visible: true
                },
                {field: 'lastModifiedUser', displayName: 'Last Modified By', width: 120, visible: true},
                {field: 'systemStatus', displayName: 'System Status', width: 120, visible: true},
                {field: 'id', displayName: 'Id', width: 100, visible: false},
                {field: 'amountBest', displayName: 'Best Case', width: 100, cellClass: 'text-right',
                cellFilter: 'number:0'},
                {field: 'amountExpected', displayName: 'Expected Case', width: 100, cellClass: 'text-right',
                    cellFilter: 'number:0'},
                {field: 'amountWorst', displayName: 'Worst Case', width: 100, cellClass: 'text-right',
                    cellFilter: 'number:0'},
                {field: 'createdUser', displayName: 'Created By', width: 100, visible: false},
                {
                    field: 'createdDate',
                    displayName: 'Created Date',
                    cellFilter: 'date:"yyyy-MMM-dd HH:mm:ss"',
                    width: 150,
                    visible: false
                },
                {field: 'remarks', displayName: 'Remarks', visible: true,
                    cellTemplate: '<div class="ui-grid-cell-contents ui-grid-cell-contents-break">{{ MODEL_COL_FIELD }}</div>'},
                {field: 'rocDetail.year', displayName: 'Year', width: 100, visible: false},
                {field: 'rocDetail.month', displayName: 'Month', width: 100, visible: false}

            ]

        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss("cancel");
        };

    }]);
