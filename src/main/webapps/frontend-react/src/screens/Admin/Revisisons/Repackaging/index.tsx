/* eslint-disable @typescript-eslint/no-explicit-any */
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
  Repackaging,
  useObtainResourceSummariesByJobNumberForAdminMutation,
  useUpdateResourceSummariesForAdminMutation
} from '../../../../services'
import { getAddressIndex, regex, validateJobNo } from '../helper'
import { getAddressKey, selectQuery } from './constant'

const RepackagingRender = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const updateDetails = useRef<Repackaging[]>([])
  const detail = useRef<Repackaging>({ id: undefined })

  const [jobNo, setJobNo] = useState<string | undefined>(undefined)
  const [details, setDetails] = useState<Repackaging[]>([])

  const [getRepackaging, { isLoading }] =
    useObtainResourceSummariesByJobNumberForAdminMutation()
  const [updateRepackaging, { isLoading: updateLoading }] =
    useUpdateResourceSummariesForAdminMutation()

  const search = async () => {
    await getRepackaging({ jobNumber: jobNo })
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
      showTotas('Warn', 'No resource modified')
      return
    }
    await updateRepackaging({ body: updateDetails.current, jobNo: jobNo ?? '' })
      .unwrap()
      .then(paylaod => {
        if (paylaod) {
          showTotas('Warn', paylaod)
        } else {
          updateDetails.current = []
          showTotas('Success', 'Resource summary updated')
        }
      })
  }

  useEffect(() => {
    if (isLoading || updateLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading, updateLoading])

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

    detail.current = { id: details[index].id, [`${key}`]: args.value }
    let obj: Repackaging = {}
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

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-8 col-md-8">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={jobNo}
            blur={(args: FocusOutEventArgs) => {
              setJobNo(args.value)
              validateJobNo(args)
            }}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent
            cssClass="e-info full-btn"
            disabled={!(jobNo && regex.test(jobNo))}
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
              name="ROC Subdetail"
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
                    <CellDirective value="Subcontract No."></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Subsidiary Code"></CellDirective>
                    <CellDirective value="Budget"></CellDirective>
                    <CellDirective value="Quantity"></CellDirective>
                    <CellDirective value="Current Amount"></CellDirective>
                    <CellDirective value="Posted Amount"></CellDirective>
                    <CellDirective value="Rate"></CellDirective>
                    <CellDirective value="Unit"></CellDirective>
                    <CellDirective value="Exclude Defect"></CellDirective>
                    <CellDirective value="Exclude Levy"></CellDirective>
                    <CellDirective value="For IV Rollback Only"></CellDirective>
                    <CellDirective value="Finalized"></CellDirective>
                    <CellDirective value="Type"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={200}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: ' ,false,true',
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: ' ,false,true',
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                  validation={{
                    type: 'List',
                    value1: ' ,false,true',
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
                  isLocked={!isQsAdm}
                ></ColumnDirective>
                <ColumnDirective
                  width={100}
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

export default RepackagingRender
