/* eslint-disable @typescript-eslint/naming-convention */

/* eslint-disable @typescript-eslint/no-explicit-any */
import { useRef } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
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

const TenderDetail = () => {
  const data = [
    {
      id: 172430,
      tender: {
        vendorNo: 426461,
        jobNo: '90013',
        packageNo: '1070'
      },
      sequenceNo: 1,
      billItem: null,
      description: 'Cleaning Services',
      quantity: 1.0,
      rateBudget: 50.0,
      rateSubcontract: 50.0,
      objectCode: '140299',
      subsidiaryCode: '49809999',
      lineType: 'BQ',
      unit: 'AM',
      amountSubcontract: 358700.0,
      amountForeign: 0.0
    },
    {
      id: 172431,
      tender: {
        vendorNo: 426461,
        jobNo: '90013',
        packageNo: '1070'
      },
      sequenceNo: 2,
      resourceNo: 313762,
      billItem: null,
      description: 'Provide Cleaning Services to Fan Coil Unit at TKO Office',
      quantity: 358700.0,
      rateBudget: 1.0,
      rateSubcontract: 1.0,
      objectCode: '140299',
      subsidiaryCode: '49809999',
      lineType: 'BQ',
      unit: 'AM',
      remark: null,
      amountBudget: 358700.0,
      amountSubcontract: 358700.0,
      amountForeign: 0.0
    }
  ]

  const dataSource = data.map(item => {
    const {
      id,
      tender,
      sequenceNo,
      description,
      quantity,
      rateBudget,
      amountBudget,
      rateSubcontract,
      amountSubcontract,
      amountForeign,
      objectCode,
      subsidiaryCode,
      lineType,
      unit,
      billItem
    } = item
    return {
      id,
      jobNumber: tender.jobNo,
      subcontract: tender.packageNo,
      vender: tender.vendorNo,
      sequenceNo,
      description,
      quantity,
      rateBudget,
      amountBudget,
      rateSubcontract,
      amountSubcontract,
      amountForeign,
      objectCode,
      subsidiaryCode,
      lineType,
      unit,
      BPI: billItem
    }
  })

  const spreadsheet = useRef<SpreadsheetComponent>(null)
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
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
          <NumericTextBoxComponent
            placeholder="Vendor Number"
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
                <RangeDirective dataSource={dataSource}></RangeDirective>
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

export default TenderDetail
