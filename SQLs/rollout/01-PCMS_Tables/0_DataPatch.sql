-- 5 records are found
--select * from QSDATAUAT.QS_RETENTIONREL where
--JOBNO in ('13259', '13268', '13421') and
--(RELEASEPERCENT = -binary_double_infinity or RELEASEPERCENT = binary_double_infinity);
-- 1 column of 5 records are updated
update QSDATAUAT.QS_RETENTIONREL set RELEASEPERCENT = 100 where
JOBNO in ('13259', '13268', '13421') and
(RELEASEPERCENT = -binary_double_infinity or RELEASEPERCENT = binary_double_infinity);

-- 5 records are found
--select * from QSDATAPROD.QS_JOB where 
--JOBNO in ('13318','13282','13238','13388','13428') and
--REPACKAGINGTYPE in (2, 3);
-- 1 column of 5 records are updated
update QSDATAPROD.QS_JOB set REPACKAGINGTYPE = 1 where 
JOBNO in ('13318','13282','13238','13388','13428') and
REPACKAGINGTYPE in (2, 3);