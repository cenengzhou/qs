import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  FocusOutEventArgs,
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  Tender,
  useGetTenderMutation,
  useUpdateTenderAdminMutation
} from '../../../../services'

const TenderRender = ({ isQsAdm }: { isQsAdm: boolean }) => {
  const dispatch = useAppDispatch()

  const [tender, setTender] = useState<Tender>({})
  const [searchRecord, setSearchRecord] = useState<{
    jobNo?: string
    subcontractNo?: number
    subcontractorNo?: number
  }>({ jobNo: undefined, subcontractNo: undefined, subcontractorNo: undefined })

  const [getTender, { isLoading }] = useGetTenderMutation()
  const [updateTender, { isLoading: updateLoading }] =
    useUpdateTenderAdminMutation()

  useEffect(() => {
    if (isLoading || updateLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [isLoading, updateLoading])

  const search = async () => {
    await getTender(searchRecord)
      .unwrap()
      .then(payload => {
        if (payload instanceof Object) {
          setTender(payload)
        }
      })
      .catch(() => {
        console.error('Fail')
      })
  }

  const update = async () => {
    await updateTender(tender)
      .unwrap()
      .then(() => {
        showTotas('Success', 'Tender updated.')
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
  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
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
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
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
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Vendor Number"
            floatLabelType="Auto"
            format="#"
            cssClass="e-outline"
            value={searchRecord.subcontractorNo}
            blur={(value: FocusOutEventArgs) => {
              setSearchRecord({
                ...searchRecord,
                subcontractorNo: Number(value.value)
              })
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn" onClick={search}>
            Search
          </ButtonComponent>
        </div>
      </div>
      {tender.jobNo && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={tender.jobNo}
                  readOnly
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Tender Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={tender.packageNo}
                  readOnly
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Vendor Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={String(tender.vendorNo)}
                  readOnly
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontractor Name"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={tender.nameSubcontractor}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={[
                    'HKD',
                    'USD',
                    'INR',
                    'GBP',
                    'EUR',
                    'AUD',
                    'THB',
                    'TWD',
                    'PHP',
                    'JPY',
                    'SGD',
                    'CAD',
                    'CNY',
                    'MOP'
                  ]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Curency Code"
                  value={tender.currencyCode}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Exchange Rate"
                  floatLabelType="Auto"
                  format="#"
                  cssClass="e-outline"
                  value={tender.exchangeRate}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Budget Amount"
                  floatLabelType="Auto"
                  format="#"
                  cssClass="e-outline"
                  value={tender.budgetAmount}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Gain/Loss"
                  floatLabelType="Auto"
                  format="#"
                  cssClass="e-outline"
                  value={tender.amtBuyingGainLoss}
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Remarks"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={tender.remarks}
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

export default TenderRender
