import { useEffect, useRef, useState } from 'react'

import { Query } from '@syncfusion/ej2-data'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  DropDownListComponent,
  ChangeEventArgs as DropdownChangeEventArgs
} from '@syncfusion/ej2-react-dropdowns'
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
  DataSourceChangedEventArgs,
  RangeDirective,
  RangesDirective,
  RowDirective,
  RowsDirective,
  SheetDirective,
  SheetsDirective,
  SpreadsheetComponent
} from '@syncfusion/ej2-react-spreadsheet'

import { GLOBALPARAMETER } from '../../../../constants/global'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  Attachment,
  CustomError,
  useDeleteAttachmentAdminMutation,
  useGetLatestRepackagingMutation,
  useObtainAttachmentListMutation
} from '../../../../services'
import { selectQuery } from './constant'
import './style.css'
import classNames from 'classnames'

const AttachmentRender = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const repackagingId = useRef<number | undefined>()
  const updateDetails = useRef<Attachment[]>([])

  const [searchRecord, setSearchRecord] = useState<{
    select: string
    vendorNo?: number
    jobNo?: string
    subcontractNo?: number
    alterParam?: number
  }>({ select: 'GT58010' })
  const [details, setDetails] = useState<Attachment[]>([])

  const [getLatestRepackaging] = useGetLatestRepackagingMutation()
  const [obtainAttachmentList, { isSuccess, isLoading }] =
    useObtainAttachmentListMutation()
  const [deleteAttachment] = useDeleteAttachmentAdminMutation()

  const getId = async () => {
    await getLatestRepackaging({ jobNo: searchRecord.jobNo })
      .unwrap()
      .then(payload => {
        repackagingId.current = payload.id
      })
      .catch(() => {
        console.error('Fail to get lastest repackaging')
      })
  }

  const search = async () => {
    setDetails([])
    if (searchRecord.select === 'REPACKAGING') {
      await getId()
    }
    await obtainAttachmentList({
      nameObject: searchRecord.select,
      textKey: getTextKey()
    })
      .unwrap()
      .then(payload => {
        setDetails(payload)
      })
      .catch((error: CustomError) => {
        showTotas('Fail', error.data.message)
      })
  }

  const dataSourceChanged = (args: DataSourceChangedEventArgs) => {
    if (args.action === 'delete') {
      args.data?.forEach(item => {
        updateDetails.current.push(item)
      })
    }
  }

  const deleteData = async () => {
    if (!updateDetails.current.length) {
      showTotas('Warn', 'No Rows to delete')
      return
    }
    updateDetails.current.forEach(async item => {
      await deleteAttachment({
        nameObject: searchRecord.select,
        sequenceNumber: item.noSequence,
        textKey: getTextKey()
      })
        .unwrap()
        .then(payload => {
          if (payload != true) {
            showTotas('Warn', `SequenceNo ${item.noSequence} Delele Fail`)
          }
        })
        .catch(() => {
          showTotas('Fail', 'Fail')
        })
    })
    updateDetails.current = []
  }

  function getTextKey() {
    switch (searchRecord.select) {
      case 'GT58024':
        return String(searchRecord.vendorNo)
      case 'TRANSIT':
        return searchRecord.jobNo + '|--|'
      case 'GT59026':
        return searchRecord.jobNo + '|--|' + searchRecord.alterParam
      case 'REPACKAGING':
        return searchRecord.jobNo + '|--|' + repackagingId.current
      default:
        return (
          searchRecord.jobNo +
          '|' +
          searchRecord.subcontractNo +
          '|' +
          searchRecord.alterParam
        )
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
        'A1:L1'
      )
    }
    spreadsheetRef.current?.refresh()
  }, [isQsAdm])

  useEffect(() => {
    if (isLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading])

  // 清除殘留spreadsheet的數據
  useEffect(() => {
    spreadsheetRef.current!.sheets[0].rows =
      spreadsheetRef.current!.sheets[0].rows?.slice(0, 1)
  }, [isSuccess])

  const showTotas = (mode: 'Fail' | 'Success' | 'Warn', msg?: string) => {
    dispatch(
      setNotificationVisible({
        visible: true,
        mode: mode,
        content: msg
      })
    )
  }

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-2 col-md-2">
          <DropDownListComponent
            dataSource={GLOBALPARAMETER.nameObjectTable}
            fields={{ text: 'display', value: 'value' }}
            cssClass="e-outline"
            floatLabelType="Always"
            showClearButton={false}
            placeholder="Table Name"
            value={searchRecord.select}
            onChange={(args: DropdownChangeEventArgs) => {
              setDetails([])
              spreadsheetRef.current!.sheets[0].rows =
                spreadsheetRef.current!.sheets[0].rows?.slice(0, 1)
              setSearchRecord({ ...searchRecord, select: String(args.value) })
            }}
          />
        </div>
        {searchRecord.select !== 'GT58024' && (
          <div className="col-lg-2 col-md-2">
            <TextBoxComponent
              placeholder="Job Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value={searchRecord.jobNo}
              blur={(value: FocusOutEventArgs) => {
                setSearchRecord({
                  ...searchRecord,
                  jobNo: value.value ?? ''
                })
              }}
            />
          </div>
        )}
        {searchRecord.select === 'GT58024' && (
          <div className="col-lg-2 col-md-2">
            <NumericTextBoxComponent
              placeholder="Vendor Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="#"
              value={searchRecord.vendorNo}
              blur={(value: FocusOutEventArgs) => {
                setSearchRecord({
                  ...searchRecord,
                  vendorNo: Number(value.value)
                })
              }}
            />
          </div>
        )}
        {searchRecord.select !== 'GT59026' &&
          searchRecord.select !== 'GT58024' &&
          searchRecord.select !== 'REPACKAGING' &&
          searchRecord.select !== 'TRANSIT' && (
            <div className="col-lg-2 col-md-2">
              <NumericTextBoxComponent
                placeholder="Subcontract Number"
                floatLabelType="Auto"
                cssClass="e-outline"
                format="#"
                value={searchRecord.subcontractNo}
                blur={(value: FocusOutEventArgs) => {
                  setSearchRecord({
                    ...searchRecord,
                    subcontractNo: Number(value.value)
                  })
                }}
              />
            </div>
          )}

        {searchRecord.select !== 'GT58010' &&
          searchRecord.select !== 'GT58024' &&
          searchRecord.select !== 'SPLIT' &&
          searchRecord.select !== 'TERMINATE' &&
          searchRecord.select !== 'REPACKAGING' &&
          searchRecord.select !== 'JOBINFO' &&
          searchRecord.select !== 'TRANSIT' && (
            <div className="col-lg-2 col-md-2">
              <NumericTextBoxComponent
                floatLabelType="Auto"
                cssClass="e-outline"
                format="#"
                placeholder={
                  searchRecord.select === 'GT58011'
                    ? 'ADDENDUM Number'
                    : searchRecord.select === 'GT58012'
                    ? 'PAYMENT Number'
                    : 'MAIN_CERT Number'
                }
                value={searchRecord.alterParam}
                blur={(value: FocusOutEventArgs) => {
                  setSearchRecord({
                    ...searchRecord,
                    alterParam: Number(value.value)
                  })
                }}
              />
            </div>
          )}

        <div
          className={classNames([
            {
              ['col-lg-4 col-md-4']:
                searchRecord.select === 'GT58011' ||
                searchRecord.select === 'GT58012'
            },
            {
              ['col-lg-6 col-md-6']:
                searchRecord.select === 'GT59026' ||
                searchRecord.select === 'GT58010' ||
                searchRecord.select === 'SPLIT' ||
                searchRecord.select === 'TERMINATE' ||
                searchRecord.select === 'JOBINFO'
            },
            {
              ['col-lg-8 col-md-8']:
                searchRecord.select === 'GT58024' ||
                searchRecord.select === 'REPACKAGING' ||
                searchRecord.select === 'TRANSIT'
            }
          ])}
        >
          <ButtonComponent cssClass="e-info full-btn" onClick={search}>
            Search
          </ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <SpreadsheetComponent
          ref={spreadsheetRef}
          allowEditing={false}
          dataSourceChanged={dataSourceChanged}
        >
          <SheetsDirective>
            <SheetDirective name="Attachment">
              <RangesDirective>
                <RangeDirective
                  dataSource={details}
                  query={new Query().select(selectQuery)}
                ></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective index={0} height={40}>
                  <CellsDirective>
                    <CellDirective value="ID"></CellDirective>
                    <CellDirective value="Table Id"></CellDirective>
                    <CellDirective value="Table Name"></CellDirective>
                    <CellDirective value="Sequence No."></CellDirective>
                    <CellDirective value="Docment Type"></CellDirective>
                    <CellDirective value="File Nam"></CellDirective>
                    <CellDirective value="File Path"></CellDirective>
                    <CellDirective value="Text"></CellDirective>
                    <CellDirective value="Created User"></CellDirective>
                    <CellDirective value="Created Date"></CellDirective>
                    <CellDirective value="Last Modified User"></CellDirective>
                    <CellDirective value="Last Modified Date"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={60}></ColumnDirective>
                <ColumnDirective width={60}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={200}></ColumnDirective>
                <ColumnDirective width={200}></ColumnDirective>
                <ColumnDirective width={200}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
                <ColumnDirective width={120}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
      {isQsAdm && (
        <div className="row">
          <div className="col-lg-12 col-md-12">
            <ButtonComponent cssClass="e-info full-btn" onClick={deleteData}>
              Delete
            </ButtonComponent>
          </div>
        </div>
      )}
    </div>
  )
}

export default AttachmentRender
