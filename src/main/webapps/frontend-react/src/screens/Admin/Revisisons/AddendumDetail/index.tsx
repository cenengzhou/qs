import { useEffect, useRef, useState } from 'react'

import { Query } from '@syncfusion/ej2-data'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  FocusOutEventArgs,
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

import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  IDADDENDUM,
  useGetAllAddendumDetailsMutation,
  useUpdateAddendumDetailListAdminMutation
} from '../../../../services'
import { getAddressIndex, regex, validateJobNo } from '../helper'
import { getAddressKey, selectQuery } from './constant'

const AddendumDetail = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const updateDetails = useRef<IDADDENDUM[]>([])
  const detail = useRef<IDADDENDUM>({ id: undefined })

  const [searchRecord, setSearchRecord] = useState<{
    jobNo?: string
    subcontractNo?: number
    addendumNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined, addendumNo: undefined })
  const [details, setDetails] = useState<IDADDENDUM[]>([])

  const [getAddendumDetails, { isLoading }] = useGetAllAddendumDetailsMutation()
  const [updateAddendumDetails, { isLoading: updateLoading }] =
    useUpdateAddendumDetailListAdminMutation()

  const search = async () => {
    setDetails([])
    await getAddendumDetails(searchRecord)
      .unwrap()
      .then(payload => {
        setDetails(payload)
      })
      .catch(() => {
        showTotas('Fail', 'Fail')
      })
  }

  const update = async () => {
    if (!updateDetails.current.length) {
      showTotas('Warn', 'No Addendum Detail modified')
      return
    }
    await updateAddendumDetails(updateDetails.current)
      .unwrap()
      .then(payload => {
        if (!payload) {
          updateDetails.current = []
          showTotas('Success', 'Addendum Detail updated')
        } else {
          showTotas('Warn', 'Addendum Detail update fail')
        }
      })
      .catch(() => {
        showTotas('Fail', 'Fail')
      })
  }

  const cellSave = (args: CellSaveEventArgs) => {
    const index = getAddressIndex(args.address)
    const key = getAddressKey(args.address)

    detail.current = { id: details[index].id, [`${key}`]: args.value }
    let obj: IDADDENDUM = {}
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
  }

  useEffect(() => {
    if (isLoading || updateLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading, updateLoading])

  const showTotas = (mode: 'Fail' | 'Success' | 'Warn', msg?: string) => {
    dispatch(
      setNotificationVisible({
        visible: true,
        mode: mode,
        content: msg
      })
    )
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
        'A1:V1'
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
      spreadsheet.numberFormat('#,##0.0000_);[Red]-#,##0.0000', 'M2:Q1000')
    }
  }, [isLoading])

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={searchRecord.jobNo}
            blur={(args: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                jobNo: args.value ?? ''
              })
              validateJobNo(args)
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="###"
            value={searchRecord.subcontractNo}
            change={(args: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                subcontractNo: Number(args.value)
              })
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="ADDENDUM Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="###"
            value={searchRecord.addendumNo}
            change={(args: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                addendumNo: Number(args.value)
              })
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent
            cssClass="e-info full-btn"
            disabled={
              !(
                searchRecord.jobNo &&
                searchRecord.subcontractNo &&
                searchRecord.addendumNo &&
                regex.test(searchRecord.jobNo)
              )
            }
            onClick={search}
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent
          ref={spreadsheetRef}
          allowEditing={!!details.length}
          cellSave={cellSave}
        >
          <SheetsDirective>
            <SheetDirective
              name="Addendum Detail"
              isProtected={true}
              protectSettings={{ selectCells: true, formatCells: true }}
              frozenRows={1}
              selectedRange="A2"
            >
              <RangesDirective>
                <RangeDirective
                  dataSource={details}
                  query={new Query().select(selectQuery)}
                  startCell="A2"
                  showFieldAsHeader={false}
                ></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective index={0} height={40}>
                  <CellsDirective>
                    <CellDirective value="ID"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                    <CellDirective value="Remark"></CellDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Subsidiary Code"></CellDirective>
                    <CellDirective value="Object Code for day work"></CellDirective>
                    <CellDirective value="BPI"></CellDirective>
                    <CellDirective value="Unit"></CellDirective>
                    <CellDirective value="typeVo"></CellDirective>
                    <CellDirective value="Resource summary"></CellDirective>
                    <CellDirective value="Header Reference"></CellDirective>
                    <CellDirective value="Subcontract charged ref"></CellDirective>
                    <CellDirective value="Addendum Rate"></CellDirective>
                    <CellDirective value="Budget Rate"></CellDirective>
                    <CellDirective value="Quantity"></CellDirective>
                    <CellDirective value="Addendum Amount"></CellDirective>
                    <CellDirective value="Budget Amount"></CellDirective>
                    <CellDirective value="Last Modify Date"></CellDirective>
                    <CellDirective value="Last Modify User"></CellDirective>
                    <CellDirective value="Date Create"></CellDirective>
                    <CellDirective value="User Create"></CellDirective>
                    <CellDirective value="Recoverablet"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective
                  width={120}
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
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective
                  width={120}
                  validation={{
                    type: 'List',
                    value1: ' ,R,NR',
                    ignoreBlank: false
                  }}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
      {isQsAdm && (
        <div className="row">
          <div className="col-lg-12 col-md-12">
            <ButtonComponent
              cssClass="e-info full-btn"
              onClick={update}
              disabled={!details.length}
            >
              Update Addendum Detail
            </ButtonComponent>
          </div>
        </div>
      )}
    </div>
  )
}

export default AddendumDetail
