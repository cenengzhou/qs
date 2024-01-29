/* eslint-disable @typescript-eslint/naming-convention */
import {
  BaseQueryFn,
  FetchArgs,
  createApi,
  fetchBaseQuery
} from '@reduxjs/toolkit/query/react'

import { GLOBALPARAMETER } from '../constants/global'

export interface CustomError {
  data: {
    status: string
    message: string
  }
  status: number
}

const apiSlice = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: '/pcms'
  }) as BaseQueryFn<string | FetchArgs, unknown, CustomError, object>,
  tagTypes: ['updateStatus'],
  endpoints: builder => {
    return {
      validateCurrentSession: builder.query<boolean, void>({
        query: () => ({
          method: 'POST',
          url: 'service/system/ValidateCurrentSession'
        })
      }),
      obtainUserPreferenceByCurrentUser: builder.query<
        UserPreferenceResponse,
        void
      >({
        query: () => ({
          method: 'POST',
          url: 'service/userPreference/obtainUserPreferenceByCurrentUser'
        })
      }),
      getCurrentUser: builder.query<CurrentUser, void>({
        query: () => ({
          url: 'service/security/getCurrentUser'
        })
      }),
      getSessionList: builder.query<SessionListDetail[], void>({
        query: () => ({
          method: 'POST',
          url: 'service/system/GetSessionList'
        })
      }),
      getAuditTableMap: builder.query<
        Map<string, ProceduresAuditTableItemMap>,
        void
      >({
        query: () => ({
          method: 'POST',
          url: 'service/system/getAuditTableMap'
        })
      }),
      getCutoffPeriod: builder.query<ProceduresCutoffPeriod, void>({
        query: () => ({
          method: 'GET',
          url: 'service/roc/getCutoffPeriod'
        })
      }),
      obtainCacheKey: builder.query<string, string>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/system/obtainCacheKey',
          body: queryArg
        })
      }),
      getNotificationReadStatusByCurrentUser: builder.query<
        NotificationReadStatus,
        void
      >({
        query: () => ({
          url: 'service/userPreference/getNotificationReadStatusByCurrentUser'
        }),
        providesTags: ['updateStatus']
      }),
      updateNotificationReadStatusByCurrentUser: builder.mutation<
        NotificationReadStatus,
        string
      >({
        query: queryArg => ({
          method: 'POST',
          url: 'service/userPreference/updateNotificationReadStatusByCurrentUser',
          body: queryArg
        }),
        invalidatesTags: ['updateStatus']
      }),
      getProperties: builder.query<PropertiesResponse, void>({
        query: () => ({
          method: 'POST',
          url: 'service/system/getProperties'
        })
      }),
      getJob: builder.query<JobInfo, JobNo>({
        query: queryArg => ({
          url: 'service/job/getJob',
          params: queryArg
        })
      }),
      getJobList: builder.mutation<JobListResponse, boolean>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/job/getJobList',
          body: queryArg,
          headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
      }),
      getJobDates: builder.query<JobDatesResponse, JobNo>({
        query: queryArg => ({
          url: 'service/job/getJobDates',
          params: queryArg
        })
      }),
      getRepackaging: builder.query<RepackagingResponse, RepackagingRequest>({
        query: queryArg => ({
          url: 'service/repackaging/getRepackaging',
          params: queryArg
        })
      }),
      getJobDashboardData: builder.query<
        JobDashboardDataResponse,
        JobDashboardDataRequest
      >({
        query: queryArg => ({
          url: 'service/adl/getJobDashboardData',
          params: queryArg
        })
      }),
      getResourceSummariesGroupByObjectCode: builder.query<
        ResourceSummariesResponse,
        JobNo
      >({
        query: queryArg => ({
          url: 'service/resourceSummary/getResourceSummariesGroupByObjectCode',
          params: queryArg
        })
      }),
      getAllWorkScopes: builder.query<AllWorkScopesResponseTrans, void>({
        query: () => ({
          url: 'service/adl/getAllWorkScopes'
        }),
        transformResponse: (
          response: AllWorkScopesResponse
        ): AllWorkScopesResponseTrans => {
          const data = response.map((item: WorkScopes) => {
            return {
              codeWorkscope: Number(item.codeWorkscope?.trim()),
              workscopeDescription: item.workscopeDescription?.trim()
            }
          })
          return data
        }
      }),
      getSubcontract: builder.mutation<Subcontract, SCDetailRequest>({
        query: ({ jobNo, subcontractNo }) => ({
          url: `service/subcontract/getSubcontract?jobNo=${jobNo}&subcontractNo=${subcontractNo}`
        })
      }),
      getSCDetails: builder.mutation<SCDetail[], SCDetailRequest>({
        query: ({ jobNo, subcontractNo }) => ({
          url: `service/subcontract/getSCDetails?jobNo=${jobNo}&subcontractNo=${subcontractNo}`,
          method: 'GET'
        })
      }),
      housekeepAuditTable: builder.mutation<number, string>({
        query: queryArg => ({
          url: `service/system/housekeepAuditTable?tableName=${queryArg}`,
          method: 'POST'
        })
      }),
      runProvisionPostingManually: builder.mutation<
        void,
        {
          glDate?: string | undefined
          jobNumber?: string | undefined
        }
      >({
        query: ({ glDate, jobNumber }) => ({
          url: `service/subcontract/runProvisionPostingManually?glDate=${glDate}&jobNumber=${jobNumber}`,
          method: 'POST'
        })
      }),
      updateCEOApproval: builder.mutation<
        void,
        {
          jobNumber?: string | undefined
          packageNo?: string | undefined
        }
      >({
        query: ({ jobNumber, packageNo }) => ({
          url: `service/subcontract/updateCEDApprovalManually?jobNo=${jobNumber}&packageNo=${packageNo}`,
          method: 'POST'
        })
      }),
      generateSCPackageSnapshotManually: builder.mutation<void, void>({
        query: () => ({
          url: 'service/subcontract/generateSCPackageSnapshotManually',
          method: 'POST'
        })
      }),
      runPaymentPosting: builder.mutation<void, void>({
        query: () => ({
          url: 'service/payment/runPaymentPosting',
          method: 'POST'
        })
      }),
      updateF58001FromSCPackageManually: builder.mutation<void, void>({
        query: () => ({
          url: 'service/subcontract/updateF58001FromSCPackageManually',
          method: 'POST'
        })
      }),
      updateF58011FromSCPaymentCertManually: builder.mutation<void, void>({
        query: () => ({
          url: 'service/payment/updateF58011FromSCPaymentCertManually',
          method: 'POST'
        })
      }),
      updateMainCertFromF03B14Manually: builder.mutation<boolean, void>({
        query: () => ({
          url: 'service/mainCert/updateMainCertFromF03B14Manually',
          method: 'POST'
        })
      }),
      generatePaymentPDFAdmin: builder.mutation<
        string,
        {
          jobNo?: string | undefined
          packageNo?: string | undefined
          paymentNo?: string | undefined
        }
      >({
        query: ({ jobNo, packageNo, paymentNo }) => ({
          url: `service/payment/generatePaymentPDFAdmin?jobNo=${jobNo}&packageNo=${packageNo}&paymentNo=${paymentNo}`,
          method: 'POST'
        })
      }),
      updateSubcontractAdmin: builder.mutation<void, Subcontract>({
        query: queryArg => ({
          url: 'service/subcontract/updateSubcontractAdmin',
          method: 'POST',
          body: queryArg
        })
      }),
      getPaymentCert: builder.mutation<
        Payment,
        {
          jobNo?: string
          subcontractNo?: number
          paymentCertNo?: number
        }
      >({
        query: ({ jobNo, subcontractNo, paymentCertNo }) => ({
          url: `service/payment/getPaymentCert?jobNo=${jobNo}&paymentCertNo=${paymentCertNo}&subcontractNo=${subcontractNo}`,
          method: 'GET'
        })
      }),
      updateSubcontractDetailListAdmin: builder.mutation<string, SCDetail[]>({
        query: queryArg => ({
          url: 'service/subcontract/updateSubcontractDetailListAdmin',
          method: 'POST',
          body: queryArg
        })
      }),
      deletePendingPaymentAndDetails: builder.mutation<void, number>({
        query: (paymentCertId: number) => ({
          url: `service/payment/deletePendingPaymentAndDetails?paymentCertId=${paymentCertId}`,
          method: 'POST'
        })
      }),
      updatePaymentCert: builder.mutation<void, Payment>({
        query: queryArg => ({
          url: 'service/payment/updatePaymentCert',
          method: 'POST',
          body: queryArg
        })
      }),
      getAddendum: builder.mutation<AddendumResponse, AddendumRequest>({
        query: queryArg => ({
          url: 'service/addendum/getAddendum',
          params: queryArg
        })
      }),
      updateAddendumAdmin: builder.mutation<string, AddendumResponse>({
        query: queryArg => ({
          url: 'service/addendum/updateAddendumAdmin',
          method: 'POST',
          body: queryArg
        })
      }),
      getFinalAccountAdmin: builder.mutation<
        FinalAccountResponse,
        AddendumRequest
      >({
        query: queryArg => ({
          url: `service/finalAccount/getFinalAccountAdmin/${queryArg.jobNo}/${queryArg.subcontractNo}/${queryArg.addendumNo}`
        })
      }),
      updateFinalAccountAdmin: builder.mutation<string, FinalAccountRequest>({
        query: queryArg => ({
          url: 'service/finalAccount/updateFinalAccountAdmin',
          method: 'POST',
          params: {
            jobNo: queryArg.data.jobNo,
            subcontractNo: queryArg.subcontractNo,
            addendumNo: queryArg.data.addendumNo
          },
          body: queryArg.data
        })
      }),
      deleteFinalAccountAdmin: builder.mutation<string, FinalAccountRequest>({
        query: queryArg => ({
          url: 'service/finalAccount/deleteFinalAccountAdmin',
          method: 'POST',
          params: {
            jobNo: queryArg.data.jobNo,
            subcontractNo: queryArg.subcontractNo,
            addendumNo: queryArg.data.addendumNo
          },
          body: queryArg.data
        })
      }),
      findByUsernameIsNotNull: builder.query<UserInfo[], void>({
        query: () => ({
          url: 'service/hr/findByUsernameIsNotNull'
        })
      }),
      getMemo: builder.mutation<MemoResponse, SubcontractResquest>({
        query: queryArg => ({
          url: 'service/consultancyAgreement/getMemo',
          params: queryArg
        })
      }),
      updateConsultancyAgreementAdmin: builder.mutation<
        string,
        ConsultancyAgreementRequest
      >({
        query: queryArg => ({
          url: 'service/consultancyAgreement/updateConsultancyAgreementAdmin',
          method: 'POST',
          params: {
            jobNo: queryArg.jobNo,
            subcontractNo: queryArg.subcontractNo
          },
          body: queryArg.data
        })
      }),
      getAllAddendumDetails: builder.mutation<
        ADDENDUM[],
        { addendumNo?: number; jobNo?: string; subcontractNo?: number }
      >({
        query: queryArg => ({
          url: 'service/addendum/getAllAddendumDetails',
          params: queryArg,
          method: 'GET'
        })
      }),
      updateAddendumDetailListAdmin: builder.mutation<string, ADDENDUM[]>({
        query: queryArg => ({
          url: 'service/addendum/updateAddendumDetailListAdmin',
          body: queryArg,
          method: 'POST'
        })
      }),
      obtainResourceSummariesByJobNumberForAdmin: builder.mutation<
        Repackaging[],
        { jobNumber?: string }
      >({
        query: queryArg => ({
          url: 'service/resourceSummary/obtainResourceSummariesByJobNumberForAdmin',
          params: queryArg,
          method: 'GET'
        }),
        transformResponse: (response: Repackaging[]): Repackaging[] => {
          const data = response.map((item: Repackaging) => {
            const { excludeDefect, forIvRollbackOnly, excludeLevy } = item
            return {
              ...item,
              excludeDefect: String(excludeDefect),
              forIvRollbackOnly: String(forIvRollbackOnly),
              excludeLevy: String(excludeLevy)
            }
          })
          return data
        }
      }),
      updateResourceSummariesForAdmin: builder.mutation<
        string,
        { body: ADDENDUM[]; jobNo: string }
      >({
        query: queryArg => ({
          url: `service/resourceSummary/updateResourceSummariesForAdmin?jobNo=${queryArg.jobNo}`,
          body: queryArg.body,
          method: 'POST'
        })
      }),
      obtainAttachmentList: builder.mutation<
        Attachment[],
        { nameObject?: string; textKey?: string }
      >({
        query: queryArg => ({
          url: 'service/attachment/obtainAttachmentList',
          params: queryArg,
          method: 'POST'
        })
      }),
      getLatestRepackaging: builder.mutation<
        LatestRepackaging,
        { jobNo?: string }
      >({
        query: queryArg => ({
          url: 'service/repackaging/getLatestRepackaging',
          params: queryArg,
          method: 'GET'
        })
      }),
      deleteAttachmentAdmin: builder.mutation<
        boolean,
        { nameObject?: string; sequenceNumber?: number; textKey?: string }
      >({
        query: queryArg => ({
          url: 'service/attachment/deleteAttachmentAdmin',
          params: queryArg,
          method: 'POST'
        })
      }),
      getTenderDetailList: builder.mutation<
        TenderDetailList[],
        { jobNo?: string; subcontractNo?: number; subcontractorNo?: number }
      >({
        query: queryArg => ({
          url: 'service/tender/getTenderDetailList',
          params: queryArg,
          method: 'GET'
        })
      }),
      updateTenderDetailListAdmin: builder.mutation<string, TenderDetailList[]>(
        {
          query: queryArg => ({
            url: 'service/tender/updateTenderDetailListAdmin',
            body: queryArg,
            method: 'POST'
          })
        }
      ),
      getRocClassDescMap: builder.query<string, void>({
        query: () => ({
          url: 'service/roc/getRocClassDescMap',
          method: 'GET'
        }),
        transformResponse: (response: RocClassDescMap[]): string => {
          const data: Array<string> = [' ']
          response.forEach(item => {
            data.push(item.classification)
          })
          return data.join(',')
        }
      }),
      getRocCategoryList: builder.query<string, void>({
        query: () => ({
          url: 'service/roc/getRocCategoryList',
          method: 'GET'
        }),
        transformResponse: (response: Array<string>): string => {
          response.unshift(' ')
          return response.join(',')
        }
      }),
      getImpactList: builder.query<string, void>({
        query: () => ({
          url: 'service/roc/getImpactList',
          method: 'GET'
        }),
        transformResponse: (response: Array<string>): string => {
          response.unshift(' ')
          return response.join(',')
        }
      }),
      getRocAdmin: builder.mutation<
        RocAdmin[],
        { jobNo: string; itemNo?: string }
      >({
        query: queryArg => ({
          url: queryArg.itemNo
            ? `service/roc/getRocAdmin/${queryArg.jobNo}?itemNo=${queryArg.itemNo}`
            : `service/roc/getRocAdmin/${queryArg.jobNo}`,
          method: 'GET'
        })
      }),
      updateRocAdmin: builder.mutation<
        string,
        { body: RocAdmin[]; jobNo: string }
      >({
        query: queryArg => ({
          url: `service/roc/updateRocAdmin?jobNo=${queryArg.jobNo}`,
          body: queryArg.body,
          method: 'POST'
        })
      }),
      getRocDetailListAdmin: builder.mutation<
        RocDetail[],
        { jobNo: string; itemNo?: string; period: string }
      >({
        query: queryArg => ({
          url: queryArg.itemNo
            ? `service/roc/getRocDetailListAdmin/${queryArg.jobNo}?itemNo=${queryArg.itemNo}&period=${queryArg.period}`
            : `service/roc/getRocDetailListAdmin/${queryArg.jobNo}?period=${queryArg.period}`,
          method: 'GET'
        })
      }),
      updateRocDetailListAdmin: builder.mutation<
        string,
        { body: RocDetail[]; jobNo: string }
      >({
        query: queryArg => ({
          url: `service/roc/updateRocDetailListAdmin?jobNo=${queryArg.jobNo}`,
          method: 'POST',
          body: queryArg.body
        })
      }),
      getRocSubdetailListAdmin: builder.mutation<
        RocSubDetail[],
        { jobNo: string; itemNo?: string; period: string }
      >({
        query: queryArg => ({
          url: queryArg.itemNo
            ? `service/roc/getRocSubdetailListAdmin/${queryArg.jobNo}?itemNo=${queryArg.itemNo}&period=${queryArg.period}`
            : `service/roc/getRocSubdetailListAdmin/${queryArg.jobNo}?period=${queryArg.period}`,
          method: 'GET'
        })
      }),
      updateRocSubdetailListAdmin: builder.mutation<
        string,
        { body: RocSubDetail[]; jobNo: string }
      >({
        query: queryArg => ({
          url: `service/roc/updateRocSubdetailListAdmin?jobNo=${queryArg.jobNo}`,
          method: 'POST',
          body: queryArg.body
        })
      }),
      getCertificate: builder.mutation<
        Certificate,
        { jobNo?: string; certificateNumber?: number }
      >({
        query: queryArg => ({
          url: 'service/mainCert/getCertificate',
          method: 'GET',
          params: queryArg
        })
      }),
      updateCertificateByAdmin: builder.mutation<string, Certificate>({
        query: queryArg => ({
          url: 'service/mainCert/updateCertificateByAdmin',
          method: 'POST',
          body: queryArg
        })
      }),
      getTender: builder.mutation<
        Certificate,
        { jobNo?: string; subcontractNo?: number; subcontractorNo?: number }
      >({
        query: queryArg => ({
          url: 'service/tender/getTender',
          method: 'GET',
          params: queryArg
        })
      }),
      updateTenderAdmin: builder.mutation<string, Tender>({
        query: queryArg => ({
          url: 'service/tender/updateTenderAdmin',
          method: 'POST',
          body: queryArg
        })
      }),
      unlockTransitAdmin: builder.mutation<void, { jobNumber: string }>({
        query: queryArg => ({
          url: `service/transit/unlockTransitAdmin`,
          method: 'POST',
          params: queryArg
        })
      }),
      completeAwardApprovalAdmin: builder.mutation<
        { completed: boolean },
        { jobNo?: string; packageNo?: string; type?: string }
      >({
        query: ({ jobNo, packageNo, type }) => ({
          url: `service/ap/completeAwardApprovalAdmin/${jobNo}/${packageNo}/${type}`,
          method: 'GET'
        })
      }),
      completePaymentApprovalAdmin: builder.mutation<
        { completed: boolean },
        { jobNo?: string; packageNo?: string; type?: string }
      >({
        query: ({ jobNo, packageNo, type }) => ({
          url: `service/ap/completePaymentApprovalAdmin/${jobNo}/${packageNo}/${type}`,
          method: 'GET'
        })
      }),
      completeSplitTerminateApprovalAdmin: builder.mutation<
        { completed: boolean },
        {
          jobNo?: string
          packageNo?: string
          type?: string
          extraType?: string
        }
      >({
        query: ({ jobNo, packageNo, type, extraType }) => ({
          url: `service/ap/completeSplitTerminateApprovalAdmin/${jobNo}/${packageNo}/${type}/${extraType}`,
          method: 'GET'
        })
      }),
      completeAddendumApprovalAdmin: builder.mutation<
        { completed: boolean },
        {
          jobNo?: string
          packageNo?: string
          type?: string
        }
      >({
        query: ({ jobNo, packageNo, type }) => ({
          url: `service/ap/completeAddendumApprovalAdmin/${jobNo}/${packageNo}/undefined/${type}`,
          method: 'GET'
        })
      }),
      completeMainCertApprovalAdmin: builder.mutation<
        { completed: boolean },
        {
          jobNo?: string
          packageNo?: string
          type?: string
        }
      >({
        query: ({ jobNo, packageNo, type }) => ({
          url: `service/ap/completeMainCertApprovalAdmin/${jobNo}/${packageNo}/${type}`,
          method: 'GET'
        })
      }),
      getCompanyCodeAndName: builder.query<
        Map<string, CompanyCodeAndName>,
        void
      >({
        query: () => ({
          method: 'POST',
          url: 'service/adl/obtainCompanyCodeAndName'
        })
      }),
      getDivisions: builder.query<string[], void>({
        query: () => ({
          method: 'GET',
          url: 'service/job/obtainAllJobDivision'
        })
      }),
      downloadFile: builder.mutation<string, string>({
        query: url => ({
          method: 'GET',
          url: url,
          responseHandler(response) {
            return response.text()
          }
        })
      }),
      getSCStandardTermsList: builder.query<ScStandardTerms[], void>({
        query: () => ({
          method: 'POST',
          url: 'service/system/getSCStandardTermsList'
        }),
        transformResponse: (response: ScStandardTerms[]): ScStandardTerms[] => {
          const data = response.map(item => {
            const { scPaymentTerm } = item
            GLOBALPARAMETER.paymentTerms.map(i => {
              if (i.id === scPaymentTerm) {
                item.scPaymentTerm = `${i.id} - ${i.value}`
              }
            })
            return item
          })
          return data
        }
      }),
      updateMultipleSystemConstants: builder.mutation<void, ScStandardTerms[]>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/system/updateMultipleSystemConstants',
          body: queryArg
        })
      }),
      deleteSCStandardTerms: builder.mutation<void, ScStandardTerms[]>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/system/deleteSCStandardTerms',
          body: queryArg
        })
      }),
      createSystemConstant: builder.mutation<void, ScStandardTerms>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/system/createSystemConstant',
          body: queryArg
        })
      }),
      obtainAllJobCompany: builder.query<Array<string>, void>({
        query: () => ({
          method: 'GET',
          url: 'service/job/obtainAllJobCompany'
        })
      }),
      getForecastList: builder.mutation<
        MonthlyMovement[],
        { jobNo?: string; year?: number; month?: number; type?: string }
      >({
        query: ({ jobNo, year, month, type }) => ({
          method: 'GET',
          url: `service/forecast/getForecastList/${jobNo}/${year}/${month}/${type}`
        })
      }),
      updateForecastListAdmin: builder.mutation<string, MonthlyMovement[]>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/forecast/updateForecastListAdmin',
          body: queryArg
        })
      }),
      deleteForecastListAdmin: builder.mutation<
        void,
        { jobNo?: string; id?: number }
      >({
        query: ({ jobNo, id }) => ({
          method: 'DELETE',
          url: `service/forecast/${jobNo}/${id}`
        })
      })
    }
  }
})

