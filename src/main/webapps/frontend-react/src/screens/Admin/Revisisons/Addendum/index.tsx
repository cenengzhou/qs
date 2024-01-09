import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../../components/DatePicker'

const Addendum = () => {
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Addendum Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Subcontract Description"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Subcontract Name"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Remarks"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Title"
              floatLabelType="Auto"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Prepared By"
              floatLabelType="Auto"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum Total Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum Total TBA Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Subcontract Remesured Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum Revised Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum Revised TBA Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum No."
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Next Addendum Detail No."
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Job No."
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Subcontract No."
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Subcontractor No."
              floatLabelType="Auto"
              format="n"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="CED Approved Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={['Approved', 'Rejected']}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Status"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={['Approved', 'Rejected']}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Approval Status"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={['Yes', 'No']}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Final Account"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={['Yes', 'No']}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="CED Approval"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Approval Date" value={new Date()} />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Submission Date" value={'2022-11-19'} />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-lg-12 col-md-12">
          <ButtonComponent cssClass="e-info full-btn">Update</ButtonComponent>
        </div>
      </div>
    </div>
  )
}

export default Addendum
