import { useEffect, useRef, useState } from 'react'

import { Query } from '@syncfusion/ej2-data'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { ChangedEventArgs as calendarsChangedEventArgs } from '@syncfusion/ej2-react-calendars'
import {
  FocusOutEventArgs,
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

import DatePicker from '../../../../components/DatePicker'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  RocDetail,
  useGetRocDetailListAdminMutation,
  useUpdateRocDetailListAdminMutation
} from '../../../../services'
import { getAddressIndex, validateJobNo } from '../helper'
import { getAddressKey, selectQuery } from './constant'
import dayjs from 'dayjs'

const RocDetailRender = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const updateDetails = useRef<RocDetail[]>([])
  const detail = useRef<RocDetail>({ itemNo: undefined })

  const [searchRecord, setSearchRecord] = useState<{
    jobNo: string
    itemNo?: string
    period: string
  }>({
    jobNo: '',
    period: ''
  })
  const [details, setDetails] = useState<RocDetail[]>([])

  const [getRocDetail, { isLoading }] = useGetRocDetailListAdminMutation()
  const [upateRocDetail, { isLoading: updateLoading }] =
    useUpdateRocDetailListAdminMutation()

  const search = async () => {
    setDetails([])
    await getRocDetail(searchRecord)
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
      showTotas('Warn', 'No ROC Detail modified')
      return
    }
    await upateRocDetail({
      body: updateDetails.current,
      jobNo: searchRecord.jobNo
    })
      .unwrap()
      .then(payload => {
        if (payload.length > 0) {
          showTotas('Warn', payload)
        } else {
          if (payload == '') {
            updateDetails.current = []
            showTotas('Success', 'Roc Detail updated')
          }
        }
      })
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
        'A1:K1'
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
      spreadsheet.numberFormat('#,##0.00_);[Red]-#,##0.00', 'D2:F1000')
    }
  }, [isLoading])

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

  const cellSave = (args: CellSaveEventArgs) => {
    const index = getAddressIndex(args.address)
    const key = getAddressKey(args.address)

    detail.current = { itemNo: details[index].itemNo, [`${key}`]: args.value }
    let obj: RocDetail = {}
    if (
      updateDetails.current.find(item => item.itemNo === detail.current.itemNo)
    ) {
      obj =
        updateDetails.current.find(
          item => item.itemNo === detail.current.itemNo
        ) ?? {}
    } else {
      obj = details.find(item => item.itemNo === detail.current.itemNo) ?? {}
    }
    obj = { ...obj, [`${key}`]: args.value }
    const updateIndex = updateDetails.current.findIndex(
      item => item.itemNo === obj.itemNo
    )
    if (updateIndex === -1) {
      updateDetails.current.push(obj)
    } else {
      updateDetails.current[updateIndex] = obj
    }
  }

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
          <TextBoxComponent
            placeholder="Item No (Optional)"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={searchRecord.itemNo}
            blur={(args: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                itemNo: args.value
              })
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <DatePicker
            placeholder="Date"
            depth="Year"
            start="Year"
            format="MMMM y"
            value={searchRecord.period}
            onChange={(args: calendarsChangedEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                period: dayjs(args.value).format('YYYY-MM')
              })
            }}
          />
        </div>

        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn" onClick={search}>
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
              name="ROC Detail"
              isProtected={true}
              protectSettings={{
                selectCells: details.length ? true : false,
                formatCells: true
              }}
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
                    <CellDirective value="Item No"></CellDirective>
                    <CellDirective value="Year"></CellDirective>
                    <CellDirective value="Month"></CellDirective>
                    <CellDirective value="Best Case"></CellDirective>
                    <CellDirective value="Realistic"></CellDirective>
                    <CellDirective value="Worst Case"></CellDirective>
                    <CellDirective value="Remarks"></CellDirective>
                    <CellDirective value="Status"></CellDirective>
                    <CellDirective value="System Status"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
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
                  validation={{
                    type: 'List',
                    value1: ' ,Live,Closed',
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: ' ,ACTIVE,INACTIVE',
                    ignoreBlank: false
                  }}
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
              disabled={!details.length}
              onClick={update}
            >
              Update Roc Detail
            </ButtonComponent>
          </div>
        </div>
      )}
    </div>
  )
}

export default RocDetailRender