export type SystemStatus = 'ACTIVE' | 'INACTIVE'

export type JobNo = {
  jobNo?: string
}

export type Job = JobNo & {
  description?: string
  division?: string
  completionStatus?: string
}

export type JobListResponse = Job[]

export type NotificationReadStatus = 'Y' | 'N' | ''

export type UserPreferenceResponse = {
  GRID_subcontract_details?: string
  GRID_iv_update?: string
  GRID_admin_revisions_subcontract_detail?: string
  GRID_admin_revisions_attachment?: string
  NOTIFICATION_READ_STATUS?: NotificationReadStatus
  GRIDPREFIX?: string
}

export type CurrentUser = {
  EmailAddress?: string
  StaffID?: string
  accountNonExpired?: boolean
  accountNonLocked?: boolean
  authType?: string
  credentialsNonExpired?: boolean
  domainName?: string
  enabled?: boolean
  fullname?: string
  jobNoExcludeList?: string[]
  password?: string
  title?: string
  username?: string
  UserRoles?: Role[]
  authorities?: Role[]
}

export type Role = {
  RoleDescription?: string
  RoleName?: string
  authority?: string
}

export type PropertiesResponse = {
  HELPDESK?: string
  GUIDE_LINES?: string
  deployEnvironment?: string
  OTHER_HOME?: string
  JDE_HOME?: string
  apUrl?: string
  ERP_HOME?: string
  versionDate?: string
  revision?: string
  ANNOUNCEMENT?: string
  FORMS?: string
  UCC_LIST?: string
  gsfUrl?: string
  jdeUrl?: string
  AP_HOME?: string
  BMS_HOME?: string
  wfUrl?: string
}

