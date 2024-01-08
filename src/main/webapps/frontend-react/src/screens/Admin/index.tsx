import { useNavigate } from 'react-router-dom'

import { NodeSelectEventArgs } from '@syncfusion/ej2-react-navigations'

import Sidebar from '../../components/SideBar'
import { ROUTE_LIST } from './route'
import './style.css'

type TreeDataProps = {
  id: string
  text: string
  iconCss: string
}
const Admin = () => {
  const slectedName = window.location.pathname.split('/').reverse()[0]
  const navigate = useNavigate()

  const treeData: {
    id: string
    text: string
    iconCss: string
    fullText: string
    selected?: boolean
    child?: TreeDataProps[]
  }[] = [
    {
      id: '1',
      text: 'Session',
      fullText: 'Session',
      selected: false,
      iconCss: 'e-icons e-folder'
    },
    {
      id: '2',
      text: 'Manual Procedures',
      fullText: 'ManualProcedures',
      selected: false,
      iconCss: 'e-icons e-form-field'
    },
    {
      id: '3',
      text: 'Revisions',
      fullText: 'Revisions',
      iconCss: 'e-icons e-edit-2',
      selected: false
    },
    {
      id: '4',
      text: 'Transit',
      fullText: 'Transit',
      selected: false,
      iconCss: 'e-icons e-page-columns',
      child: [
        {
          id: '41',
          text: 'UOM Maintenance',
          iconCss: 'e-icons e-radio-button'
        },
        {
          id: '42',
          text: 'Resource Code Maintenance',
          iconCss: 'e-icons e-radio-button'
        }
      ]
    },
    {
      id: '5',
      text: 'Subcontract',
      selected: false,
      fullText: 'Subcontract',
      iconCss: 'e-icons e-activities',
      child: [
        {
          id: '51',
          text: 'Standard Terms Maintenance',
          iconCss: 'e-icons e-radio-button'
        }
      ]
    },
    {
      id: '6',
      text: 'Scheduler Maintenance',
      selected: false,
      fullText: 'SchedulerMaintenance',
      iconCss: 'e-icons e-month'
    },
    {
      id: '7',
      text: 'Announcement Setting',
      selected: false,
      fullText: 'AnnouncementSetting',
      iconCss: 'e-icons e-comments'
    },
    {
      id: '8',
      text: 'System Information',
      selected: false,
      fullText: 'SystemInformation',
      iconCss: 'e-icons e-circle-info'
    },
    {
      id: '9',
      text: 'Health Check',
      selected: false,
      fullText: 'HealthCheck',
      iconCss: 'e-icons e-zoom-in-2'
    }
  ].map(item => {
    if (item.fullText === slectedName) {
      item.selected = true
    } else {
      item.selected = false
    }
    return item
  })

  const onSelect = (e: NodeSelectEventArgs) => {
    if (e.nodeData.text === 'Session') {
      navigate('/admin/Session')
    } else if (e.nodeData.text === 'Revisions') {
      navigate('/admin/Revisions')
    } else if (e.nodeData.text === 'Manual Procedures') {
      navigate('/admin/ManualProcedures')
    } else if (e.nodeData.text === 'UOM Maintenance') {
      navigate('/admin/TransitUOMMaintenance')
    } else if (e.nodeData.text === 'Resource Code Maintenance') {
      navigate('/admin/TransitResourceCodeMaintenance')
    } else if (e.nodeData.text === 'Standard Terms Maintenance') {
      navigate('/admin/SubcontractStandardTermsMaintenance')
    } else if (e.nodeData.text === 'System Information') {
      navigate('/admin/SystemInformation')
    } else if (e.nodeData.text === 'Announcement Setting') {
      navigate('/admin/AnnouncementSetting')
    } else if (e.nodeData.text === 'Health Check') {
      navigate('/admin/HealthCheck')
    } else if (e.nodeData.text === 'Scheduler Maintenance') {
      navigate('/admin/SchedulerMaintenance')
    }
  }

  return (
    <Sidebar
      routeList={ROUTE_LIST}
      treeData={treeData}
      onSelect={onSelect}
    ></Sidebar>
  )
}

export default Admin
