import * as React from 'react'

import {
  TabComponent,
  TabItemDirective,
  TabItemsDirective
} from '@syncfusion/ej2-react-navigations'

import Subcontract from '../Revisisons/Subcontract'
import Addendum from './Addendum'
import AddendumDetail from './AddendumDetail'
import Approval from './Approval'
import MainCertificate from './MainCertificate'
import Payment from './Payment'
import SubcontractDetail from './SubcontractDetail'
import './style.css'

const Revisions = () => {
  const headertext = [
    { text: 'Subcontract', id: 'subcontract' },
    { text: 'Subcontract Detail', id: 'subcontractDetail' },
    { text: 'Payment', id: 'payment' },
    { text: 'Addendum', id: 'addendum' },
    { text: 'Addendum Detail', id: 'addendumDetail' },
    { text: 'Main Certificate', id: 'mainCertificate' },
    { text: 'Approval', id: 'approval' },
    { text: 'Attachment', id: 'attachment' },
    { text: 'Transit', id: 'transit' },
    { text: 'Tender', id: 'tender' },
    { text: 'Tender Detail', id: 'tenderDetail' },
    { text: 'Monthly Movement', id: 'monthlyMovement' },
    { text: 'ROC', id: 'ROC' },
    { text: 'ROC Detail', id: 'ROCDetail' },
    { text: 'ROC Subdetail', id: 'ROCSubdetail' },
    { text: 'Final Account', id: 'finalAccount' },
    { text: 'Consultancy Agreement', id: 'consultancyAgreement' }
  ]
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const contents: { [key: string]: any } = {
    subcontract: () => <Subcontract />,
    subcontractDetail: () => <SubcontractDetail />,
    payment: () => <Payment />,
    addendum: () => <Addendum />,
    addendumDetail: () => <AddendumDetail />,
    mainCertificate: () => <MainCertificate />,
    approval: () => <Approval />,
    attachment: () => <div>8</div>,
    transit: () => <div>9</div>,
    tender: () => <div>10</div>,
    tenderDetail: () => <div>11</div>,
    monthlyMovement: () => <div>12</div>,
    // eslint-disable-next-line @typescript-eslint/naming-convention
    ROC: () => <div>13</div>,
    // eslint-disable-next-line @typescript-eslint/naming-convention
    ROCDetail: () => <div>14</div>,
    // eslint-disable-next-line @typescript-eslint/naming-convention
    ROCSubdetail: () => <div>15</div>,
    finalAccount: () => <div>16</div>,
    consultancyAgreement: () => <div>17</div>
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const select = (e: any) => {
    if (e.isSwiped) {
      e.cancel = true
    }
  }

  return (
    <TabComponent id="defaultTab" overflowMode="MultiRow" selecting={select}>
      <TabItemsDirective>
        {headertext.map(e => (
          <TabItemDirective header={e} content={contents[e.id]} key={e.id} />
        ))}
      </TabItemsDirective>
    </TabComponent>
  )
}

export default Revisions
