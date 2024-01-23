/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  DropDownListComponent,
  SelectEventArgs
} from '@syncfusion/ej2-react-dropdowns'
import {
  ChangeEventArgs,
  InputEventArgs,
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import { FIELDS, STATUS } from '../../../../constants/global'
import { useHasRole } from '../../../../hooks/useHasRole'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  AddendumRequest,
  FinalAccountResponse,
  useDeleteFinalAccountAdminMutation,
  useGetFinalAccountAdminMutation,
  useUpdateFinalAccountAdminMutation
} from '../../../../services'
import { textBoxValidation } from '../helper'

const FinalAccount = () => {
  const dispatch = useAppDispatch()
  const { showQSAdmin } = useHasRole()
  const [subcontractNo, setSubcontractNo] = useState<string>()
  const [finalAccountSearch, setFinalAccountSearch] = useState<AddendumRequest>(
    {
      jobNo: undefined,
      subcontractNo: undefined,
      addendumNo: undefined
    }
  )
  const [getFinalAddendum, { isLoading: getLoading }] =
    useGetFinalAccountAdminMutation()
  const [updateFinalAddendumAdmin, { isLoading: updateLoading }] =
    useUpdateFinalAccountAdminMutation()
  const [deleteFinalAccountAdmin, { isLoading: deleteLoading }] =
    useDeleteFinalAccountAdminMutation()
  const [data, setData] = useState<FinalAccountResponse>()

  const onSearch = () => {
    setSubcontractNo(String(finalAccountSearch.subcontractNo))
    try {
      getFinalAddendum(finalAccountSearch)
        .unwrap()
        .then(res => {
          if (res) {
            setData(res)
          } else {
            setData({})
            showTotas('Warn', 'Addendum not found')
          }
        })
        .catch(() => {
          showTotas('Fail', 'Failed to fetch')
        })
    } catch (error) {
      showTotas('Fail', 'Fail')
    }
  }
  const update = () => {
    try {
      if (data) {
        updateFinalAddendumAdmin({ data, subcontractNo })
          .unwrap()
          .then(() => {
            showTotas('Success', 'Success')
          })
          .catch(() => {
            showTotas('Fail', 'Failed to fetch')
          })
      }
    } catch (error) {
      showTotas('Fail', 'Fail')
    }
  }
  const onDelete = () => {
    try {
      if (data) {
        deleteFinalAccountAdmin({ data, subcontractNo })
          .unwrap()
          .then(() => {
            setData({})
            showTotas('Success', 'Success')
          })
          .catch(() => {
            showTotas('Fail', 'Failed to fetch')
          })
      }
    } catch (error) {
      showTotas('Fail', 'Fail')
    }
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
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={finalAccountSearch.jobNo}
            input={(value: InputEventArgs) => {
              textBoxValidation(value, 5)
              setFinalAccountSearch(prev => ({ ...prev, jobNo: value.value }))
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            type="number"
            value={finalAccountSearch.subcontractNo}
            input={(value: InputEventArgs) => {
              textBoxValidation(value)
              setFinalAccountSearch(prev => ({
                ...prev,
                subcontractNo: value.value
              }))
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Addendum Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            type="number"
            value={finalAccountSearch.addendumNo}
            input={(value: InputEventArgs) => {
              textBoxValidation(value)
              setFinalAccountSearch(prev => ({
                ...prev,
                addendumNo: value.value
              }))
            }}
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={onSearch}
            disabled={
              finalAccountSearch.jobNo?.length !== 5 ||
              !finalAccountSearch.subcontractNo ||
              !finalAccountSearch.addendumNo
            }
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      {data?.id && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Subcontract Description"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.jobNo}
                  readOnly
                />
              </div>
              <div className="col-lg-6 col-md-6">
                <TextBoxComponent
                  placeholder="Subcontract Name"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.addendumNo}
                  readOnly
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Final Account App Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={data?.finalAccountAppAmt}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, finalAccountAppAmt: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Final Account App CC Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={data?.finalAccountAppCCAmt}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, finalAccountAppCCAmt: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Latest Budget Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={data?.latestBudgetAmt}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, latestBudgetAmt: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Latest Budget CC Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={data?.latestBudgetAmtCC}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, latestBudgetAmtCC: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="This Final Account CC Amount"
                  floatLabelType="Auto"
                  format="c2"
                  cssClass="e-outline"
                  value={data?.finalAccountThisCCAmt}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, finalAccountThisCCAmt: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <TextBoxComponent
                  placeholder="Final Account Comments"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  multiline={true}
                  value={data?.comments}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={STATUS}
                  fields={FIELDS}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Status"
                  value={data?.status}
                  select={(value: SelectEventArgs) =>
                    setData({ ...data, status: value.itemData.value })
                  }
                />
              </div>
            </div>
          </div>
          {showQSAdmin && (
            <div className="row">
              <div
                className={
                  data?.status == 'PENDING'
                    ? 'col-lg-6 col-md-6'
                    : 'col-lg-12 col-md-12'
                }
              >
                <ButtonComponent cssClass="e-info full-btn" onClick={update}>
                  Update
                </ButtonComponent>
              </div>
              {data?.status == 'PENDING' && (
                <div className="col-lg-6 col-md-6">
                  <ButtonComponent
                    cssClass="e-danger full-btn"
                    onClick={onDelete}
                  >
                    Delete
                  </ButtonComponent>
                </div>
              )}
            </div>
          )}
        </>
      )}
    </div>
  )
}

export default FinalAccount
