import {
  ButtonComponent,
  CheckBoxComponent
} from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../../components/DatePicker'
import './style.css'

const Subcontract = () => {
  const data = [
    { text: '1111', value: 1 },
    { text: '2222', value: 2 },
    { text: '3333', value: 3 },
    { text: '4444', value: 4 }
  ]
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-4 col-md-4">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      {/* content */}
      <div className="admin-content">
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Job Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value="13892"
              readOnly
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Subcontract Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value="1001"
              readOnly
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Vendor Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Subcontractor Name"
              floatLabelType="Auto"
              cssClass="e-outline"
              value="Gaint Dragon Construction Engineering Ltd"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Approval Route"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Internal Job Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
        </div>
        {/* input */}
        {/* textarea */}
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Notes"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline={true}
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Description"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Payment Terms Description"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Period for Payment"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Method of execution of main certificate"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Proposed method of execution"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Reason for use of LoA"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Reason for relaxation to three minimum quotations requirement"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Reason for change manner of execution from main contract"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline
              value=""
            />
          </div>
        </div>
        {/* textarea */}
        {/* dropdowns */}
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="CPF Calculation"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Form of Subcontract"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Package Type"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Payment Currency"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Payment Information"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Package Type"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Submitted Addendum"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Payment Terms"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Retention Terms"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Subcontract Terms"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Package Status"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Payment Status"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Split Terminate Status"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Subcontract Status"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Work Scope"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Payment Method"
            />
          </div>
        </div>
        {/* dropdowns */}
        {/* checkbox */}
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <CheckBoxComponent checked={true} label="Labour Included" />
          </div>
          <div className="col-lg-4 col-md-4">
            <CheckBoxComponent checked={true} label="Material Included" />
          </div>
          <div className="col-lg-4 col-md-4">
            <CheckBoxComponent checked={true} label="Plant Included" />
          </div>
        </div>
        {/* checkbox */}

        {/* numericText */}
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="CPF Base Period"
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="CPF Base Year"
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Accumulated Retention"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="c2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Approved VO Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total AP Posted Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Exchange Rate"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="n2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Interim Retention %"
              floatLabelType="Auto"
              format="n2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Max Retention %"
              floatLabelType="Auto"
              format="n2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="MOS Retention %"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="n2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Original SC Sum"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Remeasured SC Sum"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Retention Amount"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="c2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Retention Release"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total CC Posted Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total Cumlated Cert Amount"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="c2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total Cumulated Work Done Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total MOS Posted Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total Posted Cert Amount"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="c2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Total Posted Work Done Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Package Stretch Target Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Recoverable"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="c2"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Non-Recoverable"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="CEO Approved amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
        </div>
        {/* numericText */}

        {/* datePicker */}
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Final Payment Issued Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="First Payment Cert Issued Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Last Addendum Value Update Date" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Last Payment Cert Issueed Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="LOA Signed Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Preaward Metting Date" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Requisition Approved Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC Approval Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC Award Approval Requestent Date" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC Created Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC DOC LEGAL Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC Doc SCR Date" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="TA Approved Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Work Commence Date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="On-site start Date" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC final account draft date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="SC final account sign off date" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Target date for subcontract execution" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Sub-Contract Duration From" />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Sub-Contract Duration To" />
          </div>
        </div>
        {/* datePicker */}
      </div>
      <div className="row">
        <div className="col-lg-12 col-md-12">
          <ButtonComponent cssClass="e-info full-btn">Update</ButtonComponent>
        </div>
      </div>
    </div>
  )
}

export default Subcontract