export type JobDatesResponse = {
  jobNumber?: string
  plannedStartDate?: string
  actualStartDate?: string
  plannedEndDate?: string
  actualEndDate?: string
  anticipatedCompletionDate?: string
  revisedCompletionDate?: string
}

export type CreatedUser = {
  createdUser?: string
  createdDate?: string
  lastModifiedUser?: string
  lastModifiedDate?: string
  systemStatus?: SystemStatus
  id?: number
}

export type RepackagingResponse = CreatedUser & {
  jobInfo?: JobInfo
  repackagingVersion?: number
  createDate?: string
  totalResourceAllowance?: number
  status?: string
  remarks?: string
}

export type JobInfo = CreatedUser & {
  budgetPosted?: string | number
  eotApplied?: number
  eotAwarded?: number
  ldExposureAmount?: number
  residualAfterEot?: number
  nameExecutiveDirector?: string
  nameDirector?: string
  nameDirectSupervisorPic?: string
  nameProjectInCharge?: string
  nameCommercialInCharge?: string
  nameTempWorkController?: string
  nameSafetyEnvRep?: string
  nameAuthorizedPerson?: string
  nameSiteAdmin1?: string
  nameSiteAdmin2?: string
  nameSiteManagement1?: string
  nameSiteManagement2?: string
  nameSiteManagement3?: string
  nameSiteManagement4?: string
  nameSiteSupervision1?: string
  nameSiteSupervision2?: string
  nameSiteSupervision3?: string
  nameSiteSupervision4?: string
  noReference?: number
  statusApproval?: string
  projectFullDescription?: string
  projectLocation?: string
  natureOfWorkBB?: string
  natureOfWorkGammon?: string
  tenderNumber?: string
  immediateParentJobNumber?: string
  ultimateParentJobNumber?: string
  ultimateClient?: string
  immediateClientBusinessSector?: string
  ultimateClientBusinessSector?: string
  clientType?: string
  engineerArchitect?: string
  beam?: string
  beamPlus?: string
  leed?: string
  innovationApplicable?: string
  innovationPercent?: string
  paymentTermFromClient?: string
  provision?: string
  isParentCompanyGuarantee?: string
  jobNo?: string
  description?: string
  company?: string
  employer?: string
  contractType?: string
  division?: string
  department?: string
  internalJob?: string
  soloJV?: string
  completionStatus?: string
  insuranceCAR?: string
  insuranceECI?: string
  insuranceTPL?: string
  clientContractNo?: string
  parentJobNo?: string
  jvPartnerNo?: string
  jvPercentage?: number
  originalContractValue?: number
  projectedContractValue?: number
  orginalNSCContractValue?: number
  tenderGP?: number
  forecastEndYear?: number
  forecastEndPeriod?: number
  maxRetPercent?: number
  interimRetPercent?: number
  mosRetPercent?: number
  valueOfBSWork?: number
  grossFloorArea?: number
  grossFloorAreaUnit?: string
  billingCurrency?: string
  paymentTermForNSC?: string
  defectProvisionPercent?: number
  cpfApplicable?: string
  cpfIndexName?: string
  cpfBaseYear?: string
  cpfBasePeriod?: string
  levyApplicable?: string
  levyCITAPercent?: number
  levyPCFBPercent?: number
  expectedPCCDate?: string
  actualPCCDate?: string
  expectedMakingGoodDate?: string
  actualMakingGoodDate?: string
  defectLiabilityPeriod?: number
  defectListIssuedDate?: string
  financialEndDate?: string
  dateFinalACSettlement?: string
  yearOfCompletion?: number
  bqFinalizedFlag?: string
  manualInputSCWD?: string
  legacyJob?: string
  conversionStatus?: string
  repackagingType?: string
  finQS0Review?: string
}

