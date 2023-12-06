import { useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
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

const RocSubDetail = () => {
  const spreadsheet = useRef<SpreadsheetComponent>(null)
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
          ref={spreadsheet}
          allowOpen={true}
          openUrl="https://services.syncfusion.com/react/production/api/spreadsheet/open"
          allowSave={true}
          saveUrl="https://services.syncfusion.com/react/production/api/spreadsheet/save"
        >
          <SheetsDirective>
            <SheetDirective name="ROC Subdetail">
              <RangesDirective>
                <RangeDirective dataSource={[]}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
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
                <ColumnDirective width={130}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={280}></ColumnDirective>
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

export default RocSubDetail
