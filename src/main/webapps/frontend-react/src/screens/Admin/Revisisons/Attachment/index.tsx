/* eslint-disable @typescript-eslint/no-explicit-any */
import { useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'
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

const Attachment = () => {
  const spreadsheet = useRef<SpreadsheetComponent>(null)
  const data: any[] = [
    {
      usernameCreated: 'victorhui',
      dateCreated: '2020-11-12T03:54:14.000+00:00',
      usernameLastModified: 'victorhui',
      dateLastModified: '2020-11-12T03:54:14.000+00:00',
      id: 412114,
      idTable: 75852,
      nameTable: 'SUBCONTRACT',
      noSequence: 1,
      typeDocument: '5',
      nameFile: 'Subcontract Tender Analysis Approved.pdf',
      pathFile: '13892\\Subcontract Tender Analysis Approved.pdf',
      text: null
    },
    {
      usernameCreated: 'victorhui',
      dateCreated: '2020-11-27T09:01:51.000+00:00',
      usernameLastModified: 'victorhui',
      dateLastModified: '2020-11-27T09:01:51.000+00:00',
      id: 413785,
      idTable: 75852,
      nameTable: 'SUBCONTRACT',
      noSequence: 2,
      typeDocument: '5',
      nameFile: 'Subcontract Document Executed by Subcontractor.pdf',
      pathFile: '13892\\Subcontract Document Executed by Subcontractor.pdf',
      text: undefined
    }
  ]
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <DropDownListComponent
            dataSource={['ADDENDUM', 'PAYMENT', 'MAIN_CERT']}
            cssClass="e-outline"
            floatLabelType="Always"
            showClearButton
            placeholder="Table Name"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
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
            <SheetDirective name="Attachment">
              <RangesDirective>
                <RangeDirective dataSource={data}></RangeDirective>
              </RangesDirective>
              <ColumnsDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
    </div>
  )
}

export default Attachment