export type RepackagingRequest = JobNo & {
  version?: number
}

export type JobDashboardDataResponse = JobDashboardData[]

export type JobDashboardDataRequest = {
  noJob?: string
  year?: string
}

export type JobDashboardData = {
  category?: string
  startYear?: string
  endYear?: string
  monthList?: string[]
  detailList?: number[]
}

export type ResourceSummaries = {
  amountBudget?: number
  objectCode?: string
}

export type ResourceSummariesResponse = ResourceSummaries[]

export type WorkScopes = {
  codeWorkscope?: string
  workscopeDescription?: string
}
export type WorkScopesTrans = {
  codeWorkscope?: number
  workscopeDescription?: string
}

export type AllWorkScopesResponseTrans = WorkScopesTrans[]
export type AllWorkScopesResponse = WorkScopes[]

export type SubcontractResquest = JobNo & {
  subcontractNo?: string
}

export type Subcontract = CreatedUser & {
  accumlatedRetention?: number
  amountPackageStretchTarget?: number
  amtCEDApproved?: number
  approvalRoute?: string
  approvedVOAmount?: number
  awarded?: boolean
  balanceToCompleteAmount?: number
  cpfBasePeriod?: number
  cpfBaseYear?: number
  cpfCalculation?: string
  dateScExecutionTarget?: string
  description?: string
  durationFrom?: string
  durationTo?: string
  exchangeRate?: number
  executionMethodMainContract?: string
  executionMethodPropsed?: string
  finalPaymentIssuedDate?: string
  firstPaymentCertIssuedDate?: string
  formOfSubcontract?: string
  interimRentionPercentage?: number
  internalJobNo?: string
  jobInfo?: JobInfo
  labourIncludedContract?: boolean
  lastPaymentCertIssuedDate?: string
  latestAddendumValueUpdatedDate?: string
  loaSignedDate?: string
  materialIncludedContract?: boolean
  maxRetentionPercentage?: number
  mosRetentionPercentage?: number
  nameSubcontractor?: string
  notes?: string
  onSiteStartDate?: string
  originalSubcontractSum?: number
  packageNo?: string
  packageStatus?: string
  packageType?: string
  paymentCurrency?: string
  paymentInformation?: string
  paymentMethod?: string
  paymentStatus?: string
  paymentStatusText?: string
  paymentTerms?: string
  paymentTermsDescription?: string
  periodForPayment?: string
  plantIncludedContract?: boolean
  preAwardMeetingDate?: string
  reasonLoa?: string
  reasonManner?: string
  reasonQuotation?: string
  remeasuredSubcontractSum?: number
  requisitionApprovedDate?: string
  retentionAmount?: number
  retentionBalance?: number
  retentionReleased?: number
  retentionTerms?: string
  scApprovalDate?: string
  scAwardApprovalRequestSentDate?: string
  scCreatedDate?: string
  scDocLegalDate?: string
  scDocScrDate?: string
  scFinalAccDraftDate?: string
  scFinalAccSignoffDate?: string
  scStatus?: number
  splitTerminateStatus?: string
  splitTerminateStatusText?: string
  subcontractSum?: number
  subcontractTerm?: string
  subcontractType?: string
  subcontractorNature?: string
  submittedAddendum?: string
  systemStatus?: SystemStatus
  tenderAnalysisApprovedDate?: string
  totalAPPostedCertAmount?: number
  totalCCPostedCertAmount?: number
  totalCumCertifiedAmount?: number
  totalCumWorkDoneAmount?: number
  totalMOSPostedCertAmount?: number
  totalNetPostedCertifiedAmount?: number
  totalNonRecoverableAmount?: number
  totalPostedCertifiedAmount?: number
  totalPostedWorkDoneAmount?: number
  totalProvisionAmount?: number
  totalRecoverableAmount?: number
  vendorNo?: string
  workCommenceDate?: string
  workscope?: number
}

