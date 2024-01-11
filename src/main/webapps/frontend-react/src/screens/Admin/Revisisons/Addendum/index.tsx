/* eslint-disable @typescript-eslint/naming-convention */
import { useState } from 'react'

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
import {
  FIELDS,
  STATUS,
  STATUS_APPROVAL,
  YESORNO
} from '../../../../constants/global'
import { useHasRole } from '../../../../hooks/useHasRole'
import {
  AddendumRequest,
  AddendumResponse,
  useGetAddendumMutation,
  useUpdateAddendumAdminMutation
} from '../../../../services'

const Addendum = () => {
  const { showQSAdmin } = useHasRole()
  const [addendumSearch, setAddendumSearch] = useState<AddendumRequest>({
    jobNo: undefined,
    subcontractNo: undefined,
    addendumNo: undefined
  })
  const [getAddendum] = useGetAddendumMutation()
  const [updateAddendumAdmin] = useUpdateAddendumAdminMutation()
  const [data, setData] = useState<AddendumResponse>()
  const onSearch = async () => {
    try {
      const result = await getAddendum(addendumSearch).unwrap()
      setData(result)
    } catch (error) {
      console.log(error)
    }
  }
  const update = async () => {
    try {
      if (data) {
        updateAddendumAdmin(data)
          .unwrap()
          .then(result => {
            console.log(result)
          })
      }
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <div className="admin-container">
      {/* input */}
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
          <ButtonComponent cssClass="e-info full-btn" onClick={onSearch}>
            Search
          </ButtonComponent>
        </div>
      </div>
      {data && (
        <>
          <div className="admin-content">
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontract Description"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.descriptionSubcontract}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, descriptionSubcontract: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Subcontract Name"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.nameSubcontractor}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, nameSubcontractor: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Remarks"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.remarks}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, remarks: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Title"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.title}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, title: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Prepared By"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                  value={data?.usernamePreparedBy}
                  input={(value: InputEventArgs) =>
                    setData({ ...data, usernamePreparedBy: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.amtAddendum}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtAddendum: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum Total Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.amtAddendumTotal}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtAddendumTotal: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum Total TBA Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.amtAddendumTotalTba}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtAddendumTotalTba: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Subcontract Remesured Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.amtSubcontractRemeasured}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtSubcontractRemeasured: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum Revised Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.amtSubcontractRevised}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtSubcontractRevised: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum Revised TBA Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={data?.amtSubcontractRevisedTba}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtSubcontractRevisedTba: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Addendum No."
                  floatLabelType="Auto"
                  format="####"
                  cssClass="e-outline"
                  value={data?.no}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, no: value.value })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Next Addendum Detail No."
                  floatLabelType="Auto"
                  format="####"
                  cssClass="e-outline"
                  value={data?.noAddendumDetailNext}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, noAddendumDetailNext: value.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Job No."
                  floatLabelType="Auto"
                  format="####"
                  cssClass="e-outline"
                  value={Number(data?.noJob)}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, noJob: String(value.value) })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Subcontract No."
                  floatLabelType="Auto"
                  format="####"
                  cssClass="e-outline"
                  value={Number(data?.noSubcontract)}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, noSubcontract: String(value.value) })
                  }
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="Subcontractor No."
                  floatLabelType="Auto"
                  format="####"
                  cssClass="e-outline"
                  value={Number(data?.noSubcontractor)}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, noSubcontractor: String(value.value) })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <NumericTextBoxComponent
                  placeholder="CED Approved Amount"
                  floatLabelType="Auto"
                  format="n2"
                  cssClass="e-outline"
                  value={Number(data?.amtCEDApproved)}
                  change={(value: ChangeEventArgs) =>
                    setData({ ...data, amtCEDApproved: String(value.value) })
                  }
                />
              </div>
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
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={STATUS_APPROVAL}
                  fields={FIELDS}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Approval Status"
                  value={
                    data?.statusApproval == null ? '' : data?.statusApproval
                  }
                  select={(value: SelectEventArgs) =>
                    setData({ ...data, statusApproval: value.itemData.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={YESORNO}
                  fields={FIELDS}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Final Account"
                  value={data?.finalAccount}
                  select={(value: SelectEventArgs) =>
                    setData({ ...data, finalAccount: value.itemData.value })
                  }
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={YESORNO}
                  fields={FIELDS}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="CED Approval"
                  value={data?.cedApproval}
                  select={(value: SelectEventArgs) =>
                    setData({ ...data, cedApproval: value.itemData.value })
                  }
                />
              </div>
            </div>
            <div className="row">
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

export default Addendum
