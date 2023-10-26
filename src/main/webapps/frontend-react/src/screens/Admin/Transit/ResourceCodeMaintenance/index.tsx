/* eslint-disable @typescript-eslint/no-explicit-any */

/* eslint-disable @typescript-eslint/naming-convention */
import {
  ColumnChooser,
  ColumnDirective,
  ColumnMenu,
  ColumnsDirective,
  ExcelExport,
  Filter,
  GridComponent,
  Inject,
  Sort,
  Toolbar,
  ToolbarItems
} from '@syncfusion/ej2-react-grids'

import './style.css'

const ResourceCodeMaintenance = () => {
  const toolbar: ToolbarItems[] = ['ExcelExport', 'CsvExport', 'ColumnChooser']
  const data = [
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 1,
      matchingType: 'BS',
      resourceCode: '10101',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 2,
      matchingType: 'BS',
      resourceCode: '10102',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 3,
      matchingType: 'BS',
      resourceCode: '10103',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 4,
      matchingType: 'BS',
      resourceCode: '10104',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 5,
      matchingType: 'BS',
      resourceCode: '10105',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 6,
      matchingType: 'BS',
      resourceCode: '10106',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 7,
      matchingType: 'BS',
      resourceCode: '10107',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 8,
      matchingType: 'BS',
      resourceCode: '10108',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    },
    {
      createdUser: 'chinhangwo',
      createdDate: '2011-01-19T01:35:03.000+00:00',
      lastModifiedUser: 'chinhangwo',
      lastModifiedDate: '2011-01-19T01:35:03.000+00:00',
      systemStatus: 'ACTIVE',
      id: 9,
      matchingType: 'BS',
      resourceCode: '10109',
      objectCode: '110199',
      subsidiaryCode: '19999999'
    }
  ]
  return (
    <div className="admin-container flex-row">
      <div className="col-sm-12 col-md-4 padding10">
        <div className="e-card transit-card">
          <div className="e-card-header bg-info">
            <div className="e-card-header-caption">
              <div className="e-card-header-title transit-title">
                <span className="e-icons e-circle-info"></span>
                <span>Resource code matching</span>
              </div>
            </div>
          </div>
          <div className="e-card-content text-content transit-content">
            <div className="text-border">
              <h3>
                Lexical pattern matchers provide excellent performance and ease
                of use,
              </h3>
              <p>
                but have a limited vocabulary. Syntactic matchers provide more
                precision, but may sacrifice performance, robustness, or power.
                To achieve more of the benefits of both models, we extend the
                pattern syntax of AWK to support matching of abstract syntax
                trees, as demonstrated in a tool called TAWK. Its pattern syntax
                is language-independent, based on abstract tree patterns. As in
                AWK, patterns can have associated actions, which in TAWK are
                written in this for generality, familiarity, and performance.
                The use of this is simplified by high-level libraries and
                dynamic linking. To allow processing of program files containing
                non-syntactic constructs such as textual macros, mechanisms have
                been designed that allow matching of ""language-like" macros in
                a syntactic fashion
              </p>
            </div>
          </div>
        </div>
      </div>
      <div className="admin-content col-sm-12 col-md-8 padding10">
        <GridComponent
          dataSource={data}
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
              field="matchingType"
              headerText="Matching Type"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="resourceCode"
              headerText="Resource Code"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="150"
            />
            <ColumnDirective
              field="subsidiaryCode"
              headerText="Subsidiary Code"
              width="150"
            ></ColumnDirective>
          </ColumnsDirective>
          <Inject
            services={[
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

export default ResourceCodeMaintenance
