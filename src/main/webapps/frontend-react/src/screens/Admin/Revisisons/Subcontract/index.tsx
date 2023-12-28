import { useState } from 'react'

import {
  ButtonComponent,
  ChangeEventArgs as CheckBoxChangeEventArgs,
  CheckBoxComponent
} from '@syncfusion/ej2-react-buttons'
import {
  DropDownListComponent,
  ChangeEventArgs as DropdownChangeEventArgs
} from '@syncfusion/ej2-react-dropdowns'
import {
  FocusOutEventArgs,
  ChangeEventArgs as InputChangeEventArgs,
  InputEventArgs,
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../../components/DatePicker'
import NotificationModal from '../../../../components/NotificationModal'
import { GLOBALPARAMETER } from '../../../../constants/global'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  Subcontract as SubcontractRecordProps,
  useGetAllWorkScopesQuery,
  useGetSubcontractMutation,
  useUpdateSubcontractAdminMutation
} from '../../../../services'
import {
  PaymentMethodData,
  commonFields,
  regex,
  workScopesFields
} from './interface'
import './style.css'

const Subcontract = () => {
  const dispatch = useAppDispatch()

  const { data: workScopesData } = useGetAllWorkScopesQuery()
  const [getSubcontract] = useGetSubcontractMutation()
  const [updateSubcontract] = useUpdateSubcontractAdminMutation()

  const [SubcontractRecord, setSubcontractRecord] =
    useState<SubcontractRecordProps>({})
  const [subcontractSearch, setSubcontractSearch] = useState<{
    jobNo?: string
    subcontractNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined })
  const [notificationContent, setNotificationContent] = useState<string>('')
  const [visibleNotificationModal, setVisibleNotificationModal] =
    useState<boolean>(false)
  const [notificationMode, setNotificationMode] = useState<
    'Success' | 'Fail' | 'Warn'
  >('Success')

  const closeNotification = () => {
    setVisibleNotificationModal(false)
  }

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

  const getSubcontractSubmit = async () => {
    dispatch(openLoading())
    await getSubcontract(subcontractSearch)
      .unwrap()
      .then(payload => {
        dispatch(closeLoading())
        if (payload instanceof Object) {
          setSubcontractRecord(payload)
          console.log(SubcontractRecord)
        } else {
          setNotificationMode('Warn')
          setNotificationContent('Subcontract not found')
          setVisibleNotificationModal(true)
        }
      })
      .catch((error: CustomError) => {
        dispatch(closeLoading())
        setVisibleNotificationModal(true)
        setNotificationMode('Fail')
        setNotificationContent(error.data.message)
      })
  }

  const updateSubcontractSubmit = async () => {
    console.log(SubcontractRecord)
    dispatch(openLoading())
    await updateSubcontract(SubcontractRecord)
      .unwrap()
      .then(() => {
        dispatch(closeLoading())
        setNotificationMode('Success')
        setNotificationContent('Subcontract updated.')
        setVisibleNotificationModal(true)
      })
      .catch((error: CustomError) => {
        dispatch(closeLoading())
        setNotificationMode('Fail')
        setNotificationContent(error.data.message)
        setVisibleNotificationModal(true)
        console.error(error)
      })
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
            value={subcontractSearch.jobNo}
            input={(value: InputEventArgs) => {
              setSubcontractSearch({ ...subcontractSearch, jobNo: value.value })
              validateInput(value)
            }}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={subcontractSearch.subcontractNo}
            format="######"
            change={(value: InputChangeEventArgs) => {
              setSubcontractSearch({
                ...subcontractSearch,
                subcontractNo: Number(value.value) ?? undefined
              })
            }}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={getSubcontractSubmit}
            disabled={
              !(subcontractSearch.subcontractNo && subcontractSearch.jobNo)
            }
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      {/* content */}

      {SubcontractRecord.jobInfo && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.jobInfo?.jobNo}
                  readOnly
                  // SubcontractRecord.jobInfo.jobNo
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontract Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.packageNo}
                  readOnly
                  // SubcontractRecord.packageNo
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Vendor Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.vendorNo}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      vendorNo: value.value
                    })
                  }}
                  // SubcontractRecord.vendorNo
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontractor Name"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.nameSubcontractor}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      nameSubcontractor: value.value
                    })
                  }}
                  // SubcontractRecord.nameSubcontractor
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Approval Route"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.approvalRoute}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      approvalRoute: value.value
                    })
                  }}
                  // SubcontractRecord.approvalRoute
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Internal Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.internalJobNo}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      internalJobNo: value.value
                    })
                  }}
                  // SubcontractRecord.internalJobNo
                />
              </div>
            </div>
            {/* input */}
            {/* textarea */}
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Notes"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline={true}
                  value={SubcontractRecord?.notes}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      notes: value.value
                    })
                  }}
                  // SubcontractRecord.notes
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Description"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.description}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      description: value.value
                    })
                  }}
                  // SubcontractRecord.description
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Payment Terms Description"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.paymentTermsDescription}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      paymentTermsDescription: value.value
                    })
                  }}
                  // SubcontractRecord.paymentTermsDescription
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Period for Payment"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.periodForPayment}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      periodForPayment: value.value
                    })
                  }}
                  // SubcontractRecord.periodForPayment
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Method of execution of main certificate"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.executionMethodMainContract}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      executionMethodMainContract: value.value
                    })
                  }}
                  // SubcontractRecord.executionMethodMainContract
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Proposed method of execution"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.executionMethodPropsed}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      executionMethodPropsed: value.value
                    })
                  }}
                  // SubcontractRecord.executionMethodPropsed
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Reason for use of LoA"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.reasonLoa}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      reasonLoa: value.value
                    })
                  }}
                  // SubcontractRecord.reasonLoa
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Reason for relaxation to three minimum quotations requirement"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.reasonQuotation}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      reasonQuotation: value.value
                    })
                  }}
                  // SubcontractRecord.reasonQuotation
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Reason for change manner of execution from main contract"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={SubcontractRecord?.reasonManner}
                  blur={(value: FocusOutEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      reasonManner: value.value
                    })
                  }}
                  // SubcontractRecord.reasonManner
                />
              </div>
            </div>
            {/* textarea */}
            {/* dropdowns */}
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.cpfCalculation}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="CPF Calculation"
                  value={SubcontractRecord?.cpfCalculation}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      cpfCalculation: String(value.value)
                    })
                  }}
                  // SubcontractRecord.cpfCalculation
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.formOfSubcontract}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Form of Subcontract"
                  value={SubcontractRecord?.formOfSubcontract}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      formOfSubcontract: String(value.value)
                    })
                  }}
                  // SubcontractRecord.formOfSubcontract
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.packageType}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Package Type"
                  value={SubcontractRecord?.packageType}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      packageType: String(value.value)
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.paymentCurrency}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Currency"
                  value={SubcontractRecord?.paymentCurrency}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      paymentCurrency: String(value.value)
                    })
                  }}
                  // SubcontractRecord.paymentCurrency
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.paymentInformation}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Information"
                  value={SubcontractRecord?.paymentInformation}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      paymentInformation: String(value.value)
                    })
                  }}
                  // SubcontractRecord.paymentInformation
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.subcontractorNature}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Subcontractor Nature"
                  value={SubcontractRecord?.subcontractorNature}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      subcontractorNature: String(value.value)
                    })
                  }}
                  // SubcontractRecord.subcontractorNature
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.submittedAddendum}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Submitted Addendum"
                  value={SubcontractRecord?.submittedAddendum}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      submittedAddendum: String(value.value)
                    })
                  }}
                  // SubcontractRecord.submittedAddendum
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.paymentTerms}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Terms"
                  value={SubcontractRecord?.paymentTerms}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      paymentTerms: String(value.value)
                    })
                  }}
                  // SubcontractRecord.paymentTerms
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.retentionTerms}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Retention Terms"
                  value={SubcontractRecord?.retentionTerms}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      retentionTerms: String(value.value)
                    })
                  }}
                  // SubcontractRecord.retentionTerms
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.subcontractTerm}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Subcontract Terms"
                  value={SubcontractRecord?.subcontractTerm}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      subcontractTerm: String(value.value)
                    })
                  }}
                  // SubcontractRecord.subcontractTerm
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.packageStatus}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Package Status"
                  value={SubcontractRecord?.packageStatus}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      packageStatus: String(value.value)
                    })
                  }}
                  // SubcontractRecord.packageStatus
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.subcontract_paymentStatus}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Status"
                  value={SubcontractRecord?.paymentStatus}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      paymentStatus: String(value.value)
                    })
                  }}
                  // SubcontractRecord.paymentStatus
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.splitTerminateStatus}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Split Terminate Status"
                  value={SubcontractRecord?.splitTerminateStatus}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      splitTerminateStatus: String(value.value)
                    })
                  }}
                  // SubcontractRecord.splitTerminateStatus
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={commonFields}
                  dataSource={GLOBALPARAMETER.subcontractStatus}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Subcontract Status"
                  value={SubcontractRecord?.scStatus}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scStatus: Number(value.value)
                    })
                  }}
                  // SubcontractRecord.scStatus
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={workScopesFields}
                  dataSource={workScopesData}
                  value={SubcontractRecord?.workscope}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Work Scope"
                  change={(value: DropdownChangeEventArgs) =>
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      workscope: Number(value.value)
                    })
                  }
                  // SubcontractRecord.workscope
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={PaymentMethodData}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Method"
                  value={SubcontractRecord?.paymentMethod}
                  change={(value: DropdownChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      paymentMethod: String(value.value)
                    })
                  }}
                  // SubcontractRecord.paymentMethod
                />
              </div>
            </div>
            {/* dropdowns */}
            {/* checkbox */}
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <CheckBoxComponent
                  checked={SubcontractRecord?.labourIncludedContract}
                  label="Labour Included"
                  change={(value: CheckBoxChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      labourIncludedContract: value.checked
                    })
                  }}
                />
                {/* SubcontractRecord.labourIncludedContract */}
              </div>
              <div className="col-lg-4 col-md-4">
                <CheckBoxComponent
                  checked={SubcontractRecord?.materialIncludedContract}
                  label="Material Included"
                  change={(value: CheckBoxChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      materialIncludedContract: value.checked
                    })
                  }}
                />
                {/* SubcontractRecord. */}
              </div>
              <div className="col-lg-4 col-md-4">
                <CheckBoxComponent
                  checked={SubcontractRecord?.plantIncludedContract}
                  label="Plant Included"
                  change={(value: CheckBoxChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      plantIncludedContract: value.checked
                    })
                  }}
                />
                {/* SubcontractRecord.plantIncludedContract */}
              </div>
            </div>
            {/* checkbox */}

            {/* numericText */}
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="CPF Base Period"
                  floatLabelType="Auto"
                  format="n"
                  cssClass="e-outline"
                  value={SubcontractRecord?.cpfBasePeriod}
                  // SubcontractRecord.cpfBasePeriod
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="CPF Base Year"
                  floatLabelType="Auto"
                  format="n"
                  cssClass="e-outline"
                  value={Number(SubcontractRecord?.cpfBaseYear)}
                  // SubcontractRecord.cpfBaseYear
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Accumulated Retention"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="c2"
                  step={0.01}
                  value={SubcontractRecord?.accumlatedRetention}
                  // SubcontractRecord.accumlatedRetention
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Approved VO Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.approvedVOAmount}
                  // SubcontractRecord.approvedVOAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total AP Posted Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.totalAPPostedCertAmount}
                  // SubcontractRecord.totalAPPostedCertAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Exchange Rate"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="n2"
                  step={0.01}
                  value={SubcontractRecord?.exchangeRate}
                  // SubcontractRecord.exchangeRate
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Interim Retention %"
                  floatLabelType="Auto"
                  format="###.## '%'"
                  cssClass="e-outline"
                  value={SubcontractRecord?.interimRentionPercentage}
                  // SubcontractRecord.interimRentionPercentage
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Max Retention %"
                  floatLabelType="Auto"
                  format="###.## '%'"
                  cssClass="e-outline"
                  value={SubcontractRecord?.maxRetentionPercentage}
                  // SubcontractRecord.maxRetentionPercentage
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="MOS Retention %"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="###.## '%'"
                  value={SubcontractRecord?.mosRetentionPercentage}
                  // SubcontractRecord.mosRetentionPercentage
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Original SC Sum"
                  floatLabelType="Auto"
                  format="c2"
                  step={0.01}
                  cssClass="e-outline"
                  value={SubcontractRecord?.originalSubcontractSum}
                  // SubcontractRecord.originalSubcontractSum
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Remeasured SC Sum"
                  floatLabelType="Auto"
                  format="c2"
                  step={0.01}
                  cssClass="e-outline"
                  value={SubcontractRecord?.remeasuredSubcontractSum}
                  // SubcontractRecord.remeasuredSubcontractSum
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Retention Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="c2"
                  step={0.01}
                  value={SubcontractRecord?.retentionAmount}
                  // SubcontractRecord.retentionAmount
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Retention Release"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord.retentionReleased}
                  // SubcontractRecord.retentionReleased
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total CC Posted Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.totalCCPostedCertAmount}
                  // SubcontractRecord.totalCCPostedCertAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total Cumlated Cert Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="c2"
                  step={0.01}
                  value={SubcontractRecord?.totalCumCertifiedAmount}
                  // SubcontractRecord.totalCumCertifiedAmount
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total Cumulated Work Done Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.totalCumWorkDoneAmount}
                  // SubcontractRecord.totalCumWorkDoneAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total MOS Posted Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.totalMOSPostedCertAmount}
                  // SubcontractRecord.totalMOSPostedCertAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total Posted Cert Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="c2"
                  step={0.01}
                  value={SubcontractRecord?.totalPostedCertifiedAmount}
                  // SubcontractRecord.totalPostedCertifiedAmount
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Total Posted Work Done Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.totalPostedWorkDoneAmount}
                  // SubcontractRecord.totalPostedWorkDoneAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Package Stretch Target Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={Number(SubcontractRecord?.amountPackageStretchTarget)}
                  // SubcontractRecord.amountPackageStretchTarget
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Recoverable"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="c2"
                  step={0.01}
                  value={SubcontractRecord?.totalRecoverableAmount}
                  // SubcontractRecord.totalRecoverableAmount
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Non-Recoverable"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={SubcontractRecord?.totalNonRecoverableAmount}
                  // SubcontractRecord.totalNonRecoverableAmount
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="CEO Approved amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  step={0.01}
                  value={Number(SubcontractRecord?.amtCEDApproved)}
                  // SubcontractRecord.amtCEDApproved
                />
              </div>
            </div>
            {/* numericText */}

            {/* datePicker */}
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Final Payment Issued Date"
                  value={SubcontractRecord?.finalPaymentIssuedDate}
                />
                {/* SubcontractRecord.finalPaymentIssuedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="First Payment Cert Issued Date"
                  value={SubcontractRecord?.firstPaymentCertIssuedDate}
                />
                {/* SubcontractRecord.firstPaymentCertIssuedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Last Addendum Value Update Date"
                  value={SubcontractRecord?.latestAddendumValueUpdatedDate}
                />
                {/* SubcontractRecord.latestAddendumValueUpdatedDate */}
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Last Payment Cert Issueed Date"
                  value={SubcontractRecord?.lastPaymentCertIssuedDate}
                />
                {/* SubcontractRecord.lastPaymentCertIssuedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="LOA Signed Date"
                  value={SubcontractRecord.loaSignedDate}
                />
                {/* SubcontractRecord.loaSignedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Preaward Metting Date"
                  value={SubcontractRecord?.preAwardMeetingDate}
                />
                {/* SubcontractRecord.preAwardMeetingDate */}
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Requisition Approved Date"
                  value={SubcontractRecord.requisitionApprovedDate}
                />
                {/* SubcontractRecord.requisitionApprovedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Approval Date"
                  value={SubcontractRecord?.scApprovalDate}
                />
                {/* SubcontractRecord.scApprovalDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Award Approval Requestent Date"
                  value={SubcontractRecord?.scAwardApprovalRequestSentDate}
                />
                {/* SubcontractRecord.scAwardApprovalRequestSentDate */}
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Created Date"
                  value={SubcontractRecord?.scCreatedDate}
                />
                {/* SubcontractRecord.scCreatedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC DOC LEGAL Date"
                  value={SubcontractRecord?.scDocLegalDate}
                />
                {/* SubcontractRecord.scDocLegalDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Doc SCR Date"
                  value={SubcontractRecord?.scDocScrDate}
                />
                {/* SubcontractRecord.scDocScrDate */}
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="TA Approved Date"
                  value={SubcontractRecord?.tenderAnalysisApprovedDate}
                />
                {/* SubcontractRecord.tenderAnalysisApprovedDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Work Commence Date"
                  value={SubcontractRecord?.workCommenceDate}
                />
                {/* SubcontractRecord.workCommenceDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="On-site start Date"
                  value={SubcontractRecord?.onSiteStartDate}
                />
                {/* SubcontractRecord.onSiteStartDate */}
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC final account draft date"
                  value={SubcontractRecord?.scFinalAccDraftDate}
                />
                {/* SubcontractRecord.scFinalAccDraftDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC final account sign off date"
                  value={SubcontractRecord?.scFinalAccSignoffDate}
                />
                {/* SubcontractRecord.scFinalAccSignoffDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Target date for subcontract execution"
                  value={SubcontractRecord?.dateScExecutionTarget}
                />
                {/* SubcontractRecord.dateScExecutionTarget */}
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Sub-Contract Duration From"
                  value={SubcontractRecord?.durationFrom}
                />
                {/* SubcontractRecord.durationFrom */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Sub-Contract Duration To"
                  value={SubcontractRecord?.durationTo}
                />
                {/* SubcontractRecord.durationTo */}
              </div>
            </div>
            {/* datePicker */}
          </div>

          <div className="row">
            <div className="col-lg-12 col-md-12">
              {/* post service/subcontract/updateSubcontractAdmin */}
              <ButtonComponent
                cssClass="e-info full-btn"
                onClick={updateSubcontractSubmit}
              >
                Update
              </ButtonComponent>
            </div>
          </div>
        </>
      )}

      <NotificationModal
        mode={notificationMode}
        visible={visibleNotificationModal}
        dialogClose={closeNotification}
        content={notificationContent}
      />
    </div>
  )
}

export default Subcontract
