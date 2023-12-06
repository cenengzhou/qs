import { useRef } from 'react'

import { ListViewComponent } from '@syncfusion/ej2-react-lists'
import {
  TabComponent,
  TabItemDirective,
  TabItemsDirective
} from '@syncfusion/ej2-react-navigations'
import {
  CellDirective,
  CellsDirective,
  ColumnDirective,
  ColumnsDirective,
  RangeDirective,
  RangesDirective,
  RowDirective,
  RowsDirective,
  SheetDirective,
  SheetsDirective,
  SpreadsheetComponent
} from '@syncfusion/ej2-react-spreadsheet'

import './style.css'

interface LinksDataPorps {
  title: string
  content: string
  icon: string
  category: string
}

const RightSidebar = () => {
  const linksData = [
    {
      title: 'Helpdesk',
      content:
        'The fastest way to get help from IMS! Tell us about your problem so we can help ASAP!',
      icon: 'circle-info bg-gray',
      category: 'Useful Links'
    },
    {
      title: 'Guidelines',
      content:
        'Step-by-Step video to guide you through the core workflows and comprehensive documentation of QS 2.0',
      icon: 'bookmark-fill bg-green',
      category: 'Useful Links'
    },
    {
      title: 'Forms, Templates & Documents',
      content: 'Common forms for download',
      icon: 'border-box bg-blue',
      category: 'Useful Links'
    },
    {
      title: 'Tips',
      content: 'Information and tips',
      icon: 'comment-add bg-purple',
      category: 'Useful Links'
    },
    {
      title: 'UCC List',
      content: 'UCC List',
      icon: 'list-unordered bg-yellow',
      category: 'Useful Links'
    },
    {
      title: 'Approval System',
      content:
        'To Review and Enquiry Subcontract, Procurement, Subcontract Appraisal Approvals',
      icon: 'check bg-red',
      category: 'Useful Systems'
    },
    {
      title: 'Gammon ERP Portal',
      content: 'Gammon ERP Portal',
      icon: 'settings bg-yellow',
      category: 'Useful Systems'
    },
    {
      title: 'JDE',
      content: 'Oracle JD Edwards',
      icon: 'form-field bg-green',
      category: 'Useful Systems'
    },
    {
      title: 'Business Management System',
      content:
        'Documents for Pre-Contract, Project Delivery, Group Wide and Business Support',
      icon: 'page-columns bg-cyan',
      category: 'Useful Systems'
    },
    {
      title: 'Other Gammon Systems',
      content: 'Full list of Gammon Systems',
      icon: 'plus-small bg-yellow',
      category: 'Useful Systems'
    }
  ]
  const fields = { text: 'Name', groupBy: 'category' }
  const listTemplate = (data: LinksDataPorps) => {
    return (
      <div className="e-list-wrapper e-list-multi-line e-list-avatar">
        <div className={`${data.icon} e-avatar e-avatar-small e-avatar-circle`}>
          <span className={`e-icons e-${data.icon} color-white`}></span>
        </div>
        <span className="e-list-item-header" title={data.title}>
          {data.title}
        </span>
        <span className="e-list-content" title={data.content}>
          {data.content}
        </span>
      </div>
    )
  }
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const groupTemplate = (data: any) => {
    return (
      <div className="e-list-wrapper">
        <span className="e-list-item-content">{data.items[0].category}</span>
      </div>
    )
  }
  const linksContent = () => {
    return (
      <ListViewComponent
        id="linksList"
        dataSource={linksData}
        fields={fields}
        cssClass="e-list-template"
        template={listTemplate}
        groupTemplate={groupTemplate}
      ></ListViewComponent>
    )
  }
  const accountCodeContent = () => {
    return (
      <div id="AccountCode">
        <TabComponent cssClass="e-fill" allowDragAndDrop={false}>
          <TabItemsDirective>
            <TabItemDirective
              header={{ text: 'Object Code' }}
              content={ObjectCode}
            />
            <TabItemDirective
              header={{ text: 'Subsidiary Code' }}
              content={SubsidiaryCode}
            />
          </TabItemsDirective>
        </TabComponent>
      </div>
    )
  }

  const ObjectCode = () => {
    const spreadsheet = useRef<SpreadsheetComponent>(null)
    return (
      <div id="ObjectCode">
        <SpreadsheetComponent ref={spreadsheet} height="85vh">
          <SheetsDirective>
            <SheetDirective name="Object Code">
              <RangesDirective>
                <RangeDirective dataSource={[]}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
                  <CellsDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
    )
  }
  const SubsidiaryCode = () => {
    const spreadsheet = useRef<SpreadsheetComponent>(null)
    return (
      <div id="SubsidiaryCode">
        <SpreadsheetComponent ref={spreadsheet} height="85vh">
          <SheetsDirective>
            <SheetDirective name="Subsidiary Code">
              <RangesDirective>
                <RangeDirective dataSource={[]}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
                  <CellsDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
    )
  }

  const AddressBookContent = () => {
    const addressspreadsheet = useRef<SpreadsheetComponent>(null)
    return (
      <div id="AddressBook">
        <SpreadsheetComponent ref={addressspreadsheet} height="85vh">
          <SheetsDirective>
            <SheetDirective name="Address Book">
              <RangesDirective>
                <RangeDirective dataSource={[]}></RangeDirective>
              </RangesDirective>
              <RowsDirective>
                <RowDirective>
                  <CellsDirective>
                    <CellDirective value="Object Code"></CellDirective>
                    <CellDirective value="Description"></CellDirective>
                  </CellsDirective>
                </RowDirective>
              </RowsDirective>
              <ColumnsDirective>
                <ColumnDirective width={100}></ColumnDirective>
                <ColumnDirective width={100}></ColumnDirective>
              </ColumnsDirective>
            </SheetDirective>
          </SheetsDirective>
        </SpreadsheetComponent>
      </div>
    )
  }

  return (
    <TabComponent width={250} cssClass="right-sidebar">
      <TabItemsDirective>
        <TabItemDirective
          header={{ text: 'Links' }}
          content={linksContent}
        ></TabItemDirective>
        <TabItemDirective
          header={{ text: 'Account Code' }}
          content={accountCodeContent}
        ></TabItemDirective>
        <TabItemDirective
          header={{ text: 'Address Book' }}
          content={AddressBookContent}
        ></TabItemDirective>
      </TabItemsDirective>
    </TabComponent>
  )
}

export default RightSidebar
