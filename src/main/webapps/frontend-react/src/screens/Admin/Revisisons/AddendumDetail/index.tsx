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
import {
  NumericTextBoxComponent,
  TextBoxComponent
} from '@syncfusion/ej2-react-inputs'
import { TooltipComponent } from '@syncfusion/ej2-react-popups'

import './style.css'

const AddendumDetail = () => {
  const toolbar: ToolbarItems[] = ['ExcelExport', 'CsvExport', 'ColumnChooser']

  const data: any[] = [
    {
      usernameCreated: 'raymondcwy',
      dateCreated: '2022-01-06T05:09:11.000+00:00',
      usernameLastModified: 'raymondcwy',
      dateLastModified: '2022-01-06T05:09:11.000+00:00',
      id: 97463,
      idAddendum: {
        usernameCreated: 'raymondcwy',
        dateCreated: '2022-01-06T05:06:00.000+00:00',
        usernameLastModified: 'SYSTEM',
        dateLastModified: '2022-01-10T01:18:10.000+00:00',
        id: 29478,
        idSubcontract: {
          createdUser: 'conniewoo',
          createdDate: '2019-09-17T02:52:57.000+00:00',
          lastModifiedUser: 'SYSTEM',
          lastModifiedDate: '2022-03-25T20:29:23.000+00:00',
          systemStatus: 'ACTIVE',
          id: 73570,
          jobInfo: {
            createdUser: 'conniewoo',
            createdDate: '2019-08-23T01:58:42.000+00:00',
            lastModifiedUser: 'IDT-KOEY',
            lastModifiedDate: '2022-09-05T13:45:08.000+00:00',
            systemStatus: 'ACTIVE',
            id: 7813,
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
            noReference: 47523469,
            statusApproval: 'APPROVED',
            projectFullDescription:
              'WORKS CONTRACT NO.: PD/WC/209\nMAIN WORKS CONTRACT FOR DEVELOPMENT OF IE 2.0 PROJECT C\nADVANCED MANUFACTURING CENTRE',
            projectLocation: 'TKO Industrial Estate',
            natureOfWorkBB: null,
            natureOfWorkGammon: 'K02',
            tenderNumber: 'T19010E',
            immediateParentJobNumber: null,
            ultimateParentJobNumber: null,
            ultimateClient: '33103',
            immediateClientBusinessSector: null,
            ultimateClientBusinessSector: null,
            clientType: null,
            engineerArchitect: 'WONG TUNG & PARTNERS LIMITED',
            beam: null,
            beamPlus: null,
            leed: null,
            innovationApplicable: '0',
            innovationPercent: null,
            paymentTermFromClient: 49,
            provision: 'N',
            isParentCompanyGuarantee: null,
            jobNo: '13838',
            description: 'Adv Manfacturg Ctr E&M(S13828)',
            company: '00007',
            employer: '33540',
            contractType: 'LS',
            division: 'E&M',
            department: 'E&M',
            internalJob: 'EB',
            soloJV: 'S',
            completionStatus: '1',
            insuranceCAR: ' ',
            insuranceECI: ' ',
            insuranceTPL: ' ',
            clientContractNo: 'PD/WC/209',
            parentJobNo: '13838',
            jvPartnerNo: '0',
            jvPercentage: 0.0,
            originalContractValue: 1.413171954e9,
            projectedContractValue: 1.413171954e9,
            orginalNSCContractValue: 0.0,
            tenderGP: 9.8922037e7,
            forecastEndYear: 2021,
            forecastEndPeriod: 0,
            maxRetPercent: 5.0,
            interimRetPercent: 10.0,
            mosRetPercent: 10.0,
            valueOfBSWork: 1.413171954e9,
            grossFloorArea: 0.0,
            grossFloorAreaUnit: '  ',
            billingCurrency: '   ',
            paymentTermForNSC: null,
            defectProvisionPercent: 0.5,
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
            defectLiabilityPeriod: 12,
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
          packageNo: '1003',
          description:
            'Provision of skilled labour for the movement and fixing of modules',
          packageType: 'S',
          packageStatus: null,
          subcontractorNature: 'DSC',
          originalSubcontractSum: 9667300.0,
          approvedVOAmount: 8528584.0,
          remeasuredSubcontractSum: 9667300.0,
          approvalRoute: null,
          retentionTerms: 'No Retention',
          maxRetentionPercentage: 0.0,
          interimRentionPercentage: 0.0,
          mosRetentionPercentage: 0.0,
          retentionAmount: 0.0,
          accumlatedRetention: 0.0,
          retentionReleased: 0.0,
          paymentInformation: null,
          paymentCurrency: 'HKD',
          exchangeRate: 1.0,
          paymentTerms: 'QS0',
          subcontractTerm: 'Re-measurement',
          cpfCalculation: '0',
          cpfBasePeriod: null,
          cpfBaseYear: null,
          formOfSubcontract: 'Major',
          internalJobNo: null,
          paymentStatus: 'I',
          submittedAddendum: ' ',
          splitTerminateStatus: '0',
          paymentTermsDescription: 'Pay when GEM get paid + 14 days',
          notes:
            'Inclusive of car park module connection works\nThe SED module connection works are by Fugo \nThe Airtech rates are inclusive of hand tools, PPE + MPF\nSC sum not fixed dependent on quantity of labour provided by Airtech',
          workscope: 264,
          nameSubcontractor: 'Airtech M&E HK Ltd',
          scCreatedDate: null,
          latestAddendumValueUpdatedDate: '2022-01-10',
          firstPaymentCertIssuedDate: '2020-06-10',
          lastPaymentCertIssuedDate: '2022-03-15',
          finalPaymentIssuedDate: null,
          scAwardApprovalRequestSentDate: '2020-05-27',
          scApprovalDate: '2020-06-02',
          labourIncludedContract: true,
          plantIncludedContract: false,
          materialIncludedContract: false,
          totalPostedWorkDoneAmount: 17689304.5,
          totalCumWorkDoneAmount: 17689304.5,
          totalPostedCertifiedAmount: 17229417.5,
          totalCumCertifiedAmount: 17229417.5,
          totalCCPostedCertAmount: -124965.0,
          totalMOSPostedCertAmount: 0.0,
          totalAPPostedCertAmount: 0,
          totalRecoverableAmount: 616340,
          totalNonRecoverableAmount: 1448710,
          requisitionApprovedDate: '2019-12-02',
          tenderAnalysisApprovedDate: '2020-04-16',
          preAwardMeetingDate: '2020-04-03',
          loaSignedDate: null,
          scDocScrDate: '2020-05-20',
          scDocLegalDate: '2020-05-20',
          workCommenceDate: '2020-05-05',
          onSiteStartDate: '2020-05-06',
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
          amtCEDApproved: 18168484,
          retentionBalance: 0.0,
          splitTerminateStatusText: 'Not Submitted',
          balanceToCompleteAmount: 506579.5,
          totalProvisionAmount: 459887.0,
          paymentStatusText: 'Interim',
          subcontractType: 'Labour',
          totalNetPostedCertifiedAmount: 17104452.5,
          awarded: true,
          subcontractSum: 18195884.0,
          vendorNo: '502934',
          scStatus: 500
        },
        noJob: '13838',
        noSubcontract: '1003',
        descriptionSubcontract:
          'Provision of skilled labour for the movement and fixing of modules',
        noSubcontractor: '502934',
        nameSubcontractor: 'Airtech M&E HK Ltd',
        no: 7,
        title: 'Addendum No.7',
        amtSubcontractRemeasured: 9667300,
        amtSubcontractRevised: 18188084,
        amtAddendumTotal: 8520784,
        amtAddendumTotalTba: 8528584,
        amtAddendum: 7800,
        amtSubcontractRevisedTba: 18195884,
        recoverableAmount: 7800,
        nonRecoverableAmount: 0,
        dateSubmission: '2022-01-06',
        dateApproval: '2022-01-10',
        status: 'APPROVED',
        statusApproval: 'APPROVED',
        usernamePreparedBy: 'raymondcwy',
        remarks:
          'ISC-010 Diversion of temporary rainwater pipe to underground drain pipe',
        finalAccount: null,
        noAddendumDetailNext: 3,
        amtCEDApproved: null,
        cedApproval: null
      },
      noJob: '13838',
      noSubcontract: '1003',
      no: 7,
      typeHd: 'DETAIL',
      typeVo: 'V1',
      bpi: '95/7/V1/0002',
      description:
        'ISC-010 Diversion of temporary rainwater pipe to underground drain pipe',
      quantity: 1,
      rateAddendum: 7800,
      amtAddendum: 7800,
      rateBudget: 0,
      amtBudget: 0,
      codeObject: '140199',
      codeSubsidiary: '20801422',
      unit: 'AM',
      remarks: 'charge to GCL',
      idHeaderRef: null,
      codeObjectForDaywork: null,
      noSubcontractChargedRef: null,
      typeAction: 'ADD',
      idResourceSummary: null,
      idSubcontractDetail: null,
      typeRecoverable: 'R'
    }
  ]
  return (
    <div className="admin-container">
      {/* input */}
      <div className="admin-header row">
        <div className="col-lg-4 col-md-4">
          <TextBoxComponent
            placeholder="Job Number"
            floatLabelType="Auto"
            cssClass="e-outline"
          />
        </div>
        <div className="col-lg-4 col-md-4">
          <NumericTextBoxComponent
            placeholder="Subcontract Number"
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
              field="description"
              headerText="Description"
              width="250"
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
              field="bpi"
              headerText="bpi"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="unit"
              headerText="Unit"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="typeVo"
              headerText="typeVo"
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="idResourceSummary"
              headerText="Resource summary"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="idHeaderRef"
              headerText="Header Reference"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="noSubcontractChargedRef"
              headerText="Subcontract charged ref"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="rateAddendum"
              headerText="Addendum Rate"
              format="n4"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="rateBudget"
              headerText="Budget Rate"
              format="n4"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="quantity"
              headerText="Quantity"
              format="n"
              width="100"
            ></ColumnDirective>
            <ColumnDirective
              field="amtAddendum"
              headerText="Addendum Amount"
              format="n4"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="amtBudget"
              headerText="Budget Amount"
              width="180"
              format="n4"
            ></ColumnDirective>
            <ColumnDirective
              template={(e: any) =>
                new Date(e.dateLastModified).toLocaleString()
              }
              headerText="Last Modify Date"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="usernameLastModified"
              headerText="Last Modify User"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="dateCreated"
              template={(e: any) => new Date(e.dateCreated).toLocaleString()}
              headerText="Date Create"
              width="180"
            ></ColumnDirective>
            <ColumnDirective
              field="usernameCreated"
              headerText="User Create"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="typeRecoverable"
              headerText="Recoverable"
              width="150"
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

export default AddendumDetail
