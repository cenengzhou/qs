/* eslint-disable @typescript-eslint/naming-convention */
import {
  BaseQueryFn,
  FetchArgs,
  createApi,
  fetchBaseQuery
} from '@reduxjs/toolkit/query/react'

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
      getJobList: builder.query<JobListResponse, boolean>({
        query: queryArg => ({
          method: 'POST',
          url: 'service/job/getJobList',
          body: queryArg
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
      getSubcontract: builder.mutation<
        Subcontract,
        {
          jobNo?: string
          subcontractNo?: number
        }
      >({
        query: ({ jobNo, subcontractNo }) => ({
          url: `service/subcontract/getSubcontract?jobNo=${jobNo}&subcontractNo=${subcontractNo}`
        })
      }),
      getSCDetails: builder.mutation<SCDetail[], SCDetailRequest>({
        query: queryArg => ({
          url: 'service/subcontract/getSCDetails',
          params: queryArg
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

export type Subcontract = {
  accumlatedRetention?: number
  amountPackageStretchTarget?: string
  amtCEDApproved?: string
  approvalRoute?: string
  approvedVOAmount?: number
  awarded?: boolean
  balanceToCompleteAmount?: number
  cpfBasePeriod?: number
  cpfBaseYear?: string
  cpfCalculation?: string
  createdDate?: string
  createdUser?: string
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
  id?: number
  interimRentionPercentage?: number
  internalJobNo?: string
  jobInfo?: JobInfo
  labourIncludedContract?: boolean
  lastModifiedDate?: string
  lastModifiedUser?: string
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
  subcontractNo?: string
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
  useGetJobListQuery,
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
  useUpdateSubcontractAdminMutation
} = apiSlice
export default apiSlice