export type Payment = CreatedUser &
  Subcontract & {
    paymentCertNo?: string
    paymentStatus?: string
    mainContractPaymentCertNo?: number
    dueDate?: string
    asAtDate?: string
    ipaOrInvoiceReceivedDate?: string | null
    certIssueDate?: string
    certAmount?: number
    intermFinalPayment?: string
    addendumAmount?: number
    remeasureContractSum?: number
    directPayment?: string
    jobNo?: string
    packageNo?: string
    bypassPaymentTerms?: string
    originalDueDate?: string | null
    vendorNo?: string | null
    explanation?: string | null
  }

export type SCDetail = CreatedUser & {
  jobNo?: string
  sequenceNo?: number
  resourceNo?: number
  billItem?: string
  description?: string
  quantity?: number
  scRate?: number
  objectCode?: string
  subsidiaryCode?: string
  lineType?: string
  approved?: string
  unit?: string
  remark?: string
  typeRecoverable?: string
  postedCertifiedQuantity?: number
  cumCertifiedQuantity?: number
  amountCumulativeCert?: number
  amountPostedCert?: number
  amountSubcontractNew?: number
  newQuantity?: number
  originalQuantity?: number
  tenderAnalysisDetail_ID?: string
  subcontract?: Subcontract
  amountSubcontract?: number
  amountBudget?: number
  balanceType?: string
  amountPostedWD?: number
  postedWorkDoneQuantity?: number
  amountCumulativeWD?: number
  cumWorkDoneQuantity?: number
  costRate?: number
  ivamount?: number
  sourceType?: string
  totalAmount?: number
  toBeApprovedAmount?: number
  provision?: number
  projectedProvision?: number
  workDoneMovement?: number
  altObjectCode?: string
  toBeApprovedRate?: number
  toBeApprovedQuantity?: number
  contraChargeSCNo?: string
  corrSCLineSeqNo?: number
  amountBudgetNew?: number
}

