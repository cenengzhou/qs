/* eslint-disable @typescript-eslint/no-unused-vars */

/* eslint-disable @typescript-eslint/no-explicit-any */

/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useRef, useState } from 'react'

import { DataManager, JsonAdaptor, Query } from '@syncfusion/ej2-data'
import { ButtonComponent, ChangeEventArgs } from '@syncfusion/ej2-react-buttons'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'
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

import { SCDetail, useGetSCDetailsMutation } from '../../../../services'
import './style.css'

const SubcontractDetail = () => {
  const data = [
    {
      id: 603069,
      jobNo: '13892',
      subcontract: {
        packageNo: '1001'
      },
      sequenceNo: 1,
      description: 'Ground Investigation Works (Buying Gain)',
      remark: null,
      objectCode: '149999',
      subsidiaryCode: '25999999',
      billItem: ' ',
      unit: 'UN',
      lineType: 'BQ',
      approved: 'A',
      resourceNo: 402141,
      scRate: 0.00125,
      quantity: 171239.9,
      cumCertifiedQuantity: 0.0,
      amountCumulativeWD: 0,
      amountPostedCert: 0,
      postedCertifiedQuantity: 0.0,
      amountSubcontractNew: 0,
      newQuantity: 171239.9,
      originalQuantity: 171239.9,
      toBeApprovedRate: 0.0,
      amountSubcontract: 0,
      amountBudget: 171239.9,
      lastModifiedDate: '2020-11-17T15:00:04.000+00:00',
      lastModifiedUser: 'SYSTEM',
      createdDate: '2020-11-17T15:00:04.000+00:00',
      createdUser: 'SYSTEM',
      systemStatus: 'ACTIVE',
      typeRecoverable: null
    }
  ]
  const dataSource = data.map(item => {
    const { id, subcontract, ...rest } = item
    return { id, subcontract: subcontract.packageNo, ...rest }
  })
  const [jobNo, setJobNo] = useState<string>('')
  const [subcontractNo, setSubcontractNo] = useState<number>()
  const spreadsheet = useRef<SpreadsheetComponent>(null)
  const [getSCdetails, { isLoading }] = useGetSCDetailsMutation()
  const [detail, setDetail] = useState<SCDetail[]>([])
  const getData = async () => {
    let result: SCDetail[] = []
    try {
      result = new DataManager({
        json: await getSCdetails({
          jobNo: jobNo,
          subcontractNo: subcontractNo?.toString()
        }).unwrap(),
        adaptor: new JsonAdaptor()
      }).executeLocal(
        new Query().select([
          'id',
          'jobNo',
          'subcontract.packageNo',
          'sequenceNo',
          'description',
          'remark',
          'objectCode',
          'subsidiaryCode',
          'billItem',
          'unit',
          'lineType',
          'approved',
          'resourceNo',
          'scRate',
          'quantity',
          'cumCertifiedQuantity',
          'createdUser',
          'systemStatus'
        ])
      )
    } catch (e) {
      console.log(e)
    }
    setDetail(result)
  }

  useEffect(() => {
    const root = document.getElementById('root')!
    if (isLoading) {
      showSpinner(root)
    } else {
      hideSpinner(root)
    }
  }, [isLoading])

  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-4 col-md-4">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={jobNo}
            onChange={(e: any) => setJobNo(e.value)}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="#"
            value={subcontractNo}
            onChange={(e: any) => setSubcontractNo(e.value)}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent
            disabled={!(!!jobNo && !!subcontractNo)}
            cssClass="e-info full-btn"
            onClick={getData}
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent ref={spreadsheet}>
          <SheetsDirective>
            <SheetDirective name="Subcontract Detail">
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

export default SubcontractDetail
