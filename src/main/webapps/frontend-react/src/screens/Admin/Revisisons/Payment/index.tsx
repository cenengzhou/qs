import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
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

import DatePicker from '../../../../components/DatePicker'
import { FIELDS, GLOBALPARAMETER } from '../../../../constants/global'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  Payment,
  useDeletePendingPaymentAndDetailsMutation,
  useGetPaymentCertMutation,
  useUpdatePaymentCertMutation
} from '../../../../services'
import dayjs from 'dayjs'

const PaymentRender = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const [getPaymentCert, { isLoading: getLoading }] =
    useGetPaymentCertMutation()
  const [deletePaymentCert, { isLoading: deleteLoading }] =
    useDeletePendingPaymentAndDetailsMutation()
  const [updatePaymentCert, { isLoading: updateLoading }] =
    useUpdatePaymentCertMutation()
  const [paymentRecord, setPaymentRecord] = useState<Payment>({})
  const [paymentSearch, setPaymentSearch] = useState<{
    jobNo?: string
    subcontractNo?: number
    paymentCertNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined, paymentCertNo: undefined })
  const search = async () => {
    await getPaymentCert(paymentSearch)
      .unwrap()
      .then(payload => {
        if (payload instanceof Object) {
          setPaymentRecord(payload)
        } else {
          setPaymentRecord({})
          showTotas('Warn', 'Payment certificate not found!')
        }
      })
      .catch((error: CustomError) => {
        showTotas('Fail', error.data.message)
      })
  }

  const deletePayment = async () => {
    if (paymentRecord.id)
      await deletePaymentCert(paymentRecord.id)
        .unwrap()
        .then(() => {
          setPaymentRecord({})
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
  }

  const updatePayment = async () => {
    await updatePaymentCert(paymentRecord)
      .unwrap()
      .then(() => {
        showTotas('Success', 'Payment Certificate updated.')
      })
      .catch((error: CustomError) => {
        showTotas('Fail', error.data.message)
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
    if (getLoading || updateLoading || deleteLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [getLoading, updateLoading, deleteLoading])

  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            input={(value: InputEventArgs) => {
              setPaymentSearch({ ...paymentSearch, jobNo: value.value })
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            change={(value: InputChangeEventArgs) => {
              setPaymentSearch({
                ...paymentSearch,
                subcontractNo: Number(value.value) ?? undefined
              })
            }}
            format="####"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Payment Certificate No"
            floatLabelType="Auto"
            cssClass="e-outline"
            change={(value: InputChangeEventArgs) => {
              setPaymentSearch({
                ...paymentSearch,
                paymentCertNo: Number(value.value) ?? undefined
              })
            }}
            format="####"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={search}
            disabled={
              !(
                !!paymentSearch.jobNo &&
                !!paymentSearch.subcontractNo &&
                !!paymentSearch.paymentCertNo
              )
            }
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      {!!paymentRecord.jobNo && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={paymentRecord.jobNo}
                  readOnly
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontract Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={paymentRecord.packageNo}
                  readOnly
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Payment Certificate Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={String(paymentRecord.paymentCertNo)}
                  blur={(value: FocusOutEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      paymentCertNo: value.value
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Vendor No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={paymentRecord.vendorNo}
                  blur={(value: FocusOutEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      vendorNo: value.value
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={FIELDS}
                  dataSource={GLOBALPARAMETER.directPayment}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Direct Payment"
                  value={paymentRecord.directPayment}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      directPayment: String(value.value)
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={FIELDS}
                  dataSource={GLOBALPARAMETER.paymentStatus}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Status"
                  value={paymentRecord.paymentStatus}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      paymentStatus: String(value.value)
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  fields={FIELDS}
                  dataSource={GLOBALPARAMETER.intermFinalPayment}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Interm Final Payment"
                  value={paymentRecord.intermFinalPayment}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      intermFinalPayment: String(value.value)
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={paymentRecord.addendumAmount}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      addendumAmount: Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Certificate Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={paymentRecord.certAmount}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      certAmount: Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Remeasure Contract Sum"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="n2"
                  value={paymentRecord.remeasureContractSum}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      remeasureContractSum: Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Main Contract Payment Cert No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="######"
                  value={paymentRecord.mainContractPaymentCertNo}
                  change={(value: DropdownChangeEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      mainContractPaymentCertNo:
                        Number(value.value) ?? undefined
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Final Payment Issued Date"
                  value={paymentRecord.asAtDate}
                  onChange={(args: calendarsChangedEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      asAtDate: dayjs(args.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="First Payment Cert Issued Date"
                  value={paymentRecord.certIssueDate}
                  onChange={(args: calendarsChangedEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      certIssueDate: dayjs(args.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Last Addendum Value Update Date"
                  value={paymentRecord.dueDate}
                  onChange={(args: calendarsChangedEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      dueDate: dayjs(args.value).format('YYYY-MM-DD')
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="IPA or Invoice Received Date"
                  value={paymentRecord.ipaOrInvoiceReceivedDate ?? ''}
                  onChange={(args: calendarsChangedEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      ipaOrInvoiceReceivedDate: dayjs(args.value).format(
                        'YYYY-MM-DD'
                      )
                    })
                  }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <TextBoxComponent
                  placeholder="Explanation"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline={true}
                  value={paymentRecord.explanation ?? ''}
                  blur={(value: FocusOutEventArgs) => {
                    setPaymentRecord({
                      ...paymentRecord,
                      explanation: value.value
                    })
                  }}
                />
              </div>
            </div>
          </div>
          {isQsAdm && (
            <>
              {paymentRecord.paymentStatus !== 'PND' && (
                <div className="row">
                  <div className="col-lg-12 col-md-12">
                    <ButtonComponent
                      cssClass="e-info full-btn"
                      onClick={updatePayment}
                    >
                      Update
                    </ButtonComponent>
                  </div>
                </div>
              )}
              {paymentRecord.paymentStatus === 'PND' && (
                <div className="row">
                  <div className="col-lg-6 col-md-6">
                    <ButtonComponent
                      cssClass="e-info full-btn"
                      onClick={updatePayment}
                    >
                      Update
                    </ButtonComponent>
                  </div>
                  <div className="col-lg-6 col-md-6">
                    <ButtonComponent
                      cssClass="e-danger full-btn"
                      onClick={deletePayment}
                    >
                      Delete
                    </ButtonComponent>
                  </div>
                </div>
              )}
            </>
          )}
        </>
      )}
    </div>
  )
}

export default PaymentRender
