mainApp.controller('AdminTransitUOMMaintenanceCtrl', [ '$scope', '$rootScope',
		function($scope, $rootScope) {

			this.gridData = [ {
				'causewayUom' : '%',
				'jdeUom' : '%'
			}, {
				'causewayUom' : '%',
				'jdeUom' : '%'
			}, {
				'causewayUom' : '%',
				'jdeUom' : '%'
			}, {
				'causewayUom' : '%.',
				'jdeUom' : '%'
			}, {
				'causewayUom' : '%.',
				'jdeUom' : '%'
			}, {
				'causewayUom' : 'AM',
				'jdeUom' : 'AM'
			}, {
				'causewayUom' : 'AM',
				'jdeUom' : 'AM'
			}, {
				'causewayUom' : 'AM',
				'jdeUom' : 'AM'
			}, {
				'causewayUom' : 'BAG',
				'jdeUom' : 'BG'
			}, {
				'causewayUom' : 'BAG',
				'jdeUom' : 'BG'
			}, {
				'causewayUom' : 'BAG',
				'jdeUom' : 'BG'
			}, {
				'causewayUom' : 'CUBE',
				'jdeUom' : 'M3'
			}, {
				'causewayUom' : 'CUBE',
				'jdeUom' : 'M3'
			}, {
				'causewayUom' : 'CUBE',
				'jdeUom' : 'M3'
			}, {
				'causewayUom' : 'DAY',
				'jdeUom' : 'DY'
			}, {
				'causewayUom' : 'DAY',
				'jdeUom' : 'DY'
			}, {
				'causewayUom' : 'DAY',
				'jdeUom' : 'DY'
			}, {
				'causewayUom' : 'FLOOR',
				'jdeUom' : 'FL'
			}, {
				'causewayUom' : 'FLOOR',
				'jdeUom' : 'FL'
			}, {
				'causewayUom' : 'FLOOR',
				'jdeUom' : 'FL'
			}, {
				'causewayUom' : 'H',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'H',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'H',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'HA',
				'jdeUom' : 'HT'
			}, {
				'causewayUom' : 'HA',
				'jdeUom' : 'HT'
			}, {
				'causewayUom' : 'HA',
				'jdeUom' : 'HT'
			}, {
				'causewayUom' : 'HOUR',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'HOUR',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'HOUR',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'ITEM',
				'jdeUom' : 'IT'
			}, {
				'causewayUom' : 'ITEM',
				'jdeUom' : 'IT'
			}, {
				'causewayUom' : 'ITEM',
				'jdeUom' : 'IT'
			}, {
				'causewayUom' : 'JK',
				'jdeUom' : 'RS'
			}, {
				'causewayUom' : 'JK',
				'jdeUom' : 'RS'
			}, {
				'causewayUom' : 'KG',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KG',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KG',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KG.',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KG.',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KG.',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KILLO',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KILLO',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KILLO',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KILO',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KILO',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KILO',
				'jdeUom' : 'KG'
			}, {
				'causewayUom' : 'KM',
				'jdeUom' : 'KM'
			}, {
				'causewayUom' : 'KM',
				'jdeUom' : 'KM'
			}, {
				'causewayUom' : 'KM',
				'jdeUom' : 'KM'
			}, {
				'causewayUom' : 'KM',
				'jdeUom' : 'KM'
			}, {
				'causewayUom' : 'LANE-M',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'LANE-M',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'LANE-M',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'LITRE',
				'jdeUom' : 'LT'
			}, {
				'causewayUom' : 'LITRE',
				'jdeUom' : 'LT'
			}, {
				'causewayUom' : 'LLITE',
				'jdeUom' : 'LT'
			}, {
				'causewayUom' : 'LLITE',
				'jdeUom' : 'LT'
			}, {
				'causewayUom' : 'LLITE',
				'jdeUom' : 'LT'
			}, {
				'causewayUom' : 'LOAD',
				'jdeUom' : 'LD'
			}, {
				'causewayUom' : 'LOAD',
				'jdeUom' : 'LD'
			}, {
				'causewayUom' : 'LOAD',
				'jdeUom' : 'LD'
			}, {
				'causewayUom' : 'LOT',
				'jdeUom' : 'LO'
			}, {
				'causewayUom' : 'LOT',
				'jdeUom' : 'LO'
			}, {
				'causewayUom' : 'LOT',
				'jdeUom' : 'LO'
			}, {
				'causewayUom' : 'M',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'M',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'M',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'M2',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'M2',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'M2',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'M3',
				'jdeUom' : 'M3'
			}, {
				'causewayUom' : 'M3',
				'jdeUom' : 'M3'
			}, {
				'causewayUom' : 'M3',
				'jdeUom' : 'M3'
			}, {
				'causewayUom' : 'M3/DAY',
				'jdeUom' : 'DY'
			}, {
				'causewayUom' : 'M3/DAY',
				'jdeUom' : 'DY'
			}, {
				'causewayUom' : 'M3/DAY',
				'jdeUom' : 'DY'
			}, {
				'causewayUom' : 'MANMTH',
				'jdeUom' : 'MM'
			}, {
				'causewayUom' : 'MANMTH',
				'jdeUom' : 'MM'
			}, {
				'causewayUom' : 'MANMTH',
				'jdeUom' : 'MM'
			}, {
				'causewayUom' : 'MIN',
				'jdeUom' : 'MU'
			}, {
				'causewayUom' : 'MIN',
				'jdeUom' : 'MU'
			}, {
				'causewayUom' : 'MIN',
				'jdeUom' : 'MU'
			}, {
				'causewayUom' : 'MONTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MONTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MONTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MONTHS',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MONTHS',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MONTHS',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MTH.',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'MTH.',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'NIGHT',
				'jdeUom' : 'NI'
			}, {
				'causewayUom' : 'NIGHT',
				'jdeUom' : 'NI'
			}, {
				'causewayUom' : 'NIGHT',
				'jdeUom' : 'NI'
			}, {
				'causewayUom' : 'NO',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NO',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NO',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NO.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NO.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NO.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NOS.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NOS.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NOS.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR-HR',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'NR-HR',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'NR-HR',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'NR-MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'NR-MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'NR-MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'NR-WK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'NR-WK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'NR-WK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'NR.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR.',
				'jdeUom' : 'NO'
			}, {
				'causewayUom' : 'NR/MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'NR/MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'NR/MTH',
				'jdeUom' : 'MO'
			}, {
				'causewayUom' : 'RIG',
				'jdeUom' : 'RG'
			}, {
				'causewayUom' : 'RIG',
				'jdeUom' : 'RG'
			}, {
				'causewayUom' : 'RIG',
				'jdeUom' : 'RG'
			}, {
				'causewayUom' : 'ROLL',
				'jdeUom' : 'RL'
			}, {
				'causewayUom' : 'ROLL',
				'jdeUom' : 'RL'
			}, {
				'causewayUom' : 'ROLL',
				'jdeUom' : 'RL'
			}, {
				'causewayUom' : 'RUN',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'RUN',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'RUN',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'RUN.',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'RUN.',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'RUN.',
				'jdeUom' : 'LM'
			}, {
				'causewayUom' : 'SET',
				'jdeUom' : 'ST'
			}, {
				'causewayUom' : 'SET',
				'jdeUom' : 'ST'
			}, {
				'causewayUom' : 'SET',
				'jdeUom' : 'ST'
			}, {
				'causewayUom' : 'SUM',
				'jdeUom' : 'SU'
			}, {
				'causewayUom' : 'SUM',
				'jdeUom' : 'SU'
			}, {
				'causewayUom' : 'SUM',
				'jdeUom' : 'SU'
			}, {
				'causewayUom' : 'SUP',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUP',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUP',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUP.',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUP.',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUP.',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUPER',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUPER',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'SUPER',
				'jdeUom' : 'M2'
			}, {
				'causewayUom' : 'T',
				'jdeUom' : 'MT'
			}, {
				'causewayUom' : 'T',
				'jdeUom' : 'MT'
			}, {
				'causewayUom' : 'T',
				'jdeUom' : 'MT'
			}, {
				'causewayUom' : 'TEAM-H',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'TEAM-H',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'TEAM-H',
				'jdeUom' : 'HR'
			}, {
				'causewayUom' : 'TEL-MT',
				'jdeUom' : 'UM'
			}, {
				'causewayUom' : 'TEL-MT',
				'jdeUom' : 'UM'
			}, {
				'causewayUom' : 'TEL-MT',
				'jdeUom' : 'UM'
			}, {
				'causewayUom' : 'TON',
				'jdeUom' : 'TN'
			}, {
				'causewayUom' : 'TON',
				'jdeUom' : 'TN'
			}, {
				'causewayUom' : 'TON',
				'jdeUom' : 'TN'
			}, {
				'causewayUom' : 'TONNE',
				'jdeUom' : 'MT'
			}, {
				'causewayUom' : 'TONNE',
				'jdeUom' : 'MT'
			}, {
				'causewayUom' : 'TONNE',
				'jdeUom' : 'MT'
			}, {
				'causewayUom' : 'TRIP',
				'jdeUom' : 'TP'
			}, {
				'causewayUom' : 'TRIP',
				'jdeUom' : 'TP'
			}, {
				'causewayUom' : 'TRIP',
				'jdeUom' : 'TP'
			}, {
				'causewayUom' : 'UN',
				'jdeUom' : 'UN'
			}, {
				'causewayUom' : 'UN',
				'jdeUom' : 'UN'
			}, {
				'causewayUom' : 'UN',
				'jdeUom' : 'UN'
			}, {
				'causewayUom' : 'UNIT-M',
				'jdeUom' : 'UM'
			}, {
				'causewayUom' : 'UNIT-M',
				'jdeUom' : 'UM'
			}, {
				'causewayUom' : 'UNIT-M',
				'jdeUom' : 'UM'
			}, {
				'causewayUom' : 'VEH-WK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'VEH-WK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'VEH-WK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'VISIT',
				'jdeUom' : 'VS'
			}, {
				'causewayUom' : 'VISIT',
				'jdeUom' : 'VS'
			}, {
				'causewayUom' : 'VISIT',
				'jdeUom' : 'VS'
			}, {
				'causewayUom' : 'WEEK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'WEEK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'WEEK',
				'jdeUom' : 'WK'
			}, {
				'causewayUom' : 'YEAR',
				'jdeUom' : 'YR'
			}, {
				'causewayUom' : 'YEAR',
				'jdeUom' : 'YR'
			}, {
				'causewayUom' : 'YEAR',
				'jdeUom' : 'YR'
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
					field : 'causewayUom',
					displayName : "Causeway UOM",
					enableCellEdit : false
				}, {
					field : 'jdeUom',
					displayName : "JDE UOM",
					enableCellEdit : false
				} ]
			};
		} ]);
