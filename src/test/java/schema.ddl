
    create table pcmsdatadev.APP_SUBCONTRACT_STANDARD_TERMS (
        company varchar2(5 char) not null,
        system_Code varchar2(2 char) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        finQS0Review varchar2(3 char),
        retentionType varchar2(30 char),
        scInterimRetPercent double precision,
        scMOSRetPercent double precision,
        scMaxRetPercent double precision,
        scPaymentTerm varchar2(10 char),
        primary key (company, system_Code)
    );

    create table pcmsdatadev.APP_TRANSIT_RESOURCE_CODE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        matchingType varchar2(2 char),
        objectCode varchar2(6 char),
        resourceCode varchar2(10 char),
        subsidiaryCode varchar2(8 char),
        primary key (ID)
    );

    create table pcmsdatadev.APP_TRANSIT_UOM (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        causewayUom varchar2(10 char),
        jdeUom varchar2(2 char),
        primary key (ID)
    );

    create table pcmsdatadev.ATTACH_MAIN_CERT (
        SEQUENCENO number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        id number(19,0),
        documentType number(10,0),
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        textAttachment varchar2(2000 char),
        MAINCERTIFICATE_ID number(19,0),
        primary key (MAINCERTIFICATE_ID, SEQUENCENO)
    );

    create table pcmsdatadev.ATTACH_PAYMENT (
        sequenceNo number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        id number(19,0),
        documentType number(10,0),
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        textAttachment varchar2(2000 char),
        scPaymentCert_ID number(19,0),
        primary key (scPaymentCert_ID, sequenceNo)
    );

    create table pcmsdatadev.ATTACH_REPACKAGING (
        sequenceNo number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        id number(19,0),
        documentType number(10,0),
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        textAttachment varchar2(2000 char),
        repackagingEntry_ID number(19,0),
        primary key (repackagingEntry_ID, sequenceNo)
    );

    create table pcmsdatadev.ATTACH_SUBCONTRACT (
        sequenceNo number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        id number(19,0),
        documentType number(10,0),
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        textAttachment varchar2(2000 char),
        SCPackage_ID number(19,0),
        primary key (SCPackage_ID, sequenceNo)
    );

    create table pcmsdatadev.ATTACH_SUBCONTRACT_DETAIL (
        sequenceNo number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        id number(19,0),
        documentType number(10,0),
        fileLink varchar2(500 char),
        fileName varchar2(100 char),
        textAttachment varchar2(2000 char),
        scDetails_ID number(19,0),
        primary key (scDetails_ID, sequenceNo)
    );

    create table pcmsdatadev.BPI_BILL (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        billNo varchar2(20 char),
        description varchar2(255 char),
        sectionNo varchar2(20 char),
        subBillNo varchar2(20 char),
        Job_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.BPI_ITEM (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        activityCode varchar2(12 char),
        bqStatus varchar2(255 char),
        bqType varchar2(20 char),
        certifiedCumQty double precision,
        certifiedPostedQty double precision,
        costRate double precision,
        description varchar2(2000 char),
        genuineMarkupRate double precision,
        ipaCumQty double precision,
        ipaGroup varchar2(12 char),
        ipaPostedQty double precision,
        itemNo varchar2(20 char),
        ivCumAmount double precision,
        ivCumQty double precision,
        ivGroup varchar2(12 char),
        ivPostedAmount double precision,
        ivPostedQty double precision,
        quantity double precision,
        BillNoref varchar2(20 char),
        JobNoref varchar2(12 char),
        PageNoref varchar2(20 char),
        SectionNoref varchar2(20 char),
        SubBillNoref varchar2(20 char),
        remeasuredQty double precision,
        section varchar2(20 char),
        sellingRate double precision,
        sequenceNo number(10,0),
        sortDisplaySeqNo number(10,0),
        subSection varchar2(20 char),
        subsidiaryCode varchar2(8 char),
        unit varchar2(10 char),
        Page_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.BPI_ITEM_RESOURCE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        costRate double precision,
        description varchar2(255 char),
        ivCumAmount double precision,
        ivCumQty double precision,
        ivMovementAmount double precision,
        ivPostedAmount double precision,
        ivPostedQty double precision,
        jobNoRef varchar2(12 char),
        materialWastage double precision,
        objectCode varchar2(6 char),
        packageNature varchar2(10 char),
        packageNo varchar2(10 char),
        packageStatus varchar2(10 char),
        packageType varchar2(10 char),
        quantity double precision,
        billNoRef varchar2(255 char),
        itemNoRef varchar2(255 char),
        pageNoRef varchar2(255 char),
        sectionNoRef varchar2(255 char),
        subBillNoRef varchar2(255 char),
        remeasuredFactor double precision,
        resourceNo number(10,0),
        resourceType varchar2(10 char),
        splitStatus varchar2(10 char),
        subsidiaryCode varchar2(8 char),
        unit varchar2(10 char),
        bqItem_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.BPI_PAGE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        description varchar2(255 char),
        pageNo varchar2(20 char),
        Bill_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.IV_POSTING_HIST (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        DOCUMENTNO number(10,0),
        EDIBATCHNO number(10,0),
        IVMOVEMENTAMOUNT double precision,
        JOBNO varchar2(12 char),
        objectCode varchar2(6 char),
        PACKAGENO varchar2(10 char),
        RATE double precision,
        DESCRIPTION varchar2(255 char),
        RESOURCE_SUMMARY_ID number(19,0),
        subsidiaryCode varchar2(8 char),
        UNIT varchar2(3 char),
        primary key (ID)
    );

    create table pcmsdatadev.JDE_ACCOUNT_LEDGER (
        ENTRY_ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        BEGIN_BALANCE double precision,
        CUM_TO_DATE double precision,
        CURRENT_YEAR_TOTAL double precision,
        DESCRIPTION varchar2(60 char),
        FISCAL_YEAR number(10,0),
        JOB_NO varchar2(20 char) not null,
        LEDGER_TYPE varchar2(5 char),
        OBJECT_CODE varchar2(10 char) not null,
        PERIOD01 double precision,
        PERIOD02 double precision,
        PERIOD03 double precision,
        PERIOD04 double precision,
        PERIOD05 double precision,
        PERIOD06 double precision,
        PERIOD07 double precision,
        PERIOD08 double precision,
        PERIOD09 double precision,
        PERIOD10 double precision,
        PERIOD11 double precision,
        PERIOD12 double precision,
        PERIOD13 double precision,
        PERIOD14 double precision,
        SUBLEDGER varchar2(10 char),
        SUBLEDGER_TYPE varchar2(5 char),
        SUBSIDIARY_CODE varchar2(10 char),
        primary key (ENTRY_ID)
    );

    create table pcmsdatadev.JOB_INFO (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        actualMakingGoodDate timestamp,
        actualPCCDate timestamp,
        manualInputSCWD varchar2(1 char),
        billingCurrency varchar2(10 char),
        bqFinalizedFlag varchar2(255 char),
        clientContractNo varchar2(20 char),
        company varchar2(12 char),
        completionStatus varchar2(1 char),
        contractType varchar2(10 char),
        conversionStatus varchar2(3 char),
        cpfApplicable varchar2(3 char),
        cpfBasePeriod number(10,0),
        cpfBaseYear number(10,0),
        cpfIndexName varchar2(12 char),
        dateFinalACSettlement timestamp,
        defectLiabilityPeriod number(10,0),
        defectListIssuedDate timestamp,
        defectProvisionPercent double precision,
        department varchar2(10 char),
        description varchar2(255 char),
        division varchar2(10 char),
        employer varchar2(12 char),
        expectedMakingGoodDate timestamp,
        expectedPCCDate timestamp,
        finQS0Review varchar2(3 char),
        financialEndDate timestamp,
        forecastEndPeriod number(10,0),
        forecastEndYear number(10,0),
        grossFloorArea double precision,
        grossFloorAreaUnit varchar2(10 char),
        insuranceCAR varchar2(10 char),
        insuranceECI varchar2(10 char),
        insuranceTPL varchar2(10 char),
        interimRetPercent double precision,
        internalJob varchar2(3 char),
        jobNo varchar2(12 char),
        jvPartnerNo varchar2(12 char),
        jvPercentage double precision,
        legacyJob varchar2(1 char),
        levyApplicable varchar2(3 char),
        levyCITAPercent double precision,
        levyPCFBPercent double precision,
        maxRetPercent double precision,
        mosRetPercent double precision,
        orginalNSCContractValue double precision,
        originalContractValue double precision,
        parentJobNo varchar2(12 char),
        paymentTermForNSC varchar2(10 char),
        projectedContractValue double precision,
        repackagingType varchar2(1 char),
        soloJV varchar2(4 char),
        tenderGP double precision,
        valueOfBSWork double precision,
        yearOfCompletion number(10,0),
        primary key (ID)
    );

    create table pcmsdatadev.MAIN_CERT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ACTUAL_RECEIPT_DATE timestamp,
        appAdjustmentAmt double precision,
        appAdvancePayment double precision,
        appCPFAmt double precision,
        appCCAmt double precision,
        appMOSAmt double precision,
        appMOSRet double precision,
        appMOSRetRel double precision,
        appMainContractorAmt double precision,
        appMainContractorRet double precision,
        appMainContractorRetRel double precision,
        appNSCNDSCAmt double precision,
        appRetForNSCNDSC double precision,
        appRetRelForNSCNDSC double precision,
        arDocumentNo number(10,0),
        CERT_AS_AT_DATE timestamp,
        certDueDate timestamp,
        CERT_ISSUE_DATE timestamp,
        certStatuschangeDate timestamp,
        certNo number(10,0),
        CERTIFICATESTATUS varchar2(10 char),
        certAdjustmentAmt double precision,
        certAdvancePayment double precision,
        certCPFAmt double precision,
        certCCAmt double precision,
        certMOSAmt double precision,
        certMOSRet double precision,
        certMOSRetRel double precision,
        certMainContractorAmt double precision,
        certMainContractorRet double precision,
        certMainContractorRetRel double precision,
        certNSCNDSCAmt double precision,
        certRetForNSCNDSC double precision,
        certRetRelForNSCNDSC double precision,
        clientCertNo varchar2(255 char),
        GSTPAYABLE double precision,
        GSTRECEIVABLE double precision,
        IPASENTOUTDATE timestamp,
        IPA_SUBMISSION_DATE timestamp,
        JOBNO varchar2(12 char),
        REMARK varchar2(255 char),
        TOTAL_RECEIPT_AMOUNT double precision,
        primary key (ID)
    );

    create table pcmsdatadev.MAIN_CERT_CONTRA_CHARGE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        CumAmount double precision,
        objectCode varchar2(6 char),
        PostedAmount double precision,
        SubsidiaryCode varchar2(8 char),
        mainCert_id number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.MAIN_CERT_RETENTION_RELEASE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ACTRELEASEAMT double precision,
        contractualDueDate timestamp,
        dueDate timestamp,
        FSTRELEASEAMT double precision,
        jobNo varchar2(12 char) not null,
        mainCertNo number(10,0),
        RELEASEPERCENT double precision,
        sequenceNo number(10,0) not null,
        STATUS varchar2(3 char),
        primary key (ID)
    );

    create table pcmsdatadev.PAYMENT_CERT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        addendumAmount double precision,
        asAtDate timestamp,
        certAmount double precision,
        certIssueDate timestamp,
        directPayment varchar2(1 char),
        dueDate timestamp,
        paymentType varchar2(5 char),
        scIpaReceivedDate timestamp,
        jobNo varchar2(12 char),
        mainContractCertNo number(10,0),
        packageNo varchar2(10 char),
        paymentCertNo number(10,0),
        paymentStatus varchar2(3 char),
        remeasuredSCSum double precision,
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.PAYMENT_CERT_DETAIL (
        scSeqNo number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        billItem varchar2(110 char),
        cumAmount double precision,
        lineType varchar2(10 char),
        movementAmount double precision,
        objectCode varchar2(6 char),
        paymentCertNo varchar2(5 char),
        subsidiaryCode varchar2(8 char),
        scPaymentCert_ID number(19,0),
        scDetail_ID number(19,0),
        primary key (scPaymentCert_ID, scSeqNo)
    );

    create table pcmsdatadev.PROVISION_POSTING_HIST (
        postedMonth number(10,0) not null,
        postedYr number(10,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        liabilitiesCumAmount double precision,
        liabilitiesCumQty double precision,
        jobNo varchar2(12 char),
        objectCode varchar2(6 char) not null,
        packageNo varchar2(10 char),
        certPostedAmount double precision,
        certPostedQty double precision,
        scRate double precision,
        subsidiaryCode varchar2(8 char) not null,
        scDetail_ID number(19,0),
        primary key (postedMonth, postedYr, scDetail_ID)
    );

    create table pcmsdatadev.QRTZ_CRON_TRIGGERS (
        SCHED_NAME varchar2(120 char) not null,
        TRIGGER_GROUP varchar2(200 char) not null,
        TRIGGER_NAME varchar2(200 char) not null,
        CRON_EXPRESSION varchar2(120 char),
        primary key (SCHED_NAME, TRIGGER_GROUP, TRIGGER_NAME)
    );

    create table pcmsdatadev.QRTZ_TRIGGERS (
        SCHED_NAME varchar2(120 char) not null,
        TRIGGER_GROUP varchar2(200 char) not null,
        TRIGGER_NAME varchar2(200 char) not null,
        CALENDAR_NAME varchar2(200 char),
        DESCRIPTION varchar2(250 char),
        END_TIME number(19,0),
        JOB_GROUP varchar2(200 char),
        JOB_NAME varchar2(200 char),
        MISFIRE_INSTR number(19,0),
        NEXT_FIRE_TIME number(19,0),
        PREV_FIRE_TIME number(19,0),
        PRIORITY number(19,0),
        START_TIME number(19,0),
        TRIGGER_TYPE varchar2(8 char),
        TRIGGER_STATE varchar2(16 char),
        primary key (SCHED_NAME, TRIGGER_GROUP, TRIGGER_NAME)
    );

    create table pcmsdatadev.QS_AUDIT_RESOURCESUMMARY (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ACTION_TYPE varchar2(20 char),
        dataType varchar2(20 char),
        repackagingEntryId number(19,0),
        RESOURCE_SUMMARY_ID number(19,0),
        VALUE_FROM varchar2(255 char),
        VALUE_TO varchar2(255 char),
        VALUE_TYPE varchar2(20 char),
        primary key (ID)
    );

    create table pcmsdatadev.REPACKAGING (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        createDate date,
        REMARKS varchar2(500 char),
        REPACKAGINGVERSION number(10,0),
        STATUS varchar2(3 char),
        totalResAllowance double precision,
        job_id number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.REPACKAGING_DETAIL (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        AMOUNT double precision,
        excludeDefect number(1,0),
        excludeLevy number(1,0),
        objectCode varchar2(6 char),
        packageNo varchar2(10 char),
        quantity double precision,
        RATE double precision,
        description varchar2(255 char),
        resourceSummaryId number(19,0),
        resourceType varchar2(10 char),
        subsidiaryCode varchar2(8 char),
        unit varchar2(3 char),
        RepackagingEntry_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.RESOURCE_SUMMARY (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ivCumAmount double precision,
        excludeDefect number(1,0),
        excludeLevy number(1,0),
        finalized varchar2(255 char),
        forIvRollbackOnly number(1,0),
        objectCode varchar2(8 char),
        PACKAGENO varchar2(10 char),
        ivPostedAmount double precision,
        QUANTITY double precision,
        RATE double precision,
        description varchar2(255 char),
        resourceType varchar2(10 char),
        subsidiaryCode varchar2(8 char),
        UNIT varchar2(10 char),
        JOB_ID number(19,0),
        MERGE_TO_ID number(19,0),
        SPLIT_FROM_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.SUBCONTRACT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        accumlatedRet double precision,
        approvalRoute varchar2(5 char),
        approvedVOAmount double precision,
        cpfBasePeriod number(10,0),
        cpfBaseYear number(10,0),
        cpfCalculation varchar2(20 char),
        description varchar2(255 char),
        exchangeRate double precision,
        finalPaymentIssuedDate timestamp,
        firstPaymentCertIssuedDate timestamp,
        formOfSubcontract varchar2(22 char),
        interimRetPerent double precision,
        internalJobNo varchar2(12 char),
        labourIncluded number(1,0),
        lastPaymentCertIssuedDate timestamp,
        latestAddendumValueUpdatedDate timestamp,
        LOA_SIGNED_DATE date,
        materialIncluded number(1,0),
        maxRetPercent double precision,
        mosRetPerent double precision,
        ONSITE_START_DATE date,
        originalSCSum double precision,
        packageNo varchar2(10 char),
        packageStatus varchar2(3 char),
        packageType varchar2(4 char),
        paymentCurrency varchar2(3 char),
        paymentInfo varchar2(50 char),
        paymentStatus varchar2(1 char),
        paymentTerms varchar2(3 char),
        paymentTermsDescription varchar2(255 char),
        plantIncluded number(1,0),
        PREAWARD_MEETING_DATE date,
        remeasuredSCSum double precision,
        REQUISITION_APPROVED_DATE date,
        retAmount double precision,
        retRelease double precision,
        retentionTerms varchar2(30 char),
        scApprovalDate timestamp,
        scAwardApprovalRequestSentDate timestamp,
        scCreatedDate timestamp,
        SC_DOC_LEGAL_DATE date,
        SC_DOC_SCR_DATE date,
        splitTerminateStatus varchar2(1 char),
        scStatus number(10,0),
        scTerm varchar2(15 char),
        scNature varchar2(4 char),
        submittedAddendum varchar2(1 char),
        TA_APPROVED_DATE date,
        totalCCPostedCertAmt double precision,
        totalCumCertAmt double precision,
        totalCumWDAmt double precision,
        totalMOSPostedCertAmt double precision,
        totalPostedCertAmt double precision,
        totalPostedWDAmt double precision,
        vendorNo varchar2(255 char),
        WORK_COMMENCE_DATE date,
        job_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.SUBCONTRACT_DETAIL (
        TYPE varchar2(2 char) not null,
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        approved varchar2(1 char),
        billItem varchar2(110 char),
        cumCertQty double precision,
        description varchar2(255 char),
        jobNo varchar2(12 char),
        lineType varchar2(10 char),
        newQty double precision,
        objectCode varchar2(6 char),
        originalQty double precision,
        postedCertQty double precision,
        quantity double precision,
        scRemark varchar2(255 char),
        resourceNo number(10,0),
        scRate double precision,
        sequenceNo number(10,0),
        subsidiaryCode varchar2(8 char),
        TENDER_ANALYSIS_DETAIL_ID number(19,0),
        unit varchar2(10 char),
        altObjectCode varchar2(6 char),
        contraChargeSCNo varchar2(10 char),
        corrSCLineSeqNo number(19,0),
        cumWDQty double precision,
        postedWDQty double precision,
        costRate double precision,
        toBeApprovedQty double precision,
        toBeApprovedRate double precision,
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.SUBCONTRACT_SNAPSHOT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        accumlatedRet double precision,
        approvalRoute varchar2(5 char),
        approvedVOAmount double precision,
        cpfBasePeriod number(10,0),
        cpfBaseYear number(10,0),
        cpfCalculation varchar2(20 char),
        description varchar2(255 char),
        exchangeRate double precision,
        formOfSubcontract varchar2(22 char),
        interimRetPerent double precision,
        internalJobNo varchar2(12 char),
        labourIncluded number(1,0),
        LOA_SIGNED_DATE date,
        maxRetPercent double precision,
        mosRetPerent double precision,
        ONSITE_START_DATE date,
        originalSCSum double precision,
        packageNo varchar2(10 char),
        packageStatus varchar2(3 char),
        packageType varchar2(4 char),
        paymentCurrency varchar2(3 char),
        paymentInfo varchar2(50 char),
        paymentStatus varchar2(1 char),
        paymentTerms varchar2(3 char),
        paymentTermsDescription varchar2(255 char),
        plantIncluded number(1,0),
        PREAWARD_MEETING_DATE date,
        remeasuredSCSum double precision,
        REQUISITION_APPROVED_DATE date,
        retAmount double precision,
        retRelease double precision,
        retentionTerms varchar2(30 char),
        SC_DOC_LEGAL_DATE date,
        SC_DOC_SCR_DATE date,
        SNAPSHOT_DATE date,
        splitTerminateStatus varchar2(1 char),
        scStatus number(10,0),
        scTerm varchar2(15 char),
        scNature varchar2(4 char),
        submittedAddendum varchar2(1 char),
        TA_APPROVED_DATE date,
        totalCCPostedCertAmt double precision,
        totalCumCertAmt double precision,
        totalCumWDAmt double precision,
        totalMOSPostedCertAmt double precision,
        totalPostedCertAmt double precision,
        totalPostedWDAmt double precision,
        vendorNo varchar2(255 char),
        WORK_COMMENCE_DATE date,
        job_ID number(19,0),
        QS_SC_PACKAGE_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.SUBCONTRACT_WORKSCOPE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        workScope varchar2(10 char),
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.TENDER (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        budgetAmount double precision,
        currencyCode varchar2(10 char),
        exchangeRate double precision,
        jobNo varchar2(12 char),
        packageNo varchar2(10 char),
        status varchar2(10 char),
        vendorNo number(10,0),
        scPackage_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.TENDER_DETAIL (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        billItem varchar2(110 char),
        description varchar2(255 char),
        feedbackRateD double precision,
        feedbackRateF double precision,
        lineType varchar2(10 char),
        objectCode varchar2(6 char),
        quantity double precision,
        remark varchar2(255 char),
        resourceNo number(10,0),
        sequenceNo number(10,0),
        subsidiaryCode varchar2(8 char),
        unit varchar2(10 char),
        tenderAnalysis_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.TRANSIT_BPI (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        billNo varchar2(20 char),
        costRate double precision,
        description varchar2(1000 char),
        itemNo varchar2(20 char),
        pageNo varchar2(20 char),
        quantity double precision,
        sellingRate double precision,
        sequenceNo number(10,0),
        subBillNo varchar2(20 char),
        unit varchar2(10 char),
        value double precision,
        transitHeader_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.TRANSIT_RESOURCE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        description varchar2(255 char),
        objectCode varchar2(6 char),
        packageNo varchar2(5 char),
        rate double precision,
        resourceCode varchar2(10 char),
        resourceNo number(10,0),
        subsidiaryCode varchar2(8 char),
        totalQuantity double precision,
        type varchar2(10 char),
        unit varchar2(10 char),
        value double precision,
        waste double precision,
        transitBQ_ID number(19,0),
        primary key (ID)
    );

    create table pcmsdatadev.TRASNIT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        company varchar2(10 char),
        estimateNo varchar2(12 char),
        jobDescription varchar2(255 char),
        jobNumber varchar2(12 char),
        matchingCode varchar2(10 char),
        status varchar2(30 char),
        primary key (ID)
    );

    create table pcmsdatadev.V_CONTRACT_RECEIVABLE_RPT (
        JOBNO varchar2(255 char) not null,
        ACTUALRECEIPTDATE varchar2(10 char),
        BENCHMARK1 varchar2(40 char),
        BENCHMARK2 varchar2(40 char),
        CERTASATDATE varchar2(10 char),
        CERTDUEDATE varchar2(10 char),
        CERTISSUEDATE varchar2(10 char),
        CERTNO varchar2(255 char),
        CERTSTATUS varchar2(10 char),
        CLIENTCERTNO varchar2(255 char),
        clientName varchar2(255 char),
        CLIENTNO varchar2(12 char),
        COMPANY varchar2(12 char),
        CURRENCY varchar2(10 char),
        DIVISION varchar2(10 char),
        IPADATE varchar2(10 char),
        JOBDESCRIPTION varchar2(255 char),
        RECEIVABLEAMOUNT varchar2(19 char),
        TOTALRECEIPTAMOUNT varchar2(19 char),
        primary key (JOBNO)
    );

    alter table pcmsdatadev.REPACKAGING 
        add constraint UK_20ddai7ofk7rp80jjrs9clnxn  unique (REPACKAGINGVERSION);

    alter table pcmsdatadev.REPACKAGING 
        add constraint UK_nwdcrp8cxw4h3e6o80i1pkh72  unique (job_id);

    alter table pcmsdatadev.ATTACH_MAIN_CERT 
        add constraint FK_MainCertAttachMainCert_PK 
        foreign key (MAINCERTIFICATE_ID) 
        references pcmsdatadev.MAIN_CERT;

    alter table pcmsdatadev.ATTACH_PAYMENT 
        add constraint FK_SCPayAttachSCPayCert_PK 
        foreign key (scPaymentCert_ID) 
        references pcmsdatadev.PAYMENT_CERT;

    alter table pcmsdatadev.ATTACH_REPACKAGING 
        add constraint FK_RepackAttachRepackEntry_PK 
        foreign key (repackagingEntry_ID) 
        references pcmsdatadev.REPACKAGING;

    alter table pcmsdatadev.ATTACH_SUBCONTRACT 
        add constraint FK_SCATTACHMENT_SCPACKAGE_PK 
        foreign key (SCPackage_ID) 
        references pcmsdatadev.SUBCONTRACT;

    alter table pcmsdatadev.ATTACH_SUBCONTRACT_DETAIL 
        add constraint FK_ss7a3ep7ejq6r2qqkkan74n2f 
        foreign key (scDetails_ID) 
        references pcmsdatadev.SUBCONTRACT_DETAIL;

    alter table pcmsdatadev.BPI_BILL 
        add constraint FK_BillJob_PK 
        foreign key (Job_ID) 
        references pcmsdatadev.JOB_INFO;

    alter table pcmsdatadev.BPI_ITEM 
        add constraint FK_BQItemPage_PK 
        foreign key (Page_ID) 
        references pcmsdatadev.BPI_PAGE;

    alter table pcmsdatadev.BPI_ITEM_RESOURCE 
        add constraint FK_ResourceBQItem_PK 
        foreign key (bqItem_ID) 
        references pcmsdatadev.BPI_ITEM;

    alter table pcmsdatadev.BPI_PAGE 
        add constraint FK_PageBill_PK 
        foreign key (Bill_ID) 
        references pcmsdatadev.BPI_BILL;

    alter table pcmsdatadev.MAIN_CERT_CONTRA_CHARGE 
        add constraint PK_MainCert_CC_FK 
        foreign key (mainCert_id) 
        references pcmsdatadev.MAIN_CERT;

    alter table pcmsdatadev.PAYMENT_CERT 
        add constraint FK_3rup5wm3cfaadwljpftqs0fh0 
        foreign key (scPackage_ID) 
        references pcmsdatadev.SUBCONTRACT;

    alter table pcmsdatadev.PAYMENT_CERT_DETAIL 
        add constraint FK_SCPaymentDetailSCPayCert_PK 
        foreign key (scPaymentCert_ID) 
        references pcmsdatadev.PAYMENT_CERT;

    alter table pcmsdatadev.PAYMENT_CERT_DETAIL 
        add constraint FK_gpwjkmy7pj57lihuk37xr7o83 
        foreign key (scDetail_ID) 
        references pcmsdatadev.SUBCONTRACT_DETAIL;

    alter table pcmsdatadev.PROVISION_POSTING_HIST 
        add constraint FK_PROVISIONHIST_SCDETAIL_PK 
        foreign key (scDetail_ID) 
        references pcmsdatadev.SUBCONTRACT_DETAIL;

    alter table pcmsdatadev.REPACKAGING 
        add constraint FK_RepackagingEntryJob_PK 
        foreign key (job_id) 
        references pcmsdatadev.JOB_INFO;

    alter table pcmsdatadev.REPACKAGING_DETAIL 
        add constraint FK_RepackDetailRepackEntry_PK 
        foreign key (RepackagingEntry_ID) 
        references pcmsdatadev.REPACKAGING;

    alter table pcmsdatadev.RESOURCE_SUMMARY 
        add constraint FK_BQResourSumJOB_PK 
        foreign key (JOB_ID) 
        references pcmsdatadev.JOB_INFO;

    alter table pcmsdatadev.RESOURCE_SUMMARY 
        add constraint FK_BQRS_MT 
        foreign key (MERGE_TO_ID) 
        references pcmsdatadev.RESOURCE_SUMMARY;

    alter table pcmsdatadev.RESOURCE_SUMMARY 
        add constraint FK_BQRS_SF 
        foreign key (SPLIT_FROM_ID) 
        references pcmsdatadev.RESOURCE_SUMMARY;

    alter table pcmsdatadev.SUBCONTRACT 
        add constraint FK_scPackage_Job_PK 
        foreign key (job_ID) 
        references pcmsdatadev.JOB_INFO;

    alter table pcmsdatadev.SUBCONTRACT_DETAIL 
        add constraint FK_SCDetailSCPackage_PK 
        foreign key (scPackage_ID) 
        references pcmsdatadev.SUBCONTRACT;

    alter table pcmsdatadev.SUBCONTRACT_SNAPSHOT 
        add constraint FK_scPackageSnapshot_Job_PK 
        foreign key (job_ID) 
        references pcmsdatadev.JOB_INFO;

    alter table pcmsdatadev.SUBCONTRACT_SNAPSHOT 
        add constraint FK_Snapshot_SCPackage_PK 
        foreign key (QS_SC_PACKAGE_ID) 
        references pcmsdatadev.SUBCONTRACT;

    alter table pcmsdatadev.SUBCONTRACT_WORKSCOPE 
        add constraint FK_SCWORKSCOPE_SCPACKAGE_PK 
        foreign key (scPackage_ID) 
        references pcmsdatadev.SUBCONTRACT;

    alter table pcmsdatadev.TENDER 
        add constraint FK_TenderAnalysisSCPackage_PK 
        foreign key (scPackage_ID) 
        references pcmsdatadev.SUBCONTRACT;

    alter table pcmsdatadev.TENDER_DETAIL 
        add constraint FK_TADetailTA_PK 
        foreign key (tenderAnalysis_ID) 
        references pcmsdatadev.TENDER;

    alter table pcmsdatadev.TRANSIT_BPI 
        add constraint FK_TransitBQTransitHeader_PK 
        foreign key (transitHeader_ID) 
        references pcmsdatadev.TRASNIT;

    alter table pcmsdatadev.TRANSIT_RESOURCE 
        add constraint FK_TransitHeaderTransitRes_PK 
        foreign key (transitBQ_ID) 
        references pcmsdatadev.TRANSIT_BPI;

    create sequence pcmsdatadev.APP_TRANSIT_RESOURCE_CODE_SEQ;

    create sequence pcmsdatadev.APP_TRANSIT_UOM_SEQ;

    create sequence pcmsdatadev.BPI_BILL_SEQ;

    create sequence pcmsdatadev.BPI_ITEM_RESOURCE_SEQ;

    create sequence pcmsdatadev.BPI_ITEM_SEQ;

    create sequence pcmsdatadev.BPI_PAGE_SEQ;

    create sequence pcmsdatadev.IV_POSTING_HIST_SEQ;

    create sequence pcmsdatadev.JDE_ACCOUNT_LEDGER_SEQ;

    create sequence pcmsdatadev.JOB_INFO_SEQ;

    create sequence pcmsdatadev.MAIN_CERT_CONTRA_CHARGE_SEQ;

    create sequence pcmsdatadev.MAIN_CERT_RETENTION_RELEASE_SEQ;

    create sequence pcmsdatadev.MAIN_CERT_SEQ;

    create sequence pcmsdatadev.PAYMENT_CERT_SEQ;

    create sequence pcmsdatadev.REPACKAGING_DETAIL_SEQ;

    create sequence pcmsdatadev.REPACKAGING_SEQ;

    create sequence pcmsdatadev.RESOURCE_SUMMARY_AUDIT_CUSTOM_SEQ;

    create sequence pcmsdatadev.RESOURCE_SUMMARY_SEQ;

    create sequence pcmsdatadev.SUBCONTRACT_DETAIL_SEQ;

    create sequence pcmsdatadev.SUBCONTRACT_SEQ;

    create sequence pcmsdatadev.SUBCONTRACT_SNAPSHOT_SEQ;

    create sequence pcmsdatadev.SUBCONTRACT_WORKSCOPE_SEQ;

    create sequence pcmsdatadev.TENDER_DETAIL_SEQ;

    create sequence pcmsdatadev.TENDER_SEQ;

    create sequence pcmsdatadev.TRANSIT_BPI_SEQ;

    create sequence pcmsdatadev.TRANSIT_RESOURCE_SEQ;

    create sequence pcmsdatadev.TRANSIT_SEQ;

    create sequence pcmsdatadev.hibernate_sequence;
