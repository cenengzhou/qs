import {
  TextBoxComponent,
  NumericTextBoxComponent,
} from "@syncfusion/ej2-react-inputs"
import { ButtonComponent } from "@syncfusion/ej2-react-buttons"

const MainCertificate = () => {
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
            placeholder="Certificate Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      <div className="admin-content"></div>
    </div>
  );
};

export default MainCertificate;
