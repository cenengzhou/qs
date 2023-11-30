import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import DatePicker from '../../../../components/DatePicker'

const Payment = () => {
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
            placeholder="Payment Certificate No"
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
              placeholder="Payment Certificate Number"
              floatLabelType="Auto"
              cssClass="e-outline"
              value=""
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Vendor No"
              floatLabelType="Auto"
              cssClass="e-outline"
              value="55555"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={data}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Direct Payment"
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
              placeholder="Interm Final Payment"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Addendum Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Certificate Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Remeasure Contract Sum"
              floatLabelType="Auto"
              cssClass="e-outline"
              format="n2"
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
              placeholder="Main Contract Payment Cert No"
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker
              placeholder="Final Payment Issued Date"
              value={new Date()}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker
              placeholder="First Payment Cert Issued Date"
              value={'2022-11-9'}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DatePicker placeholder="Last Addendum Value Update Date" />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <DatePicker
              placeholder="IPA or Invoice Received Date"
              value={new Date()}
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-12 col-md-12">
            <TextBoxComponent
              placeholder="Explanation"
              floatLabelType="Auto"
              cssClass="e-outline"
              multiline={true}
              value=""
            />
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

export default Payment
