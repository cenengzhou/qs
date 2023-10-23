import * as React from "react"
import {
  AccordionComponent,
  AccordionItemDirective,
  AccordionItemsDirective,
} from "@syncfusion/ej2-react-navigations"
import { ButtonComponent } from "@syncfusion/ej2-react-buttons"
import { TextBoxComponent } from "@syncfusion/ej2-react-inputs"
import { DropDownListComponent } from "@syncfusion/ej2-react-dropdowns"
import "./style.css"

interface Data {
  header: () => JSX.Element,
  content: () => JSX.Element
}

const Approval = () => {
  const approvalData: Data[] = [
    {
      header: () => {
        return(
          <div>To Complete Subcontract Award</div>
        )
      },
      content: () => {
        return (
          <>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Package No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={["Approvaled", "Rejected"]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Method"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Search
                </ButtonComponent>
              </div>
            </div>
          </>
        );
      }
    },
    {
      header: () => {
        return <div>To Complete Payment</div>;
      },
      content: () => {
        return (
          <>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Package No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={["Approvaled", "Rejected"]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Method"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Search
                </ButtonComponent>
              </div>
            </div>
          </>
        );
      }
    },
    {
      header: () => {
        return <div>To Complete Split Terminate</div>;
      },
      content: () => {
        return (
          <>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Package No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={["Approvaled", "Rejected"]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Method"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Search
                </ButtonComponent>
              </div>
            </div>
          </>
        );
      }
    },
    {
      header: () => {
        return <div>To Complete Addendum</div>;
      },
      content: () => {
        return (
          <>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Package No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={["Approvaled", "Rejected"]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Method"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Search
                </ButtonComponent>
              </div>
            </div>
          </>
        );
      }
    },
    {
      header: () => {
        return <div>To Complate Main Cert</div>;
      },
      content: () => {
        return (
          <>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Job Number"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <TextBoxComponent
                  placeholder="Package No"
                  floatLabelType="Auto"
                  cssClass="e-outline"
                />
              </div>
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={["Approvaled", "Rejected"]}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Payment Method"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Search
                </ButtonComponent>
              </div>
            </div>
          </>
        );
      }
    },
  ];
  
  
  return (
    <div className="padding15">
      <AccordionComponent>
        <AccordionItemsDirective>
          {approvalData.map((e: Data, index) => {
            return (
              <AccordionItemDirective
                header={e.header}
                expanded={index === 0}
                content={e.content}
              />
            );
          })}
        </AccordionItemsDirective>
      </AccordionComponent>
    </div>
  )
};
export default Approval
