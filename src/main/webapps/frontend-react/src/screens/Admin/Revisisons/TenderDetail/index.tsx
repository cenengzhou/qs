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
  TenderDetailList,
  useGetTenderDetailListMutation,
  useUpdateTenderDetailListAdminMutation
} from '../../../../services'
import { getAddressIndex, regex, validateJobNo } from '../helper'
import { getAddressKey, selectQuery } from './constant'

const TenderDetail = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const updateDetails = useRef<TenderDetailList[]>([])
  const detail = useRef<TenderDetailList>({ id: undefined })

  const [searchRecord, setSearchRecord] = useState<{
    jobNo?: string
    subcontractNo?: number
    subcontractorNo?: number
  }>({})
  const [details, setDetails] = useState<TenderDetailList[]>([])

  const [getTenderDetail, { isLoading }] = useGetTenderDetailListMutation()
  const [updateTenderDetail, { isLoading: updateLoading }] =
    useUpdateTenderDetailListAdminMutation()

  const search = async () => {
    setDetails([])
    await getTenderDetail(searchRecord)
      .unwrap()
      .then(payload => {
        setDetails(payload)
      })
  }
  const update = async () => {
    if (!updateDetails.current.length) {
      showTotas('Warn', 'No Tender Detail modified')
      return
    }
    await updateTenderDetail(updateDetails.current)
      .unwrap()
      .then(payload => {
        if (!payload) {
          updateDetails.current = []
          showTotas('Success', 'Tender Detail updated')
        } else {
          showTotas('Warn', 'Fail')
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
    let obj: TenderDetailList = {}
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
        'A1:P1'
      )
    }
    spreadsheetRef.current?.refresh()
  }, [isQsAdm])

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

  // 清除殘留spreadsheet的數據
  useEffect(() => {
    spreadsheetRef.current!.sheets[0].rows =
      spreadsheetRef.current!.sheets[0].rows?.slice(0, 1)
    const spreadsheet = spreadsheetRef.current
    if (spreadsheet) {
      spreadsheet.numberFormat('#,##0.0000_);[Red]-#,##0.0000', 'F2:G1000')
      spreadsheet.numberFormat('#,##0.00_);[Red]-#,##0.00', 'H2:H1000')
      spreadsheet.numberFormat('#,##0.0000_);[Red]-#,##0.0000', 'I2:I1000')
      spreadsheet.numberFormat('#,##0.00_);[Red]-#,##0.00', 'J2:K1000')
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
                jobNo: args.value
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
            value={searchRecord.subcontractNo}
            format="###"
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
            placeholder="Vendor Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="###"
            value={searchRecord.subcontractorNo}
            change={(args: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                subcontractorNo: Number(args.value)
              })
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={search}
            disabled={
              !(
                searchRecord.jobNo &&
                searchRecord.subcontractNo &&
                searchRecord.subcontractorNo &&
                regex.test(searchRecord.jobNo)
              )
            }
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
              name="Attachment"
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
                    <CellDirective value="Job Number"></CellDirective>
                    <CellDirective value="Subcontract"></CellDirective>
                    <CellDirective value="Vendor"></CellDirective>
                    <CellDirective value="Sequence Number"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                    <CellDirective value="Quantity"></CellDirective>
                    <CellDirective value="Rate Budget"></CellDirective>
                    <CellDirective value="Amount Budget"></CellDirective>
                    <CellDirective value="Rate Subcontract"></CellDirective>
                    <CellDirective value="Amount Subcontract"></CellDirective>
                    <CellDirective value="Amount Foreign"></CellDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Subsidiary Code"></CellDirective>
                    <CellDirective value="Line Type"></CellDirective>
                    <CellDirective value="Unit"></CellDirective>
                    <CellDirective value="BillItem"></CellDirective>
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
                  width={110}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={110}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={110}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={110}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={110}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={110}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={60}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={80}
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
                <ColumnDirective width={55}></ColumnDirective>
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

export default TenderDetail
