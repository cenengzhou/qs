/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from 'react'

import {
  ButtonComponent,
  ChangeEventArgs as SwitchChangeEventArgs,
  SwitchComponent
} from '@syncfusion/ej2-react-buttons'
import { ChangedEventArgs as CalendarsChangedEventArgs } from '@syncfusion/ej2-react-calendars'
import {
  ChangeEventArgs,
  DropDownListComponent
} from '@syncfusion/ej2-react-dropdowns'
import { InputEventArgs, TextBoxComponent } from '@syncfusion/ej2-react-inputs'
import {
  BreadcrumbBeforeItemRenderEventArgs,
  BreadcrumbComponent
} from '@syncfusion/ej2-react-navigations'

import DatePicker from '../../components/DatePicker'
import {
  useGetCompanyCodeAndNameQuery,
  useGetDivisionsQuery
} from '../../services'
import reportData2, {
  ParametersForSearch,
  ReportData,
  SearchFields
} from './data'
import './style.css'
import dayjs from 'dayjs'

const beforeItemRenderHandler = (
  args: BreadcrumbBeforeItemRenderEventArgs
): void => {
  if (
    args.item.text === 'pcms' ||
    args.item.text === 'web' ||
    args.item.text === 'reports'
  ) {
    args.cancel = true
  }
  if (args.item.iconCss === 'e-icons e-home') {
    args.item.url = '/pcms/web/Reports'
  }
}
const Reports = () => {
  const [reportDatas, setReportData] = useState<ReportData[]>(reportData2)

  const { data: companyCodeAndName } = useGetCompanyCodeAndNameQuery()
  const companies = Object.values(companyCodeAndName ?? {}).map(
    (item, index) => {
      return {
        value: index,
        text: item.companyCode + '-' + item.companyName
      }
    }
  )
  const changeCompany = (reportData: ReportData, value: ChangeEventArgs) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'company') {
        field.value = String(value.value)
        setReportData([...reportDatas])
      }
    })
  }

  const { data: divisions } = useGetDivisionsQuery()
  const division = Object.values(divisions ?? {}).map((item, index) => {
    return {
      value: index,
      text: item
    }
  })
  const changeDivision = (reportData: ReportData, e: ChangeEventArgs) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'division') {
        field.value = e.value as string
        setReportData([...reportDatas])
      }
    })
  }

  const changeDueDate = (
    reportData: ReportData,
    e: CalendarsChangedEventArgs
  ) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'dueDate') {
        field.value = dayjs(e.value)?.format('YYYY-MM-DD')
        setReportData([...reportDatas])
      }
    })
  }

  const changeJobNumber = (reportData: ReportData, value?: string) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'jobNumber') {
        field.value = value
        setReportData([...reportDatas])
      }
    })
  }

  const changeSubcontractNo = (reportData: ReportData, value?: string) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'subcontractNumber') {
        field.value = value
        setReportData([...reportDatas])
      }
    })
  }

  const changeSubcontractorNo = (reportData: ReportData, value?: string) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'subcontractorNumber') {
        field.value = value
        setReportData([...reportDatas])
      }
    })
  }

  const changeDueDateType = (
    reportData: ReportData,
    value: SwitchChangeEventArgs
  ) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'dueDateType') {
        field.value = String(value.checked)
        setReportData([...reportDatas])
      }
    })
  }

  const changePeriodsType = (
    reportData: ReportData,
    value: SwitchChangeEventArgs
  ) => {
    reportData.showPeriod = value.checked as boolean
    setReportData([...reportDatas])
  }

  const changePeriod = (
    reportData: ReportData,
    e: CalendarsChangedEventArgs
  ) => {
    reportData.searchFields?.map(field => {
      if (field.field === 'periods') {
        field.value = dayjs(e.value)?.format('YYYY-MM')
        setReportData([...reportDatas])
      }
    })
  }

  function exportPdfFile(paramsData: ReportData, downloadType: string) {
    const parameters: ParametersForSearch = {
      company: '',
      jobNumber: '',
      dueDateType: undefined,
      dueDate: '',
      division: '',
      subcontractNumber: '',
      subcontractorNumber: '',
      month: '',
      year: '',
      subcontractorNature: '',
      paymentType: '',
      workScope: '',
      clientNo: '',
      includeJobCompletionDate: '',
      splitTerminateStatus: ''
    }
    paramsData.searchFields?.map(field => {
      setParameters(paramsData, field, parameters)
    })

    setDownloadFileUrl(paramsData, downloadType, parameters, getDownloadFile)
  }

  function exportExcelFile(paramsData: ReportData, downloadType: string) {
    const parameters: ParametersForSearch = {
      company: '',
      jobNumber: '',
      dueDateType: undefined,
      dueDate: '',
      division: '',
      subcontractNumber: '',
      subcontractorNumber: '',
      month: '',
      year: '',
      subcontractorNature: '',
      paymentType: '',
      workScope: '',
      clientNo: '',
      includeJobCompletionDate: '',
      splitTerminateStatus: ''
    }
    paramsData.searchFields?.map(field => {
      setParameters(paramsData, field, parameters)
    })

    setDownloadFileUrl(paramsData, downloadType, parameters, getDownloadFile)
  }

  const getDownloadFile = async (url: string) => {
    const a = document.createElement('a')
    a.href = url
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
  }

  function setDownloadFileUrl(
    paramsData: ReportData,
    downloadType: string,
    parameters: ParametersForSearch,
    getDownloadFile: (url: string) => Promise<void>
  ) {
    paramsData.reportUrls?.map(item => {
      if (item.type === downloadType) {
        let url = 'http://localhost:3000/pcms/service/report/' + item.url + '?'
        if (item.parameters?.includes('company')) {
          url += `company=${parameters.company}`
        }
        if (item.parameters?.includes('jobNumber')) {
          url += `&jobNumber=${parameters.jobNumber}`
        }
        if (item.parameters?.includes('dueDateType')) {
          url += `&dueDateType=${parameters.dueDateType}`
        }
        if (item.parameters?.includes('dueDate')) {
          url += `&dueDate=${parameters.dueDate}`
        }
        if (item.parameters?.includes('division')) {
          url += `&division=${parameters.division}`
        }
        if (item.parameters?.includes('subcontractNumber')) {
          url += `&subcontractNumber=${parameters.subcontractNumber}`
        }
        if (item.parameters?.includes('subcontractorNumber')) {
          url += `&subcontractorNumber=${parameters.subcontractorNumber}`
        }
        if (item.parameters?.includes('subcontractorNature')) {
          url += `&subcontractorNature=${parameters.subcontractorNature}`
        }
        if (item.parameters?.includes('month')) {
          url += `&month=${parameters.month}`
        }
        if (item.parameters?.includes('year')) {
          url += `&year=${parameters.year}`
        }
        if (item.parameters?.includes('paymentType')) {
          url += `&paymentType=${parameters.paymentType}`
        }
        if (item.parameters?.includes('workScope')) {
          url += `&workScope=${parameters.workScope}`
        }
        if (item.parameters?.includes('clientNo')) {
          url += `&clientNo=${parameters.clientNo}`
        }
        if (item.parameters?.includes('includeJobCompletionDate')) {
          url += `&includeJobCompletionDate=${parameters.includeJobCompletionDate}`
        }
        if (item.parameters?.includes('splitTerminateStatus')) {
          url += `&splitTerminateStatus=${parameters.splitTerminateStatus}`
        }
        getDownloadFile(url)
      }
    })
  }

  function setParameters(
    reportData: ReportData,
    field: SearchFields,
    parameters: ParametersForSearch
  ) {
    if (field.inputType === 'company') {
      parameters.company = field.value?.split('-')[0]
    }
    if (field.inputType === 'jobNumber') {
      parameters.jobNumber = field.value === '' ? '90013' : field.value
    }
    if (field.inputType === 'division') {
      parameters.division = field.value ?? ''
    }
    if (field.inputType === 'subcontractNumber') {
      parameters.subcontractNumber = field.value ?? ''
    }
    if (field.inputType === 'subcontractorNumber') {
      parameters.subcontractorNumber = field.value ?? ''
    }
    if (field.inputType === 'dueDateType') {
      parameters.dueDateType = field.value === 'true' ? true : false
    }
    if (field.inputType === 'dueDate') {
      parameters.dueDate =
        field.value === ''
          ? dayjs(new Date()).format('DD/MM/YYYY')
          : dayjs(new Date(field.value as string)).format('DD/MM/YYYY')
    }
    if (field.inputType === 'periods' && !reportData.showPeriod) {
      parameters.month =
        field.value === ''
          ? dayjs(new Date()).format('MM')
          : dayjs(new Date(field.value as string)).format('MM')
      parameters.year =
        field.value === ''
          ? dayjs(new Date()).format('YYYY')
          : dayjs(new Date(field.value as string)).format('YYYY')
    }
  }

  return (
    <div className="control-section">
      <div className="listView">
        <BreadcrumbComponent
          cssClass="breadcrumb"
          beforeItemRender={beforeItemRenderHandler}
        />
        <div className="reports-container">
          <div className="row card-layout manual-procedures">
            {reportDatas.map((item: ReportData, index: number) => (
              <div key={item.id} className="col-sm-12 col-md-4 py-2">
                <div className="e-card">
                  <div className="e-card-header card-info">
                    <div>
                      <span className="e-icons e-page-columns card-header-icon"></span>
                    </div>
                    <div className="e-card-header-caption flex-row">
                      <div className="e-card-header-title">{item.name}</div>
                      <div className="px-2">
                        {item.comingSoon === 'YES' && (
                          <span className="bg-lime-light">
                            &nbsp;Coming soon!
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                  <div className="e-card-content card-content-flex">
                    {reportDatas[index].searchFields?.map(
                      (searchField: SearchFields) => {
                        return (
                          <div
                            key={`${searchField.field}: ${index}`}
                            className="row"
                          >
                            {/* {searchField.inputType} */}
                            {searchField.inputType === 'company' && (
                              <DropDownListComponent
                                dataSource={companies}
                                cssClass="e-outline"
                                floatLabelType="Always"
                                showClearButton
                                placeholder="Company"
                                text={companies[0]?.text}
                                change={(value: ChangeEventArgs) => {
                                  changeCompany(item, value)
                                }}
                              />
                            )}
                            {searchField.inputType === 'division' && (
                              <DropDownListComponent
                                dataSource={divisions}
                                cssClass="e-outline"
                                floatLabelType="Always"
                                showClearButton
                                placeholder="Division"
                                text={division[0]?.text}
                                change={(value: ChangeEventArgs) =>
                                  changeDivision(item, value)
                                }
                              />
                            )}
                            {searchField.inputType === 'jobNumber' && (
                              <>
                                <TextBoxComponent
                                  placeholder="Job Number"
                                  floatLabelType="Auto"
                                  cssClass="e-outline"
                                  value={
                                    searchField.value === ''
                                      ? '90013'
                                      : searchField.value
                                  }
                                  input={(value: InputEventArgs) => {
                                    changeJobNumber(item, value.value)
                                  }}
                                />
                                <p className="mt-0 px-3 p-text-color">
                                  Enter <span className="text-color">*</span>
                                  &nbsp;to for search all jobs
                                </p>
                              </>
                            )}
                            {searchField.inputType === 'dueDateType' && (
                              <>
                                <label
                                  htmlFor="Due Date Type"
                                  className="px-3 pb-3"
                                >
                                  Due Date Type
                                </label>
                                <SwitchComponent
                                  checked={
                                    searchField.value === 'true' ? true : false
                                  }
                                  onLabel={'On or Before'}
                                  offLabel={'Exact Date'}
                                  change={(value: SwitchChangeEventArgs) =>
                                    changeDueDateType(item, value)
                                  }
                                ></SwitchComponent>
                              </>
                            )}
                            {searchField.inputType === 'dueDate' && (
                              <DatePicker
                                placeholder="Due Date"
                                value={
                                  searchField.value === ''
                                    ? dayjs(new Date()).format('YYYY-MM-DD')
                                    : searchField.value
                                }
                                onChange={(value: CalendarsChangedEventArgs) =>
                                  changeDueDate(item, value)
                                }
                              />
                            )}
                            {searchField.inputType === 'subcontractNumber' && (
                              <TextBoxComponent
                                placeholder="Subcontract No"
                                floatLabelType="Auto"
                                cssClass="e-outline"
                                value={searchField.value}
                                input={(value: InputEventArgs) =>
                                  changeSubcontractNo(item, value.value)
                                }
                              />
                            )}
                            {searchField.inputType ===
                              'subcontractorNumber' && (
                              <TextBoxComponent
                                placeholder="Subcontractor No"
                                floatLabelType="Auto"
                                cssClass="e-outline"
                                value={searchField.value}
                                input={(value: InputEventArgs) =>
                                  changeSubcontractorNo(item, value.value)
                                }
                              />
                            )}
                            {searchField.inputType === 'periods' && (
                              <>
                                <label htmlFor="Periods" className="px-3 pb-3">
                                  Periods
                                </label>
                                <SwitchComponent
                                  checked={item.showPeriod}
                                  onLabel={'As of Today'}
                                  offLabel={'Month End History'}
                                  change={(value: SwitchChangeEventArgs) =>
                                    changePeriodsType(item, value)
                                  }
                                ></SwitchComponent>
                              </>
                            )}
                            {!item.showPeriod &&
                              searchField.inputType === 'periods' && (
                                <div className="px-0 pt-4">
                                  <DatePicker
                                    format="yyyy-MM"
                                    start="Year"
                                    depth="Year"
                                    placeholder="Period"
                                    value={
                                      searchField.value === ''
                                        ? dayjs(new Date()).format('YYYY-MM')
                                        : searchField.value
                                    }
                                    onChange={(
                                      value: CalendarsChangedEventArgs
                                    ) => changePeriod(item, value)}
                                  />
                                </div>
                              )}
                          </div>
                        )
                      }
                    )}

                    <p className="my-0 px-3 p-text-color">
                      Available to download in
                    </p>
                    {item.comingSoon === 'NO' && (
                      <>
                        <div className="row">
                          <ButtonComponent
                            iconCss="e-icons e-export-excel"
                            cssClass="e-success full-btn e-excel-btn"
                            onClick={() => exportExcelFile(item, 'xls')}
                            disabled={false}
                          >
                            Excel
                          </ButtonComponent>
                        </div>
                        <div className="row">
                          <ButtonComponent
                            iconCss="e-icons e-export-pdf"
                            cssClass="e-danger full-btn e-pdf-btn"
                            onClick={() => exportPdfFile(item, 'pdf')}
                            disabled={false}
                          >
                            PDF
                          </ButtonComponent>
                        </div>
                      </>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
export default Reports
