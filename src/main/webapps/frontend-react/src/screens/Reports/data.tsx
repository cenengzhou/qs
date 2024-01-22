const paymentCertParameters = ['dueDateType', 'dueDate', 'company', 'jobNumber']
const commonParameters = [
  'company',
  'division',
  'jobNumber',
  'subcontractNumber',
  'subcontractorNumber',
  'subcontractorNature',
  'paymentType',
  'workScope',
  'clientNo',
  'includeJobCompletionDate',
  'splitTerminateStatus',
  'month',
  'year'
]
export type ReportData = {
  id?: number
  name?: string
  comingSoon?: string
  reportUrls?: ReportUrls[]
  showPeriod?: boolean
  searchFields?: SearchFields[]
}

export type ReportUrls = {
  type?: string
  url?: string
  parameters?: string[]
}

export type SearchFields = {
  field?: string
  value?: string
  inputType?: string
}

export type ParametersForSearch = {
  company?: string
  jobNumber?: string
  dueDateType?: boolean
  dueDate?: string
  division?: string
  subcontractNumber?: string
  subcontractorNumber?: string
  month?: string
  year?: string
  subcontractorNature?: string
  paymentType?: string
  workScope?: string
  clientNo?: string
  includeJobCompletionDate?: string
  splitTerminateStatus?: string
}

const reportDatas: ReportData[] = [
  {
    id: 0,
    name: 'paymentCertificateReport',
    comingSoon: 'NO',
    reportUrls: [
      {
        type: 'xls',
        url: 'paymentCertificateEnquiryExcelExport',
        parameters: paymentCertParameters
      },
      {
        type: 'pdf',
        url: 'printPaymentCertificateReportPdf',
        parameters: paymentCertParameters
      }
    ],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' },
      { field: 'dueDateType', value: 'true', inputType: 'dueDateType' },
      { field: 'dueDate', value: '', inputType: 'dueDate' }
    ]
  },
  {
    id: 1,
    name: 'subcontractReport',
    comingSoon: 'NO',
    reportUrls: [
      {
        type: 'xls',
        url: 'financeSubcontractListDownload',
        parameters: commonParameters
      },
      {
        type: 'pdf',
        url: 'subcontractReportExport',
        parameters: commonParameters
      }
    ],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' },
      { field: 'subcontractNumber', value: '', inputType: 'subcontractNumber' },
      {
        field: 'subcontractorNumber',
        value: '',
        inputType: 'subcontractorNumber'
      }
    ]
  },
  {
    id: 2,
    name: 'subcontractLiabilityReport',
    comingSoon: 'NO',
    reportUrls: [
      {
        type: 'xls',
        url: 'subcontractLiabilityExcelExport',
        parameters: commonParameters
      },
      {
        type: 'pdf',
        url: 'subcontractLiabilityReportExport',
        parameters: commonParameters
      }
    ],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' },
      { field: 'subcontractNumber', value: '', inputType: 'subcontractNumber' },
      {
        field: 'subcontractorNumber',
        value: '',
        inputType: 'subcontractorNumber'
      },
      { field: 'periods', value: '', inputType: 'periods' }
    ]
  },
  {
    id: 3,
    name: 'subcontractorAnalysisReport',
    comingSoon: 'NO',
    reportUrls: [
      {
        type: 'xls',
        url: 'subcontractorAnalysisExcelExport',
        parameters: commonParameters
      },
      {
        type: 'pdf',
        url: 'subcontractorAnalysisReportExport',
        parameters: commonParameters
      }
    ],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' },
      { field: 'subcontractNumber', value: '', inputType: 'subcontractNumber' },
      {
        field: 'subcontractorNumber',
        value: '',
        inputType: 'subcontractorNumber'
      },
      { field: 'periods', value: '', inputType: 'periods' }
    ]
  },
  {
    id: 4,
    name: 'monthlyContractExpenditureReport',
    comingSoon: 'YES',
    reportUrls: [],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' },
      { field: 'dueDateType', value: 'true', inputType: 'dueDateType' },
      { field: 'dueDate', value: '', inputType: 'dueDate' }
    ]
  },
  {
    id: 5,
    name: 'contractFinancialPerformanceReport',
    comingSoon: 'YES',
    reportUrls: [],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' }
    ]
  },
  {
    id: 6,
    name: 'jobCostReport',
    comingSoon: 'YES',
    reportUrls: [],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' }
    ]
  },
  {
    id: 7,
    name: 'cashFlowReport',
    comingSoon: 'YES',
    reportUrls: [],
    showPeriod: true,
    searchFields: [
      { field: 'company', value: '', inputType: 'company' },
      { field: 'division', value: '', inputType: 'division' },
      { field: 'jobNumber', value: '', inputType: 'jobNumber' }
    ]
  }
]

export default reportDatas
