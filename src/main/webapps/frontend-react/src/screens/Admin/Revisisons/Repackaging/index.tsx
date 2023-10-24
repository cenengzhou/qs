/* eslint-disable @typescript-eslint/no-explicit-any */
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
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
import { TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import { TooltipComponent } from '@syncfusion/ej2-react-popups'

import './style.css'

const Repackaging = () => {
  const toolbar: ToolbarItems[] = ['ExcelExport', 'CsvExport', 'ColumnChooser']
  const data: any[] = []
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-8 col-md-8">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-4 col-md-4">
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
              field="jobNo"
              headerText="Job Number"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) => e.subcontract.id}
              headerText="Subcontract"
              width="120"
            />
            <ColumnDirective
              field="sequenceNo"
              headerText="Sequence Number"
              width="160"
            />
            <ColumnDirective
              field="description"
              headerText="Description"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) => (
                <TooltipComponent content={e.remark}>
                  <div className="nowrap">{e.remark}</div>
                </TooltipComponent>
              )}
              headerText="Remark"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="subsidiaryCode"
              headerText="Subsidiary Code"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="billItem"
              headerText="Bill Item"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="unit"
              headerText="Unit"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="lineType"
              headerText="Line Type"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="approved"
              headerText="Approved"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="resourceNo"
              headerText="Resource Number"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) => (
                <div
                  className={
                    typeof e.scRate === 'number' && e.scRate < 0
                      ? 'text-red'
                      : ''
                  }
                >
                  {e.scRate.toFixed(4)}
                </div>
              )}
              format="n4"
              headerText="Subcontract Rate"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="quantity"
              headerText="Quantity"
              width="180"
              format="n4"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) => (
                <div
                  className={
                    typeof e.amountCumulativeCert === 'number' &&
                    e.amountCumulativeCert < 0
                      ? 'text-red'
                      : ''
                  }
                >
                  {e.amountCumulativeCert.toFixed(4)}
                </div>
              )}
              headerText="Cum Certified Amount"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="cumCertifiedQuantity"
              headerText="Cum Certified Quantity"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="100"
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

export default Repackaging
