/* eslint-disable @typescript-eslint/no-explicit-any */
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

import './style.css'

const Repackaging = () => {
  const spreadsheet = useRef<SpreadsheetComponent>(null)
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-8 col-md-8">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent ref={spreadsheet}>
          <SheetsDirective>
            <SheetDirective name="ROC Subdetail">
              <RangesDirective>
                <RangeDirective dataSource={[]}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
                  <CellsDirective>
                    <CellDirective value="id"></CellDirective>
                    <CellDirective value="subcontractNo"></CellDirective>
                    <CellDirective value="description"></CellDirective>
                    <CellDirective value="objectCode"></CellDirective>
                    <CellDirective value="subsidiaryCode"></CellDirective>
                    <CellDirective value="budget"></CellDirective>
                    <CellDirective value="quantity"></CellDirective>
                    <CellDirective value="currentAmount"></CellDirective>
                    <CellDirective value="postedAmount"></CellDirective>
                    <CellDirective value="rate"></CellDirective>
                    <CellDirective value="unit"></CellDirective>
                    <CellDirective value="excludeDefect"></CellDirective>
                    <CellDirective value="excludeLevy"></CellDirective>
                    <CellDirective value="forIvRollbackOnly"></CellDirective>
                    <CellDirective value="finalized"></CellDirective>
                    <CellDirective value="type"></CellDirective>
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

export default Repackaging
