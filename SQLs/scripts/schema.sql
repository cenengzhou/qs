
    drop table qsdatadev.QS_AUDIT_RESOURCESUMMARY cascade constraints;

    drop table qsdatadev.QS_BQItem cascade constraints;

    drop table qsdatadev.QS_BQItem_Control cascade constraints;

    drop table qsdatadev.QS_BQRESOURCE_SUMMARY cascade constraints;

    drop table qsdatadev.QS_BQRESOURCE_SUMMARY_Control cascade constraints;

    drop table qsdatadev.QS_Bill cascade constraints;

    drop table qsdatadev.QS_FORECAST cascade constraints;

    drop table qsdatadev.QS_IVPOSTING_HISTORY cascade constraints;

    drop table qsdatadev.QS_JOB cascade constraints;

    drop table qsdatadev.QS_MAIN_CONTRACT_CERTIFICATE cascade constraints;

    drop table qsdatadev.QS_MAIN_CONTRA_CHARGE cascade constraints;

    drop table qsdatadev.QS_Page cascade constraints;

    drop table qsdatadev.QS_REPACKAGING_ATTACHMENT cascade constraints;

    drop table qsdatadev.QS_REPACKAGING_DETAIL cascade constraints;

    drop table qsdatadev.QS_REPACKAGING_ENTRY cascade constraints;

    drop table qsdatadev.QS_RESOURCE cascade constraints;

    drop table qsdatadev.QS_RESOURCE_CONTROL cascade constraints;

    drop table qsdatadev.QS_SCDETAIL_PROV_HIST cascade constraints;

    drop table qsdatadev.QS_SCPAYMENT_CERT cascade constraints;

    drop table qsdatadev.QS_SCPAYMENT_CERT_CONTROL cascade constraints;

    drop table qsdatadev.QS_SCPAYMENT_DETAIL cascade constraints;

    drop table qsdatadev.QS_SCPAYMENT_DETAIL_CONTROL cascade constraints;

    drop table qsdatadev.QS_SCPackage_Control cascade constraints;

    drop table qsdatadev.QS_SCWorkScope cascade constraints;

    drop table qsdatadev.QS_SC_ATTACHMENT cascade constraints;

    drop table qsdatadev.QS_SC_DETAILS cascade constraints;

    drop table qsdatadev.QS_SC_DETAILS_ATTACHMENT cascade constraints;

    drop table qsdatadev.QS_SC_DETAILS_CONTROL cascade constraints;

    drop table qsdatadev.QS_SC_PACKAGE cascade constraints;

    drop table qsdatadev.QS_SC_PAYMENT_ATTACHMENT cascade constraints;

    drop table qsdatadev.QS_SystemConstant cascade constraints;

    drop table qsdatadev.QS_TERM cascade constraints;

    drop table qsdatadev.QS_TERM_VARIANCES cascade constraints;

    drop table qsdatadev.QS_TenderAnalysis cascade constraints;

    drop table qsdatadev.QS_TenderAnalysisDetail cascade constraints;

    drop table qsdatadev.QS_TransitBQ cascade constraints;

    drop table qsdatadev.QS_TransitCodeMatch cascade constraints;

    drop table qsdatadev.QS_TransitHeader cascade constraints;

    drop table qsdatadev.QS_TransitResource cascade constraints;

    drop table qsdatadev.QS_TransitUomMatch cascade constraints;

    drop table qsadmin.QS_APP_ACTIVE_SESSION cascade constraints;

    drop table qsadmin.QS_APP_AUTHORITY cascade constraints;

    drop table qsadmin.QS_APP_JOB cascade constraints;

    drop table qsadmin.QS_APP_USER cascade constraints;

    drop table qsadmin.QS_LINK_JOB_USER cascade constraints;

    drop table qsadmin.QS_LINK_USER_AUTHORITY cascade constraints;

    drop table qsadmin.QS_SYSTEM_MESSAGE cascade constraints;

    drop table qsadmin.QS_USER_GENERAL_PREFERENCES cascade constraints;

    drop table qsadmin.QS_USER_SCREEN_PREFERENCES cascade constraints;

    drop table qsadmin.qrtz3_cron_triggers cascade constraints;

    drop table qsadmin.qrtz3_triggers cascade constraints;

    drop sequence qsadmin.app_active_session_seq;

    drop sequence qsadmin.app_authority_seq;

    drop sequence qsadmin.app_job_seq;

    drop sequence qsadmin.app_user_seq;

    drop sequence qsadmin.system_message_seq;

    drop sequence qsdatadev.QS_BQRESOURCE_SUMMARY_CTL_SEQ;

    drop sequence qsdatadev.QS_SC_PAYMENT_DETAIL_CTL_SEQ;

    drop sequence qsdatadev.main_contract_certificate_seq;

    drop sequence qsdatadev.qs_audit_resourcesummary_seq;

    drop sequence qsdatadev.qs_bill_seq;

    drop sequence qsdatadev.qs_bqitem_control_seq;

    drop sequence qsdatadev.qs_bqitem_seq;

    drop sequence qsdatadev.qs_bqresource_summary_seq;

    drop sequence qsdatadev.qs_forecast_seq;

    drop sequence qsdatadev.qs_ivposting_history_seq;

    drop sequence qsdatadev.qs_job_seq;

    drop sequence qsdatadev.qs_main_contra_charge_seq;

    drop sequence qsdatadev.qs_page_seq;

    drop sequence qsdatadev.qs_repackagingEntry_seq;

    drop sequence qsdatadev.qs_repackagingdetail_seq;

    drop sequence qsdatadev.qs_resource_control_seq;

    drop sequence qsdatadev.qs_resource_seq;

    drop sequence qsdatadev.qs_sc_details_control_seq;

    drop sequence qsdatadev.qs_sc_details_seq;

    drop sequence qsdatadev.qs_sc_package_control_seq;

    drop sequence qsdatadev.qs_sc_package_seq;

    drop sequence qsdatadev.qs_sc_payment_cert_control_seq;

    drop sequence qsdatadev.qs_sc_payment_cert_seq;

    drop sequence qsdatadev.qs_sc_workscope_seq;

    drop sequence qsdatadev.qs_tenderAnalysisDetail_seq;

    drop sequence qsdatadev.qs_tenderAnalysis_seq;

    drop sequence qsdatadev.qs_term_variances_seq;

    drop sequence qsdatadev.qs_transitBQ_seq;

    drop sequence qsdatadev.qs_transitCodeMatch_seq;

    drop sequence qsdatadev.qs_transitHeader_seq;

    drop sequence qsdatadev.qs_transitResource_seq;

    drop sequence qsdatadev.qs_transitUomMatch_seq;

    create table qsdatadev.QS_AUDIT_RESOURCESUMMARY (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        repackagingEntryId number(19,0),
        RESOURCE_SUMMARY_ID number(19,0),
        ACTION_TYPE varchar2(20 char),
        VALUE_TYPE varchar2(20 char),
        VALUE_FROM varchar2(255 char),
        VALUE_TO varchar2(255 char),
        primary key (ID)
    );

    create table qsdatadev.QS_BQItem (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        section varchar2(20 char),
        subSection varchar2(20 char),
        itemNo varchar2(20 char),
        sequenceNo number(10,0),
        sortDisplaySeqNo number(10,0),
        subsidiaryCode varchar2(8 char),
        bqType varchar2(20 char),
        description varchar2(2000 char),
        quantity double precision,
        remeasuredQty double precision,
        unit varchar2(10 char),
        sellingRate double precision,
        costRate double precision,
        genuineMarkupRate double precision,
        bqStatus varchar2(255 char),
        activityCode varchar2(12 char),
        certifiedPostedQty double precision,
        certifiedCumQty double precision,
        ipaGroup varchar2(12 char),
        ipaPostedQty double precision,
        ipaCumQty double precision,
        ivGroup varchar2(12 char),
        ivPostedQty double precision,
        ivCumQty double precision,
        JobNoref varchar2(12 char),
        BillNoref varchar2(20 char),
        SubBillNoref varchar2(20 char),
        SectionNoref varchar2(20 char),
        PageNoref varchar2(20 char),
        Page_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_BQItem_Control (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        beforePageId number(19,0),
        beforeSection varchar2(20 char),
        beforeSubSection varchar2(20 char),
        beforeItemNo varchar2(20 char),
        beforeSequenceNo number(10,0),
        beforeSortDisplaySeqNo number(10,0),
        beforeSubsidiaryCode varchar2(8 char),
        beforeBqType varchar2(20 char),
        beforeDescription varchar2(1000 char),
        beforeQuantity double precision,
        beforeRemeasuredQty double precision,
        beforeUnit varchar2(10 char),
        beforeSellingRate double precision,
        beforeCostRate double precision,
        beforeGenuineMarkupRate double precision,
        beforeBqStatus varchar2(10 char),
        beforeActivityCode varchar2(12 char),
        beforeCertifiedPostedQty double precision,
        beforeCertifiedCumQty double precision,
        beforeIpaGroup varchar2(12 char),
        beforeIpaPostedQty double precision,
        beforeIpaCumQty double precision,
        beforeIvGroup varchar2(12 char),
        beforeIvPostedQty double precision,
        beforeIvCumQty double precision,
        beforeJobNoref varchar2(12 char),
        beforeBillNoref varchar2(20 char),
        beforeSubBillNoref varchar2(20 char),
        beforeSectionNoref varchar2(20 char),
        beforePageNoref varchar2(20 char),
        afterPageId number(19,0),
        afterSection varchar2(20 char),
        afterSubSection varchar2(20 char),
        afterItemNo varchar2(20 char),
        afterSequenceNo number(10,0),
        afterSortDisplaySeqNo number(10,0),
        afterSubsidiaryCode varchar2(8 char),
        afterBqType varchar2(20 char),
        afterDescription varchar2(1000 char),
        afterQuantity double precision,
        afterRemeasuredQty double precision,
        afterUnit varchar2(10 char),
        afterSellingRate double precision,
        afterCostRate double precision,
        afterGenuineMarkupRate double precision,
        afterBqStatus varchar2(10 char),
        afterActivityCode varchar2(12 char),
        afterCertifiedPostedQty double precision,
        afterCertifiedCumQty double precision,
        afterIpaGroup varchar2(12 char),
        afterIpaPostedQty double precision,
        afterIpaCumQty double precision,
        afterIvGroup varchar2(12 char),
        afterIvPostedQty double precision,
        afterIvCumQty double precision,
        afterJobNoref varchar2(12 char),
        afterBillNoref varchar2(20 char),
        afterSubBillNoref varchar2(20 char),
        afterSectionNoref varchar2(20 char),
        afterPageNoref varchar2(20 char),
        primary key (ID)
    );

    create table qsdatadev.QS_BQRESOURCE_SUMMARY (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        subsidiaryCode varchar2(8 char),
        objectCode varchar2(6 char),
        description varchar2(255 char),
        UNIT varchar2(10 char),
        PACKAGENO varchar2(10 char),
        QUANTITY double precision,
        RATE double precision,
        ivPostedAmount double precision,
        ivCumAmount double precision,
        resourceType varchar2(10 char),
        excludeLevy number(1,0),
        excludeDefect number(1,0),
        forIvRollbackOnly number(1,0),
        JOB_ID number(19,0),
        SPLIT_FROM_ID number(19,0),
        MERGE_TO_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_BQRESOURCE_SUMMARY_Control (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        afterSubsidiaryCode varchar2(8 char),
        afterObjectCode varchar2(6 char),
        afterDescription varchar2(255 char),
        afterUNIT varchar2(10 char),
        afterPACKAGENO varchar2(10 char),
        afterQUANTITY double precision,
        afterRATE double precision,
        afterIvPostedAmount double precision,
        afterIvCumAmount double precision,
        afterResourceType varchar2(10 char),
        afterExcludeLevy number(1,0),
        afterExcludeDefect number(1,0),
        afterJobId number(19,0),
        afterplitFromId number(19,0),
        afterMergeToId number(19,0),
        beforeSubsidiaryCode varchar2(8 char),
        beforeObjectCode varchar2(6 char),
        beforeDescription varchar2(255 char),
        beforeUNIT varchar2(10 char),
        beforePACKAGENO varchar2(10 char),
        beforeQUANTITY double precision,
        beforeRATE double precision,
        beforeIvPostedAmount double precision,
        beforeIvCumAmount double precision,
        beforeResourceType varchar2(10 char),
        beforeExcludeLevy number(1,0),
        beforeExcludeDefect number(1,0),
        beforeJobId number(19,0),
        beforeSplitFromId number(19,0),
        beforeMergeToId number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_Bill (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp,
        CREATED_USER varchar2(50 char),
        SYSTEM_STATUS varchar2(20 char),
        billNo varchar2(20 char),
        description varchar2(255 char),
        subBillNo varchar2(20 char),
        sectionNo varchar2(20 char),
        Job_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_FORECAST (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        JOBNO varchar2(12 char),
        ASOFDATE timestamp,
        FORECASTYEAR number(10,0),
        SUBSIDIARYCODE varchar2(8 char),
        OBJECTCODE varchar2(6 char),
        CATEGORY varchar2(10 char),
        DESCRIPTION varchar2(255 char),
        RESOURCETYPE varchar2(10 char),
        PACKAGENO varchar2(10 char),
        PERIOD01AMOUNT double precision,
        PERIOD02AMOUNT double precision,
        PERIOD03AMOUNT double precision,
        PERIOD04AMOUNT double precision,
        PERIOD05AMOUNT double precision,
        PERIOD06AMOUNT double precision,
        PERIOD07AMOUNT double precision,
        PERIOD08AMOUNT double precision,
        PERIOD09AMOUNT double precision,
        PERIOD10AMOUNT double precision,
        PERIOD11AMOUNT double precision,
        PERIOD12AMOUNT double precision,
        LINKID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_IVPOSTING_HISTORY (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        RESOURCE_SUMMARY_ID number(19,0),
        JOBNO varchar2(12 char),
        subsidiaryCode varchar2(8 char),
        objectCode varchar2(6 char),
        DESCRIPTION varchar2(255 char),
        UNIT varchar2(3 char),
        PACKAGENO varchar2(10 char),
        RATE double precision,
        IVMOVEMENTAMOUNT double precision,
        DOCUMENTNO number(10,0),
        EDIBATCHNO number(10,0),
        primary key (ID)
    );

    create table qsdatadev.QS_JOB (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        jobNo varchar2(12 char),
        description varchar2(255 char),
        company varchar2(12 char),
        employer varchar2(12 char),
        contractType varchar2(10 char),
        division varchar2(10 char),
        department varchar2(10 char),
        internalJob varchar2(3 char),
        soloJV varchar2(4 char),
        completionStatus varchar2(1 char),
        insuranceCAR varchar2(10 char),
        insuranceECI varchar2(10 char),
        insuranceTPL varchar2(10 char),
        clientContractNo varchar2(20 char),
        parentJobNo varchar2(12 char),
        jvPartnerNo varchar2(12 char),
        jvPercentage double precision,
        originalContractValue double precision,
        projectedContractValue double precision,
        orginalNSCContractValue double precision,
        tenderGP double precision,
        forecastEndYear number(10,0),
        forecastEndPeriod number(10,0),
        maxRetPercent double precision,
        interimRetPercent double precision,
        mosRetPercent double precision,
        valueOfBSWork double precision,
        grossFloorArea double precision,
        grossFloorAreaUnit varchar2(10 char),
        billingCurrency varchar2(10 char),
        paymentTermForNSC varchar2(10 char),
        defectProvisionPercent double precision,
        cpfApplicable varchar2(3 char),
        cpfIndexName varchar2(12 char),
        cpfBaseYear number(10,0),
        cpfBasePeriod number(10,0),
        levyApplicable varchar2(3 char),
        levyCITAPercent double precision,
        levyPCFBPercent double precision,
        expectedPCCDate timestamp,
        actualPCCDate timestamp,
        expectedMakingGoodDate timestamp,
        actualMakingGoodDate timestamp,
        defectLiabilityPeriod number(10,0),
        defectListIssuedDate timestamp,
        financialEndDate timestamp,
        dateFinalACSettlement timestamp,
        yearOfCompletion number(10,0),
        bqFinalizedFlag varchar2(1 char),
        manualInputSCWD varchar2(1 char),
        legacyJob varchar2(1 char),
        conversionStatus varchar2(3 char),
        repackagingType varchar2(1 char),
        primary key (ID)
    );

    create table qsdatadev.QS_MAIN_CONTRACT_CERTIFICATE (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        JOBNO varchar2(12 char),
        certNo number(10,0),
        appMainContractorAmt double precision,
        appNSCNDSCAmt double precision,
        appMOSAmt double precision,
        appMainContractorRet double precision,
        appMainContractorRetRel double precision,
        appRetForNSCNDSC double precision,
        appRetRelForNSCNDSC double precision,
        appMOSRet double precision,
        appMOSRetRel double precision,
        appCCAmt double precision,
        appAdjustmentAmt double precision,
        appAdvancePayment double precision,
        appCPFAmt double precision,
        certMainContractorAmt double precision,
        certNSCNDSCAmt double precision,
        certMOSAmt double precision,
        certMainContractorRet double precision,
        certMainContractorRetRel double precision,
        certRetForNSCNDSC double precision,
        certRetRelForNSCNDSC double precision,
        certMOSRet double precision,
        certMOSRetRel double precision,
        certCCAmt double precision,
        certAdjustmentAmt double precision,
        certAdvancePayment double precision,
        certCPFAmt double precision,
        GSTRECEIVABLE double precision,
        GSTPAYABLE double precision,
        CERTIFICATESTATUS varchar2(10 char),
        arDocumentNo number(10,0),
        IPADATE timestamp,
        IPASENTOUTDATE timestamp,
        CERTIFIEDDATE timestamp,
        ASATDATE timestamp,
        certStatuschangeDate timestamp,
        certDueDate timestamp,
        primary key (ID)
    );

    create table qsdatadev.QS_MAIN_CONTRA_CHARGE (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        PostedAmount double precision,
        CumAmount double precision,
        ObjectCode varchar2(6 char),
        SubsidiaryCode varchar2(8 char),
        mainCert_id number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_Page (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp,
        CREATED_USER varchar2(50 char),
        SYSTEM_STATUS varchar2(20 char),
        pageNo varchar2(20 char),
        description varchar2(255 char),
        Bill_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_REPACKAGING_ATTACHMENT (
        repackagingEntry_ID number(19,0) not null,
        sequenceNo number(10,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        documentType number(10,0),
        textAttachment varchar2(2000 char),
        primary key (repackagingEntry_ID, sequenceNo)
    );

    create table qsdatadev.QS_REPACKAGING_DETAIL (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        packageNo varchar2(10 char),
        objectCode varchar2(6 char),
        subsidiaryCode varchar2(8 char),
        description varchar2(255 char),
        UNIT varchar2(3 char),
        QUANTITY double precision,
        RATE double precision,
        AMOUNT double precision,
        resourceType varchar2(10 char),
        excludeLevy number(1,0),
        excludeDefect number(1,0),
        resourceSummaryId number(19,0),
        RepackagingEntry_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_REPACKAGING_ENTRY (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        CREATEDATE timestamp,
        totalResAllowance double precision,
        STATUS varchar2(3 char),
        REMARKS varchar2(500 char),
        job_id number(19,0),
        REPACKAGINGVERSION number(10,0),
        primary key (ID),
        unique (job_id, REPACKAGINGVERSION)
    );

    create table qsdatadev.QS_RESOURCE (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        resourceNo number(10,0),
        subsidiaryCode varchar2(8 char),
        objectCode varchar2(6 char),
        resourceType varchar2(10 char),
        description varchar2(255 char),
        quantity double precision,
        remeasuredFactor double precision,
        unit varchar2(10 char),
        costRate double precision,
        materialWastage double precision,
        packageNo varchar2(10 char),
        packageNature varchar2(10 char),
        packageStatus varchar2(10 char),
        packageType varchar2(10 char),
        splitStatus varchar2(10 char),
        ivPostedQty double precision,
        ivCumQty double precision,
        ivPostedAmount double precision,
        ivMovementAmount double precision,
        jobNoRef varchar2(12 char),
        billNoRef varchar2(20 char),
        subBillNoRef varchar2(20 char),
        sectionNoRef varchar2(20 char),
        pageNoRef varchar2(20 char),
        itemNoRef varchar2(20 char),
        bqItem_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_RESOURCE_CONTROL (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        afterBqItemId number(19,0),
        afterResourceNo number(10,0),
        afterSubsidiaryCode varchar2(8 char),
        afterObjectCode varchar2(6 char),
        afterResourceType varchar2(10 char),
        afterDescription varchar2(255 char),
        afterQuantity double precision,
        afterRemeasuredFactor double precision,
        afterUnit varchar2(10 char),
        afterCostRate double precision,
        afterMaterialWastage double precision,
        afterPackageNo varchar2(10 char),
        afterPackageNature varchar2(10 char),
        afterPackageStatus varchar2(10 char),
        afterPackageType varchar2(10 char),
        afterSplitStatus varchar2(10 char),
        afterIvPostedQty double precision,
        afterIvCumQty double precision,
        afterIvPostedAmount double precision,
        afterIvMovementAmount double precision,
        afterJobNoRef varchar2(12 char),
        afterBillNoRef varchar2(20 char),
        afterSubBillNoRef varchar2(20 char),
        afterSectionNoRef varchar2(20 char),
        afterPageNoRef varchar2(20 char),
        afterItemNoRef varchar2(20 char),
        beforeBqItemId number(19,0),
        beforeResourceNo number(10,0),
        beforeSubsidiaryCode varchar2(8 char),
        beforeObjectCode varchar2(6 char),
        beforeResourceType varchar2(10 char),
        beforeDescription varchar2(255 char),
        beforeQuantity double precision,
        beforeRemeasuredFactor double precision,
        beforeUnit varchar2(10 char),
        beforeCostRate double precision,
        beforeMaterialWastage double precision,
        beforePackageNo varchar2(10 char),
        beforePackageNature varchar2(10 char),
        beforePackageStatus varchar2(10 char),
        beforePackageType varchar2(10 char),
        beforeSplitStatus varchar2(10 char),
        beforeIvPostedQty double precision,
        beforeIvCumQty double precision,
        beforeIvPostedAmount double precision,
        beforeIvMovementAmount double precision,
        beforeJobNoRef varchar2(12 char),
        beforeBillNoRef varchar2(20 char),
        beforeSubBillNoRef varchar2(20 char),
        beforeSectionNoRef varchar2(20 char),
        beforePageNoRef varchar2(20 char),
        beforeItemNoRef varchar2(20 char),
        primary key (ID)
    );

    create table qsdatadev.QS_SCDETAIL_PROV_HIST (
        scDetail_ID number(19,0) not null,
        postedYr number(10,0) not null,
        postedMonth number(10,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        objectCode varchar2(6 char) not null,
        subsidiaryCode varchar2(8 char) not null,
        certPostedAmount double precision,
        liabilitiesCumAmount double precision,
        certPostedQty double precision,
        liabilitiesCumQty double precision,
        scRate double precision,
        jobNo varchar2(12 char),
        packageNo varchar2(10 char),
        primary key (scDetail_ID, postedYr, postedMonth)
    );

    create table qsdatadev.QS_SCPAYMENT_CERT (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        paymentCertNo number(10,0),
        paymentStatus varchar2(3 char),
        mainContractCertNo number(10,0),
        dueDate timestamp,
        asAtDate timestamp,
        scIpaReceivedDate timestamp,
        certIssueDate timestamp,
        certAmount double precision,
        addendumAmount double precision,
        paymentType varchar2(5 char),
        remeasuredSCSum double precision,
        jobNo varchar2(12 char),
        packageNo varchar2(10 char),
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_SCPAYMENT_CERT_CONTROL (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        afterScPackageId number(19,0),
        afterPaymentCertNo number(10,0),
        afterPaymentStatus varchar2(3 char),
        afterMainContractCertNo number(10,0),
        afterDueDate timestamp,
        afterAsAtDate timestamp,
        afterScIpaReceivedDate timestamp,
        afterCertIssueDate timestamp,
        afterCertAmount double precision,
        afterAddendumAmount double precision,
        afterPaymentType varchar2(5 char),
        afterRemeasuredSCSum double precision,
        afterJobNo varchar2(12 char),
        afterPackageNo varchar2(10 char),
        beforeScPackageId number(19,0),
        beforePaymentCertNo number(10,0),
        beforePaymentStatus varchar2(3 char),
        beforeMainContractCertNo number(10,0),
        beforeDueDate timestamp,
        beforeAsAtDate timestamp,
        beforeScIpaReceivedDate timestamp,
        beforeCertIssueDate timestamp,
        beforeCertAmount double precision,
        beforeAddendumAmount double precision,
        beforePaymentType varchar2(5 char),
        beforeRemeasuredSCSum double precision,
        beforeJobNo varchar2(12 char),
        beforePackageNo varchar2(10 char),
        primary key (ID)
    );

    create table qsdatadev.QS_SCPAYMENT_DETAIL (
        scPaymentCert_ID number(19,0) not null,
        scSeqNo number(10,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        paymentCertNo varchar2(5 char),
        lineType varchar2(10 char),
        billItem varchar2(110 char),
        movementAmount double precision,
        cumAmount double precision,
        objectCode varchar2(6 char),
        subsidiaryCode varchar2(8 char),
        scDetail_ID number(19,0),
        primary key (scPaymentCert_ID, scSeqNo)
    );

    create table qsdatadev.QS_SCPAYMENT_DETAIL_CONTROL (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        afterScPaymentCertId number(19,0),
        afterScDetailsId number(19,0),
        afterScSeqNo number(10,0),
        afterPaymentCertNo varchar2(5 char),
        afterLineType varchar2(10 char),
        afterBillItem varchar2(110 char),
        afterMovementAmount double precision,
        afterCumAmount double precision,
        afterObjectCode varchar2(6 char),
        afterSubsidiaryCode varchar2(8 char),
        beforeScPaymentCertId number(19,0),
        beforeScDetailsId number(19,0),
        beforeScSeqNo number(10,0),
        beforePaymentCertNo varchar2(5 char),
        beforeLineType varchar2(10 char),
        beforeBillItem varchar2(110 char),
        beforeMovementAmount double precision,
        beforeCumAmount double precision,
        beforeObjectCode varchar2(6 char),
        beforeSubsidiaryCode varchar2(8 char),
        primary key (ID)
    );

    create table qsdatadev.QS_SCPackage_Control (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        afterJobId number(19,0),
        afterPackageNo varchar2(10 char),
        afterDescription varchar2(255 char),
        afterPackageType varchar2(4 char),
        afterVendorNo varchar2(20 char),
        afterPackageStatus varchar2(3 char),
        afterScStatus number(10,0),
        afterScNature varchar2(4 char),
        afterOriginalSCSum double precision,
        afterApprovedVOAmount double precision,
        afterRemeasuredSCSum double precision,
        afterApprovalRoute varchar2(5 char),
        afterRetentionTerms varchar2(30 char),
        afterMaxRetPercent double precision,
        afterInterimRetPerent double precision,
        afterMosRetPerent double precision,
        afterRetAmount double precision,
        afterAccumlatedRet double precision,
        afterRetRelease double precision,
        afterPaymentInfo varchar2(50 char),
        afterPaymentCurrency varchar2(3 char),
        afterExchangeRate double precision,
        afterPaymentTerms varchar2(3 char),
        afterScTerm varchar2(15 char),
        afterCpfCalculation varchar2(20 char),
        afterCpfBasePeriod number(10,0),
        afterCpfBaseYear number(10,0),
        afterFormOfSubcontract varchar2(22 char),
        afterInternalJobNo varchar2(12 char),
        afterPaymentStatus varchar2(1 char),
        afterSubmittedAddendum varchar2(1 char),
        afterLabourIncluded number(1,0),
        afterPlantIncluded number(1,0),
        afterMaterialIncluded number(1,0),
        afterSplitTerminateStatus varchar2(1 char),
        afterScCreatedDate timestamp,
        afterRebidRequestSentDate timestamp,
        afterLastPerformAppDate timestamp,
        afterLatestAddenUpdatedDate timestamp,
        afterTenderDocIssuedDate timestamp,
        afterTenderDocReceivedDate timestamp,
        afterFirstPaymentIssuedDate timestamp,
        afterLastPaymentIssuedDate timestamp,
        afterFinalPaymentIssuedDate timestamp,
        afterScAwardRequestSentDate timestamp,
        afterScApprovalDate timestamp,
        afterLoaPrintedDate timestamp,
        afterLoaIssuedDate timestamp,
        afterLoaSignedDate timestamp,
        afterScPrintedDate timestamp,
        afterScIssuedDate timestamp,
        afterScSignedDate timestamp,
        afterGclSignedDate timestamp,
        afterScDistributionDate timestamp,
        afterTotalPostedWDAmt double precision,
        afterTotalCumWDAmt double precision,
        afterTotalPostedCertAmt double precision,
        afterTotalCumCertAmt double precision,
        afterTotalCCPostedCertAmt double precision,
        afterTotalMOSPostedCertAmt double precision,
        beforeJobId number(19,0),
        beforePackageNo varchar2(10 char),
        beforeDescription varchar2(255 char),
        beforePackageType varchar2(4 char),
        beforeVendorNo varchar2(20 char),
        beforePackageStatus varchar2(3 char),
        beforeScStatus number(10,0),
        beforeScNature varchar2(4 char),
        beforeOriginalSCSum double precision,
        beforeApprovedVOAmount double precision,
        beforeRemeasuredSCSum double precision,
        beforeApprovalRoute varchar2(5 char),
        beforeRetentionTerms varchar2(30 char),
        beforeMaxRetPercent double precision,
        beforeInterimRetPerent double precision,
        beforeMosRetPerent double precision,
        beforeRetAmount double precision,
        beforeAccumlatedRet double precision,
        beforeRetRelease double precision,
        beforePaymentInfo varchar2(50 char),
        beforePaymentCurrency varchar2(3 char),
        beforeExchangeRate double precision,
        beforePaymentTerms varchar2(3 char),
        beforeScTerm varchar2(15 char),
        beforeCpfCalculation varchar2(20 char),
        beforeCpfBasePeriod number(10,0),
        beforeCpfBaseYear number(10,0),
        beforeFormOfSubcontract varchar2(22 char),
        beforeInternalJobNo varchar2(12 char),
        beforePaymentStatus varchar2(1 char),
        beforeSubmittedAddendum varchar2(1 char),
        beforeLabourIncluded number(1,0),
        beforePlantIncluded number(1,0),
        beforeMaterialIncluded number(1,0),
        beforeSplitTerminateStatus varchar2(1 char),
        beforeScCreatedDate timestamp,
        beforeRebidRequestSentDate timestamp,
        beforeLastPerformAppDate timestamp,
        beforeLatestAddenUpdatedDate timestamp,
        beforeTenderDocIssuedDate timestamp,
        beforeTenderDocReceivedDate timestamp,
        beforeFirstPaymentIssuedDate timestamp,
        beforeLastPaymentIssuedDate timestamp,
        beforeFinalPaymentIssuedDate timestamp,
        beforeScAwardRequestSentDate timestamp,
        beforeScApprovalDate timestamp,
        beforeLoaPrintedDate timestamp,
        beforeLoaIssuedDate timestamp,
        beforeLoaSignedDate timestamp,
        beforeScPrintedDate timestamp,
        beforeScIssuedDate timestamp,
        beforeScSignedDate timestamp,
        beforeGclSignedDate timestamp,
        beforeScDistributionDate timestamp,
        beforeTotalPostedWDAmt double precision,
        beforeTotalCumWDAmt double precision,
        beforeTotalPostedCertAmt double precision,
        beforeTotalCumCertAmt double precision,
        beforeTotalCCPostedCertAmt double precision,
        beforeTotalMOSPostedCertAmt double precision,
        primary key (ID)
    );

    create table qsdatadev.QS_SCWorkScope (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        workscope varchar2(10 char),
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_SC_ATTACHMENT (
        SCPackage_ID number(19,0) not null,
        sequenceNo number(10,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        documentType number(10,0),
        textAttachment varchar2(2000 char),
        primary key (SCPackage_ID, sequenceNo)
    );

    create table qsdatadev.QS_SC_DETAILS (
        ID number(19,0) not null,
        TYPE varchar2(2 char) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        jobNo varchar2(12 char),
        sequenceNo number(10,0),
        resourceNo number(10,0),
        billItem varchar2(110 char),
        description varchar2(255 char),
        quantity double precision,
        scRate double precision,
        objectCode varchar2(6 char),
        subsidiaryCode varchar2(8 char),
        lineType varchar2(10 char),
        approved varchar2(1 char),
        unit varchar2(10 char),
        scRemark varchar2(255 char),
        postedCertQty double precision,
        cumCertQty double precision,
        originalQty double precision,
        scPackage_ID number(19,0),
        costRate double precision,
        postedWDQty double precision,
        cumWDQty double precision,
        toBeApprovedQty double precision,
        newQty double precision,
        toBeApprovedRate double precision,
        contraChargeSCNo varchar2(10 char),
        altObjectCode varchar2(6 char),
        primary key (ID)
    );

    create table qsdatadev.QS_SC_DETAILS_ATTACHMENT (
        scDetails_ID number(19,0) not null,
        sequenceNo number(10,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        documentType number(10,0),
        textAttachment varchar2(2000 char),
        primary key (scDetails_ID, sequenceNo)
    );

    create table qsdatadev.QS_SC_DETAILS_CONTROL (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        actionType varchar2(10 char),
        afterScPackageId number(19,0),
        afterJobNo varchar2(12 char),
        afterSequenceNo number(10,0),
        afterResourceNo number(10,0),
        afterBillItem varchar2(110 char),
        afterDescription varchar2(255 char),
        afterQuantity double precision,
        afterScRate double precision,
        afterObjectCode varchar2(6 char),
        afterSubsidiaryCode varchar2(8 char),
        afterLineType varchar2(10 char),
        afterApproved varchar2(1 char),
        afterUnit varchar2(10 char),
        afterScRemark varchar2(255 char),
        afterPostedCertQty double precision,
        afterCumCertQty double precision,
        afterOriginalQty double precision,
        afterCostRate double precision,
        afterPostedWDQty double precision,
        afterCumWDQty double precision,
        afterToBeApprovedQty double precision,
        afterNewQty double precision,
        afterToBeApprovedRate double precision,
        afterContraChargeSCNo varchar2(10 char),
        afterAltObjectCode varchar2(6 char),
        beforeScPackageId number(19,0),
        beforeJobNo varchar2(12 char),
        beforeSequenceNo number(10,0),
        beforeResourceNo number(10,0),
        beforeBillItem varchar2(110 char),
        beforeDescription varchar2(255 char),
        beforeQuantity double precision,
        beforeScRate double precision,
        beforeObjectCode varchar2(6 char),
        beforeSubsidiaryCode varchar2(8 char),
        beforeLineType varchar2(10 char),
        beforeApproved varchar2(1 char),
        beforeUnit varchar2(10 char),
        beforeScRemark varchar2(255 char),
        beforePostedCertQty double precision,
        beforeCumCertQty double precision,
        beforeOriginalQty double precision,
        beforeCostRate double precision,
        beforePostedWDQty double precision,
        beforeCumWDQty double precision,
        beforeToBeApprovedQty double precision,
        beforeNewQty double precision,
        beforeToBeApprovedRate double precision,
        beforeContraChargeSCNo varchar2(10 char),
        beforeAltObjectCode varchar2(6 char),
        primary key (ID)
    );

    create table qsdatadev.QS_SC_PACKAGE (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        job_ID number(19,0),
        packageNo varchar2(10 char),
        description varchar2(255 char),
        packageType varchar2(4 char),
        vendorNo varchar2(255 char),
        packageStatus varchar2(3 char),
        scStatus number(10,0),
        scNature varchar2(4 char),
        originalSCSum double precision,
        approvedVOAmount double precision,
        remeasuredSCSum double precision,
        approvalRoute varchar2(5 char),
        retentionTerms varchar2(30 char),
        maxRetPercent double precision,
        interimRetPerent double precision,
        mosRetPerent double precision,
        retAmount double precision,
        accumlatedRet double precision,
        retRelease double precision,
        paymentInfo varchar2(50 char),
        paymentCurrency varchar2(3 char),
        exchangeRate double precision,
        paymentTerms varchar2(3 char),
        scTerm varchar2(15 char),
        cpfCalculation varchar2(20 char),
        cpfBasePeriod number(10,0),
        cpfBaseYear number(10,0),
        formOfSubcontract varchar2(22 char),
        internalJobNo varchar2(12 char),
        paymentStatus varchar2(1 char),
        submittedAddendum varchar2(1 char),
        labourIncluded number(1,0),
        plantIncluded number(1,0),
        materialIncluded number(1,0),
        splitTerminateStatus varchar2(1 char),
        scCreatedDate timestamp,
        rebidRequestSentDate timestamp,
        lastPerformanceAppraisalDate timestamp,
        latestAddendumValueUpdatedDate timestamp,
        tenderEnquiryDocIssuedDate timestamp,
        tenderEnquiryDocReceivedDate timestamp,
        firstPaymentCertIssuedDate timestamp,
        lastPaymentCertIssuedDate timestamp,
        finalPaymentIssuedDate timestamp,
        scAwardApprovalRequestSentDate timestamp,
        scApprovalDate timestamp,
        loaPrintedDate timestamp,
        loaIssuedDate timestamp,
        loaSignedDate timestamp,
        scPrintedDate timestamp,
        scIssuedDate timestamp,
        scSignedDate timestamp,
        gclSignedDate timestamp,
        scDistributionDate timestamp,
        totalPostedCertAmt double precision,
        totalCumCertAmt double precision,
        totalPostedWDAmt double precision,
        totalCumWDAmt double precision,
        totalCCPostedCertAmt double precision,
        totalMOSPostedCertAmt double precision,
        primary key (ID)
    );

    create table qsdatadev.QS_SC_PAYMENT_ATTACHMENT (
        scPaymentCert_ID number(19,0) not null,
        sequenceNo number(10,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        documentType number(10,0),
        textAttachment varchar2(2000 char),
        primary key (scPaymentCert_ID, sequenceNo)
    );

    create table qsdatadev.QS_SystemConstant (
        system_Code varchar2(2 char) not null,
        company varchar2(5 char) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        scPaymentTerm varchar2(10 char),
        scMaxRetPercent double precision,
        scInterimRetPercent double precision,
        scMOSRetPercent double precision,
        RetentionType varchar2(30 char),
        primary key (system_Code, company)
    );

    create table qsdatadev.QS_TERM (
        TERMCODE varchar2(10 char) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        termDescription varchar2(255 char),
        generalCondition varchar2(255 char),
        primary key (TERMCODE)
    );

    create table qsdatadev.QS_TERM_VARIANCES (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        varianceDescription varchar2(255 char),
        varianceReason varchar2(255 char),
        scPackage_ID number(19,0),
        termCode varchar2(10 char),
        primary key (ID)
    );

    create table qsdatadev.QS_TenderAnalysis (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        vendorNo number(10,0),
        status varchar2(10 char),
        currencyCode varchar2(10 char),
        exchangeRate double precision,
        budgetAmount double precision,
        jobNo varchar2(12 char),
        packageNo varchar2(10 char),
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_TenderAnalysisDetail (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        sequenceNo number(10,0),
        resourceNo number(10,0),
        billItem varchar2(110 char),
        description varchar2(255 char),
        quantity double precision,
        feedbackRateD double precision,
        feedbackRateF double precision,
        objectCode varchar2(6 char),
        subsidiaryCode varchar2(8 char),
        lineType varchar2(10 char),
        unit varchar2(10 char),
        remark varchar2(255 char),
        tenderAnalysis_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_TransitBQ (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        sequenceNo number(10,0),
        billNo varchar2(20 char),
        subBillNo varchar2(20 char),
        pageNo varchar2(20 char),
        itemNo varchar2(20 char),
        description varchar2(1000 char),
        quantity double precision,
        unit varchar2(10 char),
        sellingRate double precision,
        costRate double precision,
        value double precision,
        transitHeader_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_TransitCodeMatch (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        matchingType varchar2(2 char),
        resourceCode varchar2(10 char),
        objectCode varchar2(6 char),
        subsidiaryCode varchar2(8 char),
        primary key (ID)
    );

    create table qsdatadev.QS_TransitHeader (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        jobNumber varchar2(12 char),
        estimateNo varchar2(12 char),
        jobDescription varchar2(255 char),
        company varchar2(10 char),
        status varchar2(30 char),
        matchingCode varchar2(10 char),
        primary key (ID)
    );

    create table qsdatadev.QS_TransitResource (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        resourceNo number(10,0),
        resourceCode varchar2(10 char),
        type varchar2(10 char),
        description varchar2(255 char),
        waste double precision,
        totalQuantity double precision,
        unit varchar2(10 char),
        rate double precision,
        value double precision,
        objectCode varchar2(6 char),
        subsidiaryCode varchar2(8 char),
        packageNo varchar2(5 char),
        transitBQ_ID number(19,0),
        primary key (ID)
    );

    create table qsdatadev.QS_TransitUomMatch (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        causewayUom varchar2(10 char),
        jdeUom varchar2(2 char),
        primary key (ID)
    );

    create table qsadmin.QS_APP_ACTIVE_SESSION (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        USER_ID number(19,0) not null,
        REMOTE_ADDR varchar2(255 char) not null,
        HOST_NAME varchar2(255 char) not null,
        LOGIN_DATETIME timestamp not null,
        LAST_ACCESS_DATETIME timestamp not null,
        SESSION_ID varchar2(255 char) not null,
        primary key (ID)
    );

    create table qsadmin.QS_APP_AUTHORITY (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    );

    create table qsadmin.QS_APP_JOB (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        DESCRIPTION varchar2(512 char),
        JOB_NUMBER varchar2(255 char) not null,
        primary key (ID)
    );

    create table qsadmin.QS_APP_USER (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        USERNAME varchar2(255 char) not null,
        FULLNAME varchar2(255 char),
        primary key (ID)
    );

    create table qsadmin.QS_LINK_JOB_USER (
        JOB_ID number(19,0) not null,
        USER_ID number(19,0) not null,
        primary key (JOB_ID, USER_ID)
    );

    create table qsadmin.QS_LINK_USER_AUTHORITY (
        USER_ID number(19,0) not null,
        AUTHORITY_ID number(19,0) not null,
        primary key (USER_ID, AUTHORITY_ID)
    );

    create table qsadmin.QS_SYSTEM_MESSAGE (
        ID number(19,0) not null,
        LAST_MODIFIED_DATE timestamp not null,
        LAST_MODIFIED_USER varchar2(50 char),
        CREATED_DATE timestamp not null,
        CREATED_USER varchar2(50 char) not null,
        SYSTEM_STATUS varchar2(20 char) not null,
        USERNAME varchar2(255 char) not null,
        MESSAGE varchar2(255 char),
        SCHEDULED_DATE timestamp,
        primary key (ID)
    );

    create table qsadmin.QS_USER_GENERAL_PREFERENCES (
        USER_ID number(19,0) not null,
        PREFERENCE_VALUE varchar2(256 char),
        PREFERENCE_KEY varchar2(255 char) not null,
        primary key (USER_ID, PREFERENCE_KEY)
    );

    create table qsadmin.QS_USER_SCREEN_PREFERENCES (
        USER_ID number(19,0) not null,
        PREFERENCE varchar2(1024 char) not null,
        SCREEN_NAME varchar2(255 char) not null,
        primary key (USER_ID, SCREEN_NAME)
    );

    create table qsadmin.qrtz3_cron_triggers (
        TRIGGER_NAME varchar2(200 char) not null,
        TRIGGER_GROUP varchar2(200 char) not null,
        CRON_EXPRESSION varchar2(120 char),
        primary key (TRIGGER_NAME, TRIGGER_GROUP)
    );

    create table qsadmin.qrtz3_triggers (
        TRIGGER_NAME varchar2(200 char) not null,
        TRIGGER_GROUP varchar2(200 char),
        JOB_NAME varchar2(200 char),
        JOB_GROUP varchar2(200 char),
        IS_VOLATILE varchar2(1 char),
        DESCRIPTION varchar2(250 char),
        NEXT_FIRE_TIME number(19,0),
        PREV_FIRE_TIME number(19,0),
        PRIORITY number(19,0),
        TRIGGER_STATE varchar2(16 char),
        TRIGGER_TYPE varchar2(8 char),
        START_TIME number(19,0),
        END_TIME number(19,0),
        CALENDAR_NAME varchar2(200 char),
        MISFIRE_INSTR number(19,0),
        primary key (TRIGGER_NAME)
    );

    alter table qsdatadev.QS_BQItem 
        add constraint FK_BQItemPage_PK 
        foreign key (Page_ID) 
        references qsdatadev.QS_Page;

    alter table qsdatadev.QS_BQRESOURCE_SUMMARY 
        add constraint FK_BQRS_SF 
        foreign key (SPLIT_FROM_ID) 
        references qsdatadev.QS_BQRESOURCE_SUMMARY;

    alter table qsdatadev.QS_BQRESOURCE_SUMMARY 
        add constraint FK_BQRS_MT 
        foreign key (MERGE_TO_ID) 
        references qsdatadev.QS_BQRESOURCE_SUMMARY;

    alter table qsdatadev.QS_BQRESOURCE_SUMMARY 
        add constraint FK_BQResourSumJOB_PK 
        foreign key (JOB_ID) 
        references qsdatadev.QS_JOB;

    alter table qsdatadev.QS_Bill 
        add constraint FK_BillJob_PK 
        foreign key (Job_ID) 
        references qsdatadev.QS_JOB;

    alter table qsdatadev.QS_MAIN_CONTRA_CHARGE 
        add constraint PK_MainCert_CC_FK 
        foreign key (mainCert_id) 
        references qsdatadev.QS_MAIN_CONTRACT_CERTIFICATE;

    alter table qsdatadev.QS_Page 
        add constraint FK_PageBill_PK 
        foreign key (Bill_ID) 
        references qsdatadev.QS_Bill;

    alter table qsdatadev.QS_REPACKAGING_ATTACHMENT 
        add constraint FK_RepackAttachRepackEntry_PK 
        foreign key (repackagingEntry_ID) 
        references qsdatadev.QS_REPACKAGING_ENTRY;

    alter table qsdatadev.QS_REPACKAGING_DETAIL 
        add constraint FK_RepackDetailRepackEntry_PK 
        foreign key (RepackagingEntry_ID) 
        references qsdatadev.QS_REPACKAGING_ENTRY;

    alter table qsdatadev.QS_REPACKAGING_ENTRY 
        add constraint FK_RepackagingEntryJob_PK 
        foreign key (job_id) 
        references qsdatadev.QS_JOB;

    alter table qsdatadev.QS_RESOURCE 
        add constraint FK_ResourceBQItem_PK 
        foreign key (bqItem_ID) 
        references qsdatadev.QS_BQItem;

    alter table qsdatadev.QS_SCDETAIL_PROV_HIST 
        add constraint FK_PROVISIONHIST_SCDETAIL_PK 
        foreign key (scDetail_ID) 
        references qsdatadev.QS_SC_DETAILS;

    alter table qsdatadev.QS_SCPAYMENT_CERT 
        add constraint FK_SCPaymentCertSCPackage_PK 
        foreign key (scPackage_ID) 
        references qsdatadev.QS_SC_PACKAGE;

    alter table qsdatadev.QS_SCPAYMENT_DETAIL 
        add constraint FK_SCPaymentDetailSCPayCert_PK 
        foreign key (scPaymentCert_ID) 
        references qsdatadev.QS_SCPAYMENT_CERT;

    alter table qsdatadev.QS_SCPAYMENT_DETAIL 
        add constraint FK_scPaymentDetailSCDetails_PK 
        foreign key (scDetail_ID) 
        references qsdatadev.QS_SC_DETAILS;

    alter table qsdatadev.QS_SCWorkScope 
        add constraint FK_SCWORKSCOPE_SCPACKAGE_PK 
        foreign key (scPackage_ID) 
        references qsdatadev.QS_SC_PACKAGE;

    alter table qsdatadev.QS_SC_ATTACHMENT 
        add constraint FK_SCATTACHMENT_SCPACKAGE_PK 
        foreign key (SCPackage_ID) 
        references qsdatadev.QS_SC_PACKAGE;

    alter table qsdatadev.QS_SC_DETAILS 
        add constraint FK_SCDetailSCPackage_PK 
        foreign key (scPackage_ID) 
        references qsdatadev.QS_SC_PACKAGE;

    alter table qsdatadev.QS_SC_DETAILS_ATTACHMENT 
        add constraint FK_SCPayAttachSCPayCert_PK 
        foreign key (scDetails_ID) 
        references qsdatadev.QS_SC_DETAILS;

    alter table qsdatadev.QS_SC_PACKAGE 
        add constraint FK_scPackage_Job_PKbfe52374 
        foreign key (job_ID) 
        references qsdatadev.QS_JOB;

    alter table qsdatadev.QS_SC_PAYMENT_ATTACHMENT 
        add constraint FK_SCPayAttachSCPayCert_PK 
        foreign key (scPaymentCert_ID) 
        references qsdatadev.QS_SCPAYMENT_CERT;

    alter table qsdatadev.QS_TERM_VARIANCES 
        add constraint pk_term_termVar_fk 
        foreign key (termCode) 
        references qsdatadev.QS_TERM;

    alter table qsdatadev.QS_TERM_VARIANCES 
        add constraint pk_scpackage_termVar_FK 
        foreign key (scPackage_ID) 
        references qsdatadev.QS_SC_PACKAGE;

    alter table qsdatadev.QS_TenderAnalysis 
        add constraint FK_TenderAnalysisSCPackage_PK 
        foreign key (scPackage_ID) 
        references qsdatadev.QS_SC_PACKAGE;

    alter table qsdatadev.QS_TenderAnalysisDetail 
        add constraint FK_TADetailTA_PK 
        foreign key (tenderAnalysis_ID) 
        references qsdatadev.QS_TenderAnalysis;

    alter table qsdatadev.QS_TransitBQ 
        add constraint FK_TransitBQTransitHeader_PK 
        foreign key (transitHeader_ID) 
        references qsdatadev.QS_TransitHeader;

    alter table qsdatadev.QS_TransitResource 
        add constraint FK_TransitHeaderTransitRes_PK 
        foreign key (transitBQ_ID) 
        references qsdatadev.QS_TransitBQ;

    alter table qsadmin.QS_APP_ACTIVE_SESSION 
        add constraint FK521FC578A0152C9C 
        foreign key (USER_ID) 
        references qsadmin.QS_APP_USER;

    alter table qsadmin.QS_LINK_JOB_USER 
        add constraint FK17E41275A0152C9C 
        foreign key (USER_ID) 
        references qsadmin.QS_APP_USER;

    alter table qsadmin.QS_LINK_JOB_USER 
        add constraint FK17E41275C0B75086 
        foreign key (JOB_ID) 
        references qsadmin.QS_APP_JOB;

    alter table qsadmin.QS_LINK_USER_AUTHORITY 
        add constraint FK759DB8F7A0152C9C 
        foreign key (USER_ID) 
        references qsadmin.QS_APP_USER;

    alter table qsadmin.QS_LINK_USER_AUTHORITY 
        add constraint FK759DB8F7C7A9D58 
        foreign key (AUTHORITY_ID) 
        references qsadmin.QS_APP_AUTHORITY;

    alter table qsadmin.QS_USER_GENERAL_PREFERENCES 
        add constraint FKECB8EAEAA0152C9C 
        foreign key (USER_ID) 
        references qsadmin.QS_APP_USER;

    alter table qsadmin.QS_USER_SCREEN_PREFERENCES 
        add constraint FKCE4B0D5CA0152C9C 
        foreign key (USER_ID) 
        references qsadmin.QS_APP_USER;

    create sequence qsadmin.app_active_session_seq;

    create sequence qsadmin.app_authority_seq;

    create sequence qsadmin.app_job_seq;

    create sequence qsadmin.app_user_seq;

    create sequence qsadmin.system_message_seq;

    create sequence qsdatadev.QS_BQRESOURCE_SUMMARY_CTL_SEQ;

    create sequence qsdatadev.QS_SC_PAYMENT_DETAIL_CTL_SEQ;

    create sequence qsdatadev.main_contract_certificate_seq;

    create sequence qsdatadev.qs_audit_resourcesummary_seq;

    create sequence qsdatadev.qs_bill_seq;

    create sequence qsdatadev.qs_bqitem_control_seq;

    create sequence qsdatadev.qs_bqitem_seq;

    create sequence qsdatadev.qs_bqresource_summary_seq;

    create sequence qsdatadev.qs_forecast_seq;

    create sequence qsdatadev.qs_ivposting_history_seq;

    create sequence qsdatadev.qs_job_seq;

    create sequence qsdatadev.qs_main_contra_charge_seq;

    create sequence qsdatadev.qs_page_seq;

    create sequence qsdatadev.qs_repackagingEntry_seq;

    create sequence qsdatadev.qs_repackagingdetail_seq;

    create sequence qsdatadev.qs_resource_control_seq;

    create sequence qsdatadev.qs_resource_seq;

    create sequence qsdatadev.qs_sc_details_control_seq;

    create sequence qsdatadev.qs_sc_details_seq;

    create sequence qsdatadev.qs_sc_package_control_seq;

    create sequence qsdatadev.qs_sc_package_seq;

    create sequence qsdatadev.qs_sc_payment_cert_control_seq;

    create sequence qsdatadev.qs_sc_payment_cert_seq;

    create sequence qsdatadev.qs_sc_workscope_seq;

    create sequence qsdatadev.qs_tenderAnalysisDetail_seq;

    create sequence qsdatadev.qs_tenderAnalysis_seq;

    create sequence qsdatadev.qs_term_variances_seq;

    create sequence qsdatadev.qs_transitBQ_seq;

    create sequence qsdatadev.qs_transitCodeMatch_seq;

    create sequence qsdatadev.qs_transitHeader_seq;

    create sequence qsdatadev.qs_transitResource_seq;

    create sequence qsdatadev.qs_transitUomMatch_seq;
