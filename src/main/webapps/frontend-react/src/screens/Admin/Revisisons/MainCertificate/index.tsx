import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  FocusOutEventArgs,
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../../components/DatePicker'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  Certificate,
  useGetCertificateMutation,
  useUpdateCertificateByAdminMutation
} from '../../../../services'
import { validateJobNo } from '../helper'

const MainCertificate = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const [certificate, setCertificate] = useState<Certificate>({})
  const [searchRecord, setSearchRecord] = useState<{
    jobNo?: string
    certificateNumber?: number
  }>({
    jobNo: undefined,
    certificateNumber: undefined
  })

  const [getCertificate, { isLoading }] = useGetCertificateMutation()
  const [upateCertificate, { isLoading: updateLoading }] =
    useUpdateCertificateByAdminMutation()

  const search = async () => {
    await getCertificate(searchRecord)
      .unwrap()
      .then(payload => {
        if (payload instanceof Object) {
          setCertificate(payload)
        } else {
          showTotas('Warn', 'Main Cert not found!')
        }
      })
      .catch(() => {
        showTotas('Fail', 'Fail')
      })
  }

  const update = async () => {
    await upateCertificate(certificate)
      .unwrap()
      .then(payload => {
        if (payload !== '') {
          showTotas('Success', 'MainCert updated.')
        } else {
          showTotas('Fail', payload)
        }
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
    if (isLoading || updateLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading, updateLoading])

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-4 col-md-4">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={searchRecord.jobNo}
            blur={(value: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                jobNo: value.value
              })
              validateJobNo(value)
            }}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <NumericTextBoxComponent
            placeholder="Certificate Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="#"
            value={searchRecord.certificateNumber}
            change={(value: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                certificateNumber: Number(value.value)
              })
            }}
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={search}
            disabled={!(searchRecord.jobNo && searchRecord.certificateNumber)}
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      {certificate.jobNo && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Certificate Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  readOnly
                  value={certificate.certificateNumber}
                  blur={(value: FocusOutEventArgs) => {
                    setCertificate({
                      ...certificate,
                      certificateNumber: value.value
                    })
                  }}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Client Certificate No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={certificate.clientCertNo}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  readOnly
                  value={certificate.jobNo}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Certificate Status"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={certificate.certificateStatus}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="AR Document Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="#"
                  value={certificate.arDocNumber}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Total Receipt Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.totalReceiptAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="GST Payable"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.gstPayable}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="GST Receivable"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.gstReceivable}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Adjustment Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedAdjustmentAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Adjustment Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedAdjustmentAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Advance Payment"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedAdvancePayment}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Advance Payment"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedAdvancePayment}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Contra Charge Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedContraChargeAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Contra Charge Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedContraChargeAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied CPF Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedCPFAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified CPF Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedCPFAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Main Contractor Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedMainContractorAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Main Contractor Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedMainContractorAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Main Contractor Retention"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedMainContractorRetention}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Main Contractor Retention"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedMainContractorRetention}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Main Contractor Retention Released"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedMainContractorRetentionReleased}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Main Contractor Retention Released"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedMainContractorRetentionReleased}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied MOS Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedMOSAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified MOS Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedMOSAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied MOS Retention"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedMOSRetention}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified MOS Retention"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedMOSRetentionReleased}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied NSCNDSC Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedNSCNDSCAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified NSCNDSC Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedNSCNDSCAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Retention for NSCNDSC"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedRetentionforNSCNDSC}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Retention for NSCNDSC"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedRetentionforNSCNDSC}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Retention for NSCNDSC Released"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedRetentionforNSCNDSCReleased}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Retention for NSCNDSC Released"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedRetentionforNSCNDSCReleased}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Claims Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedClaimsAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Claims Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedClaimsAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Applied Variation Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.appliedVariationAmount}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <NumericTextBoxComponent
                  placeholder="Certified Variation Amount"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  format="'$'###.##"
                  step={0.01}
                  value={certificate.certifiedVariationAmount}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="Certificate Issue Date"
                  value={certificate.certIssueDate}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="Certificate Status Change Date"
                  value={certificate.certStatusChangeDate}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="Certificate As At Date"
                  value={certificate.certAsAtDate}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="Certificate Due Date"
                  value={certificate.certDueDate}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="Date"
                  value={certificate.ipaSubmissionDate}
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="IPA Sentout Date"
                  value={certificate.ipaSentoutDate}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <DatePicker
                  placeholder="Actual Receipt Date"
                  value={certificate.actualReceiptDate}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Remark"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline
                  value={certificate.remark}
                  blur={(value: FocusOutEventArgs) => {
                    setCertificate({
                      ...certificate,
                      remark: value.value ?? ''
                    })
                  }}
                />
              </div>
            </div>
          </div>
          {isQsAdm && (
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn" onClick={update}>
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

export default MainCertificate
