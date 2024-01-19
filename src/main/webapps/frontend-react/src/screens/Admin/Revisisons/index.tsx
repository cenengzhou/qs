import {
  SelectingEventArgs,
  TabComponent,
  TabItemDirective,
  TabItemsDirective
} from '@syncfusion/ej2-react-navigations'

import { useHasRole } from '../../../hooks/useHasRole'
import Subcontract from '../Revisisons/Subcontract'
import Addendum from './Addendum'
import AddendumDetail from './AddendumDetail'
import Approval from './Approval'
import AttachmentRender from './Attachment'
import ConsultancyAgreement from './ConsultancyAgreement'
import FinalAccount from './FinalAccount'
import MainCertificate from './MainCertificate'
import MonthlyMovement from './MonthlyMovement'
import PaymentRender from './Payment'
import RocRender from './ROC'
import ROCDetail from './ROCDetail'
import RocSubDetail from './ROCSubDetail'
import RepackagingRender from './Repackaging'
import SubcontractDetail from './SubcontractDetail'
import Tender from './Tender'
import TenderDetail from './TenderDetail'
import Transit from './Transit'
import './style.css'

interface TabData {
  text: string
  id: string
  content: JSX.Element
}

const Revisions = () => {
  const { hasRole } = useHasRole()
  const isQsAdm = hasRole('ROLE_QS_QS_ADM')

  const tabData: TabData[] = [
    {
      text: 'Subcontract',
      id: 'subcontract',
      content: <Subcontract isQsAdm={isQsAdm} />
    },
    {
      text: 'Subcontract Detail',
      id: 'subcontractDetail',
      content: <SubcontractDetail isQsAdm={isQsAdm} />
    },
    {
      text: 'Payment',
      id: 'payment',
      content: <PaymentRender isQsAdm={isQsAdm} />
    },
    { text: 'Addendum', id: 'addendum', content: <Addendum /> },
    {
      text: 'Addendum Detail',
      id: 'addendumDetail',
      content: <AddendumDetail isQsAdm={isQsAdm} />
    },
    {
      text: 'Main Certificate',
      id: 'mainCertificate',
      content: <MainCertificate />
    },
    {
      text: 'Repackaging',
      id: 'Repackaging',
      content: <RepackagingRender isQsAdm={isQsAdm} />
    },
    { text: 'Approval', id: 'approval', content: <Approval /> },
    {
      text: 'Attachment',
      id: 'attachment',
      content: <AttachmentRender isQsAdm={isQsAdm} />
    },
    { text: 'Transit', id: 'transit', content: <Transit /> },
    { text: 'Tender', id: 'tender', content: <Tender /> },
    {
      text: 'Tender Detail',
      id: 'tenderDetail',
      content: <TenderDetail isQsAdm={isQsAdm} />
    },
    {
      text: 'Monthly Movement',
      id: 'monthlyMovement',
      content: <MonthlyMovement />
    },
    { text: 'ROC', id: 'ROC', content: <RocRender isQsAdm={isQsAdm} /> },
    { text: 'ROC Detail', id: 'ROCDetail', content: <ROCDetail /> },
    {
      text: 'ROC Subdetail',
      id: 'ROCSubdetail',
      content: <RocSubDetail />
    },
    {
      text: 'Final Account',
      id: 'finalAccount',
      content: <FinalAccount />
    },
    {
      text: 'Consultancy Agreement',
      id: 'consultancyAgreement',
      content: <ConsultancyAgreement />
    }
  ]

  const select = (e: SelectingEventArgs) => {
    if (e.isSwiped) {
      e.cancel = true
    }
  }

  return (
    <>
      <TabComponent id="defaultTab" overflowMode="MultiRow" selecting={select}>
        <TabItemsDirective>
          {tabData.map((e: TabData) => (
            <TabItemDirective header={e} content={() => e.content} key={e.id} />
          ))}
        </TabItemsDirective>
      </TabComponent>
    </>
  )
}

export default Revisions
