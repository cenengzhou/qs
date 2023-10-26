/* eslint-disable @typescript-eslint/naming-convention */

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

import './style.css'

const Session = () => {
  const toolbar: ToolbarItems[] = ['ExcelExport', 'CsvExport', 'ColumnChooser']

  const data: any[] = [
    {
      authType: 'LDAP',
      idleTime: 65,
      expired: false,
      creationTime: 1698219512026,
      lastAccessedTime: 1698289099682,
      maxInactiveInterval: 60000,
      principal: {
        fullname: 'Ce Neng Zhou',
        username: 'cenengzhou',
        authType: 'LDAP',
        domainName: null,
        title: 'System Developer',
        jobNoExcludeList: [],
        enabled: true,
        password: null,
        authorities: [
          {
            authority: 'ROLE_QS_QS',
            RoleDescription: 'QS who can handle daily operations',
            RoleName: 'ROLE_QS_QS'
          },
          {
            authority: 'ROLE_QS_REVIEWER',
            RoleDescription:
              'QS Reviewer who review daily operations that are done by QSs',
            RoleName: 'ROLE_QS_REVIEWER'
          },
          {
            authority: 'ROLE_QS_ENQ',
            RoleDescription:
              'Any user who has to access QS has to be granted with this role',
            RoleName: 'ROLE_QS_ENQ'
          },
          {
            authority: 'JOB_ALL',
            RoleDescription: 'All jobs available in Gammon',
            RoleName: 'JOB_ALL'
          },
          {
            authority: 'ROLE_QS_IMS_ENQ',
            RoleDescription: 'IMS who can view QS applications settings',
            RoleName: 'ROLE_QS_IMS_ENQ'
          },
          {
            authority: 'ROLE_QS_IMS_ADM',
            RoleDescription: 'IMS who can handle QS application settings',
            RoleName: 'ROLE_QS_IMS_ADM'
          }
        ],
        accountNonLocked: true,
        credentialsNonExpired: true,
        accountNonExpired: true,
        EmailAddress: 'CeNeng.Zhou@gammonconstruction.com',
        StaffID: '000001',
        UserRoles: [
          {
            authority: 'ROLE_QS_QS',
            RoleDescription: 'QS who can handle daily operations',
            RoleName: 'ROLE_QS_QS'
          },
          {
            authority: 'ROLE_QS_REVIEWER',
            RoleDescription:
              'QS Reviewer who review daily operations that are done by QSs',
            RoleName: 'ROLE_QS_REVIEWER'
          },
          {
            authority: 'ROLE_QS_ENQ',
            RoleDescription:
              'Any user who has to access QS has to be granted with this role',
            RoleName: 'ROLE_QS_ENQ'
          },
          {
            authority: 'JOB_ALL',
            RoleDescription: 'All jobs available in Gammon',
            RoleName: 'JOB_ALL'
          },
          {
            authority: 'ROLE_QS_IMS_ENQ',
            RoleDescription: 'IMS who can view QS applications settings',
            RoleName: 'ROLE_QS_IMS_ENQ'
          },
          {
            authority: 'ROLE_QS_IMS_ADM',
            RoleDescription: 'IMS who can handle QS application settings',
            RoleName: 'ROLE_QS_IMS_ADM'
          }
        ]
      },
      lastRequest: '2023-10-26T02:58:19.621+00:00',
      sessionId: '321EB5F772CB29602D8254C0D38C1584'
    }
  ]
  return (
    <div className="admin-container">
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
              template={(e: any) => e.principal.fullname}
              headerText="Name"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="authType"
              headerText="AuthType"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="sessionId"
              headerText="Session Id"
              width="150"
            />
            <ColumnDirective
              template={(e: any) => new Date(e.creationTime).toLocaleString()}
              headerText="Creation Time"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) =>
                new Date(e.lastAccessedTime).toLocaleString()
              }
              headerText="Last Accessed Time"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) => new Date(e.lastRequest).toLocaleString()}
              headerText="Last Request"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="maxInactiveInterval"
              headerText="Max Inactive Interval"
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
      <div className="row">
        <ButtonComponent cssClass="e-info full-btn" iconCss="e-icons e-save-2">
          Expire Selected
        </ButtonComponent>
      </div>
    </div>
  )
}

export default Session
