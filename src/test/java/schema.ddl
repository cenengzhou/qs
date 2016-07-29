
    create table ADDENDUM (
        ID number(19,2) not null,
        DATE_CREATED date not null,
        DATE_LAST_MODIFIED date,
        USERNAME_CREATED varchar2(40 char) not null,
        USERNAME_LAST_MODIFIED varchar2(40 char),
        AMT_ADDENDUM number(19,2),
        AMT_ADDENDUM_TOTAL number(19,2),
        AMT_ADDENDUM_TOTAL_TBA number(19,2),
        AMT_SUBCONTRACT_REMEASURED number(19,2),
        AMT_SUBCONTRACT_REVISED number(19,2),
        AMT_SUBCONTRACT_REVISED_TBA number(19,2),
        DATE_APPROVAL date,
        DATE_SUBMISSION date,
        DESCRIPTION_SUBCONTRACT varchar2(510 char),
        NAME_SUBCONTRACTOR varchar2(510 char),
        NO number(19,0) not null,
        NO_JOB varchar2(10 char) not null,
        NO_SUBCONTRACT varchar2(8 char) not null,
        NO_SUBCONTRACTOR varchar2(20 char) not null,
        REMARKS varchar2(2000 char),
        STATUS varchar2(40 char),
        STATUS_APPROVAL varchar2(40 char),
        TITLE varchar2(510 char),
        USERNAME_PREPARED_BY varchar2(40 char),
        ID_SUBCONTRACT number(19,0) not null,
        primary key (ID)
    );

    create table ADDENDUM_DETAIL (
        ID number(19,2) not null,
        DATE_CREATED date not null,
        DATE_LAST_MODIFIED date,
        USERNAME_CREATED varchar2(40 char) not null,
        USERNAME_LAST_MODIFIED varchar2(40 char),
        AMT_ADDENDUM number(19,2),
        AMT_ADDENDUM_TBA number(19,2),
        AMT_BUDGET number(19,2),
        AMT_BUDGET_TBA number(19,2),
        BPI varchar2(510 char),
        CODE_OBJECT varchar2(12 char),
        CODE_SUBSIDIARY varchar2(16 char),
        DESCRIPTION varchar2(4000 char),
        NO number(19,0) not null,
        NO_JOB varchar2(10 char) not null,
        NO_SUBCONTRACT varchar2(8 char) not null,
        QUANTITY number(19,2),
        QUANTITY_TBA number(19,2),
        RATE_ADDENDUM number(19,2),
        RATE_ADDENDUM_TBA number(19,2),
        RATE_BUDGET number(19,2),
        RATE_BUDGET_TBA number(19,2),
        REMARKS varchar2(1000 char),
        TYPE_HD varchar2(20 char) not null,
        TYPE_VO varchar2(4 char) not null,
        UNIT varchar2(20 char),
        ID_ADDENDUM number(19,2) not null,
        primary key (ID)
    );

    create table APP_SUBCONTRACT_STANDARD_TERMS (
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

    create table APP_TRANSIT_RESOURCE_CODE (
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

    create table APP_TRANSIT_UOM (
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

    create table ATTACHMENT (
        ID number(19,2) not null,
        DATE_CREATED date not null,
        DATE_LAST_MODIFIED date,
        USERNAME_CREATED varchar2(40 char) not null,
        USERNAME_LAST_MODIFIED varchar2(40 char),
        ID_TABLE number(19,2) not null,
        NAME_FILE varchar2(200 char),
        NAME_TABLE varchar2(60 char) not null,
        NO_SEQUENCE number(19,2) not null,
        PATH_FILE varchar2(1000 char),
        TEXT varchar2(4000 char),
        TYPE_DOCUMENT varchar2(20 char) not null,
        primary key (ID)
    );

    create table ATTACH_MAIN_CERT (
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
        Main_Cert_ID number(19,0),
        primary key (Main_Cert_ID, SEQUENCENO)
    );

    create table ATTACH_PAYMENT (
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
        Payment_Cert_ID number(19,0),
        primary key (Payment_Cert_ID, sequenceNo)
    );

    create table ATTACH_REPACKAGING (
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
        repackaging_ID number(19,0),
        primary key (repackaging_ID, sequenceNo)
    );

    create table ATTACH_SUBCONTRACT (
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
        Subcontract_ID number(19,0),
        primary key (sequenceNo, Subcontract_ID)
    );

    create table ATTACH_SUBCONTRACT_DETAIL (
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
        Subcontract_Detail_ID number(19,0),
        primary key (sequenceNo, Subcontract_Detail_ID)
    );

    create table BPI_BILL (
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
        Job_Info_ID number(19,0),
        primary key (ID)
    );

    create table BPI_ITEM (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        activityCode varchar2(12 char),
        AMT_BUDGET double precision,
        AMT_SELLING double precision,
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
        Bpi_Page_ID number(19,0),
        primary key (ID)
    );

    create table BPI_ITEM_RESOURCE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        AMT_BUDGET double precision,
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
        Bpi_Item_ID number(19,0),
        primary key (ID)
    );

    create table BPI_PAGE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        description varchar2(255 char),
        pageNo varchar2(20 char),
        Bpi_Bill_ID number(19,0),
        primary key (ID)
    );

    create table DIM_ACCOUNT_MASTER (
        ACCOUNT_CODE_KEY varchar2(32 char) not null,
        ACCOUNT_DESCRIPTION varchar2(120 char),
        BUSINESS_UNIT varchar2(48 char),
        COMPANY_CODE varchar2(20 char),
        FULL_ACCOUNT_CODE varchar2(116 char),
        OBJECT varchar2(24 char),
        POST_EDIT varchar2(244 char),
        POST_EDIT_CODE varchar2(4 char),
        SUBSIDIARY varchar2(32 char),
        primary key (ACCOUNT_CODE_KEY)
    );

    create table DIM_ADDRESS_BOOK (
        ADDRESS_BOOK_NUMBER number(22,0) not null,
        ADDR_BOOK_TYPE varchar2(244 char),
        ADDRESS_BOOK_NAME varchar2(160 char),
        ADDRESS_BOOK_TYPE_CODE varchar2(12 char),
        BUSINESS_REGISTRATION_NUMBER varchar2(80 char),
        BUSINESS_SECTOR varchar2(244 char),
        BUSINESS_SECTOR_CODE varchar2(12 char),
        CLIENT_GROUP varchar2(244 char),
        CLIENT_GROUP_CODE varchar2(12 char),
        HOLD varchar2(244 char),
        HOLD_CODE varchar2(12 char),
        PROPOSED_VENDOR varchar2(244 char),
        PROPOSED_VENDOR_CODE varchar2(12 char),
        SECURITY_BU varchar2(48 char),
        SUBCONTRACTOR_APPROVAL varchar2(244 char),
        SUBCONTRACTOR_APPROVAL_CODE varchar2(12 char),
        SUPPLIER_APPROVAL varchar2(244 char),
        SUPPLIER_APPROVAL_CODE varchar2(12 char),
        VENDOR_APPR_STATUS_CODE varchar2(40 char),
        VENDOR_APPROVAL_STATUS varchar2(244 char),
        VENDOR_STATUS varchar2(244 char),
        VENDOR_STATUS_CODE varchar2(12 char),
        VENDOR_TYPE varchar2(244 char),
        VENDOR_TYPE_CODE varchar2(12 char),
        VMS_EXEMPTED varchar2(244 char),
        VMS_EXEMPTED_CODE varchar2(12 char),
        primary key (ADDRESS_BOOK_NUMBER)
    );

    create table DIM_BUSINESS_UNIT (
        BUSINESS_UNIT_CODE varchar2(48 char) not null,
        AMOUNT_ORIGINAL_CONTRACT double precision,
        AMOUNT_PROJECTED_CONTRACT double precision,
        BALANCE_SHEET_CODE varchar2(12 char),
        BALANCE_SHEET_STATUS varchar2(244 char),
        BOND_REQUIREMENT varchar2(244 char),
        BOND_RQMT_CODE varchar2(40 char),
        BUSINESS_TYPE varchar2(8 char),
        BUSINESS_UNIT_DESCRIPTION varchar2(120 char),
        COMPANY_CODE varchar2(20 char),
        COMPANY_NAME varchar2(120 char),
        CONTRACT_SIGNED_CODE varchar2(12 char),
        CONTRACT_TYPE varchar2(244 char),
        CONTRACT_TYPE_CODE varchar2(16 char),
        CPF_APPLICABLE char(1 char),
        CPF_BASE_PERIOD number(19,0),
        CPF_BASE_YEAR number(19,0),
        CPF_INDEX_NAME varchar2(48 char),
        CURRENCY_CODE varchar2(12 char),
        CURRENCY_DECIMAL varchar2(4 char),
        DATE_ACTUAL_END timestamp,
        DATE_ACTUAL_START timestamp,
        DATE_ANTICIPATED_COMPLETION timestamp,
        DATE_FORECAST_END_PERIOD number(19,0),
        DATE_FORECAST_END_YEAR number(19,0),
        DATE_PLANNED_END timestamp,
        DATE_PLANNED_START timestamp,
        DATE_REVISED_COMPLETION timestamp,
        DATE_YEAR_OF_COMPLETION number(19,0),
        DEPARTMENT varchar2(244 char),
        DEPARTMENT_CODE varchar2(12 char),
        DIVISION varchar2(120 char),
        DIVISION_CODE varchar2(12 char),
        ENTITY_JOB_KEY varchar2(48 char),
        ICUSTOMER_GROUP varchar2(244 char),
        IMMEDIATE_CUSTOMER varchar2(160 char),
        IMMEDIATE_CUSTOMER_NO number(22,0),
        INTERNAL_JOB varchar2(244 char),
        INTERNAL_JOB_CODE varchar2(12 char),
        JOB_TYPE varchar2(244 char),
        LEVY_APPLICABLE char(1 char),
        NUMBER_CLIENT_CONTRACT varchar2(80 char),
        PERCENTAGE_DEFECT_PROVISION double precision,
        PERCENTAGE_JV double precision,
        PERCENTAGE_LEVY_CITA double precision,
        PERCENTAGE_LEVY_PCFB double precision,
        PERCENTAGE_RETENTION_INTERIM double precision,
        PERCENTAGE_RETENTION_MAXIMUM double precision,
        PROFIT_AND_LOSS_STATUS varchar2(244 char),
        PROFIT_LOSS_CODE varchar2(12 char),
        REGION_CODE varchar2(12 char),
        SECTION varchar2(244 char),
        SECTION_CODE varchar2(12 char),
        SOLO_JV varchar2(244 char),
        SOLO_JV_CODE varchar2(12 char),
        SOLO_JV_JO varchar2(244 char),
        SOLO_JV_JO_CODE varchar2(40 char),
        TENDER_GP double precision,
        UCUSTOMER_GROUP varchar2(244 char),
        ULTIMATE_CUSTOMER varchar2(160 char),
        ULTIMATE_CUSTOMER_NO number(22,0),
        primary key (BUSINESS_UNIT_CODE)
    );

    create table FACT_ACCOUNT_LEDGER (
        ACCOUNT_PERIOD number(22,0) not null,
        ACCOUNT_TYPE_LEDGER varchar2(8 char) not null,
        ENTITY_DOC_COMPANY_KEY varchar2(20 char) not null,
        NUMBER_DOCUMENT number(22,0) not null,
        NUMBER_JE_LINE_EXTENSION varchar2(8 char) not null,
        NUMBER_JOURNAL_ENTRY_LINE number(22,0) not null,
        TYPE_DOCUMENT varchar2(8 char) not null,
        ACCOUNT_FISCAL_YEAR number(22,0),
        ACCOUNT_GL varchar2(116 char),
        ACCOUNT_GL_CLASS varchar2(16 char),
        ACCOUNT_KEY varchar2(32 char),
        ACCOUNT_OBJECT varchar2(24 char),
        ACCOUNT_SUB_LEDGER varchar2(32 char),
        ACCOUNT_SUBSIDIARY varchar2(32 char),
        ACCOUNT_TYPE_LEDGER_DESCR varchar2(120 char),
        ACCOUNT_TYPE_SUB_LEDGER varchar2(4 char),
        AMOUNT number(22,0),
        CURRENCY_CODE varchar2(12 char),
        DATE_BATCH timestamp,
        DATE_GL date,
        DATE_INPUT timestamp,
        DATE_INVOICE date,
        DATE_PAYMENT_OR_CHECK timestamp,
        DATE_POSTED date,
        ENTITY_ADDRESS_BOOK_KEY number(22,0),
        ENTITY_BUSINESS_UNIT_KEY varchar2(48 char),
        ENTITY_COMPANY_KEY varchar2(20 char),
        ENTITY_GL_POSTED_BY varchar2(40 char),
        ENTITY_INPUT_BY varchar2(40 char),
        EXPLANATION_ADDRESS_BOOK varchar2(120 char),
        EXPLANATION_REMARK varchar2(120 char),
        ITEM_KEY number(22,0),
        NUMBER_ASSET_IDENTIFICATION varchar2(100 char),
        NUMBER_BATCH number(22,0),
        NUMBER_INVOICE varchar2(100 char),
        NUMBER_PAY_ITEM varchar2(12 char),
        NUMBER_PAYMENT_OR_CHECK varchar2(32 char),
        QUANTITY number(22,0),
        QUANTITY_UNIT_OF_MEASUREMENT varchar2(8 char),
        RECORD_KEY_MATCHED_PO varchar2(112 char),
        RECORD_KEY_PAYMENT_ID number(22,0),
        RECORD_KEY_VOUCHER varchar2(84 char),
        REFERENCE_1 varchar2(32 char),
        REFERENCE_2 varchar2(32 char),
        REFERENCE_3 varchar2(32 char),
        STATUS_FIXED_ASSET_POSTED varchar2(4 char),
        STATUS_POST varchar2(4 char),
        STATUS_POST_DESCRIPTION varchar2(9 char),
        TAX_EXPL_CODE varchar2(8 char),
        TAX_EXPLANATION_DESCRIPTION varchar2(120 char),
        TAX_RATE_CODE varchar2(40 char),
        TYPE_BATCH varchar2(8 char),
        TYPE_BATCH_DESCRIPTION varchar2(244 char),
        TYPE_DOCUMENT_DESCRIPTION varchar2(120 char),
        TYPE_RECONCILIATION varchar2(120 char),
        TYPE_REVERSE_OR_VOID varchar2(7 char),
        primary key (ACCOUNT_PERIOD, ACCOUNT_TYPE_LEDGER, ENTITY_DOC_COMPANY_KEY, NUMBER_DOCUMENT, NUMBER_JE_LINE_EXTENSION, NUMBER_JOURNAL_ENTRY_LINE, TYPE_DOCUMENT)
    );

    create table FACT_ACCT_BAL (
        ACCOUNT_TYPE_LEDGER varchar2(8 char) not null,
        FISCAL_YEAR number(22,0) not null,
        ACCOUNT_OBJECT varchar2(24 char),
        ACCOUNT_SUBSIDIARY varchar2(32 char),
        AMOUNT_ACCUM_PERIOD_01 number(22,0),
        AMOUNT_ACCUM_PERIOD_02 number(22,0),
        AMOUNT_ACCUM_PERIOD_03 number(22,0),
        AMOUNT_ACCUM_PERIOD_04 number(22,0),
        AMOUNT_ACCUM_PERIOD_05 number(22,0),
        AMOUNT_ACCUM_PERIOD_06 number(22,0),
        AMOUNT_ACCUM_PERIOD_07 number(22,0),
        AMOUNT_ACCUM_PERIOD_08 number(22,0),
        AMOUNT_ACCUM_PERIOD_09 number(22,0),
        AMOUNT_ACCUM_PERIOD_10 number(22,0),
        AMOUNT_ACCUM_PERIOD_11 number(22,0),
        AMOUNT_ACCUM_PERIOD_12 number(22,0),
        AMOUNT_ACCUM_PERIOD_13 number(22,0),
        AMOUNT_ACCUM_PERIOD_14 number(22,0),
        AMOUNT_BEGINNING_BALANCE number(22,0),
        AMOUNT_PERIOD_01 number(22,0),
        AMOUNT_PERIOD_02 number(22,0),
        AMOUNT_PERIOD_03 number(22,0),
        AMOUNT_PERIOD_04 number(22,0),
        AMOUNT_PERIOD_05 number(22,0),
        AMOUNT_PERIOD_06 number(22,0),
        AMOUNT_PERIOD_07 number(22,0),
        AMOUNT_PERIOD_08 number(22,0),
        AMOUNT_PERIOD_09 number(22,0),
        AMOUNT_PERIOD_10 number(22,0),
        AMOUNT_PERIOD_11 number(22,0),
        AMOUNT_PERIOD_12 number(22,0),
        AMOUNT_PERIOD_13 number(22,0),
        AMOUNT_PERIOD_14 number(22,0),
        AMOUNT_PRIOR_YEAR_NET_POST number(22,0),
        CURRENCY_LOCAL varchar2(12 char),
        ENTITY_BUSINESS_UNIT_KEY varchar2(48 char),
        ENTITY_COMPANY_KEY varchar2(20 char),
        ACCOUNT_CODE_KEY varchar2(32 char) not null,
        primary key (ACCOUNT_CODE_KEY, ACCOUNT_TYPE_LEDGER, FISCAL_YEAR)
    );

    create table FACT_APPR_INSTANCE_DETAIL (
        AMOUNT_DLOA double precision not null,
        APPROVER_ACTION varchar2(30 char) not null,
        APPROVER_GROUP varchar2(200 char) not null,
        ENTITY_APPROVER_ID number(19,0) not null,
        NUMBER_APPR_SEQ number(19,0) not null,
        NUMBER_APPR_SUB_SEQ number(19,2) not null,
        RECORD_KEY_DELEGATION number(19,2) not null,
        TYPE_APPROVAL varchar2(40 char) not null,
        TYPE_DOCUMENT varchar2(200 char) not null,
        TYPE_SUB_APPROVAL varchar2(200 char) not null,
        AMOUNT_LOCAL_CURRENCY number(22,0),
        AND_OR varchar2(3 char),
        APPROVER_COMMENT varchar2(1000 char),
        APPROVER_GROUP_DESCRIPTION varchar2(1000 char),
        APPROVER_NAME varchar2(1020 char),
        CURRENCY_LOCAL varchar2(12 char),
        DATE_APPROVED varchar2(29 char),
        DATE_CREATED varchar2(29 char),
        ENTITY_BUSINESS_UNIT_KEY varchar2(48 char),
        ENTITY_COMPANY_KEY varchar2(20 char),
        ESCALATION varchar2(1 char),
        IS_DELEGATED char(1 char),
        NUMBER_DOCUMENT number(19,0),
        PREREQUISITE varchar2(1 char),
        RECORD_KEY_DOCUMENT varchar2(1968 char),
        STATUS_APPROVAL varchar2(200 char),
        TYPE_APPROVAL_DESCRIPTION varchar2(40 char),
        TYPE_DOCUMENT_DESCR varchar2(1000 char),
        RECORD_KEY_INSTANCE number(19,2) not null,
        primary key (AMOUNT_DLOA, APPROVER_ACTION, APPROVER_GROUP, ENTITY_APPROVER_ID, NUMBER_APPR_SEQ, NUMBER_APPR_SUB_SEQ, RECORD_KEY_DELEGATION, RECORD_KEY_INSTANCE, TYPE_APPROVAL, TYPE_DOCUMENT, TYPE_SUB_APPROVAL)
    );

    create table FACT_APPR_INSTANCE_HEADER (
        RECORD_KEY_INSTANCE number(19,2) not null,
        AMOUNT_IN_FOREIGN_CURR double precision not null,
        AMOUNT_IN_LOCAL_CURR number(22,0),
        APPROVAL_ROUTE_VENDOR varchar2(200 char) not null,
        APPROVAL_ROUTE_VERSION number(19,0) not null,
        CURRENCY_DOCUMENT varchar2(12 char),
        CURRENY_LOCAL varchar2(12 char),
        DATE_CREATED varchar2(29 char),
        DATE_MODIFIED varchar2(29 char),
        ENTITY_BUSINESS_UNIT_KEY varchar2(48 char),
        ENTITY_VENDOR_ID number(19,0),
        RECORD_KEY_DOCUMENT varchar2(1472 char),
        STATUS_APPROVAL varchar2(200 char),
        STATUS_APPROVAL_CODE varchar2(4 char),
        TYPE_APPROVAL_CATEGORY varchar2(40 char) not null,
        TYPE_DOCUMENT varchar2(200 char) not null,
        primary key (RECORD_KEY_INSTANCE)
    );

    create table IV_POSTING_HIST (
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

    create table JDE_ACCOUNT_LEDGER (
        ID number(19,0) not null,
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
        primary key (ID)
    );

    create table JOB_INFO (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
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
        primary key (ID)
    );

    create table MAIN_CERT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ACTUAL_RECEIPT_DATE date,
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
        CERT_AS_AT_DATE date,
        certDueDate date,
        CERT_ISSUE_DATE date,
        certStatuschangeDate date,
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
        IPASENTOUTDATE date,
        IPA_SUBMISSION_DATE date,
        JOBNO varchar2(12 char),
        REMARK varchar2(255 char),
        TOTAL_RECEIPT_AMOUNT double precision,
        primary key (ID)
    );

    create table MAIN_CERT_CONTRA_CHARGE (
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
        MAIN_CERT_ID number(19,0),
        primary key (ID)
    );

    create table MAIN_CERT_RETENTION_RELEASE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ACTRELEASEAMT double precision,
        contractualDueDate date,
        dueDate date,
        FSTRELEASEAMT double precision,
        jobNo varchar2(12 char) not null,
        mainCertNo number(10,0),
        RELEASEPERCENT double precision,
        sequenceNo number(10,0) not null,
        STATUS varchar2(3 char),
        primary key (ID)
    );

    create table PAYMENT_CERT (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
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
        primary key (ID)
    );

    create table PAYMENT_CERT_DETAIL (
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
        Payment_Cert_ID number(19,0),
        SUBCONTRACT_DETAIL_ID number(19,0),
        primary key (Payment_Cert_ID, scSeqNo)
    );

    create table PROVISION_POSTING_HIST (
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
        Subcontract_Detail_ID number(19,0),
        primary key (postedMonth, postedYr, Subcontract_Detail_ID)
    );

    create table QRTZ_CRON_TRIGGERS (
        SCHED_NAME varchar2(120 char) not null,
        TRIGGER_GROUP varchar2(200 char) not null,
        TRIGGER_NAME varchar2(200 char) not null,
        CRON_EXPRESSION varchar2(120 char),
        primary key (SCHED_NAME, TRIGGER_GROUP, TRIGGER_NAME)
    );

    create table QRTZ_TRIGGERS (
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

    create table REPACKAGING (
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
        Job_Info_ID number(19,0),
        primary key (ID)
    );

    create table REPACKAGING_DETAIL (
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
        Repackaging_ID number(19,0),
        primary key (ID)
    );

    create table RESOURCE_SUMMARY (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
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
        primary key (ID)
    );

    create table RESOURCE_SUMMARY_AUDIT_CUSTOM (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        ACTION_TYPE varchar2(20 char),
        dataType varchar2(20 char),
        REPACKAGING_ID number(19,0),
        RESOURCE_SUMMARY_ID number(19,0),
        VALUE_FROM varchar2(255 char),
        VALUE_TO varchar2(255 char),
        VALUE_TYPE varchar2(20 char),
        primary key (ID)
    );

    create table REVISION (
        ID number(10,0) not null,
        timestamp number(19,0) not null,
        username varchar2(255 char),
        primary key (ID)
    );

    create table RPT_IV_ACCT_BALANCE (
        ACCOUNT_PERIOD number(19,2) not null,
        FISCAL_YEAR number(22,0) not null,
        AA_AMOUNT_ACCUM number(22,0),
        AA_AMOUNT_PERIOD number(22,0),
        ACCOUNT_DESCRIPTION varchar2(120 char),
        ACCOUNT_OBJECT varchar2(24 char),
        ACCOUNT_SUBSIDIARY varchar2(32 char),
        CURRENCY_LOCAL varchar2(12 char),
        ENTITY_BUSINESS_UNIT_KEY varchar2(48 char),
        JI_AMOUNT_ACCUM number(22,0),
        JI_AMOUNT_PERIOD number(22,0),
        ACCOUNT_CODE_KEY varchar2(32 char),
        primary key (ACCOUNT_CODE_KEY, ACCOUNT_PERIOD, FISCAL_YEAR)
    );

    create table RPT_IV_ACCT_BALANCE_SL (
        ACCOUNT_PERIOD number(19,2) not null,
        ACCOUNT_SUB_LEDGER varchar2(32 char) not null,
        ACCOUNT_TYPE_SUB_LEDGER varchar2(4 char) not null,
        FISCAL_YEAR number(22,0) not null,
        AA_AMOUNT_ACCUM number(22,0),
        AA_AMOUNT_PERIOD number(22,0),
        ACCOUNT_DESCRIPTION varchar2(120 char),
        ACCOUNT_OBJECT varchar2(24 char),
        ACCOUNT_SUBSIDIARY varchar2(32 char),
        CURRENCY_LOCAL varchar2(12 char),
        ENTITY_BUSINESS_UNIT_KEY varchar2(48 char),
        JI_AMOUNT_ACCUM number(22,0),
        JI_AMOUNT_PERIOD number(22,0),
        ACCOUNT_CODE_KEY varchar2(32 char),
        primary key (ACCOUNT_CODE_KEY, ACCOUNT_PERIOD, ACCOUNT_SUB_LEDGER, ACCOUNT_TYPE_SUB_LEDGER, FISCAL_YEAR)
    );

    create table SUBCONTRACT (
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
        primary key (ID)
    );

    create table SUBCONTRACT_DETAIL (
        TYPE varchar2(2 char) not null,
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
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
        cumWDQty double precision,
        postedWDQty double precision,
        costRate double precision,
        toBeApprovedQty double precision,
        altObjectCode varchar2(6 char),
        contraChargeSCNo varchar2(10 char),
        corrSCLineSeqNo number(19,0),
        toBeApprovedRate double precision,
        SUBCONTRACT_ID number(19,0),
        primary key (ID)
    );

    create table SUBCONTRACT_SNAPSHOT (
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
        Job_Info_ID number(19,0),
        Subcontract_ID number(19,0),
        primary key (ID)
    );

    create table SUBCONTRACT_WORKSCOPE (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        workScope varchar2(10 char),
        Subcontract_ID number(19,0),
        primary key (ID)
    );

    create table TENDER (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        AMT_BUYING_GAIN_LOSS number(19,2),
        budgetAmount double precision,
        currencyCode varchar2(10 char),
        DATE_PREPARED date,
        exchangeRate double precision,
        jobNo varchar2(12 char),
        NAME_SUBCONTRACTOR varchar2(20 char),
        packageNo varchar2(10 char),
        remarks varchar2(500 char),
        status varchar2(10 char),
        STATUS_CHANGE_EXECUTION_OF_SC varchar2(10 char),
        USERNAME_PREPARED varchar2(20 char),
        vendorNo number(10,0),
        Subcontract_ID number(19,0),
        primary key (ID)
    );

    create table TENDER_DETAIL (
        ID number(19,0) not null,
        CREATED_DATE date not null,
        CREATED_USER varchar2(255 char) not null,
        LAST_MODIFIED_DATE date,
        LAST_MODIFIED_USER varchar2(255 char),
        SYSTEM_STATUS varchar2(255 char),
        AMT_BUDGET double precision,
        AMT_FOREIGN double precision,
        AMT_SUBCONTRACT double precision,
        billItem varchar2(110 char),
        description varchar2(255 char),
        lineType varchar2(10 char),
        objectCode varchar2(6 char),
        quantity double precision,
        RATE_BUDGET double precision,
        RATE_SUBCONTRACT double precision,
        remark varchar2(255 char),
        resourceNo number(10,0),
        sequenceNo number(10,0),
        subsidiaryCode varchar2(8 char),
        unit varchar2(10 char),
        Tender_ID number(19,0),
        primary key (ID)
    );

    create table TENDER_VARIANCE (
        ID number(19,2) not null,
        DATE_CREATED date not null,
        DATE_LAST_MODIFIED date,
        USERNAME_CREATED varchar2(40 char) not null,
        USERNAME_LAST_MODIFIED varchar2(40 char),
        CLAUSE varchar2(1000 char),
        GENERAL_CONDITION varchar2(4000 char),
        NAME_SUBCONTRACTOR varchar2(510 char),
        NO_JOB varchar2(10 char) not null,
        NO_SUBCONTRACT varchar2(8 char) not null,
        NO_SUBCONTRACTOR varchar2(20 char) not null,
        PROPOSED_VARIANCE varchar2(4000 char),
        REASON varchar2(4000 char),
        ID_TENDER number(19,0) not null,
        primary key (ID)
    );

    create table TRANSIT (
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

    create table TRANSIT_BPI (
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
        Transit_ID number(19,0),
        primary key (ID)
    );

    create table TRANSIT_RESOURCE (
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
        Transit_Bpi_ID number(19,0),
        primary key (ID)
    );

    create table V_CONTRACT_RECEIVABLE_RPT (
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

    alter table REPACKAGING 
        add constraint UK_20ddai7ofk7rp80jjrs9clnxn unique (REPACKAGINGVERSION);

    alter table REPACKAGING 
        add constraint UK_st38nd88u0vy6h09g50ey4en5 unique (Job_Info_ID);

    alter table ADDENDUM 
        add constraint FK_em1556ls3xyf6ltu3a2f0p7m2 
        foreign key (ID_SUBCONTRACT) 
        references SUBCONTRACT;

    alter table ADDENDUM_DETAIL 
        add constraint FK_55qfrjsbbf6gq8yku552da593 
        foreign key (ID_ADDENDUM) 
        references ADDENDUM;

    alter table ATTACH_MAIN_CERT 
        add constraint FK_AttachMainCert_MainCert_PK 
        foreign key (Main_Cert_ID) 
        references MAIN_CERT;

    alter table ATTACH_PAYMENT 
        add constraint FK_AttachPayment_PaymentCert_PK 
        foreign key (Payment_Cert_ID) 
        references PAYMENT_CERT;

    alter table ATTACH_REPACKAGING 
        add constraint FK_AttachRepackaging_Repackaging_PK 
        foreign key (repackaging_ID) 
        references REPACKAGING;

    alter table ATTACH_SUBCONTRACT 
        add constraint FK_AttachSubcontract_Subcontract_PK 
        foreign key (Subcontract_ID) 
        references SUBCONTRACT;

    alter table ATTACH_SUBCONTRACT_DETAIL 
        add constraint FK_AttachSubcontractDetail_SubcontractDetail_PK 
        foreign key (Subcontract_Detail_ID) 
        references SUBCONTRACT_DETAIL;

    alter table BPI_BILL 
        add constraint FK_BpiBill_JobInfo_PK 
        foreign key (Job_Info_ID) 
        references JOB_INFO;

    alter table BPI_ITEM 
        add constraint FK_BpiItem_BpiPage_PK 
        foreign key (Bpi_Page_ID) 
        references BPI_PAGE;

    alter table BPI_ITEM_RESOURCE 
        add constraint FK_BpiItemResource_BpiItem_PK 
        foreign key (Bpi_Item_ID) 
        references BPI_ITEM;

    alter table BPI_PAGE 
        add constraint FK_BpiPage_BpiBill_PK 
        foreign key (Bpi_Bill_ID) 
        references BPI_BILL;

    alter table FACT_ACCT_BAL 
        add constraint FK_gbvvv5hpkomwa4oxoouo3u301 
        foreign key (ACCOUNT_CODE_KEY) 
        references DIM_ACCOUNT_MASTER;

    alter table FACT_APPR_INSTANCE_DETAIL 
        add constraint FK_avtgifs1qop7g40wy6wjkljfq 
        foreign key (RECORD_KEY_INSTANCE) 
        references FACT_APPR_INSTANCE_HEADER;

    alter table MAIN_CERT_CONTRA_CHARGE 
        add constraint FK_MainCertContraCharge_MainCert_PK 
        foreign key (MAIN_CERT_ID) 
        references MAIN_CERT;

    alter table PAYMENT_CERT 
        add constraint FK_Payment_Cert_Subcontract_PK 
        foreign key (SUBCONTRACT_ID) 
        references SUBCONTRACT;

    alter table PAYMENT_CERT_DETAIL 
        add constraint FK_PaymentCertDetail_PaymentCert_PK 
        foreign key (Payment_Cert_ID) 
        references PAYMENT_CERT;

    alter table PAYMENT_CERT_DETAIL 
        add constraint FK_3s0psgb5miy7wrd64cs4mfdtd 
        foreign key (SUBCONTRACT_DETAIL_ID) 
        references SUBCONTRACT_DETAIL;

    alter table PROVISION_POSTING_HIST 
        add constraint FK_ProvisionPostingHist_SubcontractDetail_PK 
        foreign key (Subcontract_Detail_ID) 
        references SUBCONTRACT_DETAIL;

    alter table REPACKAGING 
        add constraint FK_Repackaging_JobInfo_PK 
        foreign key (Job_Info_ID) 
        references JOB_INFO;

    alter table REPACKAGING_DETAIL 
        add constraint FK_RepackagingDetail_Repackaging_PK 
        foreign key (Repackaging_ID) 
        references REPACKAGING;

    alter table RESOURCE_SUMMARY 
        add constraint FK_ResourceSummary_JobInfo_PK 
        foreign key (Job_Info_ID) 
        references JOB_INFO;

    alter table RESOURCE_SUMMARY 
        add constraint FK_BQRS_MT 
        foreign key (MERGE_TO_ID) 
        references RESOURCE_SUMMARY;

    alter table RESOURCE_SUMMARY 
        add constraint FK_BQRS_SF 
        foreign key (SPLIT_FROM_ID) 
        references RESOURCE_SUMMARY;

    alter table RPT_IV_ACCT_BALANCE 
        add constraint FK_ro51kt8hnye7gxly2dlnba9ga 
        foreign key (ACCOUNT_CODE_KEY) 
        references DIM_ACCOUNT_MASTER;

    alter table RPT_IV_ACCT_BALANCE_SL 
        add constraint FK_drmdet47bl6okql1d7jpaa9aw 
        foreign key (ACCOUNT_CODE_KEY) 
        references DIM_ACCOUNT_MASTER;

    alter table SUBCONTRACT 
        add constraint FK_Subcontract_JobInfo_PK 
        foreign key (Job_Info_ID) 
        references JOB_INFO;

    alter table SUBCONTRACT_DETAIL 
        add constraint FK_SubcontractDetail_Subcontract_PK 
        foreign key (SUBCONTRACT_ID) 
        references SUBCONTRACT;

    alter table SUBCONTRACT_SNAPSHOT 
        add constraint FK_SubcontractSnapshot_JobInfo_PK 
        foreign key (Job_Info_ID) 
        references JOB_INFO;

    alter table SUBCONTRACT_SNAPSHOT 
        add constraint FK_SubcontractSnapshot_Subcontract_PK 
        foreign key (Subcontract_ID) 
        references SUBCONTRACT;

    alter table SUBCONTRACT_WORKSCOPE 
        add constraint FK_SubcontractWorkScope_Subcontract_PK 
        foreign key (Subcontract_ID) 
        references SUBCONTRACT;

    alter table TENDER 
        add constraint FK_Tender_Subcontract_PK 
        foreign key (Subcontract_ID) 
        references SUBCONTRACT;

    alter table TENDER_DETAIL 
        add constraint FK_TenderDetail_Tender_PK 
        foreign key (Tender_ID) 
        references TENDER;

    alter table TENDER_VARIANCE 
        add constraint FK_lvai9r2u17qohg4jy5pdvpmp5 
        foreign key (ID_TENDER) 
        references TENDER;

    alter table TRANSIT_BPI 
        add constraint FK_TransitBpi_Transit_PK 
        foreign key (Transit_ID) 
        references TRANSIT;

    alter table TRANSIT_RESOURCE 
        add constraint FK_TransitResource_TransitBpi_PK 
        foreign key (Transit_Bpi_ID) 
        references TRANSIT_BPI;

    create sequence ADDENDUM_DETAIL_SEQ;

    create sequence ADDENDUM_SEQ;

    create sequence APP_TRANSIT_RESOURCE_CODE_SEQ;

    create sequence APP_TRANSIT_UOM_SEQ;

    create sequence ATTACHMENT_SEQ;

    create sequence BPI_BILL_SEQ;

    create sequence BPI_ITEM_RESOURCE_SEQ;

    create sequence BPI_ITEM_SEQ;

    create sequence BPI_PAGE_SEQ;

    create sequence IV_POSTING_HIST_SEQ;

    create sequence JDE_ACCOUNT_LEDGER_SEQ;

    create sequence JOB_INFO_SEQ;

    create sequence MAIN_CERT_CONTRA_CHARGE_SEQ;

    create sequence MAIN_CERT_RETENTION_RELEASE_SEQ;

    create sequence MAIN_CERT_SEQ;

    create sequence PAYMENT_CERT_SEQ;

    create sequence REPACKAGING_DETAIL_SEQ;

    create sequence REPACKAGING_SEQ;

    create sequence RESOURCE_SUMMARY_AUDIT_CUS_SEQ;

    create sequence RESOURCE_SUMMARY_SEQ;

    create sequence REVISION_SEQ;

    create sequence SUBCONTRACT_DETAIL_SEQ;

    create sequence SUBCONTRACT_SEQ;

    create sequence SUBCONTRACT_SNAPSHOT_SEQ;

    create sequence SUBCONTRACT_WORKSCOPE_SEQ;

    create sequence TENDER_DETAIL_SEQ;

    create sequence TENDER_SEQ;

    create sequence TENDER_VARIANCE_SEQ;

    create sequence TRANSIT_BPI_SEQ;

    create sequence TRANSIT_RESOURCE_SEQ;

    create sequence TRANSIT_SEQ;

    create sequence hibernate_sequence;
