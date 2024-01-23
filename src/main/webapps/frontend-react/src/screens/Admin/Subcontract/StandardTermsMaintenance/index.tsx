/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useRef, useState } from 'react'

import { Query } from '@syncfusion/ej2-data'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  DropDownListComponent,
  ChangeEventArgs as DropdownChangeEventArgs
} from '@syncfusion/ej2-react-dropdowns'
import {
  FocusOutEventArgs,
  NumericTextBoxComponent
} from '@syncfusion/ej2-react-inputs'
import { DialogComponent } from '@syncfusion/ej2-react-popups'
import {
  CellDirective,
  CellSaveEventArgs,
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

import { GLOBALPARAMETER, IDFIELDS } from '../../../../constants/global'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  ScStandardTerms,
  useCreateSystemConstantMutation,
  useDeleteSCStandardTermsMutation,
  useGetSCStandardTermsListQuery,
  useObtainAllJobCompanyQuery,
  useUpdateMultipleSystemConstantsMutation
} from '../../../../services'
import { getAddressIndex } from '../../Revisisons/helper'
import {
  getAddressKey,
  getPaymentTerm,
  getRetentionTerm,
  selectQuery
} from './constant'

const StandardTermsMaintenance = () => {
  const dispatch = useAppDispatch()
  const spreadsheetRef = useRef<SpreadsheetComponent>(null)

  const [visibility, setDialogVisibility] = useState(false)
  const [add, setAdd] = useState<{
    company?: string
    formOfSubcontract?: string
    retentionType?: string
    scInterimRetentionPercent?: number
    scMOSRetentionPercent?: number
    scMaxRetentionPercent?: number
    scPaymentTerm?: string
  }>({})

  const updateDetails = useRef<ScStandardTerms[]>([])
  const deleteDetails = useRef<ScStandardTerms[]>([])
  const detail = useRef<ScStandardTerms>({ id: undefined })

  const { data: details, refetch } = useGetSCStandardTermsListQuery()
  const [updateData, { isLoading: updateLoading }] =
    useUpdateMultipleSystemConstantsMutation()
  const [deleteData, { isLoading: deleteLoading }] =
    useDeleteSCStandardTermsMutation()
  const [createData, { isLoading: createLoading }] =
    useCreateSystemConstantMutation()
  const { data: companyList } = useObtainAllJobCompanyQuery()

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
        'A1:H1'
      )
    }
    spreadsheetRef.current?.refresh()
  }, [])

  const update = async () => {
    if (!updateDetails.current.length) {
      showTotas('Warn', 'no Records to update')
      return
    }

    updateDetails.current.map(item => {
      const { scPaymentTerm } = item
      item.scPaymentTerm = scPaymentTerm?.split('-')[0].trim()
      return item
    })

    await updateData(updateDetails.current)
      .unwrap()
      .then(() => {
        showTotas('Success', `Updated  ${updateDetails.current.length} records`)
        updateDetails.current = []
      })
      .catch(error => {
        showTotas('Fail', error)
      })
  }

  const create = async () => {
    setDialogVisibility(true)
  }

  const Add = async () => {
    if (
      !(
        add.company &&
        add.formOfSubcontract &&
        add.retentionType &&
        add.retentionType &&
        add.scInterimRetentionPercent &&
        add.scMOSRetentionPercent &&
        add.scMaxRetentionPercent &&
        add.scPaymentTerm
      )
    ) {
      showTotas('Warn', 'lack of data')
      return
    }
    await createData(add)
      .unwrap()
      .then(() => {
        showTotas('Success', 'Create succeeded')
        refetch()
        setAdd({
          company: undefined,
          formOfSubcontract: undefined,
          retentionType: undefined,
          scInterimRetentionPercent: undefined,
          scMOSRetentionPercent: undefined,
          scMaxRetentionPercent: undefined,
          scPaymentTerm: undefined
        })
      })
      .catch(() => {
        showTotas('Fail', 'Fail')
      })
  }

  const Delete = async () => {
    if (!deleteDetails.current.length) {
      showTotas('Warn', 'No Rows to delete')
      return
    }

    details?.forEach(item => {
      deleteDetails.current = deleteDetails.current.map(i => {
        if (item.id === i.id) {
          i = { ...item }
        }
        return i
      })
    })
    deleteDetails.current.map(item => {
      const { scPaymentTerm } = item
      item.scPaymentTerm = scPaymentTerm?.split('-')[0].trim()
      return item
    })

    await deleteData(deleteDetails.current)
      .unwrap()
      .then(() => {
        showTotas('Success', `Deleted  ${deleteDetails.current.length} records`)
      })
      .catch(error => {
        showTotas('Fail', error)
      })
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
    if (updateLoading || deleteLoading || createLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [updateLoading, deleteLoading, createLoading])

  const cellSave = (args: CellSaveEventArgs) => {
    const index = getAddressIndex(args.address)
    const key = getAddressKey(args.address)
    if (details) {
      detail.current = { id: details[index].id, [`${key}`]: args.value }
      let obj: ScStandardTerms = {}
      if (updateDetails.current.find(item => item.id === detail.current.id)) {
        obj =
          updateDetails.current.find(item => item.id === detail.current.id) ??
          {}
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
  }

  const dataSourceChanged = (args: DataSourceChangedEventArgs) => {
    if (args.action === 'delete') {
      args.data?.forEach(item => {
        deleteDetails.current.push(item)
      })
    }
  }

  const dialogClose = () => {
    setDialogVisibility(false)
  }

  return (
    <div className="admin-container">
      <div className="admin-content">
        <SpreadsheetComponent
          ref={spreadsheetRef}
          allowEditing={!!details}
          cellSave={cellSave}
          dataSourceChanged={dataSourceChanged}
        >
          <SheetsDirective>
            <SheetDirective
              name="Standard Terms"
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
                    <CellDirective value="Company"></CellDirective>
                    <CellDirective value="Form of Subcontract"></CellDirective>
                    <CellDirective value="SC Payment Term"></CellDirective>
                    <CellDirective value="SC Max Retention %"></CellDirective>
                    <CellDirective value="SC Interim Retention %"></CellDirective>
                    <CellDirective value="SC MOS Retention %"></CellDirective>
                    <CellDirective value="Retention Type"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective hidden={true} width={80}></ColumnDirective>
                <ColumnDirective width={80}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective
                  width={180}
                  isLocked={false}
                  validation={{
                    type: 'List',
                    value1: getPaymentTerm(),
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
                <ColumnDirective width={80} isLocked={false}></ColumnDirective>
                <ColumnDirective width={80} isLocked={false}></ColumnDirective>
                <ColumnDirective width={80} isLocked={false}></ColumnDirective>
                <ColumnDirective
                  width={180}
                  isLocked={false}
                  validation={{
                    type: 'List',
                    value1: getRetentionTerm(),
                    ignoreBlank: false
                  }}
                ></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
      <div className="row">
        <div className="col-lg-4 col-md-4">
          <ButtonComponent cssClass="e-info full-btn" onClick={update}>
            Update Modified Standard Terms
          </ButtonComponent>
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent cssClass="e-warning full-btn" onClick={Delete}>
            Delete Selected Standard Terms
          </ButtonComponent>
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent cssClass="e-success full-btn" onClick={create}>
            Create New Standard Terms
          </ButtonComponent>
        </div>
      </div>
      <DialogComponent
        width="600px"
        visible={visibility}
        showCloseIcon={true}
        isModal={true}
        close={dialogClose}
        header={() => <p>Add New System Constant</p>}
        footerTemplate={() => (
          <div className="row">
            <div className="col-lg-12 col-md-12">
              <ButtonComponent
                cssClass="e-info full-btn"
                disabled={createLoading}
                onClick={Add}
              >
                Add
              </ButtonComponent>
            </div>
          </div>
        )}
      >
        <div>
          <span className="e-icons e-star-filled" />
          Please input the required fields below.
          <div className="row">
            <div className="col-lg-6 col-md-6">
              <DropDownListComponent
                cssClass="e-outline"
                floatLabelType="Always"
                showClearButton
                placeholder="Company"
                dataSource={companyList}
                value={add.company}
                onChange={(args: DropdownChangeEventArgs) => {
                  setAdd({
                    ...add,
                    company: String(args.value)
                  })
                }}
              />
            </div>
            <div className="col-lg-6 col-md-6">
              <DropDownListComponent
                fields={IDFIELDS}
                cssClass="e-outline"
                floatLabelType="Always"
                showClearButton
                placeholder="Form of Subcontract"
                dataSource={GLOBALPARAMETER.formOfSubcontract}
                value={add.formOfSubcontract}
                onChange={(args: DropdownChangeEventArgs) => {
                  setAdd({
                    ...add,
                    formOfSubcontract: String(args.value)
                  })
                }}
              />
            </div>
          </div>
          <div className="row">
            <div className="col-lg-6 col-md-6">
              <DropDownListComponent
                fields={IDFIELDS}
                cssClass="e-outline"
                floatLabelType="Always"
                showClearButton
                placeholder="SC Payment Term"
                dataSource={GLOBALPARAMETER.paymentTerms}
                value={add.scPaymentTerm}
                onChange={(args: DropdownChangeEventArgs) => {
                  setAdd({
                    ...add,
                    scPaymentTerm: String(args.value)
                  })
                }}
              />
            </div>
            <div className="col-lg-6 col-md-6">
              <DropDownListComponent
                fields={IDFIELDS}
                cssClass="e-outline"
                floatLabelType="Always"
                showClearButton
                placeholder="Retention Type"
                dataSource={GLOBALPARAMETER.retentionTerms}
                value={add.retentionType}
                onChange={(args: DropdownChangeEventArgs) => {
                  setAdd({
                    ...add,
                    retentionType: String(args.value)
                  })
                }}
              />
            </div>
          </div>
          <div className="row">
            <div className="col-lg-6 col-md-6">
              <NumericTextBoxComponent
                placeholder="SC Max Retention %"
                floatLabelType="Auto"
                format="##"
                cssClass="e-outline"
                value={add.scMaxRetentionPercent}
                change={(args: FocusOutEventArgs) => {
                  setAdd({
                    ...add,
                    scMaxRetentionPercent: Number(args.value)
                  })
                }}
              />
            </div>
            <div className="col-lg-6 col-md-6">
              <NumericTextBoxComponent
                placeholder="SC Interim Retention %"
                floatLabelType="Auto"
                format="##"
                cssClass="e-outline"
                value={add.scInterimRetentionPercent}
                change={(args: FocusOutEventArgs) => {
                  setAdd({
                    ...add,
                    scInterimRetentionPercent: Number(args.value)
                  })
                }}
              />
            </div>
          </div>
          <div className="row">
            <div className="col-lg-6 col-md-6">
              <NumericTextBoxComponent
                placeholder="SC MOS Retention %"
                floatLabelType="Auto"
                format="##"
                cssClass="e-outline"
                value={add.scMOSRetentionPercent}
                change={(args: FocusOutEventArgs) => {
                  setAdd({
                    ...add,
                    scMOSRetentionPercent: Number(args.value)
                  })
                }}
              />
            </div>
          </div>
        </div>
      </DialogComponent>
    </div>
  )
}

export default StandardTermsMaintenance
