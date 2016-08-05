	create or replace procedure audit_Housekeep(tableName in varchar2, period in int, rcount out number) AS
		stmt varchar2(1000);
		l_date  DATE := SYSDATE - period;
	Begin
	  stmt := 'delete from '||tableName||' where REVTYPE != 2 and REV in (select r.id from pcmsdatadev.revision r join '||tableName||' t on r.id = t.rev and unix_ts_to_date(r.timestamp/1000) < to_date('''|| l_date ||'''))' ;
	  dbms_output.put_line(stmt);
	  execute IMMEDIATE stmt;
	  rcount := SQL%ROWCOUNT;
	  dbms_output.put_line('SQL%ROWCOUNT:'||rcount);
	End;
	grant EXECUTE on "PCMSDATADEV"."AUDIT_HOUSEKEEP" to "PCMSUSER_ROLE";
	
	create or replace FUNCTION unix_ts_to_date( p_unix_ts IN NUMBER ) RETURN DATE AS
	  l_date DATE;
	BEGIN
	  l_date := date '1970-01-01' + p_unix_ts/60/60/24;
	  RETURN l_date;
	END;
	grant EXECUTE on "PCMSDATADEV"."UNIX_TS_TO_DATE" to "PCMSUSER_ROLE" ;
	
    create sequence pcmsdatadev.REVISION_SEQ start with 1 increment by 1;
	GRANT SELECT ON "PCMSDATADEV"."REVISION_SEQ" TO "PCMSUSER_ROLE";
	
	create table pcmsdatadev.REVISION (
        ID number(10,0) CONSTRAINT REVISION_ID_NN not null,
        timestamp number(19,0) CONSTRAINT REVISION_TIMESTAMP_NN not null,
        username varchar2(255 char),
        CONSTRAINT REVISION_PK primary key (ID)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."REVISION" TO "PCMSUSER_ROLE";

	create table pcmsdatadev.REVCHANGES (
        REV number(10,0) CONSTRAINT REVCHANGES_REV_NN not null,
        ENTITYNAME varchar2(255 char)
    )
    TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."REVCHANGES" TO "PCMSUSER_ROLE";

	create table pcmsdatadev.ADDENDUM_AUDIT (
		ID number(19,2) CONSTRAINT ADDENDUM_AUDIT_ID_NN not null,
		REV number(10,0) CONSTRAINT ADDENDUM_AUDIT_REV_NN not null,
		REVTYPE number(3,0),
		AMT_ADDENDUM number(19,2),
		AMT_ADDENDUM_TOTAL number(19,2),
		AMT_ADDENDUM_TOTAL_TBA number(19,2),
		AMT_SUBCONTRACT_REMEASURED number(19,2),
		AMT_SUBCONTRACT_REVISED number(19,2),
		AMT_SUBCONTRACT_REVISED_TBA number(19,2),
		DATE_APPROVAL date,
		DATE_CREATED date,
		DATE_LAST_MODIFIED date,
		DATE_SUBMISSION date,
		DESCRIPTION_SUBCONTRACT varchar2(510 char),
		NAME_SUBCONTRACTOR varchar2(510 char),
		NO number(19,0),
		NO_JOB varchar2(10 char),
		NO_SUBCONTRACT varchar2(8 char),
		NO_SUBCONTRACTOR varchar2(20 char),
		REMARKS varchar2(2000 char),
		STATUS varchar2(40 char),
		STATUS_APPROVAL varchar2(40 char),
		TITLE varchar2(510 char),
		USERNAME_CREATED varchar2(40 char),
		USERNAME_LAST_MODIFIED varchar2(40 char),
		USERNAME_PREPARED_BY varchar2(40 char),
		ID_SUBCONTRACT number(19,0),
		CONSTRAINT ADDENDUM_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."ADDENDUM_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.ADDENDUM_DETAIL_AUDIT (
        ID number(19,2) CONSTRAINT ADDENDUM_DETAIL_AUDIT_ID_NN not null,
        REV number(10,0) CONSTRAINT ADDENDUM_DETAIL_AUDIT_REV_NN not null,
        REVTYPE number(3,0),
        AMT_ADDENDUM number(19,2),
        AMT_ADDENDUM_TBA number(19,2),
        AMT_BUDGET number(19,2),
        AMT_BUDGET_TBA number(19,2),
        BPI varchar2(510 char),
        CODE_OBJECT varchar2(12 char),
        CODE_SUBSIDIARY varchar2(16 char),
        DATE_CREATED date,
        DATE_LAST_MODIFIED date,
        DESCRIPTION varchar2(4000 char),
        NO number(19,0),
        NO_JOB varchar2(10 char),
        NO_SUBCONTRACT varchar2(8 char),
        QUANTITY number(19,2),
        QUANTITY_TBA number(19,2),
        RATE_ADDENDUM number(19,2),
        RATE_ADDENDUM_TBA number(19,2),
        RATE_BUDGET number(19,2),
        RATE_BUDGET_TBA number(19,2),
        REMARKS varchar2(1000 char),
        TYPE_HD varchar2(20 char),
        TYPE_VO varchar2(4 char),
        UNIT varchar2(20 char),
        USERNAME_CREATED varchar2(40 char),
        USERNAME_LAST_MODIFIED varchar2(40 char),
        ID_ADDENDUM number(19,2),
        ID_HEADER_REF number(19,0),
        CONSTRAINT ADDENDUM_DETAIL_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."ADDENDUM_DETAIL_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.JOB_INFO_AUDIT (
        ID number(19,0) CONSTRAINT JOB_INFO_AUDIT_ID_NN not null,
        REV number(10,0) CONSTRAINT JOB_INFO_AUDIT_REV_NN not null,
        REVTYPE number(3,0),
        CREATED_DATE date,
        CREATED_USER varchar2(255 char),
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        actualMakingGoodDate date,
        actualPCCDate date,
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
        dateFinalACSettlement date,
        defectLiabilityPeriod number(10,0),
        defectListIssuedDate date,
        defectProvisionPercent double precision,
        department varchar2(10 char),
        description varchar2(255 char),
        division varchar2(10 char),
        employer varchar2(12 char),
        expectedMakingGoodDate date,
        expectedPCCDate date,
        finQS0Review varchar2(3 char),
        financialEndDate date,
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
        CONSTRAINT JOB_INFO_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."JOB_INFO_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.PAYMENT_CERT_AUDIT (
        ID number(19,0) CONSTRAINT PAYMENT_CERT_AUDIT_ID_NN not null,
        REV number(10,0) CONSTRAINT PAYMENT_CERT_AUDIT_REV_NN  not null,
        REVTYPE number(3,0),
        CREATED_DATE date,
        CREATED_USER varchar2(255 char),
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        addendumAmount double precision,
        asAtDate date,
        certAmount double precision,
        certIssueDate date,
        directPayment varchar2(1 char),
        dueDate date,
        paymentType varchar2(5 char),
        scIpaReceivedDate date,
        jobNo varchar2(12 char),
        mainContractCertNo number(10,0),
        packageNo varchar2(10 char),
        paymentCertNo number(10,0),
        paymentStatus varchar2(3 char),
        remeasuredSCSum double precision,
        SUBCONTRACT_ID number(19,0),
        CONSTRAINT PAYMENT_CERT_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."PAYMENT_CERT_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.PAYMENT_CERT_DETAIL_AUDIT (
        Payment_Cert_ID number(19,0) CONSTRAINT PAYMENTCERTDETAIL_AUDIT_ID_NN not null,
        scSeqNo number(10,0) CONSTRAINT PAYMENTCERTDETAIL_AUDIT_SEQ_NN not null,
        REV number(10,0) CONSTRAINT PAYMENTCERTDETAIL_AUDIT_REV_NN not null,
        REVTYPE number(3,0),
        CREATED_DATE date,
        CREATED_USER varchar2(255 char),
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
        SUBCONTRACT_DETAIL_ID number(19,0),
        CONSTRAINT PAYMENT_CERT_DETAIL_AUDIT_PK primary key (Payment_Cert_ID, scSeqNo, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."PAYMENT_CERT_DETAIL_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.RESOURCE_SUMMARY_AUDIT (
        ID number(19,0) CONSTRAINT RESOURCE_SUMMARY_AUDIT_ID_NN not null,
        REV number(10,0) CONSTRAINT RESOURCE_SUMMARY_AUDIT_REV_NN not null,
        REVTYPE number(3,0),
        CREATED_DATE date,
        CREATED_USER varchar2(255 char),
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        AMT_BUDGET double precision,
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
        Job_Info_ID number(19,0),
        MERGE_TO_ID number(19,0),
        SPLIT_FROM_ID number(19,0),
        CONSTRAINT RESOURCE_SUMMARY_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."RESOURCE_SUMMARY_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.SUBCONTRACT_AUDIT (
        ID number(19,0) CONSTRAINT SUBCONTRACT_AUDIT_ID_NN not null,
        REV number(10,0) CONSTRAINT SUBCONTRACT_AUDIT_REV_NN not null,
        REVTYPE number(3,0),
        CREATED_DATE date,
        CREATED_USER varchar2(255 char),
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
        finalPaymentIssuedDate date,
        firstPaymentCertIssuedDate date,
        formOfSubcontract varchar2(22 char),
        interimRetPerent double precision,
        internalJobNo varchar2(12 char),
        labourIncluded number(1,0),
        lastPaymentCertIssuedDate date,
        latestAddendumValueUpdatedDate date,
        LOA_SIGNED_DATE date,
        materialIncluded number(1,0),
        maxRetPercent double precision,
        mosRetPerent double precision,
        NOTES varchar2(1000 char),
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
        scApprovalDate date,
        scAwardApprovalRequestSentDate date,
        scCreatedDate date,
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
        WORK_SCOPE number(10,0),
        Job_Info_ID number(19,0),
        NAME_SUBCONTRACTOR varchar2(500 char) default ' ',
        CONSTRAINT SUBCONTRACT_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."SUBCONTRACT_AUDIT" TO "PCMSUSER_ROLE";

    create table pcmsdatadev.SUBCONTRACT_DETAIL_AUDIT (
        ID number(19,0) CONSTRAINT SUBCONTRACTDETAIL_AUDIT_ID_NN not null,
        REV number(10,0) CONSTRAINT SUBCONTRACTDETAIL_AUDIT_REV_NN not null,
        TYPE varchar2(2 char) CONSTRAINT SUBCONTRACTDETAIL_AUDIT_TYP_NN not null,
        REVTYPE number(3,0),
        CREATED_DATE date,
        CREATED_USER varchar2(255 char),
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        AMT_BUDGET double precision,
        AMT_CUMULATIVE_CERT number(19,2),
        AMT_CUMULATIVE_WD number(19,2),
        AMT_POSTED_CERT number(19,2),
        AMT_POSTED_WD number(19,2),
        AMT_SUBCONTRACT double precision,
        AMT_SUBCONTRACT_TBA double precision,
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
        TENDER_ID number(19,0),
        unit varchar2(10 char),
        SUBCONTRACT_ID number(19,0),
        AMT_SUBCONTRACT_NEW number(19,2),
        CONSTRAINT SUBCONTRACTDETAIL_AUDIT_PK primary key (ID, REV)
    )
	TABLESPACE "QSDATADEVT" ;
	GRANT DELETE, INSERT, SELECT, UPDATE ON "PCMSDATADEV"."SUBCONTRACT_DETAIL_AUDIT" TO "PCMSUSER_ROLE";
	
    alter table pcmsdatadev.REVCHANGES 
        add constraint REVCHANGES_FK 
        foreign key (REV) 
        references pcmsdatadev.REVISION;

    alter table pcmsdatadev.ADDENDUM_AUDIT 
        add constraint ADDENDUM_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;

    alter table pcmsdatadev.ADDENDUM_DETAIL_AUDIT 
        add constraint ADDENDUMDETAIL_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;

	alter table pcmsdatadev.JOB_INFO_AUDIT 
        add constraint JOB_INFO_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;

    alter table pcmsdatadev.PAYMENT_CERT_AUDIT 
        add constraint PAYMENT_CERT_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;

    alter table pcmsdatadev.PAYMENT_CERT_DETAIL_AUDIT 
        add constraint PAYMENTCERTDETAIL_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;

	alter table pcmsdatadev.RESOURCE_SUMMARY_AUDIT 
        add constraint RESOURCE_SUMMARY_AUDIT_FK 
        foreign key (REV) 
        references pcmsdatadev.REVISION;

    alter table pcmsdatadev.SUBCONTRACT_AUDIT 
        add constraint SUBCONTRACT_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;

    alter table pcmsdatadev.SUBCONTRACT_DETAIL_AUDIT 
        add constraint SUBCONTRACT_DETAIL_AUDIT_FK
        foreign key (REV) 
        references pcmsdatadev.REVISION;