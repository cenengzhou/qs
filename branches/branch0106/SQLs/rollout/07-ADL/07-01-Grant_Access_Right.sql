-- Tables
-- select owner, table_name from all_tables where OWNER = 'ADLPRD';
-- select count(1) from all_tables where OWNER = 'ADLPRD';
-- 12 tables
grant select on ADLPRD.DIM_ACCOUNT_MASTER to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_CURRENCY to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_ITEM_MASTER_BY_BU to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_ITEM_MASTER to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_PAYEE_MASTER to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_PAYMENT_TERM to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_ADDRESS_BOOK to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_CALENDAR to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_CALENDAR_AND_FN_PERIOD to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_COMPANY to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_APPROVAL_ROUTE to PCMSUSER_ROLE;
grant select on ADLPRD.DIM_BUSINESS_UNIT to PCMSUSER_ROLE;

-- Views
-- select owner, view_name from all_views where OWNER = 'ADLPRD';
-- select count(1) from all_views where OWNER = 'ADLPRD';
-- 11 views
grant select on ADLPRD.FACT_ACCT_BAL_SL_UNPIVOT to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_ACCT_BAL_UNPIVOT to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_APPR_INSTANCE_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_APPR_INSTANCE_HEADER to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_AP_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_AP_PAYMENT_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRD.RPT_IV_ACCT_BALANCE to PCMSUSER_ROLE;
grant select on ADLPRD.RPT_IV_ACCT_BALANCE_SL to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_ACCOUNT_LEDGER to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_AR_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRD.FACT_AR_RECEIPT_DETAIL to PCMSUSER_ROLE;