export type SCDetailRequest = {
  jobNo?: string
  subcontractNo?: number
}

export type SessionListDetail = {
  authType?: string
  creationTime?: number
  expired?: boolean
  idleTime?: number
  lastAccessedTime?: number
  lastRequest?: string
  maxInactiveInterval?: number
  principal?: CurrentUser
  sessionId?: string
}

export type ProceduresAuditTableItemMap = {
  housekeep?: boolean
  period?: string
  tableName?: string
}

export type ProceduresCutoffPeriod = CreatedUser & {
  cutoffDate?: string
  excludeJobList?: null | JobListResponse
  period?: string
}

export type AddendumRequest = SubcontractResquest & {
  addendumNo?: string
}

export type AddendumResponse = {
  usernameCreated?: string
  dateCreated?: string
  usernameLastModified?: string
  dateLastModified?: string
  id?: number
  idSubcontract?: Subcontract
  noJob?: string
  noSubcontract?: string
  descriptionSubcontract?: string
  noSubcontractor?: string
  nameSubcontractor?: string
  no?: number
  title?: string
  amtSubcontractRemeasured?: number
  amtSubcontractRevised?: number
  amtAddendumTotal?: number
  amtAddendumTotalTba?: number
  amtAddendum?: number
  amtSubcontractRevisedTba?: number
  recoverableAmount?: number
  nonRecoverableAmount?: number
  dateSubmission?: string
  dateApproval?: string
  status?: string
  statusApproval?: string
  usernamePreparedBy?: string
  remarks?: string
  finalAccount?: string
  noAddendumDetailNext?: number
  amtCEDApproved?: string
  cedApproval?: string
}

export type FinalAccountResponse = CreatedUser & {
  jobNo?: string
  addendumNo?: string
  addendum?: AddendumResponse
  finalAccountAppAmt?: number
  finalAccountAppCCAmt?: number
  finalAccountPreAmt?: number
  finalAccountPreCCAmt?: number
  finalAccountThisAmt?: number
  finalAccountThisCCAmt?: number
  latestBudgetAmt?: number
  latestBudgetAmtCC?: number
  comments?: string
  preparedDate?: string
  preparedUser?: string
  status?: string
}

export type FinalAccountRequest = {
  subcontractNo?: string
  data: FinalAccountResponse
}

export type UserInfo = {
  department?: string
  division?: string
  email?: string
  employeeId?: string
  fullName?: string
  mobile?: string
  physicalDeliveryOfficeName?: string
  supportBy?: string
  telOffice?: string
  title?: string
  username?: string
}

export type MemoResponse = CreatedUser & {
  budgetAmount?: number
  ccList?: string
  consultantName?: string
  dateApproval?: string
  dateSubmission?: string
  description?: string
  explanation?: string
  feeEstimate?: number
  fromList?: string
  idSubcontract?: Subcontract
  period?: string
  ref?: string
  statusApproval?: string
  subject?: string
  toList?: string
}

export type ConsultancyAgreementRequest = SubcontractResquest & {
  data?: MemoResponse
}

