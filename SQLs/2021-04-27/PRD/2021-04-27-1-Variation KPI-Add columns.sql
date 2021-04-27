--------------------------------------------------------
--  Table VARIATION_KPI
--------------------------------------------------------
alter table "PCMSDATAPROD"."VARIATION_KPI"
    add EOJ_SECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATAPROD"."VARIATION_KPI"
    add EOJ_UNSECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATAPROD"."VARIATION_KPI"
    add EOJ_TOTAL NUMBER(19,4) default 0 not null;
alter table "PCMSDATAPROD"."VARIATION_KPI"
    add EXCEPTION_COMMENT VARCHAR2(255);

--------------------------------------------------------
--  Table VARIATION_KPI_AUDIT
--------------------------------------------------------
alter table "PCMSDATAPROD"."VARIATION_KPI_AUDIT"
    add EOJ_SECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATAPROD"."VARIATION_KPI_AUDIT"
    add EOJ_UNSECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATAPROD"."VARIATION_KPI_AUDIT"
    add EOJ_TOTAL NUMBER(19,4) default 0 not null;
alter table "PCMSDATAPROD"."VARIATION_KPI_AUDIT"
    add EXCEPTION_COMMENT VARCHAR2(255);
