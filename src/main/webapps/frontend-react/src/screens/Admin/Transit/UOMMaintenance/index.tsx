/* eslint-disable @typescript-eslint/no-explicit-any */

/* eslint-disable @typescript-eslint/naming-convention */
import {
  ColumnChooser,
  ColumnDirective,
  ColumnMenu,
  ColumnsDirective,
  ExcelExport,
  Filter,
  GridComponent,
  Inject,
  Sort,
  Toolbar,
  ToolbarItems
} from '@syncfusion/ej2-react-grids'

import './style.css'

const UOMMaintenance = () => {
  const toolbar: ToolbarItems[] = ['ExcelExport', 'CsvExport', 'ColumnChooser']
  const data = [
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 1,
      causewayUom: '%',
      jdeUom: '%'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 2,
      causewayUom: '%.',
      jdeUom: '%'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 3,
      causewayUom: 'AM',
      jdeUom: 'AM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2013-01-17T09:12:08.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2013-01-17T09:12:08.000+00:00',
      systemStatus: 'ACTIVE',
      id: 83,
      causewayUom: 'BAG',
      jdeUom: 'BG'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 28,
      causewayUom: 'CUBE',
      jdeUom: 'M3'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 4,
      causewayUom: 'DAY',
      jdeUom: 'DY'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 5,
      causewayUom: 'FLOOR',
      jdeUom: 'FL'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 6,
      causewayUom: 'H',
      jdeUom: 'HR'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 10,
      causewayUom: 'HA',
      jdeUom: 'HT'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 7,
      causewayUom: 'HOUR',
      jdeUom: 'HR'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 11,
      causewayUom: 'ITEM',
      jdeUom: 'IT'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 12,
      causewayUom: 'KG',
      jdeUom: 'KG'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 13,
      causewayUom: 'KG.',
      jdeUom: 'KG'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 14,
      causewayUom: 'KILLO',
      jdeUom: 'KG'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 15,
      causewayUom: 'KILO',
      jdeUom: 'KG'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-11-10T01:52:18.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-11-10T01:52:18.000+00:00',
      systemStatus: 'ACTIVE',
      id: 61,
      causewayUom: 'KM',
      jdeUom: 'KM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 17,
      causewayUom: 'LANE-M',
      jdeUom: 'LM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 22,
      causewayUom: 'LITRE',
      jdeUom: 'LT'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 23,
      causewayUom: 'LLITE',
      jdeUom: 'LT'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 16,
      causewayUom: 'LOAD',
      jdeUom: 'LD'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 21,
      causewayUom: 'LOT',
      jdeUom: 'LO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 18,
      causewayUom: 'M',
      jdeUom: 'LM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 24,
      causewayUom: 'M2',
      jdeUom: 'M2'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 29,
      causewayUom: 'M3',
      jdeUom: 'M3'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2013-01-17T09:12:08.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2013-01-17T09:12:08.000+00:00',
      systemStatus: 'ACTIVE',
      id: 82,
      causewayUom: 'M3/DAY',
      jdeUom: 'DY'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 30,
      causewayUom: 'MANMTH',
      jdeUom: 'MM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 38,
      causewayUom: 'MIN',
      jdeUom: 'MU'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 31,
      causewayUom: 'MONTH',
      jdeUom: 'MO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 32,
      causewayUom: 'MONTHS',
      jdeUom: 'MO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 33,
      causewayUom: 'MTH',
      jdeUom: 'MO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 34,
      causewayUom: 'MTH.',
      jdeUom: 'MO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 39,
      causewayUom: 'NIGHT',
      jdeUom: 'NI'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 40,
      causewayUom: 'NO',
      jdeUom: 'NO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 41,
      causewayUom: 'NO.',
      jdeUom: 'NO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 42,
      causewayUom: 'NOS.',
      jdeUom: 'NO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 43,
      causewayUom: 'NR',
      jdeUom: 'NO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 8,
      causewayUom: 'NR-HR',
      jdeUom: 'HR'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 35,
      causewayUom: 'NR-MTH',
      jdeUom: 'MO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 54,
      causewayUom: 'NR-WK',
      jdeUom: 'WK'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 44,
      causewayUom: 'NR.',
      jdeUom: 'NO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2013-01-17T09:12:08.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2013-01-17T09:12:08.000+00:00',
      systemStatus: 'ACTIVE',
      id: 81,
      causewayUom: 'NR/MTH',
      jdeUom: 'MO'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 45,
      causewayUom: 'RIG',
      jdeUom: 'RG'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 46,
      causewayUom: 'ROLL',
      jdeUom: 'RL'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 19,
      causewayUom: 'RUN',
      jdeUom: 'LM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 20,
      causewayUom: 'RUN.',
      jdeUom: 'LM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 47,
      causewayUom: 'SET',
      jdeUom: 'ST'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 48,
      causewayUom: 'SUM',
      jdeUom: 'SU'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 25,
      causewayUom: 'SUP',
      jdeUom: 'M2'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 26,
      causewayUom: 'SUP.',
      jdeUom: 'M2'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 27,
      causewayUom: 'SUPER',
      jdeUom: 'M2'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 36,
      causewayUom: 'T',
      jdeUom: 'MT'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 9,
      causewayUom: 'TEAM-H',
      jdeUom: 'HR'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 51,
      causewayUom: 'TEL-MT',
      jdeUom: 'UM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 49,
      causewayUom: 'TON',
      jdeUom: 'TN'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 37,
      causewayUom: 'TONNE',
      jdeUom: 'MT'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 50,
      causewayUom: 'TRIP',
      jdeUom: 'TP'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-11-10T01:52:18.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-11-10T01:52:18.000+00:00',
      systemStatus: 'ACTIVE',
      id: 62,
      causewayUom: 'UN',
      jdeUom: 'UN'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 52,
      causewayUom: 'UNIT-M',
      jdeUom: 'UM'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 55,
      causewayUom: 'VEH-WK',
      jdeUom: 'WK'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 53,
      causewayUom: 'VISIT',
      jdeUom: 'VS'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 56,
      causewayUom: 'WEEK',
      jdeUom: 'WK'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:40.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:40.000+00:00',
      systemStatus: 'ACTIVE',
      id: 57,
      causewayUom: 'YEAR',
      jdeUom: 'YR'
    }
  ]
  return (
    <div className="admin-container flex-row">
      <div className="col-sm-12 col-md-4 padding10">
        <div className="e-card transit-card">
          <div className="e-card-header bg-info">
            <div className="e-card-header-caption">
              <div className="e-card-header-title transit-title">
                <span className="e-icons e-circle-info"></span>
                <span>Unit code matching</span>
              </div>
            </div>
          </div>
          <div className="e-card-content text-content transit-content">
            <div className="text-border">
              <h3>The Unified Code for Units of Measure (UCUM)</h3>
              <p>
                is a code system intended to include all units of measures being
                contemporarily used in international science, engineering, and
                business. The purpose is to facilitate unambiguous electronic
                communication of quantities together with their units. The focus
                is on electronic communication, as opposed to communication
                between humans. A typical application of The Unified Code for
                Units of Measure are electronic data interchange (EDI)
                protocols, but there is nothing that prevents it from being used
                in other types of machine communication. UCUM is based on the
                ISO 80000: 2009 Quantities and Units standards series that
                specify the use of System International (SI) units in
                publications. ISO 80000 standards series is developed by
                Technical Committee 12, International Organization of
                Standardization (ISO/TC12) Quantities and units in co-operation
                with Technical Committee 25, International Electrotechnical
                Committee (IEC/TC 25).
              </p>
            </div>
          </div>
        </div>
      </div>
      <div className="admin-content col-sm-12 col-md-8 padding10">
        <GridComponent
          dataSource={data}
          width="100%"
          height="100%"
          allowExcelExport
          toolbar={toolbar}
          allowTextWrap={true}
          showColumnChooser
          showColumnMenu
          allowFiltering
          allowSorting
          filterSettings={{ type: 'Menu' }}
          cssClass="no-margin-right"
        >
          <ColumnsDirective>
            <ColumnDirective
              field="causewayUom"
              headerText="Causeway UOM"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="jdeUom"
              headerText="JDE UOM"
              width="120"
            ></ColumnDirective>
          </ColumnsDirective>
          <Inject
            services={[
              ExcelExport,
              Toolbar,
              ColumnChooser,
              ColumnMenu,
              Filter,
              Sort
            ]}
          />
        </GridComponent>
      </div>
    </div>
  )
}

export default UOMMaintenance
