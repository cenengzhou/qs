mainApp.controller('AdminTransitResourceCodeMaintenanceCtrl', [ '$scope',
		'$rootScope', function($scope, $rootScope) {
			this.gridData = [ {
				'matchingType' : 'BS',
				'resourceCode' : '10101',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10102',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10103',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10104',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10105',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10106',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10107',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10108',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			}, {
				'matchingType' : 'BS',
				'resourceCode' : '10109',
				'objectCode' : '110199',
				'subsidiaryCode' : '19999999'
			} ];
			
			this.gridOptions = {
				enableFiltering : true,
				enableColumnResizing : true,
				enableGridMenu : true,
				enableRowSelection : true,
				enableSelectAll : true,
				enableFullRowSelection : false,
				multiSelect : true,
				showGridFooter : true,
				enableCellEditOnFocus : false,
				paginationPageSizes : [ 50 ],
				paginationPageSize : 50,
				allowCellFocus : false,
				enableCellSelection : false,
				data : this.gridData,
				isRowSelectable : function(row) {
					if (row.entity.sessionId === $rootScope.sessionId) {
						return false;
					}
					return true;
				},
				columnDefs : [ {
					field : 'matchingType',
					displayName : "Matching Type",
					enableCellEdit : false
				}, {
					field : 'resourceCode',
					displayName : "Resource Code",
					enableCellEdit : false
				}, {
					field : 'objectCode',
					displayName : "Object Code",
					enableCellEdit : false
				}, {
					field : 'subsidiaryCode',
					displayName : "Subsidiary Code",
					enableCellEdit : false
				} ]
			};
		} ]);
