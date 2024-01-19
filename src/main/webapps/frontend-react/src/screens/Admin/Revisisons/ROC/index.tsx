import { useEffect, useRef, useState } from 'react'

import { Query } from '@syncfusion/ej2-data'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
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

import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  RocAdmin,
  useGetImpactListQuery,
  useGetRocAdminMutation,
  useGetRocCategoryListQuery,
  useGetRocClassDescMapQuery,
  useUpdateRocAdminMutation
} from '../../../../services'
import { getAddressIndex, regex, validateJobNo } from '../helper'
import { getAddressKey, selectQuery } from './constant'

const RocRender = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const spreadsheetRef = useRef<SpreadsheetComponent>(null)
  const updateDetails = useRef<RocAdmin[]>([])
  const detail = useRef<RocAdmin>({ id: undefined })

  const [searchRecord, setSearchRecord] = useState<{
    jobNo: string
    itemNo?: string
  }>({ jobNo: '' })
  const [details, setDetails] = useState<RocAdmin[]>([])

  const { data: rocClassDescMap } = useGetRocClassDescMapQuery()
  const { data: rocCategoryList } = useGetRocCategoryListQuery()
  const { data: impactList } = useGetImpactListQuery()
  const [getDetail, { isLoading }] = useGetRocAdminMutation()
  const [updateDetail, { isLoading: updateLoading }] =
    useUpdateRocAdminMutation()

  const search = async () => {
    setDetails([])
    await getDetail(searchRecord)
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
      showTotas('Warn', 'No ROC modified')
      return
    }
    await updateDetail({
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
            showTotas('Success', 'Roc updated')
          }
        }
      })
      .catch(() => {
        showTotas('Fail', 'Roc updated Failed')
      })
  }

  const cellSave = (args: CellSaveEventArgs) => {
    const index = getAddressIndex(args.address)
    const key = getAddressKey(args.address)

    detail.current = { id: details[index].id, [`${key}`]: args.value }
    let obj: RocAdmin = {}
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
        'A1:K1'
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

  // 清除殘留spreadsheet的數據
  useEffect(() => {
    spreadsheetRef.current!.sheets[0].rows =
      spreadsheetRef.current!.sheets[0].rows?.slice(0, 1)
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
          <ButtonComponent
            cssClass="e-info full-btn"
            disabled={!(searchRecord.jobNo && regex.test(searchRecord.jobNo))}
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
              name="ROC"
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
                    <CellDirective value="Project R&O Ref"></CellDirective>
                    <CellDirective value="Category"></CellDirective>
                    <CellDirective value="Classification"></CellDirective>
                    <CellDirective value="Impact"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                    <CellDirective value="ROC Owner"></CellDirective>
                    <CellDirective value="Open Date"></CellDirective>
                    <CellDirective value="Closed Date"></CellDirective>
                    <CellDirective value="Status"></CellDirective>
                    <CellDirective value="System Status"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: rocCategoryList,
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: rocClassDescMap,
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={160}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: impactList,
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={240}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={120}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={280}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={180}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={180}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: ' ,Live,Closed',
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={180}
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
              onClick={update}
              disabled={!details.length}
            >
              Update Roc
            </ButtonComponent>
          </div>
        </div>
      )}
    </div>
  )
}

export default RocRender
