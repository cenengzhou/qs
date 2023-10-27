import * as React from 'react'
import { useRef, useState } from 'react'
import { Route, Routes, useNavigate } from 'react-router-dom'

import {
  BreadcrumbComponent,
  NodeSelectEventArgs,
  SidebarComponent,
  TreeViewComponent
} from '@syncfusion/ej2-react-navigations'

import { Route as RouteType } from '../../interfaces/Route'
import './style.css'

interface Props {
  routeList: RouteType[]
}

const Sidebar = ({ routeList }: Props) => {
  const navigate = useNavigate()
  const sidebar = useRef<SidebarComponent>(null)
  const [isOpen, setIsOpen] = useState(true)

  // eslint-disable-next-line @typescript-eslint/ban-types
  const treeData: { [key: string]: Object }[] = [
    {
      id: '1',
      name: 'Session',
      iconCss: 'e-icons e-folder',
      selected: true
    },
    {
      id: '2',
      name: 'Manual Procedures',
      iconCss: 'e-icons e-form-field'
    },
    { id: '3', name: 'Revisions', iconCss: 'e-icons e-edit-2' },
    {
      id: '4',
      name: 'Transit',
      iconCss: 'e-icons e-page-columns',
      child: [
        {
          id: '41',
          name: 'UOM Maintenance',
          iconCss: 'e-icons e-radio-button'
        },
        {
          id: '42',
          name: 'Resource Code Maintenance',
          iconCss: 'e-icons e-radio-button'
        }
      ]
    },
    {
      id: '5',
      name: 'Subcontract',
      iconCss: 'e-icons e-activities',
      child: [
        {
          id: '51',
          name: 'Standard Terms Maintenance',
          iconCss: 'e-icons e-radio-button'
        }
      ]
    },
    {
      id: '6',
      name: 'Scheduler Maintenance',
      iconCss: 'e-icons e-month'
    },
    {
      id: '7',
      name: 'Announcement Setting',
      iconCss: 'e-icons e-comments'
    },
    {
      id: '8',
      name: 'System Information',
      iconCss: 'e-icons e-circle-info'
    },
    {
      id: '9',
      name: 'System Information',
      iconCss: 'e-icons e-zoom-in-2'
    }
  ]

  // eslint-disable-next-line @typescript-eslint/ban-types
  const treeFields: { [key: string]: Object } = {
    dataSource: treeData,
    id: 'id',
    text: 'name',
    selected: 'selected',
    child: 'child',
    expanded: 'expanded',
    iconCss: 'iconCss'
  }

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

  const sidebarToggle = () => {
    sidebar.current?.toggle()
  }

  const sidebarChange = () => {
    setIsOpen((p: boolean) => {
      return !p
    })
  }

  return (
    <div
      className={isOpen ? 'control-section' : 'control-section paddingL50'}
      id="sidebar-wrapper"
    >
      <div className="siderbar-btn-content">
        <span
          className="e-icons e-chevron-right-double e-large"
          onClick={sidebarToggle}
        ></span>
      </div>
      <div className="maincontent">
        <div className="listView">
          <BreadcrumbComponent cssClass="breadcrumb"></BreadcrumbComponent>
          <Routes>
            {routeList.map((item, index) => (
              <Route key={index} path={item.path} element={item.element} />
            ))}
          </Routes>
        </div>

        <SidebarComponent
          id="defaultSidebar"
          ref={sidebar}
          className="default-sidebar"
          width="250px"
          mediaQuery="(min-width: 600px)"
          target=".maincontent"
          position="Left"
          enableGestures={false}
          isOpen={isOpen}
          change={sidebarChange}
        >
          <h6 className="sidebar-title">19019 HKHA Wah King St FDN</h6>
          <TreeViewComponent
            id="mainTree"
            cssClass="main-treeview"
            fields={treeFields}
            expandOn="Click"
            nodeSelected={onSelect}
          ></TreeViewComponent>
        </SidebarComponent>
      </div>
    </div>
  )
}

export default Sidebar
