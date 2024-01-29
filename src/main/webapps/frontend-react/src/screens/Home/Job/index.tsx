import { useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'

import { NodeSelectEventArgs } from '@syncfusion/ej2-react-navigations'

import Sidebar from '../../../components/SideBar'
import { ROUTE_LIST } from './route'
import './style.css'

type TreeDataProps = {
  id: string
  text: string
  iconCss: string
}
const Job = () => {
  const location = useLocation()
  const navigate = useNavigate()

  useEffect(() => {
    if (location.pathname === '/home/job') {
      navigate('/home/job/dashboard')
    }
  })

  const treeData: {
    id: string
    text: string
    iconCss: string
    url: string
    selected?: boolean
    child?: TreeDataProps[]
  }[] = [
    {
      id: '1',
      text: 'Job Dashboard',
      url: '/home/job/dashboard',
      selected: true,
      iconCss: 'e-icons e-folder'
    },
    {
      id: '2',
      text: 'Select Job',
      url: '/home/job-select',
      selected: false,
      iconCss: 'e-icons e-form-field'
    },
    {
      id: '3',
      text: 'Job Information',
      url: '/home/job/info',
      iconCss: 'e-icons e-edit-2',
      selected: false
    },
    {
      id: '4',
      text: 'Personnel',
      url: '/home/job/personnel',
      selected: false,
      iconCss: 'e-icons e-page-columns'
    },
    {
      id: '5',
      text: 'Variation \\ CE KPI',
      selected: false,
      url: '/home/job/kpi',
      iconCss: 'e-icons e-activities'
    },
    {
      id: '6',
      text: 'Claims \\ CNCE KPI',
      selected: false,
      url: '/home/job/claim',
      iconCss: 'e-icons e-month'
    },
    {
      id: '7',
      text: 'Monthly Movement',
      selected: false,
      url: '/home/job/forecast',
      iconCss: 'e-icons e-comments'
    },
    {
      id: '8',
      text: 'ROC',
      selected: false,
      url: '/home/job/roc',
      iconCss: 'e-icons e-circle-info'
    },
    {
      id: '9',
      text: 'Attachment',
      selected: false,
      url: '/home/job/attachment',
      iconCss: 'e-icons e-zoom-in-2'
    },
    {
      id: '10',
      text: 'Create Account Code',
      selected: false,
      url: '/home/job/accountMaster',
      iconCss: 'e-icons e-zoom-in-2'
    }
  ].map(item => {
    if (item.url === location.pathname) {
      item.selected = true
    } else {
      item.selected = false
    }
    return item
  })

  const onSelect = (e: NodeSelectEventArgs) => {
    if (e.nodeData.id) {
      const index = treeData.findIndex(item => item.id === e.nodeData.id)
      navigate(treeData[index].url)
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

export default Job
