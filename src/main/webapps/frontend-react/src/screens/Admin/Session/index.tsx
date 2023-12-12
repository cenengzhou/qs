/* eslint-disable @typescript-eslint/naming-convention */

/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  CellDirective,
  CellsDirective,
  ColumnDirective,
  ColumnsDirective,
  RangeDirective,
  RangesDirective,
  RowDirective,
  RowsDirective,
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
        fullname: 'cenengzhou'
      },
      lastRequest: '2023-10-26T02:58:19.621+00:00',
      sessionId: '321EB5F772CB29602D8254C0D38C1584'
    }
  ]
  const dataSource = data.map(item => {
    const {
      authType,
      sessionId,
      creationTime,
      lastAccessedTime,
      lastRequest,
      maxInactiveInterval
    } = item
    return {
      name: item.principal.fullname,
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
          fontWeight: 'bold',
          verticalAlign: 'middle',
          textAlign: 'center'
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
            <SheetDirective
              name="Session"
              isProtected={true}
              protectSettings={{ selectCells: true }}
            >
              <RangesDirective>
                <RangeDirective
                  dataSource={dataSource}
                  startCell="A2"
                  showFieldAsHeader={false}
                ></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective index={0} height={40}>
                  <CellsDirective>
                    <CellDirective value="Name"></CellDirective>
                    <CellDirective value="AuthType"></CellDirective>
                    <CellDirective value="Session Id"></CellDirective>
                    <CellDirective value="Creation Time"></CellDirective>
                    <CellDirective value="Last Accessed Time"></CellDirective>
                    <CellDirective value="Last Request"></CellDirective>
                    <CellDirective value="Max Inactive Interval"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
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
