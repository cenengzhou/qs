/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useState } from 'react'

import {
  ButtonComponent,
  ChangeEventArgs as CheckBoxChangeEventArgs,
  CheckBoxComponent
} from '@syncfusion/ej2-react-buttons'
import { ChangedEventArgs as calendarsChangedEventArgs } from '@syncfusion/ej2-react-calendars'
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

import { regex } from '..'
import DatePicker from '../../../../components/DatePicker'
import { GLOBALPARAMETER } from '../../../../constants/global'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  Subcontract as SubcontractRecordProps,
  useGetAllWorkScopesQuery,
  useGetSubcontractMutation,
  useUpdateSubcontractAdminMutation
} from '../../../../services'
import { PaymentMethodData, commonFields, workScopesFields } from './interface'
import './style.css'
import dayjs from 'dayjs'

const Subcontract = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const { data: workScopesData } = useGetAllWorkScopesQuery()
  const [getSubcontract, { isLoading }] = useGetSubcontractMutation()
  const [updateSubcontract, { isLoading: updateLoading }] =
    useUpdateSubcontractAdminMutation()

  const [SubcontractRecord, setSubcontractRecord] =
    useState<SubcontractRecordProps>({})
  const [subcontractSearch, setSubcontractSearch] = useState<{
    jobNo?: string
    subcontractNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined })

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
    await getSubcontract(subcontractSearch)
      .unwrap()
      .then(payload => {
        if (payload instanceof Object) {
          setSubcontractRecord(payload)
        } else {
          showTotas('Warn', 'Subcontract not found')
        }
      })
      .catch((error: CustomError) => {
        showTotas('Fail', error.data.message)
      })
  }

  const updateSubcontractSubmit = async () => {
    await updateSubcontract(SubcontractRecord)
      .unwrap()
      .then(() => {
        showTotas('Success', 'Subcontract updated.')
      })
      .catch((error: CustomError) => {
        showTotas('Fail', error.data.message)

        console.error(error)
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
    if (isLoading || updateLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading, updateLoading])

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
              !(
                regex.test(subcontractSearch.jobNo ?? '') &&
                subcontractSearch.subcontractNo &&
                subcontractSearch.jobNo
              )
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
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontract Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={SubcontractRecord?.packageNo}
                  readOnly
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      cpfBasePeriod: Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="CPF Base Year"
                  floatLabelType="Auto"
                  format="n"
                  cssClass="e-outline"
                  value={Number(SubcontractRecord?.cpfBaseYear)}
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      cpfBaseYear: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      accumlatedRetention: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      approvedVOAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalAPPostedCertAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      exchangeRate: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      interimRentionPercentage: Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Max Retention %"
                  floatLabelType="Auto"
                  format="###.## '%'"
                  cssClass="e-outline"
                  value={SubcontractRecord?.maxRetentionPercentage}
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      maxRetentionPercentage: Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="MOS Retention %"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="###.## '%'"
                  value={SubcontractRecord?.mosRetentionPercentage}
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      mosRetentionPercentage: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      originalSubcontractSum: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      remeasuredSubcontractSum: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      retentionAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      retentionReleased: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalCCPostedCertAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalCumCertifiedAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalCumWorkDoneAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalMOSPostedCertAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalPostedCertifiedAmount:
                        Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalPostedWorkDoneAmount:
                        Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      amountPackageStretchTarget:
                        Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalRecoverableAmount: Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      totalNonRecoverableAmount:
                        Number(value.value) ?? undefined
                    })
                  }}
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
                  change={(value: InputChangeEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      amtCEDApproved: Number(value.value) ?? undefined
                    })
                  }}
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
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      finalPaymentIssuedDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="First Payment Cert Issued Date"
                  value={SubcontractRecord?.firstPaymentCertIssuedDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      firstPaymentCertIssuedDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Last Addendum Value Update Date"
                  value={SubcontractRecord?.latestAddendumValueUpdatedDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      latestAddendumValueUpdatedDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Last Payment Cert Issueed Date"
                  value={SubcontractRecord?.lastPaymentCertIssuedDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      lastPaymentCertIssuedDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="LOA Signed Date"
                  value={SubcontractRecord.loaSignedDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      loaSignedDate: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Preaward Metting Date"
                  value={SubcontractRecord?.preAwardMeetingDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      preAwardMeetingDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Requisition Approved Date"
                  value={SubcontractRecord.requisitionApprovedDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      requisitionApprovedDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Approval Date"
                  value={SubcontractRecord?.scApprovalDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scApprovalDate: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Award Approval Requestent Date"
                  value={SubcontractRecord?.scAwardApprovalRequestSentDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scAwardApprovalRequestSentDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
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
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scDocLegalDate: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC Doc SCR Date"
                  value={SubcontractRecord?.scDocScrDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scDocScrDate: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="TA Approved Date"
                  value={SubcontractRecord?.tenderAnalysisApprovedDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      tenderAnalysisApprovedDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Work Commence Date"
                  value={SubcontractRecord?.workCommenceDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      workCommenceDate: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="On-site start Date"
                  value={SubcontractRecord?.onSiteStartDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      onSiteStartDate: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC final account draft date"
                  value={SubcontractRecord?.scFinalAccDraftDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scFinalAccDraftDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="SC final account sign off date"
                  value={SubcontractRecord?.scFinalAccSignoffDate}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scFinalAccSignoffDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
                {/* SubcontractRecord.scFinalAccSignoffDate */}
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Target date for subcontract execution"
                  value={SubcontractRecord?.dateScExecutionTarget}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      scFinalAccSignoffDate: dayjs(value.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Sub-Contract Duration From"
                  value={SubcontractRecord?.durationFrom}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      durationFrom: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Sub-Contract Duration To"
                  value={SubcontractRecord?.durationTo}
                  onChange={(value: calendarsChangedEventArgs) => {
                    setSubcontractRecord({
                      ...SubcontractRecord,
                      durationTo: dayjs(value.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
            </div>
            {/* datePicker */}
          </div>

          {isQsAdm && (
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
          )}
        </>
      )}
    </div>
  )
}

export default Subcontract
