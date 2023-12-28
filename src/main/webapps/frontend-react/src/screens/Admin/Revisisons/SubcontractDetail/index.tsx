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

import NotificationModal from '../../../../components/NotificationModal'
import { useHasRole } from '../../../../hooks/useHasRole'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  SCDetail,
  useGetSCDetailsMutation
} from '../../../../services'
import './style.css'

const SubcontractDetail = () => {
  const dispatch = useAppDispatch()
  const query = new Query().select([
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
    'amountCumulativeCert',
    'cumCertifiedQuantity',
    'amountCumulativeWD',
    'cumWorkDoneQuantity',
    'amountPostedCert',
    'postedCertifiedQuantity',
    'amountSubcontractNew',
    'newQuantity',
    'originalQuantity',
    'toBeApprovedRate',
    'amountSubcontract',
    'amountBudget',
    'lastModifiedDate',
    'lastModifiedUser',
    'createdDate',
    'createdUser',
    'systemStatus',
    'typeRecoverable'
  ])
  const isQsAdm = useHasRole('ROLE_QS_QS_ADM')
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const [searchRecord, setSearchRecord] = useState<{
    jobNo?: string
    subcontractNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined })
  const [detail, setDetail] = useState<SCDetail[]>([])
  const [notificationContent, setNotificationContent] = useState<string>('')
  const [visibleNotificationModal, setVisibleNotificationModal] =
    useState<boolean>(false)
  const [notificationMode, setNotificationMode] = useState<
    'Success' | 'Fail' | 'Warn'
  >('Success')

  const [getScdetails, { isLoading }] = useGetSCDetailsMutation()
  const getData = async () => {
    dispatch(openLoading())
    await getScdetails({
      jobNo: searchRecord.jobNo,
      subcontractNo: searchRecord.subcontractNo
    })
      .unwrap()
      .then(payload => {
        dispatch(closeLoading())
        setDetail(payload)
      })
      .catch((error: CustomError) => {
        dispatch(closeLoading())
        setNotificationContent(error.data.message)
        setNotificationMode('Fail')
        setVisibleNotificationModal(true)
        console.error(error.data.message)
      })
  }
  useEffect(() => {
    const spreadsheet = spreadsheetRef.current
    spreadsheetRef.current?.refresh()

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
  }, [isQsAdm])

  // 清除殘留spreadsheet的數據
  useEffect(() => {
    spreadsheetRef.current!.sheets[0].rows =
      spreadsheetRef.current!.sheets[0].rows?.slice(0, 1)
  }, [isLoading])

  const closeNotification = () => {
    setVisibleNotificationModal(false)
  }
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
            blur={(e: FocusOutEventArgs) =>
              setSearchRecord({ ...searchRecord, jobNo: e.value ?? '' })
            }
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
            disabled={!(searchRecord.jobNo && !!searchRecord.subcontractNo)}
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
        <SpreadsheetComponent ref={spreadsheetRef} allowDataValidation={true}>
          <SheetsDirective>
            <SheetDirective
              name="Subcontract Detail"
              isProtected={true}
              protectSettings={{ selectCells: true }}
            >
              <RangesDirective>
                <RangeDirective
                  dataSource={detail}
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
      <NotificationModal
        mode={notificationMode}
        visible={visibleNotificationModal}
        dialogClose={closeNotification}
        content={notificationContent}
      />
    </div>
  )
}

export default SubcontractDetail
