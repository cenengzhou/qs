import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  ChangedEventArgs,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import { GLOBALPARAMETER, IDFIELDS } from '../../../../constants/global'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  CustomError,
  useCompleteAddendumApprovalAdminMutation,
  useCompleteAwardApprovalAdminMutation,
  useCompleteMainCertApprovalAdminMutation,
  useCompletePaymentApprovalAdminMutation,
  useCompleteSplitTerminateApprovalAdminMutation
} from '../../../../services'
import './style.css'
import type { CollapseProps } from 'antd'
import { Collapse } from 'antd'

const Approval = () => {
  const dispatch = useAppDispatch()

  const [subcontract, setSubcontract] = useState<{
    jobNo?: string
    packageNo?: string
    type?: string
    extraType?: string
  }>({})
  const [index, setIndex] = useState<Array<string>>(['1'])

  const [updateSubcontract, { isLoading: subcontractLoading }] =
    useCompleteAwardApprovalAdminMutation()
  const [updatePayment, { isLoading: paymentLoading }] =
    useCompletePaymentApprovalAdminMutation()
  const [updateSplit, { isLoading: slitLoading }] =
    useCompleteSplitTerminateApprovalAdminMutation()
  const [updateAddendum, { isLoading: addendumLoading }] =
    useCompleteAddendumApprovalAdminMutation()
  const [updateMainCert, { isLoading: maincertLoading }] =
    useCompleteMainCertApprovalAdminMutation()

  useEffect(() => {
    if (
      subcontractLoading ||
      paymentLoading ||
      slitLoading ||
      addendumLoading ||
      maincertLoading
    ) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [
    subcontractLoading,
    paymentLoading,
    slitLoading,
    addendumLoading,
    maincertLoading
  ])
  const submitUpdate = async () => {
    if (index.includes('1')) {
      await updateSubcontract(subcontract)
        .unwrap()
        .then(payload => {
          if (payload.completed == true) {
            showTotas('Success', 'Record updated')
          } else {
            showTotas('Fail', 'Fail')
          }
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
      return
    }
    if (index.includes('2')) {
      await updatePayment(subcontract)
        .unwrap()
        .then(payload => {
          if (payload.completed == true) {
            showTotas('Success', 'Record updated')
          } else {
            showTotas('Fail', 'Fail')
          }
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
      return
    }
    if (index.includes('3')) {
      await updateSplit(subcontract)
        .unwrap()
        .then(payload => {
          if (payload.completed == true) {
            showTotas('Success', 'Record updated')
          } else {
            showTotas('Fail', 'Fail')
          }
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
      return
    }
    if (index.includes('4')) {
      await updateAddendum(subcontract)
        .unwrap()
        .then(payload => {
          if (payload.completed == true) {
            showTotas('Success', 'Record updated')
          } else {
            showTotas('Fail', 'Fail')
          }
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
      return
    }
    if (index.includes('5')) {
      await updateMainCert(subcontract)
        .unwrap()
        .then(payload => {
          if (payload.completed == true) {
            showTotas('Success', 'Record updated')
          } else {
            showTotas('Fail', 'Fail')
          }
        })
        .catch((error: CustomError) => {
          showTotas('Fail', error.data.message)
        })
      return
    }
  }

  const Content = () => {
    return (
      <>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Job Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value={subcontract.jobNo}
              change={(args: ChangedEventArgs) => {
                setSubcontract({
                  ...subcontract,
                  jobNo: args.value
                })
              }}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder={index.includes('5') ? 'Main Cert No' : 'Package No'}
              floatLabelType="Auto"
              cssClass="e-outline"
              value={subcontract.packageNo}
              change={(args: ChangedEventArgs) => {
                setSubcontract({
                  ...subcontract,
                  packageNo: args.value
                })
              }}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={GLOBALPARAMETER.approvedOrRejectedOptions}
              fields={IDFIELDS}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Approved Or Rejected"
              value={subcontract.type}
              change={(args: { itemData: { id: string; value: string } }) => {
                setSubcontract({
                  ...subcontract,
                  type: args.itemData.id
                })
              }}
            />
          </div>
          {index.includes('3') && (
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={GLOBALPARAMETER.splitOrTerminateOptions}
                  fields={IDFIELDS}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Split Or Terminate"
                  value={subcontract.extraType}
                  change={(args: {
                    itemData: { id: string; value: string }
                  }) => {
                    setSubcontract({
                      ...subcontract,
                      extraType: args.itemData.id
                    })
                  }}
                />
              </div>
            </div>
          )}
        </div>
        <div className="row">
          <div className="col-lg-12 col-md-12">
            <ButtonComponent
              cssClass="e-info full-btn"
              onClick={submitUpdate}
              disabled={
                !(
                  subcontract.jobNo &&
                  subcontract.packageNo &&
                  subcontract.type
                )
              }
            >
              Update
            </ButtonComponent>
          </div>
        </div>
      </>
    )
  }

  const items: CollapseProps['items'] = [
    {
      key: '1',
      label: 'To Complete Subcontract Award',
      children: <Content />
    },
    {
      key: '2',
      label: 'To Complete Payment',
      children: <Content />
    },
    {
      key: '3',
      label: 'To Complete Split Terminate',
      children: <Content />
    },
    {
      key: '4',
      label: 'To Complete Addendum',
      children: <Content />
    },
    {
      key: '5',
      label: 'To Complate Main Cert',
      children: <Content />
    }
  ]

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
      <Collapse
        accordion
        items={items}
        defaultActiveKey={['1']}
        onChange={(args: string | string[]) => {
          if (index !== args) {
            setIndex(args as string[])
            setSubcontract({
              jobNo: undefined,
              packageNo: undefined,
              type: undefined,
              extraType: undefined
            })
          }
        }}
      />
    </div>
  )
}

export default Approval
