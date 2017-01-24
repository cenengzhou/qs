--------------------------------------------------------
--  Alter Table - Column Name
--------------------------------------------------------
-- IDs
alter table PCMSDATAPROD.ATTACH_MAIN_CERT rename column MAINCERTIFICATE_ID to MAIN_CERT_ID;
alter table PCMSDATAPROD.ATTACH_PAYMENT rename column SCPAYMENTCERT_ID to PAYMENT_CERT_ID;
alter table PCMSDATAPROD.ATTACH_REPACKAGING rename column REPACKAGINGENTRY_ID to REPACKAGING_ID;
alter table PCMSDATAPROD.ATTACH_SUBCONTRACT_DETAIL rename column SCDETAILS_ID to SUBCONTRACT_DETAIL_ID;
alter table PCMSDATAPROD.ATTACH_SUBCONTRACT rename column SCPACKAGE_ID to SUBCONTRACT_ID;

alter table PCMSDATAPROD.BPI_BILL rename column JOB_ID to JOB_INFO_ID;
alter table PCMSDATAPROD.BPI_PAGE rename column BILL_ID to BPI_BILL_ID;
alter table PCMSDATAPROD.BPI_ITEM rename column PAGE_ID to BPI_PAGE_ID;
alter table PCMSDATAPROD.BPI_ITEM_RESOURCE rename column BQITEM_ID to BPI_ITEM_ID;

alter table PCMSDATAPROD.MAIN_CERT_CONTRA_CHARGE rename column MAINCERT_ID to MAIN_CERT_ID;

alter table PCMSDATAPROD.PAYMENT_CERT rename column SCPACKAGE_ID to SUBCONTRACT_ID;
alter table PCMSDATAPROD.PAYMENT_CERT_DETAIL rename column SCPAYMENTCERT_ID to PAYMENT_CERT_ID;
alter table PCMSDATAPROD.PAYMENT_CERT_DETAIL rename column SCDETAIL_ID to SUBCONTRACT_DETAIL_ID;
alter table PCMSDATAPROD.PROVISION_POSTING_HIST rename column SCDETAIL_ID to SUBCONTRACT_DETAIL_ID;

alter table PCMSDATAPROD.REPACKAGING rename column JOB_ID to JOB_INFO_ID;
alter table PCMSDATAPROD.REPACKAGING_DETAIL rename column REPACKAGINGENTRY_ID to REPACKAGING_ID;
alter table PCMSDATAPROD.RESOURCE_SUMMARY rename column JOB_ID to JOB_INFO_ID;
alter table PCMSDATAPROD.RESOURCE_SUMMARY_AUDIT_CUSTOM rename column REPACKAGINGENTRYID to REPACKAGING_ID;

alter table PCMSDATAPROD.SUBCONTRACT rename column JOB_ID to JOB_INFO_ID;
alter table PCMSDATAPROD.SUBCONTRACT_DETAIL rename column SCPACKAGE_ID to SUBCONTRACT_ID;
alter table PCMSDATAPROD.SUBCONTRACT_DETAIL rename column TENDER_ANALYSIS_DETAIL_ID to TENDER_ID;
alter table PCMSDATAPROD.SUBCONTRACT_SNAPSHOT rename column JOB_ID to JOB_INFO_ID;
alter table PCMSDATAPROD.SUBCONTRACT_SNAPSHOT rename column QS_SC_PACKAGE_ID to SUBCONTRACT_ID;
alter table PCMSDATAPROD.SUBCONTRACT_WORKSCOPE rename column SCPACKAGE_ID to SUBCONTRACT_ID;

alter table PCMSDATAPROD.TENDER rename column SCPACKAGE_ID to SUBCONTRACT_ID;
alter table PCMSDATAPROD.TENDER_DETAIL rename column  TENDERANALYSIS_ID to TENDER_ID;
alter table PCMSDATAPROD.TENDER_DETAIL rename column  FEEDBACKRATED to RATE_BUDGET;
alter table PCMSDATAPROD.TENDER_DETAIL rename column  FEEDBACKRATEF to RATE_SUBCONTRACT;

alter table PCMSDATAPROD.TRANSIT_BPI rename column TRANSITHEADER_ID to TRANSIT_ID;
alter table PCMSDATAPROD.TRANSIT_RESOURCE rename column TRANSITBQ_ID to TRANSIT_BPI_ID;

alter table PCMSDATAPROD.JDE_ACCOUNT_LEDGER rename column ENTRY_ID to ID;
