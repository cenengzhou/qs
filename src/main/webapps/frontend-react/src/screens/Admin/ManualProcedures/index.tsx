import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { ChangedEventArgs as calendarsChangedEventArgs } from '@syncfusion/ej2-react-calendars'
import {
  ChangeEventArgs,
  DropDownListComponent
} from '@syncfusion/ej2-react-dropdowns'
import { InputEventArgs, TextBoxComponent } from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../components/DatePicker'
import { GLOBALPARAMETER } from '../../../constants/global'
import { closeLoading, openLoading } from '../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../redux/notificationReducer'
import { useAppDispatch } from '../../../redux/store'
import {
  CustomError,
  useGeneratePaymentPDFAdminMutation,
  useGenerateSCPackageSnapshotManuallyMutation,
  useGetAuditTableMapQuery,
  useGetCutoffPeriodQuery,
  useHousekeepAuditTableMutation,
  useRunPaymentPostingMutation,
  useRunProvisionPostingManuallyMutation,
  useUpdateCEOApprovalMutation,
  useUpdateF58001FromSCPackageManuallyMutation,
  useUpdateF58011FromSCPaymentCertManuallyMutation,
  useUpdateMainCertFromF03B14ManuallyMutation
} from '../../../services'
import './style.css'
import dayjs from 'dayjs'

