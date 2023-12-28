/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useRef, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { ChangedEventArgs as calendarsChangedEventArgs } from '@syncfusion/ej2-react-calendars'
import { InputEventArgs, TextBoxComponent } from '@syncfusion/ej2-react-inputs'
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

import DatePicker from '../../../../components/DatePicker'
import { useHasRole } from '../../../../hooks/useHasRole'
import dayjs from 'dayjs'

const RocSubDetail = () => {
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)
  const isQsAdm = useHasRole('ROLE_QS_QS_ADM')

  const [jobNo, setJobNo] = useState<string>('')
  const [itemNo, setItemNo] = useState<string>('')
  const [date, setDate] = useState<string>('')
  useEffect(() => {
    const spreadsheet = spreadsheetRef.current
    spreadsheetRef.current?.refresh()

    if (spreadsheet) {
      spreadsheet.cellFormat(
        {
          backgroundColor: '#1E88E5',
          color: '#F5F5F5',
          fontWeight: 'bold',
          verticalAlign: 'middle',
          textAlign: 'center'
        },
        'A1:J1'
      )
    }
  }, [isQsAdm])

  const updateJobNo = (value?: string) => {
    setJobNo(value ?? '')
  }
  const updateItemNo = (value?: string) => {
    setItemNo(value ?? '')
  }
  const changeDate = (e: calendarsChangedEventArgs) => {
    setDate(dayjs(e.value).format('YYYY-MM-DD') ?? new Date())
  }

  const search = async () => {}

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={jobNo}
            input={(value: InputEventArgs) => {
              updateJobNo(value.value)
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Item No (Optional)"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={itemNo}
            input={(value: InputEventArgs) => {
              updateItemNo(value.value)
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <DatePicker placeholder="Date" value={date} onChange={changeDate} />
        </div>

        <div className="col-lg-3 col-md-3">
          <ButtonComponent
            cssClass="e-info full-btn"
            disabled={!(jobNo && date)}
            onClick={search}
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent
          allowDataValidation={true}
          ref={spreadsheetRef}
          allowOpen={true}
          openUrl="https://services.syncfusion.com/react/production/api/spreadsheet/open"
          allowSave={true}
          saveUrl="https://services.syncfusion.com/react/production/api/spreadsheet/save"
        >
          <SheetsDirective>
            <SheetDirective
              name="ROC Subdetail"
              isProtected={true}
              protectSettings={{ selectCells: true }}
            >
              <RangesDirective>
                <RangeDirective
                  dataSource={[]}
                  startCell="A2"
                  showFieldAsHeader={false}
                ></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective index={0} height={40}>
                  <CellsDirective>
                    <CellDirective value="Item No"></CellDirective>
                    <CellDirective value="Secondary Detail"></CellDirective>
                    <CellDirective value="Best Case"></CellDirective>
                    <CellDirective value="Realistic"></CellDirective>
                    <CellDirective value="Worst Case"></CellDirective>
                    <CellDirective value="Year"></CellDirective>
                    <CellDirective value="Month"></CellDirective>
                    <CellDirective value="Hyperlink"></CellDirective>
                    <CellDirective value="Remarks"></CellDirective>
                    <CellDirective value="System Status"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective
                  width={130}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={80}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={80}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective isLocked={false} width={180}></ColumnDirective>
                <ColumnDirective
                  width={180}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  validation={{
                    type: 'List',
                    value1: 'INACTIVE,ACTIVE',
                    ignoreBlank: false
                  }}
                  width={180}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
      <div className="row">
        <div className="col-lg-12 col-md-12">
          <ButtonComponent cssClass="e-info full-btn">
            Update Roc Subdetail
          </ButtonComponent>
        </div>
      </div>
    </div>
  )
}

export default RocSubDetail
