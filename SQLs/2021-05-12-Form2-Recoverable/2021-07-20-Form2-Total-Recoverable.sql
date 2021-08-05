-- Add Total Recoverable and Non Recoverable Amount

alter table SUBCONTRACT
   add TOTAL_RECOVERABLE_AMOUNT NUMBER(19,2) default 0.0
/

alter table SUBCONTRACT
   add TOTAL_NON_RECOVERABLE_AMOUNT NUMBER(19,2) default 0.0
/

alter table SUBCONTRACT_AUDIT
    add TOTAL_RECOVERABLE_AMOUNT NUMBER(19,2) default 0.0
/

alter table SUBCONTRACT_AUDIT
    add TOTAL_NON_RECOVERABLE_AMOUNT NUMBER(19,2) default 0.0
/
alter table ADDENDUM
   add RECOVERABLE_AMOUNT NUMBER(19,2) default 0
/

alter table ADDENDUM
   add NON_RECOVERABLE_AMOUNT NUMBER(19,2) default 0
/

alter table ADDENDUM_AUDIT
    add RECOVERABLE_AMOUNT NUMBER(19,2) default 0
/

alter table ADDENDUM_AUDIT
    add NON_RECOVERABLE_AMOUNT NUMBER(19,2) default 0
/

