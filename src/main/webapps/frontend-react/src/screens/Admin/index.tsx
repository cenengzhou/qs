/* eslint-disable @typescript-eslint/ban-types */
import { useNavigate } from 'react-router-dom'

import { NodeSelectEventArgs } from '@syncfusion/ej2-react-navigations'

import Sidebar from '../../components/SideBar'
import { ROUTE_LIST } from './route'
import './style.css'

const Admin = () => {
  const navigate = useNavigate()
  const treeData: { [key: string]: Object }[] = [
    {
      id: '1',
      text: 'Session',
      iconCss: 'e-icons e-folder',
      selected: true
    },
    {
      id: '2',
      text: 'Manual Procedures',
      iconCss: 'e-icons e-form-field'
    },
    { id: '3', text: 'Revisions', iconCss: 'e-icons e-edit-2' },
    {
      id: '4',
      text: 'Transit',
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
      iconCss: 'e-icons e-month'
    },
    {
      id: '7',
      text: 'Announcement Setting',
      iconCss: 'e-icons e-comments'
    },
    {
      id: '8',
      text: 'System Information',
      iconCss: 'e-icons e-circle-info'
    },
    {
      id: '9',
      text: 'System Information',
      iconCss: 'e-icons e-zoom-in-2'
    }
  ]

  const onSelect = (e: NodeSelectEventArgs) => {
    if (e.nodeData.text === 'Session') {
      navigate('/admin/Session')
    } else if (e.nodeData.text === 'Revisions') {
      navigate('/admin/Revisisons')
    } else if (e.nodeData.text === 'Manual Procedures') {
      navigate('/admin/ManualProcedures')
    } else if (e.nodeData.text === 'UOM Maintenance') {
      navigate('/admin/TransitUOMMaintenance')
    } else if (e.nodeData.text === 'Resource Code Maintenance') {
      navigate('/admin/TransitResourceCodeMaintenance')
    } else if (e.nodeData.text === 'Standard Terms Maintenance') {
      navigate('/admin/SubcontractStandardTermsMaintenance')
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
