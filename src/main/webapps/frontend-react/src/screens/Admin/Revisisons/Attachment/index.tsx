/* eslint-disable @typescript-eslint/no-explicit-any */
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  ColumnChooser,
  ColumnDirective,
  ColumnMenu,
  ColumnsDirective,
  ExcelExport,
  Filter,
  GridComponent,
  Inject,
  Page,
  Sort,
  Toolbar,
  ToolbarItems
} from '@syncfusion/ej2-react-grids'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

import './style.css'

const Attachment = () => {
  const toolbar: ToolbarItems[] = ['ExcelExport', 'CsvExport', 'ColumnChooser']

  const data: any[] = [
    {
      usernameCreated: 'victorhui',
      dateCreated: '2020-11-12T03:54:14.000+00:00',
      usernameLastModified: 'victorhui',
      dateLastModified: '2020-11-12T03:54:14.000+00:00',
      id: 412114,
      idTable: 75852,
      nameTable: 'SUBCONTRACT',
      noSequence: 1,
      typeDocument: '5',
      nameFile: 'Subcontract Tender Analysis Approved.pdf',
      pathFile: '13892\\Subcontract Tender Analysis Approved.pdf',
      text: null
    },
    {
      usernameCreated: 'victorhui',
      dateCreated: '2020-11-27T09:01:51.000+00:00',
      usernameLastModified: 'victorhui',
      dateLastModified: '2020-11-27T09:01:51.000+00:00',
      id: 413785,
      idTable: 75852,
      nameTable: 'SUBCONTRACT',
      noSequence: 2,
      typeDocument: '5',
      nameFile: 'Subcontract Document Executed by Subcontractor.pdf',
      pathFile: '13892\\Subcontract Document Executed by Subcontractor.pdf',
      text: undefined
    }
  ]
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-3 col-md-3">
          <DropDownListComponent
            dataSource={['ADDENDUM', 'PAYMENT', 'MAIN_CERT']}
            cssClass="e-outline"
            floatLabelType="Always"
            showClearButton
            placeholder="Table Name"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <GridComponent
          dataSource={data}
          allowPaging={true}
          width="100%"
          height="100%"
          allowExcelExport
          toolbar={toolbar}
          allowTextWrap={true}
          showColumnChooser
          showColumnMenu
          allowFiltering
          allowSorting
          filterSettings={{ type: 'Menu' }}
          cssClass="no-margin-right"
        >
          <ColumnsDirective>
            <ColumnDirective
              field="id"
              headerText="ID"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="idTable"
              headerText="Table ID"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="nameTable"
              headerText="Table Name"
              width="150"
            />
            <ColumnDirective
              field="noSequence"
              headerText="Sequence Number"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="typeDocument"
              headerText="Document Type"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="nameFile"
              headerText="File Name"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="pathFile"
              headerText="File Path"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) => e.text}
              headerText="Text"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="usernameCreated"
              headerText="Created User"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="dateCreated"
              headerText="Created Date"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="usernameLastModified"
              headerText="Last Modified User"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="dateLastModified"
              headerText="Last Modified Date"
              width="200"
            ></ColumnDirective>
          </ColumnsDirective>
          <Inject
            services={[
              Page,
              ExcelExport,
              Toolbar,
              ColumnChooser,
              ColumnMenu,
              Filter,
              Sort
            ]}
          />
        </GridComponent>
      </div>
    </div>
  )
}

export default Attachment
