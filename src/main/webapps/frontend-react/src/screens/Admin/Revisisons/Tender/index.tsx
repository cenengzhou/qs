/* eslint-disable @typescript-eslint/naming-convention */
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import { DropDownListComponent } from '@syncfusion/ej2-react-dropdowns'
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'

const Tender = () => {
  const data = {
    createdUser: 'yikkeungch',
    createdDate: '2016-07-27T06:37:47.000+00:00',
    lastModifiedUser: 'SYSTEM',
    lastModifiedDate: '2016-07-29T10:25:00.000+00:00',
    systemStatus: 'ACTIVE',
    id: 88071,
    subcontract: {
      createdUser: 'yikkeungch',
      createdDate: '2016-07-27T06:34:49.000+00:00',
      lastModifiedUser: 'SYSTEM',
      lastModifiedDate: '2022-06-26T03:01:15.000+00:00',
      systemStatus: 'ACTIVE',
      id: 65758,
      jobInfo: {
        createdUser: 'alexyeung',
        createdDate: '2011-02-21T07:54:14.000+00:00',
        lastModifiedUser: 'IDT-KOEY',
        lastModifiedDate: '2022-09-05T13:45:02.000+00:00',
        systemStatus: 'ACTIVE',
        id: 643,
        budgetPosted: null,
        eotApplied: 0.0,
        eotAwarded: 0.0,
        ldExposureAmount: 0.0,
        residualAfterEot: 0.0,
        nameExecutiveDirector: null,
        nameDirector: null,
        nameDirectSupervisorPic: null,
        nameProjectInCharge: null,
        nameCommercialInCharge: null,
        nameTempWorkController: null,
        nameSafetyEnvRep: null,
        nameAuthorizedPerson: null,
        nameSiteAdmin1: null,
        nameSiteAdmin2: null,
        nameSiteManagement1: null,
        nameSiteManagement2: null,
        nameSiteManagement3: null,
        nameSiteManagement4: null,
        nameSiteSupervision1: null,
        nameSiteSupervision2: null,
        nameSiteSupervision3: null,
        nameSiteSupervision4: null,
        noReference: 0,
        statusApproval: null,
        projectFullDescription: 'Gammon TKO Workshop Maintenance Works',
        projectLocation: ' ',
        natureOfWorkBB: null,
        natureOfWorkGammon: ' ',
        tenderNumber: ' ',
        immediateParentJobNumber: null,
        ultimateParentJobNumber: null,
        ultimateClient: '0',
        immediateClientBusinessSector: null,
        ultimateClientBusinessSector: null,
        clientType: null,
        engineerArchitect: null,
        beam: null,
        beamPlus: null,
        leed: null,
        innovationApplicable: '0',
        innovationPercent: null,
        paymentTermFromClient: null,
        provision: 'N',
        isParentCompanyGuarantee: null,
        jobNo: '90013',
        description: 'TKO Office',
        company: '00001',
        employer: '0',
        contractType: 'LS',
        division: 'TKO',
        department: 'TKO',
        internalJob: ' ',
        soloJV: 'S',
        completionStatus: ' ',
        insuranceCAR: ' ',
        insuranceECI: ' ',
        insuranceTPL: ' ',
        clientContractNo: null,
        parentJobNo: '90013',
        jvPartnerNo: '0',
        jvPercentage: 0.0,
        originalContractValue: 615090.06,
        projectedContractValue: 615090.06,
        orginalNSCContractValue: 0.0,
        tenderGP: 0.0,
        forecastEndYear: 0,
        forecastEndPeriod: 0,
        maxRetPercent: 0.0,
        interimRetPercent: 0.0,
        mosRetPercent: 0.0,
        valueOfBSWork: 0.0,
        grossFloorArea: 0.0,
        grossFloorAreaUnit: '  ',
        billingCurrency: '   ',
        paymentTermForNSC: null,
        defectProvisionPercent: 0.0,
        cpfApplicable: '0',
        cpfIndexName: null,
        cpfBaseYear: null,
        cpfBasePeriod: null,
        levyApplicable: '0',
        levyCITAPercent: 0.0,
        levyPCFBPercent: 0.0,
        expectedPCCDate: null,
        actualPCCDate: null,
        expectedMakingGoodDate: null,
        actualMakingGoodDate: null,
        defectLiabilityPeriod: 0,
        defectListIssuedDate: null,
        financialEndDate: null,
        dateFinalACSettlement: null,
        yearOfCompletion: 0,
        bqFinalizedFlag: ' ',
        manualInputSCWD: 'Y',
        legacyJob: null,
        conversionStatus: 'Y',
        repackagingType: '1',
        finQS0Review: 'D'
      },
      packageNo: '1070',
      description: 'Provide Cleaning Services to Fan Coil Unit at TKO Office',
      packageType: 'S',
      packageStatus: null,
      subcontractorNature: 'DSC',
      originalSubcontractSum: 358750.0,
      approvedVOAmount: 1184300.0,
      remeasuredSubcontractSum: 358750.0,
      approvalRoute: null,
      retentionTerms: null,
      maxRetentionPercentage: 0.0,
      interimRentionPercentage: 0.0,
      mosRetentionPercentage: 0.0,
      retentionAmount: 0.0,
      accumlatedRetention: 0.0,
      retentionReleased: 0.0,
      paymentInformation: 'Interim Payment Schedule (Main Contract)',
      paymentCurrency: 'HKD',
      exchangeRate: 1.0,
      paymentTerms: 'QS5',
      subcontractTerm: 'Re-measurement',
      cpfCalculation: 'Not Subject to CPF',
      cpfBasePeriod: null,
      cpfBaseYear: null,
      formOfSubcontract: 'Minor',
      internalJobNo: null,
      paymentStatus: 'I',
      submittedAddendum: ' ',
      splitTerminateStatus: '0',
      paymentTermsDescription: null,
      notes: ' ',
      workscope: 272,
      nameSubcontractor: 'Check Well Air Condition Company',
      scCreatedDate: '2016-07-27',
      latestAddendumValueUpdatedDate: '2021-11-16',
      firstPaymentCertIssuedDate: '2016-08-04',
      lastPaymentCertIssuedDate: '2022-06-15',
      finalPaymentIssuedDate: null,
      scAwardApprovalRequestSentDate: '2016-07-27',
      scApprovalDate: '2016-07-29',
      labourIncludedContract: true,
      plantIncludedContract: false,
      materialIncludedContract: false,
      totalPostedWorkDoneAmount: 1512525.0,
      totalCumWorkDoneAmount: 1512525.0,
      totalPostedCertifiedAmount: 1512525.0,
      totalCumCertifiedAmount: 1512525.0,
      totalCCPostedCertAmount: 0.0,
      totalMOSPostedCertAmount: 0.0,
      totalAPPostedCertAmount: 0,
      totalRecoverableAmount: 0,
      totalNonRecoverableAmount: 0,
      requisitionApprovedDate: '2016-04-29',
      tenderAnalysisApprovedDate: '2016-07-20',
      preAwardMeetingDate: '2016-07-20',
      loaSignedDate: '2016-07-27',
      scDocScrDate: null,
      scDocLegalDate: null,
      workCommenceDate: '2016-07-27',
      onSiteStartDate: '2016-07-27',
      scFinalAccDraftDate: null,
      scFinalAccSignoffDate: null,
      paymentMethod: null,
      periodForPayment: null,
      amountPackageStretchTarget: null,
      reasonLoa: null,
      dateScExecutionTarget: null,
      executionMethodMainContract: null,
      executionMethodPropsed: null,
      durationFrom: null,
      durationTo: null,
      reasonQuotation: null,
      reasonManner: null,
      amtCEDApproved: 1376300,
      retentionBalance: 0.0,
      splitTerminateStatusText: 'Not Submitted',
      balanceToCompleteAmount: 30525.0,
      totalProvisionAmount: 0.0,
      paymentStatusText: 'Interim',
      subcontractType: 'Labour',
      totalNetPostedCertifiedAmount: 1512525.0,
      awarded: true,
      subcontractSum: 1543050.0,
      vendorNo: '426461',
      scStatus: 500
    },
    vendorNo: 426461,
    status: 'AWD',
    currencyCode: 'HKD',
    exchangeRate: 1.0,
    budgetAmount: 358750.0,
    jobNo: '90013',
    packageNo: '1070',
    amtBuyingGainLoss: 0,
    remarks: ' ',
    statusChangeExecutionOfSC: 'N/A',
    usernamePrepared: null,
    datePrepared: null,
    nameSubcontractor: 'Check Well Air Condition Company',
    validTender: null,
    latestBudgetForecast: null
  }

  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
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
          <NumericTextBoxComponent
            placeholder="Vendor Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-3 col-md-3">
          <ButtonComponent cssClass="e-info full-btn">Search</ButtonComponent>
        </div>
      </div>
      <div className="admin-content">
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Job Number"
              floatLabelType="Auto"
              cssClass="e-outline readonly-input"
              value={data.jobNo}
              readOnly
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Tender Number"
              floatLabelType="Auto"
              cssClass="e-outline readonly-input"
              value={data.packageNo}
              readOnly
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Vendor Number"
              floatLabelType="Auto"
              cssClass="e-outline readonly-input"
              value={data.vendorNo.toString()}
              readOnly
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Subcontractor Name"
              floatLabelType="Auto"
              cssClass="e-outline"
              value={data.nameSubcontractor}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <DropDownListComponent
              dataSource={[
                'HKD',
                'USD',
                'INR',
                'GBP',
                'EUR',
                'AUD',
                'THB',
                'TWD',
                'PHP',
                'JPY',
                'SGD',
                'CAD',
                'CNY',
                'MOP'
              ]}
              cssClass="e-outline"
              floatLabelType="Always"
              showClearButton
              placeholder="Curency Code"
              value={data.currencyCode}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Exchange Rate"
              floatLabelType="Auto"
              format="n2"
              cssClass="e-outline"
              value={data.exchangeRate}
            />
          </div>
        </div>
        <div className="row">
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Budget Amount"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
              value={data.budgetAmount}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <NumericTextBoxComponent
              placeholder="Gain/Loss"
              floatLabelType="Auto"
              format="c2"
              cssClass="e-outline"
              value={data.amtBuyingGainLoss}
            />
          </div>
          <div className="col-lg-4 col-md-4">
            <TextBoxComponent
              placeholder="Remarks"
              floatLabelType="Auto"
              cssClass="e-outline"
              value={data.remarks}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-lg-12 col-md-12">
          <ButtonComponent cssClass="e-info full-btn">Update</ButtonComponent>
        </div>
      </div>
    </div>
  )
}

export default Tender
