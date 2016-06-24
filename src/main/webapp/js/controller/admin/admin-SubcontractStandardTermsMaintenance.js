mainApp.controller('AdminSubcontractStandardTermsMaintenanceCtrl', [ '$scope',
		'$rootScope', function($scope, $rootScope) {

			this.gridData = [ {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}, {
				'systemCode' : '59',
				'company' : '00001',
				'paymentTerm' : ' QS2 - Pay when Paid + 14 days',
				'maxRetention' : '5.00',
				'interimRetention' : '10.00',
				'mosRetention' : '10.00',
				'retentionType' : 'Percentage - Revised SC Sum',
				'reviewed' : 'Y'
			}
			];
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
					field : 'systemCode',
					displayName : "System Code",
					enableCellEdit : false
				}, {
					field : 'company',
					displayName : "Company",
					enableCellEdit : false
				}, {
					field : 'paymentTerm',
					displayName : "SC Payment Term",
					enableCellEdit : false
				}, {
					field : 'maxRetention',
					displayName : "SC Max Retention %",
					enableCellEdit : false
				}, {
					field : 'interimRetention',
					displayName : "SC Interim Retention %",
					enableCellEdit : false
				}, {
					field : 'mosRetention',
					displayName : "SC MOS Retention %",
					enableCellEdit : false
				}, {
					field : 'retentionType',
					displayName : "Retention Type",
					enableCellEdit : false
				}, {
					field : 'reviewed',
					displayName : "Reviewed by Finance",
					enableCellEdit : false
				} ]
			};
		} ]);
