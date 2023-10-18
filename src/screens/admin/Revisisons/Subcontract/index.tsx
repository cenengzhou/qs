import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'

const Subcontract = () => {
  return (
    <div className="row">
      <div className="col-xs-4 col-sm-4 col-lg-4 col-md-4">
        <TextBoxComponent
          placeholder="Job Number"
          floatLabelType="Auto"
          cssClass="e-outline"
        />
      </div>
      <div className="col-xs-4 col-sm-4 col-lg-4 col-md-4">
        <TextBoxComponent
          placeholder="Filled"
          floatLabelType="Auto"
          cssClass="e-outline"
        />
      </div>
      <div className="col-xs-4 col-sm-4 col-lg-4 col-md-4">
        <ButtonComponent>Search</ButtonComponent>
      </div>
    </div>
  );
}

export default Subcontract
