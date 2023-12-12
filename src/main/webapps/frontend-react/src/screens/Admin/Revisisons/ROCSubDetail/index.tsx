/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
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

const RocSubDetail = () => {
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)
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
        'A1:J1'
      )
    }
  }, [])

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Item No (Optional)"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <DatePicker placeholder="Date" />
        </div>

        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
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
              name="ROC Subdetail"
              isProtected={true}
              protectSettings={{ selectCells: true }}
            >
              <RangesDirective>
                {/* <RangeDirective
                  dataSource={[]}
                  startCell="A2"
                  showFieldAsHeader={false}
                ></RangeDirective> */}
                <RangeDirective
                  template={dropDownList}
                  address="J2:J9"
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
                  isLocked={!useHasRole('ROLE_QS_QS_ADM')}
                  width={130}
                ></ColumnDirective>
                <ColumnDirective
                  isLocked={!useHasRole('ROLE_QS_QS_ADM')}
                  width={100}
                ></ColumnDirective>
                <ColumnDirective
                  isLocked={!useHasRole('ROLE_QS_QS_ADM')}
                  width={80}
                ></ColumnDirective>
                <ColumnDirective
                  isLocked={!useHasRole('ROLE_QS_QS_ADM')}
                  width={80}
                ></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective isLocked={false} width={180}></ColumnDirective>
                <ColumnDirective
                  isLocked={!useHasRole('ROLE_QS_QS_ADM')}
                  width={180}
                ></ColumnDirective>
                <ColumnDirective isLocked={false} width={180}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
    </div>
  )
}
const dropDownList = (): Node => {
  const selete = document.createElement('input')
  // const experience = [
  //   '0 - 1 year',
  //   '1 - 3 years',
  //   '3 - 5 years',
  //   '5 - 10 years'
  // ]
  // return (
  //   <DropDownListComponent
  //     placeholder="Experience"
  //     dataSource={experience}
  //   ></DropDownListComponent>
  // )
  return selete
}
export default RocSubDetail
