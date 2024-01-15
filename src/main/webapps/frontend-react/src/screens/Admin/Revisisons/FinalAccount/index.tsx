/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  ChangeEventArgs,
  InputEventArgs,
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import { useHasRole } from '../../../../hooks/useHasRole'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  AddendumRequest,
  FinalAccountResponse,
  useGetFinalAccountAdminMutation,
  useUpdateFinalAccountAdminMutation
} from '../../../../services'

const FinalAccount = () => {
  const dispatch = useAppDispatch()
  const { showQSAdmin } = useHasRole()
  const [subcontractNo, setSubcontractNo] = useState<string>()
  const [addendumSearch, setAddendumSearch] = useState<AddendumRequest>({
    jobNo: undefined,
    subcontractNo: undefined,
    addendumNo: undefined
  })
  const [getFinalAddendum, { isLoading: getLoading }] =
    useGetFinalAccountAdminMutation()
  const [updateFinalAddendumAdmin, { isLoading: updateLoading }] =
    useUpdateFinalAccountAdminMutation()
  const [data, setData] = useState<FinalAccountResponse>()

  const onSearch = () => {
    setSubcontractNo(String(addendumSearch.subcontractNo))
    try {
      getFinalAddendum(addendumSearch)
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
    if (getLoading || updateLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [getLoading, updateLoading])

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            value={addendumSearch.jobNo}
            input={(value: InputEventArgs) =>
              setAddendumSearch(prev => ({ ...prev, jobNo: value.value }))
            }
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="######"
            value={addendumSearch.subcontractNo}
            change={(value: ChangeEventArgs) =>
              setAddendumSearch(prev => ({
                ...prev,
                subcontractNo: value.value
              }))
            }
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Addendum Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            format="######"
            value={addendumSearch.addendumNo}
            change={(value: ChangeEventArgs) =>
              setAddendumSearch(prev => ({
                ...prev,
                addendumNo: value.value
              }))
            }
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={onSearch}
            disabled={
              !addendumSearch.jobNo ||
              !addendumSearch.subcontractNo ||
              !addendumSearch.addendumNo
            }
          >
            Search
          </ButtonComponent>
        </div>
      </div>
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
      </div>
      {showQSAdmin && (
        <div className="row">
          <div className="col-lg-12 col-md-12">
            <ButtonComponent cssClass="e-info full-btn" onClick={update}>
              Update
            </ButtonComponent>
          </div>
        </div>
      )}
    </div>
  )
}

export default FinalAccount
