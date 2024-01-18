/* eslint-disable @typescript-eslint/naming-convention */
import { useEffect, useState } from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { ChangedEventArgs } from '@syncfusion/ej2-react-calendars'
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

import DatePicker from '../../../../components/DatePicker'
import MultiSelectUser from '../../../../components/MultiSelectUser'
import { FIELDS, STATUS } from '../../../../constants/global'
import { useHasRole } from '../../../../hooks/useHasRole'
import { closeLoading, openLoading } from '../../../../redux/loadingReducer'
import { setNotificationVisible } from '../../../../redux/notificationReducer'
import { useAppDispatch } from '../../../../redux/store'
import {
  MemoResponse,
  SubcontractResquest,
  useFindByUsernameIsNotNullQuery,
  useGetMemoMutation,
  useUpdateConsultancyAgreementAdminMutation
} from '../../../../services'

const ConsultancyAgreement = () => {
  const dispatch = useAppDispatch()
  const { showQSAdmin } = useHasRole()
  const [memoSearch, setMemoSearch] = useState<SubcontractResquest>({
    jobNo: undefined,
    subcontractNo: undefined
  })
  const [memoUpdate, setMemoUpdate] = useState<SubcontractResquest>({
    jobNo: undefined,
    subcontractNo: undefined
  })
  const { data: userList, isLoading: getUserLoading } =
    useFindByUsernameIsNotNullQuery()
  const [getMemo, { isLoading: getLoading }] = useGetMemoMutation()
  const [updateConsultancyAgreementAdmin, { isLoading: updateLoading }] =
    useUpdateConsultancyAgreementAdminMutation()
  const [data, setData] = useState<MemoResponse>()
  const onSearch = () => {
    setMemoUpdate(memoSearch)
    try {
      getMemo(memoSearch)
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
        updateConsultancyAgreementAdmin({ ...memoUpdate, data })
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
    if (getLoading || updateLoading || getUserLoading) {
      dispatch(openLoading())
    } else {
      dispatch(closeLoading())
    }
  }, [getLoading, updateLoading, getUserLoading])

  return (
    <div className="admin-container">
      <div className="admin-header row">
        <div className="col-lg-4 col-md-4">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            type="number"
            value={memoSearch.jobNo}
            input={(value: InputEventArgs) =>
              setMemoSearch(prev => ({ ...prev, jobNo: value.value }))
            }
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <TextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
            type="number"
            value={memoSearch.subcontractNo}
            input={(value: InputEventArgs) =>
              setMemoSearch(prev => ({
                ...prev,
                subcontractNo: value.value
              }))
            }
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent
            cssClass="e-info full-btn"
            onClick={onSearch}
            disabled={!memoSearch.jobNo || !memoSearch.subcontractNo}
          >
            Search
          </ButtonComponent>
        </div>
      </div>
      {data?.id && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Ref"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.ref}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, ref: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <MultiSelectUser
                  placeholder="From List"
                  userList={userList || []}
                  value={data?.fromList?.split(';') || []}
                  selected={e => setData({ ...data, fromList: e })}
                  removed={e => setData({ ...data, fromList: e })}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <MultiSelectUser
                  placeholder="To List"
                  userList={userList || []}
                  value={data?.toList?.split(';') || []}
                  selected={e => setData({ ...data, toList: e })}
                  removed={e => setData({ ...data, toList: e })}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <MultiSelectUser
                  placeholder="CC List"
                  userList={userList || []}
                  value={data?.ccList?.split(';') || []}
                  selected={e => setData({ ...data, ccList: e })}
                  removed={e => setData({ ...data, ccList: e })}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <TextBoxComponent
                  placeholder="Subject"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.subject}
                  multiline
                  input={(value: InputEventArgs) =>
                    setData({ ...data, subject: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Name of Consultant"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.consultantName}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, consultantName: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <TextBoxComponent
                  placeholder="Description of Service"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.description}
                  multiline
                  input={(value: InputEventArgs) =>
                    setData({ ...data, description: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Period of Appointment"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.period}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, period: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Estimate of Fee"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.feeEstimate}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, feeEstimate: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Budget"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.budgetAmount}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, budgetAmount: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <TextBoxComponent
                  placeholder="Explanations"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.explanation}
                  multiline
                  input={(value: InputEventArgs) =>
                    setData({ ...data, explanation: value.value })
                  }
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
                  placeholder="Approval Status"
                  value={data?.statusApproval}
                  select={(value: SelectEventArgs) =>
                    setData({ ...data, statusApproval: value.itemData.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Approval Date"
                  value={data?.dateApproval}
                  onChange={(value: ChangedEventArgs) =>
                    setData({
                      ...data,
                      dateApproval: value.value?.toJSON()
                    })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DatePicker
                  placeholder="Submission Date"
                  value={data?.dateSubmission}
                  onChange={(value: ChangedEventArgs) =>
                    setData({
                      ...data,
                      dateSubmission: value.value?.toJSON()
                    })
                  }
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
        </>
      )}
    </div>
  )
}

export default ConsultancyAgreement
