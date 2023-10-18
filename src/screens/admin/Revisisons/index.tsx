import * as React from 'react'
import {
  TabComponent,
  TabItemDirective,
  TabItemsDirective,
} from '@syncfusion/ej2-react-navigations'
import Sidebar from '../../../components/SideBar'
import Subcontract from './Subcontract'

const Revisions = () => {
  const headertext = [
    { text: "Subcontract", id: "subcontract" },
    { text: "Subcontract Detail", id: "subcontractDetail" },
    { text: "Payment", id: "payment" },
    { text: "Addendum", id: "addendum" },
    { text: "Addendum Detail", id: "addendumDetail" },
    { text: "Main Certificate", id: "mainCertificate" },
    { text: "Approval", id: "approval" },
    { text: "Attachment", id: "attachment" },
    { text: "Transit", id: "transit" },
    { text: "Tender", id: "tender" },
    { text: "Tender Detail", id: "tenderDetail" },
    { text: "Monthly Movement", id: "monthlyMovement" },
    { text: "ROC", id: "ROC" },
    { text: "ROC Detail", id: "ROCDetail" },
    { text: "ROC Subdetail", id: "ROCSubdetail" },
    { text: "Final Account", id: "finalAccount" },
    { text: "Consultancy Agreement", id: "consultancyAgreement" },
  ]
  const contents: { [key: string]: any } = {
    subcontract: () => <Subcontract></Subcontract>,
    subcontractDetail: () => <div>2</div>,
    payment: () => <div>3</div>,
    addendum: () => <div>4</div>,
    addendumDetail: () => <div>5</div>,
    mainCertificate: () => <div>6</div>,
    approval: () => <div>7</div>,
    attachment: () => <div>8</div>,
    transit: () => <div>9</div>,
    tender: () => <div>10</div>,
    tenderDetail: () => <div>11</div>,
    monthlyMovement: () => <div>12</div>,
    ROC: () => <div>13</div>,
    ROCDetail: () => <div>14</div>,
    ROCSubdetail: () => <div>15</div>,
    finalAccount: () => <div>16</div>,
    consultancyAgreement: () => <div>17</div>,
  }

  return (
    <div className="control-pane">
      <div className="control-section tab-control-section">
        {/* Render the Tab Component */}
        <TabComponent id="defaultTab" overflowMode="MultiRow">
          <TabItemsDirective>
            {headertext.map(e => (
              <TabItemDirective header={e} content={contents[e.id]} key={e.id} />
            ))}
          </TabItemsDirective>
        </TabComponent>
      </div>
    </div>
  )
}

export default Revisions;
