import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../components/DatePicker'
import { GLOBALPARAMETER } from '../../../constants/global'
import {
  useGetAuditTableMapQuery,
  useGetCutoffPeriodQuery
} from '../../../services'
import './style.css'

const ManualProcedures = () => {
  const { data: AuditTable } = useGetAuditTableMapQuery()
  const auditTables = Object.values(AuditTable ?? {}).map((item, index) => {
    return {
      value: index,
      text: item.tableName
    }
  })
  const { data: CutoffPeriod } = useGetCutoffPeriodQuery()
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
                  value={auditTables[0]?.text}
                />
              </div>
              <div className="e-card-actions">
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                    value=""
                  />
                </div>
                <DatePicker placeholder="GL Date" value={new Date()} />
              </div>
              <div className="e-card-actions">
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                    value=""
                  />
                </div>
                <TextBoxComponent
                  placeholder="Package Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value=""
                />
              </div>
              <div className="e-card-actions">
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
                      value=""
                    />
                  </div>
                  <div className="col-lg-6 col-md-6">
                    <TextBoxComponent
                      placeholder="Package Number"
                      floatLabelType="Auto"
                      cssClass="e-outline"
                      value=""
                    />
                  </div>
                </div>
                <div className="row">
                  <TextBoxComponent
                    placeholder="Payment Cert No"
                    floatLabelType="Auto"
                    cssClass="e-outline"
                    value=""
                  />
                </div>
              </div>
              <div className="e-card-actions">
                <ButtonComponent cssClass="e-info full-btn e-card-btn-txt">
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
