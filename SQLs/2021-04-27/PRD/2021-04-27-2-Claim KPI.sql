--------------------------------------------------------
--  Sequence CLAIM_KPI_SEQ
--------------------------------------------------------
CREATE SEQUENCE "PCMSDATAPROD"."CLAIM_KPI_SEQ" MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 NOCACHE  NOORDER  NOCYCLE;
--------------------------------------------------------
--  Table CLAIM_KPI
--------------------------------------------------------
CREATE TABLE "PCMSDATAPROD"."CLAIM_KPI"
(
    "ID"                     NUMBER(19,0) NOT NULL ENABLE,
    "NO_JOB"                 VARCHAR2(5 CHAR) NOT NULL ENABLE,
    "YEAR"                   NUMBER(4,0) NOT NULL ENABLE,
    "MONTH"                  NUMBER(2,0) NOT NULL ENABLE,
    "NUMBER_ISSUED"          NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_ISSUED"          NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_SUBMITTED"       NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_SUBMITTED"       NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_ASSESSED"        NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_ASSESSED"        NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_APPLIED"         NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_APPLIED"         NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_CERTIFIED"       NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_CERTIFIED"       NUMBER(19,4) NOT NULL ENABLE,
    "REMARKS"                VARCHAR2(4000 CHAR),
    "USERNAME_CREATED"       VARCHAR2(20 CHAR) NOT NULL ENABLE,
    "DATE_CREATED"           DATE NOT NULL ENABLE,
    "USERNAME_LAST_MODIFIED" VARCHAR2(20 CHAR),
    "DATE_LAST_MODIFIED"     DATE,
    "NUMBER_AGREED"          NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_AGREED"          NUMBER(19,4) NOT NULL ENABLE,
    "EOJ_SECURED"            NUMBER(19,4) NOT NULL ENABLE,
    "EOJ_UNSECURED"          NUMBER(19,4) NOT NULL ENABLE,
    "EOJ_TOTAL"              NUMBER(19,4) NOT NULL ENABLE
);
--------------------------------------------------------
--  Table CLAIM_KPI_AUDIT
--------------------------------------------------------
CREATE TABLE "PCMSDATAPROD"."CLAIM_KPI_AUDIT"
(
    "REV"                    NUMBER(10,0) NOT NULL ENABLE,
    "REVTYPE"                NUMBER(3,0) NOT NULL ENABLE,
    "ID"                     NUMBER(19,0) NOT NULL ENABLE,
    "NO_JOB"                 VARCHAR2(5 BYTE) NOT NULL ENABLE,
    "YEAR"                   NUMBER(4,0) NOT NULL ENABLE,
    "MONTH"                  NUMBER(2,0) NOT NULL ENABLE,
    "NUMBER_ISSUED"          NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_ISSUED"          NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_SUBMITTED"       NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_SUBMITTED"       NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_ASSESSED"        NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_ASSESSED"        NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_APPLIED"         NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_APPLIED"         NUMBER(19,4) NOT NULL ENABLE,
    "NUMBER_CERTIFIED"       NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_CERTIFIED"       NUMBER(19,4) NOT NULL ENABLE,
    "REMARKS"                VARCHAR2(4000 BYTE),
    "USERNAME_CREATED"       VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "DATE_CREATED"           DATE NOT NULL ENABLE,
    "USERNAME_LAST_MODIFIED" VARCHAR2(20 BYTE),
    "DATE_LAST_MODIFIED"     DATE,
    "NUMBER_AGREED"          NUMBER(19,0) NOT NULL ENABLE,
    "AMOUNT_AGREED"          NUMBER(19,4) NOT NULL ENABLE,
    "EOJ_SECURED"            NUMBER(19,4) NOT NULL ENABLE,
    "EOJ_UNSECURED"          NUMBER(19,4) NOT NULL ENABLE,
    "EOJ_TOTAL"              NUMBER(19,4) NOT NULL ENABLE
);

--  Index CLAIM_KPI_UN
--------------------------------------------------------
CREATE
UNIQUE INDEX "PCMSDATAPROD"."CLAIM_KPI_UN" ON "PCMSDATAPROD"."CLAIM_KPI" ("YEAR", "MONTH", "NO_JOB") ;
--------------------------------------------------------
--  Index CLAIM_KPI_PK
--------------------------------------------------------
  CREATE
UNIQUE INDEX "PCMSDATAPROD"."CLAIM_KPI_PK" ON "PCMSDATAPROD"."CLAIM_KPI" ("ID") ;
--------------------------------------------------------
--------------------------------------------------------
--  Constraints for Table CLAIM_KPI
--------------------------------------------------------
ALTER TABLE "PCMSDATAPROD"."CLAIM_KPI"
    ADD CONSTRAINT "CLAIM_KPI_UN" UNIQUE ("YEAR", "MONTH", "NO_JOB") ENABLE;
ALTER TABLE "PCMSDATAPROD"."CLAIM_KPI"
    ADD CONSTRAINT "CLAIM_KPI_PK" PRIMARY KEY ("ID") ENABLE;

--------------------------------------------------------
--  Grant
--------------------------------------------------------
grant select on "PCMSDATAPROD"."CLAIM_KPI_SEQ" to PCMSUSER_ROLE;
grant select, insert, update, delete on "PCMSDATAPROD"."CLAIM_KPI" to "PCMSUSER_ROLE";
grant select, insert, update, delete on "PCMSDATAPROD"."CLAIM_KPI_AUDIT" to "PCMSUSER_ROLE";

grant select on "PCMSDATAPROD"."CLAIM_KPI" to IMS_SUPPORT_ROLE_QS;
grant select on "PCMSDATAPROD"."CLAIM_KPI_AUDIT" to IMS_SUPPORT_ROLE_QS;


