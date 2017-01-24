--1. Copy from QSDATAPROD to PCMSDATATST

-- Total: 58 tables copy from QSDATAPROD to PCMSDATATST
-- 47 tables starting with 'QS_*'
-- 11 tables starting with 'QRTZ_*'
select count(1) from SYS.DBA_TABLES where 
OWNER = 'QSDATAPROD' and 
(TABLE_NAME like 'QS%' or TABLE_NAME like 'QRTZ%')
order by TABLE_NAME;

select count(1) from SYS.DBA_TABLES where 
OWNER = 'PCMSDATAPROD' and 
(TABLE_NAME like 'QS%' or TABLE_NAME like 'QRTZ%')
order by TABLE_NAME;



-- Total: 38 sequences copy from QSDATAPROD to PCMSDATATST
-- 37 sequences starting with 'QS_*'
-- 1 sequence named 'MAIN_CONTRACT_CERTIFICATE_SEQ'
select count(1)  from SYS.DBA_SEQUENCES where 
SEQUENCE_OWNER = 'QSDATAPROD' and
(SEQUENCE_NAME like 'QS%' or SEQUENCE_NAME = 'MAIN_CONTRACT_CERTIFICATE_SEQ')
order by SEQUENCE_NAME;


select count(1)  from SYS.DBA_SEQUENCES where 
SEQUENCE_OWNER = 'PCMSDATAPROD' and
(SEQUENCE_NAME like 'QS%' or SEQUENCE_NAME = 'MAIN_CONTRACT_CERTIFICATE_SEQ')
order by SEQUENCE_NAME;


-- 34 indexes of tables starting with 'QS_*'
-- 15 indexes of tables starting with 'QRTZ_*'

-- Total: 63 indexes copy from QSDATAPROD to PCMSDATATST
-- 48 indexes owned by table name starting with 'QS_*'
-- 15 indexes owned by table name starting with 'QRTZ_*'
select count(1) from SYS.DBA_INDEXES where 
OWNER = 'QSDATAPROD'and 
(TABLE_NAME like 'QS%' or TABLE_NAME like 'QRTZ%')
order by TABLE_NAME; 

select count(1) from SYS.DBA_INDEXES where 
OWNER = 'PCMSDATAPROD'and 
(TABLE_NAME like 'QS%' or TABLE_NAME like 'QRTZ%')
order by TABLE_NAME; 