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
                <RangeDirective dataSource={detail}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
                  <CellsDirective>
                    <CellDirective value="ID"></CellDirective>
                    <CellDirective value="Job Number"></CellDirective>
                    <CellDirective value="Subcontract"></CellDirective>
                    <CellDirective value="Ship City"></CellDirective>
                    <CellDirective value="Ship Country"></CellDirective>
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
                <ColumnDirective width={80}></ColumnDirective>
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

export default SubcontractDetail
