/* eslint-disable @typescript-eslint/naming-convention */

/* eslint-disable @typescript-eslint/no-explicit-any */
import { useRef } from 'react'

import {
  ColumnDirective,
  ColumnsDirective,
  RangeDirective,
  RangesDirective,
  SheetDirective,
  SheetsDirective,
  SpreadsheetComponent
} from '@syncfusion/ej2-react-spreadsheet'

import './style.css'

// TODO 接口 system/getSCStandardTermsList POST
const StandardTermsMaintenance = () => {
  const spreadsheet = useRef<SpreadsheetComponent>(null)
  const data: any[] = [
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:53.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:53.000+00:00',
      systemStatus: 'ACTIVE',
      id: 3,
      formOfSubcontract: 'Consultancy Agreement',
      company: '00000',
      scPaymentTerm: 'QS4',
      scMaxRetentionPercent: 0.0,
      scInterimRetentionPercent: 0.0,
      scMOSRetentionPercent: 0.0,
      retentionType: 'No Retention'
    },
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:55.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:55.000+00:00',
      systemStatus: 'ACTIVE',
      id: 4,
      formOfSubcontract: 'Internal Trading',
      company: '00000',
      scPaymentTerm: 'QS1',
      scMaxRetentionPercent: 0.0,
      scInterimRetentionPercent: 0.0,
      scMOSRetentionPercent: 0.0,
      retentionType: 'No Retention'
    },
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:53.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:53.000+00:00',
      systemStatus: 'ACTIVE',
      id: 1,
      formOfSubcontract: 'Major',
      company: '00000',
      scPaymentTerm: 'QS2',
      scMaxRetentionPercent: 5.0,
      scInterimRetentionPercent: 10.0,
      scMOSRetentionPercent: 10.0,
      retentionType: 'Percentage - Revised SC Sum'
    },
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:53.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:53.000+00:00',
      systemStatus: 'ACTIVE',
      id: 2,
      formOfSubcontract: 'Minor',
      company: '00000',
      scPaymentTerm: 'QS2',
      scMaxRetentionPercent: 5.0,
      scInterimRetentionPercent: 10.0,
      scMOSRetentionPercent: 10.0,
      retentionType: 'Percentage - Revised SC Sum'
    }
  ]

  const dataSource = data.map(item => {
    const {
      company,
      formOfSubcontract,
      scPaymentTerm,
      scMaxRetentionPercent,
      scInterimRetentionPercent,
      scMOSRetentionPercent,
      retentionType
    } = item
    return {
      company,
      formOfSubcontract,
      scPaymentTerm,
      scMaxRetentionPercent,
      scInterimRetentionPercent,
      scMOSRetentionPercent,
      retentionType
    }
  })

  return (
    <div className="admin-container">
      <div className="admin-content">
        <SpreadsheetComponent
          ref={spreadsheet}
          allowOpen={true}
          openUrl="https://services.syncfusion.com/react/production/api/spreadsheet/open"
          allowSave={true}
          saveUrl="https://services.syncfusion.com/react/production/api/spreadsheet/save"
        >
          <SheetsDirective>
            <SheetDirective name="Attachment">
              <RangesDirective>
                <RangeDirective dataSource={dataSource}></RangeDirective>
              </RangesDirective>
              <ColumnsDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={180}></ColumnDirective>
                <ColumnDirective width={180}></ColumnDirective>
                <ColumnDirective width={180}></ColumnDirective>
                <ColumnDirective width={180}></ColumnDirective>
                <ColumnDirective width={180}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
    </div>
  )
}

export default StandardTermsMaintenance
