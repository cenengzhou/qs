/* eslint-disable @typescript-eslint/naming-convention */

/* eslint-disable @typescript-eslint/no-explicit-any */
import { ButtonComponent } from '@syncfusion/ej2-react-buttons'
import {
  Aggregate,
  AggregateColumnDirective,
  AggregateColumnsDirective,
  AggregateDirective,
  AggregatesDirective,
  ColumnChooser,
  ColumnDirective,
  ColumnMenu,
  ColumnsDirective,
  Edit,
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

import './style.css'

const TenderDetail = () => {
  const toolbar: ToolbarItems[] = [
    'ExcelExport',
    'CsvExport',
    'ColumnChooser',
    'Update',
    'Cancel'
  ]

  const data = [
    {
      createdUser: 'yikkeungch',
      createdDate: '2016-07-27T06:37:47.000+00:00',
      lastModifiedUser: 'yikkeungch',
      lastModifiedDate: '2016-07-27T06:37:47.000+00:00',
      systemStatus: 'ACTIVE',
      id: 172430,
      tender: {
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
          description:
            'Provide Cleaning Services to Fan Coil Unit at TKO Office',
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
      },
      sequenceNo: 1,
      resourceNo: 313763,
      billItem: null,
      description: 'Cleaning Services',
      quantity: 1.0,
      rateBudget: 50.0,
      rateSubcontract: 50.0,
      objectCode: '140299',
      subsidiaryCode: '49809999',
      lineType: 'BQ',
      unit: 'AM',
      remark: null,
      amountBudget: 50.0,
      amountSubcontract: 50.0,
      amountForeign: 0.0
    },
    {
      createdUser: 'yikkeungch',
      createdDate: '2016-07-27T06:37:47.000+00:00',
      lastModifiedUser: 'yikkeungch',
      lastModifiedDate: '2016-07-27T06:37:47.000+00:00',
      systemStatus: 'ACTIVE',
      id: 172431,
      tender: {
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
          description:
            'Provide Cleaning Services to Fan Coil Unit at TKO Office',
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
      },
      sequenceNo: 2,
      resourceNo: 313762,
      billItem: null,
      description: 'Provide Cleaning Services to Fan Coil Unit at TKO Office',
      quantity: 358700.0,
      rateBudget: 1.0,
      rateSubcontract: 1.0,
      objectCode: '140299',
      subsidiaryCode: '49809999',
      lineType: 'BQ',
      unit: 'AM',
      remark: null,
      amountBudget: 358700.0,
      amountSubcontract: 358700.0,
      amountForeign: 0.0
    }
  ]

  const totalSum = (e: any) => {
    return e.Sum
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
          editSettings={{
            allowEditing: true,
            mode: 'Batch'
          }}
          filterSettings={{ type: 'Menu' }}
          cssClass="no-margin-right"
        >
          <ColumnsDirective>
            <ColumnDirective
              field="id"
              headerText="ID"
              allowEditing={false}
              visible={false}
              isPrimaryKey={true}
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="tender.jobNo"
              headerText="Job Number"
              allowEditing={false}
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="tender.packageNo"
              headerText="Subcontract"
              allowEditing={false}
              width="120"
            ></ColumnDirective>
            <ColumnDirective
              field="tender.vendorNo"
              headerText="Vendor"
              allowEditing={false}
              width="150"
            />
            <ColumnDirective
              field="sequenceNo"
              headerText="Sequence Number"
              allowEditing={false}
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="description"
              headerText="Description"
              width="250"
            ></ColumnDirective>
            <ColumnDirective
              field="quantity"
              headerText="Quantity"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="rateBudget"
              headerText="Budget Rate"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="amountBudget"
              headerText="Budget Amount"
              width="160"
            ></ColumnDirective>
            <ColumnDirective
              field="rateSubcontract"
              headerText="SC Rate"
              width="150"
            ></ColumnDirective>
            <ColumnDirective
              field="amountSubcontract"
              headerText="SC Amount"
              editType="numericedit"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="amountForeign"
              headerText="Amount Foreign"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="objectCode"
              headerText="Object Code"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="subsidiaryCode"
              headerText="Subsidiary Code"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="lineType"
              headerText="Line Type"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="unit"
              headerText="Unit"
              width="200"
            ></ColumnDirective>
            <ColumnDirective
              field="billItem"
              headerText="B/P/I"
              width="200"
            ></ColumnDirective>
          </ColumnsDirective>
          <AggregatesDirective>
            <AggregateDirective>
              <AggregateColumnsDirective>
                <AggregateColumnDirective
                  field="amountSubcontract"
                  type="Sum"
                  footerTemplate={totalSum}
                ></AggregateColumnDirective>
                <AggregateColumnDirective
                  field="amountForeign"
                  type="Sum"
                  footerTemplate={totalSum}
                ></AggregateColumnDirective>
              </AggregateColumnsDirective>
            </AggregateDirective>
          </AggregatesDirective>
          <Inject
            services={[
              Page,
              ExcelExport,
              Toolbar,
              ColumnChooser,
              ColumnMenu,
              Filter,
              Sort,
              Aggregate,
              Edit
            ]}
          />
        </GridComponent>
      </div>
    </div>
  )
}

export default TenderDetail
