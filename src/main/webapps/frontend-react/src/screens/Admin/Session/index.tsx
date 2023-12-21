import { useEffect, useRef, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { hideSpinner, showSpinner } from '@syncfusion/ej2-react-popups'
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

import NotificationModal from '../../../components/NotificationModal'
import { useGetSessionListQuery } from '../../../services'
import './style.css'
import dayjs from 'dayjs'

const Session = () => {
  const [notificationContent, setNotificationContent] = useState<string>('')
  const [visibleNotificationModal, setVisibleNotificationModal] =
    useState<boolean>(false)
  const [notificationMode, setNotificationMode] = useState<
    'Success' | 'Fail' | 'Warn'
  >('Success')

  const closeNotification = () => {
    setVisibleNotificationModal(false)
  }
  const {
    isLoading,
    isError,
    error,
    data: sessionDetails
  } = useGetSessionListQuery()

  useEffect(() => {
    const root = document.getElementById('root')!
    if (isLoading) {
      showSpinner(root)
    } else {
      hideSpinner(root)
    }
  }, [isLoading])

  useEffect(() => {
    setVisibleNotificationModal(true)
    setNotificationMode('Fail')
    if (error && 'status' in error) {
      setNotificationContent(error?.data.message)
    } else {
      setNotificationContent('Error!')
    }
  }, [isError])

  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const dataSource = sessionDetails?.map(item => {
    const {
      authType,
      sessionId,
      creationTime,
      lastAccessedTime,
      lastRequest,
      maxInactiveInterval
    } = item
    return {
      name: item.principal?.fullname,
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
      <NotificationModal
        mode={notificationMode}
        visible={visibleNotificationModal}
        dialogClose={closeNotification}
        content={notificationContent}
      />
    </div>
  )
}

export default Session