export type IDADDENDUM = {
  amtAddendum?: number
  amtAddendumTotal?: number
  amtAddendumTotalTba?: number
  amtCEDApproved?: string
  amtSubcontractRemeasured?: number
  amtSubcontractRevised?: number
  amtSubcontractRevisedTba?: number
  cedApproval?: string
  dateApproval?: string
  dateCreated?: string
  dateLastModified?: string
  dateSubmission?: string
  descriptionSubcontract?: string
  finalAccount?: string
  id?: number
  nameSubcontractor?: string
  no?: number
  noAddendumDetailNext?: number
  noJob?: string
  noSubcontract?: string
  noSubcontractor?: string
  nonRecoverableAmount?: number
  recoverableAmount?: number
  remarks?: string
  status?: string
  statusApproval?: string
  title?: string
  usernameCreated?: string
  usernameLastModified?: string
  usernamePreparedBy?: string
  idSubcontract?: Subcontract
}

export type ADDENDUM = {
  amtAddendum?: number
  amtBudget?: number
  bpi?: string
  codeObject?: string
  codeObjectForDaywork?: string
  codeSubsidiary?: string
  dateCreated?: string
  dateLastModified?: string
  description?: string
  id?: number
  idHeaderRef?: string
  idResourceSummary?: string
  idSubcontractDetail?: string
  no?: number
  noJob?: string
  noSubcontract?: string
  noSubcontractChargedRef?: string
  quantity?: number
  rateAddendum?: number
  rateBudget?: number
  remarks?: string
  typeAction?: string
  typeHd?: string
  typeRecoverable?: string
  typeVo?: string
  unit?: string
  usernameCreated?: string
  usernameLastModified?: string
  idAddendum?: IDADDENDUM
}

export type Attachment = {
  dateCreated?: string
  dateLastModified?: string
  id?: number
  idTable?: number
  nameFile?: string
  nameTable?: string
  noSequence?: number
  pathFile?: string
  text?: string
  typeDocument?: string
  usernameCreated?: string
  usernameLastModified?: string
}

export type Repackaging = {
  amountBudget?: number
  createdDate?: string
  createdUser?: string
  cumQuantity?: number
  currIVAmount?: number
  excludeDefect?: string
  excludeLevy?: string
  finalized?: string
  forIvRollbackOnly?: string
  id?: number
  ivMovement?: number
  jobInfo?: JobInfo
  lastModifiedDate?: string
  lastModifiedUser?: string
  mergeTo?: string
  objectCode?: string
  packageNo?: string
  postedIVAmount?: number
  quantity?: number
  rate?: number
  resourceDescription?: string
  resourceType?: string
  splitFrom?: string
  subsidiaryCode?: string
  systemStatus?: string
  unit?: string
}

export type LatestRepackaging = {
  createDate?: string
  createdDate?: string
  createdUser?: string
  id?: number
  jobInfo?: JobInfo
  lastModifiedDate?: string
  lastModifiedUser?: string
  remarks?: string
  repackagingVersion?: number
  status?: string
  systemStatus?: string
  totalResourceAllowance?: number
}

export type TENDER = {
  amtBuyingGainLoss?: number
  budgetAmount?: number
  createdDate?: string
  createdUser?: string
  currencyCode?: string
  datePrepared?: string
  exchangeRate?: number
  id?: number
  jobNo?: string
  lastModifiedDate?: string
  lastModifiedUser?: string
  latestBudgetForecast?: string
  nameSubcontractor?: string
  packageNo?: string
  remarks?: string
  status?: string
  statusChangeExecutionOfSC?: string
  subcontract?: Subcontract
  systemStatus?: string
  usernamePrepared?: string
  validTender?: string
  vendorNo?: number
}

export type TenderDetailList = {
  amountBudget?: number
  amountForeign?: number
  amountSubcontract?: number
  billItem?: string
  createdDate?: string
  createdUser?: string
  description?: string
  id?: number
  lastModifiedDate?: string
  lastModifiedUser?: string
  lineType?: string
  objectCode?: string
  quantity?: number
  rateBudget?: number
  rateSubcontract?: number
  remark?: string
  resourceNo?: number
  sequenceNo?: number
  subsidiaryCode?: string
  systemStatus?: string
  tender?: TENDER
  unit?: string
}

export type RocClassDescMap = {
  classification: string
  createdDate?: string
  createdUser?: string
  description?: string
  id?: number
  lastModifiedDate?: string
  lastModifiedUser?: string
  systemStatus?: string
}

export type RocAdmin = {
  classification?: string
  closedDate?: string
  createdDate?: string
  createdUser?: string
  description?: string
  id?: number
  impact?: string
  itemNo?: number
  lastModifiedDate?: string
  lastModifiedUser?: string
  openDate?: string
  projectNo?: string
  projectRef?: string
  rocCategory?: string
  rocOwner?: string
  status?: string
  systemStatus?: string
}

export type RocDetail = {
  amountBest?: number
  amountRealistic?: number
  amountWorst?: number
  detailId?: number
  itemNo?: number
  month?: number
  remarks?: string
  status?: string
  systemStatus?: string
  year?: number
}

export type RocSubDetail = {
  amountBest?: number
  amountRealistic?: number
  amountWorst?: number
  description?: string
  hyperlink?: string
  itemNo?: number
  month?: number
  remarks?: string
  subdetailId?: number
  systemStatus?: string
  year?: number
}
export type Certificate = {
  actualReceiptDate?: string
  amount_cumulativeRetention?: number
  amount_retentionRelease?: number
  appGrossAmount?: number
  appNetAmount?: number
  appliedAdjustmentAmount?: number
  appliedAdvancePayment?: number
  appliedCPFAmount?: number
  appliedClaimsAmount?: number
  appliedContraChargeAmount?: number
  appliedMOSAmount?: number
  appliedMOSRetention?: number
  appliedMOSRetentionReleased?: number
  appliedMainContractorAmount?: number
  appliedMainContractorRetention?: number
  appliedMainContractorRetentionReleased?: number
  appliedNSCNDSCAmount?: number
  appliedRetentionforNSCNDSC?: number
  appliedRetentionforNSCNDSCReleased?: number
  appliedVariationAmount?: number
  arDocNumber?: number
  certAsAtDate?: string
  certDueDate?: string
  certGrossAmount?: number
  certIssueDate?: string
  certNetAmount?: number
  certStatusChangeDate?: string
  certificateNumber?: string
  certificateStatus?: string
  certifiedAdjustmentAmount?: number
  certifiedAdvancePayment?: number
  certifiedCPFAmount?: number
  certifiedClaimsAmount?: number
  certifiedContraChargeAmount?: number
  certifiedMOSAmount?: number
  certifiedMOSRetention?: number
  certifiedMOSRetentionReleased?: number
  certifiedMainContractorAmount?: number
  certifiedMainContractorRetention?: number
  certifiedMainContractorRetentionReleased?: number
  certifiedNSCNDSCAmount?: number
  certifiedRetentionforNSCNDSC?: number
  certifiedRetentionforNSCNDSCReleased?: number
  certifiedVariationAmount?: number
  clientCertNo?: string
  createdDate?: string
  createdUser?: string
  gstPayable?: number
  gstReceivable?: number
  id?: number
  ipaSentoutDate?: string
  ipaSubmissionDate?: string
  jobNo?: string
  lastModifiedDate?: string
  lastModifiedUser?: string
  remark?: string
  systemStatus?: string
  totalReceiptAmount?: number
}

