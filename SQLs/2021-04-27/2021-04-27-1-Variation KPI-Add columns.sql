--------------------------------------------------------
--  Table VARIATION_KPI
--------------------------------------------------------
alter table "PCMSDATADEV"."VARIATION_KPI"
    add EOJ_SECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATADEV"."VARIATION_KPI"
    add EOJ_UNSECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATADEV"."VARIATION_KPI"
    add EOJ_TOTAL NUMBER(19,4) default 0 not null;

--------------------------------------------------------
--  Table VARIATION_KPI_AUDIT
--------------------------------------------------------
alter table "PCMSDATADEV"."VARIATION_KPI_AUDIT"
    add EOJ_SECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATADEV"."VARIATION_KPI_AUDIT"
    add EOJ_UNSECURED NUMBER(19,4) default 0 not null;
alter table "PCMSDATADEV"."VARIATION_KPI_AUDIT"
    add EOJ_TOTAL NUMBER(19,4) default 0 not null;