/* eslint-disable @typescript-eslint/naming-convention */

/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  ColumnChooser,
  ColumnDirective,
  ColumnMenu,
  ColumnsDirective,
  Edit,
  ExcelExport,
  Filter,
  ForeignKey,
  GridComponent,
  Inject,
  Page,
  Sort,
  Toolbar,
  ToolbarItems
} from '@syncfusion/ej2-react-grids'

import { GLOBALPARAMETER } from '../../../../constants/global'
import './style.css'

const StandardTermsMaintenance = () => {
  const toolbar: ToolbarItems[] = [
    'Add',
    'Edit',
    'Update',
    'Cancel',
    'ExcelExport',
    'CsvExport',
    'ColumnChooser'
  ]

  const data: any[] = [
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:53.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:53.000+00:00',
      systemStatus: 'ACTIVE',
      id: 3,
      formOfSubcontract: 'Consultancy Agreement',
      company: '00000',
      scPaymentTerm: 'QS4',
      scMaxRetentionPercent: 0.0,
      scInterimRetentionPercent: 0.0,
      scMOSRetentionPercent: 0.0,
      retentionType: 'No Retention'
    },
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:55.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:55.000+00:00',
      systemStatus: 'ACTIVE',
      id: 4,
      formOfSubcontract: 'Internal Trading',
      company: '00000',
      scPaymentTerm: 'QS1',
      scMaxRetentionPercent: 0.0,
      scInterimRetentionPercent: 0.0,
      scMOSRetentionPercent: 0.0,
      retentionType: 'No Retention'
    },
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:53.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:53.000+00:00',
      systemStatus: 'ACTIVE',
      id: 1,
      formOfSubcontract: 'Major',
      company: '00000',
      scPaymentTerm: 'QS2',
      scMaxRetentionPercent: 5.0,
      scInterimRetentionPercent: 10.0,
      scMOSRetentionPercent: 10.0,
      retentionType: 'Percentage - Revised SC Sum'
    },
    {
      createdUser: 'SYSTEM',
      createdDate: '2017-08-10T11:10:53.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2017-08-10T11:10:53.000+00:00',
      systemStatus: 'ACTIVE',
      id: 2,
      formOfSubcontract: 'Minor',
      company: '00000',
      scPaymentTerm: 'QS2',
      scMaxRetentionPercent: 5.0,
      scInterimRetentionPercent: 10.0,
      scMOSRetentionPercent: 10.0,
      retentionType: 'Percentage - Revised SC Sum'
    }
  ]

  const save = (e: any) => {
    console.log(e)
    console.log(data)
  }

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
          editSettings={{
            allowAdding: true,
            allowEditing: true,
            allowDeleting: true
          }}
          actionBegin={save}
          cssClass="no-margin-right"
        >
          <ColumnsDirective>
            <ColumnDirective
              field="company"
              headerText="Company"
              allowEditing={false}
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="formOfSubcontract"
              headerText="Form Of Subcontract"
              allowEditing={false}
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="scPaymentTerm"
              headerText="SC Payment Term"
              width="200"
              dataSource={GLOBALPARAMETER.paymentTerms}
              foreignKeyField="id"
              foreignKeyValue="value"
              edit={{ params: { popupWidth: '280px' } }}
            />
            <ColumnDirective
              field="scMaxRetentionPercent"
              headerText="SC Max Retention %"
              format="n2"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="scInterimRetentionPercent"
              headerText="SC MOS Retention %"
              format="n2"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="scMOSRetentionPercent"
              headerText="SC MOS Retention %"
              format="n2"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="retentionType"
              headerText="Retention Type"
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
              Sort,
              Edit,
              ForeignKey
            ]}
          />
        </GridComponent>
      </div>
    </div>
  )
}

export default StandardTermsMaintenance
