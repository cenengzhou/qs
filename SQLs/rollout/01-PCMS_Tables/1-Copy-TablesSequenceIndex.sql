--1. Copy from QSDATAUAT to PCMSDATAUAT

-- Total: 58 tables copy from QSDATAUAT to PCMSDATAUAT
-- 47 tables starting with 'QS_*'
-- 11 tables starting with 'QRTZ_*'
select count(1) from SYS.DBA_TABLES where 
OWNER = 'QSDATAUAT' and 
(TABLE_NAME like 'QS%' or TABLE_NAME like 'QRTZ%')
order by TABLE_NAME;

-- Total: 38 sequences copy from QSDATAUAT to PCMSDATAUAT
-- 37 sequences starting with 'QS_*'
-- 1 sequence named 'MAIN_CONTRACT_CERTIFICATE_SEQ'
select count(1)  from SYS.DBA_SEQUENCES where 
SEQUENCE_OWNER = 'QSDATAUAT' and
(SEQUENCE_NAME like 'QS%' or SEQUENCE_NAME = 'MAIN_CONTRACT_CERTIFICATE_SEQ')
order by SEQUENCE_NAME;

-- 34 indexes of tables starting with 'QS_*'
-- 15 indexes of tables starting with 'QRTZ_*'

-- Total: 63 indexes copy from QSDATAUAT to PCMSDATAUAT
-- 48 indexes owned by table name starting with 'QS_*'
-- 15 indexes owned by table name starting with 'QRTZ_*'
select count(1) from SYS.DBA_INDEXES where 
OWNER = 'QSDATAUAT'and 
(TABLE_NAME like 'QS%' or TABLE_NAME like 'QRTZ%')
order by TABLE_NAME; 