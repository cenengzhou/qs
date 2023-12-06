/* eslint-disable @typescript-eslint/naming-convention */

/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
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
import dayjs from 'dayjs'

// TODO 接口 service/system/GetSessionList POST
const Session = () => {
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)
  const data: any[] = [
    {
      authType: 'LDAP',
      creationTime: 1698219512026,
      lastAccessedTime: 1698289099682,
      maxInactiveInterval: 60000,
      principal: {
        username: 'cenengzhou'
      },
      lastRequest: '2023-10-26T02:58:19.621+00:00',
      sessionId: '321EB5F772CB29602D8254C0D38C1584'
    }
  ]
  const dataSource = data.map(item => {
    const {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      principal,
      authType,
      sessionId,
      creationTime,
      lastAccessedTime,
      lastRequest,
      maxInactiveInterval
    } = item
    return {
      name: item.principal.username,
      authType,
      sessionId,
      creationTime: dayjs(creationTime).format('DD/MM/YYYY HH:MM'),
      lastAccessedTime: dayjs(lastAccessedTime).format('DD/MM/YYYY HH:MM'),
      lastRequest: dayjs(lastRequest).format('DD/MM/YYYY HH:MM'),
      maxInactiveInterval
    }
  })

  useEffect(() => {
    const spreadsheet = spreadsheetRef.current
    if (spreadsheet) {
      spreadsheet.cellFormat(
        {
          backgroundColor: '#1E88E5',
          color: '#F5F5F5',
          fontSize: '14px',
          fontWeight: 'bold'
        },
        'A1:G1'
      )
    }
  }, [])

  return (
    <div className="admin-container">
      <div className="admin-content">
        <SpreadsheetComponent
          ref={spreadsheetRef}
          allowOpen={true}
          openUrl="https://services.syncfusion.com/react/production/api/spreadsheet/open"
          allowSave={true}
          saveUrl="https://services.syncfusion.com/react/production/api/spreadsheet/save"
        >
          <SheetsDirective>
            <SheetDirective name="Session">
              <RangesDirective>
                <RangeDirective dataSource={dataSource}></RangeDirective>
              </RangesDirective>
              <ColumnsDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={250}></ColumnDirective>
                <ColumnDirective width={130}></ColumnDirective>
                <ColumnDirective width={130}></ColumnDirective>
                <ColumnDirective width={130}></ColumnDirective>
                <ColumnDirective width={130}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
      <div className="row">
        <ButtonComponent cssClass="e-info full-btn" iconCss="e-icons e-save-2">
          Expire Selected
        </ButtonComponent>
      </div>
    </div>
  )
}

export default Session