export type Tender = {
  amtBuyingGainLoss?: number
  budgetAmount?: number
  createdDate?: string
  createdUser?: string
  currencyCode?: string
  datePrepared?: string
  exchangeRate?: number
  id?: number
  jobNo?: string
  lastModifiedDate?: string
  lastModifiedUser?: string
  latestBudgetForecast?: string
  nameSubcontractor?: string
  packageNo?: string
  remarks?: string
  status?: string
  statusChangeExecutionOfSC?: string
  subcontract?: Subcontract
  systemStatus?: string
  usernamePrepared?: string
  validTender?: string
  vendorNo?: number
}

export type CompanyCodeAndName = {
  companyCode?: string
  companyName?: string
}

export type ScStandardTerms = {
  company?: string
  createdDate?: string
  createdUser?: string
  formOfSubcontract?: string
  id?: number
  lastModifiedDate?: string
  lastModifiedUser?: string
  retentionType?: string
  scInterimRetentionPercent?: number
  scMOSRetentionPercent?: number
  scMaxRetentionPercent?: number
  scPaymentTerm?: string
  systemStatus?: string
}

export type MonthlyMovement = {
  amount?: number
  date?: string
  dateCreated?: string
  dateLastModified?: string
  explanation?: string
  forecastDesc?: string
  forecastFlag?: string
  forecastPeriod?: string
  forecastType?: string
  id?: number
  month?: number
  noJob?: string
  usernameCreated?: string
  usernameLastModified?: string
  year?: number
}

export const {
  useObtainUserPreferenceByCurrentUserQuery,
  useGetCurrentUserQuery,
  useValidateCurrentSessionQuery,
  useObtainCacheKeyQuery,
  useGetNotificationReadStatusByCurrentUserQuery,
  useGetPropertiesQuery,
  useGetJobQuery,
  useGetJobDatesQuery,
  useGetRepackagingQuery,
  useGetJobDashboardDataQuery,
  useGetResourceSummariesGroupByObjectCodeQuery,
  useGetJobListMutation,
  useGetSubcontractMutation,
  useGetAllWorkScopesQuery,
  useUpdateNotificationReadStatusByCurrentUserMutation,
  useGetSCDetailsMutation,
  useGetSessionListQuery,
  useGetAuditTableMapQuery,
  useGetCutoffPeriodQuery,
  useHousekeepAuditTableMutation,
  useRunProvisionPostingManuallyMutation,
  useUpdateCEOApprovalMutation,
  useGenerateSCPackageSnapshotManuallyMutation,
  useRunPaymentPostingMutation,
  useUpdateF58001FromSCPackageManuallyMutation,
  useUpdateF58011FromSCPaymentCertManuallyMutation,
  useUpdateMainCertFromF03B14ManuallyMutation,
  useGeneratePaymentPDFAdminMutation,
  useUpdateSubcontractAdminMutation,
  useGetPaymentCertMutation,
  useUpdateSubcontractDetailListAdminMutation,
  useDeletePendingPaymentAndDetailsMutation,
  useUpdatePaymentCertMutation,
  useGetAddendumMutation,
  useUpdateAddendumAdminMutation,
  useGetFinalAccountAdminMutation,
  useUpdateFinalAccountAdminMutation,
  useDeleteFinalAccountAdminMutation,
  useFindByUsernameIsNotNullQuery,
  useGetMemoMutation,
  useUpdateConsultancyAgreementAdminMutation,
  useGetAllAddendumDetailsMutation,
  useUpdateAddendumDetailListAdminMutation,
  useObtainResourceSummariesByJobNumberForAdminMutation,
  useUpdateResourceSummariesForAdminMutation,
  useObtainAttachmentListMutation,
  useGetLatestRepackagingMutation,
  useDeleteAttachmentAdminMutation,
  useGetTenderDetailListMutation,
  useUpdateTenderDetailListAdminMutation,
  useGetRocClassDescMapQuery,
  useGetRocCategoryListQuery,
  useGetImpactListQuery,
  useGetRocAdminMutation,
  useUpdateRocAdminMutation,
  useGetRocDetailListAdminMutation,
  useUpdateRocDetailListAdminMutation,
  useGetRocSubdetailListAdminMutation,
  useUpdateRocSubdetailListAdminMutation,
  useGetCertificateMutation,
  useUpdateCertificateByAdminMutation,
  useGetTenderMutation,
  useUnlockTransitAdminMutation,
  useUpdateTenderAdminMutation,
  useCompleteAwardApprovalAdminMutation,
  useCompletePaymentApprovalAdminMutation,
  useCompleteSplitTerminateApprovalAdminMutation,
  useCompleteAddendumApprovalAdminMutation,
  useCompleteMainCertApprovalAdminMutation,
  useGetCompanyCodeAndNameQuery,
  useGetDivisionsQuery,
  useDownloadFileMutation,
  useGetSCStandardTermsListQuery,
  useUpdateMultipleSystemConstantsMutation,
  useDeleteSCStandardTermsMutation,
  useCreateSystemConstantMutation,
  useObtainAllJobCompanyQuery,
  useGetForecastListMutation,
  useUpdateForecastListAdminMutation,
  useDeleteForecastListAdminMutation
} = apiSlice
export default apiSlice
