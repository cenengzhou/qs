import { TextBoxComponent, NumericTextBoxComponent } from '@syncfusion/ej2-react-inputs'
import { ButtonComponent, CheckBoxComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import './style.css'

const Subcontract = () => {
  const data = [
    {text: "1111", value: 1},
    {text: "2222", value: 2},
    {text: "3333", value: 3},
    {text: "4444", value: 4}
  ]
  return (
    <div className="subcontract">
      {/* input */}
      <div className="subcontract-header row">
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
          <ButtonComponent cssClass="e-info search-btn">Search</ButtonComponent>
        </div>
      </div>
      {/* content */}
      <div className="subcontract-content">
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
        {/* numericText */}
      </div>
    </div>
  );
}

export default Subcontract
