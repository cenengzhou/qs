import * as React from 'react'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import {
  AccordionComponent,
  AccordionItemDirective,
  AccordionItemsDirective
} from '@syncfusion/ej2-react-navigations'

import './style.css'

interface Data {
  header: () => JSX.Element
  content: () => JSX.Element
}

const Approval = () => {
  const approvalData: Data[] = [
    {
      header: () => {
        return <div>To Complete Subcontract Award</div>
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
                  dataSource={['Approvaled', 'Rejected']}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Approved Or Rejected"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Update
                </ButtonComponent>
              </div>
            </div>
          </>
        )
      }
    },
    {
      header: () => {
        return <div>To Complete Payment</div>
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
                  dataSource={['Approvaled', 'Rejected']}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Approved Or Rejected"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Update
                </ButtonComponent>
              </div>
            </div>
          </>
        )
      }
    },
    {
      header: () => {
        return <div>To Complete Split Terminate</div>
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
                  dataSource={['Approvaled', 'Rejected']}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Approved Or Rejected"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-4 col-md-4">
                <DropDownListComponent
                  dataSource={['Split', 'Terminate']}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Split Or Terminate"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Update
                </ButtonComponent>
              </div>
            </div>
          </>
        )
      }
    },
    {
      header: () => {
        return <div>To Complete Addendum</div>
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
                  dataSource={['Approvaled', 'Rejected']}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Approved Or Rejected"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Update
                </ButtonComponent>
              </div>
            </div>
          </>
        )
      }
    },
    {
      header: () => {
        return <div>To Complate Main Cert</div>
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
                  dataSource={['Approvaled', 'Rejected']}
                  cssClass="e-outline"
                  floatLabelType="Always"
                  showClearButton
                  placeholder="Approved Or Rejected"
                />
              </div>
            </div>
            <div className="row">
              <div className="col-lg-12 col-md-12">
                <ButtonComponent cssClass="e-info full-btn">
                  Update
                </ButtonComponent>
              </div>
            </div>
          </>
        )
      }
    }
  ]

  return (
    <div className="admin-container">
      <AccordionComponent expandMode="Single">
        <AccordionItemsDirective>
          {approvalData.map((e: Data, index) => {
            return (
              <AccordionItemDirective
                header={e.header}
                expanded={index === 0}
                content={e.content}
                key={index}
              />
            )
          })}
        </AccordionItemsDirective>
      </AccordionComponent>
    </div>
  )
}
export default Approval
