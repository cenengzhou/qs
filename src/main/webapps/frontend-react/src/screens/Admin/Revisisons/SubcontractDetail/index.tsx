import { useEffect, useRef, useState } from 'react'

import { Query } from '@syncfusion/ej2-data'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  FocusOutEventArgs,
  InputEventArgs,
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'
import {
  CellDirective,
  CellSaveEventArgs,
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

import { regex } from '..'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import {
  setNotificationContent,
  setNotificationMode,
  setNotificationVisible
} from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  SCDetail,
  useGetSCDetailsMutation,
  useUpdateSubcontractDetailListAdminMutation
} from '../../../../services'
import { getAddressIndex, getAddressKey, selectQuery } from './constants'
import './style.css'

const SubcontractDetail = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()
  const query = new Query().select(selectQuery)
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)
  const validateInput = (value: InputEventArgs) => {
    if (value.value && regex.test(value.value)) {
      value.container?.classList.add('e-success')
      value.container?.classList.remove('e-error')
    } else if (value.value && !regex.test(value.value)) {
      value.container?.classList.remove('e-success')
      value.container?.classList.add('e-error')
    } else {
      value.container?.classList.remove('e-error')
      value.container?.classList.remove('e-success')
    }
  }

  const updateDetails = useRef<SCDetail[]>([])
  const detail = useRef<SCDetail>({ id: undefined })
  const [searchRecord, setSearchRecord] = useState<{
    jobNo?: string
    subcontractNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined })
  const [details, setDetails] = useState<SCDetail[]>([])
  const [getScdetails, { isLoading }] = useGetSCDetailsMutation()
  const [updateDetail] = useUpdateSubcontractDetailListAdminMutation()

  const getData = async () => {
    dispatch(openLoading())
    await getScdetails({
      jobNo: searchRecord.jobNo,
      subcontractNo: searchRecord.subcontractNo
    })
      .unwrap()
      .then(payload => {
        dispatch(closeLoading())
        setDetails(payload)
      })
      .catch((error: CustomError) => {
        dispatch(closeLoading())
        dispatch(setNotificationMode('Fail'))
        dispatch(setNotificationContent(error.data.message))
        dispatch(setNotificationVisible(true))
        console.error(error.data.message)
      })
  }
  const updateDedail = async () => {
    if (!updateDetails.current.length) {
      dispatch(setNotificationMode('Warn'))
      dispatch(setNotificationContent('No Subcontract Detail modified'))
      dispatch(setNotificationVisible(true))
      return
    }
    dispatch(openLoading())
    await updateDetail(updateDetails.current)
      .then(() => {
        dispatch(closeLoading())

        dispatch(setNotificationMode('Success'))
        dispatch(setNotificationContent('Subcontract Detail updated'))
        dispatch(setNotificationVisible(true))
      })
      .catch((error: CustomError) => {
        dispatch(closeLoading())

        dispatch(setNotificationMode('Fail'))
        dispatch(setNotificationContent(error.data.message))
        dispatch(setNotificationVisible(true))
      })
  }

  const cellSave = (args: CellSaveEventArgs) => {
    const index = getAddressIndex(args.address)
    const key = getAddressKey(args.address)

    detail.current = { id: details[index].id, [`${key}`]: args.value }
    let obj: SCDetail = {}
    if (updateDetails.current.find(item => item.id === detail.current.id)) {
      obj =
        updateDetails.current.find(item => item.id === detail.current.id) ?? {}
    } else {
      obj = details.find(item => item.id === detail.current.id) ?? {}
    }
    obj = { ...obj, [`${key}`]: args.value }
    const updateIndex = updateDetails.current.findIndex(
      item => item.id === obj.id
    )
    if (updateIndex === -1) {
      updateDetails.current.push(obj)
    } else {
      updateDetails.current[updateIndex] = obj
    }
    console.log(updateDetails.current)
  }

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
        'A1:AG1'
      )
    }
    spreadsheetRef.current?.refresh()
  }, [isQsAdm])

  // 清除殘留spreadsheet的數據
  useEffect(() => {
    spreadsheetRef.current!.sheets[0].rows =
      spreadsheetRef.current!.sheets[0].rows?.slice(0, 1)
    const spreadsheet = spreadsheetRef.current
    if (spreadsheet) {
      spreadsheet.numberFormat('#,##0.0000_);[Red]-#,##0.0000', 'N2:N1000')
      spreadsheet.numberFormat('$#,##0.0000_);[Red]-$#,##0.0000', 'O2:AA1000')
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
            value={searchRecord.jobNo}
            blur={(e: FocusOutEventArgs) => {
              setSearchRecord({ ...searchRecord, jobNo: e.value ?? '' })
              validateInput(e)
            }}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="#"
            value={searchRecord.subcontractNo}
            blur={(e: FocusOutEventArgs) =>
              setSearchRecord({
                ...searchRecord,
                subcontractNo: Number(e.value) ?? ''
              })
            }
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent
            disabled={
              !(
                regex.test(searchRecord.jobNo ?? '') &&
                searchRecord.jobNo &&
                !!searchRecord.subcontractNo
              )
            }
            cssClass="e-info full-btn"
            onClick={() => {
              getData()
            }}
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent
          ref={spreadsheetRef}
          allowDataValidation={true}
          cellSave={cellSave}
          allowEditing={!!details.length}
        >
          <SheetsDirective>
            <SheetDirective
              name="Subcontract Detail"
              isProtected={true}
              protectSettings={{ selectCells: true, formatCells: true }}
              frozenRows={1}
              selectedRange="A2"
            >
              <RangesDirective>
                <RangeDirective
                  dataSource={details}
                  query={query}
                  startCell="A2"
                  showFieldAsHeader={false}
                ></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective index={0} height={40}>
                  <CellsDirective>
                    <CellDirective value="ID"></CellDirective>
                    <CellDirective value="Job Number"></CellDirective>
                    <CellDirective value="Subcontract"></CellDirective>
                    <CellDirective value="Sequence Number"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                    <CellDirective value="Remark"></CellDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Subsidiary Code"></CellDirective>
                    <CellDirective value="Bill Item"></CellDirective>
                    <CellDirective value="Unit"></CellDirective>
                    <CellDirective value="Line Type"></CellDirective>
                    <CellDirective value="Approved"></CellDirective>
                    <CellDirective value="Resource Number"></CellDirective>
                    <CellDirective value="Subcontract Rate"></CellDirective>
                    <CellDirective value="Quantity"></CellDirective>
                    <CellDirective value="Cum Cert Amount"></CellDirective>
                    <CellDirective value="Cum Cert Qty"></CellDirective>
                    <CellDirective value="Cum WD Amount"></CellDirective>
                    <CellDirective value="Cum WD Qty"></CellDirective>
                    <CellDirective value="Posted Cert Amount"></CellDirective>
                    <CellDirective value="Posted Cert Qty"></CellDirective>
                    <CellDirective value="New Subcontract Amount"></CellDirective>
                    <CellDirective value="New Qty"></CellDirective>
                    <CellDirective value="Original Qty"></CellDirective>
                    <CellDirective value="To Be Approved Rate"></CellDirective>
                    <CellDirective value="Subcontract Amount"></CellDirective>
                    <CellDirective value="Budget Amount"></CellDirective>
                    <CellDirective value="Last Modify Date"></CellDirective>
                    <CellDirective value="Last Modify User"></CellDirective>
                    <CellDirective value="Date Create"></CellDirective>
                    <CellDirective value="User Create"></CellDirective>
                    <CellDirective value="System Status"></CellDirective>
                    <CellDirective value="Recoverable"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective
                  width={240}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={360}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective
                  width={100}
                  validation={{
                    type: 'List',
                    value1: 'INACTIVE,ACTIVE',
                    ignoreBlank: false
                  }}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  validation={{
                    type: 'List',
                    value1: ' ,Recoverable,Non-Recoverable',
                    ignoreBlank: false
                  }}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
      <div className="row">
        <div className="col-lg-12 col-md-12">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={updateDedail}
            disabled={!details.length}
          >
            Update Subcontract Detail
          </ButtonComponent>
        </div>
      </div>
    </div>
  )
}

export default SubcontractDetail
