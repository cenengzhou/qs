/* eslint-disable @typescript-eslint/ban-types */
import * as React from 'react'
import { useRef, useState } from 'react'
import { Route, Routes } from 'react-router-dom'

import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  BreadcrumbBeforeItemRenderEventArgs,
  BreadcrumbComponent,
  FieldsSettingsModel,
  NodeSelectEventArgs,
  SidebarComponent,
  TreeViewComponent
} from '@syncfusion/ej2-react-navigations'

import { Route as RouteType } from '../../interfaces/Route'
import './style.css'

interface Props {
  routeList: RouteType[]
  treeData: { [key: string]: Object }[]
  onSelect: (e: NodeSelectEventArgs) => void
}

const Sidebar = ({ routeList, treeData, onSelect }: Props) => {
  const sidebar = useRef<SidebarComponent>(null)
  const [isOpen, setIsOpen] = useState(true)

  const treeFields: FieldsSettingsModel = {
    dataSource: treeData,
    id: 'id',
    text: 'text',
    selected: 'selected',
    child: 'child',
    expanded: 'expanded',
    iconCss: 'iconCss'
  }

  const sidebarToggle = () => {
    sidebar.current?.toggle()
  }

  const sidebarChange = () => {
    setIsOpen((p: boolean) => {
      return !p
    })
  }
  const beforeItemRenderHandler = (
    args: BreadcrumbBeforeItemRenderEventArgs
  ): void => {
    if (
      args.item.text === 'pcms' ||
      args.item.text === 'web' ||
      args.item.text === 'admin'
    ) {
      args.cancel = true
    }
    if (args.item.iconCss === 'e-icons e-home') {
      args.item.url = '/pcms/web'
    }
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
          <BreadcrumbComponent
            cssClass="breadcrumb"
            beforeItemRender={beforeItemRenderHandler}
          />
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
          <div className="chevron-container">
            <ButtonComponent
              cssClass="e-small e-round"
              iconCss="e-info e-icons e-chevron-left-double"
              onClick={() => sidebar.current?.hide()}
            />
          </div>
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
