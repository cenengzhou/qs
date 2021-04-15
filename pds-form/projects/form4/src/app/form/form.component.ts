import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FormComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute){
    this.activatedRoute.queryParams.subscribe(params => {
      this.mode = params['mode'] || 'h';
    });
  }

  ngOnInit(): void {
  }

  filename = 'TenderAnalysis_A.ftl.html';
  id = 'form4-container';
  html = `[#ftl]
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=7" />
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
  </head>
  <body>
    <style>
    [#-- style --#]
    </style>
    [#-- data --#]
  </body>
</html>`;
  mode: 'h' | 'f';
  // [#--          => <!--
  // --#]          => -->
  // ☐          => &#9744;
  // ☑          => &#9745;
  // <!-- PCMS start PCMS end --> print, save
  // <!-- AP start --> <!-- AP end --> Contractual Date
  tenderHeader = { name: 'TENDERER', amount: 'AMOUNT', remarks: 'REMARKS'};
  varianceHeader = { term: 'TERM', condition: 'GENERAL CONDITION', proposed: 'PROPOSED VARIANCE', reason: 'REASON FOR VARIANCE' };

  v = {
    jobTitle: { h: '13389 - Ko Shan Theatre', f: '${job.jobNumber!""} - ${job.description!"&nbsp;"}' },
    jobPackage: { h: '1075 - Subcontract 1075', f: '${subcontract.packageNo!""} - ${subcontract.description!"&nbsp;"}' },
    typeDsc: { h: '&#9745;', f: '[#if subcontract.subcontractorNature == "DSC"]&#9745;[#else]&#9744;[/#if]' },
    typeIb: { h: '&#9744', f: '[#if subcontract.subcontractorNature == "IB"]&#9745;[#else]&#9744;[/#if]' },
    typeNdsc: { h: '&#9744', f: '[#if subcontract.subcontractorNature == "NDSC"]&#9745;[#else]&#9744;[/#if]' },
    typeC: { h: '&#9744', f: '[#if subcontract.subcontractorNature == "C"]&#9745;[#else]&#9744;[/#if]' },
    typeNsc: { h: '&#9744', f: '[#if subcontract.subcontractorNature == "NSC"]&#9745;[#else]&#9744;[/#if]' },
    typeOthers: { h: '&#9744', f: '[#if subcontract.subcontractorNature != "DSC" \\and subcontract.subcontractorNature != "IB" \\and subcontract.subcontractorNature != "NDSC" \\and subcontract.subcontractorNature != "C"]&#9745;[#else]&#9744;[/#if]' },
    typeOthersDesc: {h: '&nbsp;', f: '[#if subcontract.subcontractorNature != "DSC" \\and subcontract.subcontractorNature != "IB" \\and subcontract.subcontractorNature != "NDSC" \\and subcontract.subcontractorNature != "C"]${subcontract.subcontractorNature!""}[#else]&nbsp;[/#if]'},
    focMajor: { h: '&#9745', f: '[#if subcontract.formOfSubcontract == "Major"]&#9745;[#else]&#9744;[/#if]' },
    focMinor: { h: '&#9744', f: '[#if subcontract.formOfSubcontract == "Minor"]&#9745;[#else]&#9744;[/#if]' },
    focCA: { h: '&#9744', f: '[#if subcontract.formOfSubcontract == "Consultancy Agreement"]&#9745;[#else]&#9744;[/#if]' },
    focIT: { h: '&#9744', f: '[#if subcontract.formOfSubcontract == "Internal Trading"]&#9745;[#else]&#9744;[/#if]' },
    focOthers: { h: '&#9744', f: '[#if subcontract.formOfSubcontract != "Major" \\and subcontract.formOfSubcontract != "Minor" \\and subcontract.formOfSubcontract != "Consultancy Agreement" \\and subcontract.formOfSubcontract != "Internal Trading"]&#9745;[#else]&#9744;[/#if]' },
    focOthersDesc: { h: '&nbsp;', f: '[#if subcontract.formOfSubcontract != "Major" \\and subcontract.formOfSubcontract != "Minor" \\and subcontract.formOfSubcontract != "Consultancy Agreement" \\and subcontract.formOfSubcontract != "Internal Trading"]${subcontract.formOfSubcontract!""}[#else]&nbsp;[/#if]'},
    rcmAward: { h: 'company name', f: '${rcmTenderer.nameSubcontractor!""}' },
    rcmAmount: { h: 'HK$100.00', f: '${rcmTenderer.currencyCode!""} ${(((rcmTenderer.budgetAmount!0)-(rcmTenderer.amtBuyingGainLoss!0))/(rcmTenderer.exchangeRate!0)) ?string["#,##0.00"]}' },
    rcmBudget: { h: 'HK$90.00', f: '${rcmTenderer.currencyCode!""} ${(budgetTender.budgetAmount!0)?string["#,##0.00"]}' },
    rcmBuyLoss: { h: 'HK$10.00', f: '${rcmTenderer.currencyCode!""} ${(rcmTenderer.amtBuyingGainLoss!0)?string["#,##0.00"]}' },
    rcmTargetPercent: { h: '3', f: '[#if subcontract.amountPackageStretchTarget?? \\and subcontract.amountPackageStretchTarget > 0 \\and rcmTenderer.budgetAmount?? \\and rcmTenderer.budgetAmount > 0]${((1 - (subcontract.amountPackageStretchTarget / rcmTenderer.budgetAmount))  * 100)?string["0.##"]}[#else]&nbsp;[/#if]' },
    rcmTargetAmount: { h: '0', f: '${rcmTenderer.currencyCode!""} ${subcontract.amountPackageStretchTarget!0}' },
    rmkPrice: { h: 'Lump-Sum', f: '[#if subcontract.subcontractTerm =="Lump Sum"]Lump-Sum[#else]Remeasure[/#if]' },
    rmkCpf: { h: 'CPF Base Period: 17 CPF Base Year: 2019', f: '[#if subcontract.cpfCalculation?? \\and (subcontract.cpfCalculation == "1" || subcontract.cpfCalculation == "Subject to CPF")]CPF Base Period: ${subcontract.cpfBasePeriod!0} CPF Base Year: ${subcontract.cpfBaseYear!0}[#else]No CPF[/#if]' },
    rmkPaymentMethod: { h: 'IPS', f: '[#if subcontract.paymentMethod?? \\and subcontract.paymentMethod == "IPS"]IPS[#elseif subcontract.paymentMethod?? \\and subcontract.paymentMethod == "Work Done"]Work Done[#elseif subcontract.paymentMethod?? \\and subcontract.paymentMethod == "Milestone"]Milestone[#else]&nbsp;[/#if]' },
    rmkPeriod: { h: 'QS1 - Pay when Paid + 7 days', f: '[#assign paymentTermDesc = {"QS0":"Manual Input Due Date", "QS1":"Pay when Paid + 7 days", "QS2":"Pay when Paid + 14 days", "QS3":"Pay when IPA Received + 56 days", "QS4":"Pay when Invoice Received + 28 days", "QS5":"Pay when Invoice Received + 30 days", "QS6":"Pay when Invoice Received + 45 days", "QS7":"Pay when Invoice Received + 60 days", "QS8":"Pay when Paid + 0 day"} /]${subcontract.paymentTerms} - ${paymentTermDesc[subcontract.paymentTerms]!""}' },
    rmkTention: { h: 'Percentage - Revised SC Sum Max:5% Interm:5% MOS:5%', f: '[#if subcontract.retentionTerms?? \\and subcontract.retentionTerms?starts_with("Percentage")]${subcontract.retentionTerms} Max:${subcontract.maxRetentionPercentage}% Interm:${subcontract.interimRentionPercentage}% MOS:${subcontract.mosRetentionPercentage}%[#elseif subcontract.retentionTerms?? \\and subcontract.retentionTerms == "Lump Sum Amount Retention"]${subcontract.retentionAmount!0}[#else]No Retention[/#if]' },
    rmkDuration: { h: '2020-01-01 to 2020-02-02', f: '[#if subcontract.durationFrom??]${subcontract.durationFrom!"&nbsp;"} to ${subcontract.durationTo!"&nbsp;"}[#else]&nbsp;[/#if]' },
    preparedUser: { h: 'paulnpyiu', f: '${rcmTenderer.usernamePrepared!"&nbsp;"}' },
    preparedDate: { h: '2020-02-20', f: '${rcmTenderer.datePrepared!"&nbsp;"}' },
    preAward: { h: '&#9744;', f: '[#if subcontract.preAwardMeetingDate??]&#9745;[#else]&#9744;[/#if]'},
    threeQReason: { h: 'becasuse of ...', f: '${subcontract.reasonQuotation!"&nbsp;"}' },
    changeMainCertReason: { h: 'because of ...', f: '${subcontract.reasonManner!"&nbsp;"}' },
    changeMainCertExecution: { h: 'sth method', f: '${subcontract.executionMethodMainContract!"&nbsp;"}' },
    changeMainCertProposed: { h: 'sth propose', f: '${subcontract.executionMethodPropsed!"&nbsp;"}' },
    loaReason: { h: 'loa reason', f: '${subcontract.reasonLoa!"&nbsp;"}' },
    loaExecution: { h: '2020-02-02', f: '${subcontract.dateScExecutionTarget!"&nbsp;"}' },
    yesno3q: {h : 'Yes', f: '[#if tenderList?size lt 3]Yes[#else]No[/#if]' },
    yesnoVariance: {h : 'No', f: '[#if tenderVarianceList?size gt 0]Yes[#else]No[/#if]' },
    yesnoManner: {h : 'Yes', f: '[#if subcontract.reasonManner??]Yes[#else]No[/#if]' },
    yesnoLoa: {h : 'No', f: '[#if subcontract.reasonLoa??]Yes[#else]No[/#if]' },
    dateList: [
      { title: 'Subcontract Requisition Approved Date', h: '2020-01-01', f: '[#if subcontract.requisitionApprovedDate??]${subcontract.requisitionApprovedDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Subcontract Tender Analysis Approved Date', h: '2020-01-01', f: '[#if subcontract.tenderAnalysisApprovedDate??]${subcontract.tenderAnalysisApprovedDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Pre-Award Finalization Meeting Date', h: '2020-01-01', f: '[#if subcontract.preAwardMeetingDate??]${subcontract.preAwardMeetingDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Letter of Acceptance Signed by Subcontractor Date', h: '2020-01-01', f: '[#if subcontract.loaSignedDate??]${subcontract.loaSignedDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Subcontract Document Executed by Subcontractor Date', h: '2020-01-01', f: '[#if subcontract.scDocScrDate??]${subcontract.scDocScrDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Subcontract Document Executed by Legal Date', h: '2020-01-01', f: '[#if subcontract.scDocLegalDate??]${subcontract.scDocLegalDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Works Commencement Date', h: '2020-01-01', f: '[#if subcontract.workCommenceDate??]${subcontract.workCommenceDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
      { title: 'Subcontractor Start on-site Date', h: '2020-01-01', f: '[#if subcontract.onSiteStartDate??]${subcontract.onSiteStartDate?date?string("dd/MM/yyyy")}[#else]&nbsp;[/#if]' },
    ],
    approversList: [
      [
        { title: 'Reviewed by:', position: 'Managing Quantity Surveyor' },
        { title: '&nbsp;', position: 'Senior Project Manager' }
      ],
      [
        { title: 'Approved by:', position: 'Commercial Lead' },
        { title: '&nbsp;', position: 'Project-In-Charge' }
      ]
    ],
    varianceList: {
      h:
        [
          Object.assign({ isHeader: true }, this.varianceHeader),
          Object.assign({ isHeader: false }, this.varianceHeader),
          Object.assign({ isHeader: false }, this.varianceHeader)
        ],
      f:
        [
          Object.assign({ isHeader: true }, this.varianceHeader)
        ]
    },
    tenderList: {
      h:
        [
          Object.assign({ isHeader: true }, this.tenderHeader),
          Object.assign({ isHeader: false }, this.tenderHeader),
          Object.assign({ isHeader: false }, this.tenderHeader)
        ],
      f:
        [
          Object.assign({ isHeader: true }, this.tenderHeader)
        ]
    }
  }

  get(item: string): string {
    return this.v[item][this.mode];
  }

}
