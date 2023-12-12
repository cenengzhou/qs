import { useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'
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

const MonthlyMovement = () => {
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
        <div className="col-lg-2 col-md-2">
          <NumericTextBoxComponent
            placeholder="Year"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="#"
          />
        </div>
        <div className="col-lg-2 col-md-2">
          <NumericTextBoxComponent
            placeholder="Month"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="#"
          />
        </div>
        <div className="col-lg-2 col-md-2">
          <DropDownListComponent
            dataSource={[
              { text: '1111', value: 1 },
              { text: '2222', value: 2 }
            ]}
            cssClass="e-outline"
            floatLabelType="Always"
            showClearButton
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent ref={spreadsheet}>
          <SheetsDirective>
            <SheetDirective name="Monthly Movement">
              <RangesDirective>
                <RangeDirective dataSource={[]}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
                  <CellsDirective>
                    <CellDirective value="ID"></CellDirective>
                    <CellDirective value="Job Number"></CellDirective>
                    <CellDirective value="Year"></CellDirective>
                    <CellDirective value="Month"></CellDirective>
                    <CellDirective value="Type"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                    <CellDirective value="Forecast Flag"></CellDirective>
                    <CellDirective value="Amount"></CellDirective>
                    <CellDirective value="Critical Program Date"></CellDirective>
                    <CellDirective value="Date Button"></CellDirective>
                    <CellDirective value="Explanation"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
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

export default MonthlyMovement
