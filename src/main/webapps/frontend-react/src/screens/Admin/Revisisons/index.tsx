/* eslint-disable @typescript-eslint/naming-convention */
import * as React from 'react'

import {
  SelectingEventArgs,
  TabComponent,
  TabItemDirective,
  TabItemsDirective
} from '@syncfusion/ej2-react-navigations'

import Subcontract from '../Revisisons/Subcontract'
import Addendum from './Addendum'
import AddendumDetail from './AddendumDetail'
import Approval from './Approval'
import Attachment from './Attachment'
import MainCertificate from './MainCertificate'
import Payment from './Payment'
import SubcontractDetail from './SubcontractDetail'
import './style.css'

interface TabData {
  text: string
  id: string
  content: JSX.Element
}

const Revisions = () => {
  const tabData: TabData[] = [
    { text: 'Subcontract', id: 'subcontract', content: <Subcontract /> },
    {
      text: 'Subcontract Detail',
      id: 'subcontractDetail',
      content: <SubcontractDetail />
    },
    { text: 'Payment', id: 'payment', content: <Payment /> },
    { text: 'Addendum', id: 'addendum', content: <Addendum /> },
    {
      text: 'Addendum Detail',
      id: 'addendumDetail',
      content: <AddendumDetail />
    },
    {
      text: 'Main Certificate',
      id: 'mainCertificate',
      content: <MainCertificate />
    },
    { text: 'Approval', id: 'approval', content: <Approval /> },
    { text: 'Attachment', id: 'attachment', content: <Attachment /> },
    { text: 'Transit', id: 'transit', content: <div>Transit</div> },
    { text: 'Tender', id: 'tender', content: <div>Tender</div> },
    {
      text: 'Tender Detail',
      id: 'tenderDetail',
      content: <div>tenderDetail</div>
    },
    {
      text: 'Monthly Movement',
      id: 'monthlyMovement',
      content: <div>monthlyMovement</div>
    },
    { text: 'ROC', id: 'ROC', content: <div>ROC</div> },
    { text: 'ROC Detail', id: 'ROCDetail', content: <div>ROCDetail</div> },
    {
      text: 'ROC Subdetail',
      id: 'ROCSubdetail',
      content: <div>ROCSubdetail</div>
    },
    {
      text: 'Final Account',
      id: 'finalAccount',
      content: <div>finalAccount</div>
    },
    {
      text: 'Consultancy Agreement',
      id: 'consultancyAgreement',
      content: <div>consultancyAgreement</div>
    }
  ]

  const select = (e: SelectingEventArgs) => {
    if (e.isSwiped) {
      e.cancel = true
    }
  }

  return (
    <TabComponent id="defaultTab" overflowMode="MultiRow" selecting={select}>
      <TabItemsDirective>
        {tabData.map((e: TabData) => (
          <TabItemDirective header={e} content={() => e.content} key={e.id} />
        ))}
      </TabItemsDirective>
    </TabComponent>
  )
}

export default Revisions
