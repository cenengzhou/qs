-- Tables
-- select owner, table_name from all_tables where OWNER = 'ADLPRE';
-- select count(1) from all_tables where OWNER = 'ADLPRE';
-- 12 tables
grant select on ADLPRE.DIM_ACCOUNT_MASTER to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_CURRENCY to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_ITEM_MASTER_BY_BU to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_ITEM_MASTER to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_PAYEE_MASTER to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_PAYMENT_TERM to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_ADDRESS_BOOK to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_CALENDAR to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_CALENDAR_AND_FN_PERIOD to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_COMPANY to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_APPROVAL_ROUTE to PCMSUSER_ROLE;
grant select on ADLPRE.DIM_BUSINESS_UNIT to PCMSUSER_ROLE;

-- Views
-- select owner, view_name from all_views where OWNER = 'ADLPRE';
-- select count(1) from all_views where OWNER = 'ADLPRE';
-- 11 views
grant select on ADLPRE.FACT_ACCT_BAL_SL_UNPIVOT to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_ACCT_BAL_UNPIVOT to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_APPR_INSTANCE_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_APPR_INSTANCE_HEADER to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_AP_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_AP_PAYMENT_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRE.RPT_IV_ACCT_BALANCE to PCMSUSER_ROLE;
grant select on ADLPRE.RPT_IV_ACCT_BALANCE_SL to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_ACCOUNT_LEDGER to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_AR_DETAIL to PCMSUSER_ROLE;
grant select on ADLPRE.FACT_AR_RECEIPT_DETAIL to PCMSUSER_ROLE;