const ManualProcedures = () => {
  const dispatch = useAppDispatch()

  const { data: AuditTable } = useGetAuditTableMapQuery()
  const { data: CutoffPeriod } = useGetCutoffPeriodQuery()
  const [updateHouseKeep, { isLoading: houseKeepLoading }] =
    useHousekeepAuditTableMutation()
  const [updateProvisionPosting, { isLoading: provisionPostingLoading }] =
    useRunProvisionPostingManuallyMutation()
  const [updateCeoApproval, { isLoading: ceoApprovalLoading }] =
    useUpdateCEOApprovalMutation()
  const [generateSnapshot, { isLoading: generateSnapshotLoading }] =
    useGenerateSCPackageSnapshotManuallyMutation()
  const [postSubcontractPayment, { isLoading: paymentLoading }] =
    useRunPaymentPostingMutation()
  const [synchronizeSubcontractPackage, { isLoading: packageLoading }] =
    useUpdateF58001FromSCPackageManuallyMutation()
  const [synchronizeSubcontractPaymentCert, { isLoading: paymentCertLoading }] =
    useUpdateF58011FromSCPaymentCertManuallyMutation()
  const [synchronizeMainContract, { isLoading: mainContractLoading }] =
    useUpdateMainCertFromF03B14ManuallyMutation()
  const [generatePaymentCertPdf, { isLoading: certPdfLoading }] =
    useGeneratePaymentPDFAdminMutation()

  const auditTables = Object.values(AuditTable ?? {}).map((item, index) => {
    return {
      value: index,
      text: item.tableName
    }
  })

  const cutoffMonth = GLOBALPARAMETER.getChineseMonth(
    CutoffPeriod?.cutoffDate?.split(' ')[1].replace('月,', '')
  )
  // format yyyy-MM-dd
  const newCutoffPeriod =
    cutoffMonth +
    '-' +
    CutoffPeriod?.cutoffDate?.split(' ')[0] +
    '-' +
    CutoffPeriod?.cutoffDate?.split(' ')[2]

  const [auditTableName, setAuditTableName] = useState(auditTables[0]?.text)
  const [provisionPostingJobNo, setProvisionPostingJobNo] = useState<string>('')
  const [provisionPostingDate, setProvisionPostingDate] = useState<string>(
    dayjs(new Date()).format('YYYY-MM-DD')
  )
  const [updateCeoApprovalJobNo, setUpdateCeoApprovalJobNo] =
    useState<string>('')
  const [updateCeoApprovalPackageNo, setUpdateCeoApprovalPackageNo] =
    useState<string>('')
  const [paymentCertPdfJobNo, setPaymentCertPdfJobNo] = useState<string>('')
  const [paymentCertPdfPackageNo, setPaymentCertPdfPackageNo] =
    useState<string>('')
  const [paymentCertPdfPaymentNo, setPaymentCertPdfPaymentNo] =
    useState<string>('')

  const houseKeepSubmit = async () => {
    try {
      await updateHouseKeep(auditTableName)
        .unwrap()
        .then(payload => {
          if (payload) {
            showTotas(
              'Success',
              `Removed ${payload} records from ${auditTableName}`
            )
          }
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('failed:', err)
    }
  }

  const provisionPostingSubmit = async () => {
    try {
      await updateProvisionPosting({
        jobNumber: provisionPostingJobNo,
        glDate: provisionPostingDate
      })
        .unwrap()
        .then(() => {
          showTotas('Success', 'Posted.')
          setProvisionPostingJobNo('')
          setProvisionPostingDate(dayjs(new Date()).format('YYYY-MM-DD'))
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error(err)
    }
  }

  const updateCeoApprovalSubmit = async () => {
    try {
      await updateCeoApproval({
        jobNumber: updateCeoApprovalJobNo,
        packageNo: updateCeoApprovalPackageNo
      })
        .unwrap()
        .then(() => {
          showTotas('Success', 'Update CED Approval completed.')
          setUpdateCeoApprovalJobNo('')
          setUpdateCeoApprovalPackageNo('')
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('Failed:', err)
    }
  }

  const generateSnapshotSubmit = async () => {
    try {
      await generateSnapshot()
        .unwrap()
        .then(() => {
          showTotas('Success', 'Generate Payment Cert PDF completed.')
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('Failed:', err)
    }
  }

  const postSubcontractPaymentSubmit = async () => {
    try {
      await postSubcontractPayment()
        .unwrap()
        .then(() => {
          showTotas('Success', 'Success')
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('failed:', err)
    }
  }

  const synchronizeF58001Submit = async () => {
    try {
      await synchronizeSubcontractPackage()
        .unwrap()
        .then(() => {
          showTotas('Success', 'Success')
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('Failed:', err)
    }
  }

  const synchronizeSubcontractPaymentCertSubmit = async () => {
    try {
      await synchronizeSubcontractPaymentCert()
        .unwrap()
        .then(() => {
          showTotas('Success', 'Success')
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('Failed:', err)
    }
  }

  const synchronizeMainContractSubmit = async () => {
    try {
      await synchronizeMainContract()
        .unwrap()
        .then(() => {
          showTotas('Success', 'Success')
        })
        .catch(error => {
          showTotas('Fail', error.data.message)
        })
    } catch (err) {
      console.error('Failed:', err)
    }
  }
  const generatePaymentCertPdfSubmit = async () => {
    try {
      await generatePaymentCertPdf({
        jobNo: paymentCertPdfJobNo,
        packageNo: paymentCertPdfPackageNo,
        paymentNo: paymentCertPdfPaymentNo
      })
        .unwrap()
        .then(() => {
          showTotas('Success', 'Success')
          setPaymentCertPdfJobNo('')
          setPaymentCertPdfPackageNo('')
          setPaymentCertPdfPaymentNo('')
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
      // {data:"1 payment PDF(s) failed to generate. Please check the log file."}
    } catch (err) {
      console.error('Failed:', err)
    }
  }

  const showTotas = (mode: 'Fail' | 'Success', msg?: string) => {
    dispatch(
      setNotificationVisible({
        visible: true,
        mode: mode,
        content: msg
      })
    )
  }

  const changeAudiTableName = (value: ChangeEventArgs) => {
    setAuditTableName(value.value)
  }
  const changeProvisionPostingJobNo = (value?: string) => {
    setProvisionPostingJobNo(value ?? '')
  }
  const changeProvisionPostingDate = (e: calendarsChangedEventArgs) => {
    setProvisionPostingDate(dayjs(e.value).format('YYYY-MM-DD') ?? new Date())
  }
  const changeUpdateCeoApprovalJobNo = (value?: string) => {
    setUpdateCeoApprovalJobNo(value ?? '')
  }
  const changeUpdateCeoApprovalPackageNo = (value?: string) => {
    setUpdateCeoApprovalPackageNo(value ?? '')
  }
  const changeUpadatePaymentCertPdfJobNo = (value?: string) => {
    setPaymentCertPdfJobNo(value ?? '')
  }
  const changeUpadatePaymentCertPdfPackageNo = (value?: string) => {
    setPaymentCertPdfPackageNo(value ?? '')
  }
  const changeUpadatePaymentCertPdfPaymentNo = (value?: string) => {
    setPaymentCertPdfPaymentNo(value ?? '')
  }

  useEffect(() => {
    if (
      houseKeepLoading ||
      provisionPostingLoading ||
      ceoApprovalLoading ||
      generateSnapshotLoading ||
      paymentLoading ||
      packageLoading ||
      paymentCertLoading ||
      mainContractLoading ||
      certPdfLoading
    ) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [
    houseKeepLoading,
    provisionPostingLoading,
    ceoApprovalLoading,
    generateSnapshotLoading,
    paymentLoading,
    packageLoading,
    paymentCertLoading,
    mainContractLoading,
    certPdfLoading
  ])

  return (
    <div className="admin-container">
      <div className="manual-procedures-container">
        <div className="row card-layout manual-procedures">
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Audit Table Housekeep
                  </div>
                </div>
              </div>
              <div className="e-card-content">
                <DropDownListComponent
                  dataSource={auditTables}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Audit Table"
                  text={auditTables[0]?.text}
                  change={(value: ChangeEventArgs) =>
                    changeAudiTableName(value)
                  }
                />
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={houseKeepSubmit}
                  disabled={!auditTableName}
                >
                  Housekeep
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">Provision Posting</div>
                </div>
              </div>
              <div className="e-card-content">
                <div className="row">
                  <TextBoxComponent
                    placeholder="Job Number"
                    floatLabelType="Auto"
                    cssClass="e-outline"
                    value={provisionPostingJobNo}
                    input={(value: InputEventArgs) =>
                      changeProvisionPostingJobNo(value.value)
                    }
                  />
                </div>
                <DatePicker
                  placeholder="GL Date"
                  value={provisionPostingDate}
                  onChange={changeProvisionPostingDate}
                />
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={provisionPostingSubmit}
                  disabled={!provisionPostingJobNo}
                >
                  Post
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">Roc Cutoff Schedule</div>
                </div>
              </div>
              <div className="e-card-content">
                <div className="row nopadding">
                  <div className="col-lg-6 col-md-6">
                    <DatePicker
                      placeholder="Cutoff Date"
                      format="yyyy-MM-dd"
                      value={newCutoffPeriod}
                    />
                  </div>
                  <div className="col-lg-6 col-md-6">
                    <DatePicker
                      format="yyyy-MM"
                      start="Year"
                      depth="Year"
                      placeholder="Period"
                      value={CutoffPeriod?.period}
                    />
                  </div>
                </div>
                <div className="row">
                  <TextBoxComponent
                    placeholder="Exclude Project List"
                    floatLabelType="Auto"
                    cssClass="e-outline"
                    value=""
                  />
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
                  Update
                </ButtonComponent>
              </div>
            </div>
          </div>
        </div>
        <div className="row card-layout manual-procedures">
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">Update CED Approval</div>
                </div>
              </div>
              <div className="e-card-content">
                <div className="row">
                  <TextBoxComponent
                    placeholder="Job Number"
                    floatLabelType="Auto"
                    cssClass="e-outline"
                    value={updateCeoApprovalJobNo}
                    input={(value: InputEventArgs) =>
                      changeUpdateCeoApprovalJobNo(value.value)
                    }
                  />
                </div>
                <TextBoxComponent
                  placeholder="Package Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={updateCeoApprovalPackageNo}
                  input={(value: InputEventArgs) => {
                    changeUpdateCeoApprovalPackageNo(value.value)
                  }}
                />
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={updateCeoApprovalSubmit}
                  disabled={
                    !(updateCeoApprovalJobNo && updateCeoApprovalPackageNo)
                  }
                >
                  Post
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Generate Subcontract Package Snapshot
                  </div>
                </div>
              </div>
              <div className="e-card-content text-content">
                <div className="text-border">
                  ALL Subcontract Package data will be cloned to Subcontract
                  Package Snapshot Table serving for Month End History enquiry
                  in Subcontract Enquiry Page.
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={generateSnapshotSubmit}
                >
                  Generate Snapshot
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Post Subcontract Payment
                  </div>
                </div>
              </div>
              <div className="e-card-content text-content">
                <div className="text-border">
                  Subcontract Payments with status 'Waiting For Posting' will be
                  posted to Finance Account Payable Ledger
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={postSubcontractPaymentSubmit}
                >
                  Post
                </ButtonComponent>
              </div>
            </div>
          </div>
        </div>
        <div className="row card-layout manual-procedures">
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Synchronize F58001 From Subcontract Package
                  </div>
                </div>
              </div>
              <div className="e-card-content text-content">
                <div className="text-border">
                  Missing records will be inserted into F58001(JDE) from
                  Subcontract Package(QS System). For mis-matched records,
                  F58001(JDE) will be updated as well.
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={synchronizeF58001Submit}
                >
                  Synchronize
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Synchronize F58011 From Subcontract Payment Certificate
                  </div>
                </div>
              </div>
              <div className="e-card-content text-content">
                <div className="text-border">
                  Missing records will be inserted into F58011(JDE) from
                  Subcontract Payment Certificate(QS System)
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={synchronizeSubcontractPaymentCertSubmit}
                >
                  Synchronize
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Synchronize Main Contract Certificate From JDE(F03B14)
                  </div>
                </div>
              </div>
              <div className="e-card-content text-content">
                <div className="text-border">
                  Actual Recipet Date of Main Contract Certificate will be
                  mirrored from JDE(F03B14).
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={synchronizeMainContractSubmit}
                >
                  Synchronize
                </ButtonComponent>
              </div>
            </div>
          </div>
        </div>
        <div className="row card-layout manual-procedures">
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Recalculate Job Summary
                  </div>
                </div>
              </div>
              <div className="e-card-content text-content">
                <div className="text-border">
                  All Job Summaries will be recalculated.
                </div>
              </div>
              <div className="e-card-actions">
                {/* 响应boolean POST /service/subcontract/calculateTotalWDandCertAmount?jobNo=${jobno}&recalculateRententionAmount=false&subcontractNo= */}
                {/* TODO 接口待优化 */}
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
                  Recalculate
                </ButtonComponent>
              </div>
            </div>
          </div>
          <div className="col-sm-12 col-md-4">
            <div className="e-card">
              <div className="e-card-header bg-info">
                <div className="e-card-header-caption">
                  <div className="e-card-header-title">
                    Generate Payment Cert PDF
                  </div>
                </div>
              </div>
              <div className="e-card-content">
                <div className="row nopadding">
                  <div className="col-lg-6 col-md-6">
                    <TextBoxComponent
                      placeholder="Job Number"
                      floatLabelType="Auto"
                      cssClass="e-outline"
                      value={paymentCertPdfJobNo}
                      input={(value: InputEventArgs) =>
                        changeUpadatePaymentCertPdfJobNo(value.value)
                      }
                    />
                  </div>
                  <div className="col-lg-6 col-md-6">
                    <TextBoxComponent
                      placeholder="Package Number"
                      floatLabelType="Auto"
                      cssClass="e-outline"
                      value={paymentCertPdfPackageNo}
                      input={(value: InputEventArgs) =>
                        changeUpadatePaymentCertPdfPackageNo(value.value)
                      }
                    />
                  </div>
                </div>
                <div className="row">
                  <TextBoxComponent
                    placeholder="Payment Cert No"
                    floatLabelType="Auto"
                    cssClass="e-outline"
                    value={paymentCertPdfPaymentNo}
                    input={(value: InputEventArgs) =>
                      changeUpadatePaymentCertPdfPaymentNo(value.value)
                    }
                  />
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent
                  cssClass="e-info full-btn e-card-btn-txt"
                  onClick={generatePaymentCertPdfSubmit}
                  disabled={!paymentCertPdfJobNo}
                >
                  Generate
                </ButtonComponent>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ManualProcedures
