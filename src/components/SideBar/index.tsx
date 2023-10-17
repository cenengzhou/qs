import * as React from 'react';
import { useRef, useState } from 'react';
import { SidebarComponent, TreeViewComponent, ToolbarComponent, NodeSelectEventArgs, ItemsDirective, ItemDirective, ClickEventArgs } from '@syncfusion/ej2-react-navigations';
import { ListViewComponent, SelectEventArgs } from '@syncfusion/ej2-react-lists';
import './style.css';
const Sidebar = () => {
    let sidebarobj = useRef<SidebarComponent>(null);
    let listObj = useRef<ListViewComponent>(null);
    //ListView data source initialization
    let inboxData: { [key: string]: Object }[] = [
        { id: "1", text: "Albert Lives", subject: "Business dinner invitation", message: "Hello Uta Morgan," },
        { id: "2", text: "Ila Russo", subject: "Opening for Sales Manager", message: "Hello Jelani Moreno," },
        { id: "3", text: "Garth Owen", subject: "Application for Job Title", message: "Hello Ila Russo," },
        { id: "4", text: "Ursula Patterson", subject: "Programmer Position Application", message: "Hello Kerry Best," },
        { id: "5", text: "Nichole Rivas", subject: "Annual Conference", message: "Hi Igor Mccoy," }
    ];
    let treeData: { [key: string]: Object }[] = [
      {
        id: "1",
        name: "Session",
        iconCss: "e-icons e-folder",
        selected: true,
      },
      {
        id: "2",
        name: "Manual Procedures",
        iconCss: "e-icons e-form-field",
      },
      { id: "3", name: "Revisions", iconCss: "e-icons e-edit-2" },
      {
        id: "4",
        name: "Transit",
        iconCss: "e-icons e-page-columns",
        child: [
          {
            id: "41",
            name: "UOM Maintenance",
            iconCss: "e-icons e-radio-button",
          },
          {
            id: "42",
            name: "Resource Code Maintenance",
            iconCss: "e-icons e-radio-button",
          },
        ],
      },
      {
        id: "5",
        name: "Subcontract",
        iconCss: "e-icons e-activities",
        child: [
          {
            id: "51",
            name: "Standard Terms Maintenance",
            iconCss: "e-icons e-radio-button",
          },
        ],
      },
      {
        id: "6",
        name: "Scheduler Maintenance",
        iconCss: "e-icons e-month",
      },
      {
        id: "7",
        name: "Announcement Setting",
        iconCss: "e-icons e-comments",
      },
      {
        id: "8",
        name: "System Information",
        iconCss: "e-icons e-circle-info",
      },
      {
        id: "9",
        name: "System Information",
        iconCss: "e-icons e-zoom-in-2",
      },
    ];
    const [data, setData] = useState<{[key: string]: Object}[]>(inboxData);

    let treeFields: { [key: string]: Object } = {
      dataSource: treeData,
      id: "id",
      text: "name",
      selected: "selected",
      child: "child",
      expanded: "expanded",
      iconCss: "iconCss",
    };
    const listTemplate = (data: any) => {
        return (
            <div className="e-list-wrapper e-list-avatar e-list-multi-line">
                <span className="e-avatar e-avatar-circle e-icon sf-icon-profile"></span>
                <span className="e-list-item-header">{data.text}</span>
                <span className="e-list-content">{data.subject}</span>
                <span className="e-list-text">{data.message}</span>
            </div>
        );
    }
    let fields: { [ key: string]: Object } = { id: "id", text: "text" }

    return (
      <div className="control-section" id="sidebar-wrapper">
        {/* main content declaration */}
        <div></div>
        <div className="maincontent">
          <ListViewComponent
            id="listView"
            ref={listObj}
            template={listTemplate as any}
            cssClass="e-list-template"
            dataSource={data}
            fields={fields}
          ></ListViewComponent>
        </div>
        <SidebarComponent
          id="defaultSidebar"
          ref={sidebarobj}
          className="default-sidebar"
          width="250px"
          mediaQuery="(min-width: 600px)"
          target=".maincontent"
          position="Left"
          isOpen={true}
        >
          <h6 className="sidebar-title">19019 HKHA Wah King St FDN</h6>
          <TreeViewComponent
            id="mainTree"
            cssClass="main-treeview"
            fields={treeFields}
            expandOn="Click"
          ></TreeViewComponent>
        </SidebarComponent>
      </div>
    );
    
    
}
export default Sidebar;